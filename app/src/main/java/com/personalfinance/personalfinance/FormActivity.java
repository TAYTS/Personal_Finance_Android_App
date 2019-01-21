package com.personalfinance.personalfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {
    private int recordType;
    private TextView textViewTitle;
    private EditText editTextAmount;
    private EditText editTextDescription;
    private EditText editTextDate;
    private Spinner spinnerType;
    private Button buttonAddRecord;
    private Calendar calendar;
    private Locale locale = new Locale("en","MY");
    private String[] typesArray;
    private Record record;

    // Define constant
    private static final String DATEFORMAT = "dd MMMM yyyy";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);

        // Set the record type
        Intent intent = getIntent();
        recordType = intent.getIntExtra(getResources().getString(R.string.recordID), 0);

        // Get Calendar instance
        calendar = Calendar.getInstance(locale);


        // Get all the views
        textViewTitle = findViewById(R.id.formTitle);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editDescription);
        editTextDate = findViewById(R.id.editCalender);
        spinnerType = findViewById(R.id.spinnerType);
        buttonAddRecord = findViewById(R.id.buttonAddRecord);


        // Set initial values (Title, TYPE string array, calendar)
        if (recordType == 0) {
            textViewTitle.setText(R.string.recordIncomeTitle);
            typesArray = getResources().getStringArray(R.array.income_type);
        } else {
            textViewTitle.setText(R.string.recordExpenseTitle);
            typesArray = getResources().getStringArray(R.array.expenses_type);
        }
        updateLabel(calendar);


        // Disable Edit Text Amount from Select All and Paste operation
        editTextAmount.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                return false;
            }
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });

        // Edit Text Amount listener for currency formatting
        editTextAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTextAmount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String textAmount = s.toString().replaceAll("[RM][ ]*", "");

                            // Prevent infinity loop
                            editTextAmount.removeTextChangedListener(this);
                            textAmount = currencyFormatter(textAmount);

                            // Update the Edit Text Amount
                            editTextAmount.setText(textAmount);
                            editTextAmount.setSelection(textAmount.length());
                            editTextAmount.addTextChangedListener(this);
                        }
                    });
                } else {
                    // Clean up procedure for Edit Text Amount
                    String textAmount = editTextAmount.getText().toString();
                    String appendZero = "";
                    if (!textAmount.isEmpty()) {
                        if (textAmount.contains(".")) {
                            // Stop at 2 decimal
                            int fractionLength = textAmount.substring(textAmount.indexOf(".")).length() - 1;

                            if (fractionLength == 0)
                                appendZero = "00";
                            else if (fractionLength == 1)
                                appendZero = "0";

                        } else {
                            appendZero = ".00";
                        }
                        textAmount = textAmount + appendZero;
                        editTextAmount.setText(textAmount);
                    }
                }
            }
        });

        // Edit Text Calendar listener for trigger DatePickerDialog
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FormActivity.this,
                        R.style.DatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                updateLabel(calendar);
                            }
                        }, year, month, day_of_month
                );


                // DatePickerDialog Settings
                datePickerDialog.setIcon(R.drawable.ic_calendar);
                datePickerDialog.setTitle("Please select the date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        // Spinner
        // Create ArrayAdapter for the Type Spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
                this, R.layout.type_spinner, typesArray) {

            // Disable the first option
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            // Change the first option color to GREY
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0)
                    textView.setTextColor(Color.GRAY);
                else
                    textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        // Set the dropdown style for the Spinner
        typeAdapter.setDropDownViewResource(R.layout.type_spinner_dropdown);
        // Add the custom ArrayAdapter to Spinner
        spinnerType.setAdapter(typeAdapter);


        // Set initial value for update record
        Gson gson = new Gson();
        String json = intent.getStringExtra("record");
        if (json != null) {
            record = gson.fromJson(json, Record.class);
            editTextAmount.setText(currencyFormatter(record.getAmount().toPlainString()));
            editTextDescription.setText(record.getDescription());
            updateLabel(record.getCreate_timestamp());
            int pos = typeAdapter.getPosition(record.getType());
            spinnerType.setSelection(pos);
            buttonAddRecord.setText(R.string.buttonTextUpdate);
        }


        // Add/Update Button: Add/Update Record Listener
        buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check all the field has filled
                String amount = editTextAmount.getText().toString().replaceAll("[RM][ ]*", "");
                String description = editTextDescription.getText().toString();
                String date = editTextDate.getText().toString();
                String type = spinnerType.getSelectedItemPosition() > 0 ? spinnerType.getSelectedItem().toString() : "";

                if (!amount.isEmpty() && !description.isEmpty() && !date.isEmpty() && !type.isEmpty()) {
                    BigDecimal amtBigDecimal = new BigDecimal(amount);
                    if (amtBigDecimal.compareTo(BigDecimal.ZERO) > 0) {
                        Intent replyIntent = new Intent();
                        Gson gson = new Gson();
                        String json;
                        if (record != null) {
                            record.setAmount(amtBigDecimal);
                            record.setDescription(description);
                            record.setType(type);
                            record.setCreate_timestamp(calendar);
                            json = gson.toJson(record);
                        } else {
                            Record newRecord = new Record(amtBigDecimal, description, type, recordType, calendar);
                            json = gson.toJson(newRecord);
                        }
                        replyIntent.putExtra("record", json);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                }
            }
        });


    }


    // Method: Update the Edit Text Calendar text
    private void updateLabel(Calendar date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATEFORMAT, locale);
        editTextDate.setText(dateFormatter.format(date.getTime()));
    }

    // Method: Format currency string
    private String currencyFormatter(String textAmount) {
        if (!textAmount.isEmpty()) {
            textAmount = "RM" + textAmount;

            if (textAmount.contains(".")) {
                // Prevent multiple "."
                textAmount.split("\\.", -1);

                // Stop at 2 decimal
                int fractionLength = textAmount.substring(textAmount.indexOf(".")).length() - 1;
                if (fractionLength > 2) {
                    textAmount = textAmount.substring(0, textAmount.indexOf(".") + 3);
                }
            }
        }
        return textAmount;
    }
}




