package com.bphc.buyandsell;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("code")
    private int code;

    @SerializedName("result")
    private Result result;

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public Result getResult() {
        return result;
    }
}
