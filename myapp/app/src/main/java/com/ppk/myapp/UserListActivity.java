package com.ppk.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ppk.myapp.api.UserInterface;
import com.ppk.myapp.api.response.UserListResponse;
import com.ppk.myapp.api.services.UserInstance;
import com.ppk.myapp.model.User;
import com.ppk.myapp.ui.UserAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        getUserList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private String getUserToken(){
        SharedPreferences authPreferences = this.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return "Bearer "+ authPreferences.getString("token", "kosong");
    }

    private void getUserList(){
        UserInterface userInterface = UserInstance.getInstance().create(UserInterface.class);
        Call<UserListResponse> userListResponseCall = userInterface.getAllUsers(getUserToken());

        userListResponseCall.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful()){
                    generateList(response.body().getUserList());
                } else {
                    openToast("Failed to load data");
                    Log.d("[REST_API]", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                openToast("Please check your connection");
                Log.d("[REST_API]", t.getMessage());
            }
        });
    }

    private void openToast(String message) {
        Log.d("[REST_API]", message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void generateList(ArrayList<User> usersList){
        RecyclerView recyclerView = findViewById(R.id.user_recycle_view);
        UserAdapter adapter = new UserAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserListActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}