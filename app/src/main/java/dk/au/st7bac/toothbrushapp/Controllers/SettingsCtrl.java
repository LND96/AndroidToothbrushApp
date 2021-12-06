
package dk.au.st7bac.toothbrushapp.Controllers;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.DataProcessor;
import dk.au.st7bac.toothbrushapp.DataProcessorFactory.Processor1;
import dk.au.st7bac.toothbrushapp.Interfaces.IApiRepo;
import dk.au.st7bac.toothbrushapp.Interfaces.IConfigReader;
import dk.au.st7bac.toothbrushapp.Model.Configs;
import dk.au.st7bac.toothbrushapp.Model.ConfigReader;
import dk.au.st7bac.toothbrushapp.Repositories.ApiRepo;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

// inspiration for preference change listener: https://www.youtube.com/watch?v=B155JJwHB3c
public class SettingsCtrl implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static SettingsCtrl settingsCtrl;
    private final Configs configs;
    private DataProcessor dataProcessor;
    private final UpdateDataCtrl updateDataCtrl;
    private final IApiRepo apiRepo;
    private long lowerEpochIntervalLimit;
    private long higherEpochIntervalLimit;

    // public singleton constructor
    public static SettingsCtrl getInstance() {
        if (settingsCtrl == null) {
            settingsCtrl = new SettingsCtrl();
        }
        return settingsCtrl;
    }

    // private constructor
    private SettingsCtrl() {
        // get shared preferences and configurations
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ToothbrushApp.getAppContext());
        IConfigReader reader = new ConfigReader();
        configs = reader.getConfigSettings(ToothbrushApp.getAppContext());

        // local variables
        String baseUrl = configs.getBaseUrl();
        String apiSince = configs.getApiSince();
        String apiLimit;
        String sensorId;
        int daysWithoutTb;
        int tbEachDay;

        // if first run get settings from configuration file, else get settings from shared preferences
        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            apiLimit = configs.getApiLimitFirstRun();
            sensorId = sharedPreferences.getString(Constants.SETTING_SENSOR_ID_KEY, "");
            daysWithoutTb = configs.getDaysWithoutTb();
            tbEachDay = configs.getTbEachDay();
        } else {
            apiLimit = configs.getApiLimit();
            sensorId = sharedPreferences.getString(Constants.SETTING_SENSOR_ID_KEY, "");
            daysWithoutTb = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_DAYS_WITHOUT_TB_KEY, ""));
            tbEachDay = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_TB_EACH_DAY_KEY, ""));
        }

        // switch case allows more processor types in the future
        switch (configs.getDataProcessor().toLowerCase()) {
            case "processor1":{
                dataProcessor = new Processor1(configs);
                break;
            }
        }

        // create objects
        updateDataCtrl = UpdateDataCtrl.getInstance();
        apiRepo = new ApiRepo(updateDataCtrl, sensorId, apiSince, apiLimit, baseUrl);
        updateDataCtrl.setDataProcessor(dataProcessor);
        updateDataCtrl.setApiRepo(apiRepo);
        updateDataCtrl.setNumTbMissing(daysWithoutTb * tbEachDay);
        findEpochInterval();
        updateDataCtrl.setEpochLimits(lowerEpochIntervalLimit, higherEpochIntervalLimit);

        // register on shared preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // finds epoch values for interval
    private void findEpochInterval() {
        // find system zone id
        ZoneId zoneId = ZoneId.systemDefault();

        // find utc +0 zone id
        ZoneId utcZoneId = ZoneId.of("Greenwich");

        // get number of days in interval
        int numIntervalDays = configs.getNumIntervalDays();

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
        // depending on what changed, settings are updated
        if (key.equals(Constants.SETTING_SENSOR_ID_KEY)) {
            apiRepo.setApiSensorId(sharedPreferences.getString(key, ""));
            updateDataCtrl.setApiRepo(apiRepo);
        } else if (key.equals(Constants.SETTING_MIN_ACCP_TIME_KEY)
                || key.equals(Constants.SETTING_MIN_ACCP_PERCENT_KEY)
                || key.equals(Constants.SETTING_NUM_INTERVAL_DAYS_KEY)) {
            dataProcessor.updateSettings(sharedPreferences, key);
            updateDataCtrl.setDataProcessor(dataProcessor);
        } else if (key.equals(Constants.SETTING_TB_EACH_DAY_KEY)
                || key.equals(Constants.SETTING_DAYS_WITHOUT_TB_KEY)) {
            int tbEachDay = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_TB_EACH_DAY_KEY, ""));
            int numTbMissing = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_DAYS_WITHOUT_TB_KEY, "")) * tbEachDay;
            updateDataCtrl.setNumTbMissing(numTbMissing);
        }

        updateDataCtrl.initUpdateTbData(Constants.FROM_SETTINGS_CTRL);
    }
}