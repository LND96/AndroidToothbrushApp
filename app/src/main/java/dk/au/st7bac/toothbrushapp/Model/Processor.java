package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;

public class Processor {

    private ArrayList<TbData> tbProcessorList;

    public ArrayList<TbData> ProcessData(ArrayList<TbData> TBDataList, int days, int tbEachDay)
    {
        //calculate #tb
        int tbDays = tbDays(TBDataList);

        //calculate % of days brushed
        tbDaysOK(tbDays, days, tbEachDay);

        //calculate the average time
        averageTime(TBDataList);

        return tbProcessorList;
    }


    public int tbDays (ArrayList<TbData> TBDataList)
    {
        int tbDays_count = TBDataList.size();
        return tbDays_count;
    }

    public boolean tbDaysOK(int tbDays, int days, int tbEachDay)
    {
        if(tbDays/(days*tbEachDay+0.0) > 0.8){
            return true;
        }
        else
            return false;
    }

    private void averageTime(ArrayList<TbData> TBDataList) {

    }

}



