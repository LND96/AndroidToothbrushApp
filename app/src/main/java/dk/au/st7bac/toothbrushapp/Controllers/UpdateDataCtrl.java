package dk.au.st7bac.toothbrushapp.Controllers;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.DataProcessor;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;
import dk.au.st7bac.toothbrushapp.Repositories.DbRepo;
import dk.au.st7bac.toothbrushapp.Services.NotificationHelper;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

// settings https://www.youtube.com/watch?v=B155JJwHB3c
public class UpdateDataCtrl implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static UpdateDataCtrl updateDataCtrl;

    private DataProcessor dataProcessor;

    private MutableLiveData<TbStatus> tbStatusLiveData;

    private ApiRepo apiRepo;
    private final DbRepo dbRepo;

    private long lowerEpochIntervalLimit;
    private long higherEpochIntervalLimit;

    private final ExecutorService executor; // for asynch processing

    private NotificationHelper notificationHelper;

    private String methodSender;

    private SharedPreferences sharedPreferences;

    private int numTbMissing;

    private int tbEachDay;


    // singleton pattern
    public static UpdateDataCtrl getInstance() {
        if (updateDataCtrl == null) {
            updateDataCtrl = new UpdateDataCtrl();
        }
        return updateDataCtrl;
    }

    // private constructor
    private UpdateDataCtrl() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
        tbStatusLiveData = new MutableLiveData<>();

        notificationHelper = new NotificationHelper(ToothbrushApp.getAppContext());

        // register on shared preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this); // skal den unregisteres igen?
    }

    public void setDataProcessor(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public void setApiRepo(ApiRepo apiRepo) {
        this.apiRepo = apiRepo;
    }

    public void setNumTbMissing(int numTbMissing) {
        this.numTbMissing = numTbMissing;
    }

    public void setEpochLimits(long lowerEpochIntervalLimit, long higherEpochIntervalLimit) {
        this.lowerEpochIntervalLimit = lowerEpochIntervalLimit;
        this.higherEpochIntervalLimit = higherEpochIntervalLimit;
    }

    // method for returning updated tb data
    public LiveData<TbStatus> getTbStatusLiveData() {
        return tbStatusLiveData;
    }

    public void initUpdateTbData(String methodSender) {
        this.methodSender = methodSender;

        getApiData();
    }

    ////// Api repo //////
    private void getApiData() {

        apiRepo.getTbData();
    }

    //
    public void updateTbData(List<TbData> tbDataList) {
        // filter and clean data
        tbDataList = dataProcessor.processData(tbDataList); // bemærk at elementer i tbDataList nu har modsat rækkefølge, så det ældste datapunkt ligger først i listen på indeksplads 0

        // add data to database
        addDataToDb(tbDataList);

        // get data from database
        tbDataList = getDbTbDataInInterval();

        TbStatus tbStatus = dataProcessor.calculateTbStatus(tbDataList);

        tbStatusLiveData.setValue(tbStatus);


        boolean isNotificationEnabled = sharedPreferences.getBoolean(Constants.SETTING_ENABLE_NOTIFICATIONS_KEY, true);

        if (methodSender.equals(Constants.FROM_ALERT_RECEIVER) && isNotificationEnabled){
            checkForNotification(tbStatus);
        }
    }

    private void checkForNotification(TbStatus tbStatus) {

        //skal vi tjekke på både tiden og om der er børstet? - to notifikationer

        boolean notificationShouldFire = true;
        boolean[] isTimeOk =  tbStatus.getIsTimeOk();
        for (int i = 0; i < numTbMissing; i ++)
        {
            if (isTimeOk[i]) {
                notificationShouldFire = false;
                break;
            }
        }

        if (notificationShouldFire) {
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(
                    ToothbrushApp.getAppContext().getString(R.string.NotificationHeader),
                    ToothbrushApp.getAppContext().getString(R.string.NotificationText)); // hardcoded med 3 dage...
            notificationHelper.getManager().notify(1, nb.build());
        }
    }


    ////// Db repo //////

    private void addDataToDb(List<TbData> tbDataList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbRepo.tbDao().addTbDataList(tbDataList);
            }
        });
    }

    private List<TbData> getDbTbDataInInterval() {
        Future<List<TbData>> tbDataList = executor.submit(new Callable<List<TbData>>() {

            @Override
            public List<TbData> call() {
                return dbRepo.tbDao().getTbDataInInterval(lowerEpochIntervalLimit, higherEpochIntervalLimit);
            }
        });

        try {
            return tbDataList.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*
        if (key.equals(Constants.SETTING_DAYS_WITHOUT_TB_KEY)) {
            numTbMissing = Integer.parseInt(sharedPreferences.getString(key, "")) * tbEachDay;
        } else if (key.equals(Constants.SETTING_TB_EACH_DAY_KEY)) {
            tbEachDay = Integer.parseInt(sharedPreferences.getString(key, ""));
        }

        initUpdateTbData(Constants.FROM_UPDATE_DATA_CTRL);

         */
    }



}
