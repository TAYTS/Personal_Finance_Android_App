package com.personalfinance.personalfinance.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordRepository {

    private RecordDao recordDao;
    private LiveData<List<Record>> currentMonthRecord;

    private static Locale locale = new Locale("en","MY");

    RecordRepository(Application application) {
        FinanceDatabase db = FinanceDatabase.getDatabase(application);
        recordDao = db.recordDao();
        Calendar calendar = Calendar.getInstance(locale);
        Calendar startDate = Calendar.getInstance(locale);
        Calendar endDate = Calendar.getInstance(locale);

        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        currentMonthRecord = recordDao.getRecordByRange(1, startDate, endDate);
    }


    LiveData<List<Record>> getCurrentMonthRecord() {
        return  currentMonthRecord;
    }


    public void insert(Record record) {
        new insertAsyncTask(recordDao).execute(record);

    }

    private static class insertAsyncTask extends AsyncTask<Record, Void, Void> {
        private RecordDao asyncTaskDao;

        insertAsyncTask(RecordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
