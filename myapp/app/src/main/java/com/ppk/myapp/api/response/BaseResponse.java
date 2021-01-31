package com.ppk.myapp.api.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("message")
    protected String message;
    @SerializedName("status")
    protected int status;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
