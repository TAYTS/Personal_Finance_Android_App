package com.personalfinance.personalfinance;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;


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