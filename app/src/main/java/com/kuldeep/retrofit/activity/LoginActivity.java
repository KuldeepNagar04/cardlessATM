package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kuldeep.retrofit.Constants;
import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.retro.RetroService;
import com.kuldeep.retrofit.util.PrefUtil;
import com.kuldeep.retrofit.util.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {
    private EditText etUname, etPass;
    private Button btnlogin;
    private TextView tvreg;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceHelper = new PreferenceHelper(this);

        etUname = (EditText) findViewById(R.id.etusername);
        etPass = (EditText) findViewById(R.id.etpassword);

        btnlogin = (Button) findViewById(R.id.btnLogout);
        tvreg = (TextView) findViewById(R.id.tvreg);

        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {

        username = etUname.getText().toString().trim();
        password = etPass.getText().toString().trim();

        Retrofit retrofit = RetroClient.getInstance();
        RetroService api = retrofit.create(RetroService.class);

        Call<String> call = api.getUserLogin(username, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void parseLoginData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {

                saveInfo(response);

                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.STRING_EXTRA, username);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    private void saveInfo(String response) {
        PrefUtil.putBoolean(this, Constants.PREFERENCE_LOGIN, true);
        PrefUtil.putString(this, Constants.PREFERENCE_USERNAME, username);

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
