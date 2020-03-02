package com.aji.donasi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.adapters.DonasiMasukAdapter;
import com.aji.donasi.adapters.KontenAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonasiMasukFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonasiMasukAdapter adapter;
    //private List<Konten> kontenList;
    private ArrayList<Donatur> donaturList;

    ProgressBar progressBar;

    private static final String TAG = "DonasiMasukFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donasimasuk, container, false);

        recyclerView = view.findViewById(R.id.recyclerDonasiMasuk);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progBar);

        donaturList = new ArrayList<>();

        //initializeRecyclerView();

        listDonaturUser();

        return view;
    }

    private void listDonaturUser() {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        String token = Session.getInstance(getActivity()).getToken();

        Call<DonaturResponse> call = api.getDonaturUser(token);

        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    donaturList = (ArrayList<Donatur>) donaturResponse.getData();
                    adapter = new DonasiMasukAdapter(getActivity(), donaturList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
                }

                //adapter.setOnItemClickListener(ListGalangDanaFragment.this);
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });
    }

}
