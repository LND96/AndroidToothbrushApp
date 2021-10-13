package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;

public class DataFilter {

    private ArrayList<TbData> tbFilterDataList;
    String prev_rawTelemetry_last26 = "";
    String curr_rawTelemetry_last26 = "";

    public ArrayList<TbData> FilterData(ArrayList<TbData> TBDataList)
    {

        //Need to initiate list, if no data pass the criteria.
        ArrayList<TbData> tbFilterDataList = new ArrayList<>();

        for (int i = 0; i < TBDataList.size(); ++i) {

            if (TBDataList.get(i).getTbVal() == 0 && TBDataList.get(i).getTbHb() == 0)
            {
                TBDataList.get(i).setTbSecs(TBDataList.get(i).getTbSecs()-6);

                if (TBDataList.get(i).getTbSecs()>10)
                {
                    prev_rawTelemetry_last26 = curr_rawTelemetry_last26;
                    //https://howtodoinjava.com/java/string/get-last-4-characters/
                    curr_rawTelemetry_last26 = TBDataList.get(i).getRawTelemetry()
                            .substring(TBDataList.get(i).getRawTelemetry().length()-26);
                    //TBDataList.get(i).setRawTelemetry(curr_rawTelemetry_last26);

                    if (i==0)
                    {
                        //save element in tbFilterDataList (must be returned)
                        tbFilterDataList.add(TBDataList.get(i));
                    }

                    else if (!curr_rawTelemetry_last26.equals(prev_rawTelemetry_last26) )
                    {
                        //if lower than 600 s
                        if (TBDataList.get(i).getTbSecs()<600)
                        {
                            //save element in tbFilterDataList (must be returned)
                            tbFilterDataList.add(TBDataList.get(i));
                        }
                    }
                }
            }
        }

        return tbFilterDataList;
    }
}
