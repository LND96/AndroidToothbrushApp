package dk.au.st7bac.toothbrushapp.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

// inspiration for parsing hex value to integer: https://stackoverflow.com/questions/11194513/convert-hex-string-to-int
// inspiration for converting epoch value to date time object: https://stackoverflow.com/questions/35183146/how-can-i-create-a-java-8-localdate-from-a-long-epoch-time-in-milliseconds
public class DataCleaner {

    private final int timeBetweenMeasurements; // maximum time in minutes between two measurements for them to be counted as one

    public DataCleaner(int timeBetweenMeasurements) {
        this.timeBetweenMeasurements = timeBetweenMeasurements;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<TbData> cleanData(List<TbData> tbDataList) {

        tbDataList = setDateTime(tbDataList);
        tbDataList = checkMeasurements(tbDataList);

        return tbDataList;
    }

    // sets correct epoch and date time values
    private List<TbData> setDateTime(List<TbData> tbDataList) {
        for (TbData tbData : tbDataList) {

            // get hex value from rawTelemetry
            String hexValue = tbData.getRawTelemetry().substring(4, 12);

            // convert to decimal value corresponding to epoch value
            int deciValue = Integer.parseInt(hexValue, 16);

            // convert epoch value to local date time
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(deciValue),
                    ZoneId.systemDefault());

            // set epoch value and new date time
            tbData.setEpoch(deciValue);
            tbData.setDateTime(dateTime);
        }

        return tbDataList;
    }

    // checks if two or more measurements are within 10 minutes and combines them if they are
    private List<TbData> checkMeasurements(List<TbData> tbDataList) {
        List<TbData> tbCleanDataList = new ArrayList<>();

        for (int i = tbDataList.size() - 1; i >= 0; i--) {

            if (i == 0) {
                // always add the last measurement
                tbCleanDataList.add(tbDataList.get(i));
            } else {
                if (tbDataList.get(i).getDateTime().isAfter(tbDataList.get(i - 1).getDateTime().minusMinutes(timeBetweenMeasurements))) {
                    // if current measurement is less than x minutes before the next measurement,
                    // add the current tb time to the next tb time
                    tbDataList.get(i - 1).setTbSecs(tbDataList.get(i - 1).getTbSecs() + tbDataList.get(i).getTbSecs());
                } else {
                    // if current measurement is more than x minutes before the next measurement,
                    // consider it as a separate measurement, and add it to the clean data list
                    tbCleanDataList.add(tbDataList.get(i));
                }
            }
        }

        return tbCleanDataList;
    }
}
