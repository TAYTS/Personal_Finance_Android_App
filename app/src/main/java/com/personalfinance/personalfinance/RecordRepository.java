package com.personalfinance.personalfinance;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordRepository {

    private RecordDao recordDao;
    private LiveData<List<Record>> allRecord;
    private MutableLiveData<List<RecordSumPojo>> incomeTotal = new MutableLiveData<List<RecordSumPojo>>();
    private MutableLiveData<List<RecordSumPojo>> expenseTotal = new MutableLiveData<List<RecordSumPojo>>();
    private MutableLiveData<List<Record>> monthRecord = new MutableLiveData<List<Record>>();
    private static Locale locale = new Locale("en","MY");

    RecordRepository(Application application) {
        FinanceDatabase db = FinanceDatabase.getDatabase(application);
        recordDao = db.recordDao();

        Calendar startDate = Calendar.getInstance(locale);
        Calendar endDate = Calendar.getInstance(locale);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Clean past year record
        this.deletePastRecord();

        // Initialise the data
        allRecord = recordDao.getAll();
        this.getSumByType(0, startDate, endDate);
        this.getSumByType(1, startDate, endDate);
    }


    // Wrapper method
    LiveData<List<Record>> getAll() { return allRecord; }
    MutableLiveData<List<RecordSumPojo>> getIncomeTotal() { return incomeTotal; }
    MutableLiveData<List<RecordSumPojo>> getExpenseTotal() { return expenseTotal; }
    MutableLiveData<List<Record>> getAllByMonth() { return monthRecord; }


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
        new mthRecAsyncTask(recordDao).execute(startDate, endDate);
    }

    private class mthRecAsyncTask extends AsyncTask<Calendar, Void, List<Record>> {
        private RecordDao asyncTaskDao;

        mthRecAsyncTask(RecordDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected List<Record> doInBackground(Calendar... calendars) {
            Calendar startDate = calendars[0];
            Calendar endDate = calendars[1];
            return this.asyncTaskDao.getRecordByMonth(startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<Record> records) {
            monthRecord.setValue(records);
        }
    }


    // Get Sum By Type
    public void getSumByType(int recordType, Calendar startDate, Calendar endDate) {
        new sumAsyncTask(recordDao, recordType).execute(startDate, endDate);
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
            Calendar startDate = (Calendar) objects[0];
            Calendar endDate = (Calendar) objects[1];
            return this.asyncTaskDao.getSumByType(this.recordType, startDate, endDate);
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

    // Delete Record
    public void delete(Long recId) {
        new deleteAsyncTask(recordDao).execute(recId);
    }

    private class deleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private RecordDao asyncTaskDao;

        deleteAsyncTask(RecordDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            asyncTaskDao.deleteRecord(params[0]);
            return null;
        }
    }

    // Update Record
    public void update(Record record) { new updateAsyncTask(recordDao).execute(record); }

    private class updateAsyncTask extends AsyncTask<Record, Void, Void> {
        private RecordDao asyncTaskDao;

        updateAsyncTask(RecordDao dao) { this.asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Record... records) {
            asyncTaskDao.updateRecord(records[0]);
            return null;
        }
    }

    // Clean database
    public void deletePastRecord() { new delPastRecAsyncTask(recordDao).execute(); }

    private class delPastRecAsyncTask extends AsyncTask<Void, Void, Void> {
        private RecordDao asyncTaskDao;

        delPastRecAsyncTask(RecordDao dao) { this.asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar threshold = Calendar.getInstance();
            threshold.set(Calendar.YEAR, threshold.get(Calendar.YEAR) - 1);
            threshold.set(Calendar.MONTH, 0);
            threshold.set(Calendar.DAY_OF_MONTH, 1);
            asyncTaskDao.deletePastRecord(threshold);
            return null;
        }
    }

}
