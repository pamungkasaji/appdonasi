package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.activities.LoginActivity;
import com.aji.donasi.activities.MainActivity;
import com.aji.donasi.activities.RegisterActivity;

public class AkunFragment extends Fragment {

    private Button button_login, button_register, button_logout;
    TextView belumauth;

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

    private void logout() {
        Session.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
