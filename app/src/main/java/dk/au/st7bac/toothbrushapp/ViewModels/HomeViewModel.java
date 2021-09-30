package dk.au.st7bac.toothbrushapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dk.au.st7bac.toothbrushapp.Model.Repository;

// Inspired by MAD spring 21 course: class demo "Code Demo / walkthrough : using Room (and SharedPreferences)"
// and "Demo: Multiple fragments in UI using ViewModels and a Repository class - bonus: background service, singleton pattern example and overriding Application object to get app Context"
public class HomeViewModel extends ViewModel {


    private LiveData<boolean[]> isToothbrushDoneMorning;
    private Repository repository;

    public HomeViewModel() {
        super();
        repository = Repository.getInstance();
    }

    public LiveData<boolean[]> getToothbrushData()
    {
        return repository.getToothbrushData();
    }



}
