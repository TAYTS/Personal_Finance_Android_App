package com.personalfinance.personalfinance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static Locale locale = new Locale("en","MY");
    private ViewPager pieChartViewPager;
    private SummarySlideAdapter summarySlideAdapter;
    private ArrayList<PieData> pieData = new ArrayList<>();
    private static final String[] pieChartTitles = {"Income", "Expenses"};
    private static final LinkedHashMap<String, String> incomeDescriptions = new LinkedHashMap<>();
    private static final LinkedHashMap<String, String> expenseDescriptions = new LinkedHashMap<>();
    private ArrayList<LinkedHashMap<String, String>> descriptions = new ArrayList<>();
    private static final HashMap<String, BigDecimal> incomeAmt = new HashMap<>();
    private static final HashMap<String, BigDecimal> expenseAmt = new HashMap<>();
    private ArrayList<HashMap<String, BigDecimal>> amount = new ArrayList<>();

    private Spinner spinnerMonth;
    private FloatingActionButton fabIncome;
    private FloatingActionButton fabRecord;
    private FloatingActionButton fabExpense;
    private RecordViewModel recordViewModel;

    private static final int NEW_RECORD_REQUEST_CODE = 1;

    // Define the color hash map
    private static HashMap<String, Integer> colorMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define template value
        // Color Map
        colorMap.put(
                getResources().getString(R.string.typeSalary),
                Color.parseColor(getResources().getString(R.color.colorSalary))
        );
        colorMap.put(
            getResources().getString(R.string.typeEntertainment),
            Color.parseColor(getResources().getString(R.color.colorEntertainment))
        );
        colorMap.put(
                getResources().getString(R.string.typeFood),
                Color.parseColor(getResources().getString(R.color.colorFood))
        );
        colorMap.put(
                getResources().getString(R.string.typeLoan),
                Color.parseColor(getResources().getString(R.color.colorLoan))
        );
        colorMap.put(
                getResources().getString(R.string.typeOthers),
                Color.parseColor(getResources().getString(R.color.colorOthers))
        );

        // Income Descriptions (Order is important)
        String[] incomeType = getResources().getStringArray(R.array.income_type);
        for (String type : incomeType) {
            if (!type.equals("Type")) {
                incomeDescriptions.put(
                        type.toLowerCase(),
                        type + " (0.0%)"
                );
                incomeAmt.put(
                        type.toLowerCase(),
                        new BigDecimal(0)
                );
            }
        }

        // Expense Descriptions (Order is important)
        String[] expenseType = getResources().getStringArray(R.array.expenses_type);
        for (String type : expenseType) {
            if (!type.equals("Type")) {
                expenseDescriptions.put(
                        type.toLowerCase(),
                        type + " (0.0%)"
                );
                expenseAmt.put(
                        type.toLowerCase(),
                        new BigDecimal(0)
                );
            }
        }

        // Get all the views
        spinnerMonth = findViewById(R.id.spinnerMonth);
        pieChartViewPager = findViewById(R.id.summaryViewPager);
        fabIncome = findViewById(R.id.incomeFAB);
        fabRecord = findViewById(R.id.recordFAB);
        fabExpense = findViewById(R.id.expensesFAB);


        // Define the calendar data
        final Calendar startDate = Calendar.getInstance(locale);
        final Calendar endDate = Calendar.getInstance(locale);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));


        // Record View Model
        recordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        recordViewModel.getAll().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable List<Record> records) {
                // Handle record update (Update summary view)
                recordViewModel.getSumByType(0, startDate, endDate);
                recordViewModel.getSumByType(1, startDate, endDate);
            }
        });

        recordViewModel.getIncomeTotal().observe(this, new Observer<List<RecordSumPojo>>() {
            @Override
            public void onChanged(@Nullable List<RecordSumPojo> recordSumPojos) {
                descriptions.add(0, getDescriptionString(recordSumPojos, 0));
                amount.add(0, getAllAmount(recordSumPojos, 0));
                pieData.add(0, getPieData(recordSumPojos));
                summarySlideAdapter.notifyDataSetChanged();
            }
        });


        recordViewModel.getExpenseTotal().observe(this, new Observer<List<RecordSumPojo>>() {
            @Override
            public void onChanged(@Nullable List<RecordSumPojo> recordSumPojos) {
                Log.d("FINANCE_TESTING", "expense changed");
                descriptions.add(1, getDescriptionString(recordSumPojos, 1));
                amount.add(1, getAllAmount(recordSumPojos, 1));
                pieData.add(1, getPieData(recordSumPojos));
                summarySlideAdapter.notifyDataSetChanged();
            }
        });

        // Setup Spinner Month
        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.months_spinner);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthsAdapter);
        spinnerMonth.setSelection(startDate.get(Calendar.MONTH));

        // Spinner Listener
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startDate.set(Calendar.MONTH, position);
                startDate.set(Calendar.DAY_OF_MONTH, 1);
                endDate.set(Calendar.MONTH, position);
                endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                // Handle the change of month(Update summary view)
                recordViewModel.getSumByType(0, startDate, endDate);
                recordViewModel.getSumByType(1, startDate, endDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Set Pie Chart View
        // TODO: Dynamic pieChartCount
        summarySlideAdapter = new SummarySlideAdapter(this, 2, pieChartTitles, pieData, descriptions, amount);
        pieChartViewPager.setAdapter(summarySlideAdapter);


        // Fab Income Listener
        fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, FormActivity.class);
                intent.putExtra(getResources().getString(R.string.recordID), getResources().getInteger(R.integer.recordIncome));
                startActivityForResult(intent, NEW_RECORD_REQUEST_CODE);
            }
        });

        // Fab Record Listener
        fabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // Fab Expense Listener
        fabExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, FormActivity.class);
                intent.putExtra(getResources().getString(R.string.recordID), getResources().getInteger(R.integer.recordExpense));
                startActivityForResult(intent, NEW_RECORD_REQUEST_CODE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check the request type
        if (requestCode == NEW_RECORD_REQUEST_CODE) {
            // Check the replied status
            if (resultCode == RESULT_OK) {
                // Get SharedPreference
                String sharedPrefFile = getResources().getString(R.string.sharedPreference);
                SharedPreferences formData = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                Gson gson = new Gson();
                String json = formData.getString("record", "");
                if (!json.isEmpty()) {
                    Record record = gson.fromJson(json, Record.class);
                    recordViewModel.insert(record);
                }
            }
        }
    }

    private PieData getPieData(List<RecordSumPojo> recordSumPojos) {
        ArrayList<Integer> colors = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();

        if (!recordSumPojos.isEmpty()) {
            for (RecordSumPojo recordSumPojo : recordSumPojos) {
                entries.add(new PieEntry(recordSumPojo.total.floatValue(), recordSumPojo.type));
                colors.add(colorMap.get(recordSumPojo.type.toLowerCase()));
            }
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(5);
            dataSet.setSelectionShift(0);
            dataSet.setDrawValues(false);
            dataSet.setColors(colors);
            return new PieData(dataSet);
        } else {
            entries.add(new PieEntry(0f, ""));
            return new PieData(new PieDataSet(entries, ""));
        }

    }

    private LinkedHashMap<String, String> getDescriptionString(List<RecordSumPojo> recordSumPojos, int recordType) {
        LinkedHashMap<String, String> descriptions = new LinkedHashMap<>();

        // Select the template
        if (recordType == 0) {
            descriptions.putAll(incomeDescriptions);
        } else {
            descriptions.putAll(expenseDescriptions);
        }

        if (!recordSumPojos.isEmpty()) {
            // Get the total
            BigDecimal sum = new BigDecimal(0);
            for (RecordSumPojo recordSumPojo : recordSumPojos) {
                sum = sum.add(recordSumPojo.total);
            }


            // Calculate percentage
            if (sum.compareTo(BigDecimal.ZERO) >  0) {
                for (RecordSumPojo recordSumPojo : recordSumPojos) {
                    String percentageStr = recordSumPojo.total.divide(sum, 2, RoundingMode.UP).multiply(new BigDecimal(100)).toPlainString();
                    descriptions.put(
                            recordSumPojo.type.toLowerCase(),
                            descriptions.get(recordSumPojo.type.toLowerCase()).replaceAll("\\d+.\\d+", percentageStr)
                    );
                }
            }
        }

        return descriptions;
    }

    private HashMap<String, BigDecimal> getAllAmount(List<RecordSumPojo> recordSumPojos, int recordType) {
        HashMap<String, BigDecimal> newAmount = new HashMap<>();

        if (recordType == 0) {
            newAmount.putAll(incomeAmt);
        } else {
            newAmount.putAll(expenseAmt);
        }

        for (RecordSumPojo recordSumPojo : recordSumPojos) {
            newAmount.put(recordSumPojo.type.toLowerCase(), recordSumPojo.total);
        }

        return newAmount;
    }


}


