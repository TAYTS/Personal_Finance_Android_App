package com.personalfinance.personalfinance;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.Locale;

/*
 * Conversion between Calendar data type and Long data type(Save inside database)
 */

public class CalendarConverter {
    private static Locale locale = new Locale("en","MY");

    @TypeConverter
    public static Calendar toDate(Long timestamp) {
        Calendar date = Calendar.getInstance(locale);
        if (timestamp == null)
            return null;
        else {
            date.setTimeInMillis(timestamp);
            return date;
        }
    }

    @TypeConverter
    public static Long toTimestamp(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}
