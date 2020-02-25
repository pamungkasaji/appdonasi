package com.aji.donasi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.R;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.PerkembanganResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailKontenFragment extends Fragment implements DetailKontenActivity.FragmentCommunicator {

    private TextView tv_judul, tv_deskripsi, tv_target;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailkonten, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_judul = view.findViewById(R.id.tv_judul);
        tv_deskripsi = view.findViewById(R.id.tv_deskripsi);
        tv_target = view.findViewById(R.id.tv_target);

        if (getActivity() instanceof DetailKontenActivity) {
            ((DetailKontenActivity) getActivity()).fragmentCommunicators.add(this);
        }

    }

    @Override
    public void sendData(Integer data) {
        displayDetail(data);
    }

    private void displayDetail(int id_konten) {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.getKontenDetail(id_konten);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    tv_judul.setText(response.body().getKonten().getJudul());
                    tv_deskripsi.setText(response.body().getKonten().getDeskripsi());
                    tv_target.setText(String.valueOf(response.body().getKonten().getTarget()));

                } else {
                    Toast.makeText(getActivity(), "Detail konten tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
                //if (response.body()!=null) {
                    //WResponse wResponse = response.body();
//
//                    responseText.setText("Temp: "+wResponse.getMain().getTemp() +"\n " +
//                            "Humidity: "+wResponse.getMain().getHumidity()+"\n" +
//                            "Pressure: "+wResponse.getMain().getPressure());
                //}
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
