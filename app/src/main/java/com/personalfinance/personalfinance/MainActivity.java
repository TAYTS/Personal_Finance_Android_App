package com.personalfinance.personalfinance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private double totalIncome = 0.0;
    private double totalExpenses = 999.00;
    private ListView listViewDesciptions;
    private Toolbar toolbar;
    private PieChart pieChart;
    private ArrayList<Integer> colors;
    private List<PieEntry> entries;
    private PieDataSet dataSet;
    private PieData pieData;
    private FloatingActionButton fabIncome;
    private FloatingActionButton fabRecord;
    private FloatingActionButton fabExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all the views
        toolbar = findViewById(R.id.toolbar);
        pieChart = findViewById(R.id.pieChart);
        fabIncome = findViewById(R.id.incomeFAB);
        fabRecord = findViewById(R.id.recordFAB);
        fabExpense = findViewById(R.id.expensesFAB);
        listViewDesciptions = findViewById(R.id.listDescriptions);


        // Get Toolbar
        setSupportActionBar(toolbar);


        // Pie Chart Settings
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(70f);
        pieChart.setUsePercentValues(true);

        // Create Pie Chart data set color
        // TODO: Delegate to other functions
        colors = new ArrayList<>();
        colors.add(Color.parseColor(this.getString(R.color.colorEntertainment)));
        colors.add(Color.parseColor(this.getString(R.color.colorFood)));
        colors.add(Color.parseColor(this.getString(R.color.colorOthers)));
        colors.add(Color.parseColor(this.getString(R.color.colorLoan)));

        // Create data for Pie Chart
        // TODO: Delegate to other functions
        entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, this.getString(R.string.expenseEntertainment)));
        entries.add(new PieEntry(26.7f, this.getString(R.string.expenseFood)));
        entries.add(new PieEntry(10.0f, this.getString(R.string.expenseOthers)));
        entries.add(new PieEntry(100.8f, this.getString(R.string.expenseLoan)));

        // Create the Pie Chart data set
        dataSet = new PieDataSet(entries, "Expenses");

        // Settings for the Pie Chart data set
        dataSet.setColors(colors);
        dataSet.setSliceSpace(5);
        dataSet.setSelectionShift(10);
        dataSet.setDrawValues(false);

        // Create the Pie Chart Data
        pieData = new PieData(dataSet);

        // Set the Pie Chart data
        pieChart.setData(pieData);

        // Add animation to display the Pie Chart
        pieChart.animateXY(1000, 1000);


        // Fab Income Listener
        fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, FormActivity.class);
                intent.putExtra(getResources().getString(R.string.recordID), getResources().getString(R.string.recordIncome));
                startActivity(intent);
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
                intent.putExtra(getResources().getString(R.string.recordID), getResources().getString(R.string.recordExpense));
                startActivity(intent);
            }
        });


        // Test List View
        String[] values = new String[] { "Entertainment (123.54%)", "Food (50.12%)", "Load (23.12%)",
                "Others (23.25%)"};

        final DescriptionArrayAdapter adapter = new DescriptionArrayAdapter(this, values);
        listViewDesciptions.setAdapter(adapter);
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

    private static String getFormatterMoneyStr(double amount) {
        Locale locale = new Locale("en","MY");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String moneyString = formatter.format(amount);

        return moneyString;
    }
}


