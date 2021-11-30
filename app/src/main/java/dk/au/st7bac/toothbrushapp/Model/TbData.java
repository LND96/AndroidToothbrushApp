package dk.au.st7bac.toothbrushapp.Model;


import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

@Entity
public class TbData {

    @PrimaryKey
    @NonNull
    private final String rawTelemetry;
    private final String sysId;
    private final int tbVal;
    private double tbSecs;
    @TypeConverters(Converters.class)
    private LocalDateTime dateTime;
    private final int tbHb;
    private int epoch;

    public TbData(String sysId, int tbVal, double tbSecs, @NotNull String rawTelemetry, int tbHb,
                  LocalDateTime dateTime, int epoch) {
        this.sysId = sysId;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
        this.rawTelemetry = rawTelemetry;
        this.tbHb = tbHb;
        this.dateTime = dateTime;
        this.epoch = epoch;
    }

    public String getSysId() {
        return sysId;
    }

    public int getTbVal() {
        return tbVal;
    }

    public double getTbSecs() {
        return tbSecs;
    }

    public void setTbSecs(double tbSecs) {
        // Math.max returns the greater of the two values: https://www.tutorialspoint.com/java/lang/math_max_int.htm
        this.tbSecs = Math.max(tbSecs, 0.0);
    }

    @NotNull
    public String getRawTelemetry() {
        return rawTelemetry;
    }

    public int getTbHb() {
        return tbHb;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }
}
