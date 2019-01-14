package com.personalfinance.personalfinance.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters({BigDecimalConverter.class, CalendarConverter.class})
public interface RecordDao {

    // Insert new record
    @Insert(onConflict = IGNORE)
    void insert(Record record);

    // Get specific record by ID
    @Query("SELECT * FROM RECORD WHERE ID = :id")
    Record getRecordById(int id);

    // Get a list of records by range
    @Query("SELECT * FROM RECORD " +
            "WHERE (RECORD_TYPE = :recordType) AND CREATE_TIMESTAMP BETWEEN :startDate AND :endDate " +
            "ORDER BY CREATE_TIMESTAMP ASC")
    LiveData<List<Record>> getRecordByRange(int recordType, Date startDate, Date endDate);

    // Update record
    @Update(onConflict = REPLACE)
    void updateRecord(Record record);

    // Delete record by Object
    @Delete
    int deleteRecord(Record record);

    // Delete record by ID
    @Query("DELETE FROM RECORD WHERE ID = :id")
    int deleteRecord(int id);
}
