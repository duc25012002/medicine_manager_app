package com.hdcompany.plpsa888.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemMedicineDetailBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Medicine;

import java.util.List;

public class MedicineDetailDapter extends RecyclerView.Adapter<MedicineDetailDapter.ManagerMedicineViewHolder>{
    private List<Medicine> mMedicineList;
    private IManagerMedicineListener iManagerMedicineListener;

    @NonNull
    @Override
    public ManagerMedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMedicineDetailBinding itemMedicineDetailBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_medicine_detail,
                parent,false
        );
        return new ManagerMedicineViewHolder(itemMedicineDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerMedicineViewHolder holder, int position) {
        Medicine medicine = mMedicineList.get(position);
        if(medicine == null) return;
        holder.mItemMedicineDetailBinding.setMedicine(medicine);
        holder.mItemMedicineDetailBinding.layoutItemMedicineDetail.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                iManagerMedicineListener.clickedItem(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMedicineList.size();
    }

    public interface IManagerMedicineListener {
        void clickedItem(Medicine model);
    }

    public MedicineDetailDapter(List<Medicine> mMedicineList, IManagerMedicineListener iManagerMedicineListener) {
        this.mMedicineList = mMedicineList;
        this.iManagerMedicineListener = iManagerMedicineListener;
    }

    public static class ManagerMedicineViewHolder extends RecyclerView.ViewHolder{
        public ItemMedicineDetailBinding mItemMedicineDetailBinding;

        public ManagerMedicineViewHolder(ItemMedicineDetailBinding mItemMedicineDetailBinding) {
            super(mItemMedicineDetailBinding.getRoot());
            this.mItemMedicineDetailBinding = mItemMedicineDetailBinding;
        }
    }
}
