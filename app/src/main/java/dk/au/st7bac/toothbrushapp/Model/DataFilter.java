package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {

    String prev_rawTelemetry_last26 = "";
    String curr_rawTelemetry_last26 = "";
    private final double offset;
    private final int minTime;
    private final int maxTime;

    public DataFilter(double offset, int minTime, int maxTime) {
        this.offset = offset;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public List<TbData> FilterData(List<TbData> TbDataList)
    {
        List<TbData> tbFilterDataList = new ArrayList<>();

        for (int i = 0; i < TbDataList.size(); ++i) {

            if (TbDataList.get(i).getTbVal() == 0 && TbDataList.get(i).getTbHb() == 0)
            {
                // subtract offset from measurement and set time
                TbDataList.get(i).setTbSecs(TbDataList.get(i).getTbSecs() - offset); // har ændret fra 6 til offset

                // only measurements above minTime are considered as a toothbrushing
                if (TbDataList.get(i).getTbSecs() > minTime) // har ændret fra 10 til minTime
                {
                    prev_rawTelemetry_last26 = curr_rawTelemetry_last26;
                    //https://howtodoinjava.com/java/string/get-last-4-characters/
                    curr_rawTelemetry_last26 = TbDataList.get(i).getRawTelemetry()
                            .substring(TbDataList.get(i).getRawTelemetry().length()-26);
                    //TbDataList.get(i).setRawTelemetry(curr_rawTelemetry_last26);

                    if (i==0)
                    {
                        //save element in tbFilterDataList (must be returned)
                        tbFilterDataList.add(TbDataList.get(i));
                    }
                    else if (!curr_rawTelemetry_last26.equals(prev_rawTelemetry_last26)) // checks if the previous measurement is identical to the current measurement
                    {
                        //if lower than 600 s
                        if (TbDataList.get(i).getTbSecs() < maxTime) // har ændret fra 600 til maxTime
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
