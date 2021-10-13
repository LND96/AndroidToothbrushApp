package dk.au.st7bac.toothbrushapp.Model;


import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class TbData {

    // Der er lavet public gettere og settere for alle parametre, men overvej om set skal være private!

    //private int id; // er der andet der kan bruges som id?

    private String sysId;
    private String dateTimeString;
    private int tbVal;
    private double tbSecs;

    @TypeConverters(Converters.class)
    private LocalDateTime dateTime;

    @PrimaryKey
    @NonNull
    private String rawTelemetry;



    private int tbHb;
    private String epoch;

    public TbData(String sysId, String dateTimeString, int tbVal, double tbSecs, String rawTelemetry, int tbHb, LocalDateTime dateTime, String epoch) {
        this.sysId = sysId;
        this.dateTimeString = dateTimeString;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
        this.rawTelemetry = rawTelemetry;
        this.tbHb = tbHb;
        this.dateTime = dateTime;
        this.epoch = epoch;
    }

    /*
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

     */

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

    public int getTbHb() {
        return tbHb;
    }

    public void setTbHb(int tbHb) {
        this.tbHb = tbHb;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

}
