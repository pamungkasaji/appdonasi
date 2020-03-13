package com.aji.donasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.LoginResponse;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout et_username, et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        Button login = findViewById(R.id.user_login);
        Button register = findViewById(R.id.user_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        String username = et_username.getEditText().getText().toString();
        String password = et_password.getEditText().getText().toString();

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<LoginResponse> call = api.userLogin(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null){
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // SP user dan token
                        Session.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());
                        Session.getInstance(LoginActivity.this).saveToken(loginResponse.getToken());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

//                        Toast.makeText(LoginActivity.this, loginResponse.getUser().getUsername(), Toast.LENGTH_LONG).show();
//                        Toast.makeText(LoginActivity.this, loginResponse.getToken(), Toast.LENGTH_SHORT).show();
                    } else {
                        Helper.warningDialog(LoginActivity.this, "Kesalahan", loginResponse.getMessage());
                    }
                } else {
                    Helper.warningDialog(LoginActivity.this, "Kesalahan", "Terjadi kesalaha saat login");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}
