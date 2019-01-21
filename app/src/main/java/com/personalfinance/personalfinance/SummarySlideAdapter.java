package com.personalfinance.personalfinance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/*
 * Handling the Slide View in the Home Page
 */
public class SummarySlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    private int pieChartCount;
    private String[] pieChartTitles;
    private ArrayList<LinkedHashMap<String, String>> descriptions;
    private ArrayList<HashMap<String, BigDecimal>> amountType;
    private ArrayList<PieData> pieData;

    private TextView pieChartTitle;
    private PieChart pieChart;
    private ListView listViewDesciptions;

    public SummarySlideAdapter(
            Context context,
            int pieChartCount,
            String[] pieChartTitles,
            ArrayList<PieData> pieData,
            ArrayList<LinkedHashMap<String, String>> descriptions,
            ArrayList<HashMap<String, BigDecimal>> amountType) {
        this.context = context;
        this.pieChartCount = pieChartCount;
        this.pieChartTitles = pieChartTitles;
        this.pieData = pieData;
        this.descriptions = descriptions;
        this.amountType = amountType;
    }

    @Override
    public int getCount() {
        return this.pieChartCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Setup the layout
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.summary_view,container,false);

        // Get all view components
        pieChartTitle = view.findViewById(R.id.pieChartTitle);
        pieChart = view.findViewById(R.id.pieChart);
        listViewDesciptions = view.findViewById(R.id.listDescriptions);

        // Pie Chart Settings
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(70f);
        pieChart.setUsePercentValues(true);

        // Pie Chart Title
        pieChartTitle.setText(pieChartTitles[position]);

        // Add Pie Chart data
        if (!pieData.isEmpty() && pieData.get(position).getDataSet().getYMax() > 0) {
            pieChart.setData(pieData.get(position));
            pieChart.animateXY(1000, 1000);
        }
        else {
            pieChart.setDrawHoleEnabled(false);
            Utils.init(context);
            pieChart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(25f));
        }

        final DescriptionArrayAdapter adapter = new DescriptionArrayAdapter(context, descriptions.get(position), amountType.get(position));
        listViewDesciptions.setAdapter(adapter);

        // Set tag for the view
        view.setTag(position);

        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeAllViews();
    }
}
