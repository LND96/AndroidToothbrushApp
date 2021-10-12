package dk.au.st7bac.toothbrushapp.Model;


import java.time.LocalDateTime;
import java.util.Date;


public class TbData {

    // Der er lavet public gettere og settere for alle parametre, men overvej om set skal være private!

    private String sysId;
    private String dateTimeString;
    private int tbVal;
    private double tbSecs;
    private LocalDateTime dateTime;
    private String rawTelemetry;
    private int tbHb;

    public TbData(String sysId, String dateTimeString, int tbVal, double tbSecs, String rawTelemetry, int tbHb, LocalDateTime dateTime) {
        this.sysId = sysId;
        this.dateTimeString = dateTimeString;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
        this.rawTelemetry = rawTelemetry;
        this.tbHb = tbHb;
        this.dateTime = dateTime;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
