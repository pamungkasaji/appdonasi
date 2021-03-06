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

import com.aji.donasi.Helper;
import com.aji.donasi.IsUserMessage;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.PerpanjanganActivity;
import com.aji.donasi.api.KontenClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailFragment extends Fragment {

    private TextView tv_judul, tv_deskripsi, tv_target,  tv_perpanjangan, tv_terkumpul, tv_lama, tv_ket_perpanjangan, tv_penggalang, tv_nohp;
    private ProgressBar progressBar;
    private ProgressBar progressDonasi;
    private LinearLayout layout_perpanjangan, layout_selesai;

    private Button perpanjangan;
    private String token;

    //Eventbus
    private int id_konten;
    private Konten kontenMessage;
    private boolean is_user;

    private static final String TAG = "DetailFragment";

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
        progressDonasi = view.findViewById(R.id.progressDonasi);

        layout_perpanjangan.setVisibility(View.GONE);
        layout_selesai.setVisibility(View.GONE);

        token = Session.getInstance(getActivity()).getToken();

        perpanjangan = view.findViewById(R.id.perpanjangan);
        perpanjangan.setEnabled(false);

        id_konten = kontenMessage.getId();

        //detailData();

        perpanjangan.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), PerpanjanganActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //progressBar.setVisibility(View.VISIBLE);
        detailData();
        Log.d(TAG, "Fragment on resume, detailData();");

        if (Session.getInstance(getActivity()).isLoggedIn() && kontenMessage.getLamaDonasi().equals(0) ) {
            //progressBar.setVisibility(View.VISIBLE);
            initPerpanjangan();
            //initPerpanj();
            Log.d(TAG, "init perpanjangan");
        }
    }

    private void detailData() {
        tv_judul.setText(kontenMessage.getJudul());
        tv_deskripsi.setText(kontenMessage.getDeskripsi());
        tv_lama.setText(String.valueOf(kontenMessage.getLamaDonasi()));
        tv_penggalang.setText(kontenMessage.getUser().getNamalengkap());
        tv_nohp.setText(kontenMessage.getUser().getNohp());

        tv_target.setText(Helper.mataUang(kontenMessage.getTarget()));
        tv_terkumpul.setText(Helper.mataUang(kontenMessage.getTerkumpul()));

        double progress = ((double)kontenMessage.getTerkumpul()/(double)kontenMessage.getTarget())*100;
        progressDonasi.setProgress((int)progress);

        if (kontenMessage.getStatus().equals("selesai") && !is_user){
            layout_selesai.setVisibility(View.VISIBLE);
        }
    }

//    private void initPerpanj(){
//
//        if (kontenMessage.getTerkumpul() < kontenMessage.getTarget()) {
//            Log.d(TAG, "Is user iya, terkumpul lebih kecil");
//            layout_perpanjangan.setVisibility(View.VISIBLE);
//            if(kontenMessage.getPerpanjangan() == null){
//                Log.d(TAG, "Is user iya, perpanjangan empty");
//                perpanjangan.setEnabled(true);
//                tv_ket_perpanjangan.setText(getResources().getString(R.string.bisa_perpanjang));
//            } else {
//                Log.d(TAG, "Is user iya, perpanjangan not empty");
//                tv_perpanjangan.setText(kontenMessage.getPerpanjangan().getStatus());
//                if(kontenMessage.getPerpanjangan().getStatus().equals("verifikasi")){
//                    Log.d(TAG, "Is user iya, perpanjangan not empty, status verifikasi");
//                    tv_ket_perpanjangan.setText(getResources().getString(R.string.verif_perpanjangan));
//                    //perpanjangan.setEnabled(false);
//                } else if(kontenMessage.getPerpanjangan().getStatus().equals("ditolak")){
//                    Log.d(TAG, "Is user iya, perpanjangan not empty, status ditolak");
//                    tv_ket_perpanjangan.setText(getResources().getString(R.string.ditolak_perpanjangan));
//                }
//            }
//
//        } else {
//            Log.d(TAG, "Is user iya, target terpenuhi");
//            tv_ket_perpanjangan.setText(getResources().getString(R.string.tidak_bisa_perpanjang));
//        }
//        //progressBar.setVisibility(View.GONE);
//    }

    private void initPerpanjangan(){
        Retrofit retrofit = NetworkClient.getApiClient();
        KontenClient kontenClient = retrofit.create(KontenClient.class);

        Call<KontenResponse> call = kontenClient.isUser(id_konten, token);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(@NonNull Call<KontenResponse> call,@NonNull Response<KontenResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    layout_selesai.setVisibility(View.GONE);
                    KontenResponse kontenResponse = response.body();

                    if (kontenResponse.getKonten().getTerkumpul() < kontenResponse.getKonten().getTarget()) {
                        Log.d(TAG, "Is user iya, terkumpul lebih kecil");
                        layout_perpanjangan.setVisibility(View.VISIBLE);
                        if(kontenResponse.getKonten().getPerpanjangan() == null){
                            Log.d(TAG, "Is user iya, perpanjangan empty");
                            perpanjangan.setEnabled(true);
                            tv_ket_perpanjangan.setText(getResources().getString(R.string.bisa_perpanjang));
                        } else {
                            Log.d(TAG, "Is user iya, perpanjangan not empty");
                            tv_perpanjangan.setText(kontenResponse.getKonten().getPerpanjangan().getStatus());
                            if(kontenResponse.getKonten().getPerpanjangan().getStatus().equals("verifikasi")){
                                Log.d(TAG, "Is user iya, perpanjangan not empty, status verifikasi");
                                tv_ket_perpanjangan.setText(getResources().getString(R.string.verif_perpanjangan));
                                //perpanjangan.setEnabled(false);
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
                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<KontenResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Init perpanjangan, Request gagal");
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IsUserMessage event) {
        is_user = event.isUser;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
