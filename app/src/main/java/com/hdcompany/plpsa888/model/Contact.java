package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;
import androidx.room.Ignore;

import java.io.Serializable;

/* LIÊN HỆ NHÀ PHÁT TRIỂN */
public class Contact extends BaseObservable implements Serializable {
    private String name;
    private int image;

    @Ignore
    public Contact(){}

    public Contact(String name, int image) {
        this.name = name;
        this.image = image;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }
}
