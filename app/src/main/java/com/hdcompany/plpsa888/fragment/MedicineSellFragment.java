package com.hdcompany.plpsa888.fragment;

import static com.hdcompany.plpsa888.constant.GlobalFunction.showToast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hdcompany.plpsa888.PLPSAApplication;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.HistoryAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.FragmentSellMedicineBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.util.DateTimeUtils;
import com.hdcompany.plpsa888.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicineSellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineSellFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MedicineSellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicineSellFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineSellFragment newInstance(String param1, String param2) {
        MedicineSellFragment fragment = new MedicineSellFragment();
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

    public MedicineSellFragment(Medicine medicine) {
        this.medicine = medicine;
    }

    public ClickHandlers clickHandlers;
    public FragmentSellMedicineBinding sellMedicineBinding;

    /* KHỞI TẠO BIẾN */
    private Context context;
    private Medicine medicine;
    private List<History> historyList;
    private HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sellMedicineBinding = FragmentSellMedicineBinding.inflate(inflater, container, false);
        this.context = sellMedicineBinding.getRoot().getContext();

        clickHandlers = new ClickHandlers();

        sellMedicineBinding.setClickHandlers(clickHandlers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        sellMedicineBinding.rcvHistory.setLayoutManager(linearLayoutManager);

        historyList = new ArrayList<>();
        getListHistoryAdded();

        historyAdapter = new HistoryAdapter(historyList, true, new HistoryAdapter.IManagerHistoryListener() {
            @Override
            public void edit(History model) {
                onClickAddOrEditHistory(model);
            }

            @Override
            public void delete(History model) {
                onClickDeleteHistory(model);
            }

            @Override
            public void onClickItem(History model) {

            }
        });
        sellMedicineBinding.rcvHistory.setAdapter(historyAdapter);
        return sellMedicineBinding.getRoot();
    }

    /* CHO BINDING ClickHandlers ở bên XML sử dụng */
    public class ClickHandlers {
        public void onClickFab(View view) {
            onClickAddOrEditHistory(null);
        }
    }

    private void onClickAddOrEditHistory(History model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_detail_medicine_edit);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Get view
        final TextView tvTitleDialog = dialog.findViewById(R.id.tv_title_dialog);
        final TextView tvMedName = dialog.findViewById(R.id.tv_medicine_name);
        final EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        final TextView tvUnitName = dialog.findViewById(R.id.tv_measurement_name);
        final EditText edtPrice = dialog.findViewById(R.id.edt_price);
        final TextView tvDialogCancel = dialog.findViewById(R.id.tv_dialog_cancel);
        final TextView tvDialogAdd = dialog.findViewById(R.id.tv_dialog_add);

        // Set data
        if (model == null) {
            tvTitleDialog.setText(getString(R.string.feature_medicine_used));
            tvMedName.setText(medicine.getMedicineName());
            tvUnitName.setText((medicine.getMeasurementName()));
        } else {
            tvTitleDialog.setText(getString(R.string.edit_history_used));
            tvMedName.setText(model.getMedicineName());
            tvUnitName.setText(model.getMeasurementName());
            edtQuantity.setText(String.valueOf(model.getQuantity()));
            edtPrice.setText(String.valueOf(model.getUnitPrice()));
        }

        // Listener
        tvDialogCancel.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.dismiss();
            }
        });

        tvDialogAdd.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String strQuantity = edtQuantity.getText().toString().trim();
                String strPrice = edtPrice.getText().toString().trim();
                if (StringUtil.isEmpty(strQuantity) || StringUtil.isEmpty(strPrice)) {
                    showToast(context, getString(R.string.msg_enter_full_infor));
                    return;
                }

                if (model == null) {
                    History history = new History();
                    history.setHistoryId(String.valueOf(System.currentTimeMillis()));
                    history.setMedicineId(medicine.getMedicineId());
                    history.setMedicineName(medicine.getMedicineName());
                    history.setMeasurementId(medicine.getMeasurementId());
                    history.setMedicineDesc(medicine.getMedicineDesc());
                    // Lấy tên định lượng
                    history.setMeasurementName(medicine.getMeasurementName());

                    history.setQuantity(strQuantity);
                    history.setUnitPrice(strPrice);
                    history.setTotalPrice();
                    history.setAddMedicine(false);

                    String currentDate = new SimpleDateFormat(DateTimeUtils.DEFAULT_FORMAT_DATE, Locale.ENGLISH).format(new Date());
                    String strDate = DateTimeUtils.convertDateToTimeStamp(currentDate);
                    history.setDate(Long.parseLong(strDate));

                    if (context != null) {
                        PLPSAApplication.get(context).getHistoryDatabaseRef()
                                .child(history.getHistoryId())
                                .setValue(history, (error, ref) -> {
                                    showToast(context, getString(R.string.msg_used_med_success));
                                    changeQuantity(history.getMedicineId(), history.getQuantity(), false);
                                    GlobalFunction.hideSoftKeyboard(getActivity());
                                    dialog.dismiss();
                                });
                    }
                } else {
                    // Edit history
                    Map<String, Object> map = new HashMap<>();
                    map.put("quantity", Integer.parseInt(strQuantity));
                    map.put("unitPrice", Integer.parseInt(strPrice));
                    map.put("totalPrice", Integer.parseInt(strQuantity) * Integer.parseInt(strPrice));

                    if (context != null) {
                        PLPSAApplication.get(context).getHistoryDatabaseRef()
                                .child(model.getHistoryId())
                                .updateChildren(map, (error, ref) -> {
                                    GlobalFunction.hideSoftKeyboard(getActivity());
                                    showToast(context, getString(R.string.msg_edit_used_history_success));
                                    changeQuantity(model.getMedicineId(), StringUtil.getDifference(strQuantity ,model.getQuantity()), false);

                                    dialog.dismiss();
                                });
                    }
                }
            }
        });

        dialog.show();
    }
    private void changeQuantity(String medicine_id, String quantity, boolean isAdd) {
        if (context == null) {
            return;
        }
        PLPSAApplication.get(context).getQuantityDatabaseRef(medicine_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentQuantity = snapshot.getValue(String.class);
                        if (currentQuantity != null) {
                            String totalQuantity;
                            if (isAdd) {
                                totalQuantity = StringUtil.getSum(currentQuantity , quantity);
                            } else {
                                totalQuantity = StringUtil.getDifference(currentQuantity , quantity);
                            }
                            if (context != null) {
                                PLPSAApplication.get(context).getQuantityDatabaseRef(medicine_id).removeEventListener(this);
                                PLPSAApplication.get(context).getQuantityDatabaseRef(medicine_id).setValue(totalQuantity);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getListHistoryAdded() {
        if (context == null) {
            return;
        }
        PLPSAApplication.get(context).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (historyList != null) {
                            historyList.clear();
                        }

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (history != null) {
                                if (medicine.getMedicineId().equals(history.getMedicineId()) && !history.isAddMedicine()) {
                                    historyList.add(0, history);
                                }
                            }
                        }
                        historyAdapter.notifyDataSetChanged();

                        displayLayoutBottomInfor();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, getString(R.string.msg_get_data_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickDeleteHistory(History model) {
        if (context == null) {
            return;
        }
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_delete), (dialogInterface, i)
                        -> PLPSAApplication.get(context).getHistoryDatabaseRef()
                        .child(String.valueOf(model.getHistoryId()))
                        .removeValue((error, ref) -> {
                            showToast(context, getString(R.string.msg_delete_used_history_success));
                            changeQuantity(model.getMedicineId(), model.getQuantity(), true);
                            GlobalFunction.hideSoftKeyboard(getActivity());
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }
    private void displayLayoutBottomInfor() {
        // Calculator quantity
        // Lấy tên định lượng
        String strTotalQuantity = getTotalQuantity();
        // Calculator price
        String strTotalPrice = getTotalPrice() + Constant.CURRENCY;
        sellMedicineBinding.tvTotalPrice.setText(strTotalPrice);
        sellMedicineBinding.tvTotalQuantity.setText(strTotalQuantity);
    }

    private String getTotalQuantity() {
        if (historyList == null || historyList.isEmpty()) {
            return "0";
        }

        String totalQuantity = "0";
        for (History history : historyList) {
            totalQuantity = StringUtil.getSum(totalQuantity,history.getQuantity());
        }
        return StringUtil.getDottedNumber(totalQuantity);
    }

    private String getTotalPrice() {
        if (historyList == null || historyList.isEmpty()) {
            return "0";
        }

        String totalPrice = "0";
        for (History history : historyList) {
            totalPrice = StringUtil.getSum(history.getTotalPrice(),totalPrice);
        }
        return StringUtil.getDottedNumber(totalPrice);
    }

}