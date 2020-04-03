package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.activities.BeriDonasiActivity;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.adapters.KontenAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import org.greenrobot.eventbus.EventBus;

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
    private TextView keterangan;
    private String keyword;

    private ProgressBar progressBar;

    private static final String TAG = "HomeFragment";

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

        displayData();

        keterangan = view.findViewById(R.id.keterangan);
        Button button_search = view.findViewById(R.id.button_search);

        button_search.setOnClickListener((View v) -> {
            EditText cari = view.findViewById(R.id.cari);
            keyword = cari.getText().toString();
            searchKonten(keyword);
        });
    }

    private void searchKonten(String kata) {
        if (!kata.equals("")) {
            keterangan.setVisibility(View.VISIBLE);
            keterangan.setText(String.format("Hasil pencarian \"%s\"", kata));

            Retrofit retrofit = NetworkClient.getApiClient();
            Api api = retrofit.create(Api.class);

            progressBar.setVisibility(View.VISIBLE);

            Call<KontenResponse> call = api.searchKonten(keyword);
            call.enqueue(new Callback<KontenResponse>() {
                @Override
                public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {
                    if (response.body() != null) {
                        KontenResponse kontenResponse = response.body();
                        Log.d(TAG, "hasil pencarian");
                        kontenList = (ArrayList<Konten>) kontenResponse.getData();
                        if (kontenList.size() == 0) {
                            keterangan.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            keterangan.setText("Pencarian tidak ditemukan");
                            Toast.makeText(getActivity(), "Pencarian tidak ditemukan", Toast.LENGTH_SHORT).show();
                        } else {
                            keterangan.setVisibility(View.VISIBLE);
                            adapter = new KontenAdapter(getActivity(), kontenList);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Respon kosong", Toast.LENGTH_SHORT).show();
                    }

                    adapter.setOnItemClickListener(HomeFragment.this);
                }

                @Override
                public void onFailure(@NonNull Call<KontenResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getActivity(), "Silahkan Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Isi kata pencarian", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayData() {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.getKonten();

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    Log.d(TAG, "Muat ulang");
                    kontenList = (ArrayList<Konten>) kontenResponse.getData();
                    adapter = new KontenAdapter(getActivity(), kontenList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
                }

                adapter.setOnItemClickListener(HomeFragment.this);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal dan muat lagi");
                //progressBar.setVisibility(View.GONE);
                displayData();
                //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailKontenActivity.class);
        Konten clickedItem = kontenList.get(position);

        //EventBus.getDefault().postSticky(new MessageEvent(clickedItem.getId(), clickedItem.getNomorrekening(), clickedItem.getGambar()));
        EventBus.getDefault().postSticky(new KontenMessage(clickedItem));

        startActivity(detailIntent);
    }
}
