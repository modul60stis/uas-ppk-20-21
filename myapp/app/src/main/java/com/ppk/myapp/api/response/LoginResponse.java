package com.ppk.myapp.api.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {
    @SerializedName("data")
    private Data data;

    private static class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("token")
        private String token;
    }

    public int getId() {
        return data.id;
    }

    public String getToken() {
        return data.token;
    }
}
