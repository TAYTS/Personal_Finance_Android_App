package com.personalfinance.personalfinance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private static Locale locale = new Locale("en","MY");
    private Spinner spinnerMonth;
    private ListView listHistory;
    private RecordViewModel recordViewModel;
    private HistoryArrayAdapter historyArrayAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        // Get all the view
        spinnerMonth = findViewById(R.id.spinnerMonth);
        listHistory = findViewById(R.id.listHistory);

        recordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        // Setup the calendar data
        final Calendar startDate = Calendar.getInstance(locale);
        final Calendar endDate = Calendar.getInstance(locale);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Setup Spinner Month
        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(HistoryActivity.this, R.array.months, R.layout.months_spinner);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthsAdapter);
        spinnerMonth.setSelection(startDate.get(Calendar.MONTH));

        recordViewModel.getAllByMonth().observe(this, new Observer<List<Record>>() {
                    @Override
                    public void onChanged(@Nullable List<Record> records) {
                        historyArrayAdapter = new HistoryArrayAdapter(HistoryActivity.this, (ArrayList<Record>) records, recordViewModel, startDate, endDate);
                        listHistory.setAdapter(historyArrayAdapter);
                        historyArrayAdapter.notifyDataSetChanged();
                    }
                }
        );

        // Spinner Listener
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startDate.set(Calendar.MONTH, position);
                startDate.set(Calendar.DAY_OF_MONTH, 1);
                endDate.set(Calendar.MONTH, position);
                endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                // Handle the change of month(Update history view)
                recordViewModel.getRecordByMonth(startDate, endDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        historyArrayAdapter.onActivityResult(requestCode, resultCode, data);
    }
}
