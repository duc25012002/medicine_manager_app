package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemHistoryBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.util.DateTimeUtils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private final List<History> list;
    private final boolean isShowDate;
    private final IManagerHistoryListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerHistoryListener {
        void edit(History history);
        void delete(History history);
        void onClickItem(History history);
    }

    /* KHỞI TẠO */
    public HistoryAdapter(List<History> historyList, boolean isShowDate, IManagerHistoryListener iManagerHistoryListener) {
        this.list = historyList;
        this.isShowDate = isShowDate;
        this.listener = iManagerHistoryListener;
    }

    /* VIEW HOLDER */
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding historyBinding;

        public HistoryViewHolder(ItemHistoryBinding historyBinding) {
            super(historyBinding.getRoot());
            this.historyBinding = historyBinding;
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemHistoryBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_history;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = list.get(position);
        /* ĐẶT GÁN HISTORY ĐỂ HIỂN THỊ VÀO ITEM */
        holder.historyBinding.setHistory(history);
        /* NẾU LÀ THUỐC ĐƯỢC MUA VÀO - NHẬP VỀ */
        if(history.isAddMedicine()){
            /* LỊCH SỬ SẼ CÓ MÀU XANH */
            holder.historyBinding.layoutItemHistory.setBackgroundResource(R.drawable.bg_tv_green_1);
        }
        /* NẾU LÀ THUỐC ĐƯỢC MUA VÀO - NHẬP VỀ */
        else{
            /* LỊCH SỬ SẼ CÓ MÀU CAM */
            holder.historyBinding.layoutItemHistory.setBackgroundResource(R.drawable.bg_tv_orange_1);
        }

        if (isShowDate) {
            holder.historyBinding.tvDate.setVisibility(View.VISIBLE);
            holder.historyBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(String.valueOf(history.getDate())));
            holder.historyBinding.layoutItemHistory.setOnClickListener(null);
        } else {
            holder.historyBinding.tvDate.setVisibility(View.GONE);
            holder.historyBinding.layoutItemHistory.setOnClickListener(v -> {
                listener.onClickItem(history);
            });
        }
        /* SET EVENT CLICK ON DELETE AND EDIT */
        holder.historyBinding.imgDelete.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.delete(history);
            }
        });
        holder.historyBinding.imgEdit.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                listener.edit(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
