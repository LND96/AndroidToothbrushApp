package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {

    public static Repository repository;
    private MutableLiveData<boolean[]> toothbrushData; // bør være LiveData frem for MutableLiveData, men er her mutable så der kan hardcodes værdier

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
        boolean[] testData = {true, false, true, true, true, true, false};
        toothbrushData = new MutableLiveData<>(testData);
    }

    public LiveData<boolean[]> getToothbrushData() {
        return toothbrushData;
    }
}
