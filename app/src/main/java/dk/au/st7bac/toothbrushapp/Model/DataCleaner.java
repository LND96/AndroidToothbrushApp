package dk.au.st7bac.toothbrushapp.Model;

import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DataCleaner {

    private static final String TAG = "DataCleaner";
    private ArrayList<TbData> tbCleanDataList;
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<TbData> CleanData(ArrayList<TbData> tbDataList) {

        tbCleanDataList = new ArrayList<>();

        for (TbData tbData : tbDataList) {

            // get date time
            String dateString = tbData.getDateTimeString();
            // select relevant characters
            String dateSubString = dateString.substring(0, dateString.length() - 8); // Virker, men hvis pludselig dateString ændrer format (f.eks. hvis år kun skrives som YY) vil det ikke virke længere

            // convert to date time object
            // kilde: https://stackoverflow.com/questions/49598863/adding-time-in-hours-to-a-date-object-in-java
            LocalDateTime dateTime = LocalDateTime
                    .parse(dateSubString, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                    .plusHours(2); // LocalDateTime kræver Android 8.0 (API level 26) eller nyere så har ændret det i gradle filen



            // Virker ikke endnu...
            /*
            ZoneOffset offset = OffsetDateTime.now().getOffset(); // kilde: https://stackoverflow.com/questions/42468753/java-8-zoneoffset-how-to-get-current-system-utc-offset

            // kilde: https://stackoverflow.com/questions/49598863/adding-time-in-hours-to-a-date-object-in-java
            LocalDateTime localDateTime = LocalDateTime
                    .parse(dateSubString, DateTimeFormatter.ofPattern("yyyyMMddHHmm")); // LocalDateTime kræver Android 8.0 (API level 26) eller nyere så har ændret det i gradle filen

            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, offset);
             */

            // set new date time
            tbData.setDateTime(dateTime);
        }

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
