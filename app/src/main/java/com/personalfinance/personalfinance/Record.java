package com.personalfinance.personalfinance;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity(tableName = "RECORD")
public class Record {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private long id;

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
    private Calendar create_timestamp;


    public Record(BigDecimal amount, String description, String type, int recordType, Calendar create_timestamp) {
        this.id = 0;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.recordType = recordType;
        this.create_timestamp = create_timestamp;
    }

    // ID Getter
    public long getId() {
        return this.id;
    }

    // ID Setter
    public void setId(long id) { this.id = id; }

    // Amount Getter
    public BigDecimal getAmount() {
        return this.amount;
    }

    // Amount Setter
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    // Description Getter
    public String getDescription() {
        return this.description;
    }

    // Description Setter
    public void setDescription(String description) { this.description = description; }

    // Type Getter
    public String getType() {
        return this.type;
    }

    // Type Setter
    public void setType(String type) { this.type = type; }

    // Record Type Getter
    public int getRecordType() {
        return this.recordType;
    }

    // Create Timestamp Getter
    public Calendar getCreate_timestamp() {
        return this.create_timestamp;
    }

    // Create Timestamp Setter
    public void setCreate_timestamp(Calendar timestamp) { this.create_timestamp = timestamp; }
}
