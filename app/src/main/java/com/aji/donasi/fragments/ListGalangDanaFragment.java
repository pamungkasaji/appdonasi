package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.BuatKontenActivity;
import com.aji.donasi.activities.DetailActivity;
import com.aji.donasi.adapters.KontenAdapter;
import com.aji.donasi.api.KontenClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.aji.donasi.Helper.DITOLAK;
import static com.aji.donasi.Helper.VERIFIKASI;

public class ListGalangDanaFragment extends Fragment implements KontenAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private KontenAdapter adapter;
    //private List<Konten> kontenList;
    private ArrayList<Konten> kontenList;

    private static final String TAG = "BuatFragemnt: ListGalangDanaFragment";

    ProgressBar progressBar;

    private static final String EXTRA_IDKONTEN = "idkonten";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listgalangdana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonBuat = view.findViewById(R.id.buat);

        recyclerView = view.findViewById(R.id.recyclerKontenUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progBar);

        kontenList = new ArrayList<>();

        listKontenUser();

        buttonBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuatKontenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        listKontenUser();
        Log.d(TAG, "Fragment on resume, listKontenUser();");
    }

    private void listKontenUser() {
        Retrofit retrofit = NetworkClient.getApiClient();
        KontenClient kontenClient = retrofit.create(KontenClient.class);

        String token = Session.getInstance(getActivity()).getToken();

        Call<KontenResponse> call = kontenClient.getKontenUser(token);

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
                    //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
                }

                adapter.setOnItemClickListener(ListGalangDanaFragment.this);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar konten penggalangan dana tidak bisa ditampilkan");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        Konten clickedItem = kontenList.get(position);

        if(clickedItem.getStatus().equals(VERIFIKASI)) {
            Helper.warningDialog(getActivity(), "Pemberitahuan", "Tunggu verifikasi admin");
        } else if (clickedItem.getStatus().equals(DITOLAK)){
            Helper.warningDialog(getActivity(), "Pemberitahuan", "Verifikasi anda ditolak");
        }
        else {
            EventBus.getDefault().postSticky(new KontenMessage(clickedItem));
            startActivity(detailIntent);
        }
    }
}