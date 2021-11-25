package com.kuldeep.retrofit.retro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroService {

    @FormUrlEncoded
    @POST("loginUser.php")
    Call<String> getUserLogin(
            @Field("username") String uname,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("getqr.php")
    Call<String> setQRData(
            @Field("response") String response
    );

    @FormUrlEncoded
    @POST("withdrawDatail.php")
    Call<String> setWithdrawalAmount(
            @Field("amount") String amount,
            @Field("pin") String pin,
            @Field("atmid") String atmid,
            @Field("username") String username,
            @Field("tid") String tid
    );

    @FormUrlEncoded
    @POST("checkBalance.php")
    Call<String> getBalance(
            @Field("pin") String pin,
            @Field("username") String username
    );



}
