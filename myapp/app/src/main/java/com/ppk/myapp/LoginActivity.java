package com.ppk.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ppk.myapp.api.UserInterface;
import com.ppk.myapp.api.request.LoginRequest;
import com.ppk.myapp.api.response.LoginResponse;
import com.ppk.myapp.api.response.UserResponse;
import com.ppk.myapp.api.services.UserInstance;
import com.ppk.myapp.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText register_username;
    private EditText register_password;
    private Button btn_login;
    private TextView txt_error;
    private TextView txt_go_to_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_username = findViewById(R.id.txt_login_username);
        register_password = findViewById(R.id.txt_login_password);
        btn_login = findViewById(R.id.btn_login);
        txt_error = findViewById(R.id.txt_login_error);
        txt_go_to_register = findViewById(R.id.txt_go_to_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = register_username.getText().toString();
                String password = register_password.getText().toString();
                LoginRequest loginRequest = new LoginRequest(username, password );
                login(loginRequest);
            }
        });

        txt_go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void goToRegister(){
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    private void goToMainActivty(){
        Intent mainAcitivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainAcitivity);
        finish();
    }

    private void openToast(String message) {
        Log.d("[TOAST]", message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        txt_error.setText(message);
        txt_error.setVisibility(View.VISIBLE);
        Log.d("[VALIDATION ERROR]", message);
    }

    private void saveToken(int id, String token){
        SharedPreferences authPreferences = LoginActivity.this.getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor tokenEditor = authPreferences.edit();
        tokenEditor.putInt("user_id", id);
        tokenEditor.putString("token", token);
        tokenEditor.apply();
        Log.d("[SUCCESS]", "Token saved");
    }

    private void login(LoginRequest loginRequest){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<LoginResponse> call = userInterface.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    int id = response.body().getId();
                    String token = response.body().getToken();
                    saveToken(id, token);
                    openToast("Login berhasil");
                    goToMainActivty();
                } else {
                    // Merubah response untuk response code 400 - 500
                    // dari JSON string menjadi JAVA object
                    Gson gson = new GsonBuilder().create();
                    LoginResponse loginResponse;
                    try {
                        loginResponse = gson.fromJson(response.errorBody().string(), LoginResponse.class);
                        openToast("Login gagal, harap cek lagi data anda");
                        showErrorMessage(loginResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                openToast("Login gagal, harap cek koneksi internet anda");
                Log.d("[FAILED]", t.getMessage());
            }
        });
    }

}