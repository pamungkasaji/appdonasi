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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

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

import androidx.core.content.ContextCompat;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageKtp;
    private TextInputLayout editTextNamaLengkap, editTextAlamat, editTextNoktp, editTextNoHp, editTextUsername, editTextPassword, editTextConfirmPassword;
    private ProgressBar progressBar;
    private String filePath;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing views
        Button buttonChoose = findViewById(R.id.buttonChoose);
        Button buttonRegis = findViewById(R.id.buttonRegis);
        imageKtp = findViewById(R.id.imageKtp);

        editTextNamaLengkap = findViewById(R.id.editTextNamaLengkap);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        editTextNoHp = findViewById(R.id.editTextNoHp);
        editTextNoktp = findViewById(R.id.editTextNoktp);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        progressBar = findViewById(R.id.progBar);
        progressBar.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Registrasi");
        }

        //final PermissionManager permissionManager = new PermissionManager();

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonRegis.setOnClickListener((View v) -> {
            uploadMultipart();
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void uploadMultipart() {
        //getting name for the image
        String tnamalengkap = editTextNamaLengkap.getEditText().getText().toString().trim();
        String talamat = editTextAlamat.getEditText().getText().toString().trim();
        String tnohp = editTextNoHp.getEditText().getText().toString().trim();
        String tnomorktp = editTextNoktp.getEditText().getText().toString().trim();
        String tusername = editTextUsername.getEditText().getText().toString().trim();
        String tpassword = editTextPassword.getEditText().getText().toString().trim();
        String tconfirmpass = editTextConfirmPassword.getEditText().getText().toString().trim();

        if (tnamalengkap.isEmpty()) {
            editTextNamaLengkap.setError("Isi kolom nama lengkap");
            editTextNamaLengkap.requestFocus();
            return;
        }else {
            editTextNamaLengkap.setError(null);
        }

        if (talamat.isEmpty()) {
            editTextAlamat.setError("Isi kolom alamat");
            editTextAlamat.requestFocus();
            return;
        }else {
            editTextAlamat.setError(null);
        }

        if (tnohp.isEmpty()) {
            editTextNoHp.setError("Isi kolom No HP");
            editTextNoHp.requestFocus();
            return;
        }else {
            editTextNoHp.setError(null);
        }

        if (tusername.isEmpty()) {
            editTextUsername.setError("Isi kolom username");
            editTextUsername.requestFocus();
            return;
        }else {
            editTextUsername.setError(null);
        }

        if (tpassword.isEmpty()) {
            editTextPassword.setError("Isi kolom password");
            editTextPassword.requestFocus();
            return;
        }else {
            editTextPassword.setError(null);
        }

        if (tconfirmpass.isEmpty()) {
            editTextConfirmPassword.setError("Isi kolom password lagi");
            editTextConfirmPassword.requestFocus();
            return;
        }else {
            editTextConfirmPassword.setError(null);
        }

        if (!tpassword.equals(tconfirmpass)) {
            editTextConfirmPassword.setError("Password harus sama");
            editTextConfirmPassword.requestFocus();
            return;
        }else {
            editTextConfirmPassword.setError(null);
        }

        if (tnomorktp.isEmpty()) {
            editTextNoktp.setError("Isi kolom No KTP");
            editTextNoktp.requestFocus();
            return;
        }else {
            editTextNoktp.setError(null);
        }

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("fotoktp", file.getName(), fileReqBody);

        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), tusername);
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), tpassword);
        RequestBody nohp = RequestBody.create(MediaType.parse("multipart/form-data"), tnohp);
        RequestBody alamat = RequestBody.create(MediaType.parse("multipart/form-data"), talamat);
        RequestBody namalengkap = RequestBody.create(MediaType.parse("multipart/form-data"), tnamalengkap);
        RequestBody nomorktp = RequestBody.create(MediaType.parse("multipart/form-data"), tnomorktp);
        //
        Call<DefaultResponse> call = api.createUser(pic, username, password, nohp, namalengkap, nomorktp, alamat);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialogFinish(RegisterActivity.this, "Tunggu Verifikasi", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null");
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        Helper.warningDialog(RegisterActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(RegisterActivity.this, "Kesalahan", "Registrasi gagal");
            }
        });
    }

    protected void captureImage(){
        Intent i = new Intent(this, ImageSelectActivity.class);
        i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
        i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
        i.putExtra(ImageSelectActivity.FLAG_GALLERY, false);
        startActivityForResult(i, 1213);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            imageKtp.setImageBitmap(selectedImage);
        }
    }
}
