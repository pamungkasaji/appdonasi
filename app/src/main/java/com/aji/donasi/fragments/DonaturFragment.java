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

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.activities.LoginActivity;
import com.aji.donasi.adapters.DonaturAdapter;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Donatur;
import com.aji.donasi.models.DonaturResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonaturFragment extends Fragment implements DetailKontenActivity.FragmentCommunicator {

    private RecyclerView recyclerView;
    private DonaturAdapter adapter;
    private ArrayList<Donatur> donaturList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donatur, container, false);
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

        //donaturList = new ArrayList<>();
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

        Call<DonaturResponse> call = api.getDonatur(id_konten);

        call.enqueue(new Callback<DonaturResponse>() {
            @Override
            public void onResponse(Call<DonaturResponse> call, Response<DonaturResponse> response) {

                if (response.body() != null) {
                    DonaturResponse donaturResponse = response.body();

                    donaturList = (ArrayList<Donatur>) donaturResponse.getData();
                    adapter = new DonaturAdapter(getActivity(), donaturList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Helper.warningDialog(getActivity(), "Kesalahan", "Daftar donatur tidak dapat ditampilkan");
                }
//                donaturList = (ArrayList<Donatur>) donaturResponse.getData();
//                adapter = new DonaturAdapter(getActivity(), donaturList);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DonaturResponse> call, Throwable t) {
                Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
            }
        });
    }
}
