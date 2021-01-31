package com.ppk.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ppk.myapp.api.UserInterface;
import com.ppk.myapp.api.response.LoginResponse;
import com.ppk.myapp.api.services.UserInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCheckActivity extends AppCompatActivity {
    private Intent activityIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = getToken();

        if (token == null){
            goToLoginActivity();
        } else {
            validateToken(token);
        }
        finish();
    }

    private String getToken(){
        SharedPreferences authPreferences = AuthCheckActivity.this.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return "Bearer "+ authPreferences.getString("token", null);
    }

    private void saveToken(int id, String token){
        SharedPreferences authPreferences = AuthCheckActivity.this.getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor tokenEditor = authPreferences.edit();
        tokenEditor.putInt("user_id", id);
        tokenEditor.putString("token", token);
        tokenEditor.apply();
        Log.d("[REST_API]", "Token saved");
    }

    private void goToMainActivity(){
        activityIntent = new Intent(this, MainActivity.class);
        startActivity(activityIntent);
        finish();
    }

    private void goToLoginActivity(){
        activityIntent = new Intent(this, LoginActivity.class);
        startActivity(activityIntent);
        finish();
    }

    private void validateToken(String token){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<LoginResponse> validateToken = userInterface.validateToken(token);

        validateToken.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    int id = response.body().getId();
                    String token = response.body().getToken();
                    saveToken(id, token);
                    goToMainActivity();
                } else {
                    goToLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                goToLoginActivity();
            }
        });
    }
}
