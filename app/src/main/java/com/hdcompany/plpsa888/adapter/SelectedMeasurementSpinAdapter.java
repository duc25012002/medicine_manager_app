package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.model.Measurement;

import java.util.List;

public class SelectedMeasurementSpinAdapter extends ArrayAdapter<Measurement> {
    private final Context context;

    public SelectedMeasurementSpinAdapter(@NonNull Context context, int layoutItem, @NonNull List<Measurement> measurementList) {
        super(context, layoutItem, measurementList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_choose_option, null);
            TextView tvSelected = convertView.findViewById(R.id.tv_selected);
            tvSelected.setText(this.getItem(position).getMeasurementName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_drop_down_option, null);
        TextView tvName = view.findViewById(R.id.textview_name);
        tvName.setText(this.getItem(position).getMeasurementName());
        return view;
    }
}
