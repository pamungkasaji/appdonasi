package com.aji.donasi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.adapters.PerkembanganAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Perkembangan;
import com.aji.donasi.models.PerkembanganResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerkembanganFragment extends Fragment implements DetailKontenActivity.FragmentCommunicator {

    private RecyclerView recyclerView;
    private PerkembanganAdapter adapter;
    private ArrayList<Perkembangan> perkembanganList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perkembangan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            id_konten = bundle.getInt("id", 0);
//        }

        if (getActivity() instanceof DetailKontenActivity) {
            ((DetailKontenActivity) getActivity()).fragmentCommunicators.add(this);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //perkembanganList = new ArrayList<>();
    }

    @Override
    public void sendData(Integer data) {
        //id_konten = data;
        //textview.setText(data);
        displayData(data);
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
                } else {
                    //Helper.warningDialog(getActivity(), "Kesalahan", "Daftar perkembangan tidak dapat ditampilkan");
                    Toast.makeText(getActivity(), "Daftar perkembangan tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }

//                perkembanganList = (ArrayList<Perkembangan>) response.body().getData();
//                adapter = new PerkembanganAdapter(getActivity(), perkembanganList);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PerkembanganResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
