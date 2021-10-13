package dk.au.st7bac.toothbrushapp.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Processor {

    private ArrayList<TbData> tbProcessorList;

    private double numTbThreshold = 0.8; //80 % is ok
    private int timeTbThreshold = 90; //90 sec is ok

    private LocalTime morningToEveningTime = LocalTime.parse("11:59:00.000");
    private LocalTime eveningToMorningTime = LocalTime.parse("00:00:00.000");

    private ArrayList<LocalDate> dateList = new ArrayList<>();


    public ArrayList<TbData> ProcessData(ArrayList<TbData> TBDataList, int days, int tbEachDay)
    {
        int tbDays = tbDays(TBDataList);

        isAvgNumTbOk(tbDays, days, tbEachDay);

        int avgTime = avgTime(TBDataList);

        isAvgTimeTbOK(avgTime);

        isMorningAndEveningOk(TBDataList, days, tbEachDay);

        isMorningAndEveningTimeOk(TBDataList, days, tbEachDay);


        //gemme alle resultaterne i et TBstatus objekt - dette mangler!
        return tbProcessorList;

        String[] headerStrings = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"}; // hardcoded værdier
        TbStatus tbStatus = new TbStatus(headerStrings, );

        testData = new TbStatus(headerStrings, isToothbrushDoneMorning,
                isTimeOkMorning, isToothbrushDoneEvening, isTimeOkEvening, toothbrushesCompleted,
                totalNumberToothbrushes, avgBrushTime, isAvgNumberToothbrushesOk, isAvgTimeOk);


    }

    //Calculate #tb (length of TBDataList)
    public int tbDays (ArrayList<TbData> TBDataList)
    {
        return TBDataList.size();
    }

    //Calculate if average numbers of tb is ok
    public boolean isAvgNumTbOk(int tbDays, int days, int tbEachDay)
    {
        double AvgNumTb = tbDays / (days * tbEachDay + 0.0);

        //return true if average numbers of Tb is higher than threshold, else return false.
        return AvgNumTb > numTbThreshold;
    }

    //calculate average time
    private int avgTime(ArrayList<TbData> TBDataList) {
        double sumTime = 0;
        for (int i = 0; i < TBDataList.size(); i++) {
            sumTime += TBDataList.get(i).getTbSecs();
        }

        return (int) (sumTime/TBDataList.size());
    }

    //calculate if average time of tb is ok
    private boolean isAvgTimeTbOK(int avgTime)
    {
        //return true if avgTime is higher than threshold, else return false.
        return avgTime > timeTbThreshold;
    }



    //update morningAndEveningOK (true if there is a tb event.)
    private ArrayList<Boolean> isMorningAndEveningOk(ArrayList<TbData> TBDataList, int days, int tbEachDay)
    {

        //create list with the last x days. - dette kan evt. blive lagt i en metode for sig...
        LocalDate today = LocalDate.now();
        dateList.add(today);

        for (int l = 0; l < days-1; l++) {
            dateList.add(today.minusDays(l+1));
        }

        //kan disse lister initieres smartere? uden at lave et for loop..
        ArrayList<Boolean> morningAndEveningOK = new ArrayList<>();
        for (int k = 0; k < days*2; k++) {
            morningAndEveningOK.add(false);
        }

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
                            morningAndEveningOK.set(2*j, true);

                            /*
                            //Update morningAndEveningTimeOK list with true if the time of each tb is accepted.
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold ) {
                                morningAndEveningTimeOK.set(2*j, true);
                            }

                             */


                        } else {
                            //x' element in list is updated with true (evening)
                            morningAndEveningOK.set((2*j)+1, true);

                            /*
                            //Update morningAndEveningTimeOK list with true if the time of each tb is accepted.
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold ) {
                                morningAndEveningTimeOK.set((2*j)+1, true);
                            }

                             */
                        }
                    }
                }

            }
        }

        return morningAndEveningOK;

    }

    //update morningAndEveningTimeOK (true if time for the tb event is ok)
    private ArrayList<Boolean> isMorningAndEveningTimeOk(ArrayList<TbData> TBDataList, int days, int tbEachDay) {

        //create list with the last x days. - dette kan evt. blive lagt i en metode for sig...
        LocalDate today = LocalDate.now();
        dateList.add(today);

        for (int l = 0; l < days - 1; l++) {
            dateList.add(today.minusDays(l + 1));
        }

        //kan listen initieres smartere?
        //create list with x elements
        ArrayList<Boolean> morningAndEveningTimeOK = new ArrayList<>();
        for (int k = 0; k < days * 2; k++) {
            morningAndEveningTimeOK.add(false);
        }

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
                                morningAndEveningTimeOK.set(2 * j, true);
                            }


                        } else {

                            //Update morningAndEveningTimeOK list with true if the time of each tb is accepted.
                            if (TBDataList.get(i).getTbSecs() > timeTbThreshold) {
                                morningAndEveningTimeOK.set((2 * j) + 1, true);
                            }
                        }
                    }
                }

            }

        }
        return morningAndEveningTimeOK;
    }
}











