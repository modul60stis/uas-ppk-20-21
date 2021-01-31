package com.ppk.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ppk.myapp.api.UserInterface;
import com.ppk.myapp.api.response.UserResponse;
import com.ppk.myapp.api.services.UserInstance;
import com.ppk.myapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView txt_username;
    private TextView txt_email;
    private Button btn_logout;
    private Button btn_open_userlist;
    private Button btn_open_update;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btn_open_userlist = findViewById(R.id.btn_open_userlist);
        btn_open_userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserList();
            }
        });

        btn_open_update = findViewById(R.id.btn_open_update);
        btn_open_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateUser();
            }
        });

        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(getUserId(), getUserToken());
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

    private void saveUserData(User user){
        SharedPreferences userPreferences = getAuthPreferences();
        SharedPreferences.Editor userEditor = userPreferences.edit();
        userEditor.putString("username", user.getUsername());
        userEditor.putString("email", user.getEmail());
        userEditor.putString("fullname", user.getName());
        userEditor.apply();
        Log.d("[SUCCESS]", "User Data saved");
    }

    private void loadUserData(int id, String token){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<UserResponse> call = userInterface.getUser(id, token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    User user = response.body().getUser();
                    saveUserData(user);
                    txt_username.setText(user.getUsername());
                    txt_email.setText(user.getEmail());
                } else {
                    Log.d("[FAILED]", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("[FAILED]", t.getMessage());
            }
        });
    }

    private void logout(){
        SharedPreferences authPreferences = getAuthPreferences();
        SharedPreferences.Editor authEditor = authPreferences.edit();
        authEditor.clear();
        authEditor.apply();

        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToUserList(){
        intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    private void goToUpdateUser(){
        intent = new Intent(this, UpdateUserActivity.class);
        startActivity(intent);
    }
}