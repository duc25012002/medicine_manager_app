package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemMedicineBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Medicine;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private final List<Medicine> list;
    private final IManagerMedicineListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerMedicineListener{
        void edit(Medicine medicine);
        void delete(Medicine medicine);
        void onClickItem(Medicine medicine);
    }

    /* KHỞI TẠO */
    public MedicineAdapter(List<Medicine> mMedicineList, IManagerMedicineListener iManagerMedicineListener) {
        this.list = mMedicineList;
        this.listener = iManagerMedicineListener;
    }

    /* VIEW HOLDER */
    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        public ItemMedicineBinding medicineBinding;

        public MedicineViewHolder(ItemMedicineBinding medicineBinding) {
            super(medicineBinding.getRoot());
            this.medicineBinding = medicineBinding;
        }
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemMedicineBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_medicine;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new MedicineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = list.get(position);
        if (medicine == null) {
            return;
        }
        holder.medicineBinding.setMedicine(medicine);

        holder.medicineBinding.layoutItemMedicine.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.onClickItem(medicine);
            }
        });
        holder.medicineBinding.imgBtnDelete.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.delete(medicine);
            }
        });
        holder.medicineBinding.imgBtnEdit.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.edit(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
