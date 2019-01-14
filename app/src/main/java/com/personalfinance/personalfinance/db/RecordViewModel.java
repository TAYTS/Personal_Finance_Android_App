package com.personalfinance.personalfinance.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository repository;

    private LiveData<List<Record>> currentMonthRecord;

    public RecordViewModel(Application application) {
        super(application);
        repository = new RecordRepository(application);
        currentMonthRecord = repository.getCurrentMonthRecord();
    }

    LiveData<List<Record>> getCurrentMonthRecord() { return currentMonthRecord; }

    public void insert(Record record) { repository.insert(record); }
}
