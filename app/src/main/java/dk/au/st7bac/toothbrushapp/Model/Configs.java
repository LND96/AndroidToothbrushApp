package dk.au.st7bac.toothbrushapp.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Configs {
    private final String apiSince; // get api data since this date
    private final String apiLimitFirstRun; // number of data packages that should be retrieved at an api call first time the code is run
    private String apiLimit; // number of data packages that should be retrieved at an api after the first time the code is run
    private final double offset; // hardware offset
    private final int minMeasurementDuration; // minimum time in secs that a measurement should last to be considered as a tooth brushing
    private final int maxMeasurementDuration; // maximum time in secs that a measurement should last to be considered as a tooth brushing
    private final LocalTime morningToEveningTime; // time of day where morning transitions to evening
    private final LocalTime eveningToMorningTime; // time of day where evening transitions to morning
    private final int timeBetweenMeasurements; // maximum time in minutes between two measurements for them to be counted as one
    private LocalDate lastDayInInterval; // the last day of the time interval the calculations are made over
    private final String dataProcessor; // type of dataProcessor
    private final int tbEachDay; // ideal number of tooth brushes each day
    private final double numTbThres; // threshold value for minimum number of tooth brushes compared to ideal number of tooth brushes
    private final int numIntervalDays; // number of days in interval
    private final int minAccpTbTime; // minimum time in secs that a tooth brushing should last to be accepted
    private final int daysWithoutTb; // accepted number of days without tb before notification is activated
    private final String sensorId; // sensor id


    public Configs(String apiSince, String apiLimitFirstRun, String apiLimit, double offset,
                   int minMeasurementDuration, int maxMeasurementDuration,
                   String morningToEveningTime, String eveningToMorningTime,
                   int timeBetweenMeasurements, String lastDayInInterval, String dataProcessor,
                   int tbEachDay, double numTbThres, int numIntervalDays, int minAccpTbTime,
                   int daysWithoutTb, String sensorId) {
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
        this.dataProcessor = dataProcessor;
        this.tbEachDay = tbEachDay;
        this.numTbThres = numTbThres;
        this.numIntervalDays = numIntervalDays;
        this.minAccpTbTime = minAccpTbTime;
        this.daysWithoutTb = daysWithoutTb;
        this.sensorId = sensorId;

        switch (lastDayInInterval.toLowerCase()) {
            case ("now"):
                this.lastDayInInterval = LocalDate.now();
        }

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

    public String getDataProcessor() {
        return dataProcessor;
    }

    public String getApiLimitFirstRun() {
        return apiLimitFirstRun;
    }

    public int getTbEachDay() {
        return tbEachDay;
    }

    public double getNumTbThres() {
        return numTbThres;
    }

    public int getNumIntervalDays() {
        return numIntervalDays;
    }

    public int getMinAccpTbTime() {
        return minAccpTbTime;
    }

    public int getDaysWithoutTb() {
        return daysWithoutTb;
    }

    public String getSensorId() {
        return sensorId;
    }
}
