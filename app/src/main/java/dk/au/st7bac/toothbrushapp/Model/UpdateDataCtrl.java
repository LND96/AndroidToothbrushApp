package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalTime;
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

    private DataFilter dataFilter; // husk interface
    private DataCleaner dataCleaner; // husk interface
    private DataProcessor processor; //husk interface

    private MutableLiveData<TbStatus> tbStatusLiveData; // bør være LiveData frem for MutableLiveData, men er her mutable så der kan hardcodes værdier
    private TbStatus testData;

    private ApiRepo apiRepo;
    private DbRepo dbRepo;

    // skal alle disse data sættes ved contructor injection i controlleren?
    private double offset = 6.0; // hardware offset
    private int minMeasurementDuration = 10; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private int maxMeasurementDuration = 600; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private int minAccTbTime = 90; // minimum time in secs that a tooth brushing should last to be accepted
    private LocalTime morningToEveningTime = LocalTime.parse("11:59"); // time of day where morning transitions to evening
    private LocalTime eveningToMorningTime = LocalTime.parse("00:00"); // time of day where evening transitions to morning
    private int days = 7; // dage vi kigger tilbage i tiden - hvoran forklarer vi det?
    private int tbEachDay = 2; // wanted number of tooth brushes each day
    private double numTbThres = 0.8; // threshold value for minimum number of tooth brushes compared to ideal number of tooth brushes

    private ExecutorService executor; // for asynch processing

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
        dataCleaner = new DataCleaner(); // constructor injection?
        processor = new DataProcessor(minAccTbTime, days, tbEachDay, morningToEveningTime, eveningToMorningTime, numTbThres); // constructor injection?
        setTestData();
        dbRepo = DbRepo.getDbRepo(ToothbrushApp.getAppContext());
        executor = Executors.newSingleThreadExecutor();
    }

    // method for returning updated tb data
    public LiveData<TbStatus> getTbStatusLiveData() {
        // get data from api
        getApiData();


        return tbStatusLiveData;
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
        tbDataList = dataFilter.FilterData(tbDataList);
        tbDataList = dataCleaner.CleanData(tbDataList); // bemærk at elementer i tbDataList nu har modsat rækkefølge, så det ældste datapunkt ligger først i listen på indeksplads 0

        // add data to database
        addDataToDb(tbDataList);


        // get data from database
        List test = getAllDbTbData();
        List test2 = getDbTbDataInInterval();


        processor.ProcessData(tbDataList);
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
        Future<List<TbData>> tbDataList = executor.submit(new Callable<List<TbData>>() {
            @Override
            public List<TbData> call() {
                return dbRepo.tbDao().getTbDataInInterval();
            }
        });

        try {
            return tbDataList.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    // METODE TIL AT TESTE MVVM
    private void setTestData() {
        String[] headerStrings = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier
        boolean[] isTbDone = {true, false, false, true, true, true, true, true, true, true, true, false, false, true}; // hardcoded værdier
        boolean[] isTimeOk = {true, false, false, false, true, false, false, true, false, false, false, false, false, false}; // hardcoded værdier
        int toothbrushesCompleted = 10; // hardcoded værdi
        int totalNumberToothbrushes = 14; // hardcoded værdi
        int avgBrushTime = 46; // hardcoded værdi
        boolean isAvgNumberToothbrushesOk = true; // hardcoded værdi
        boolean isAvgTimeOk = false; // hardcoded værdi

        testData = new TbStatus(headerStrings, isTbDone, isTimeOk, toothbrushesCompleted,
                totalNumberToothbrushes, avgBrushTime, isAvgNumberToothbrushesOk, isAvgTimeOk);
        tbStatusLiveData = new MutableLiveData<>(testData);

    }
}
