package dk.au.st7bac.toothbrushapp;

import android.app.Application;
import android.content.Context;

// inspiration: SWMAD-01 Mobile Application Development, lecture 7, spring 2021
public class ToothbrushApp extends Application {

    private static ToothbrushApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
