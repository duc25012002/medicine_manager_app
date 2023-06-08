package com.hdcompany.plpsa888.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Bindable;

import java.io.Serializable;

/* TÀI KHOẢN */
public class Account extends BaseObservable implements Serializable {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String verifiedPassword;

    public Account() {
        userName = "";
        userEmail = "";
        userPassword = "";
        verifiedPassword = "";
    }

    public Account(String userName, String userEmail, String userPassword, String verifiedPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.verifiedPassword = verifiedPassword;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        notifyPropertyChanged(BR.userPassword);
    }

    @Bindable
    public String getVerifiedPassword() {
        return verifiedPassword;
    }

    public void setVerifiedPassword(String verifiedPassword) {
        this.verifiedPassword = verifiedPassword;
        notifyPropertyChanged(BR.verifiedPassword);
    }
}
