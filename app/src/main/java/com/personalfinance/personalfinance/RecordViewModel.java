package com.personalfinance.personalfinance;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository repository;

    private LiveData<List<Record>> allRecords;
    private MutableLiveData<List<RecordSumPojo>> incomeTotal;
    private MutableLiveData<List<RecordSumPojo>> expenseTotal;

    public RecordViewModel(Application application) {
        super(application);
        repository = new RecordRepository(application);
        allRecords = repository.getAll();
        incomeTotal = repository.getIncomeTotal();
        expenseTotal = repository.getExpenseTotal();
    }

    LiveData<List<Record>> getAll() { return allRecords; }
    MutableLiveData<List<RecordSumPojo>> getIncomeTotal() { return incomeTotal; }
    MutableLiveData<List<RecordSumPojo>> getExpenseTotal() { return expenseTotal; }


    // Methods
    // Insert Record
    public void insert(Record record) { repository.insert(record); }

    // Get the Sum by Record Type(Income, Expense)
    public void getSumByType(int recordType, Calendar startDate, Calendar endDate) {
        repository.getSumByType(recordType, startDate, endDate);
    }

    public void getRecordByMonth(Calendar startDate, Calendar endDate) {
        repository.getRecordByMonth(startDate, endDate);
    }
}
