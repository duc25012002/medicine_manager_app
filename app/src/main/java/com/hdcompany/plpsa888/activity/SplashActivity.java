package com.hdcompany.plpsa888.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.FirebaseApp;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.constant.GlobalFunction;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startLogin();

    }
    private void startLogin() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlobalFunction.startActivity(SplashActivity.this,LoginActivity.class);
                finish();
            }
        }, 2000);
    }
}