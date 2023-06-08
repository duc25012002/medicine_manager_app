package com.hdcompany.plpsa888.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.activity.ViewHistoryActivity;
import com.hdcompany.plpsa888.adapter.ModifierFragmentAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.FragmentModifierBinding;
import com.hdcompany.plpsa888.model.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifierFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModifierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifierFragment newInstance(String param1, String param2) {
        ModifierFragment fragment = new ModifierFragment();
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
    public FragmentModifierBinding modifierBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        modifierBinding = FragmentModifierBinding.inflate(inflater, container, false);

        /* GÁN CONTEXT */
        this.context = modifierBinding.getRoot().getContext();

        /* TẠO DANH SÁCH DẠNG LƯỚI CHO RCV */
        modifierBinding.rcvModifier.setLayoutManager(new GridLayoutManager(context, 1));

        /* SET ADAPTER */
        modifierBinding.rcvModifier.setAdapter(new ModifierFragmentAdapter(getListFeatures(), this::onItemClickedFeature));

        /* TRẢ VỀ VIEW */
        return modifierBinding.getRoot();
    }

    private List<Feature> getListFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new Feature(Feature.FEATURE_MED_IN, R.drawable.ic_health_med_in, getString(R.string.label_med_in)));
        features.add(new Feature(Feature.FEATURE_MED_OUT, R.drawable.ic_health_med_out, getString(R.string.label_med_out)));
        return features;
    }

    private void onItemClickedFeature(Feature feature) {
        Bundle bundle = new Bundle();
        switch (feature.getId()) {
            case Feature.FEATURE_MED_IN:
                bundle.putBoolean(Constant.KEY_INTENT_MEDICINE_SOLD, false);
                GlobalFunction.startActivity(context, ViewHistoryActivity.class, bundle);
                GlobalFunction.showToast(context, "Nhập thuốc");
                break;
            case Feature.FEATURE_MED_OUT:
                bundle.putBoolean(Constant.KEY_INTENT_MEDICINE_SOLD, true);
                GlobalFunction.startActivity(context, ViewHistoryActivity.class, bundle);
                GlobalFunction.showToast(context, "Xuất thuốc");
                break;
            default:
        }
    }
}