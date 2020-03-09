package com.aji.donasi.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aji.donasi.Helper;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.BeriDonasiActivity;
import com.aji.donasi.activities.TambahPerkembanganActivity;
import com.aji.donasi.adapters.PerkembanganAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.Perkembangan;
import com.aji.donasi.models.PerkembanganResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerkembanganFragment extends Fragment {

    private RecyclerView recyclerView;
    private PerkembanganAdapter adapter;
    private ArrayList<Perkembangan> perkembanganList;
    private int id_konten;
    private ProgressBar progressBar;
    private Button tambah;
    private String token;

    private static final String TAG = "PerkembanganFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perkembangan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        progressBar = view.findViewById(R.id.progBar);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        token = Session.getInstance(getActivity()).getToken();

        tambah = view.findViewById(R.id.tambah);
        tambah.setVisibility(View.GONE);

        if(Session.getInstance(getActivity()).isLoggedIn()) {
            initTambah();
        }

        //perkembanganList = new ArrayList<>();

        displayData(id_konten);

        tambah.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), TambahPerkembanganActivity.class);
            startActivity(intent);
        });
    }

    private void displayData(int id_konten) {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<PerkembanganResponse> call = api.getPerkembangan(id_konten);

        call.enqueue(new Callback<PerkembanganResponse>() {
            @Override
            public void onResponse(Call<PerkembanganResponse> call, Response<PerkembanganResponse> response) {

                if (response.body() != null) {
                    PerkembanganResponse perkembanganResponse = response.body();
                    perkembanganList = (ArrayList<Perkembangan>) perkembanganResponse.getData();
                    adapter = new PerkembanganAdapter(getActivity(), perkembanganList);
                    recyclerView.setAdapter(adapter);
                    Log.i(TAG, "Muat ulang");
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.w(TAG, "Body kosong");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Daftar perkembangan tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerkembanganResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTambah(){
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.isUser(id_konten, token);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    if (kontenResponse.isSuccess()) {
                        tambah.setVisibility(View.VISIBLE);
                        Log.i(TAG, "Is user iya");
                    }
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

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
