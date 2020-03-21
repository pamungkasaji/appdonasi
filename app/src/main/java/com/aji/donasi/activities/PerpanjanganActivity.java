package com.aji.donasi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aji.donasi.Helper;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Perpanjangan;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerpanjanganActivity extends AppCompatActivity {

    //Declaring views
    private EditText editTextJumlahHari, editTextAlasan;
    private int id_konten;
    private ProgressBar progressBar;

    private static final String TAG = "PerpanjanganActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perpanjangan);

        EventBus.getDefault().register(this);

        Button submit = findViewById(R.id.submit);
        editTextJumlahHari = findViewById(R.id.editTextJumlahHari);
        editTextAlasan = findViewById(R.id.editTextAlasan);
        progressBar = findViewById(R.id.progBar);
        progressBar.setVisibility(View.GONE);

        submit.setOnClickListener((View v) -> {
            submitPerpanjangan();
        });
    }

    public void submitPerpanjangan() {
        //getting name for the image
        String hari = editTextJumlahHari.getText().toString();
        String alasan = editTextAlasan.getText().toString();

        if (hari.isEmpty()) {
            editTextJumlahHari.setError("Isi jumlah hari");
            editTextJumlahHari.requestFocus();
            return;
        }

        if (alasan.isEmpty()) {
            editTextAlasan.setError("Isi kolom deskripsi");
            editTextAlasan.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        String token = Session.getInstance(PerpanjanganActivity.this).getToken();
        Call<DefaultResponse> call = api.perpanjangan(id_konten, token, hari, alasan);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null")
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialog(PerpanjanganActivity.this, "Pemberitahuan", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null")
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        Helper.warningDialog(PerpanjanganActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(PerpanjanganActivity.this, "Kesalahan", "Periksa koneksi anda");
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        id_konten = event.id_konten;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
