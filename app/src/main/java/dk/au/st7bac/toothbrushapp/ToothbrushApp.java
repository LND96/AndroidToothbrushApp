package dk.au.st7bac.toothbrushapp;

import android.app.Application;
import android.content.Context;

// source of inspiration: class demo: "Demo: Multiple fragments in UI using ViewModels and a Repository class - bonus: background service, singleton pattern example and overriding Application object to get app Context"
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

    public AppContainer appContainer = new AppContainer();
}
