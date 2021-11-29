package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;
import java.util.List;

// inspiration for getting last 4 characters of a string: https://howtodoinjava.com/java/string/get-last-4-characters/
public class DataFilter {

    String prev_rawTelemetry_last26 = "";
    String curr_rawTelemetry_last26 = "";
    private final double offset; // hardware offset in secs
    private final int minTime; // minimum time in secs that a measurement should last to be considered as a tb
    private final int maxTime; // maximum time in secs that a measurement should last to be considered as a tb

    public DataFilter(double offset, int minTime, int maxTime) {
        this.offset = offset;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    // removes measurements that are either heart beats, start events, shorter than minTime or longer than maxTime
    public List<TbData> filterData(List<TbData> TbDataList)
    {
        List<TbData> tbFilterDataList = new ArrayList<>();

        for (int i = 0; i < TbDataList.size(); ++i) {
            if (TbDataList.get(i).getTbVal() == 0 && TbDataList.get(i).getTbHb() == 0)
            {
                // subtract offset from measurement and set time
                TbDataList.get(i).setTbSecs(TbDataList.get(i).getTbSecs() - offset);

                // only measurements above minTime are considered as a tb-event
                if (TbDataList.get(i).getTbSecs() > minTime)
                {
                    prev_rawTelemetry_last26 = curr_rawTelemetry_last26;
                    curr_rawTelemetry_last26 = TbDataList.get(i).getRawTelemetry()
                            .substring(TbDataList.get(i).getRawTelemetry().length() - 26);

                    // handel bug from gateway
                    if(!curr_rawTelemetry_last26.equals(prev_rawTelemetry_last26))
                    {
                        // add element to tbFilterDataList if measurement is less than 600 s
                        if (TbDataList.get(i).getTbSecs() < maxTime)
                        {
                            tbFilterDataList.add(TbDataList.get(i));
                        }
                    }
                }
            }
        }

        return tbFilterDataList;
    }
}
