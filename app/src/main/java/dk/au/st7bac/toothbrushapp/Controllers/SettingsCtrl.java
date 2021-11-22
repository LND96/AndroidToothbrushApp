package dk.au.st7bac.toothbrushapp.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.DataProcessor;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.Processor1;
import dk.au.st7bac.toothbrushapp.Model.Configs;
import dk.au.st7bac.toothbrushapp.Model.ConfigReader;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

public class SettingsCtrl implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static SettingsCtrl settingsCtrl;

    private SharedPreferences sharedPreferences;
    private Configs configs;
    private DataProcessor dataProcessor;
    private UpdateDataCtrl updateDataCtrl;
    private ApiRepo apiRepo;
    private long lowerEpochIntervalLimit;
    private long higherEpochIntervalLimit;

    // singleton pattern
    public static SettingsCtrl getInstance() {
        if (settingsCtrl == null) {
            settingsCtrl = new SettingsCtrl();
        }
        return settingsCtrl;
    }

    private SettingsCtrl() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());
        ConfigReader reader = new ConfigReader();
        configs = reader.getConfigSettings(ToothbrushApp.getAppContext(), sharedPreferences);
        if (configs.getDataProcessor().equals("Processor1")) { // lav det til en switch. Tag højde for små bogstaver. default processor.
            dataProcessor = new Processor1(configs); // typen af processor kan også komme fra settings
        }
        updateDataCtrl = UpdateDataCtrl.getInstance();
        apiRepo = new ApiRepo(updateDataCtrl, sharedPreferences.getString(Constants.SETTING_SENSOR_ID_KEY, ""), configs.getApiSince()); // den tager default value
        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            apiRepo.setApiLimit(configs.getApiLimitFirstRun());
        } else {
            apiRepo.setApiLimit(configs.getApiLimit());
        }
        updateDataCtrl.setDataProcessor(dataProcessor);
        updateDataCtrl.setApiRepo(apiRepo);
        findEpochInterval();
        updateDataCtrl.setEpochLimits(lowerEpochIntervalLimit, higherEpochIntervalLimit);


        // register on shared preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this); // skal den unregisteres igen?
    }

    private void findEpochInterval() { // skal denne metode evt. lægges ud i en klasse?
        // find system zone id
        ZoneId zoneId = ZoneId.systemDefault();

        // find utc +0 zone id
        ZoneId utcZoneId = ZoneId.of("Greenwich");

        int numIntervalDays = Integer.parseInt(sharedPreferences.getString(Constants.SETTING_NUM_INTERVAL_DAYS_KEY, "7"));

        // find first day in interval at midnight
        ZonedDateTime firstDateTimeInterval = configs.getLastDayInInterval().minusDays(numIntervalDays).atStartOfDay().atZone(utcZoneId);

        // find last day in interval at current time
        LocalDateTime lastDateTimeInterval = configs.getLastDayInInterval().atTime(LocalTime.now());

        // save results
        lowerEpochIntervalLimit = firstDateTimeInterval.toEpochSecond();
        higherEpochIntervalLimit = lastDateTimeInterval.atZone(zoneId).toEpochSecond();

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // skal her bruges switch i stedet? Kan ikke bruge switch da strings ikke er sat ved compileti

        if (key.equals(Constants.SETTING_SENSOR_ID_KEY)) {
            apiRepo.setApiSensorId(sharedPreferences.getString(key, ""));
            updateDataCtrl.setApiRepo(apiRepo);
        } else if (key.equals(Constants.SETTING_MIN_ACCP_TIME_KEY) ||
                key.equals(Constants.SETTING_MIN_ACCP_PERCENT_KEY) ||
                key.equals(Constants.SETTING_NUM_INTERVAL_DAYS_KEY)) {
            dataProcessor.updateSettings(sharedPreferences, key);
            updateDataCtrl.setDataProcessor(dataProcessor);
        }
        updateDataCtrl.initUpdateTbData(Constants.FROM_SETTINGS_CTRL);
    }
}
