package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TbDao {

    // get all data
    @Query("SELECT * FROM tbdata")
    List<TbData> getAllTbData();

    // get data in given interval
    //@Query("SELECT * FROM tbdata WHERE epoch >= :lowerLimit AND epoch <= :higherLimit")
    @Query("SELECT * FROM tbdata WHERE :lowerLimit <= epoch <= :higherLimit")
    List<TbData> getTbDataInInterval(long lowerLimit, long higherLimit);

    // add list of data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTbDataList(List<TbData> tbDataList);

    // delete a particular data point based on the primary key
    @Delete
    void delete(TbData tbData);

    // delete all data in the list
    @Delete
    void deleteList(List<TbData> tbDataList);

    // delete all data in database
    @Query("DELETE FROM tbdata")
    void deleteAllData();
}
