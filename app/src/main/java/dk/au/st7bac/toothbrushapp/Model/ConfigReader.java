package dk.au.st7bac.toothbrushapp.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import dk.au.st7bac.toothbrushapp.R;

// https://stackoverflow.com/questions/5140539/android-configuration-file
public class ConfigReader {

    private static final String TAG = "FileReader";

    public Configs getConfigSettings(Context context) {

        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);

            String apiSince = properties.getProperty("apiSince");
            String apiLimitFirstRun = properties.getProperty("apiLimitFirstRun");
            String apiLimit = properties.getProperty("apiLimit");
            double offset = Double.parseDouble(properties.getProperty("offset"));
            int minMeasurementDuration = Integer.parseInt(properties.getProperty("minMeasurementDuration"));
            int maxMeasurementDuration = Integer.parseInt(properties.getProperty("maxMeasurementDuration"));
            String morningToEveningTime = properties.getProperty("morningToEveningTime");
            String eveningToMorningTime = properties.getProperty("eveningToMorningTime");
            int timeBetweenMeasurements = Integer.parseInt(properties.getProperty("timeBetweenMeasurements"));
            String lastDayInInterval = properties.getProperty("lastDayInInterval");
            String dataProcessor = properties.getProperty("dataProcessor");
            int tbEachDay = Integer.parseInt(properties.getProperty("tbEachDay"));
            double numTbThres = Double.parseDouble(properties.getProperty("numTbThres"));
            int numIntervalDays = Integer.parseInt(properties.getProperty("numIntervalDays"));
            int minAccpTbTime = Integer.parseInt(properties.getProperty("minAccpTbTime"));
            int daysWithoutTb = Integer.parseInt(properties.getProperty("daysWithoutTb"));
            String sensorId = properties.getProperty("sensorId");

            return new Configs(apiSince, apiLimitFirstRun, apiLimit, offset,
                    minMeasurementDuration, maxMeasurementDuration, morningToEveningTime,
                    eveningToMorningTime, timeBetweenMeasurements, lastDayInInterval, dataProcessor,
                    tbEachDay, numTbThres, numIntervalDays, minAccpTbTime, daysWithoutTb, sensorId);

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file");
        }

        return null;
    }
}
