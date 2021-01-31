package com.ppk.myapp.api;

import com.ppk.myapp.api.request.LoginRequest;
import com.ppk.myapp.api.request.RegisterRequest;
import com.ppk.myapp.api.request.UpdateRequest;
import com.ppk.myapp.api.response.BaseResponse;
import com.ppk.myapp.api.response.LoginResponse;
import com.ppk.myapp.api.response.UserListResponse;
import com.ppk.myapp.api.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserInterface {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<BaseResponse> register(@Body RegisterRequest registerRequest);

    @GET("validate")
    Call<LoginResponse> validateToken(@Header("Authorization") String token);

    @GET("users")
    Call<UserListResponse> getAllUsers(@Header("Authorization") String token);

    @GET("users/{id}")
    Call<UserResponse> getUser(@Path("id") int id, @Header("Authorization") String token);

    @PUT("users/{id}")
    Call<BaseResponse> updateUsers(@Path("id") int id, @Body UpdateRequest updateRequest, @Header("Authorization") String token);
}
