package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import com.hdcompany.plpsa888.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* THỐNG KÊ */
public class Statistical extends BaseObservable implements Serializable {

    /* ID để tìm kiếm so sánh */
    private String medicineId;
    private String measurementId;
    /* TÊN THUỐC VÀ TÊN ĐỊNH LƯỢNG */
    private String medicineName;
    private String measurementName;
    /* DANH SÁCH LỊCH SỬ THÊM HOẶC BÁN THUỐC ĐỂ THỐNG KÊ ĐÁNH GIÁ */
    private List<History> listHistory;

    @Ignore
    public Statistical(){}

    public Statistical(String medicineId, String measurementId, String medicineName, String measurementName, List<History> listHistory) {
        this.medicineId = medicineId;
        this.measurementId = measurementId;
        this.medicineName = medicineName;
        this.measurementName = measurementName;
        this.listHistory = listHistory;
    }

    @Bindable
    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
        notifyPropertyChanged(BR.medicineId);
    }

    @Bindable
    public String getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(String measurementId) {
        this.measurementId = measurementId;
        notifyPropertyChanged(BR.measurementId);
    }

    @Bindable
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
        notifyPropertyChanged(BR.medicineName);
    }

    @Bindable
    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
        notifyPropertyChanged(BR.measurementName);
    }

    @Bindable
    public List<History> getListHistory() {
        if (listHistory == null) {
            listHistory = new ArrayList<>();
        }
        return listHistory;
    }

    public void setListHistory(List<History> listHistory) {
        this.listHistory = listHistory;
        notifyPropertyChanged(BR.listHistory);
    }

    @Bindable
    /* SỐ LƯỢNG THUỐC ĐÃ MUA */
    public String getTotalQuantity() {
        if (listHistory == null || listHistory.isEmpty()) {
            return "0";
        }

        String RESULT = "0";
        for (History history : listHistory) {
            RESULT = StringUtil.getSum(RESULT, history.getQuantity());
        }
        return StringUtil.getDottedNumber(RESULT);
    }

    @Bindable
    /* TỔNG TIỀN THUỐC ĐÃ MUA */
    public String getTotalPrice() {
        if (listHistory == null || listHistory.isEmpty()) {
            return "0";
        }
        String RESULT = "0";
        for (History history : listHistory) {
            RESULT = StringUtil.getSum(RESULT, history.getTotalPrice());
        }
        return RESULT;
    }

    @Bindable
    /* TỔNG TIỀN THUỐC ĐÃ MUA */
    public String getDottedTotalPrice() {
        return StringUtil.getDottedNumber(getTotalPrice());
    }

}
