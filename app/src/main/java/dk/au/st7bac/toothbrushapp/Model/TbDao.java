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
    List<TbData> getAllTbData(); // Skal det være livedata eller en anden slags liste?

    // kilde: https://stackoverflow.com/questions/62215105/android-room-retrive-rows-for-30-days
    //@Query("SELECT * FROM tbdata WHERE epoch >= strftime('%s', dateTime('now', '-',:days,'day'))")
    @Query("SELECT * FROM tbdata WHERE epoch >= strftime('%s', dateTime('now', '-1 day'))") // Hardcoded at det er fra den sidste dag
    List<TbData> getTbDataInInterval();

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
