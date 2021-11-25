package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.kuldeep.retrofit.Constants;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.util.PreferenceHelper;
import com.kuldeep.retrofit.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {


    @BindView(R.id.btnScan)
    Button btnScan;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnProfile)
    Button btnProfile;
    @BindView(R.id.btnBalance)
    Button btnBalance;
    @BindView(R.id.btnTransaction)
    Button btnTransaction;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        preferenceHelper = new PreferenceHelper(this);
        Intent intent = getIntent();

        username = Util.getUserName(this);
    }

    @OnClick(R.id.btnLogout)
    public void logout() {
        preferenceHelper.putIsLogin(false);
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.btnScan)
    public void scanNow() {
        startActivity(new Intent(this, QRActivity.class));
    }

    @OnClick(R.id.btnProfile)
    public void viewProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        intent.putExtra(Constants.STRING_EXTRA, username);
        startActivity(intent);
    }

    @OnClick(R.id.btnBalance)
    public void viewBalance() {
        Intent intent = new Intent(WelcomeActivity.this, Balance.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
        /*WelcomeActivity.this.finish();*/
    }

    @OnClick(R.id.btnTransaction)
    public void viewTransaction() {
        Intent intent = new Intent(WelcomeActivity.this, TransactionHistory.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
        /*WelcomeActivity.this.finish();*/
    }
}
