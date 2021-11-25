package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kuldeep.retrofit.Constants;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.model.ATMResponse;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.retro.RetroService;
import com.kuldeep.retrofit.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WithdrawDetail extends BaseActivity {

    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etpin)
    EditText etpin;
    @BindView(R.id.btnWithdraw)
    MaterialButton btnWithdraw;
    @BindView(R.id.btnAbort)
    MaterialButton btnAbort;


    String atmID;
    String tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_detail);
        ButterKnife.bind(this);

        atmID = getIntent().getStringExtra(Constants.STRING_EXTRA);
        tid = getIntent().getStringExtra(Constants.STRING_EXTRA1);
    }

    @OnClick(R.id.btnAbort)
    public void abort() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
        /*WelcomeActivity.this.finish();*/
    }

    @OnClick(R.id.btnWithdraw)
    public void withdraw() {
        Retrofit retrofit = RetroClient.getInstance();
        RetroService api = retrofit.create(RetroService.class);

        username = Util.getUserName(this);

        Call<String> call = api.setWithdrawalAmount(etAmount.getText().toString(),
                etpin.getText().toString(),
                atmID,
                username,tid);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String test = "Reached here";
                        Log.i("testing stage", String.valueOf(test));
                        Log.i("response", String.valueOf(response.body()));

                        ATMResponse atmResponse = gson.fromJson(response.body(), ATMResponse.class);

                        showDialog(atmResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void showDialog(String stringExtra) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cardless ATM")
                .setMessage(stringExtra)
                .setIcon(R.drawable.ic_qr_code)
                .setPositiveButton("OK", (dialog, which) -> {
                    startActivity(new Intent(this, WelcomeActivity.class));
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
