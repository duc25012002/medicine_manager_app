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
import com.hdcompany.plpsa888.databinding.ActivityRegisterBinding;
import com.hdcompany.plpsa888.model.Account;

public class RegisterActivity extends BaseActivity {

    public ActivityRegisterBinding activityRegisterBinding;
    public RegisterClickHandlers registerClickHandlers;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapIdToView();
        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = Constant.mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            GlobalFunction.showToast(RegisterActivity.this, getString(R.string.greeting_holder));
            GlobalFunction.startActivity(RegisterActivity.this, PLPSA.class);
            finish();
        }
    }


    private void setupViews() {
    }

    private void mapIdToView() {
        registerClickHandlers = new RegisterClickHandlers();
        /* GET VIEW VỚI DATA BINDING */
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        activityRegisterBinding.setClickHandlers(registerClickHandlers);
        account = new Account();
        activityRegisterBinding.setAccount(account);
    }

    public class RegisterClickHandlers {
        public void onRegisterClicked(View view) {
            if (TextUtils.isEmpty(account.getUserName())) {
                GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_missing_username));
            } else if (TextUtils.isEmpty(account.getUserEmail())) {
                GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_missing_email));
            } else if (TextUtils.isEmpty(account.getUserPassword())) {
                GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_missing_password));
            } else if (TextUtils.isEmpty(account.getVerifiedPassword())) {
                GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_missing_password_verified));
            } else if (!account.getUserEmail().matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
                GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_email_invalid));
            } else if (account.getUserEmail().matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
                // When email is valid then check the password
                if (account.getUserPassword().equals(account.getVerifiedPassword())) {
                    // When email and password verified then register
                    Constant.mFirebaseAuth.createUserWithEmailAndPassword(account.getUserEmail(), account.getUserPassword())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Keep Lost", getString(R.string.msg_success));
                                        Log.i("Keep Lost", "Thành CÔNG");
                                        FirebaseUser mUser = Constant.mFirebaseAuth.getCurrentUser();
                                        GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_success));
                                        GlobalFunction.startActivity(RegisterActivity.this, PLPSA.class);
                                        finish();
                                    } else {
                                        Log.w("Keep Lost", getString(R.string.msg_failed), task.getException());
                                        Log.i("Keep Lost", "THẤT BẠI VÀ LỖI LÀ :: "+task.getException());
                                        GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_failed));
                                    }
                                }
                            });
                } else {
                    GlobalFunction.showToast(RegisterActivity.this, getString(R.string.msg_password_resolve_problem));
                }
            }
        }

        public void onLoginClicked(View view) {
            GlobalFunction.startActivity(RegisterActivity.this, LoginActivity.class);
            finish();
        }
    }
}