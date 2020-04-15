package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.activities.LoginActivity;
import com.aji.donasi.activities.MainActivity;
import com.aji.donasi.activities.RegisterActivity;
import com.aji.donasi.adapters.KontenAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AkunFragment extends Fragment {

    private Button button_login, button_register, button_logout;
    TextView belumauth;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_akun, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_login  = view.findViewById(R.id.button_login);
        button_register = view.findViewById(R.id.button_register);
        button_logout = view.findViewById(R.id.button_logout);

        TextView username = view.findViewById(R.id.username);
        TextView nama_lengkap = view.findViewById(R.id.nama_lengkap);
        belumauth = view.findViewById(R.id.belumauth);

        progressBar = view.findViewById(R.id.progBar);
        progressBar.setVisibility(View.GONE);

        username.setText(Session.getInstance(getActivity()).getUser().getUsername());
        nama_lengkap.setText(Session.getInstance(getActivity()).getUser().getNamalengkap());

        initAuth();

        button_login.setOnClickListener(v -> {
            login();
        });

        button_register.setOnClickListener(v -> {
            register();
        });

        button_logout.setOnClickListener(v -> {
            logout();
        });
    }

    private void initAuth(){
        if(Session.getInstance(getActivity()).isLoggedIn()) {
            button_login.setVisibility(View.GONE);
            button_register.setVisibility(View.GONE);
            button_logout.setVisibility(View.VISIBLE);
            belumauth.setVisibility(View.GONE);
        } else {
            button_login.setVisibility(View.VISIBLE);
            button_register.setVisibility(View.VISIBLE);
            button_logout.setVisibility(View.GONE);
            belumauth.setVisibility(View.VISIBLE);
        }
    }

    private void login(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void register(){
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

//    private void logout() {
//        Session.getInstance(getActivity()).clear();
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }

    private void logout() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        String token = Session.getInstance(getActivity()).getToken();

        Call<DefaultResponse> call = api.userLogout(token);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse defaultResponse = response.body();
                    Toast.makeText(getActivity(), defaultResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Logout berhasil");

                    Session.getInstance(getActivity()).clear();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Body kosong");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal dan muat lagi");
            }
        });
    }
}
