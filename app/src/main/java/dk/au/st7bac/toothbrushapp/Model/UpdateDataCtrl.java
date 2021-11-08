package dk.au.st7bac.toothbrushapp.Model;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.au.st7bac.toothbrushapp.DataProcessorFactory.DataProcessor;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.Processor1;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataCleaner;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataFilter;
import dk.au.st7bac.toothbrushapp.Interfaces.IDataCalculator;
import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;
import dk.au.st7bac.toothbrushapp.Repositories.DbRepo;
import dk.au.st7bac.toothbrushapp.Services.NotificationHelper;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

public class UpdateDataCtrl {

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

    // singleton pattern
    public static UpdateDataCtrl getInstance() {
        if (updateDataCtrl == null) {
            updateDataCtrl = new UpdateDataCtrl();
        }
        return updateDataCtrl;
    }

    // private constructor
    private UpdateDataCtrl() {
        SettingsReader reader = new SettingsReader();
        settings = reader.getConfigSettings(ToothbrushApp.getAppContext());
        //dataFilter = new DataFilter(settings.getOffset(), settings.getMinMeasurementDuration(),
        //        settings.getMaxMeasurementDuration());
        //dataCleaner = new DataCleaner(settings.getTimeBetweenMeasurements());
        //dataProcessor = new DataCalculator(settings.getMinAccpTbTime(),
        //        settings.getNumIntervalDays(), settings.getTbEachDay(),
        //        settings.getMorningToEveningTime(), settings.getEveningToMorningTime(),
        //        settings.getNumTbThres(), settings.getLastDayInInterval());
        dataProcessor = new Processor1(settings);
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
        tbStatusLiveData = new MutableLiveData<>();

        notificationHelper = new NotificationHelper(ToothbrushApp.getAppContext());
    }


    // method for returning updated tb data
    public LiveData<TbStatus> getTbStatusLiveData() {
        return tbStatusLiveData;
    }

    public void initUpdateTbData() {
        getApiData();
    }

    ////// Api repo //////
    private void getApiData() {
        if (apiRepo == null) {
            apiRepo = new ApiRepo(this, settings.getSensorId(), settings.getApiSince(),
                    settings.getApiLimit());
        }

        apiRepo.getTbData();
    }

    //
    public void updateTbData(List<TbData> tbDataList) {
        // filter and clean data
        tbDataList = dataProcessor.processData(tbDataList); // bemærk at elementer i tbDataList nu har modsat rækkefølge, så det ældste datapunkt ligger først i listen på indeksplads 0

        //tbDataList = dataFilter.filterData(tbDataList);
        //tbDataList = dataCleaner.cleanData(tbDataList);

        // add data to database
        addDataToDb(tbDataList);

        // get data from database
        tbDataList = getDbTbDataInInterval();

        TbStatus tbStatus = dataProcessor.calculateTbStatus(tbDataList);

        tbStatusLiveData.setValue(tbStatus);

        checkForNotification(tbStatus);
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
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Alarm", "besked");
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
}
