package dk.au.st7bac.toothbrushapp.Model;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataProcessor {

    private List<TbData> tbProcessorList;

    private final int timeTbThreshold = 90; //90 sec is ok // bør sættes ved constructor injection

    private final LocalTime morningToEveningTime = LocalTime.parse("11:59:00.000");
    private final LocalTime eveningToMorningTime = LocalTime.parse("00:00:00.000");


    public TbStatus ProcessData(List<TbData> TBDataList, int days, int tbEachDay)
    {
        int numTbCompleted = tbDays(TBDataList);

        int totalNumberTb = days * tbEachDay;
        boolean isAvgNumTbOK = isAvgNumTbOk(numTbCompleted, totalNumberTb);

        int avgBrushTime = avgTime(TBDataList);

        boolean isAVgTimeTbOK = isAvgTimeTbOK(avgBrushTime);

        List<LocalDate> dateList = createDateList(days);
        boolean[] isTbDone = isMorningAndEveningOk(TBDataList, days, tbEachDay, dateList);

        boolean[] isTimeOk = isMorningAndEveningTimeOk(TBDataList, days, tbEachDay, dateList);

        String[] headerStrings = new String[]{"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier - hvor skal denne liste genereres?

        //save all results in tbStatus object
        TbStatus tbStatus = new TbStatus(headerStrings, isTbDone, isTimeOk, numTbCompleted, totalNumberTb, avgBrushTime, isAvgNumTbOK, isAVgTimeTbOK);
        return tbStatus;

    }

    //Calculate #tb (length of TBDataList)
    public int tbDays (List<TbData> TBDataList)
    {
        return TBDataList.size();
    }

    //Calculate if average numbers of tb is ok
    public boolean isAvgNumTbOk(int numTbCompleted, int totalNumberTb)
    {
        double AvgNumTb = numTbCompleted / totalNumberTb;
        double numTbThreshold = 0.8; //80 % is ok

        //return true if average numbers of Tb is higher than threshold, else return false.
        return AvgNumTb > numTbThreshold;
    }

    //Calculate average time
    private int avgTime(List<TbData> TBDataList) {
        double sumTime = 0;
        for (int i = 0; i < TBDataList.size(); i++) {
            sumTime += TBDataList.get(i).getTbSecs();
        }

        return (int) (sumTime/TBDataList.size());
    }

    //Calculate if average time of tb is ok
    private boolean isAvgTimeTbOK(int avgBrushTime)
    {
        //return true if avgTime is higher than threshold, else return false.
        return avgBrushTime > timeTbThreshold;
    }

    //create list with the last x days.
    private List<LocalDate> createDateList(int days) {

        List<LocalDate> dateList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        dateList.add(today);

        for (int l = 0; l < days-1; l++) {
            dateList.add(today.minusDays(l+1));
        }

        return dateList;
    }
    //Update morningAndEveningOK (true if there is a tb event.)
    private boolean[] isMorningAndEveningOk(List<TbData> TBDataList, int days, int tbEachDay, List<LocalDate> dateList)
    {

        boolean[] morningAndEveningOK = new boolean[days*2];

        //Hvad hvis det ikke er 2 dage, men 3 dage?...
        if (tbEachDay == 2)
        {
            for (int i = 0; i < TBDataList.size(); i++) {
                for (int j = 0; j < dateList.size(); j++)
                {
                    //https://howtodoinjava.com/java/date-time/localdate-localdatetime-conversions/

                    Log.d("datalist_date", TBDataList.get(i).getDateTime().toLocalDate().toString() + "datelist_date" + dateList.get(j));
                    Log.d("dates ", "" + (TBDataList.get(i).getDateTime().toLocalDate().isEqual(dateList.get(j)) ));

                    if (TBDataList.get(i).getDateTime().toLocalDate().isEqual(dateList.get(j))) {

                        //Morning
                        if (eveningToMorningTime.isBefore(TBDataList.get(i).getDateTime().toLocalTime()) &&
                                morningToEveningTime.isAfter(TBDataList.get(i).getDateTime().toLocalTime())) {

                            //https://stackoverflow.com/questions/4352885/how-do-i-update-the-element-at-a-certain-position-in-an-arraylist
                            //x' element in list is updated with true (morning)
                            Array.setBoolean(morningAndEveningOK, 2*j, true);


                        } else {
                            //x' element in list is updated with true (evening)
                            Array.setBoolean(morningAndEveningOK, (2*j)+1, true);
                        }
                    }
                }

            }
        }

        return morningAndEveningOK;

    }

    //update morningAndEveningTimeOK (true if time for the tb event is ok)
    private boolean[] isMorningAndEveningTimeOk(List<TbData> TBDataList, int days, int tbEachDay, List<LocalDate> dateList) {

        //create list with x elements
        boolean[] morningAndEveningTimeOK = new boolean[days*2];

        if (tbEachDay == 2) {
            for (int i = 0; i < TBDataList.size(); i++) {
                for (int j = 0; j < dateList.size(); j++) {
                    //https://howtodoinjava.com/java/date-time/localdate-localdatetime-conversions/

                    if (TBDataList.get(i).getDateTime().toLocalDate() == dateList.get(j)) {
                        //Morning
                        if (eveningToMorningTime.isBefore(TBDataList.get(i).getDateTime().toLocalTime()) &&
                                morningToEveningTime.isAfter(TBDataList.get(i).getDateTime().toLocalTime())) {

                            //Update morningAndEveningTimeOK list with true if the time of each tb is accepted.
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {

                                Array.setBoolean(morningAndEveningTimeOK, 2*j, true);
                            }


                        } else {

                            //Update morningAndEveningTimeOK list with true if the time of each tb is accepted.
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {
                                Array.setBoolean(morningAndEveningTimeOK, (2*j)+1, true);
                            }
                        }
                    }
                }

            }

        }
        return morningAndEveningTimeOK;
    }
}











