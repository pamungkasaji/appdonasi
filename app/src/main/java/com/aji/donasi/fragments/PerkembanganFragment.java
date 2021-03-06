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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.IsUserMessage;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.TambahPerkembanganActivity;
import com.aji.donasi.adapters.PerkembanganAdapter;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.api.PerkembanganClient;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.Perkembangan;
import com.aji.donasi.models.PerkembanganResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerkembanganFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private RecyclerView recyclerView;
    private PerkembanganAdapter adapter;
    private ArrayList<Perkembangan> perkembanganList;
    private ProgressBar progressBar;
    private Button tambah;
    private String token;
    private TextView sort_perkembangan;

    //Eventbus
    private Konten kontenMessage;
    private boolean is_user;

    private static final String TAG = "PerkembanganFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perkembangan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        progressBar = view.findViewById(R.id.progBar);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sort_perkembangan = view.findViewById(R.id.sort_perkembangan);

        token = Session.getInstance(getActivity()).getToken();

        tambah = view.findViewById(R.id.tambah);
        tambah.setVisibility(View.GONE);

        if(is_user){
            tambah.setVisibility(View.VISIBLE);
            Log.d(TAG, "Is user iya");
        }

        tambah.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), TambahPerkembanganActivity.class);
            startActivity(intent);
        });

        sort_perkembangan.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.sort_perkembangan);
            popup.show();
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        displayData();
        Log.d(TAG, "Fragment on resume, displayData()");
    }

    private void displayData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getApiClient();
        PerkembanganClient perkembanganClient = retrofit.create(PerkembanganClient.class);

        Call<PerkembanganResponse> call = perkembanganClient.getPerkembangan(kontenMessage.getLinks().getPerkembangan());

        call.enqueue(new Callback<PerkembanganResponse>() {
            @Override
            public void onResponse(Call<PerkembanganResponse> call, Response<PerkembanganResponse> response) {

                if (response.body() != null) {
                    PerkembanganResponse perkembanganResponse = response.body();
                    perkembanganList = (ArrayList<Perkembangan>) perkembanganResponse.getData();
                    if (perkembanganList.isEmpty() && !is_user){
                        Toast.makeText(getActivity(), "Belum ada perkembangan dari penggalang dana", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Belum ada perkembangan dari penggalang dana");
                    }
                    adapter = new PerkembanganAdapter(getActivity(), perkembanganList);
                    recyclerView.setAdapter(adapter);
                    Log.d(TAG, "Muat ulang");
                } else {
                    Log.w(TAG, "Body kosong");
                    Toast.makeText(getActivity(), "Daftar perkembangan tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PerkembanganResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal dan muat lagi");
                //progressBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), R.string.periksa_koneksi, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KontenMessage event) {
        kontenMessage = event.konten;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IsUserMessage event) {
        is_user = event.isUser;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.semua:
                displayData();
                sort_perkembangan.setText(getResources().getString(R.string.semua));
                return true;
            case R.id.penggunaan_dana:
                filterPenggunaan();
                sort_perkembangan.setText(getResources().getString(R.string.penggunaan_dana));
                return true;
            case R.id.terbaru:
                filterBerita();
                sort_perkembangan.setText(getResources().getString(R.string.terbaru));
                return true;
            default:
                return false;
        }
    }

    private void filterPenggunaan(){
        ArrayList<Perkembangan> penggunaanList = new ArrayList<>() ;

        for (int i = 0 ; i<perkembanganList.size();i++){
            penggunaanList.add(perkembanganList.get(i)) ;
        }

        Iterator<Perkembangan> perkembanganIterator = penggunaanList.iterator();
        while (perkembanganIterator.hasNext()) {
            Perkembangan p = perkembanganIterator.next();
            if (p.getPenggunaanDana() == null) {
                perkembanganIterator.remove();
            }
        }

        adapter = new PerkembanganAdapter(getActivity(), penggunaanList);
        recyclerView.setAdapter(adapter);
    }

    private void filterBerita(){
        ArrayList<Perkembangan> infoList = new ArrayList<>() ;

        for (int i = 0 ; i<perkembanganList.size();i++){
            infoList.add(perkembanganList.get(i)) ;
        }

        Iterator<Perkembangan> perkembanganIterator = infoList.iterator();
        while (perkembanganIterator.hasNext()) {
            Perkembangan p = perkembanganIterator.next();
            if (p.getPenggunaanDana() != null) {
                perkembanganIterator.remove();
            }
        }
        adapter = new PerkembanganAdapter(getActivity(), infoList);
        recyclerView.setAdapter(adapter);
    }
}
