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

public class DescriptionArrayAdapter extends ArrayAdapter<String> {
    private final String[] values;

    DescriptionArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_descriptions, values);
        this.values = values;
    }


    private static class ViewHolder {
        TextView textViewType;
        ImageView imageViewBullet;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_descriptions, parent, false);
            viewHolder.textViewType = convertView.findViewById(R.id.type);
            viewHolder.imageViewBullet = convertView.findViewById(R.id.bullet);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewType.setText(values[position]);
        viewHolder.imageViewBullet.setImageResource(R.drawable.ic_bullet_entertainment);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
