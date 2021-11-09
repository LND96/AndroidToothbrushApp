package dk.au.st7bac.toothbrushapp.Model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import dk.au.st7bac.toothbrushapp.R;

// https://stackoverflow.com/questions/5140539/android-configuration-file
public class SettingsReader {

    private static final String TAG = "FileReader";

    public Settings getConfigSettings(Context context) {

        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);

            // skal vi have defaultværdier?
            // hvad hvis der er bogstaver hvor vi vil have tal mm.?
            String sensorId = properties.getProperty("sensorId");
            String apiSince = properties.getProperty("apiSince");
            String apiLimit = properties.getProperty("apiLimit");
            double offset = Double.parseDouble(properties.getProperty("offset"));
            int minMeasurementDuration = Integer.parseInt(properties.getProperty("minMeasurementDuration"));
            int maxMeasurementDuration = Integer.parseInt(properties.getProperty("maxMeasurementDuration"));
            int minAccpTbTime = Integer.parseInt(properties.getProperty("minAccpTbTime"));
            String morningToEveningTime = properties.getProperty("morningToEveningTime");
            String eveningToMorningTime = properties.getProperty("eveningToMorningTime");
            int tbEachDay = Integer.parseInt(properties.getProperty("tbEachDay"));
            double numTbThres = Double.parseDouble(properties.getProperty("numTbThres"));
            int timeBetweenMeasurements = Integer.parseInt(properties.getProperty("timeBetweenMeasurements"));
            int numIntervalDays = Integer.parseInt(properties.getProperty("numIntervalDays"));
            String lastDayInInterval = properties.getProperty("lastDayInInterval");
            boolean firstRun = Boolean.parseBoolean(properties.getProperty("firstRun", "true"));
            //if (firstRun) {
                //Files.write(Paths.get("config.txt"), "firstRun=false".getBytes(), StandardOpenOption.APPEND);
            //    String filename = "src\\main\\res\\raw\\config.txt";
            //    FileWriter fileWriter = new FileWriter(filename, true);
            //    fileWriter.write("firstRun=false\n");
            //    fileWriter.close();
            //}

            Settings settings = new Settings(sensorId, apiSince, apiLimit, offset,
                    minMeasurementDuration, maxMeasurementDuration, minAccpTbTime,
                    morningToEveningTime, eveningToMorningTime, tbEachDay, numTbThres,
                    timeBetweenMeasurements, numIntervalDays, lastDayInInterval);

            return settings;
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file");
        }

        return null;
    }



}
