package com.hdcompany.plpsa888.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hdcompany.plpsa888.PLPSAApplication;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.ProfitAdapter;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewProfitBinding;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.model.Profit;
import com.hdcompany.plpsa888.util.DateTimeUtils;
import com.hdcompany.plpsa888.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewProfitActivity extends BaseActivity {
    public ActivityViewProfitBinding mBinding;
    public ViewProfitClickHandlers mClickHandlers;
    public String title;
    private List<Profit> mListProfit;
    private ProfitAdapter mProfitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewProfitBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        if(intent != null && !intent.getStringExtra("title").equals("")){
            title = intent.getStringExtra("title");
            mBinding.tvTotalPrice.setText(title);
        }

        GlobalFunction.hideSoftKeyboard(ViewProfitActivity.this);
        mClickHandlers = new ViewProfitClickHandlers();
        mBinding.setClickHandlers(mClickHandlers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rcvProfit.setLayoutManager(linearLayoutManager);

        getListProfit();
    }

    public class ViewProfitClickHandlers{
        public void onClickDateFrom(View view){
            GlobalFunction.showDatePicker(ViewProfitActivity.this, mBinding.tvDateFrom.getText().toString(), date -> {
                mBinding.tvDateFrom.setText(date);
                getListProfit();
            });
        }
        public void onClickDateTo(View view){
            GlobalFunction.showDatePicker(ViewProfitActivity.this, mBinding.tvDateTo.getText().toString(), date -> {
                mBinding.tvDateTo.setText(date);
                getListProfit();
            });
        }

    }

    private void getListProfit() {
        PLPSAApplication.get(this).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (canAddHistory(history)) {
                                list.add(history);
                            }
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
                profit.setMeasurementId(history.getMeasurementId());
                profit.setMedicineId(history.getMedicineId());
                profit.setMedicineName(history.getMedicineName());
                profit.setMeasurementName(history.getMeasurementName());
                profit.getListHistory().add(history);
                mListProfit.add(profit);
            }
        }
        mProfitAdapter = new ProfitAdapter( mListProfit, profit -> {
            Medicine med = new Medicine(profit.getMedicineId(), profit.getMeasurementId(),
                    profit.getMeasurementName(), profit.getMedicineName());
            GlobalFunction.goToMedicineDetailActivity(this, med);
        });
        mBinding.rcvProfit.setAdapter(mProfitAdapter);
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

    private boolean canAddHistory(History history) {
        if (history == null) {
            return false;
        }
        String strDateFrom = mBinding.tvDateFrom.getText().toString();
        String strDateTo = mBinding.tvDateTo.getText().toString();
        if (StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            return true;
        }
        if (StringUtil.isEmpty(strDateFrom) && !StringUtil.isEmpty(strDateTo)) {
            long longDateTo = Long.parseLong(DateTimeUtils.convertDateToTimeStamp(strDateTo));
            return history.getDate() <= longDateTo;
        }
        if (!StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            long longDateFrom = Long.parseLong(DateTimeUtils.convertDateToTimeStamp(strDateFrom));
            return history.getDate() >= longDateFrom;
        }
        long longDateTo = Long.parseLong(DateTimeUtils.convertDateToTimeStamp(strDateTo));
        long longDateFrom = Long.parseLong(DateTimeUtils.convertDateToTimeStamp(strDateFrom));
        return history.getDate() >= longDateFrom && history.getDate() <= longDateTo;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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