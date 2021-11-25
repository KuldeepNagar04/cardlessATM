/*
 * Aurora Droid
 * Copyright (C) 2019-20, Rahul Kumar Patel <whyorean@gmail.com>
 *
 * Aurora Droid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aurora Droid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Droid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kuldeep.retrofit.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;
import com.kuldeep.retrofit.Constants;
import com.kuldeep.retrofit.model.ATMResponse;
import com.kuldeep.retrofit.model.QResponse;
import com.kuldeep.retrofit.retro.RetroClient;
import com.kuldeep.retrofit.retro.RetroService;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QRActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        checkPermissions();
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        scannerView.stopCameraPreview();
        scannerView.stopCamera();

        String rawResponse = rawResult.getText();

        QResponse qrResponse = gson.fromJson(rawResponse, QResponse.class);

        Log.e("RETRO", rawResponse);

        Retrofit retrofit = RetroClient.getInstance();
        RetroService api = retrofit.create(RetroService.class);

        Call<String> call = api.setQRData(rawResponse);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("RETRO", response.body());
                    ATMResponse atmResponse = gson.fromJson(response.body(), ATMResponse.class);
                    if (atmResponse.getStatus() == 200) {
                        Intent intent = new Intent(QRActivity.this, WithdrawDetail.class);
                        intent.putExtra(Constants.STRING_EXTRA, qrResponse.getName());
                        intent.putExtra(Constants.STRING_EXTRA1, qrResponse.getTid());
                        startActivity(intent);
                    }
                }

                finish();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                finish();
            }
        });
    }

    private void checkPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1337);
    }
}
