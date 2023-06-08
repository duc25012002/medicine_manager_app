package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import java.io.Serializable;

/* ĐỊNH LƯỢNG */
public class Measurement extends BaseObservable implements Serializable {

    private String measurementId;
    private String measurementName;

    @Ignore
    public Measurement(){}

    public Measurement(String measurementId, String measurementName) {
        this.measurementId = measurementId;
        this.measurementName = measurementName;
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
    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
        notifyPropertyChanged(BR.measurementName);
    }
}
