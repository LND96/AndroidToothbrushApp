package dk.au.st7bac.toothbrushapp.Model;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.DataProcessor;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.Processor1;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataCleaner;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataFilter;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataCalculator;
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

    private Settings settings;

    private final ExecutorService executor; // for asynch processing

    private NotificationHelper notificationHelper;

    private String methodSender;

    private SharedPreferences sharedPreferences;



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
        SettingsReader reader = new SettingsReader();
        settings = reader.getConfigSettings(ToothbrushApp.getAppContext(), sharedPreferences);
        dataProcessor = new Processor1(settings);
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
        tbStatusLiveData = new MutableLiveData<>();

        notificationHelper = new NotificationHelper(ToothbrushApp.getAppContext());

        addSettingsToDb(settings);

        // register on shared preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this); // skal den unregisteres igen?
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
        if (apiRepo == null) { // skal dette i constructoren?
            apiRepo = new ApiRepo(this, settings.getSensorId(), settings.getApiSince());
        } else {
            apiRepo.setApiLimit(settings.getApiLimit());
        }

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


        boolean isNotificationEnabled = sharedPreferences.getBoolean(ToothbrushApp.getAppContext().getString(R.string.settingEnableNotificationsKey), true);

        if (methodSender.equals(Constants.FROM_ALERT_RECEIVER) && isNotificationEnabled){
            checkForNotification(tbStatus);
        }
    }

    private void checkForNotification(TbStatus tbStatus) {

        //skal vi tjekke på både tiden og om der er børstet? - to notifikationer

        boolean notification = false;
        boolean[] isTimeOk =  tbStatus.getIsTimeOk();
        for (int i = 0; i < 6; i ++)
        {
            if (isTimeOk[i]) {
                notification = true;
            }
        }

        if (notification == false) {
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(ToothbrushApp.getAppContext().getString(R.string.NotificationHeader), ToothbrushApp.getAppContext().getString(R.string.NotificationText));
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

    private void addSettingsToDb(Settings settings) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbRepo.tbDao().addSettings(settings);
            }
        });
    }

    // har vi brug for at hente alle tb data?
    /*
    private List<TbData> getAllDbTbData() {
        Future<List<TbData>> tbDataList = executor.submit(new Callable<List<TbData>>() {
            @Override
            public List<TbData> call() {
                return dbRepo.tbDao().getAllTbData();
            }
        });

        try {
            return tbDataList.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
     */

    private List<TbData> getDbTbDataInInterval() {
        findEpochInterval();

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

    private void findEpochInterval() { // skal denne metode evt. lægges ud i en klasse?
        // find system zone id
        ZoneId zoneId = ZoneId.systemDefault();

        // find utc +0 zone id
        ZoneId utcZoneId = ZoneId.of("Greenwich");

        // find first day in interval at midnight
        ZonedDateTime firstDateTimeInterval = settings.getLastDayInInterval().minusDays(settings.getNumIntervalDays()).atStartOfDay().atZone(utcZoneId);

        // find last day in interval at current time
        LocalDateTime lastDateTimeInterval = settings.getLastDayInInterval().atTime(LocalTime.now());

        // save results
        lowerEpochIntervalLimit = firstDateTimeInterval.toEpochSecond();
        higherEpochIntervalLimit = lastDateTimeInterval.atZone(zoneId).toEpochSecond();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // skal her bruges switch i stedet?
        if (key.equals(ToothbrushApp.getAppContext().getString(R.string.settingSensorIdKey))) {
            apiRepo.setApiSensorId(sharedPreferences.getString(key, ""));
        } else {
            dataProcessor.updateSettings(sharedPreferences, key);
        }
        // skal initUpdate kaldes når settings er opdateret?
        initUpdateTbData(Constants.FROM_UPDATE_DATA_CTRL);
    }
}
