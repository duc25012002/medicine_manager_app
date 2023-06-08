package com.hdcompany.plpsa888.activity;


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
import com.hdcompany.plpsa888.adapter.StatisticalAdapter;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewStatisticalBinding;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.model.Statistical;
import com.hdcompany.plpsa888.util.DateTimeUtils;
import com.hdcompany.plpsa888.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewStatisticalActivity extends BaseActivity {

    public ActivityViewStatisticalBinding mBinding;
    public ViewStatisticalClickHandlers mClickHandlers;

    private int mType;
    private boolean isMedPopular;
    private List<Statistical> mListStatistical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityViewStatisticalBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getDataIntent();
        mClickHandlers = new ViewStatisticalClickHandlers();
        mBinding.setClickHandlers(mClickHandlers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rcvStatistical.setLayoutManager(linearLayoutManager);
        getListStatistical();
        if (isMedPopular) {
            mBinding.layoutFilter.setVisibility(View.GONE);
            mBinding.layoutBottom.setVisibility(View.GONE);
        } else {
            mBinding.layoutFilter.setVisibility(View.VISIBLE);
            mBinding.layoutBottom.setVisibility(View.VISIBLE);
        }


        switch (mType) {
            case Constant.TYPE_REVENUE:
                mBinding.labelTotalValue.setText(getString(R.string.label_total_revenue));
                break;

            case Constant.TYPE_COST:
                mBinding.labelTotalValue.setText(getString(R.string.label_total_cost));
                break;
        }
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mType = bundle.getInt(Constant.KEY_TYPE_STATISTICAL);
        isMedPopular = bundle.getBoolean(Constant.KEY_MEDICINE_POPULAR);
    }

    public class ViewStatisticalClickHandlers {
        public void onClickDateFrom(View view) {
            GlobalFunction.showDatePicker(ViewStatisticalActivity.this, mBinding.tvDateFrom.getText().toString(), date -> {
                mBinding.tvDateFrom.setText(date);
                getListStatistical();
            });
        }

        public void onClickDateTo(View view) {
            GlobalFunction.showDatePicker(ViewStatisticalActivity.this, mBinding.tvDateTo.getText().toString(), date -> {
                mBinding.tvDateTo.setText(date);
                getListStatistical();
            });
        }
    }

    private void getListStatistical() {
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
        if (mListStatistical != null) {
            mListStatistical.clear();
        } else {
            mListStatistical = new ArrayList<>();
        }
        for (History history : list) {
            String medicine_id = history.getMedicineId();
            if (checkStatisticalExist(medicine_id)) {
                getStatisticalFromMedicineId(medicine_id).getListHistory().add(history);
            } else {
                Statistical statistical = new Statistical();
                statistical.setMedicineId(history.getMedicineId());
                statistical.setMedicineName(history.getMedicineName());
                statistical.setMeasurementId(history.getMeasurementId());
                statistical.setMeasurementName(history.getMeasurementName());
                statistical.getListHistory().add(history);
                mListStatistical.add(statistical);
            }
        }
        if (isMedPopular) {
            List<Statistical> listPopular = new ArrayList<>(mListStatistical);
            listPopular.sort((statistical1, statistical2)
                    -> Integer.parseInt(StringUtil.getDifference(statistical2.getTotalPrice() ,statistical1.getTotalPrice())));
            StatisticalAdapter statisticalAdapter = new StatisticalAdapter(listPopular, statistical -> {
                Medicine med = new Medicine(statistical.getMedicineId(),statistical.getMeasurementId(), statistical.getMeasurementName(),
                        statistical.getMedicineName());
                GlobalFunction.goToMedicineDetailActivity(this, med);
            });
            mBinding.rcvStatistical.setAdapter(statisticalAdapter);
        } else {
            StatisticalAdapter statisticalAdapter = new StatisticalAdapter(mListStatistical, statistical -> {
                Medicine med = new Medicine(statistical.getMedicineId(), statistical.getMeasurementId(),statistical.getMeasurementName(),
                        statistical.getMedicineName());
                GlobalFunction.goToMedicineDetailActivity(this, med);
            });
            mBinding.rcvStatistical.setAdapter(statisticalAdapter);
        }

        // Calculate total
        String strTotalValue = getTotalValues() + Constant.CURRENCY;
        mBinding.tvTotalValue.setText(strTotalValue);
    }

    private String getTotalValues() {
        if (mListStatistical == null || mListStatistical.isEmpty()) {
            return "0";
        }

        String total = "0";
        for (Statistical statistical : mListStatistical) {
            total = StringUtil.getSum(total, statistical.getTotalPrice());
        }
        return StringUtil.getDottedNumber(total);
    }

    private boolean checkStatisticalExist(String medicine_id) {
        if (mListStatistical == null || mListStatistical.isEmpty()) {
            return false;
        }
        boolean result = false;
        for (Statistical statistical : mListStatistical) {
            if (Objects.equals(medicine_id, statistical.getMedicineId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private Statistical getStatisticalFromMedicineId(String medicine_id) {
        Statistical result = null;
        for (Statistical statistical : mListStatistical) {
            if (Objects.equals(medicine_id, statistical.getMedicineId())) {
                result = statistical;
                break;
            }
        }
        return result;
    }

    private boolean canAddHistory(History history) {
        if (history == null) {
            return false;
        }
        if (Constant.TYPE_REVENUE == mType) {
            if (history.isAddMedicine()) {
                return false;
            }
        } else {
            if (!history.isAddMedicine()) {
                return false;
            }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}