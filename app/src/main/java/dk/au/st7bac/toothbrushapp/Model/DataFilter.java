package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {

    private List<TbData> tbFilterDataList;
    String prev_rawTelemetry_last26 = "";
    String curr_rawTelemetry_last26 = "";
    private double offset;
    private int minTime;
    private int maxTime;

    public DataFilter(double offset, int minTime, int maxTime) {
        this.offset = offset;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public List<TbData> FilterData(List<TbData> TbDataList)
    {
        //Need to initiate list, if no data pass the criteria.
        tbFilterDataList = new ArrayList<>();

        for (int i = 0; i < TbDataList.size(); ++i) {

            if (TbDataList.get(i).getTbVal() == 0 && TbDataList.get(i).getTbHb() == 0)
            {
                // subtract offset from measurement and set time
                TbDataList.get(i).setTbSecs(TbDataList.get(i).getTbSecs() - offset);

                // only measurements above minTime are considered as a toothbrushing
                if (TbDataList.get(i).getTbSecs() > minTime)
                {
                    prev_rawTelemetry_last26 = curr_rawTelemetry_last26;
                    //https://howtodoinjava.com/java/string/get-last-4-characters/
                    curr_rawTelemetry_last26 = TbDataList.get(i).getRawTelemetry()
                            .substring(TbDataList.get(i).getRawTelemetry().length()-26);

                    //handel error from the hub
                    if(!curr_rawTelemetry_last26.equals(prev_rawTelemetry_last26))
                    {
                        //if lower than 600 s
                        if (TbDataList.get(i).getTbSecs() < maxTime)
                        {
                            //save element in tbFilterDataList (must be returned)
                            tbFilterDataList.add(TbDataList.get(i));
                        }
                    }
                }
            }
        }

        return tbFilterDataList;
    }
}
