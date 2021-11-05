package dk.au.st7bac.toothbrushapp.Interfaces;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Model.TbData;

public interface IDataCleaner {
    public List<TbData> cleanData(List<TbData> tbDataList);
}
