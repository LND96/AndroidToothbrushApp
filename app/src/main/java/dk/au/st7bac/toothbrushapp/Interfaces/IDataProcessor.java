package dk.au.st7bac.toothbrushapp.Interfaces;

import java.util.List;

import dk.au.st7bac.toothbrushapp.Model.TbData;
import dk.au.st7bac.toothbrushapp.Model.TbStatus;

public interface IDataProcessor {
    public TbStatus processData(List<TbData> TbDataList);
}
