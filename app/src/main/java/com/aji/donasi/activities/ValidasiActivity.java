package com.aji.donasi.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.DonaturClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Donatur;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValidasiActivity extends AppCompatActivity {

    private TextView judul, nama,jumlah, nohp, is_anonim;
    private int id, id_konten;
    private PhotoView gambarbukti;
    private ProgressBar progressBar;
    private String token;

    private static final String TAG = "ValidasiActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        RelativeLayout layout_full = findViewById(R.id.layout_full);

        judul = findViewById(R.id.judul);
        nama = findViewById(R.id.nama);
        jumlah = findViewById(R.id.jumlah);
        nohp = findViewById(R.id.nohp);
        is_anonim = findViewById(R.id.is_anonim);

        FloatingActionButton buttonterima = findViewById(R.id.buttonterima);
        FloatingActionButton  buttontolak = findViewById(R.id.buttontolak);
        gambarbukti = findViewById(R.id.bukti);

        progressBar = findViewById(R.id.progBar);

        Gson gson = new Gson();
        Donatur donatur = gson.fromJson(getIntent().getStringExtra("donaturObject"), Donatur.class);
        initData(donatur);
        id = donatur.getId();
        id_konten = donatur.getIdKonten();

        token = Session.getInstance(ValidasiActivity.this).getToken();

        buttonterima.setOnClickListener((View v) -> {
            Helper.showProgress(progressBar, ValidasiActivity.this);
            approve();
        });

        buttontolak.setOnClickListener((View v) -> {
            initAlert();
        });

        gambarbukti.setOnClickListener((View v) -> {
            if (layout_full.getVisibility() == View.VISIBLE) {
                layout_full.setVisibility(View.GONE);
            } else {
                layout_full.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData(Donatur donatur){
        judul.setText(getResources().getString(R.string.judul_full, donatur.getJudul()));
        nama.setText(getResources().getString(R.string.nama_full, donatur.getNama()));
        nohp.setText(getResources().getString(R.string.nohp_full, donatur.getNohp()));

        jumlah.setText(getResources().getString(R.string.jumlah_full, Helper.mataUang(donatur.getJumlah())));
        if (donatur.getIsAnonim() == 0) {
            is_anonim.setVisibility(View.GONE);
        }

        String bukti= Helper.IMAGE_URL_DONATUR+donatur.getBukti();;
        Glide.with(ValidasiActivity.this)
                .load(bukti)
                .placeholder(R.drawable.placeholder_full)
                .into(gambarbukti);
    }

    private void initAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.tolak_donasi)
                .setTitle(R.string.konfirmasi);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Helper.showProgress(progressBar, ValidasiActivity.this);
                disapprove();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void approve() {
        Retrofit retrofit = NetworkClient.getApiClient();
        DonaturClient donaturClient = retrofit.create(DonaturClient.class);
        Call<DefaultResponse> call = donaturClient.approveDonasi(id_konten, id, token,Helper.TERIMA_DONASI);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, ValidasiActivity.this);

                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();
                    Log.d(TAG, "Diterima");
                    Helper.infoDialogFinish(ValidasiActivity.this,"Berhasil",defaultResponse.getMessage());
                } else {
                    Log.w(TAG, "Body kosong");
                    Toast.makeText(ValidasiActivity.this, "Penerimaan donasi tidak dapat diakukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                Helper.hideProgress(progressBar, ValidasiActivity.this);
                Toast.makeText(ValidasiActivity.this, R.string.periksa_koneksi, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disapprove() {
        Retrofit retrofit = NetworkClient.getApiClient();
        DonaturClient donaturClient = retrofit.create(DonaturClient.class);
        Call<DefaultResponse> call = donaturClient.disapproveDonasi(id_konten, id, token);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, ValidasiActivity.this);

                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();
                    Log.d(TAG, "Ditolak");
                    Helper.infoDialogFinish(ValidasiActivity.this, "Ditolak", defaultResponse.getMessage());
                } else {
                    Log.e(TAG, "Body kosong");
                    Toast.makeText(ValidasiActivity.this, "Penerimaan donasi tidak dapat diakukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                Helper.hideProgress(progressBar, ValidasiActivity.this);
                Toast.makeText(ValidasiActivity.this, R.string.periksa_koneksi, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
