package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.retro.RegisterInterface;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.util.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etname)
    TextInputEditText etname;
    @BindView(R.id.etmob)
    EditText etmob;
    @BindView(R.id.etusername)
    EditText etusername;
    @BindView(R.id.etemail)
    EditText etemail;
    @BindView(R.id.etpassword)
    EditText etpassword;
    @BindView(R.id.btnRegister)
    Button btn;
    @BindView(R.id.tvlogin)
    TextView tvlogin;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preferenceHelper = new PreferenceHelper(this);

        /*if (preferenceHelper.getIsLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }*/
    }

    @OnClick(R.id.tvlogin)
    public void login() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        /*MainActivity.this.finish();*/
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        registerMe();
    }


    private void registerMe() {

        final String name = etname.getText().toString();
        final String mob = etmob.getText().toString();
        final String emailid = etemail.getText().toString();
        final String username = etusername.getText().toString();
        final String password = etpassword.getText().toString();

        Retrofit retrofit = RetroClient.getInstance();
        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.register(name, mob, username, password,emailid);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("response", String.valueOf(response.body()));
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
            saveInfo(response);
            Toast.makeText(MainActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {

            Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInfo(String response) {
        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putName(dataobj.getString("name"));
                    preferenceHelper.putHobby(dataobj.getString("hobby"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
