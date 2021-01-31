package com.ppk.myapp;

import androidx.appcompat.app.AppCompatActivity;

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
import com.ppk.myapp.api.request.RegisterRequest;
import com.ppk.myapp.api.response.BaseResponse;
import com.ppk.myapp.api.services.UserInstance;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_name;
    private EditText register_username;
    private EditText register_password;
    private EditText register_email;
    private EditText register_pass_confirm;
    private TextView txt_error;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_name = findViewById(R.id.txt_register_name);
        register_username = findViewById(R.id.txt_register_username);
        register_email = findViewById(R.id.txt_register_email);
        register_password = findViewById(R.id.txt_register_password);
        register_pass_confirm = findViewById(R.id.txt_register_pass_confirm);
        txt_error = findViewById(R.id.txt_register_error);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inisialisasi menggunakan builder design pattern
                RegisterRequest registerRequest = new RegisterRequest
                        .Builder(register_username.getText().toString())
                        .setEmail(register_email.getText().toString())
                        .setName(register_name.getText().toString())
                        .setPassword(register_password.getText().toString())
                        .setPass_confirm(register_pass_confirm.getText().toString())
                        .build();

                register(registerRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    private void register(RegisterRequest registerRequest){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<BaseResponse> call = userInterface.register(registerRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    openToast(response.body().getMessage());
                    finish();
                } else {
                    // Merubah response untuk response code 400 - 500
                    // dari JSON string menjadi JAVA object
                    Gson gson = new GsonBuilder().create();
                    BaseResponse registerResponse;
                    try {
                        registerResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        openToast(registerResponse.getMessage());
                        showErrorMessage(registerResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                openToast("Register failed, check your connection");
                Log.d("[FAILED]", t.getMessage());
            }
        });
    }


}