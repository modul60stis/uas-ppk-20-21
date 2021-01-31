package com.ppk.myapp.api.response;

import com.google.gson.annotations.SerializedName;
import com.ppk.myapp.model.User;

public class UserResponse extends BaseResponse {
    @SerializedName("data")
    private User user;

    public UserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
