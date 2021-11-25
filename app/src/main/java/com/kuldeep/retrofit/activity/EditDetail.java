package com.kuldeep.retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kuldeep.retrofit.R;
import com.kuldeep.retrofit.retro.RegisterInterface;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.util.PreferenceHelper;
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

public class EditDetail extends AppCompatActivity {

    @BindView(R.id.etnuname)
    EditText etnuname;

    @BindView(R.id.etnmob)
    EditText etnmob;

    @BindView(R.id.etnemail)
    EditText etnemail;

    @BindView(R.id.btnUpdate)
    Button update;

    String username;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);

        ButterKnife.bind(this);

        username = Util.getUserName(this);
    }

    @OnClick(R.id.btnUpdate)
    public void update() {updateMe();}

    public void updateMe() {

        final String uname = etnuname.getText().toString();
        final String mob = etnmob.getText().toString();
        final String email = etnemail.getText().toString();

        Retrofit retrofit = RetroClient.getInstance();
        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.update(uname,mob, email, username);

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
           /* saveInfo(response);*/
            Toast.makeText(EditDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditDetail.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {

            Toast.makeText(EditDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void saveInfo(String response) {
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
    }*/


}

