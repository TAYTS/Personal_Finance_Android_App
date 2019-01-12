package com.personalfinance.personalfinance;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get Pie Chart
        PieChart pieChart = findViewById(R.id.pieChart);

        // Pie Chart Settings
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(70f);
        pieChart.setUsePercentValues(true);

        // Create Pie Chart data set color
        // TODO: Delegate to other functions
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor(this.getString(R.color.colorEntertainment)));
        colors.add(Color.parseColor(this.getString(R.color.colorFood)));
        colors.add(Color.parseColor(this.getString(R.color.colorOthers)));
        colors.add(Color.parseColor(this.getString(R.color.colorLoan)));

        // Create data for Pie Chart
        // TODO: Delegate to other functions
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, this.getString(R.string.expenseEntertainment)));
        entries.add(new PieEntry(26.7f, this.getString(R.string.expenseFood)));
        entries.add(new PieEntry(10.0f, this.getString(R.string.expenseOthers)));
        entries.add(new PieEntry(100.8f, this.getString(R.string.expenseLoan)));

        // Create the Pie Chart data set
        PieDataSet set = new PieDataSet(entries, "Expenses");

        // Settings for the Pie Chart data set
        set.setColors(colors);
        set.setSliceSpace(5);
        set.setSelectionShift(10);
        set.setDrawValues(false);

        // Create the Pie Chart Data
        PieData data = new PieData(set);

        // Set the Pie Chart data
        pieChart.setData(data);

        // Add animation to display the Pie Chart
        pieChart.animateXY(1000, 1000);


        // Get the floating button
        FloatingActionButton fab = findViewById(R.id.incomeFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Test List View
        listViewDesciptions = findViewById(R.id.listDescriptions);
        String[] values = new String[] { "Entertainment (123.54%)", "Food (50.12%)", "Load (23.12%)",
                "Others (23.25%)"};


//
//        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
//            list.add(values[i]);
//        }
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}


