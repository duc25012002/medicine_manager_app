package com.hdcompany.plpsa888.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.hdcompany.plpsa888.R;
import com.hdcompany.plpsa888.constant.Constant;
import com.hdcompany.plpsa888.constant.GlobalFunction;
import com.hdcompany.plpsa888.databinding.ActivityLoginBinding;
import com.hdcompany.plpsa888.model.Account;

public class LoginActivity extends BaseActivity {
    public ActivityLoginBinding activityLoginBinding;
    public LoginClickHandlers loginClickHandlers;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapIdToView();
        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = Constant.mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            GlobalFunction.showLongToast(LoginActivity.this, getString(R.string.greeting_holder));
            GlobalFunction.startActivity(LoginActivity.this, PLPSA.class);
            finish();
        }
    }

    private void setupViews() {
    }

    private void mapIdToView() {
        loginClickHandlers = new LoginClickHandlers();
        /* GET VIEW VỚI DATA BINDING */
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setClickHandlers(loginClickHandlers);
        account = new Account();
        activityLoginBinding.setAccount(account);
    }

    public class LoginClickHandlers {
        public void onRegisterClicked(View view) {
            GlobalFunction.startActivity(LoginActivity.this, RegisterActivity.class);
            finish();
        }

        public void onLoginClicked(View view) {
            if (TextUtils.isEmpty(account.getUserEmail())) {
                GlobalFunction.showToast(LoginActivity.this, getString(R.string.msg_missing_email));
            } else if (TextUtils.isEmpty(account.getUserPassword())) {
                GlobalFunction.showToast(LoginActivity.this, getString(R.string.msg_missing_password));
            } else if (!account.getUserEmail().matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
                GlobalFunction.showToast(LoginActivity.this, getString(R.string.msg_email_invalid));
            } else if (account.getUserEmail().matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
                Constant.mFirebaseAuth.signInWithEmailAndPassword(account.getUserEmail(), account.getUserPassword())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Keep Lost", getString(R.string.msg_success));
                                    Log.i("Keep Lost", "Thành CÔNG");
                                    FirebaseUser mUser = Constant.mFirebaseAuth.getCurrentUser();
                                    GlobalFunction.showToast(LoginActivity.this, getString(R.string.msg_success));
                                    GlobalFunction.startActivity(LoginActivity.this, PLPSA.class);
                                    finish();
                                } else {
                                    Log.w("Keep Lost", getString(R.string.msg_failed), task.getException());
                                    Log.i("Keep Lost", "THẤT BẠI VÀ LỖI LÀ :: "+task.getException());
                                    GlobalFunction.showToast(LoginActivity.this, getString(R.string.msg_failed));
                                }
                            }
                        });

            }
        }

        public void onForgotPasswordClicked(View view) {
            showToast("Đang cập nhật");
        }
    }
}