package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.retro.RegisterInterface;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PinActivity extends AppCompatActivity {

    @BindView(R.id.etpin)
    EditText pin;

    @BindView(R.id.retpin)
    EditText rpin;

    @BindView(R.id.etopin)
    EditText opin;

    @BindView(R.id.etnpin)
    EditText npin;

    @BindView(R.id.retnpin)
    EditText rnpin;

    @BindView(R.id.btnSetPin)
    Button setpin;

    @BindView(R.id.btnResetPin)
    Button resetpin;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        ButterKnife.bind(this);

        username = Util.getUserName(this);
    }



    @OnClick(R.id.btnSetPin)
    public void setpin() {setMyPin();}

    @OnClick(R.id.btnResetPin)
    public void resetpin() {resetMyPin();}

    public void setMyPin() {

        final String pinno = pin.getText().toString();
        final String repin = rpin.getText().toString();

        Retrofit retrofit = RetroClient.getInstance();
        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.setpin(pinno, repin, username);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            parseRegData(response.body());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });

    }


    public void resetMyPin() {

        final String pino = opin.getText().toString();
        final String pinn = npin.getText().toString();
        final String repinn = rnpin.getText().toString();

        Retrofit retrofit = RetroClient.getInstance();
        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.resetpin(pino, pinn, repinn, username);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            parseRegData(response.body());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });

    }

    private void parseRegData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true")) {
            /* saveInfo(response);*/
            Toast.makeText(this, "PIN Set Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            /*this.finish();*/
        } else {

            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

}
