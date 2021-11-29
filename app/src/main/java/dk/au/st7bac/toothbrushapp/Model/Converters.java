package dk.au.st7bac.toothbrushapp.Model;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// inspiration for converters: https://stackoverflow.com/questions/54927913/room-localdatetime-typeconverter
public class Converters {

    @TypeConverter
    public static LocalDateTime toDateTime(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateTimeString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}
