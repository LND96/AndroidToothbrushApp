package dk.au.st7bac.toothbrushapp.Model;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import dk.au.st7bac.toothbrushapp.Interfaces.IDataCleaner;

public class DataCleaner implements IDataCleaner {

    private int timeBetweenMeasurements;

    public DataCleaner(int timeBetweenMeasurements) {
        this.timeBetweenMeasurements = timeBetweenMeasurements;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<TbData> cleanData(List<TbData> tbDataList) {

        tbDataList = setDateTime(tbDataList);

        tbDataList = checkMeasurements(tbDataList);

        return tbDataList;
    }

    private List<TbData> setDateTime(List<TbData> tbDataList) {
        //set correct epoch and dateTime value
        for (TbData tbData : tbDataList) {

            // kilde: https://stackoverflow.com/questions/35183146/how-can-i-create-a-java-8-localdate-from-a-long-epoch-time-in-milliseconds
            // kilde: https://stackoverflow.com/questions/9936648/how-to-convert-string-to-long/24309678
            // get hex value from rawTelemetry
            String hexValue = tbData.getRawTelemetry().substring(4, 12);

            // convert to decimal value corresponding to epoch value
            int deciValue = Integer.parseInt(hexValue, 16);

            // convert epoch value to local date time
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(deciValue), ZoneId.systemDefault());

            // set epoch value and new date time
            tbData.setEpoch(deciValue);
            tbData.setDateTime(dateTime);
        }
        return tbDataList;
    }

    private List<TbData> checkMeasurements(List<TbData> tbDataList) {
        List<TbData> tbCleanDataList = new ArrayList<>();

        // checking if two or more measurements are within 10 minutes
        for (int i = tbDataList.size() - 1; i >= 0; i--) {

            if (i == 0) {
                tbCleanDataList.add(tbDataList.get(i));
            } else {
                if (tbDataList.get(i).getDateTime().isAfter(tbDataList.get(i - 1).getDateTime().minusMinutes(timeBetweenMeasurements))) {
                    // if current measurement is less than x minutes before the next measurement,
                    // add the current toothbrush time to the next tooth brush time
                    tbDataList.get(i - 1).setTbSecs(tbDataList.get(i - 1).getTbSecs() + tbDataList.get(i).getTbSecs());
                } else {
                    // if current measurement is more than x minutes before the next measurement,
                    // consider it as a separate measurement, and add it to the clean data list
                    tbCleanDataList.add(tbDataList.get(i));
                }
            }
        } // Når vi bevæger os baglæns i listen, vil det betyde, at datapunkterne bliver lagt i tbCleanDataList i den modsatte rækkefølge end de kom ind, så det ældste målepunkt nu ligger på indeksplads 0 i listen

        return tbCleanDataList;
    }
}
