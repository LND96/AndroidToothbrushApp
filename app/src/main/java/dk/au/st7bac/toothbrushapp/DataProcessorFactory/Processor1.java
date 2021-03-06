package dk.au.st7bac.toothbrushapp.DataProcessorFactory;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.Model.DataCalculator;
import dk.au.st7bac.toothbrushapp.Model.DataCleaner;
import dk.au.st7bac.toothbrushapp.Model.DataFilter;
import dk.au.st7bac.toothbrushapp.Model.Configs;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

public class Processor1 extends DataProcessor {


    private DataFilter dataFilter;
    private DataCleaner dataCleaner;
    private DataCalculator dataCalculator;

    public Processor1(Configs configs) {
        super(configs);
    }

    // implementation of factory method
    @Override
    protected void createProcessElements(Configs configs) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(ToothbrushApp.getAppContext());

        // local variables
        int minAccpTbTime;
        int numIntervalDays;
        int tbEachDay;
        double numTbThres;

        // if first run get settings from configuration file, else get settings from shared preferences
        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            minAccpTbTime = configs.getMinAccpTbTime();
            numIntervalDays = configs.getNumIntervalDays();
            tbEachDay = configs.getTbEachDay();
            numTbThres = configs.getNumTbThres();
        } else {
            minAccpTbTime = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_MIN_ACCP_TIME_KEY, ""));
            numIntervalDays = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_NUM_INTERVAL_DAYS_KEY, ""));
            tbEachDay = Integer.parseInt(sharedPreferences
                    .getString(Constants.SETTING_TB_EACH_DAY_KEY, ""));
            numTbThres = Double.parseDouble(sharedPreferences
                    .getString(Constants.SETTING_MIN_ACCP_PERCENT_KEY, ""));
        }

        // create objects
        dataFilter = new DataFilter(configs.getOffset(), configs.getMinMeasurementDuration(),
                configs.getMaxMeasurementDuration());
        dataCleaner = new DataCleaner(configs.getTimeBetweenMeasurements());
        dataCalculator = new DataCalculator(minAccpTbTime, numIntervalDays, tbEachDay,
                configs.getMorningToEveningTime(), configs.getEveningToMorningTime(),
                numTbThres, configs.getLastDayInInterval());
    }

    @Override
    public List<TbData> processData(List<TbData> tbDataList) {
        tbDataList = dataFilter.filterData(tbDataList);
        tbDataList = dataCleaner.cleanData(tbDataList);
        return tbDataList;
    }

    @Override
    public TbStatus calculateTbStatus(List<TbData> tbDataList) {
        return dataCalculator.processData(tbDataList);
    }

    @Override
    public void updateSettings(SharedPreferences sharedPreferences, String key) {
        // depending on what changed, settings are updated
        if (key.equals(Constants.SETTING_MIN_ACCP_TIME_KEY)) {
            dataCalculator.setTimeTbThreshold(Integer.parseInt(sharedPreferences
                    .getString(key, "")));
        } else if (key.equals(Constants.SETTING_MIN_ACCP_PERCENT_KEY)) {
            dataCalculator.setNumTbThreshold(Double.parseDouble(sharedPreferences
                    .getString(key, "")));
        } else if (key.equals(Constants.SETTING_TB_EACH_DAY_KEY)) {
            dataCalculator.setTbEachDay(Integer.parseInt(sharedPreferences
                    .getString(key, "")));
        } else if (key.equals(Constants.SETTING_NUM_INTERVAL_DAYS_KEY)) {
            dataCalculator.setDays(Integer.parseInt(sharedPreferences
                    .getString(key, "")));
        }
    }
}
