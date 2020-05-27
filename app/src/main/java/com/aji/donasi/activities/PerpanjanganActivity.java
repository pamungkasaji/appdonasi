package com.aji.donasi.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.api.PerpanjanganClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Konten;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerpanjanganActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Declaring views
    private TextInputLayout editTextAlasan;
    private ProgressBar progressBar;
    private Button submit;

    private String token;

    //spinner
    private String hari;
    private Spinner spinner;

    //EventBus
    private int id_konten;
    private Konten kontenMessage;

    private static final String TAG = "PerpanjanganActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perpanjangan);

        EventBus.getDefault().register(this);

        submit = findViewById(R.id.submit);
        //editTextJumlahHari = findViewById(R.id.editTextJumlahHari);
        editTextAlasan = findViewById(R.id.editTextAlasan);
        progressBar = findViewById(R.id.progBar);

        id_konten = kontenMessage.getId();
        token = Session.getInstance(PerpanjanganActivity.this).getToken();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pengajuan Perpanjangan");
        }

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_perpanjangan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        submit.setOnClickListener((View v) -> {
            if(validasi()){
                submit.setEnabled(false);
                Helper.showProgress(progressBar, PerpanjanganActivity.this);
                postPerpanjangan();
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private boolean validasi(){

        boolean check = true;

        if (!Helper.notEmpty(editTextAlasan, "Alasan")) check = false;

        if (hari.equals("Jumlah hari perpanjangan")) {
            Toast.makeText(this, "Pilih jumlah hari", Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
            return false;
        }else {
            ((TextView)spinner.getSelectedView()).setError(null);
        }

        return check;
    }

    public void postPerpanjangan() {

        String alasan = editTextAlasan.getEditText().getText().toString().trim();

        Retrofit retrofit = NetworkClient.getApiClient();
        PerpanjanganClient perpanjanganClient = retrofit.create(PerpanjanganClient.class);

        Call<DefaultResponse> call = perpanjanganClient.send(id_konten, token, hari, alasan);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, PerpanjanganActivity.this);
                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialogFinish(PerpanjanganActivity.this, "Pemberitahuan", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null");
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        submit.setEnabled(true);
                        Helper.hideProgress(progressBar, PerpanjanganActivity.this);
                        Helper.warningDialogFinish(PerpanjanganActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                submit.setEnabled(true);
                Helper.hideProgress(progressBar, PerpanjanganActivity.this);
                Helper.warningDialog(PerpanjanganActivity.this, "Kesalahan", "Periksa koneksi anda");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hari = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KontenMessage event) {
        kontenMessage = event.konten;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
