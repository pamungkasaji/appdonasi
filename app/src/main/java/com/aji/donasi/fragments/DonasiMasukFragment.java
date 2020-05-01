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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.ValidasiActivity;
import com.aji.donasi.adapters.DonasiMasukAdapter;
import com.aji.donasi.api.DonaturClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;
import com.google.gson.Gson;

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

    private static final String TAG = "BuatFragemnt: DonasiMasukFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donasimasuk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerDonasiMasuk);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progBar);
        jumlahDonasiMasuk = view.findViewById(R.id.jumlahDonasiMasuk);

        donaturList = new ArrayList<>();

    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getDonaturUser();
        Log.d(TAG, "Fragment on resume, getDonaturUser();");
    }

    private void getDonaturUser() {
        String token = Session.getInstance(getActivity()).getToken();

        Retrofit retrofit = NetworkClient.getApiClient();
        DonaturClient donaturClient = retrofit.create(DonaturClient.class);
        Call<DonaturResponse> call = donaturClient.getDonaturUser(token);
        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.d(TAG, "Muat ulang getDonaturUser()");
                    donaturList = (ArrayList<Donatur>) donaturResponse.getData();
                    if (donaturList.isEmpty()){
                        Toast.makeText(getActivity(), "Belum ada donasi masuk", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Belum ada donasi masuk");
                    }
                    jumlahDonasiMasuk.setText(String.valueOf(donaturResponse.getData().size()));
                    adapter = new DonasiMasukAdapter(getActivity(), donaturList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Body kosong");
                    Toast.makeText(getActivity(), "Body kosong", Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);
                adapter.setOnItemClickListener(DonasiMasukFragment.this);
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
//                Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), ValidasiActivity.class);

        Gson gson = new Gson();
        String donatur = gson.toJson(donaturList.get(position));
        detailIntent.putExtra("donaturObject", donatur);
        startActivity(detailIntent);
    }
}
