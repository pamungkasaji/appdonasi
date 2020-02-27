package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.adapters.KontenAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements KontenAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private KontenAdapter adapter;
    //private List<Konten> kontenList;
    private ArrayList<Konten> kontenList;

    private ProgressBar progressBar;

    private static final String TAG = "HomeFragment";

    public static final String EXTRA_IDKONTEN = "idkonten";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerKonten);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progBar);

        kontenList = new ArrayList<>();

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.getKonten();

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    Log.i(TAG, "Muat ulang");
                    kontenList = (ArrayList<Konten>) kontenResponse.getData();
                    adapter = new KontenAdapter(getActivity(), kontenList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
                }

                adapter.setOnItemClickListener(HomeFragment.this);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailKontenActivity.class);
        Konten clickedItem = kontenList.get(position);

        detailIntent.putExtra(EXTRA_IDKONTEN, clickedItem.getId());

        startActivity(detailIntent);
    }
}
