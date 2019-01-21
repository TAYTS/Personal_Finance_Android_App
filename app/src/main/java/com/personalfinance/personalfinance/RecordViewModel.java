package com.personalfinance.personalfinance;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.List;

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository repository;

    private LiveData<List<Record>> allRecords;
    private MutableLiveData<List<RecordSumPojo>> incomeTotal;
    private MutableLiveData<List<RecordSumPojo>> expenseTotal;
    private MutableLiveData<List<Record>> allMthRecords;

    public RecordViewModel(Application application) {
        super(application);
        repository = new RecordRepository(application);
        allRecords = repository.getAll();
        incomeTotal = repository.getIncomeTotal();
        expenseTotal = repository.getExpenseTotal();
        allMthRecords = repository.getAllByMonth();

    }

    LiveData<List<Record>> getAll() { return allRecords; }
    MutableLiveData<List<RecordSumPojo>> getIncomeTotal() { return incomeTotal; }
    MutableLiveData<List<RecordSumPojo>> getExpenseTotal() { return expenseTotal; }
    MutableLiveData<List<Record>> getAllByMonth() { return allMthRecords; }


    // Methods
    // Insert Record
    public void insert(Record record) { repository.insert(record); }

    // Get the Sum by Record Type(Income, Expense)
    public void getSumByType(int recordType, Calendar startDate, Calendar endDate) {
        repository.getSumByType(recordType, startDate, endDate);
    }

    // Get record by month
    public void getRecordByMonth(Calendar startDate, Calendar endDate) {
        repository.getRecordByMonth(startDate, endDate);
    }

    // Delete record
    public void delete(Long recId) { repository.delete(recId); }

    // Update record
    public void update(Record record) { repository.update(record); }
}
