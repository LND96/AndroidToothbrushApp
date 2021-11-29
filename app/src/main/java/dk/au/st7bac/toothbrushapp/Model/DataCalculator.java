package dk.au.st7bac.toothbrushapp.Model;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataCalculator {
    private int timeTbThreshold; // minimum time in secs that a tb should last to be accepted
    private int days; // number of days in interval
    private int tbEachDay; // ideal number of tb's each day
    private final LocalTime morningToEveningTime; // time of day where morning transitions to evening
    private final LocalTime eveningToMorningTime; // time of day where evening transitions to morning
    private double numTbThreshold; // threshold value for minimum number of tb's compared to ideal number of tb's
    private final LocalDate lastDayOfInterval; // the last day of the time interval the calculations are made over
    private boolean[] morningAndEveningOK; // boolean array for tb status each morning and evening in interval
    private boolean[] morningAndEveningTimeOK; // boolean array for tb time status each morning and evening in interval
    private String[] dateStrings; // string array for dates in interval
    private int numMorningOk; // number of tb completed in the morning
    private int numEveningOk; // number of tb completed in the evening

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

    public void setTimeTbThreshold(int timeTbThreshold) {
        this.timeTbThreshold = timeTbThreshold;
    }

    public void setNumTbThreshold(double numTbThreshold) {
        this.numTbThreshold = numTbThreshold;
    }

    public void setTbEachDay (int tbEachDay) {
        this.tbEachDay = tbEachDay;
    }

    public void setDays (int days) {
        this.days = days;
    }

    // processes tb data to get tb status
    public TbStatus processData(List<TbData> TbDataList)
    {
        // create list for days of week
        dateStrings = new String[days];

        // create list of dates in interval
        List<LocalDate> dateList = createDateList(days);

        // create boolean arrays for tb and tb time morning and evening with default value false
        morningAndEveningOK = new boolean[days * tbEachDay];
        morningAndEveningTimeOK = new boolean[days * tbEachDay];

        // check if tb each day is done morning and evening and if the tb time is ok
        isMorningAndEveningTbOk(TbDataList, tbEachDay, dateList);

        // calculate number of tb's done morning and evening
        calcNumMorningEveningOk(morningAndEveningOK);

        // calculate number of tb's
        int numTbCompleted = calcNumOfTb(morningAndEveningOK);

        // calculate ideal number of tooth brushes
        int totalNumberTb = days * tbEachDay;

        // check if number of tb's is ok
        boolean isNumTbOK = isNumTbOk(numTbCompleted, totalNumberTb);

        // calculate average tb time
        int avgTbTime = calcAvgTime(TbDataList);

        // check if average tb time is ok
        boolean isAVgTimeTbOK = isAvgTimeTbOK(avgTbTime);

        // save results in tb status object
        return new TbStatus(dateStrings, morningAndEveningOK, morningAndEveningTimeOK,
                numTbCompleted, totalNumberTb, avgTbTime, isNumTbOK, isAVgTimeTbOK, numEveningOk,
                numMorningOk, days);
    }

    // calculates number of tb's done morning and evening
    private void calcNumMorningEveningOk(boolean[] morningAndEveningOK) {
        numMorningOk = 0;
        numEveningOk = 0;

        for (int i = 0; i < morningAndEveningOK.length; i++) {
            // if i is even and i'th element is true
            if (i % 2 == 0 && morningAndEveningOK[i])
            {
                numMorningOk++;
            }

            // if i is odd and i'th element is true
            else if (i % 2 != 0 && morningAndEveningOK[i]){
                numEveningOk ++;
            }
        }
    }

    // calculates number of tb's done
    private int calcNumOfTb(boolean[] morningAndEveningOK)
    {
        int numOfTb = 0;
        for (Boolean b : morningAndEveningOK) {
            // if each bool is true, count number of tb one up
            if (b) {
                numOfTb++;
            }
        }
        return numOfTb;
    }

    // checks if number of tb's is ok
    private boolean isNumTbOk(int numTbCompleted, int totalNumberTb)
    {
        // calculate fraction of completed tb compared to ideal number of tb
        double fracTb = numTbCompleted / (totalNumberTb + 0.0);

        //return true if fraction of tb's is higher than threshold value, else return false
        return fracTb > numTbThreshold;
    }

    // calculates average tb time
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

    // checks if average tb time is ok
    private boolean isAvgTimeTbOK(int avgBrushTime)
    {
        // return true if average tb time is higher than or equal to threshold value, else return false
        return avgBrushTime >= timeTbThreshold;
    }

    // creates list with dates in the interval
    private List<LocalDate> createDateList(int days) {
        List<LocalDate> dateList = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate date = lastDayOfInterval.minusDays(i);
            dateList.add(date);
            Array.set(dateStrings, i, date.format(DateTimeFormatter.ofPattern("dd/MM")));
        }

        return dateList;
    }

    // updates morningAndEveningOK and morningAndEveningTimeOK
    // (each element is true if there is a tb event and time is ok)
    private void isMorningAndEveningTbOk(List<TbData> TBDataList, int tbEachDay, List<LocalDate> dateList)
    {
        // not implemented if there is more or less than 2 tb each day
        if (tbEachDay == 2)
        {
            for (int i = 0; i < TBDataList.size(); i++) {
                for (int j = 0; j < dateList.size(); j++)
                {
                    // check if date for current tb is equal to j'th date in date list
                    if (TBDataList.get(i).getDateTime().toLocalDate().isEqual(dateList.get(j))) {

                        // check if time of current tb is in the morning
                        if (eveningToMorningTime.isBefore(TBDataList.get(i).getDateTime().toLocalTime()) &&
                                morningToEveningTime.isAfter(TBDataList.get(i).getDateTime().toLocalTime())) {

                            // x' element in boolean array is updated to true (morning)
                            Array.setBoolean(morningAndEveningOK, 2*j, true);

                            // update morningAndEveningTimeOK list with true if the time of tb is accepted
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {
                                Array.setBoolean(morningAndEveningTimeOK, 2*j, true);
                            }
                        } else {
                            // x' element in boolean array is updated to true (evening)
                            Array.setBoolean(morningAndEveningOK, (2*j)+1, true);

                            // update morningAndEveningTimeOK list with true if the time of tb is accepted
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











