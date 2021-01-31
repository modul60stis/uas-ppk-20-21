package com.ppk.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.ppk.myapp.api.request.UpdateRequest;
import com.ppk.myapp.api.response.BaseResponse;
import com.ppk.myapp.api.services.UserInstance;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    private EditText update_name;
    private EditText update_username;
    private EditText update_email;
    private TextView txt_error;
    private Button btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        update_email = findViewById(R.id.txt_update_email);
        update_name = findViewById(R.id.txt_update_name);
        update_username = findViewById(R.id.txt_update_username);
        txt_error = findViewById(R.id.txt_update_error);

        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = update_username.getText().toString();
                String fullname = update_name.getText().toString();
                String email = update_email.getText().toString();

                // inisialisasi menggunakan builder design pattern
                UpdateRequest updateRequest = new UpdateRequest.Builder(username)
                        .setEmail(email)
                        .setName(fullname)
                        .build();
                update(updateRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTempUpdateData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTempUpdateData();
        hideErrorMessage();
        finish();
    }

    private void openToast(String message) {
        Log.d("[TOAST]", message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        txt_error.setText(message);
        txt_error.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage(){
        txt_error.setText(null);
        txt_error.setVisibility(View.INVISIBLE);
    }

    private SharedPreferences getUpdatePreferences(){
        return this.getSharedPreferences("update", Context.MODE_PRIVATE);
    }

    private SharedPreferences getAuthPreferences(){
        return this.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    private int getUserId(){
        SharedPreferences authPreferences = getAuthPreferences();
        return authPreferences.getInt("user_id", 0);
    }

    private String getUserToken(){
        SharedPreferences authPreferences = getAuthPreferences();
        return "Bearer "+ authPreferences.getString("token", "kosong");
    }

    private void loadTempUpdateData(){
        SharedPreferences updatePreferences = getUpdatePreferences();
        update_email.setText(updatePreferences.getString("email", null));
        update_name.setText(updatePreferences.getString("fullname", null));
        update_username.setText(updatePreferences.getString("username", null));
    }

    private void saveTempUpdateData(){
        SharedPreferences updatePreferences = getUpdatePreferences();
        SharedPreferences.Editor updateEditor = updatePreferences.edit();
        updateEditor.putString("username", update_username.getText().toString());
        updateEditor.putString("email", update_email.getText().toString());
        updateEditor.putString("fullname", update_name.getText().toString());
        updateEditor.apply();
        Log.d("[SUCCESS]", "User Data saved");
    }

    private void clearTempUpdateData(){
        SharedPreferences updatePreferences = getUpdatePreferences();
        SharedPreferences.Editor updateEditor = updatePreferences.edit();
        updateEditor.clear();
        updateEditor.apply();
        loadTempUpdateData();
        hideErrorMessage();
    }

    private void update(UpdateRequest updateRequest){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<BaseResponse> call = userInterface.updateUsers(getUserId(), updateRequest, getUserToken());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    openToast("Update Success");
                    clearTempUpdateData();
                } else {
                    Gson gson = new GsonBuilder().create();
                    BaseResponse baseResponse;
                    try {
                        baseResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        openToast("Update Failed");
                        showErrorMessage(baseResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                openToast("Update Failed, check your connection");
                Log.d("[FAILED]", t.getMessage());
            }
        });
    }
}