package dk.au.st7bac.toothbrushapp.Model;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataCalculator {
    private final int timeTbThreshold;
    private final int days;
    private final int tbEachDay;
    private final LocalTime morningToEveningTime;
    private final LocalTime eveningToMorningTime;
    private final double numTbThreshold;
    private final LocalDate lastDayOfInterval;

    private boolean[] morningAndEveningOK;
    private boolean[] morningAndEveningTimeOK;
    private String[] dateStrings;


    public DataCalculator(int timeTbThreshold, int days, int tbEachDay,
                          LocalTime morningToEveningTime, LocalTime eveningToMorningTime,
                          double numTbThreshold, LocalDate lastDayOfInterval) {
        this.timeTbThreshold = timeTbThreshold;
        this.days = days;
        this.tbEachDay = tbEachDay;
        this.morningToEveningTime = morningToEveningTime;
        this.eveningToMorningTime = eveningToMorningTime;
        this.numTbThreshold = numTbThreshold;
        this.lastDayOfInterval = lastDayOfInterval;
    }

    public TbStatus processData(List<TbData> TbDataList)
    {
        // calculate number of tooth brushes
        int numTbCompleted = calcNumOfTb(TbDataList);

        // calculate ideal number of tooth brushes
        int totalNumberTb = days * tbEachDay;

        // calculate if number of tooth brushes is ok
        boolean isNumTbOK = isNumTbOk(numTbCompleted, totalNumberTb);

        // calculate average tb time
        int avgTbTime = calcAvgTime(TbDataList);

        // calculate if average tb time is ok
        boolean isAVgTimeTbOK = isAvgTimeTbOK(avgTbTime);

        // create list for days of week
        dateStrings = new String[days];

        // create list of dates in interval
        List<LocalDate> dateList = createDateList(days);

        // create boolean arrays for tb and tb time morning and evening with default value false
        morningAndEveningOK = new boolean[days * tbEachDay];
        morningAndEveningTimeOK = new boolean[days * tbEachDay];

        // check if tb each day is done morning and evening and if the tb time is ok
        isMorningAndEveningTbOk(TbDataList, tbEachDay, dateList);

        //save all results in tbStatus object
        return new TbStatus(dateStrings, morningAndEveningOK, morningAndEveningTimeOK, numTbCompleted, totalNumberTb, avgTbTime, isNumTbOK, isAVgTimeTbOK);

    }

    //Calculate #tb (length of TBDataList)
    private int calcNumOfTb(List<TbData> TBDataList)
    {
        return TBDataList.size();
    }

    //Calculate if numbers of tb is ok
    private boolean isNumTbOk(int numTbCompleted, int totalNumberTb)
    {
        // calculate fraction of completed tb compared to ideal number of tb
        double fracTb = numTbCompleted / (totalNumberTb + 0.0);

        //return true if fraction of tb's is higher than threshold, else return false.
        return fracTb > numTbThreshold;
    }

    //Calculate average time
    private int calcAvgTime(List<TbData> TBDataList) {
        int sumTime = 0;
        for (int i = 0; i < TBDataList.size(); i++) {
            sumTime += TBDataList.get(i).getTbSecs();
        }

        if (TBDataList.size() == 0) {
            return 0;
        } else {
            return (int) (sumTime/TBDataList.size());
        }
    }

    //Calculate if average time of tb is ok
    private boolean isAvgTimeTbOK(int avgBrushTime)
    {
        //return true if avgTime is higher than or equal to threshold, else return false.
        return avgBrushTime >= timeTbThreshold;
    }

    //create list with the last x days.
    private List<LocalDate> createDateList(int days) {

        List<LocalDate> dateList = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate date = lastDayOfInterval.minusDays(i);
            dateList.add(date);
            Array.set(dateStrings, i, date.format(DateTimeFormatter.ofPattern("dd/MM")));
        }

        return dateList;
    }

    //Update morningAndEveningOK and morningAndEveningTimeOK (true if there is a tb event and time is ok)
    private void isMorningAndEveningTbOk(List<TbData> TBDataList, int tbEachDay, List<LocalDate> dateList)
    {
        //Not implemented if there is more or less than 2 tb each day
        if (tbEachDay == 2)
        {
            for (int i = 0; i < TBDataList.size(); i++) {
                for (int j = 0; j < dateList.size(); j++)
                {
                    //https://howtodoinjava.com/java/date-time/localdate-localdatetime-conversions/
                    // check if date for current tb is equal to j'th date in date list
                    if (TBDataList.get(i).getDateTime().toLocalDate().isEqual(dateList.get(j))) {

                        // check if time of current tb is in the morning time interval
                        if (eveningToMorningTime.isBefore(TBDataList.get(i).getDateTime().toLocalTime()) &&
                                morningToEveningTime.isAfter(TBDataList.get(i).getDateTime().toLocalTime())) {

                            //https://stackoverflow.com/questions/4352885/how-do-i-update-the-element-at-a-certain-position-in-an-arraylist
                            //x' element in boolean array is updated to true (morning)
                            Array.setBoolean(morningAndEveningOK, 2*j, true);                                                        // test at den plads som værdien bliver lagt på i denne liste passer med den rigtige dato

                            //Update morningAndEveningTimeOK list with true if the time of tb is accepted
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {
                                Array.setBoolean(morningAndEveningTimeOK, 2*j, true);
                            }
                        } else {
                            //x' element in boolean array is updated to true (evening)
                            Array.setBoolean(morningAndEveningOK, (2*j)+1, true);

                            //Update morningAndEveningTimeOK list with true if the time of tb is accepted
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {
                                Array.setBoolean(morningAndEveningTimeOK, (2*j)+1, true);
                            }
                        }
                    }
                }
            }
        }
    }
}











