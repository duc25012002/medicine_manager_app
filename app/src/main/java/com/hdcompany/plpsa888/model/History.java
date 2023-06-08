package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import com.hdcompany.plpsa888.util.StringUtil;

import java.io.Serializable;

/* LỊCH SỬ */
public class History extends BaseObservable implements Serializable {

    private String historyId;
    /* ID để tìm kiếm so sánh */
    private String medicineId;
    private String measurementId;
    /* TÊN THUỐC VÀ TÊN ĐỊNH LƯỢNG */
    private String medicineName;
    private String measurementName;
    /* SỐ LƯỢNG - TÍNH THEO TÊN ĐỊNH LƯỢNG */
    private String quantity;
    /* ĐƠN GIÁ  medicinePrice*/
    private String unitPrice;
    private String totalPrice;
    /* HISTORY GENERATED DATE */
    private long date;
    /* STATUS: IN or OUT - BUY or SELL */
    private boolean addMedicine;
    private String medicineDesc;

    @Ignore
    public History(){}

    public History(String historyId, String medicineId, String measurementId, String medicineName, String measurementName, String quantity, String unitPrice, String totalPrice, long date, boolean addMedicine, String medicineDesc) {
        this.historyId = historyId;
        this.medicineId = medicineId;
        this.measurementId = measurementId;
        this.medicineName = medicineName;
        this.measurementName = measurementName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = getTotalPrice();
        this.date = date;
        this.addMedicine = addMedicine;
        this.medicineDesc = medicineDesc;
    }

    @Bindable
    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
        notifyPropertyChanged(BR.historyId);
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
    public String getQuantity() {
        return quantity;
    }

    @Bindable
    public String getDottedQuantity() {
        return StringUtil.getDottedNumber(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    @Bindable
    public String getUnitPrice() {
        return unitPrice;
    }

    @Bindable
    public String getDottedUnitPrice() {
        return StringUtil.getDottedNumber(unitPrice);
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
        notifyPropertyChanged(BR.unitPrice);
    }

    @Bindable
    public String getTotalPrice() {
        setTotalPrice();
        return totalPrice;
    }

    @Bindable
    public String getDottedTotalPrice() {
        setTotalPrice();
        return StringUtil.getDottedNumber(totalPrice);
    }

    public void setTotalPrice() {
        /* TỔNG GIÁ TIỀN = SỐ LƯỢNG * ĐƠN GIÁ */
        this.totalPrice = StringUtil.getMultiple(quantity, unitPrice);
        notifyPropertyChanged(BR.totalPrice);
    }

    @Bindable
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public boolean isAddMedicine() {
        return addMedicine;
    }

    public void setAddMedicine(boolean addMedicine) {
        this.addMedicine = addMedicine;
        notifyPropertyChanged(BR.addMedicine);
    }
    @Bindable
    public String getMedicineDesc() {
        return medicineDesc;
    }

    public void setMedicineDesc(String medicineDesc) {
        this.medicineDesc = medicineDesc;
        notifyPropertyChanged(BR.medicineDesc);
    }
}
