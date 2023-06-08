package com.hdcompany.plpsa888;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hdcompany.plpsa888.constant.Constant;

public class PLPSAApplication extends Application {
    private FirebaseDatabase mFirebaseDatabase;

    public static PLPSAApplication get(Context context){
        return(PLPSAApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        Initializing Firebase here.
         */
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance(Constant.FIREBASE_URL);
    }

    public DatabaseReference getMeasurementDatabaseRef() {
        /* ĐỊNH LƯỢNG */
        return mFirebaseDatabase.getReference("/measurement");
    }

    public DatabaseReference getMedicineDatabaseRef() {
        /* THUỐC */
        return mFirebaseDatabase.getReference("/medicine");
    }

    public DatabaseReference getHistoryDatabaseRef() {
        /* LỊCH SỬ MUA - BÁN */
        return mFirebaseDatabase.getReference("/history");
    }

    public DatabaseReference getQuantityDatabaseRef(String medicine_id) {
        /* SỐ LƯỢNG THUỐC */
        return mFirebaseDatabase.getReference("/medicine/" + medicine_id + "/medicineQuantity");
    }
}
