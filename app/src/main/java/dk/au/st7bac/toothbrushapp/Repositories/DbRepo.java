package dk.au.st7bac.toothbrushapp.Repositories;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dk.au.st7bac.toothbrushapp.Model.Converters;
import dk.au.st7bac.toothbrushapp.Model.TbDao;
import dk.au.st7bac.toothbrushapp.Model.TbData;

// Kilde: MAD lektion 4 demovideo
@Database(entities = {TbData.class}, version = 8)
@TypeConverters({Converters.class})
public abstract class DbRepo extends RoomDatabase {
    // Skal vi gøre noget med extending SQLiteOpenHelper som bliver beskrevet i video L4_2 omkring 2 min inde? Eller er det noget man gør hvis man ikke bruger room?
    // Overvej at rykke noget til en service så databasen kan opdateres selvom brugeren har quittet appen
    // Måske skal hele databehandling ske i en service så det kan køre når appen er lukket
    // Bruger singleton fordi databasen er et dyrt objekt at oprette, så vi vil være sikre på at vi kun har det ene
    // Vores DAO er et interface. Vi skriver ikke selv implementeringen af interfacet, for det klarer ROOM for os
    // I video L4_3 omkring 7 min inde fortælles om DB, Livedata og viewmodel
    // brug asynkrone metoder når databasen tilgås så vi ikke blokerer ui-tråden

    public abstract TbDao tbDao(); // mandatory dao getter
    private static DbRepo instance; // singleton instance

    // singleton pattern
    public static DbRepo getDbRepo (final Context context) {
        if (instance == null) {
            synchronized (DbRepo.class) { // synchronized action??
                if (instance == null) {
                    // using the database builder with context, DbRepo and filename of database
                    // provided to build database
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            DbRepo.class, "tb_database")
                            .fallbackToDestructiveMigration() // DETTE SKAL FJERNES I SIDSTE ENDE! fallbackToDestructiveMigration means if the version numbers is increased and the app is redeployed, all tables are cleared and the database is build from scratch.
                            .build();
                }
            }
        }

        return instance;
    }
}
