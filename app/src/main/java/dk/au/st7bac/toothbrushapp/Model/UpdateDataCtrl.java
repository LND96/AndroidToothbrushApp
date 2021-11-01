package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
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

import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;
import dk.au.st7bac.toothbrushapp.Repositories.DbRepo;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

public class UpdateDataCtrl {

    public static UpdateDataCtrl updateDataCtrl;

    private final DataFilter dataFilter; // husk interface
    private final DataCleaner dataCleaner; // husk interface
    private final DataProcessor dataProcessor; //husk interface

    private MutableLiveData<TbStatus> tbStatusLiveData;

    private ApiRepo apiRepo;
    private final DbRepo dbRepo;

    // skal alle disse data sættes ved contructor injection i controlleren?
    private double offset = 6.0; // hardware offset
    private int minMeasurementDuration = 10; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private int maxMeasurementDuration = 600; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private int minAccTbTime = 90; // minimum time in secs that a tooth brushing should last to be accepted
    private LocalTime morningToEveningTime = LocalTime.parse("11:59"); // time of day where morning transitions to evening
    private LocalTime eveningToMorningTime = LocalTime.parse("00:00"); // time of day where evening transitions to morning
    private int numIntervalDays = 7; // number of days in interval
    private int tbEachDay = 2; // ideal number of tooth brushes each day
    private double numTbThres = 0.8; // threshold value for minimum number of tooth brushes compared to ideal number of tooth brushes
    private LocalDate lastDayInInterval = LocalDate.now(); // the last day of the time interval the calculations are made over
    private int timeBetweenMeasurements = 10; // maximum time in minutes between two measurements for them to be counted as one
    private long lowerEpochIntervalLimit;
    private long higherEpochIntervalLimit;

    private final ExecutorService executor; // for asynch processing


    /*
    public UpdateDataCtrl() {
        dataFilter = new DataFilter(offset, minMeasurementDuration, maxMeasurementDuration); // constructor injection?
        dataCleaner = new DataCleaner(); // constructor injection?
        dataProcessor = new DataProcessor(minAccTbTime, numIntervalDays, tbEachDay, morningToEveningTime, eveningToMorningTime, numTbThres, lastDayInInterval); // constructor injection?
        //setTestData();
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
        tbStatusLiveData = new MutableLiveData<>();
    }

     */

    // singleton pattern
    public static UpdateDataCtrl getInstance() { // Er det ok at bruge singleton her?
        if (updateDataCtrl == null) {
            updateDataCtrl = new UpdateDataCtrl();
        }
        return updateDataCtrl;
    }

    // private constructor
    private UpdateDataCtrl() {
        dataFilter = new DataFilter(offset, minMeasurementDuration, maxMeasurementDuration); // constructor injection?
        dataCleaner = new DataCleaner(timeBetweenMeasurements); // constructor injection?
        dataProcessor = new DataProcessor(minAccTbTime, numIntervalDays, tbEachDay, morningToEveningTime, eveningToMorningTime, numTbThres, lastDayInInterval); // constructor injection?
        //setTestData();
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
        tbStatusLiveData = new MutableLiveData<>();
    }


    // method for returning updated tb data
    public LiveData<TbStatus> getTbStatusLiveData() {
        // get data from api
        //initUpdateTbData();

        return tbStatusLiveData;
    }

    public void initUpdateTbData() {
        getApiData();
    }

    ////// Api repo //////
    private void getApiData() {
        if (apiRepo == null) {
            apiRepo = new ApiRepo(this);
        }

        apiRepo.getTbData();
    }

    //
    public void updateTbData(List<TbData> tbDataList) {

        // filter and clean data
        tbDataList = dataFilter.filterData(tbDataList);
        tbDataList = dataCleaner.cleanData(tbDataList);                                       // bemærk at elementer i tbDataList nu har modsat rækkefølge, så det ældste datapunkt ligger først i listen på indeksplads 0

        // add data to database
        addDataToDb(tbDataList);


        // get data from database
        tbDataList = getDbTbDataInInterval();

        TbStatus tbStatus = dataProcessor.processData(tbDataList);

        tbStatusLiveData.setValue(tbStatus);
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

    private void findEpochInterval() {
        // find system zone id
        ZoneId zoneId = ZoneId.systemDefault();

        // find utc +0 zone id
        ZoneId utcZoneId = ZoneId.of("Greenwich");

        // find first day in interval at midnight
        ZonedDateTime firstDateTimeInterval = lastDayInInterval.minusDays(numIntervalDays).atStartOfDay().atZone(utcZoneId);

        // find last day in interval at current time
        LocalDateTime lastDateTimeInterval = lastDayInInterval.atTime(LocalTime.now());

        // save results
        lowerEpochIntervalLimit = firstDateTimeInterval.toEpochSecond();
        higherEpochIntervalLimit = lastDateTimeInterval.atZone(zoneId).toEpochSecond();

    }
}
