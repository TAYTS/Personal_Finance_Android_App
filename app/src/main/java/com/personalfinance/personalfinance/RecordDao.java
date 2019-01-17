package com.personalfinance.personalfinance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
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
            "ORDER BY ID ASC")
    LiveData<List<Record>> getRecordByRange(int recordType, Calendar startDate, Calendar endDate);

    @Query("SELECT * FROM RECORD " +
            "WHERE CREATE_TIMESTAMP BETWEEN :startDate AND :endDate " +
            "ORDER BY ID DESC")
    List<Record> getRecordByMonth(Calendar startDate, Calendar endDate);

    // Get all
    @Query("SELECT * FROM RECORD")
    LiveData<List<Record>> getAll();

    // Update record
    @Update(onConflict = REPLACE)
    void updateRecord(Record record);

    // Delete record by Object
    @Delete
    int deleteRecord(Record record);

    // Delete record by ID
    @Query("DELETE FROM RECORD WHERE ID = :id")
    int deleteRecord(int id);

    // Sum by column
    @Query("SELECT TYPE as type, SUM(AMOUNT) as total " +
            "FROM RECORD WHERE (RECORD_TYPE = :recordType) AND " +
            "(CREATE_TIMESTAMP BETWEEN :startDate AND :endDate) " +
            "GROUP BY TYPE")
    List<RecordSumPojo> getSumByType(int recordType, Calendar startDate, Calendar endDate);
}
