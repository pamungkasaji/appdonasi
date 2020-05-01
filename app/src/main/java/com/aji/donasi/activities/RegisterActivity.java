package com.aji.donasi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.api.AuthClient;
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

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageKtp;
    private TextInputLayout editTextNamaLengkap, editTextAlamat, editTextNoktp, editTextNoHp, editTextUsername, editTextPassword, editTextConfirmPassword;
    private ProgressBar progressBar;
    private String filePath = "";
    private Button buttonChoose, buttonRegis;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonRegis = findViewById(R.id.buttonRegis);
        imageKtp = findViewById(R.id.imageKtp);

        editTextNamaLengkap = findViewById(R.id.editTextNamaLengkap);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        editTextNoHp = findViewById(R.id.editTextNoHp);
        editTextNoktp = findViewById(R.id.editTextNoktp);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        progressBar = findViewById(R.id.progBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Registrasi");
        }

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonRegis.setOnClickListener((View v) -> {
            if(validasi()){
                buttonRegis.setEnabled(false);
                Helper.showProgress(progressBar, RegisterActivity.this);
                uploadRegistrasi();
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private boolean validasi(){

        boolean check = true;

        if (!Helper.notEmpty(editTextNamaLengkap, "nama lengkap")) check = false;
        if (!Helper.notEmpty(editTextAlamat, "alamat")) check = false;
        if (!Helper.notEmpty(editTextNoHp,"No HP")) check = false;
        if (!Helper.notEmpty(editTextNoktp,"No KTP")) check = false;
        if (!Helper.notEmpty(editTextUsername,"Username")) check = false;
        if (!Helper.notEmpty(editTextPassword,"Password")) check = false;
        if (!Helper.notEmpty(editTextConfirmPassword,"Konfirmasi Password")) check = false;

        if (!Helper.minKarakter(editTextPassword, 6, "Password")) check = false;

        if (!Helper.validasiKarakter(editTextNoHp, 6, 16, "No HP")) check = false;
        if (!Helper.validasiKarakter(editTextUsername, 6, 22, "Username")) check = false;

        String tpassword = editTextPassword.getEditText().getText().toString().trim();
        String noktp = editTextNoktp.getEditText().getText().toString().trim();
        String tconfirmpass = editTextConfirmPassword.getEditText().getText().toString().trim();

        if (noktp.length() != 16) {
            editTextNoktp.setError("No KTP harus tepat 16 digit");
            check = false;
        }else {
            editTextNoktp.setError(null);
        }

        if (!tconfirmpass.isEmpty()){
            if (!tconfirmpass.equals(tpassword)) {
                editTextConfirmPassword.setError("Password harus sama");
                check = false;
            }else {
                editTextConfirmPassword.setError(null);
            }
        }

        if(filePath.equals("")){
            Toast.makeText(this, "Upload gambar", Toast.LENGTH_SHORT).show();
            buttonChoose.setError("Upload foto KTP");
            check = false;
        }else {
            buttonChoose.setError(null);
        }

        return check;
    }

    public void uploadRegistrasi() {

        String tnamalengkap = editTextNamaLengkap.getEditText().getText().toString().trim();
        String talamat = editTextAlamat.getEditText().getText().toString().trim();
        String tnohp = editTextNoHp.getEditText().getText().toString().trim();
        String tnomorktp = editTextNoktp.getEditText().getText().toString().trim();
        String tusername = editTextUsername.getEditText().getText().toString().trim();
        String tpassword = editTextPassword.getEditText().getText().toString().trim();

        Retrofit retrofit = NetworkClient.getApiClient();
        AuthClient authClient = retrofit.create(AuthClient.class);
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
        Call<DefaultResponse> call = authClient.createUser(pic, username, password, nohp, namalengkap, nomorktp, alamat);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, RegisterActivity.this);
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
                        buttonRegis.setEnabled(true);
                        Helper.hideProgress(progressBar, RegisterActivity.this);
                        Helper.warningDialog(RegisterActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "request gagal");
                buttonRegis.setEnabled(true);
                Helper.hideProgress(progressBar, RegisterActivity.this);
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
        buttonChoose.setError(null);
    }
}
