package dk.au.st7bac.toothbrushapp.Model;

public class TbStatus {
    private String[] headerStrings;
    private boolean[] isTbDone;
    private boolean[] isTimeOk;
    private int numTbCompleted;
    private int totalNumTb;
    private int avgTbTime;
    private boolean isAvgNumTbOk;
    private boolean isAvgTimeOk;
    private int numEveningOk;
    private int numMorningOk;

    public TbStatus(String[] headerStrings, boolean[] isTbDone, boolean[] isTimeOk,
                    int numTbCompleted, int totalNumTb, int avgTbTime, boolean isAvgNumTbOk,
                    boolean isAvgTimeOk, int numEveningOk, int numMorningOk) {
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
    }

    public String[] getHeaderStrings() {
        return headerStrings;
    }

    public void setHeaderStrings(String[] headerStrings) {
        this.headerStrings = headerStrings;
    }

    public boolean[] getIsTbDone() {
        return isTbDone;
    }

    public void setIsTbDone(boolean[] isTbDone) {
        this.isTbDone = isTbDone;
    }

    public boolean[] getIsTimeOk() {
        return isTimeOk;
    }

    public void setIsTimeOk(boolean[] isTimeOk) {
        this.isTimeOk = isTimeOk;
    }

    public int getNumTbCompleted() {
        return numTbCompleted;
    }

    public void setNumTbCompleted(int numTbCompleted) {
        this.numTbCompleted = numTbCompleted;
    }

    public int getTotalNumTb() {
        return totalNumTb;
    }

    public void setTotalNumTb(int totalNumTb) {
        this.totalNumTb = totalNumTb;
    }

    public int getAvgTbTime() {
        return avgTbTime;
    }

    public void setAvgTbTime(int avgTbTime) {
        this.avgTbTime = avgTbTime;
    }

    public boolean isAvgNumTbOk() {
        return isAvgNumTbOk;
    }

    public void setAvgNumTbOk(boolean avgNumTbOk) {
        isAvgNumTbOk = avgNumTbOk;
    }

    public boolean isAvgTimeOk() {
        return isAvgTimeOk;
    }

    public void setAvgTimeOk(boolean avgTimeOk) {
        isAvgTimeOk = avgTimeOk;
    }

    public int getNumEveningOk() {
        return numEveningOk;
    }

    public void setNumEveningOk(int numEveningOk) {
        this.numEveningOk = numEveningOk;
    }

    public int getNumMorningOk() {
        return numMorningOk;
    }

    public void setNumMorningOk(int numMorningOk) {
        this.numMorningOk = numMorningOk;
    }
}
