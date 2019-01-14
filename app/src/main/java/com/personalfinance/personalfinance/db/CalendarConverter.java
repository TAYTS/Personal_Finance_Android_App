package com.personalfinance.personalfinance.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

public class CalendarConverter {
    @TypeConverter
    public static Calendar toDate(Long timestamp) {
        Calendar date = Calendar.getInstance();
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
