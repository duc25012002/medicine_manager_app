package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import com.hdcompany.plpsa888.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* LỢI NHUẬN */
public class Profit extends BaseObservable implements Serializable {

    /* ID để tìm kiếm so sánh */
    private String medicineId;
    private String measurementId;
    /* TÊN THUỐC VÀ TÊN ĐỊNH LƯỢNG */
    private String medicineName;
    private String measurementName;
    /* DANH SÁCH LỊCH SỬ THÊM HOẶC BÁN THUỐC ĐỂ THỐNG KÊ LỢI NHUẬN */
    private List<History> listHistory;

    @Ignore
    public Profit(){}

    public Profit(String medicineId, String measurementId, String medicineName, String measurementName, List<History> listHistory) {
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
        if (this.listHistory == null) {
            this.listHistory = new ArrayList<>();
        }
        return listHistory;
    }

    public void setListHistory(List<History> listHistory) {
        this.listHistory = listHistory;
        notifyPropertyChanged(BR.listHistory);
    }

    @Bindable
    /* SỐ LƯỢNG THUỐC HIỆN CÒN LẠI - SAU KHI ĐÃ THÊM - BÁN */
    public String getCurrentQuantity() {
        if (listHistory == null || listHistory.isEmpty()) {
            return "0";
        }

        String RESULT = "0";
        for (History history : listHistory) {
            /* NẾU THUỐC LÀ ĐƯỢC MUA VÀO - NHẬP VỀ */
            if (history.isAddMedicine()) {
                RESULT = StringUtil.getSum(RESULT, history.getQuantity());
            }
            /* NẾU THUỐC LÀ ĐƯỢC BÁN RA - BÁN ĐI */
            else {
                RESULT = StringUtil.getDifference(RESULT, history.getQuantity());
            }
        }
        return StringUtil.getDottedNumber(RESULT);
    }

    @Bindable
    /* LỢI NHUẬN THU ĐƯỢC - SAU KHI ĐÃ THÊM - BÁN */
    public String getProfit() {
        if (listHistory == null || listHistory.isEmpty()) {
            return "0";
        }

        String RESULT = "0";
        boolean check = false; // KO MANG DẤU
        for (History history : listHistory) {

            /* NẾU THUỐC LÀ ĐƯỢC MUA VÀO - NHẬP VỀ */
            if (history.isAddMedicine()) {
                /* TRUE: RESULT < TOTAL PRICE */
                if(StringUtil.isSmaller(RESULT, history.getTotalPrice())){
                    check = true; // MANG DẤU
                }
                /* FALSE: RESULT > TOTAL PRICE */
                else{
                    check = false; // KHÔNG MANG DẤU
                }
                RESULT = StringUtil.getDifference(RESULT, history.getTotalPrice());
            }
            /* NẾU THUỐC LÀ ĐƯỢC BÁN RA - BÁN ĐI */
            else {
                /* NẾU RESULT MANG DẤU - NHƯNG GIÁ TRỊ TUYỆT ĐỐI VẪN LỚN HƠN */
                if(RESULT.contains("-") && !StringUtil.isSmaller(RESULT, history.getTotalPrice())){
                    check = true; // MANG DẤU
                }
                /* NẾU RESULT MANG DẤU - NHƯNG GIÁ TRỊ TUYỆT ĐỐI NHỎ HƠN */
                else if (RESULT.contains("-") && StringUtil.isSmaller(RESULT, history.getTotalPrice())){
                    check = false; // KO MANG DẤU
                }
                RESULT = StringUtil.getSum(RESULT, history.getTotalPrice());

            }
            
            if(check == true){
                if(!RESULT.contains("-")){
                    RESULT = "-" + RESULT;
                }
            }else{
                RESULT = RESULT.replace("-", "");
            }
        }
        return StringUtil.getDottedNumber(RESULT);
    }
}
