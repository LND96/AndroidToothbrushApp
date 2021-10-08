package dk.au.st7bac.toothbrushapp.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.st7bac.toothbrushapp.Services.WebApiService;

public class Repository {

    public static Repository repository;
    private MutableLiveData<TbStatus> toothbrushDataLiveData; // bør være LiveData frem for MutableLiveData, men er her mutable så der kan hardcodes værdier
    private TbStatus testData;

    private WebApiService webApiService;

    // Singleton pattern
    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    private Repository() {
        setTestData();
    }

    // METODE TIL AT TESTE MVVM
    private void setTestData() {
        String[] headerStrings = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier
        boolean[] isToothbrushDoneMorning = {true, false, true, true, true, true, false}; // hardcoded værdier
        boolean[] isTimeOkMorning = {true, false, true, false, false, false, false}; // hardcoded værdier
        boolean[] isToothbrushDoneEvening = {false, true, true, true, true, false, true}; // hardcoded værdier
        boolean[] isTimeOkEvening = {true, false, true, false, false, false, false}; // hardcoded værdier
        int toothbrushesCompleted = 10; // hardcoded værdi
        int totalNumberToothbrushes = 14; // hardcoded værdi
        int avgBrushTime = 46; // hardcoded værdi
        boolean isAvgNumberToothbrushesOk = true; // hardcoded værdi
        boolean isAvgTimeOk = false; // hardcoded værdi

        testData = new TbStatus(headerStrings, isToothbrushDoneMorning,
                isTimeOkMorning, isToothbrushDoneEvening, isTimeOkEvening, toothbrushesCompleted,
                totalNumberToothbrushes, avgBrushTime, isAvgNumberToothbrushesOk, isAvgTimeOk);
        //boolean[] testData = {true, false, true, true, true, true, false};
        toothbrushDataLiveData = new MutableLiveData<>(testData);
    }

    public LiveData<TbStatus> getToothbrushDataLiveData() {
        getApiData();
        return toothbrushDataLiveData;
    }

    ////// Web Api Service //////
    private void getApiData() {
        if (webApiService == null) {
            webApiService = new WebApiService();
        }

        webApiService.getTbData();

    }

    public void setTbData(ArrayList<TbData> tbDataList) {
        Log.d("Repository tbDataList", "Test");
    }
}
