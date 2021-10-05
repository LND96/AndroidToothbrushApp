package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {

    public static Repository repository;
    private MutableLiveData<ToothbrushData> toothbrushDataLiveData; // bør være LiveData frem for MutableLiveData, men er her mutable så der kan hardcodes værdier
    private ToothbrushData testData;

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

        testData = new ToothbrushData(headerStrings, isToothbrushDoneMorning,
                isTimeOkMorning, isToothbrushDoneEvening, isTimeOkEvening, toothbrushesCompleted,
                totalNumberToothbrushes, avgBrushTime, isAvgNumberToothbrushesOk, isAvgTimeOk);
        //boolean[] testData = {true, false, true, true, true, true, false};
        toothbrushDataLiveData = new MutableLiveData<>(testData);
    }

    public LiveData<ToothbrushData> getToothbrushDataLiveData() {
        return toothbrushDataLiveData;
    }
}
