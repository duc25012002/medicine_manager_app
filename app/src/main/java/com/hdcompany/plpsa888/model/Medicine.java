package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import java.io.Serializable;

/* THUỐC */
public class Medicine extends BaseObservable implements Serializable {

    private String medicineId;
    private String measurementId;
    /* TÊN CỦA ĐỊNH LƯỢNG -  */
    private String measurementName;
    /* TÊN CỦA THUỐC */
    private String medicineName;
    /* ĐƠN GIÁ CỦA THUỐC */
    private String medicinePrice;
    /* SỐ LƯỢNG CỦA THUỐC */
    private String medicineQuantity;
    /* CHI TIẾT CỦA THUỐC */
    private String medicineDesc;

    @Ignore
    public Medicine(){}
    public Medicine(String medicineId, String measurementId, String measurementName, String medicineName) {
        this.medicineId = medicineId;
        this.measurementId = measurementId;
        this.measurementName = measurementName;
        this.medicineName = medicineName;
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
    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
        notifyPropertyChanged(BR.measurementName);
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
    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
        notifyPropertyChanged(BR.medicinePrice);
    }

    @Bindable
    public String getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(String medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
        notifyPropertyChanged(BR.medicineQuantity);
    }

    @Bindable
    public String getMedicineDesc() {
        return medicineDesc;
    }

    public void setMedicineDesc(String medicineDesc) {
        this.medicineDesc = medicineDesc;
        notifyPropertyChanged(BR.medicineDesc);
    }

    @Bindable
    public String getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(String measurementId) {
        this.measurementId = measurementId;
        notifyPropertyChanged(BR.measurementId);
    }
}
