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
    private Button submit;
    private EditText editTextJumlahHari, editTextAlasan;
    private int id_konten;

    private static final String TAG = "Perpanjangan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahperkembangan);

        EventBus.getDefault().register(this);

        //Initializing views
        submit = findViewById(R.id.submit);

        editTextJumlahHari = findViewById(R.id.editTextJumlahHari);
        editTextAlasan = findViewById(R.id.editTextAlasan);

        submit.setOnClickListener((View v) -> {
            submitPerpanjangan();
        });
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
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

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        String token = Session.getInstance(PerpanjanganActivity.this).getToken();

        Call<DefaultResponse> call = api.perpanjangan(id_konten, token, hari, alasan);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();

                    if (defaultResponse.isSuccess()) {
                        Log.i(TAG, "Pengajuan perpanjangan dikirim");
                        Helper.infoDialog(PerpanjanganActivity.this, "Pemberitahuan", defaultResponse.getMessage());
                    } else {
                        Log.w(TAG, "isSuccess false");
                        Helper.warningDialog(PerpanjanganActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                } else {
                    Log.w(TAG, "Body kosong");
                    Helper.warningDialog(PerpanjanganActivity.this, "Kesalahan", "Respon kosong");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
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
