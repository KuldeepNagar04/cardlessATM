package com.kuldeep.retrofit.retro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProfileService {

    @FormUrlEncoded
    @POST("userProfile.php")
    Call<String> getUserProfile(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("transactionHistory.php")
    Call<String> getTransactionHistory(
            @Field("username") String username
    );
}
