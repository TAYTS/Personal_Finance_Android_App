package com.personalfinance.personalfinance.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Record.class}, version = 1)
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
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
