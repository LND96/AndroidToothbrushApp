package dk.au.st7bac.toothbrushapp.Model;

public class TbStatus {
    private String[] headerStrings;
    private boolean[] isToothbrushDoneMorning;
    private boolean[] isTimeOkMorning;
    private boolean[] isToothbrushDoneEvening;
    private boolean[] isTimeOkEvening;
    private int toothbrushesCompleted;
    private int totalNumberToothbrushes;
    private int avgBrushTime;
    private boolean isAvgNumberToothbrushesOk;
    private boolean isAvgTimeOk;

    public TbStatus(String[] headerStrings, boolean[] isToothbrushDoneMorning,
                    boolean[] isTimeOkMorning, boolean[] isToothbrushDoneEvening,
                    boolean[] isTimeOkEvening, int toothbrushesCompleted,
                    int totalNumberToothbrushes, int avgBrushTime,
                    boolean isAvgNumberToothbrushesOk, boolean isAvgTimeOk) {
        this.headerStrings = headerStrings;
        this.isToothbrushDoneMorning = isToothbrushDoneMorning;
        this.isTimeOkMorning = isTimeOkMorning;
        this.isToothbrushDoneEvening = isToothbrushDoneEvening;
        this.isTimeOkEvening = isTimeOkEvening;
        this.toothbrushesCompleted = toothbrushesCompleted;
        this.totalNumberToothbrushes = totalNumberToothbrushes;
        this.avgBrushTime = avgBrushTime;
        this.isAvgNumberToothbrushesOk = isAvgNumberToothbrushesOk;
        this.isAvgTimeOk = isAvgTimeOk;
    }

    public String[] getHeaderStrings() {
        return headerStrings;
    }

    public void setHeaderStrings(String[] headerStrings) {
        this.headerStrings = headerStrings;
    }

    public boolean[] getIsToothbrushDoneMorning() {
        return isToothbrushDoneMorning;
    }

    public void setIsToothbrushDoneMorning(boolean[] isToothbrushDoneMorning) {
        this.isToothbrushDoneMorning = isToothbrushDoneMorning;
    }

    public boolean[] getIsTimeOkMorning() {
        return isTimeOkMorning;
    }

    public void setIsTimeOkMorning(boolean[] isTimeOkMorning) {
        this.isTimeOkMorning = isTimeOkMorning;
    }

    public boolean[] getIsToothbrushDoneEvening() {
        return isToothbrushDoneEvening;
    }

    public void setIsToothbrushDoneEvening(boolean[] isToothbrushDoneEvening) {
        this.isToothbrushDoneEvening = isToothbrushDoneEvening;
    }

    public boolean[] getIsTimeOkEvening() {
        return isTimeOkEvening;
    }

    public void setIsTimeOkEvening(boolean[] isTimeOkEvening) {
        this.isTimeOkEvening = isTimeOkEvening;
    }

    public int getToothbrushesCompleted() {
        return toothbrushesCompleted;
    }

    public void setToothbrushesCompleted(int toothbrushesCompleted) {
        this.toothbrushesCompleted = toothbrushesCompleted;
    }

    public int getTotalNumberToothbrushes() {
        return totalNumberToothbrushes;
    }

    public void setTotalNumberToothbrushes(int totalNumberToothbrushes) {
        this.totalNumberToothbrushes = totalNumberToothbrushes;
    }

    public int getAvgBrushTime() {
        return avgBrushTime;
    }

    public void setAvgBrushTime(int avgBrushTime) {
        this.avgBrushTime = avgBrushTime;
    }

    public boolean isAvgNumberToothbrushesOk() {
        return isAvgNumberToothbrushesOk;
    }

    public void setAvgNumberToothbrushesOk(boolean avgNumberToothbrushesOk) {
        isAvgNumberToothbrushesOk = avgNumberToothbrushesOk;
    }

    public boolean isAvgTimeOk() {
        return isAvgTimeOk;
    }

    public void setAvgTimeOk(boolean avgTimeOk) {
        isAvgTimeOk = avgTimeOk;
    }


}
