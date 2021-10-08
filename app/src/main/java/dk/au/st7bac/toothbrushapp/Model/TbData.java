package dk.au.st7bac.toothbrushapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TbData {

    private String sysId;
    private String dateTime;
    private int tbVal;
    private double tbSecs;

    public TbData(String sysId, String dateTime, int tbVal, double tbSecs) {
        this.sysId = sysId;
        this.dateTime = dateTime;
        this.tbVal = tbVal;
        this.tbSecs = tbSecs;
    }
    /*
        @SerializedName("SysId")
        @Expose
        private SysId sysId;
        @SerializedName("SystemDateTime")
        @Expose
        private SystemDateTime systemDateTime;
        @SerializedName("TTLEpoch")
        @Expose
        private TTLEpoch tTLEpoch;
        @SerializedName("TMType")
        @Expose
        private TMType tMType;
        @SerializedName("TMData")
        @Expose
        private TMData tMData;

        public SysId getSysId() {
            return sysId;
        }

        public void setSysId(SysId sysId) {
            this.sysId = sysId;
        }

        public SystemDateTime getSystemDateTime() {
            return systemDateTime;
        }

        public void setSystemDateTime(SystemDateTime systemDateTime) {
            this.systemDateTime = systemDateTime;
        }

        public TTLEpoch getTTLEpoch() {
            return tTLEpoch;
        }

        public void setTTLEpoch(TTLEpoch tTLEpoch) {
            this.tTLEpoch = tTLEpoch;
        }

        public TMType getTMType() {
            return tMType;
        }

        public void setTMType(TMType tMType) {
            this.tMType = tMType;
        }

        public TMData getTMData() {
            return tMData;
        }

        public void setTMData(TMData tMData) {
            this.tMData = tMData;
        }

     */

}
