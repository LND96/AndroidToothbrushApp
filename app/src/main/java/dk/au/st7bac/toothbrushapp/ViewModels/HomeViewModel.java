package dk.au.st7bac.toothbrushapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {


    private LiveData<boolean[]> isToothbrushDoneMorning;

    public HomeViewModel() {
        super();

    }

}
