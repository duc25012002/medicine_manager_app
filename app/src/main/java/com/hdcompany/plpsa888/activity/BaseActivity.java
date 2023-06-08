package com.hdcompany.plpsa888.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hdcompany.plpsa888.R;

public abstract class BaseActivity extends AppCompatActivity {
    public AlertDialog alertDialog;

    @Override
    public void onBackPressed() {
        alertDialog.setTitle("Xác nhận thoát?");
        alertDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton(getString(R.string.action_cancel), (dialog, which) -> {
                }).create();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void startActivity(Context context, Class<?> clss) {
        Intent i = new Intent(context, clss);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void startActivity(Context context, Class<?> clss, Bundle bundle) {
        Intent i = new Intent(context, clss);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(bundle);
        context.startActivity(i);
    }
}
