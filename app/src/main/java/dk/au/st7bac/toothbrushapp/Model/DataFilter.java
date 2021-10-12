package dk.au.st7bac.toothbrushapp.Model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {

    private ArrayList<TbData> tbFilterDataList;
    String rawTelemetry_last26 = "";

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
                    //https://howtodoinjava.com/java/string/get-last-4-characters/
                    rawTelemetry_last26 = TBDataList.get(i).getRawTelemetry()
                            .substring(TBDataList.get(i).getRawTelemetry().length()-26);
                    TBDataList.get(i).setRawTelemetry(rawTelemetry_last26);

                    if (i==0)
                    {
                        //save element in tbFilterDataList (must be returned)
                        tbFilterDataList.add(TBDataList.get(i));
                    }

                    else if (!TBDataList.get(i).getRawTelemetry().equals(TBDataList.get(i - 1).getRawTelemetry()) )
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
