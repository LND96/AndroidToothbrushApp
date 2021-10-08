package dk.au.st7bac.toothbrushapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TbData {

    private String sysId;
    private String dateTime;
    private int tbVal;
    private double tbSecs;

    public TbData(String sysId, String dateTime, int tbVal, double tbSecs) {
        this.sysId = sysId;
        this.dateTime = dateTime;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
    }

}
