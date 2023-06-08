package com.hdcompany.plpsa888.constant;


import com.google.firebase.auth.FirebaseAuth;

public interface Constant {

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    String LINK_FACEBOOK = "https://www.facebook.com/hoangduc2501";
    String PHONE_NUMBER = "+84793157708";
    String GMAIL = "ductrieuhoang@gmail.com";
    String ZALO_LINK = "https://zalo.me/g/htxedu143";

    String FIREBASE_URL = "https://plpsa-e9206-default-rtdb.firebaseio.com/";

    String CURRENCY = " VNĐ";

    // Key Intent
    String KEY_INTENT_MEDICINE_OBJECT = "med_object";
    String KEY_INTENT_MEDICINE_SOLD = "med_sold";
    String KEY_TYPE_STATISTICAL = "type_statistical";
    String KEY_MEDICINE_POPULAR = "med_popular";

    /* REVENUE = Profit : ĐÁNH GIÁ THU NHẬP*/
    int TYPE_REVENUE = 1;
    /* REVENUE = Statistical : TIÊU HAO KHI NHẬP - MUA THUỐC */
    int TYPE_COST = 2;
}
