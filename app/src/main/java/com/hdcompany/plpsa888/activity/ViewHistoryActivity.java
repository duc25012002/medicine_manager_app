package com.hdcompany.plpsa888.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hdcompany.plpsa888.PLPSAApplication;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.HistoryAdapter;
import com.hdcompany.plpsa888.adapter.SelectedMedicineSpinAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewHistoryBinding;
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
import java.util.Objects;

public class ViewHistoryActivity extends BaseActivity {

    private List<Medicine> medicineList;
    private List<History> historyList;
    private HistoryAdapter historyAdapter;
    private Medicine selectedMed;
    private boolean isMedSold;
    private String currentDate = new SimpleDateFormat(DateTimeUtils.DEFAULT_FORMAT_DATE, Locale.ENGLISH).format(new Date());
    private final Context context = this;

    public ViewHistoryClickHandlers clickHandlers;
    public ActivityViewHistoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewHistoryBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        clickHandlers = new ViewHistoryClickHandlers();
        mBinding.setClickHandlers(clickHandlers);

        getDataIntent();
        if (isMedSold) {
            mBinding.tvListTitle.setText(getString(R.string.label_history_list_med_sold));
        } else {
            mBinding.tvListTitle.setText(getString(R.string.label_history_list_med_in));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rcvHistory.setLayoutManager(linearLayoutManager);

        medicineList = new ArrayList<>();
        getMedicineList();
        historyList = new ArrayList<>();

        historyAdapter = new HistoryAdapter(historyList, false,
                new HistoryAdapter.IManagerHistoryListener() {
                    @Override
                    public void edit(History model) {
                        onClickAddOrEditHistory(model);
                    }

                    @Override
                    public void delete(History model) {
                        DeleteHistory(model);
                    }

                    @Override
                    public void onClickItem(History model) {
                        Medicine med = new Medicine(model.getMedicineId(), model.getMeasurementId(),
                                model.getMeasurementName(), model.getMedicineName());
                        GlobalFunction.goToMedicineDetailActivity(ViewHistoryActivity.this, med);
                    }
                });
        mBinding.rcvHistory.setAdapter(historyAdapter);
        mBinding.rcvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mBinding.fabAddData.hide();
                } else {
                    mBinding.fabAddData.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mBinding.tvDateSelected.setText(currentDate);
        getListHistoryMedOfDate(currentDate);
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        isMedSold = bundle.getBoolean(Constant.KEY_INTENT_MEDICINE_SOLD);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class ViewHistoryClickHandlers {
        public void onClickSelectDate(View view) {
            GlobalFunction.showDatePicker(ViewHistoryActivity.this, mBinding.tvDateSelected.getText().toString(), date -> {
                mBinding.tvDateSelected.setText(date);
                getListHistoryMedOfDate(date);
            });
        }

        public void onClickFab(View view) {
            onClickAddOrEditHistory(null);
        }
    }

    private void onClickAddOrEditHistory(History model) {
        // Không có loại thuốc nào thì không thể thêm
        if (medicineList == null || medicineList.isEmpty()) {
            showToast(getString(R.string.msg_list_milk_require));
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_history);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Get view
        final TextView tvTitleDialog = dialog.findViewById(R.id.tv_title_dialog);
        final Spinner spnMed = dialog.findViewById(R.id.spinner_medicine);
        final EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        final TextView tvUnitName = dialog.findViewById(R.id.tv_measurement_name);
        final EditText edtPrice = dialog.findViewById(R.id.edt_price);
        final TextView tvDialogCancel = dialog.findViewById(R.id.tv_dialog_cancel);
        final TextView tvDialogAdd = dialog.findViewById(R.id.tv_dialog_add);

        // Set data
        if (isMedSold) {
            tvTitleDialog.setText(getString(R.string.feature_med_sold));
        } else {
            tvTitleDialog.setText(getString(R.string.feature_add_med));
        }

        SelectedMedicineSpinAdapter selectMedicineAdapter = new SelectedMedicineSpinAdapter(this, R.layout.item_choose_option, medicineList);
        spnMed.setAdapter(selectMedicineAdapter);
        spnMed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMed = selectMedicineAdapter.getItem(position);
                tvUnitName.setText(selectedMed.getMeasurementName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (model != null) {
            if (isMedSold) {
                tvTitleDialog.setText(getString(R.string.edit_history_used));
            } else {
                tvTitleDialog.setText(getString(R.string.edit_history_add));
            }
            spnMed.setSelection(getPositionMedUpdate(model));
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
                    showToast(getString(R.string.msg_enter_full_infor));
                    return;
                }

                if (model == null) {
                    History history = new History();
                    history.setHistoryId(String.valueOf(System.currentTimeMillis()));
                    history.setMedicineId(selectedMed.getMedicineId());
                    history.setMeasurementId(selectedMed.getMeasurementId());
                    history.setMedicineName(selectedMed.getMedicineName());
                    history.setMeasurementName(selectedMed.getMeasurementName());
                    history.setQuantity(strQuantity);
                    history.setUnitPrice(strPrice);
                    history.setTotalPrice();
                    history.setAddMedicine(!isMedSold);
                    String strDate = DateTimeUtils.convertDateToTimeStamp(mBinding.tvDateSelected.getText().toString());
                    history.setDate(Long.parseLong(strDate));
                    history.setMedicineDesc(selectedMed.getMedicineDesc());

                    PLPSAApplication.get(ViewHistoryActivity.this).getHistoryDatabaseRef()
                            .child(history.getHistoryId())
                            .setValue(history, (error, ref) -> {
                                if (isMedSold) {
                                    showToast(getString(R.string.msg_used_med_success));
                                } else {
                                    showToast(getString(R.string.msg_add_med_success));
                                }
                                changeQuantity(history.getMedicineId(), history.getQuantity(), !isMedSold);
                                GlobalFunction.hideSoftKeyboard(ViewHistoryActivity.this);
                                dialog.dismiss();
                            });
                    return;
                }

                // Edit history
                Map<String, Object> map = new HashMap<>();
                map.put("medicineId", selectedMed.getMedicineId());
                map.put("measurementId", selectedMed.getMeasurementId());
                map.put("medicineName", selectedMed.getMedicineName());
                // Lấy tên định lượng
                map.put("measurementName", selectedMed.getMeasurementName());
                map.put("unitQuantity", strQuantity);
                map.put("unitPrice", strPrice);
                map.put("totalPrice", StringUtil.getMultiple(strQuantity, strPrice));
                map.put("medicineDesc", selectedMed.getMedicineDesc());

                PLPSAApplication.get(ViewHistoryActivity.this).getHistoryDatabaseRef()
                        .child(String.valueOf(model.getHistoryId()))
                        .updateChildren(map, (error, ref) -> {
                            GlobalFunction.hideSoftKeyboard(ViewHistoryActivity.this);
                            if (isMedSold) {
                                showToast(getString(R.string.msg_edit_used_history_success));
                            } else {
                                showToast(getString(R.string.msg_edit_add_history_success));
                            }
                            changeQuantity(model.getMedicineId(), StringUtil.getDifference(strQuantity, model.getQuantity()), !isMedSold);

                            dialog.dismiss();
                        });
            }
        });

