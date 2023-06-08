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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.hdcompany.plpsa888.adapter.MedicineAdapter;
import com.hdcompany.plpsa888.adapter.SelectedMeasurementSpinAdapter;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityViewMedListBinding;
import com.hdcompany.plpsa888.listener.IOnSingleClickListener;
import com.hdcompany.plpsa888.model.History;
import com.hdcompany.plpsa888.model.Measurement;
import com.hdcompany.plpsa888.model.Medicine;
import com.hdcompany.plpsa888.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMedListActivity extends BaseActivity {

    private ActivityViewMedListBinding mActivityViewMedListBinding;
    public ViewMedListClickHandlers mMedListClickHandlers;

    private List<Medicine> medicineList;
    private List<Measurement> measurementList;

    private MedicineAdapter medicineAdapter;

    private Medicine selectedMed;
    private Measurement selectedMeas;

    private String mKeySearch;
    private final ChildEventListener mChildEventListener = new ChildEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Medicine medicine = snapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || medicineAdapter == null) {
                return;
            }
            if (StringUtil.isEmpty(mKeySearch)) {
                medicineList.add(medicine);
            } else {
                if (GlobalFunction.getTextSearch(medicine.getMedicineName().toLowerCase())
                        .contains(GlobalFunction.getTextSearch(mKeySearch).toLowerCase())) {
                    medicineList.add(medicine);
                }
            }
            medicineAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Medicine medicine = snapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || medicineList.isEmpty() || medicineAdapter == null) {
                return;
            }
            for (int i = 0; i < medicineList.size(); i++) {
                if (medicine.getMedicineId().equals(medicineList.get(i).getMedicineId())) {
                    medicineList.set(i, medicine);
                    break;
                }
            }
            medicineAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Medicine medicine = snapshot.getValue(Medicine.class);
            if (medicine == null || medicineList == null || medicineList.isEmpty() || medicineAdapter == null) {
                return;
            }
            for (Medicine medicineObject : medicineList) {
                if (medicine.getMedicineId().equals(medicineObject.getMedicineId())) {
                    medicineList.remove(medicineObject);
                    break;
                }
            }
            medicineAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            GlobalFunction.showToast(ViewMedListActivity.this, getString(R.string.msg_get_data_error));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        mActivityViewMedListBinding = ActivityViewMedListBinding.inflate(getLayoutInflater());
        setContentView(mActivityViewMedListBinding.getRoot());
        mMedListClickHandlers = new ViewMedListClickHandlers();
        mActivityViewMedListBinding.setClickHandlers(mMedListClickHandlers);

        mActivityViewMedListBinding.edtSearchName.setEnabled(true);
        mActivityViewMedListBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
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
                    GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityViewMedListBinding.rcvData.setLayoutManager(linearLayoutManager);

        measurementList = new ArrayList<>();
        getListMeas();
        medicineList = new ArrayList<>();
        getListMed();

        medicineAdapter = new MedicineAdapter(medicineList, new MedicineAdapter.IManagerMedicineListener() {
            @Override
            public void edit(Medicine model) {
                onClickEditOrAddMedicine(model);
            }

            @Override
            public void delete(Medicine model) {
                onCLickDeleteMedicine(model);
            }

            @Override
            public void onClickItem(Medicine model) {
                GlobalFunction.goToMedicineDetailActivity(ViewMedListActivity.this, model);
            }
        });

        mActivityViewMedListBinding.rcvData.setAdapter(medicineAdapter);
        mActivityViewMedListBinding.rcvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mActivityViewMedListBinding.fabAddData.hide();
                } else {
                    mActivityViewMedListBinding.fabAddData.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class ViewMedListClickHandlers {
        // Search Medicine
        public void onClickSearchImg(View view) {
            searchMedicine();
        }

        public void onClickFab(View view) {
            onClickEditOrAddMedicine(null);
        }

        public void onCLickDeleteAll(View view) {
            onCLickDeleteAllMedicine();
        }
    }

    private void searchMedicine() {
        if (medicineList == null || medicineList.isEmpty()) {
            GlobalFunction.hideSoftKeyboard(this);
            return;
        }
        mKeySearch = mActivityViewMedListBinding.edtSearchName.getText().toString().trim();
        getListMed();
        GlobalFunction.hideSoftKeyboard(this);
    }

    public void getListMed() {
        if (medicineList != null) {
            medicineList.clear();
            PLPSAApplication.get(this).getMedicineDatabaseRef().removeEventListener(mChildEventListener);
        }
        PLPSAApplication.get(this).getMedicineDatabaseRef().addChildEventListener(mChildEventListener);
    }

    public void getListMeas() {
        PLPSAApplication.get(this).getMeasurementDatabaseRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (measurementList != null) measurementList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Measurement meas = dataSnapshot.getValue(Measurement.class);
                    measurementList.add(meas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(getString(R.string.msg_get_data_error));
            }
        });
    }

    private void onClickEditOrAddMedicine(Medicine model) {
        if (measurementList == null || measurementList.isEmpty()) {
            showToast(getString(R.string.msg_list_measurement_require));
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_and_edit_medicine);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Get view
        final TextView tvTitleDialog = dialog.findViewById(R.id.tv_title_dialog);
        final EditText edtMedicineName = dialog.findViewById(R.id.edt_medicine_name);
        final TextView tvDialogCancel = dialog.findViewById(R.id.tv_dialog_cancel);
        final TextView tvDialogAction = dialog.findViewById(R.id.tv_dialog_action);
        final Spinner spinMeasurement = dialog.findViewById(R.id.spinner_measurement);

        SelectedMeasurementSpinAdapter selectMeasurementAdapter = new SelectedMeasurementSpinAdapter(this, R.layout.item_choose_option, measurementList);
        spinMeasurement.setAdapter(selectMeasurementAdapter);
        spinMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeas = selectMeasurementAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set data
        if (model == null) {
            tvTitleDialog.setText(getString(R.string.add_medicine_name));
            tvDialogAction.setText(getString(R.string.action_add));
        } else {
            tvTitleDialog.setText(getString(R.string.edit_medicine_name));
            tvDialogAction.setText(getString(R.string.action_edit));
            edtMedicineName.setText(model.getMedicineName());
            spinMeasurement.setSelection(getPositionMeasUpdate(model));
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
                String strMedicineName = edtMedicineName.getText().toString().trim();
                if (StringUtil.isEmpty(strMedicineName)) {
                    showToast(getString(R.string.msg_medicine_name_require));
                    return;
                }

                if (isMedicineExist(strMedicineName)) {
                    showToast(getString(R.string.msg_medicine_exist));
                    return;
                }

                if (model == null) {
                    String id = String.valueOf(System.currentTimeMillis());
                    Medicine MedicineObject = new Medicine();
                    MedicineObject.setMedicineId(id);
                    MedicineObject.setMedicineName(strMedicineName);
                    MedicineObject.setMeasurementId(selectedMeas.getMeasurementId());
                    MedicineObject.setMeasurementName(selectedMeas.getMeasurementName());
                    MedicineObject.setMedicineQuantity("0");

                    PLPSAApplication.get(ViewMedListActivity.this).getMedicineDatabaseRef()
                            .child(id).setValue(MedicineObject, (error, ref) -> {
                                GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this, edtMedicineName);
                                showToast(getString(R.string.msg_add_medicine_success));
                                dialog.dismiss();
                                GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this);
                            });
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("medicineName", strMedicineName);
                    map.put("measurementId", selectedMeas.getMeasurementId());
                    map.put("measurementName", selectedMeas.getMeasurementName());

                    PLPSAApplication.get(ViewMedListActivity.this).getMedicineDatabaseRef()
                            .child(model.getMedicineId()).updateChildren(map, (error, ref) -> {
                                GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this, edtMedicineName);
                                showToast(getString(R.string.msg_edit_medicine_success));
                                dialog.dismiss();
                                GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this);
                                updateMedInHistory(new Medicine(
                                        model.getMedicineId(),
                                        selectedMeas.getMeasurementId(),
                                        selectedMeas.getMeasurementName(),
                                        strMedicineName));
                            });
                }
            }
        });

        dialog.show();
    }

    private void onCLickDeleteMedicine(Medicine model) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_delete), (dialogInterface, i)
                        -> PLPSAApplication.get(ViewMedListActivity.this).getMedicineDatabaseRef()
                        .child(String.valueOf(model.getMedicineId())).removeValue((error, ref) -> {
                            showToast(getString(R.string.msg_delete_medicine_success));
                            GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this);
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void onCLickDeleteAllMedicine() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete_all))
                .setPositiveButton(getString(R.string.delete_all), (dialogInterface, i)
                        -> PLPSAApplication.get(ViewMedListActivity.this).getMedicineDatabaseRef()
                        .removeValue((error, ref) -> {
                            showToast(getString(R.string.msg_delete_all_medicine_success));
                            GlobalFunction.hideSoftKeyboard(ViewMedListActivity.this);
                        }))
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void updateMedicineHistory(Medicine model) {
        PLPSAApplication.get(this).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> list = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (history != null && history.getMedicineId().equals(model.getMedicineId())) {
                                list.add(history);
                            }
                        }
                        PLPSAApplication.get(ViewMedListActivity.this).getHistoryDatabaseRef()
                                .removeEventListener(this);
                        if (list.isEmpty()) {
                            return;
                        }
                        for (History history : list) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("medicineName", model.getMedicineName());
                            map.put("measurementId", model.getMeasurementId());
                            map.put("measurementName", model.getMeasurementName());

                            PLPSAApplication.get(ViewMedListActivity.this).getHistoryDatabaseRef()
                                    .child(history.getHistoryId())
                                    .updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private int getPositionMeasUpdate(Medicine model) {
        if (measurementList == null || measurementList.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < measurementList.size(); i++) {
            if (model.getMeasurementId().equals(measurementList.get(i).getMeasurementId())) {
                return i;
            }
        }
        return 0;
    }

    private boolean isMedicineExist(String medicine_name) {
        if (medicineList == null || medicineList.isEmpty()) {
            return false;
        }

        for (Medicine medicine : medicineList) {
            if (medicine_name.equals(medicine.getMedicineName())) {
                return true;
            }
        }

        return false;
    }

    private void updateMedInHistory(Medicine medicine) {
        PLPSAApplication.get(this).getHistoryDatabaseRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<History> list = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            History history = dataSnapshot.getValue(History.class);
                            if (history != null && history.getMedicineId().equals(medicine.getMedicineId())) {
                                list.add(history);
                            }
                        }
                        PLPSAApplication.get(ViewMedListActivity.this).getHistoryDatabaseRef()
                                .removeEventListener(this);
                        if (list.isEmpty()) {
                            return;
                        }
                        for (History history : list) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("medicineName", medicine.getMedicineName());
                            map.put("measurementId", medicine.getMeasurementId());
                            map.put("measurementName", medicine.getMeasurementName());

                            PLPSAApplication.get(ViewMedListActivity.this).getHistoryDatabaseRef()
                                    .child(history.getHistoryId())
                                    .updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}