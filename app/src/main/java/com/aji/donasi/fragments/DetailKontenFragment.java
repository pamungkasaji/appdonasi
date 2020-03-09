package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.BeriDonasiActivity;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.PerkembanganResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailKontenFragment extends Fragment {

    private TextView tv_judul, tv_deskripsi, tv_target, tv_perpanjangan;
    private int id_konten;
    private Button beriDonasi;
    private ProgressBar progressBar;

    private Button perpanjangan;
    private String token;

    private static final String TAG = "DetailKontenFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailkonten, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        tv_judul = view.findViewById(R.id.tv_judul);
        tv_deskripsi = view.findViewById(R.id.tv_deskripsi);
        tv_target = view.findViewById(R.id.tv_target);
        beriDonasi = view.findViewById(R.id.beriDonasi);
        tv_perpanjangan = view.findViewById(R.id.tv_perpanjangan);
        tv_perpanjangan.setVisibility(View.GONE);

        progressBar = view.findViewById(R.id.progBar);

        token = Session.getInstance(getActivity()).getToken();

        perpanjangan = view.findViewById(R.id.perpanjangan);
        perpanjangan.setVisibility(View.GONE);

        if(Session.getInstance(getActivity()).isLoggedIn()) {
            initPerpanjangan();
        }

        displayDetail(id_konten);

        beriDonasi.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), BeriDonasiActivity.class);
            startActivity(intent);
        });
    }

    private void displayDetail(int id_konten) {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.getKontenDetail(id_konten);

        //progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    tv_judul.setText(kontenResponse.getKonten().getJudul());
                    tv_deskripsi.setText(kontenResponse.getKonten().getDeskripsi());
                    tv_target.setText(String.valueOf(kontenResponse.getKonten().getTarget()));
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Detail konten tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPerpanjangan(){
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.isUser(id_konten, token);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    if (kontenResponse.isSuccess()) {
                        perpanjangan.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), kontenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), kontenResponse.getKonten().getPerpanjangan().getStatus(), Toast.LENGTH_SHORT).show();
                        tv_perpanjangan.setText(kontenResponse.getKonten().getPerpanjangan().getStatus());
                        tv_perpanjangan.setVisibility(View.VISIBLE);
                        Log.i(TAG, "Is user iya");
                    }
                    Log.i(TAG, "Is user bukan");
                    //progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    //Toast.makeText(getActivity(), "Is User tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                //progressBar.setVisibility(View.GONE);
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                //Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        id_konten = event.id_konten;
    }
//    @Override public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
