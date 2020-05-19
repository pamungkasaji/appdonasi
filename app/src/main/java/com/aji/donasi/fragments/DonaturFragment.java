package com.aji.donasi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.adapters.DonaturAdapter;
import com.aji.donasi.api.DonaturClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonaturFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView recyclerView;
    private DonaturAdapter adapter;
    private ArrayList<Donatur> donaturList;
    private ProgressBar progressBar;
    private TextView jumlahDonatur;
    private TextView sort;

    //EventBus
    private int id_konten;
    private Konten kontenMessage;

    private static final String TAG = "DonaturFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donatur, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        progressBar = view.findViewById(R.id.progBar);
        jumlahDonatur = view.findViewById(R.id.jumlahDonatur);
        sort = view.findViewById(R.id.sort);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        id_konten = kontenMessage.getId();

        sort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.sort_menu);
            popup.show();
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        displayData();
        Log.d(TAG, "Fragment on resume, displayData();");
    }

    private void displayData() {
        Retrofit retrofit = NetworkClient.getApiClient();
        DonaturClient donaturClient = retrofit.create(DonaturClient.class);

        Call<DonaturResponse> call = donaturClient.getDonatur(id_konten);

        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(@NonNull Call<DonaturResponse> call,@NonNull Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();
                    Log.d(TAG, "Muat data");
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
            public void onFailure(@NonNull Call<DonaturResponse> call,@NonNull Throwable t) {
                Log.e(TAG, "Request gagal dan muat lagi");
//              progressBar.setVisibility(View.GONE);
                //displayData();
                //Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KontenMessage event) {
        kontenMessage = event.konten;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.terbanyak:
                sortTerbanyak();
                sort.setText(getResources().getString(R.string.terbanyak));
                return true;
            case R.id.terbaru:
                sortTerbaru();
                sort.setText(getResources().getString(R.string.terbaru));
                return true;
            default:
                return false;
        }
    }

    private void sortTerbanyak(){
        Collections.sort(donaturList, new Comparator<Donatur>() {
            @Override
            public int compare(Donatur d1, Donatur d2) {
                return d2.getJumlah().compareTo(d1.getJumlah());
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void sortTerbaru(){
        displayData();
    }
}
