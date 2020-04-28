package com.aji.donasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.ClientApi;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.LoginResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout et_username, et_password;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";
    private Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        login = findViewById(R.id.user_login);
        Button register = findViewById(R.id.user_register);
        progressBar = findViewById(R.id.progBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");
        }

        login.setOnClickListener(( View v) -> {
            if(validasi()){
                login.setEnabled(false);
                Helper.showProgress(progressBar, LoginActivity.this);
                userLogin();
            }
        });

        register.setOnClickListener((View v) -> {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private boolean validasi(){

        boolean check = true;

        if (!Helper.notEmpty(et_username, "Username")) check = false;
        if (!Helper.notEmpty(et_password, "Password")) check = false;

        return check;
    }

    private void userLogin() {
        String username = et_username.getEditText().getText().toString();
        String password = et_password.getEditText().getText().toString();

        Retrofit retrofit = NetworkClient.getApiClient();
        ClientApi clientApi = retrofit.create(ClientApi.class);

        Call<LoginResponse> call = clientApi.userLogin(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Helper.hideProgress(progressBar, LoginActivity.this);
                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    LoginResponse loginResponse = response.body();

                    Session.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());
                    Session.getInstance(LoginActivity.this).saveToken("Bearer " + loginResponse.getToken());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses error, Body not null");
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        login.setEnabled(true);
                        Helper.hideProgress(progressBar, LoginActivity.this);
                        Helper.warningDialog(LoginActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                login.setEnabled(true);
                Helper.hideProgress(progressBar, LoginActivity.this);
                Helper.warningDialog(LoginActivity.this, "Kesalahan", "Periksa koneksi internet anda");
            }
        });
    }
}
