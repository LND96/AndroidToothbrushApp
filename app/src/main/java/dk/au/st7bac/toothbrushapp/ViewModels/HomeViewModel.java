package dk.au.st7bac.toothbrushapp.ViewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;

// Inspired by MAD spring 21 course: class demo "Code Demo / walkthrough : using Room (and SharedPreferences)"
// and "Demo: Multiple fragments in UI using ViewModels and a Repository class - bonus: background service, singleton pattern example and overriding Application object to get app Context"
public class HomeViewModel extends ViewModel {

    private UpdateDataCtrl updateDataCtrl;

    public HomeViewModel() {
        super();
        updateDataCtrl = UpdateDataCtrl.getInstance();
    }

    public LiveData<TbStatus> getTbStatusData()
    {
        return updateDataCtrl.getTbStatusLiveData();
    }

    /*
    public void setController(UpdateDataCtrl updateDataCtrl) {
        this.updateDataCtrl = updateDataCtrl;
    }

     */
}
