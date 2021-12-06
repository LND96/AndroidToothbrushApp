package dk.au.st7bac.toothbrushapp.DataProcessorFactory;

import android.content.SharedPreferences;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Model.Configs;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;

public abstract class DataProcessor {

    // GoF Factory Method
    protected DataProcessor(Configs configs)
    {
        createProcessElements(configs);
    }

    protected abstract void createProcessElements(Configs configs);

    public abstract List<TbData> processData(List<TbData> tbDataList);

    public abstract TbStatus calculateTbStatus(List<TbData> tbDataList);

    public abstract void updateSettings(SharedPreferences sharedPreferences, String key);
}