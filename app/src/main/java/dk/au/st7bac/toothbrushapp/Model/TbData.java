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

    public TbData(String sysId, String dateTimeString, int tbVal, double tbSecs, LocalDateTime dateTime) {
        this.sysId = sysId;
        this.dateTimeString = dateTimeString;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
