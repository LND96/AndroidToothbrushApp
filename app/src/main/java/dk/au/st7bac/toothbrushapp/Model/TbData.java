package dk.au.st7bac.toothbrushapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TbData {

    // Der er lavet public gettere og settere for alle parametre, men overvej om set skal være private!

    private String sysId;
    private String dateTime;
    private int tbVal;
    private double tbSecs;
    private String rawTelemetry;
    private int tbHb;

    public TbData(String sysId, String dateTime, int tbVal, double tbSecs, String rawTelemetry, int tbHb) {
        this.sysId = sysId;
        this.dateTime = dateTime;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
        this.rawTelemetry = rawTelemetry;
        this.tbHb = tbHb;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getTbVal() {
        return tbVal;
    }

    public void setTbVal(int tbVal) {
        this.tbVal = tbVal;
    }

    public double getTbSecs() {
        return tbSecs;
    }

    public void setTbSecs(double tbSecs) {
        this.tbSecs = tbSecs;
    }

    public String getRawTelemetry() {
        return rawTelemetry;
    }

    public void setRawTelemetry(String rawTelemetry) {
        this.rawTelemetry = rawTelemetry;
    }

    public void setTbHb(int tbHb) {
        this.tbHb = tbHb;
    }

    public double getTbHb() {
        return tbHb;
    }

}
