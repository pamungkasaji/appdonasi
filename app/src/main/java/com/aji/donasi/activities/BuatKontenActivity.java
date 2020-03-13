package com.aji.donasi.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuatKontenActivity extends AppCompatActivity {

    //Declaring views
    private ImageView gambar;
    private TextInputLayout editTextJudul, editTextDeskripsi, editTextTarget, editTextLamaDonasi, editTextNoRek;
    private static final String TAG = "Buat Konten";
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buatkonten);

        //Requesting storage permission

        //Initializing views
        Button buttonChoose = findViewById(R.id.buttonChoose);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        editTextJudul = findViewById(R.id.editTextJudul);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        editTextTarget = findViewById(R.id.editTextTarget);
        editTextLamaDonasi = findViewById(R.id.editTextLamaDonasi);
        editTextNoRek = findViewById(R.id.editTextNoRek);

        buttonChoose.setOnClickListener(v -> captureImage());

        buttonUpload.setOnClickListener((View v) -> {
            uploadMultipart();
        });
    }

    private void uploadMultipart() {

        String tjudul = editTextJudul.getEditText().getText().toString().trim();
        String tdeskripsi = editTextDeskripsi.getEditText().getText().toString().trim();
        String ttarget = editTextTarget.getEditText().getText().toString().trim();
        String tlamadonasi = editTextLamaDonasi.getEditText().getText().toString().trim();
        String tnorek = editTextNoRek.getEditText().getText().toString().trim();

        if (tjudul.isEmpty()) {
            editTextJudul.setError("Isi kolom judul");
            editTextJudul.requestFocus();
            return;
        }

        if (tdeskripsi.isEmpty()) {
            editTextDeskripsi.setError("Isi kolom deskripsi");
            editTextDeskripsi.requestFocus();
            return;
        }

        if (ttarget.isEmpty()) {
            editTextTarget.setError("Isi kolom target");
            editTextTarget.requestFocus();
            return;
        }

        if (tlamadonasi.isEmpty()) {
            editTextLamaDonasi.setError("Isi kolom lamadonasi");
            editTextLamaDonasi.requestFocus();
            return;
        }

        if (tnorek.isEmpty()) {
            editTextNoRek.setError("Isi kolom norek");
            editTextNoRek.requestFocus();
            return;
        }

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

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
        RequestBody lama_donasi = RequestBody.create(MediaType.parse("multipart/form-data"), tlamadonasi);
        RequestBody nomorrekening = RequestBody.create(MediaType.parse("multipart/form-data"), tnorek);

        Call<DefaultResponse> call = api.createKonten(token, pic, judul, deskripsi, target, lama_donasi, nomorrekening);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();

                    if (defaultResponse.isSuccess()) {
                        Log.i(TAG, "Berhasil create");
                        Helper.infoDialog(BuatKontenActivity.this, "Tunggu Verifikasi", defaultResponse.getMessage());
                    } else {
                        Log.w(TAG, "Gagal Create");
                        Helper.warningDialog(BuatKontenActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                } else {
                    Log.w(TAG, "Body kosong");
                    Helper.warningDialog(BuatKontenActivity.this, "Kesalahan", "Respon kosong");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
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
    }

}
