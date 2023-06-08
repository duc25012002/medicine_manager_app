package com.hdcompany.plpsa888.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.PLPSAApplication;
import com.hdcompany.plpsa888.adapter.MedicineDetailDapter;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewMedOutOfStockBinding;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.model.Profit;
import com.hdcompany.plpsa888.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewMedOutOfStockActivity extends BaseActivity {

    public ActivityViewMedOutOfStockBinding mBinding;
    public ViewMedOutOfStockClickHandlers mClickHandlers;


    private List<Profit> mListProfit;

    private List<Medicine> medicineList;
    private MedicineDetailDapter manageMedicineAdapter;
    private String mKeySearch;

    private final ChildEventListener mChildEventListener = new ChildEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Medicine medicine = snapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || manageMedicineAdapter == null) {
                return;
            }
            /* KHI SỐ LƯỢNG THUỐC NHỎ HƠN 0: TỨC LÀ BẰNG 0 VÀ CÓ DẤU TRỪ PHÍA TRƯỚC */
            if (medicine.getMedicineQuantity().equals("")||medicine.getMedicineQuantity().equals("0")  || medicine.getMedicineQuantity().contains("-")) {
                if (StringUtil.isEmpty(mKeySearch)) {
                    medicineList.add(medicine);
                } else {
                    if (GlobalFunction.getTextSearch(medicine.getMedicineName().toLowerCase())
                            .contains(GlobalFunction.getTextSearch(mKeySearch).toLowerCase())) {
                        medicineList.add(medicine);
                    }
                }
            }
            manageMedicineAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            Medicine medicine = dataSnapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || medicineList.isEmpty() || manageMedicineAdapter == null) {
                return;
            }
            /* KHI SỐ LƯỢNG THUỐC LỚN HƠN 0: TỨC LÀ KHÔNG BẰNG 0 VÀ KHÔNG CÓ DẤU TRỪ PHÍA TRƯỚC */
            if (!medicine.getMedicineQuantity().equals("")||!medicine.getMedicineQuantity().equals("0")  || !medicine.getMedicineQuantity().contains("-")) {
                for (Medicine medicineObject : medicineList) {
                    if (medicine.getMedicineId().equals( medicineObject.getMedicineId() )) {
                        medicineList.remove(medicineObject);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < medicineList.size(); i++) {
                    if (medicine.getMedicineId().equals( medicineList.get(i).getMedicineId())) {
                        medicineList.set(i, medicine);
                        break;
                    }
                }
            }
            manageMedicineAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Medicine medicine = dataSnapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || medicineList.isEmpty() || manageMedicineAdapter == null) {
                return;
            }
            for (Medicine medicineObject : medicineList) {
                if (medicine.getMedicineId().equals( medicineObject.getMedicineId() )) {
                    medicineList.remove(medicineObject);
                    break;
                }
            }
            manageMedicineAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            showToast(getString(R.string.msg_get_data_error));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewMedOutOfStockBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.edtSearchName.setEnabled(true);

        mClickHandlers = new ViewMedOutOfStockClickHandlers();
        mBinding.setClickHandlers(mClickHandlers);

        mBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMed();
                return true;
            }
            return false;
        });
        mBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    mKeySearch = "";
                    getListMed();
                    GlobalFunction.hideSoftKeyboard(ViewMedOutOfStockActivity.this);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rcvData.setLayoutManager(linearLayoutManager);

        medicineList = new ArrayList<>();
        getListMed();
        getListProfit();

        manageMedicineAdapter = new MedicineDetailDapter(medicineList, Medicine -> GlobalFunction.goToMedicineDetailActivity(this, Medicine));
        mBinding.rcvData.setAdapter(manageMedicineAdapter);

    }

    private void getListMed() {
        if (medicineList != null) {
            medicineList.clear();
            PLPSAApplication.get(this).getMedicineDatabaseRef().removeEventListener(mChildEventListener);
        }
        PLPSAApplication.get(this).getMedicineDatabaseRef().addChildEventListener(mChildEventListener);
    }

    private void getListProfit() {
        PLPSAApplication.get(this).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            list.add(history);
                        }
                        handleDataHistories(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showToast(getString(R.string.msg_get_data_error));
                    }
                });
    }

    private void handleDataHistories(List<History> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (mListProfit != null) {
            mListProfit.clear();
        } else {
            mListProfit = new ArrayList<>();
        }
        for (History history : list) {
            String medicine_id = history.getMedicineId();
            if (checkProfitExist(medicine_id)) {
                getProfitFromMedicineId(medicine_id).getListHistory().add(history);
            } else {
                Profit profit = new Profit();
                profit.setMedicineId(history.getMedicineId());
                profit.setMedicineName(history.getMedicineName());
                profit.setMeasurementId(history.getMeasurementId());
                profit.setMeasurementName(history.getMeasurementName());
                profit.getListHistory().add(history);
                mListProfit.add(profit);
            }
        }
    }

    private Profit getProfitFromMedicineId(String medicine_id) {
        Profit result = null;
        for (Profit profit : mListProfit) {
            if (Objects.equals(medicine_id, profit.getMedicineId())) {
                result = profit;
                break;
            }
        }
        return result;
    }

    private boolean checkProfitExist(String medicine_id) {
        if (mListProfit == null || mListProfit.isEmpty()) {
            return false;
        }
        boolean result = false;
        for (Profit profit : mListProfit) {
            if (Objects.equals(medicine_id, profit.getMedicineId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public class ViewMedOutOfStockClickHandlers {
        public void onClickSearchImg(View view) {
            searchMed();
        }
    }

    private void searchMed() {
        if (medicineList == null || medicineList.isEmpty()) {
            GlobalFunction.hideSoftKeyboard(this);
            return;
        }
        mKeySearch = mBinding.edtSearchName.getText().toString().trim();
        getListMed();
        GlobalFunction.hideSoftKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}