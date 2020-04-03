package com.aji.donasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.BeriDonasiActivity;
import com.aji.donasi.activities.DetailKontenActivity;
import com.aji.donasi.activities.PerpanjanganActivity;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.PerkembanganResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DetailKontenFragment extends Fragment {

    private TextView tv_judul, tv_deskripsi, tv_target,  tv_perpanjangan, tv_terkumpul, tv_lama, tv_ket_perpanjangan, tv_penggalang, tv_nohp;
    private ProgressBar progressBar;
    private LinearLayout layout_perpanjangan, layout_selesai;

    //private int lama_donasi;
    //private String status_perpanjangan = "";
    private Button perpanjangan;
    private String token;

    //Eventbus
    private int id_konten;
    private Konten kontenMessage;

    //currency
    private Locale localeID;
    private NumberFormat formatRupiah;

    private static final String TAG = "DetailKontenFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailkonten, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        tv_judul = view.findViewById(R.id.tv_judul);
        tv_deskripsi = view.findViewById(R.id.tv_deskripsi);
        tv_target = view.findViewById(R.id.tv_target);
        tv_terkumpul = view.findViewById(R.id.tv_terkumpul);
        tv_lama = view.findViewById(R.id.tv_lama);
        tv_penggalang = view.findViewById(R.id.tv_penggalang);
        tv_nohp = view.findViewById(R.id.tv_nohp);

        tv_ket_perpanjangan = view.findViewById(R.id.tv_ket_perpanjangan);
        tv_perpanjangan = view.findViewById(R.id.tv_perpanjangan);
        layout_perpanjangan = view.findViewById(R.id.layout_perpanjangan);
        layout_selesai = view.findViewById(R.id.layout_selesai);
        layout_perpanjangan.setVisibility(View.GONE);
        layout_selesai.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progBar);

        token = Session.getInstance(getActivity()).getToken();

        perpanjangan = view.findViewById(R.id.perpanjangan);
        perpanjangan.setEnabled(false);

        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        id_konten = kontenMessage.getId();

        displayDetail();
        if (Session.getInstance(getActivity()).isLoggedIn() && kontenMessage.getLamaDonasi().equals(0)) {
            initPerpanjangan();
            Log.d(TAG, "init perpanjangan");
        }
        //progressBar.setVisibility(View.GONE);

        perpanjangan.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), PerpanjanganActivity.class);
            startActivity(intent);
        });
    }

    private void displayDetail() {
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.getKontenDetail(id_konten);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.body() != null) {
                    KontenResponse kontenResponse = response.body();
                    Log.d(TAG, "Muat ulang");
                    tv_judul.setText(kontenResponse.getKonten().getJudul());
                    tv_deskripsi.setText(kontenResponse.getKonten().getDeskripsi());
                    tv_lama.setText(String.valueOf(kontenResponse.getKonten().getLamaDonasi()));
                    tv_penggalang.setText(kontenResponse.getKonten().getUser().getNamalengkap());
                    tv_nohp.setText(kontenResponse.getKonten().getUser().getNohp());
                    tv_target.setText(formatRupiah.format((double)kontenResponse.getKonten().getTarget()));
                    tv_terkumpul.setText(formatRupiah.format((double)kontenResponse.getKonten().getTerkumpul()));
                    //tv_target.setText(String.valueOf(kontenResponse.getKonten().getTarget()));
                    //tv_terkumpul.setText(String.valueOf(kontenResponse.getKonten().getTerkumpul()));

                    if (kontenResponse.getKonten().getStatus().equals("selesai")){
                        layout_selesai.setVisibility(View.VISIBLE);
                    }

                    //status_perpanjangan = kontenResponse.getKonten().getPerpanjangan().getStatus();
                    //lama_donasi = kontenResponse.getKonten().getLamaDonasi();

                    Log.d(TAG, "selesai muat");
                } else {
                    Log.w(TAG, "Body kosong");
                    Toast.makeText(getActivity(), "Detail konten tidak dapat ditampilkan", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
                Log.e(TAG, "Request gagal");
                //progressBar.setVisibility(View.GONE);
                displayDetail();
                //Toast.makeText(getActivity(), "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPerpanjangan(){
        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        Call<KontenResponse> call = api.isUser(id_konten, token);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(@NonNull Call<KontenResponse> call,@NonNull Response<KontenResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    KontenResponse kontenResponse = response.body();

                    if (kontenResponse.getKonten().getTerkumpul() < kontenResponse.getKonten().getTarget()) {
                        Log.d(TAG, "Is user iya, terkumpul lebih besar");
                        if(kontenResponse.getKonten().getPerpanjangan() == null){
                            Log.d(TAG, "Is user iya, perpanjangan empty");
                            perpanjangan.setEnabled(true);
                            tv_ket_perpanjangan.setText(getResources().getString(R.string.bisa_perpanjang));
                            layout_perpanjangan.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "Is user iya, perpanjangan not empty");
                            tv_perpanjangan.setText(kontenResponse.getKonten().getPerpanjangan().getStatus());
                            if(kontenResponse.getKonten().getPerpanjangan().getStatus().equals("verifikasi")){
                                Log.d(TAG, "Is user iya, perpanjangan not empty, status verifikasi");
                                tv_ket_perpanjangan.setText(getResources().getString(R.string.verif_perpanjangan));
                            } else if(kontenResponse.getKonten().getPerpanjangan().getStatus().equals("ditolak")){
                                Log.d(TAG, "Is user iya, perpanjangan not empty, status ditolak");
                                tv_ket_perpanjangan.setText(getResources().getString(R.string.ditolak_perpanjangan));
                            }
                        }

                    } else {
                        Log.d(TAG, "Is user iya, target terpenuhi");
                        tv_ket_perpanjangan.setText(getResources().getString(R.string.tidak_bisa_perpanjang));
                    }
                } else {
                    Log.d(TAG, "Is user bukan");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<KontenResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Request gagal");
                //progressBar.setVisibility(View.GONE);
                //Helper.warningDialog(getActivity(), "Kesalahan", "Periksa koneksi internet anda");
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
}
