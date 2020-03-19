package com.aji.donasi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.Helper;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.activities.LoginActivity;
import com.aji.donasi.adapters.DonaturAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonaturFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonaturAdapter adapter;
    private ArrayList<Donatur> donaturList;
    private int id_konten;
    private ProgressBar progressBar;
    private TextView jumlahDonatur;

    private static final String TAG = "DonaturFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donatur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        progressBar = view.findViewById(R.id.progBar);
        jumlahDonatur = view.findViewById(R.id.jumlahDonatur);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        displayData(id_konten);
    }

    private void displayData(int id_konten) {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<DonaturResponse> call = api.getDonatur(id_konten);

        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    donaturList = (ArrayList<Donatur>) donaturResponse.getData();
                    jumlahDonatur.setText(String.valueOf(donaturResponse.getData().size()));
                    adapter = new DonaturAdapter(getActivity(), donaturList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Daftar donatur tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
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
