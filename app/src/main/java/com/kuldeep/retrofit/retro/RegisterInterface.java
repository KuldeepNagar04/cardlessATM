package com.kuldeep.retrofit.retro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {

    @FormUrlEncoded
    @POST("registerUser.php")
    Call<String> register(
            @Field("name") String name,
            @Field("mobileno") String mobileno,
            @Field("username") String uname,
            @Field("password") String password,
            @Field("email") String emailid
    );

    @FormUrlEncoded
    @POST("editDetails.php")
    Call<String> update(
            @Field("uname") String uname,
            @Field("mob") String mob,
            @Field("email") String email,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("editBankDetails.php")
    Call<String> bankDetails(
            @Field("bnname") String bnname,
            @Field("acchname") String acchname,
            @Field("accno") String accno,
            @Field("ifscode") String ifscode,
            @Field("panno") String panno,
            @Field("adhno") String adhno,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("setPin.php")
    Call<String> setpin(
            @Field("pin") String pinno,
            @Field("repin") String repin,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("resetPin.php")
    Call<String> resetpin(
            @Field("pino") String pino,
            @Field("pinn") String pinn,
            @Field("repinn") String repinn,
            @Field("username") String username
    );

}
