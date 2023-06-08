package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemStatisticalBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Statistical;

import java.util.List;

public class StatisticalAdapter extends RecyclerView.Adapter<StatisticalAdapter.StatisticalViewHolder>{

    private final List<Statistical> list;
    private final IManagerStatisticalListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerStatisticalListener {
        void onClickItem (Statistical statistical);
    }

    /* KHỞI TẠO */
    public StatisticalAdapter(List<Statistical> mStatisticalList, IManagerStatisticalListener iManagerStatisticalListener) {
        this.list = mStatisticalList;
        this.listener = iManagerStatisticalListener;
    }

    /* VIEW HOLDER */
    public static class StatisticalViewHolder extends RecyclerView.ViewHolder{
        public ItemStatisticalBinding statisticalBinding;

        public StatisticalViewHolder(ItemStatisticalBinding statisticalBinding) {
            super(statisticalBinding.getRoot());
            this.statisticalBinding = statisticalBinding;
        }
    }

    @NonNull
    @Override
    public StatisticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemStatisticalBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_statistical;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new StatisticalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticalViewHolder holder, int position) {
        Statistical statistical = list.get(position);
        if(statistical == null){
            return;
        }

        String STT = String.valueOf(position + 1);
        holder.statisticalBinding.tvStt.setText(STT);
        holder.statisticalBinding.setStatistical(statistical);
        holder.statisticalBinding.layoutItemStatistical.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.onClickItem(statistical);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
