package com.personalfinance.personalfinance;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/*
 * Conversion between BigDecimal data type and Long data type(Save inside database)
 */

public class BigDecimalConverter {
    @TypeConverter
    public BigDecimal toBigDecimal(Long amount) {
        return amount == null ? null : new BigDecimal(amount).divide(new BigDecimal(100));
    }

    @TypeConverter
    public Long toLong(BigDecimal amount) {
        return amount == null ? null : amount.multiply(new BigDecimal(100)).longValue();
    }
}