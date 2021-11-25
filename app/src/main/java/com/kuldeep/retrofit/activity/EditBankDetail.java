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

public class EditBankDetail extends AppCompatActivity {

    @BindView(R.id.etbname)
    EditText bname;

    @BindView(R.id.etahname)
    EditText ahname;

    @BindView(R.id.etacno)
    EditText acno;

    @BindView(R.id.etifsc)
    EditText ifsc;

    @BindView(R.id.etpan)
    EditText pan;

    @BindView(R.id.etadno)
    EditText adno;

    @BindView(R.id.btnsubmit)
    Button submit;

    String username;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank_detail);

        ButterKnife.bind(this);

        username = Util.getUserName(this);
    }

    @OnClick(R.id.btnsubmit)
    public void submit() { submitMe(); }

    public void submitMe() {

        final String bnname = bname.getText().toString();
        final String achname = ahname.getText().toString();
        final String accno = acno.getText().toString();
        final String ifscode = ifsc.getText().toString();
        final String panno = pan.getText().toString();
        final String adhno = adno.getText().toString();

        Retrofit retrofit = RetroClient.getInstance();
        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.bankDetails(bnname, achname, accno, ifscode, panno, adhno, username);

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
            Toast.makeText(EditBankDetail.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditBankDetail.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            /*this.finish();*/
        } else {

            Toast.makeText(EditBankDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }
}
