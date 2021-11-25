package com.kuldeep.retrofit.model;

import lombok.Data;

@Data
public class ATMResponse {
    int status;
    String message;
    String extra;
}
