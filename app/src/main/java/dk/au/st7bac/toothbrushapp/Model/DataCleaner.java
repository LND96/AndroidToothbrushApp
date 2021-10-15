package dk.au.st7bac.toothbrushapp.Model;

import android.content.IntentFilter;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataCleaner {

    private static final String TAG = "DataCleaner";
    private List<TbData> tbCleanDataList;
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<TbData> CleanData(List<TbData> tbDataList) {

        tbCleanDataList = new ArrayList<>();

        for (TbData tbData : tbDataList) {

            // kilde: https://stackoverflow.com/questions/35183146/how-can-i-create-a-java-8-localdate-from-a-long-epoch-time-in-milliseconds
            // kilde: https://stackoverflow.com/questions/9936648/how-to-convert-string-to-long/24309678
            String hexValue = tbData.getRawTelemetry().substring(4, 12);
            int deciValue = Integer.parseInt(hexValue, 16);

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(deciValue), ZoneId.systemDefault());

            // set new date time
            tbData.setEpoch(deciValue);
            tbData.setDateTime(dateTime);
        }

        // checking if two or more measurements are within 10 minutes
        for (int i = tbDataList.size() - 1; i >= 0; i--) {

            if (i == 0) {
                tbCleanDataList.add(tbDataList.get(i));
            } else {
                if (tbDataList.get(i).getDateTime().isAfter(tbDataList.get(i - 1).getDateTime().minusMinutes(10))) {
                    // if current measurement is less than 10 minutes before the next measurement,
                    // add the current toothbrush time to the next tooth brush time
                    tbDataList.get(i - 1).setTbSecs(tbDataList.get(i - 1).getTbSecs() + tbDataList.get(i).getTbSecs());
                } else {
                    // if current measurement is more than 10 minutes before the next measurement,
                    // consider it as a separate measurement, and add it to the clean data list
                    tbCleanDataList.add(tbDataList.get(i));
                }
            }
        } // Når vi bevæger os baglæns i listen, vil det betyde, at datapunkterne bliver lagt i tbCleanDataList i den modsatte rækkefølge end de kom ind, så det ældste målepunkt nu ligger på indeksplads 0 i listen

        return tbCleanDataList;
    }
}
