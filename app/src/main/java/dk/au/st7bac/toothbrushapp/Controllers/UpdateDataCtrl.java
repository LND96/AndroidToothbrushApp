package dk.au.st7bac.toothbrushapp.Controllers;

import android.content.SharedPreferences;

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

// TODO: KILDE TIL DATABASE METODER
public class UpdateDataCtrl {

    public static UpdateDataCtrl updateDataCtrl;
    private DataProcessor dataProcessor;
    private ApiRepo apiRepo;
    private final DbRepo dbRepo;
    private final NotificationHelper notificationHelper;
    private final ExecutorService executor;
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<TbStatus> tbStatusLiveData;
    private long lowerEpochIntervalLimit;
    private long higherEpochIntervalLimit;
    private String methodSender;
    private int numTbMissing;


    // public singleton constructor
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
        executor = Executors.newSingleThreadExecutor(); // executor for asynchronous processing
        tbStatusLiveData = new MutableLiveData<>();
        notificationHelper = new NotificationHelper(ToothbrushApp.getAppContext());
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

    // return updated tb data
    public LiveData<TbStatus> getTbStatusLiveData() {
        return tbStatusLiveData;
    }

    // initialize update of tb data
    public void initUpdateTbData(String methodSender) {
        // string to keep track of who called this method
        this.methodSender = methodSender;

        // get data from api
        getApiData();
    }

    // process and update tb data
    public void updateTbData(List<TbData> tbDataList) {
        // filter and clean data
        tbDataList = dataProcessor.processData(tbDataList);

        // add current data to database
        addDataToDb(tbDataList);

        // get data in given interval from database
        tbDataList = getDbTbDataInInterval(lowerEpochIntervalLimit, higherEpochIntervalLimit);

        // calculate tb status
        TbStatus tbStatus = dataProcessor.calculateTbStatus(tbDataList);

        // update live data
        tbStatusLiveData.setValue(tbStatus);

        // check if notification should be fired
        checkForNotification(tbStatus);
    }

    private void checkForNotification(TbStatus tbStatus) {

        boolean isNotificationEnabled = sharedPreferences
                .getBoolean(Constants.SETTING_ENABLE_NOTIFICATIONS_KEY, true);

        // first check if notification is enabled and method sender is alert receiver
        if (methodSender.equals(Constants.FROM_ALERT_RECEIVER) && isNotificationEnabled){
            boolean fireNotification = true;
            boolean[] isTimeOk =  tbStatus.getIsTimeOk();

            // if a value in isTimeOk is true, then notification should not fire
            for (int i = 0; i < numTbMissing; i ++)
            {
                if (isTimeOk[i]) {
                    fireNotification = false;
                    break;
                }
            }

            // create notification with notification helper if notification should fire
            if (fireNotification) {
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(
                        ToothbrushApp.getAppContext().getString(R.string.NotificationHeader),
                        ToothbrushApp.getAppContext().getString(R.string.NotificationText)); // TODO: hardcoded med 3 dage...
                notificationHelper.getManager().notify(1, nb.build());
            }
        }
    }

    ////// API REPO //////
    private void getApiData() {
        apiRepo.getTbData();
    }


    ////// DB REPO //////
    private void addDataToDb(List<TbData> tbDataList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbRepo.tbDao().addTbDataList(tbDataList);
            }
        });
    }

    private List<TbData> getDbTbDataInInterval(Long lowerEpochIntervalLimit, Long higherEpochIntervalLimit) {
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
}
