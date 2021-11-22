package dk.au.st7bac.toothbrushapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.LocalTime;

public class Configs {
    // overvej settere og kriterier for det

    //private String sensorId;
    private String apiSince; // get api data since this date
    private String apiLimitFirstRun; // number of data packages that should be retrieved at an api call first time the code is run
    private String apiLimit; // number of data packages that should be retrieved at an api after the first time the code is run
    private double offset; // hardware offset
    private int minMeasurementDuration; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private int maxMeasurementDuration; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private LocalTime morningToEveningTime; // time of day where morning transitions to evening
    private LocalTime eveningToMorningTime; // time of day where evening transitions to morning
    private int timeBetweenMeasurements; // maximum time in minutes between two measurements for them to be counted as one
    private LocalDate lastDayInInterval; // // the last day of the time interval the calculations are made over

    private String dataProcessor;

    public Configs(String apiSince, String apiLimitFirstRun, String apiLimit, double offset,
                   int minMeasurementDuration, int maxMeasurementDuration,
                   String morningToEveningTime, String eveningToMorningTime,
                   int timeBetweenMeasurements, String lastDayInInterval, String dataProcessor) {
        this.apiSince = apiSince;
        this.apiLimitFirstRun = apiLimitFirstRun;
        this.apiLimit = apiLimit;
        if (this.apiLimit.equals("0")) {
            this.apiLimit = "1";
        }
        this.offset = offset;
        this.minMeasurementDuration = minMeasurementDuration;
        this.maxMeasurementDuration = maxMeasurementDuration;
        this.morningToEveningTime = LocalTime.parse(morningToEveningTime);
        this.eveningToMorningTime = LocalTime.parse(eveningToMorningTime);
        this.timeBetweenMeasurements = timeBetweenMeasurements;
        if (lastDayInInterval.equals("now")) { // hvilke andre muligheder skal der være og hvordan skal det håndteres?
            this.lastDayInInterval = LocalDate.now();
        }
        this.dataProcessor = dataProcessor;
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

    public LocalTime getMorningToEveningTime() {
        return morningToEveningTime;
    }

    public LocalTime getEveningToMorningTime() {
        return eveningToMorningTime;
    }

    public int getTimeBetweenMeasurements() {
        return timeBetweenMeasurements;
    }

    public LocalDate getLastDayInInterval() {
        return lastDayInInterval;
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

    public void setMorningToEveningTime(LocalTime morningToEveningTime) {
        this.morningToEveningTime = morningToEveningTime;
    }

    public void setEveningToMorningTime(LocalTime eveningToMorningTime) {
        this.eveningToMorningTime = eveningToMorningTime;
    }

    public void setTimeBetweenMeasurements(int timeBetweenMeasurements) {
        this.timeBetweenMeasurements = timeBetweenMeasurements;
    }

    public void setLastDayInInterval(LocalDate lastDayInInterval) {
        this.lastDayInInterval = lastDayInInterval;
    }

    public String getDataProcessor() {
        return dataProcessor;
    }

    public void setDataProcessor(String dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public String getApiLimitFirstRun() {
        return apiLimitFirstRun;
    }

    public void setApiLimitFirstRun(String apiLimitFirstRun) {
        this.apiLimitFirstRun = apiLimitFirstRun;
    }

}
