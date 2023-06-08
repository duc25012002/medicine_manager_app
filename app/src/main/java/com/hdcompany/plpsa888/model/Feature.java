package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import java.io.Serializable;

/* CHỨC NĂNG TRONG APP */
public class Feature extends BaseObservable implements Serializable {
    private int id;
    private int image;
    private String title;

    @Ignore
    public Feature(){}
    public Feature(int id, int image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    // Danh sách Chức năng
    public static final int FEATURE_VIEW_LIST_MED = 0; // Danh sách thuốc
    public static final int FEATURE_VIEW_HISTORY = 1; // Lịch sử
    public static final int FEATURE_VIEW_PROFIT = 2; // Lợi tức
    public static final int FEATURE_VIEW_STATISTICAL = 3; // Thống Kê Dùng để báo cáo
    public static final int FEATURE_VIEW_COST = 4; // Tiêu phí
    public static final int FEATURE_VIEW_REVENUE = 5; // Thu nhập
    public static final int FEATURE_VIEW_OUT_OF_STOCK = 6; // Thuốc hết trong kho
    public static final int FEATURE_VIEW_MED_SOLD = 7; // Thuốc đã bán ra
    public static final int FEATURE_VIEW_MEASUREMENT = 8; // Định lượng
    public static final int FEATURE_MED_IN = 9; // Định lượng
    public static final int FEATURE_MED_OUT = 10; // Định lượng
    public static final int FEATURE_NEW_MED_BILL = 11; // Định lượng
}
