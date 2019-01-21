package com.personalfinance.personalfinance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistoryArrayAdapter extends ArrayAdapter<Record> {
    private Context context;
    private ArrayList<Record> history;
    private RecordViewModel recordViewModel;
    private Calendar startDate;
    private Calendar endDate;

    private Locale locale = new Locale("en","MY");

    // Define constant
    private static final String DATEFORMAT = "dd MMMM yyyy";
    private static final int UPDATE_RECORD_REQUEST_CODE = 2;

    HistoryArrayAdapter(Context context, ArrayList<Record> history, RecordViewModel recordViewModel, Calendar startDate, Calendar endDate) {
        super(context, R.layout.list_history, history);
        this.context = context;
        this.history = history;
        this.recordViewModel = recordViewModel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private static class ViewHolder {
        TextView textViewAmt;
        TextView textViewDesc;
        TextView textViewType;
        TextView textViewDate;
        ImageButton btnEdit;
        ImageButton btnDelete;
    }

    @Override
    public View getView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        // Get the current history record
        Record current = history.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_history, parent, false);
            viewHolder.textViewAmt = convertView.findViewById(R.id.amount);
            viewHolder.textViewDesc = convertView.findViewById(R.id.descriptions);
            viewHolder.textViewType = convertView.findViewById(R.id.type);
            viewHolder.textViewDate = convertView.findViewById(R.id.date);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the text view
        if (current.getRecordType() == 0 ){
            viewHolder.textViewAmt.setText(getFormattedMoneyStr(current.getAmount()));
            viewHolder.textViewAmt.setTextColor(Color.BLUE);
        } else {
            viewHolder.textViewAmt.setText(" - " + getFormattedMoneyStr(current.getAmount()));
            viewHolder.textViewAmt.setTextColor(Color.RED);
        }
        viewHolder.textViewDesc.setText(current.getDescription());
        viewHolder.textViewType.setText(current.getType());
        viewHolder.textViewDate.setText(getFormattedDate(current.getCreate_timestamp()));

        // Set tag
        viewHolder.btnEdit.setTag(position);
        viewHolder.btnDelete.setTag(position);

        // Set Button Edit Listener
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit Record
                Intent intent = new Intent(context, FormActivity.class);
                Record record = history.get((int) v.getTag());
                intent.putExtra(context.getResources().getString(R.string.recordID), record.getRecordType());
                Gson gson = new Gson();
                String json = gson.toJson(record);
                intent.putExtra("record", json);
                ((Activity) context).startActivityForResult(intent, UPDATE_RECORD_REQUEST_CODE);
            }
        });


        // Set Button Delete Listener
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete Record
                recordViewModel.delete(history.get((int) v.getTag()).getId());
                recordViewModel.getRecordByMonth(startDate, endDate);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check the request type
        if (requestCode == UPDATE_RECORD_REQUEST_CODE) {
            // Check the replied status
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("record")) {
                    Gson gson = new Gson();
                    String json = data.getExtras().getString("record");
                    if (json != null) {
                        Record record = gson.fromJson(json, Record.class);
                        recordViewModel.update(record);
                        recordViewModel.getRecordByMonth(startDate, endDate);
                    }
                }
            }
        }
    }

    // Method: Get formatted money string
    private String getFormattedMoneyStr(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String moneyString = formatter.format(amount);

        return moneyString;
    }

    // Method: Get formatted date
    private String getFormattedDate(Calendar date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATEFORMAT, locale);
        return dateFormatter.format(date.getTime());
    }
}
