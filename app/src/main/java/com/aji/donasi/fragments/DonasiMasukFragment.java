package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.FullscreenActivity;
import com.aji.donasi.adapters.DonasiMasukAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonasiMasukFragment extends Fragment implements DonasiMasukAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DonasiMasukAdapter adapter;
    private ArrayList<Donatur> donaturList;
    private TextView jumlahDonasiMasuk;

    private ProgressBar progressBar;

    private static final String TAG = "DonasiMasukFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donasimasuk, container, false);

        recyclerView = view.findViewById(R.id.recyclerDonasiMasuk);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progBar);
        jumlahDonasiMasuk = view.findViewById(R.id.jumlahDonasiMasuk);

        donaturList = new ArrayList<>();

        listDonaturUser();

        return view;
    }

    private void listDonaturUser() {
        String token = Session.getInstance(getActivity()).getToken();

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        Call<DonaturResponse> call = api.getDonaturUser(token);
        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    if (donaturResponse.getData().isEmpty()){
                        Toast.makeText(getActivity(), "Belum ada donasi masuk", Toast.LENGTH_SHORT).show();
                    }
                    donaturList = (ArrayList<Donatur>) donaturResponse.getData();
                    jumlahDonasiMasuk.setText(String.valueOf(donaturResponse.getData().size()));
                    adapter = new DonasiMasukAdapter(getActivity(), donaturList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                }
                adapter.setOnItemClickListener(DonasiMasukFragment.this);
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), FullscreenActivity.class);
        Donatur clickedItem = donaturList.get(position);
        detailIntent.putExtra("id", clickedItem.getId());
        detailIntent.putExtra("id_konten", clickedItem.getIdKonten());
        startActivity(detailIntent);
    }
}
