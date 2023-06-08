package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemMeasurementBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Measurement;

import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder> {

    private final List<Measurement> list;
    private final IManagerMeasurementListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerMeasurementListener{
        void edit(Measurement measurement);
        void delete(Measurement measurement);
    }

    /* KHỞI TẠO */
    public MeasurementAdapter(List<Measurement> measurementList, IManagerMeasurementListener iManagerMeasurementListener) {
        this.list = measurementList;
        this.listener = iManagerMeasurementListener;
    }

    /* VIEW HOLDER */
    public static class MeasurementViewHolder extends RecyclerView.ViewHolder{
        public ItemMeasurementBinding measurementBinding;

        public MeasurementViewHolder(ItemMeasurementBinding measurementBinding) {
            super(measurementBinding.getRoot());
            this.measurementBinding = measurementBinding;
        }
    }

    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemMeasurementBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_measurement;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new MeasurementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        Measurement measurement = list.get(position);
        if (measurement == null) {
            return;
        }
        holder.measurementBinding.setMeasurement(measurement);

        holder.measurementBinding.layoutItemMeasurement.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
            }
        });
        holder.measurementBinding.imgDelete.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.delete(measurement);
            }
        });
        holder.measurementBinding.imgEdit.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.edit(measurement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
