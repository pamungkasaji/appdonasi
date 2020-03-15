package com.aji.donasi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FullscreenActivity extends AppCompatActivity {

    private TextView judul, nama,jumlah;
    private int id, id_konten;
    private String bukti;
    //ImageView gambarbukti;
    private PhotoView gambarbukti;
    private ProgressBar progressBar;

    private static final String TAG = "FullscreenActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        judul = findViewById(R.id.judul);
        nama = findViewById(R.id.nama);
        jumlah = findViewById(R.id.jumlah);

        FloatingActionButton buttonterima = findViewById(R.id.buttonterima);
        FloatingActionButton  buttontolak = findViewById(R.id.buttontolak);
        gambarbukti = findViewById(R.id.bukti);

        progressBar = findViewById(R.id.progBar);

        Intent detailIntent=getIntent();
        id = detailIntent.getIntExtra("id", -1);
        id_konten = detailIntent.getIntExtra("id_konten", -1);

        dataDonatur(id_konten, id);

        buttonterima.setOnClickListener((View v) -> {
            terimaDonatur();
        });

        buttontolak.setOnClickListener((View v) -> {
            initAlert();
        });

    }

    private void initAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.tolak_donasi)
                .setTitle(R.string.konfirmasi);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                tolakDonatur();
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

    private void dataDonatur(int id_konten, int id) {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<DonaturResponse> call = api.getDetailDonatur(id_konten, id);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    judul.setText(donaturResponse.getDonatur().getKonten().getJudul());
                    nama.setText(donaturResponse.getDonatur().getNama());
                    jumlah.setText(String.valueOf(donaturResponse.getDonatur().getJumlah()));
                    //buktilink=donaturResponse.getDonatur().getBukti();

                    bukti= Helper.IMAGE_URL_DONATUR+donaturResponse.getDonatur().getBukti();;

                    Glide.with(FullscreenActivity.this)
                            .load(bukti)
                            .placeholder(R.drawable.loading)
                            .into(gambarbukti);

                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FullscreenActivity.this, "Detail konten tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(FullscreenActivity.this, "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void terimaDonatur() {
        String token = Session.getInstance(FullscreenActivity.this).getToken();
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        Call<DefaultResponse> call = api.terimaDonasi(id_konten, id, token,Helper.TERIMA_DONASI);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();
                    Log.i(TAG, "Diterima");
                    Helper.infoDialog(FullscreenActivity.this,"Berhasil",defaultResponse.getMessage());
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FullscreenActivity.this, "Penerimaan donasi tidak dapat diakukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FullscreenActivity.this, R.string.periksa_koneksi, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tolakDonatur() {
        String token = Session.getInstance(FullscreenActivity.this).getToken();
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        Call<DefaultResponse> call = api.tolakDonasi(id_konten, id, token);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();
                    Log.i(TAG, "Ditolak");
                    progressBar.setVisibility(View.GONE);
                    infoDialogBack(defaultResponse.getMessage());
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FullscreenActivity.this, "Penerimaan donasi tidak dapat diakukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FullscreenActivity.this, R.string.periksa_koneksi, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void infoDialogBack(String message) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_done_white_24dp)
                .setTitle(R.string.konfirmasi)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .show();
    }
}
