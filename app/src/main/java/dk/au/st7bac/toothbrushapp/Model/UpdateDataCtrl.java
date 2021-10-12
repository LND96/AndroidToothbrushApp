package dk.au.st7bac.toothbrushapp.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;

public class UpdateDataCtrl {

    public static UpdateDataCtrl updateDataCtrl;

    private DataFilter dataFilter; // husk interface
    private DataCleaner dataCleaner; // husk interface

    private MutableLiveData<TbStatus> tbStatusLiveData; // bør være LiveData frem for MutableLiveData, men er her mutable så der kan hardcodes værdier
    private TbStatus testData;

    private ApiRepo apiRepo;

    public static UpdateDataCtrl getInstance() { // Er det ok at bruge singleton her?
        if (updateDataCtrl == null) {
            updateDataCtrl = new UpdateDataCtrl();
        }
        return updateDataCtrl;
    }


    private UpdateDataCtrl() {
        setTestData();
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

    public LiveData<TbStatus> getTbStatusLiveData() {
        getApiData();
        return tbStatusLiveData;
    }

    ////// Web Api Service //////
    private void getApiData() {
        if (apiRepo == null) {
            apiRepo = new ApiRepo(this);
        }

        apiRepo.getTbData();
    }

    public void setTbData(ArrayList<TbData> tbDataList) {

        dataFilter = new DataFilter(); // bør ikke oprettes her, men i constructoreren med injection
        tbDataList = dataFilter.FilterData(tbDataList);


        dataCleaner = new DataCleaner();                                 // bør ikke oprettes her, men i constructoreren med injection
        tbDataList = dataCleaner.CleanData(tbDataList); // bemærk at elementer i tbDataList nu har modsat rækkefølge, så det ældste datapunkt ligger først i listen på indeksplads 0
    }
}
