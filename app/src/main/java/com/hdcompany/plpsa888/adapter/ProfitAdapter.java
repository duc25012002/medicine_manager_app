package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemProfitBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Profit;

import java.util.List;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder> {

    private final List<Profit> list;
    private final IManagerProfitListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerProfitListener {
        void onClickItem(Profit model);
    }

    /* KHỞI TẠO */
    public ProfitAdapter(List<Profit> mProfitList, IManagerProfitListener iManagerProfitListener) {
        this.list = mProfitList;
        this.listener = iManagerProfitListener;
    }

    /* VIEW HOLDER */
    public static class ProfitViewHolder extends RecyclerView.ViewHolder {
        public ItemProfitBinding profitBinding;

        public ProfitViewHolder(ItemProfitBinding profitBinding) {
            super(profitBinding.getRoot());
            this.profitBinding = profitBinding;
        }
    }

    @NonNull
    @Override
    public ProfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemProfitBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_profit;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new ProfitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfitViewHolder holder, int position) {
        Profit profit = list.get(position);
        if(profit == null){
            return;
        }

        String STT = String.valueOf(position + 1);
        holder.profitBinding.tvStt.setText(STT);
        holder.profitBinding.setProfit(profit);
        holder.profitBinding.layoutItemProfit.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.onClickItem(profit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
