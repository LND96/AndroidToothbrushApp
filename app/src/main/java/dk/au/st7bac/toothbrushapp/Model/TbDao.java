package dk.au.st7bac.toothbrushapp.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;

@Dao
public interface TbDao {

    // get all data
    @Query("SELECT * FROM tbdata")
    LiveData<ArrayList<TbData>> getAll();

    // add list of data to database
    @Insert // skal der være en onConflict?
    void addTbDataList(ArrayList<TbData> tbDataArrayList);

    // delete a particular data point based on the primary key
    @Delete
    void delete(TbData tbData);

    // delete all data in the list
    @Delete
    void deleteList(ArrayList<TbData> tbDataArrayList);

    // delete all data in database
    @Query("DELETE FROM tbdata")
    void deleteAllData();
}
