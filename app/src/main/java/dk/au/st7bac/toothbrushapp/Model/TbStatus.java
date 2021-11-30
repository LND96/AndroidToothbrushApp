package dk.au.st7bac.toothbrushapp.Model;

public class TbStatus {
    private final String[] headerStrings; // string array for dates in interval
    private final boolean[] isTbDone; // boolean array for tb status each morning and evening in interval
    private final boolean[] isTimeOk; // boolean array for tb time status each morning and evening in interval
    private final int numTbCompleted; // number of tb completed in the interval
    private final int totalNumTb; // ideal number of tb in interval
    private final int avgTbTime; // average tb time
    private final boolean isAvgNumTbOk; // evaluation of numbers of tb's is ok
    private final boolean isAvgTimeOk; // evaluation of average tb time is ok
    private final int numEveningOk; // number of tb completed in the morning
    private final int numMorningOk; // number of tb completed in the evening
    private final int numIntervalDays; // number of days in interval

    public TbStatus(String[] headerStrings, boolean[] isTbDone, boolean[] isTimeOk,
                    int numTbCompleted, int totalNumTb, int avgTbTime, boolean isAvgNumTbOk,
                    boolean isAvgTimeOk, int numEveningOk, int numMorningOk, int numIntervalDays) {
        this.headerStrings = headerStrings;
        this.isTbDone = isTbDone;
        this.isTimeOk = isTimeOk;
        this.numTbCompleted = numTbCompleted;
        this.totalNumTb = totalNumTb;
        this.avgTbTime = avgTbTime;
        this.isAvgNumTbOk = isAvgNumTbOk;
        this.isAvgTimeOk = isAvgTimeOk;
        this.numEveningOk = numEveningOk;
        this.numMorningOk = numMorningOk;
        this.numIntervalDays = numIntervalDays;
    }

    public String[] getHeaderStrings() {
        return headerStrings;
    }

    public boolean[] getIsTbDone() {
        return isTbDone;
    }

    public boolean[] getIsTimeOk() {
        return isTimeOk;
    }

    public int getNumTbCompleted() {
        return numTbCompleted;
    }

    public int getTotalNumTb() {
        return totalNumTb;
    }

    public int getAvgTbTime() {
        return avgTbTime;
    }

    public boolean isAvgNumTbOk() {
        return isAvgNumTbOk;
    }

    public boolean isAvgTimeOk() {
        return isAvgTimeOk;
    }

    public int getNumEveningOk() {
        return numEveningOk;
    }

    public int getNumMorningOk() {
        return numMorningOk;
    }

    public int getNumIntervalDays() {
        return numIntervalDays;
    }
}
