package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.model.BalResponse;
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

public class Balance extends AppCompatActivity {

    @BindView(R.id.etpin)
    EditText etpin;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);

        username = Util.getUserName(this);
    }

    @OnClick(R.id.btnCBalance)
    public void checkBalance() {

        Retrofit retrofit = RetroClient.getInstance();
        RetroService api = retrofit.create(RetroService.class);


        Call<String> call = api.getBalance(etpin.getText().toString(),username);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        BalResponse balResponse = gson.fromJson(response.body(), BalResponse.class);
                        showDialog(balResponse.getMessage());
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
