package dk.au.st7bac.toothbrushapp;

public class Constants {
    public static final String FROM_ALERT_RECEIVER = "FROM_ALERT_RECEIVER";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";
    public static final String FROM_SETTINGS_CTRL = "FROM_SETTINGS_CTRL";
    public static final String SETTINGS_FRAG = "SETTINGS_FRAG";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "NOTIFICATION_CHANNEL";
    public static final String FIRST_RUN = "FIRST_RUN";
    public static final String SETTING_MIN_ACCP_TIME_KEY = ToothbrushApp.getAppContext().getString(R.string.settingMinAccpTimeKey);
    public static final String SETTING_NUM_INTERVAL_DAYS_KEY = ToothbrushApp.getAppContext().getString(R.string.settingNumIntervalDaysKey);
    public static final String SETTING_TB_EACH_DAY_KEY = ToothbrushApp.getAppContext().getString(R.string.settingTbEachDayKey);
    public static final String SETTING_MIN_ACCP_PERCENT_KEY = ToothbrushApp.getAppContext().getString(R.string.settingMinAccpPercentKey);
    public static final String SETTING_ENABLE_NOTIFICATIONS_KEY = ToothbrushApp.getAppContext().getString(R.string.settingEnableNotificationsKey);
    public static final String SETTING_DAYS_WITHOUT_TB_KEY = ToothbrushApp.getAppContext().getString(R.string.settingDaysWithoutTbKey);
    public static final String SETTING_SENSOR_ID_KEY = ToothbrushApp.getAppContext().getString(R.string.settingSensorIdKey);
}
