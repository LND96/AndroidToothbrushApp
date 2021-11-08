package dk.au.st7bac.toothbrushapp.DataProcessorFactory;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Model.Settings;
import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;

public abstract class DataProcessor {

    protected DataProcessor(Settings settings)
    {
        createProcessElements(settings);
    }

    protected abstract void createProcessElements(Settings settings);

    public abstract List<TbData> processData(List<TbData> tbDataList);

    public abstract TbStatus calculateTbStatus(List<TbData> tbDataList);
}
