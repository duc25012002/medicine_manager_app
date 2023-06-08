package com.hdcompany.plpsa888.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hdcompany.plpsa888.PLPSAApplication;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.adapter.MeasurementAdapter;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewMeasurementBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Measurement;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewMeasurementActivity extends BaseActivity {

    public ActivityViewMeasurementBinding mBinding;
    public ViewMeasurementActivityClickHandlers mClickHandlers;

    private List<Measurement> measurementList;
    private MeasurementAdapter measurementAdapter;
    private String mKeySearch;

    private final ChildEventListener mChildEventListener = new ChildEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Measurement meas = snapshot.getValue(Measurement.class);
            if (meas == null || measurementList == null || measurementAdapter == null) {
                return;
            }
            if (StringUtil.isEmpty(mKeySearch)) {
                measurementList.add(meas);
            } else {
                if (GlobalFunction.getTextSearch(meas.getMeasurementName().toLowerCase())
                        .contains(GlobalFunction.getTextSearch(mKeySearch).toLowerCase())) {
                    measurementList.add(meas);
                }
            }
            measurementAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Measurement meas = snapshot.getValue(Measurement.class);
            if (meas == null || measurementList == null || measurementList.isEmpty() || measurementAdapter == null) {
                return;
            }
            for (int i = 0; i < measurementList.size(); i++) {
                if (meas.getMeasurementId().equals(measurementList.get(i).getMeasurementId())) {
                    measurementList.set(i, meas);
                    break;
                }
            }
            measurementAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Measurement meas = snapshot.getValue(Measurement.class);
            if (meas == null || measurementList == null || measurementList.isEmpty() || measurementAdapter == null) {
                return;
            }
            for (Measurement model : measurementList) {
                if (model.getMeasurementId().equals(meas.getMeasurementId())) {
                    measurementList.remove(model);
                    break;
                }
            }
            measurementAdapter.notifyDataSetChanged();
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
        mBinding = ActivityViewMeasurementBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mClickHandlers = new ViewMeasurementActivityClickHandlers();
        mBinding.setClickHandlers(mClickHandlers);

        mBinding.edtSearchName.setEnabled(true);
        mBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMeas();
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
                    getListMeas();
                    GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rcvData.setLayoutManager(linearLayoutManager);

        measurementList = new ArrayList<>();
        getListMeas();

        measurementAdapter = new MeasurementAdapter(measurementList, new MeasurementAdapter.IManagerMeasurementListener() {
            @Override
            public void edit(Measurement model) {
                onClickEditOrAddMeas(model);
            }

            @Override
            public void delete(Measurement model) {
                onCLickDeleteMeas(model);
            }
        });

        mBinding.rcvData.setAdapter(measurementAdapter);
        mBinding.rcvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mBinding.fabAddData.hide();
                } else {
                    mBinding.fabAddData.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void getListMeas() {
        if (measurementList != null) {
            measurementList.clear();
            PLPSAApplication.get(this).getMeasurementDatabaseRef().removeEventListener(mChildEventListener);
        }
        PLPSAApplication.get(this).getMeasurementDatabaseRef().addChildEventListener(mChildEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onCLickDeleteMeas(Measurement model) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_delete), (dialogInterface, i)
                        -> PLPSAApplication.get(ViewMeasurementActivity.this).getMeasurementDatabaseRef()
                        .child(model.getMeasurementId()).removeValue((error, ref) -> {
                            showToast(getString(R.string.msg_delete_measurement_success));
                            GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this);
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void onCLickDeleteAllMeas() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete_all))
                .setPositiveButton(getString(R.string.delete_all), (dialogInterface, i)
                        -> PLPSAApplication.get(ViewMeasurementActivity.this).getMeasurementDatabaseRef()
                        .removeValue((error, ref) -> {
                            showToast(getString(R.string.msg_delete_all_measurement_success));
                            GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this);
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void onClickEditOrAddMeas(Measurement model) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_and_edit_measurement);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Get view
        final TextView tvTitleDialog = dialog.findViewById(R.id.tv_title_dialog);
        final EditText edtMeasName = dialog.findViewById(R.id.edt_measurement_name);
        final TextView tvDialogCancel = dialog.findViewById(R.id.tv_dialog_cancel);
        final TextView tvDialogAction = dialog.findViewById(R.id.tv_dialog_action);

        // Set data
        if (model == null) {
            tvTitleDialog.setText(getString(R.string.add_measurement_name));
            tvDialogAction.setText(getString(R.string.action_add));
        } else {
            tvTitleDialog.setText(getString(R.string.edit_measurement_name));
            tvDialogAction.setText(getString(R.string.action_edit));
            edtMeasName.setText(model.getMeasurementName());
        }

        // Set listener
        tvDialogCancel.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.dismiss();
            }
        });

        tvDialogAction.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String strMeasName = edtMeasName.getText().toString().trim();
                if (StringUtil.isEmpty(strMeasName)) {
                    showToast(getString(R.string.msg_measurement_name_require));
                    return;
                }

                if (isMeasExist(strMeasName)) {
                    showToast(getString(R.string.msg_measurement_exist));
                    return;
                }

                if (model == null) {
                    String id = String.valueOf(System.currentTimeMillis());
                    Measurement Meas = new Measurement();
                    Meas.setMeasurementId(id);
                    Meas.setMeasurementName(strMeasName);

                    PLPSAApplication.get(ViewMeasurementActivity.this).getMeasurementDatabaseRef()
                            .child(id).setValue(Meas, (error, ref) -> {
                                GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this, edtMeasName);
                                showToast(getString(R.string.msg_add_measurement_success));
                                dialog.dismiss();
                                GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this);
                            });
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("measurementName", strMeasName);

                    PLPSAApplication.get(ViewMeasurementActivity.this).getMeasurementDatabaseRef()
                            .child(model.getMeasurementId()).updateChildren(map, (error, ref) -> {
                                GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this, edtMeasName);
                                showToast(getString(R.string.msg_edit_measurement_success));
                                dialog.dismiss();
                                GlobalFunction.hideSoftKeyboard(ViewMeasurementActivity.this);
                                updateMeasInMedicine(new Measurement(model.getMeasurementId(),strMeasName));
                                updateMeasInHistory(new Measurement(model.getMeasurementId(),strMeasName));
                            });
                }
            }
        });

        dialog.show();
    }

    private void searchMeas() {
        if (measurementList == null || measurementList.isEmpty()) {
            GlobalFunction.hideSoftKeyboard(this);
            return;
        }
        mKeySearch = mBinding.edtSearchName.getText().toString().trim();
        getListMeas();
        GlobalFunction.hideSoftKeyboard(this);
    }

    private boolean isMeasExist(String measurement_name) {
        if (measurementList == null || measurementList.isEmpty()) {
            return false;
        }

        for (Measurement meas : measurementList) {
            if (measurement_name.equals(meas.getMeasurementName())) {
                return true;
            }
        }

        return false;
    }

    private void updateMeasInMedicine(Measurement model) {
        PLPSAApplication.get(this).getMedicineDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Medicine> list = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Medicine Medicine = dataSnapshot.getValue(Medicine.class);
                            if (Medicine != null && Objects.equals(Medicine.getMeasurementId(), model.getMeasurementId())) {
                                list.add(Medicine);
                            }
                        }
                        PLPSAApplication.get(ViewMeasurementActivity.this).getMedicineDatabaseRef()
                                .removeEventListener(this);
                        if (list.isEmpty()) {
                            return;
                        }
                        for (Medicine Medicine : list) {
                            PLPSAApplication.get(ViewMeasurementActivity.this).getMedicineDatabaseRef()
                                    .child(Medicine.getMedicineId())
                                    .child("measurementName").setValue(model.getMeasurementName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void updateMeasInHistory(Measurement model) {
        PLPSAApplication.get(this).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> list = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (history != null && history.getMeasurementId().equals(model.getMeasurementId())) {
                                list.add(history);
                            }
                        }
                        PLPSAApplication.get(ViewMeasurementActivity.this).getHistoryDatabaseRef()
                                .removeEventListener(this);
                        if (list.isEmpty()) {
                            return;
                        }
                        for (History history : list) {
                            PLPSAApplication.get(ViewMeasurementActivity.this).getHistoryDatabaseRef()
                                    .child(history.getHistoryId())
                                    .child("measurementName").setValue(model.getMeasurementName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
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

    public class ViewMeasurementActivityClickHandlers {
        // Search Meas
        public void onClickSearchImg(View view) {
            searchMeas();
        }

        public void onClickFab(View view) {
            onClickEditOrAddMeas(null);
        }

        public void onClickDeleteAll(View view) {
            if (measurementList == null || measurementList.isEmpty()) {
                return;
            }
            onCLickDeleteAllMeas();
        }
    }
}