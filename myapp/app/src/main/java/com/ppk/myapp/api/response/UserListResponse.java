package com.ppk.myapp.api.response;

import com.google.gson.annotations.SerializedName;
import com.ppk.myapp.model.User;

import java.util.ArrayList;

public class UserListResponse extends BaseResponse {
    @SerializedName("data")
    private ArrayList<User> userList;

    public UserListResponse(ArrayList<User> userList) {
        this.userList = userList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}