        dialog.show();
    }

    private void DeleteHistory(History model) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_delete), (dialogInterface, i)
                        -> PLPSAApplication.get(context).getHistoryDatabaseRef()
                        .child(model.getHistoryId())
                        .removeValue((error, ref) -> {
                            if (isMedSold) {
                                GlobalFunction.showToast(context, getString(R.string.msg_delete_sold_history_success));
                            } else {
                                GlobalFunction.showToast(context, getString(R.string.msg_delete_add_history_success));

                            }
                            changeQuantity(model.getMedicineId(), model.getQuantity(), isMedSold);
                            GlobalFunction.hideSoftKeyboard((Activity) context);
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
        // GlobalFunction.showToast(getContext(),"Xóa đang cập nhật");
    }

    private void changeQuantity(String medicineId, String unitQuantity, boolean isAdd) {
        PLPSAApplication.get(context).getQuantityDatabaseRef(medicineId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentQuantity = snapshot.getValue(String.class);
                        if (currentQuantity != null) {
                            String totalQuantity;
                            if (isAdd) {
                                totalQuantity = StringUtil.getSum(currentQuantity, unitQuantity);
                            } else {
                                totalQuantity = StringUtil.getDifference(currentQuantity, unitQuantity);
                            }
                            PLPSAApplication.get(context).getQuantityDatabaseRef(medicineId).removeEventListener(this);
                            updateQuantityToFirebase(medicineId, totalQuantity);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void updateQuantityToFirebase(String medicineId, String quantity) {
        PLPSAApplication.get(context).getQuantityDatabaseRef(medicineId)
                .setValue(quantity);
    }

    private int getPositionMedUpdate(History history) {
        if (medicineList == null || medicineList.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < medicineList.size(); i++) {
            if (history.getMedicineId().equals(medicineList.get(i).getMedicineId())) {
                return i;
            }
        }
        return 0;
    }

    @NonNull
    private String getTotalPrice() {
        if (historyList == null || historyList.isEmpty()) {
            return "0";
        }
        String totalPrice = "0";
        for (History history : historyList) {
            totalPrice = StringUtil.getSum(totalPrice, history.getTotalPrice()).replaceAll("^0", "");
        }
        return StringUtil.getDottedNumber(totalPrice) + Constant.CURRENCY;
    }

    private void getMedicineList() {
        PLPSAApplication.get(context).getMedicineDatabaseRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (medicineList != null) medicineList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Medicine M = dataSnapshot.getValue(Medicine.class);
                    medicineList.add(M);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToast(context, getString(R.string.msg_get_data_error));
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListHistoryMedOfDate(@NonNull String date) {
        long longDate = Long.parseLong(DateTimeUtils.convertDateToTimeStamp(date));
        PLPSAApplication.get(context).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (historyList != null) historyList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (history != null) {
                                if (longDate == history.getDate()) {
                                    addHistoryToList(history);
                                }
                            }
                        }
                        Objects.requireNonNull(mBinding.rcvHistory.getAdapter()).notifyDataSetChanged();

                        // Calculator price
                        String strTotalPrice = getTotalPrice();
                        mBinding.tvTotalPrice.setText(strTotalPrice);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        GlobalFunction.showToast(context, getString(R.string.msg_get_data_error));
                    }
                });
    }

    private void addHistoryToList(History history) {
        if (history == null) {
            return;
        }
        if (isMedSold) {
            if (!history.isAddMedicine()) {
                historyList.add(history);
            }
        } else {
            if (history.isAddMedicine()) {
                historyList.add(history);
            }
        }
    }


}