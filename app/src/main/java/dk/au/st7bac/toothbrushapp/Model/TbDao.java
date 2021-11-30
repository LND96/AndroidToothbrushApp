package dk.au.st7bac.toothbrushapp.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// inspiration for TbDao: SWMAD-01 Mobile Application Development, lecture 4, spring 2021
// ROOM handles implementation of this interface
@Dao
public interface TbDao {
    // get data in given interval
    @Query("SELECT * FROM tbdata WHERE epoch >= :lowerLimit AND epoch <= :higherLimit")
    List<TbData> getTbDataInInterval(long lowerLimit, long higherLimit);

    // add list of data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTbDataList(List<TbData> tbDataList);
}