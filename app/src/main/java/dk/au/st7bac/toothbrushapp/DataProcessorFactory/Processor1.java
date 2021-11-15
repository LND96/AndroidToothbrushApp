package dk.au.st7bac.toothbrushapp.DataProcessorFactory;

import android.content.SharedPreferences;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Model.DataCalculator;
import dk.au.st7bac.toothbrushapp.Model.DataCleaner;
import dk.au.st7bac.toothbrushapp.Model.DataFilter;
import dk.au.st7bac.toothbrushapp.Model.Settings;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;
import dk.au.st7bac.toothbrushapp.R;
import dk.au.st7bac.toothbrushapp.ToothbrushApp;

public class Processor1 extends DataProcessor {

    private DataFilter dataFilter;
    private DataCleaner dataCleaner;
    private DataCalculator dataCalculator;

    public Processor1(Settings settings) {
        super(settings);
    }

    @Override
    protected void createProcessElements(Settings settings) {
        dataFilter = new DataFilter(settings.getOffset(), settings.getMinMeasurementDuration(),
                settings.getMaxMeasurementDuration());
        dataCleaner = new DataCleaner(settings.getTimeBetweenMeasurements());
        dataCalculator = new DataCalculator(settings.getMinAccpTbTime(),
                settings.getNumIntervalDays(), settings.getTbEachDay(),
                settings.getMorningToEveningTime(), settings.getEveningToMorningTime(),
                settings.getNumTbThres(), settings.getLastDayInInterval());
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
        if (key.equals(ToothbrushApp.getAppContext().getString(R.string.settingMinAccpTimeKey))) {
            int newSetting = Integer.parseInt(sharedPreferences.getString(key, "90"));
            dataCalculator.setTimeTbThreshold(newSetting);
        }
    }
}
