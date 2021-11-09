package dk.au.st7bac.toothbrushapp.Model;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// kilde: https://stackoverflow.com/questions/54927913/room-localdatetime-typeconverter
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

    @TypeConverter
    public static LocalTime toLocalTime(String timeString) {
        if (timeString == null) {
            return null;
        } else {
            return LocalTime.parse(timeString);
        }
    }

    @TypeConverter
    public static String toTimeString(LocalTime time) {
        if (time == null) {
            return null;
        } else {
            return time.toString();
        }
    }

    @TypeConverter
    public static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}
