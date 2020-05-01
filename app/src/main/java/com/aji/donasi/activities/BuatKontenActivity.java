package com.aji.donasi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.ClientApi;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuatKontenActivity extends AppCompatActivity{

    //Declaring views
    private ImageView gambar;
    private TextInputLayout editTextJudul, editTextDeskripsi, editTextTarget, editTextNoRek, editTextBank;
    private static final String TAG = "BuatKontenActivity";
    private String filePath = "";

    private String tlama_donasi, tbank;
    private Spinner spinner_hari, spinner_bank;

    private Button buttonChoose, buttonUpload;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buatkonten);

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        editTextJudul = findViewById(R.id.editTextJudul);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        editTextTarget = findViewById(R.id.editTextTarget);
        editTextNoRek = findViewById(R.id.editTextNoRek);
        editTextBank = findViewById(R.id.editTextBank);
        editTextBank.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Buat Konten Penggalangan Dana");
        }

        spinner_hari = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_lama_donasi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hari.setAdapter(adapter);

        spinner_bank = findViewById(R.id.spinner_bank);
        ArrayAdapter<CharSequence> adapter_bank = ArrayAdapter.createFromResource(this,
                R.array.spinner_bank, android.R.layout.simple_spinner_item);
        adapter_bank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bank.setAdapter(adapter_bank);

        buttonChoose.setOnClickListener(v -> captureImage());

        buttonUpload.setOnClickListener((View v) -> {
            if(validasi()){
                buttonUpload.setEnabled(false);
                Helper.showProgress(progressBar, BuatKontenActivity.this);
                uploadKonten();
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        spinner_hari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tlama_donasi = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("Lainnya")) {
                    editTextBank.setVisibility(View.VISIBLE);
                } else {
                    tbank = adapterView.getItemAtPosition(i).toString();
                    editTextBank.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validasi(){

        boolean check = true;

        if (!Helper.notEmpty(editTextJudul, "Judul")) check = false;
        if (!Helper.notEmpty(editTextDeskripsi, "Deskripsi")) check = false;
        if (!Helper.notEmpty(editTextTarget, "Target")) check = false;
        if (!Helper.notEmpty(editTextNoRek, "Nomor Rekening")) check = false;

        if (!Helper.validasiKarakter(editTextNoRek, 7, 24, "No HP")) check = false;

        if (tlama_donasi.equals("Jumlah hari penggalangan dana")) {
            Toast.makeText(this, "Pilih jumlah hari", Toast.LENGTH_SHORT).show();
            ((TextView)spinner_hari.getSelectedView()).setError("Pilih hari");
            check = false;
        }else {
            ((TextView)spinner_hari.getSelectedView()).setError(null);
        }

        if (editTextBank.getVisibility() == View.VISIBLE){
            tbank = editTextBank.getEditText().getText().toString().trim();

            if (tbank.isEmpty()) {
                editTextBank.setError("Isi kolom nama bank");
                check = false;
            }else {
                editTextBank.setError(null);
            }
        } else {
            if (tbank.equals("Pilih Nama Bank")) {
                Toast.makeText(this, "Pilih nama bank", Toast.LENGTH_SHORT).show();
                ((TextView)spinner_bank.getSelectedView()).setError("Pilih bank");
                check = false;
            }else {
                ((TextView)spinner_bank.getSelectedView()).setError(null);
            }
        }

        if(filePath.equals("")){
            Toast.makeText(this, "Upload gambar", Toast.LENGTH_SHORT).show();
            buttonChoose.setError("Upload gambar");
            check = false;
        }else {
            buttonChoose.setError(null);
        }

        return check;
    }

    private void uploadKonten() {

        String tjudul = editTextJudul.getEditText().getText().toString().trim();
        String tdeskripsi = editTextDeskripsi.getEditText().getText().toString().trim();
        String ttarget = editTextTarget.getEditText().getText().toString().trim();
        String tnorek = editTextNoRek.getEditText().getText().toString().trim();

        Retrofit retrofit = NetworkClient.getApiClient();
        ClientApi clientApi = retrofit.create(ClientApi.class);

        String token = Session.getInstance(BuatKontenActivity.this).getToken();
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);

        RequestBody judul = RequestBody.create(MediaType.parse("multipart/form-data"), tjudul);
        RequestBody deskripsi = RequestBody.create(MediaType.parse("multipart/form-data"), tdeskripsi);
        RequestBody target = RequestBody.create(MediaType.parse("multipart/form-data"), ttarget);
        RequestBody lama_donasi = RequestBody.create(MediaType.parse("multipart/form-data"), tlama_donasi);
        RequestBody bank = RequestBody.create(MediaType.parse("multipart/form-data"), tbank);
        RequestBody nomorrekening = RequestBody.create(MediaType.parse("multipart/form-data"), tnorek);

        Call<DefaultResponse> call = clientApi.createKonten(token, pic, judul, deskripsi, target, lama_donasi, nomorrekening, bank);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call,@NonNull Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, BuatKontenActivity.this);

                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialogFinish(BuatKontenActivity.this, "Tunggu Verifikasi", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null");
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        buttonUpload.setEnabled(true);
                        Helper.hideProgress(progressBar, BuatKontenActivity.this);
                        Helper.warningDialog(BuatKontenActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                buttonUpload.setEnabled(true);
                Helper.hideProgress(progressBar, BuatKontenActivity.this);
                Helper.warningDialog(BuatKontenActivity.this, "Kesalahan", "Pengajuan penggalangan dana gagal");
            }
        });
    }

    protected void captureImage(){
        Intent i = new Intent(this, ImageSelectActivity.class);
        i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
        i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
        i.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
        startActivityForResult(i, 1213);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            gambar.setImageBitmap(selectedImage);
        }
        buttonChoose.setError(null);
    }
}
