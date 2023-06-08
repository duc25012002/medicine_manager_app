package com.hdcompany.plpsa888.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.activity.ViewMeasurementActivity;
import com.hdcompany.plpsa888.activity.ViewMedListActivity;
import com.hdcompany.plpsa888.activity.ViewMedOutOfStockActivity;
import com.hdcompany.plpsa888.activity.ViewProfitActivity;
import com.hdcompany.plpsa888.activity.ViewStatisticalActivity;
import com.hdcompany.plpsa888.adapter.MainFragmentAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.FragmentMainBinding;
import com.hdcompany.plpsa888.model.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Context context;
    public FragmentMainBinding mainBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainBinding = FragmentMainBinding.inflate(inflater, container, false);

        /* GÁN CONTEXT */
        this.context = mainBinding.getRoot().getContext();

        /* TẠO DANH SÁCH DẠNG LƯỚI CHO RCV */
        mainBinding.rcvFeature.setLayoutManager(new GridLayoutManager(getContext(), 2));

        /* SET ADAPTER */
        mainBinding.rcvFeature.setAdapter(new MainFragmentAdapter(getListFeatures(), this::onItemClickedFeature));

        /* TRẢ VỀ VIEW */
        return mainBinding.getRoot();
    }

    /* LẤY VỀ DANH SÁCH CHỨC NĂNG HIỂN THỊ TRONG DANH SÁCH LƯỚI*/
    private List<Feature> getListFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new Feature(Feature.FEATURE_VIEW_LIST_MED, R.drawable.ic_med_list_2, getString(R.string.label_list_med)));
        features.add(new Feature(Feature.FEATURE_VIEW_MEASUREMENT, R.drawable.ic_measurement_2, getString(R.string.label_measurement)));
        features.add(new Feature(Feature.FEATURE_VIEW_PROFIT, R.drawable.ic_profit, getString(R.string.label_profit)));
        features.add(new Feature(Feature.FEATURE_VIEW_COST, R.drawable.ic_cost_2, getString(R.string.label_cost)));
//        features.add(new Feature(Feature.FEATURE_VIEW_STATISTICAL, R.drawable.ic_statistical, getString(R.string.label_statistical)));
        features.add(new Feature(Feature.FEATURE_VIEW_REVENUE, R.drawable.ic_revenue, getString(R.string.label_revenue)));
        features.add(new Feature(Feature.FEATURE_VIEW_OUT_OF_STOCK, R.drawable.ic_out_of_stock_2, getString(R.string.label_out_of_stock)));
        return features;
    }

    /* ĐẶT CÁC SỰ KIỆN CLICK */
    private void onItemClickedFeature(Feature feature) {
        switch (feature.getId()) {
            case Feature.FEATURE_VIEW_MEASUREMENT:
                GlobalFunction.startActivity(getContext(), ViewMeasurementActivity.class);
                GlobalFunction.showToast(context, "Đơn vị");
                break;

            case Feature.FEATURE_VIEW_COST:
                goToViewStatisticalActivity(Constant.TYPE_COST);
                GlobalFunction.showToast(context, "Tổng chi phí thuốc đã nhập");
                break;

            case Feature.FEATURE_VIEW_PROFIT:
                GlobalFunction.startActivity(context, ViewProfitActivity.class, "Lợi Nhuận");
                GlobalFunction.showToast(context, "Số thuốc còn lại");
                break;

            case Feature.FEATURE_VIEW_LIST_MED:
                GlobalFunction.startActivity(context, ViewMedListActivity.class);
                GlobalFunction.showToast(context, "Danh sách thuốc");
                break;
            case Feature.FEATURE_VIEW_OUT_OF_STOCK:
                GlobalFunction.startActivity(context, ViewMedOutOfStockActivity.class);
                GlobalFunction.showToast(context, "Thuốc bán hết");
                break;

            case Feature.FEATURE_VIEW_REVENUE:
                goToViewStatisticalActivity(Constant.TYPE_REVENUE);
                GlobalFunction.showToast(context, "Tổng tiền thuốc đã bán");
                break;

            default: // This is Statistical
                goToListMedPopular();
                GlobalFunction.showToast(context, "Thống kê");
                break;
        }
    }

    /* ĐẶT LOẠI THỐNG KÊ Statistical */
    private void goToViewStatisticalActivity(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_TYPE_STATISTICAL, type);
        GlobalFunction.startActivity(getContext(), ViewStatisticalActivity.class, bundle);
    }

    private void goToListMedPopular() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_TYPE_STATISTICAL, Constant.TYPE_REVENUE);
        bundle.putBoolean(Constant.KEY_MEDICINE_POPULAR, true);
        GlobalFunction.startActivity(getContext(), ViewStatisticalActivity.class, bundle);
    }
}