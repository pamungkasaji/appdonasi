package com.aji.donasi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aji.donasi.R;
import com.aji.donasi.adapter.KontenAdapter;
import com.aji.donasi.api.RetrofitClient;
import com.aji.donasi.model.Konten;
import com.aji.donasi.model.KontenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private KontenAdapter adapter;
    private List<Konten> kontenList;

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


        Call<KontenResponse> call = RetrofitClient.getInstance().getApi().getKonten();

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                kontenList = response.body().getKonten();
                adapter = new KontenAdapter(getActivity(), kontenList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {

            }
        });

    }
}
