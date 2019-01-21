package com.personalfinance.personalfinance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/*
 * Populate the Description List View inside the Summary View
 */
public class DescriptionArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private final LinkedHashMap<String, String> descriptions;
    private final HashMap<String, BigDecimal> amount;


    DescriptionArrayAdapter(Context context, LinkedHashMap<String, String> descriptions, HashMap<String, BigDecimal> amount) {
        super(context, R.layout.list_descriptions, new ArrayList<String>(descriptions.keySet()));
        this.context = context;
        this.descriptions = descriptions;
        this.amount = amount;
    }

    private static class ViewHolder {
        ImageView imageViewBullet;
        TextView textViewType;
        TextView textViewAmount;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_descriptions, parent, false);
            viewHolder.imageViewBullet = convertView.findViewById(R.id.bullet);
            viewHolder.textViewType = convertView.findViewById(R.id.type);
            viewHolder.textViewAmount = convertView.findViewById(R.id.amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get type string
        String type = (String)(descriptions.keySet().toArray())[position];

        // Setup the view components
        viewHolder.imageViewBullet.setImageResource(getImgRes(type));
        viewHolder.textViewType.setText(descriptions.get(type));
        viewHolder.textViewAmount.setText(getFormattedMoneyStr(amount.get(type)));

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        // Disable scrolling
        return false;
    }


    // Method: Map the type to the corresponding bullet img
    private int getImgRes(String type) {
        if (type.equals(this.context.getResources().getString(R.string.typeSalary))) {
            return R.drawable.ic_bullet_salary;
        } else if (type.equals(this.context.getResources().getString(R.string.typeEntertainment))) {
            return R.drawable.ic_bullet_entertainment;
        } else if (type.equals(this.context.getResources().getString(R.string.typeFood))) {
            return R.drawable.ic_bullet_food;
        } else if (type.equals(this.context.getResources().getString(R.string.typeLoan))) {
            return R.drawable.ic_bullet_loan;
        } else if (type.equals(this.context.getResources().getString(R.string.typeOthers))) {
            return R.drawable.ic_bullet_others;
        } else {
            return R.drawable.ic_bullet_others;
        }
    }

    // Method: Get formatted money string
    private String getFormattedMoneyStr(BigDecimal amount) {
        Locale locale = new Locale("en","MY");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String moneyString = formatter.format(amount);

        return moneyString;
    }

}
