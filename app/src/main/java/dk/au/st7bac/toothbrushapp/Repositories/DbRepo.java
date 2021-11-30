package dk.au.st7bac.toothbrushapp.Repositories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dk.au.st7bac.toothbrushapp.Model.Converters;
import dk.au.st7bac.toothbrushapp.Model.TbDao;
import dk.au.st7bac.toothbrushapp.Model.TbData;

// inspiration for database: SWMAD-01 Mobile Application Development, lecture 4, spring 2021
// inspiration for singleton initialization: https://en.wikipedia.org/wiki/Singleton_pattern and https://en.wikipedia.org/wiki/Double-checked_locking
@Database(entities = {TbData.class}, version = 8)
@TypeConverters({Converters.class})
public abstract class DbRepo extends RoomDatabase {

    public abstract TbDao tbDao();
    private static DbRepo instance;

    // thread-safe implementation of singleton pattern with lazy initialization
    public static DbRepo getDbRepo (final Context context) {
        // check if DpRepo is initialized (without obtaining the lock)
        if (instance == null) {
            // synchronize access to database and obtain the lock
            synchronized (DbRepo.class) {
                // check again if DpRepo has already been initialized if another thread acquired the lock first
                if (instance == null) {
                    // initialize DpRepo
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            DbRepo.class, "tb_database").build();
                }
            }
        }

        return instance;
    }
}
