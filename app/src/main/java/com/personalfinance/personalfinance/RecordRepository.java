package com.personalfinance.personalfinance;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RecordRepository {

    private RecordDao recordDao;
    private LiveData<List<Record>> allRecord;
    private MutableLiveData<List<RecordSumPojo>> incomeTotal = new MutableLiveData<List<RecordSumPojo>>();
    private MutableLiveData<List<RecordSumPojo>> expenseTotal = new MutableLiveData<List<RecordSumPojo>>();
    private static Locale locale = new Locale("en","MY");

    RecordRepository(Application application) {
        FinanceDatabase db = FinanceDatabase.getDatabase(application);
        recordDao = db.recordDao();

        Calendar startDate = Calendar.getInstance(locale);
        Calendar endDate = Calendar.getInstance(locale);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));


        // Initialise the data
        allRecord = recordDao.getAll();
        this.getSumByType(0, startDate, endDate);
        this.getSumByType(1, startDate, endDate);
    }


    // Wrapper method
    LiveData<List<Record>> getAll() { return allRecord; }
    MutableLiveData<List<RecordSumPojo>> getIncomeTotal() { return incomeTotal; }
    MutableLiveData<List<RecordSumPojo>> getExpenseTotal() { return expenseTotal; }


    // Insert Record
    public void insert(Record record) {
        new insertAsyncTask(recordDao).execute(record);
    }

    private class insertAsyncTask extends AsyncTask<Record, Void, Void> {
        private RecordDao asyncTaskDao;

        insertAsyncTask(RecordDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    // Get all record by month
    public void getRecordByMonth(Calendar startDate, Calendar endDate) {
//        allRecord.postValue(recordDao.getAll(startDate, endDate));
    }


    // Get Sum By Type
    public void getSumByType(int recordType, Calendar startDate, Calendar endDate) {
        new sumAsyncTask(recordDao, recordType).execute(recordType, startDate, endDate);
    }

    private class sumAsyncTask extends AsyncTask<Object, Void, List<RecordSumPojo>> {
        private RecordDao asyncTaskDao;
        private int recordType;

        sumAsyncTask(RecordDao dao, int recordType) {
            this.asyncTaskDao = dao;
            this.recordType = recordType;
        }

        @Override
        protected List<RecordSumPojo> doInBackground(final Object... objects) {
            int recordType = (int) objects[0];
            Calendar startDate = (Calendar) objects[1];
            Calendar endDate = (Calendar) objects[2];
            return this.asyncTaskDao.getSumByType(recordType, startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<RecordSumPojo> result) {
            if (this.recordType == 0) {
                incomeTotal.setValue(result);
            } else {
                expenseTotal.setValue(result);
            }
        }
    }


}
