package com.hdcompany.plpsa888.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.databinding.ItemFeatureBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.Feature;

import java.util.List;

/* FEATURE ADAPTER */
public class MainFragmentAdapter  extends RecyclerView.Adapter<MainFragmentAdapter.FeatureViewHolder>{

    private final List<Feature> list;
    private final IManagerFeatureListener listener;

    /* ON CLICK INTERFACE */
    public interface IManagerFeatureListener{
        void onClickItem(Feature feature);
    }

    /* KHỞI TẠO */
    public MainFragmentAdapter(List<Feature> featureList, IManagerFeatureListener iManagerFeatureListener) {
        this.list = featureList;
        this.listener = iManagerFeatureListener;
    }

    /* VIEW HOLDER */
    public class FeatureViewHolder extends RecyclerView.ViewHolder{
        public ItemFeatureBinding featureBinding;

        public FeatureViewHolder(ItemFeatureBinding mItemFeatureBinding) {
            super(mItemFeatureBinding.getRoot());
            this.featureBinding = mItemFeatureBinding;

            mItemFeatureBinding.getRoot().setOnClickListener(new IOnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    if(listener != null && clickedPosition != RecyclerView.NO_POSITION){
                        listener.onClickItem(list.get(clickedPosition));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* TẠO BIẾN ĐỂ RÚT NGẮN DÒNG LỆNH CHO DỄ NHÌN */
        ItemFeatureBinding binding;
        Context context = parent.getContext();
        int layout = R.layout.item_feature;
        /* TẠO VIEW */
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false);
        /* TRẢ VỀ VIEW */
        return new FeatureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        Feature feature = list.get(position);
        holder.featureBinding.setFeature(feature);
        holder.featureBinding.imgFeature.setImageResource(feature.getImage());
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }
}
