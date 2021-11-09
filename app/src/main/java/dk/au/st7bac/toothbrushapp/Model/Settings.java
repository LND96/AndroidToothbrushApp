package dk.au.st7bac.toothbrushapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Settings {
    // overvej settere og kriterier for det



    @PrimaryKey(autoGenerate = true)
    private int id; // kan andet bruges som id?
    private String sensorId;
    private String apiSince;
    private String apiLimit;
    private double offset; // hardware offset
    private int minMeasurementDuration; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private int maxMeasurementDuration; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private int minAccpTbTime; // minimum time in secs that a tooth brushing should last to be accepted
    @TypeConverters(Converters.class)
    private LocalTime morningToEveningTime; // time of day where morning transitions to evening
    @TypeConverters(Converters.class)
    private LocalTime eveningToMorningTime; // time of day where evening transitions to morning
    private int tbEachDay; // ideal number of tooth brushes each day
    private double numTbThres; // threshold value for minimum number of tooth brushes compared to ideal number of tooth brushes
    private int timeBetweenMeasurements; // maximum time in minutes between two measurements for them to be counted as one
    private int numIntervalDays; // number of days in interval
    @TypeConverters(Converters.class)
    private LocalDate lastDayInInterval; // // the last day of the time interval the calculations are made over

    public Settings() {

    }

    public Settings(String sensorId, String apiSince, String apiLimit, double offset,
                    int minMeasurementDuration, int maxMeasurementDuration,
                    int minAccpTbTime, String morningToEveningTime,
                    String eveningToMorningTime, int tbEachDay, double numTbThres,
                    int timeBetweenMeasurements, int numIntervalDays, String lastDayInInterval) {
        this.sensorId = sensorId;
        this.apiSince = apiSince;
        this.apiLimit = apiLimit;
        if (this.apiLimit.equals("0")) {
            this.apiLimit = "1";
        }
        this.offset = offset;
        this.minMeasurementDuration = minMeasurementDuration;
        this.maxMeasurementDuration = maxMeasurementDuration;
        this.minAccpTbTime = minAccpTbTime;
        this.morningToEveningTime = LocalTime.parse(morningToEveningTime);
        this.eveningToMorningTime = LocalTime.parse(eveningToMorningTime);
        this.tbEachDay = tbEachDay;
        this.numTbThres = numTbThres;
        this.timeBetweenMeasurements = timeBetweenMeasurements;
        this.numIntervalDays = numIntervalDays;
        if (lastDayInInterval.equals("now")) { // hvilke andre muligheder skal der være og hvordan skal det håndteres?
            this.lastDayInInterval = LocalDate.now();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getApiSince() {
        return apiSince;
    }

    public String getApiLimit() {
        return apiLimit;
    }

    public double getOffset() {
        return offset;
    }

    public int getMinMeasurementDuration() {
        return minMeasurementDuration;
    }

    public int getMaxMeasurementDuration() {
        return maxMeasurementDuration;
    }

    public int getMinAccpTbTime() {
        return minAccpTbTime;
    }

    public LocalTime getMorningToEveningTime() {
        return morningToEveningTime;
    }

    public LocalTime getEveningToMorningTime() {
        return eveningToMorningTime;
    }

    public int getTbEachDay() {
        return tbEachDay;
    }

    public double getNumTbThres() {
        return numTbThres;
    }

    public int getTimeBetweenMeasurements() {
        return timeBetweenMeasurements;
    }

    public int getNumIntervalDays() {
        return numIntervalDays;
    }

    public LocalDate getLastDayInInterval() {
        return lastDayInInterval;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void setApiSince(String apiSince) {
        this.apiSince = apiSince;
    }

    public void setApiLimit(String apiLimit) {
        this.apiLimit = apiLimit;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void setMinMeasurementDuration(int minMeasurementDuration) {
        this.minMeasurementDuration = minMeasurementDuration;
    }

    public void setMaxMeasurementDuration(int maxMeasurementDuration) {
        this.maxMeasurementDuration = maxMeasurementDuration;
    }

    public void setMinAccpTbTime(int minAccpTbTime) {
        this.minAccpTbTime = minAccpTbTime;
    }

    public void setMorningToEveningTime(LocalTime morningToEveningTime) {
        this.morningToEveningTime = morningToEveningTime;
    }

    public void setEveningToMorningTime(LocalTime eveningToMorningTime) {
        this.eveningToMorningTime = eveningToMorningTime;
    }

    public void setTbEachDay(int tbEachDay) {
        this.tbEachDay = tbEachDay;
    }

    public void setNumTbThres(double numTbThres) {
        this.numTbThres = numTbThres;
    }

    public void setTimeBetweenMeasurements(int timeBetweenMeasurements) {
        this.timeBetweenMeasurements = timeBetweenMeasurements;
    }

    public void setNumIntervalDays(int numIntervalDays) {
        this.numIntervalDays = numIntervalDays;
    }

    public void setLastDayInInterval(LocalDate lastDayInInterval) {
        this.lastDayInInterval = lastDayInInterval;
    }

}
