package com.kuldeep.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trans {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("name")
    @Expose
    private String username;
    @SerializedName("atmid")
    @Expose
    private String atmid;
}
