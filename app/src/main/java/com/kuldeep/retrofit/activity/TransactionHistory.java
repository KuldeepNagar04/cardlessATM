package com.kuldeep.retrofit.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.model.TransResponse;
import com.kuldeep.retrofit.retro.ProfileService;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.util.PreferenceHelper;
import com.kuldeep.retrofit.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionHistory extends AppCompatActivity {

    @BindView(R.id.tvdate)
    TextView txtdate;
    @BindView(R.id.tvuser)
    TextView txtuser;
    @BindView(R.id.tvatm)
    TextView txtatm;

    private PreferenceHelper preferenceHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        ButterKnife.bind(this);

        username = Util.getUserName(this);

        Retrofit retrofit = RetroClient.getInstance();
        ProfileService api = retrofit.create(ProfileService.class);

        Call<String> call = api.getTransactionHistory(username);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                Log.i("response", String.valueOf(response.body()));

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Gson gson = new Gson();
                        TransResponse trans = gson.fromJson(response.body(), TransResponse.class);
                        String test = "Reached here";
                        Log.i("testing stage", String.valueOf(test));
                        Log.i("ytans", String.valueOf(trans.getData()));
                        if (trans != null) {
                            txtuser.setText(trans.getData().getUsername());
                            txtatm.setText(trans.getData().getAtmid());
                            txtdate.setText(trans.getData().getDate());

                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }
}
