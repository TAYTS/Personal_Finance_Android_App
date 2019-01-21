package com.personalfinance.personalfinance;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;


@Database(entities = {Record.class}, version = 1, exportSchema = false)
@TypeConverters({BigDecimalConverter.class, CalendarConverter.class})
public abstract class FinanceDatabase extends RoomDatabase {

    public abstract RecordDao recordDao();

    public static volatile FinanceDatabase INSTANCE;

    static FinanceDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FinanceDatabase.class) {
                if (INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FinanceDatabase.class,
                            "financeDatabase")
                            .addCallback(financeDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback financeDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
