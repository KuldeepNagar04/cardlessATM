package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.model.Profile;
import com.kuldeep.retrofit.model.ProfileResponse;
import com.kuldeep.retrofit.retro.ProfileService;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.util.Util;
import com.kuldeep.retrofit.util.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.txt_mobile)
    TextView txtMobile;
    @BindView(R.id.txt_email)
    TextView txtEmail;

    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.txt_account)
    TextView txtAccount;
    @BindView(R.id.txt_bank)
    TextView txtBank;

    @BindView(R.id.btn_set_bank)
    Button btnSetBank;
    @BindView(R.id.btn_change_pin)
    Button btnChangePin;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        username = Util.getUserName(this);
        fetchProfile();
    }

    private void fetchProfile() {
        Retrofit retrofit = RetroClient.getInstance();
        ProfileService api = retrofit.create(ProfileService.class);

        Call<String> call = api.getUserProfile(username);



        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                Log.i("response", String.valueOf(response));

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        ProfileResponse profile = gson.fromJson(response.body(), ProfileResponse.class);
                        String test = "Reached here";
                        Log.i("testing stage", String.valueOf(test));
                        Log.i("response", String.valueOf(profile));
                        if (profile != null) {
                            populateData(profile.getData());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    private void populateData(Profile profile) {
        txtUsername.setText(username);
        txtMobile.setText(profile.getMobileNumber());
        txtEmail.setText(profile.getEmailId());

        txtAccount.setText(profile.getAccountNumber());
        txtBank.setText(profile.getBankName());

    }

    @OnClick(R.id.btn_edit)
    public void editDetail() {

        Intent intent = new Intent(this, EditDetail.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
    }

    @OnClick(R.id.btn_set_bank)
    public void setBank() {

        Intent intent = new Intent(this, EditBankDetail.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
    }

    @OnClick(R.id.btn_change_pin)
    public void changePin() {

        Intent intent = new Intent(this, PinActivity.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        startActivity(intent);
    }
}
