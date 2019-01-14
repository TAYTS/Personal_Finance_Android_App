package com.personalfinance.personalfinance.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "RECORD")
public class Record {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "AMOUNT")
    private BigDecimal amount;

    @ColumnInfo(name = "DESCRIPTION")
    private String description;

    @ColumnInfo(name = "TYPE")
    private String type;

    // 0: INCOME
    // 1: EXPENSE
    @ColumnInfo(name = "RECORD_TYPE")
    private int recordType;

    @ColumnInfo(name = "CREATE_TIMESTAMP")
    private Date create_timestamp;


    public Record(@NonNull int id, BigDecimal amount, String description, String type, int recordType, Date create_timestamp) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.recordType = recordType;
        this.create_timestamp = create_timestamp;
    }

    public int getId() {
        return this.id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }

    public int getRecordType() {
        return this.recordType;
    }

    public Date getCreate_timestamp() {
        return this.create_timestamp;
    }
}
