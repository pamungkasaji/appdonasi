package com.aji.donasi.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.aji.donasi.BuildConfig;
import com.aji.donasi.Helper;
import com.aji.donasi.PermissionManager;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.LoginResponse;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import androidx.core.content.ContextCompat;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonChoose;
    private Button buttonRegis;
    private ImageView imageKtp;
    private EditText editTextNamaLengkap, editTextAlamat, editTextNoKtp, editTextNoHp, editTextUsername, editTextPassword, editTextConfirmPassword;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    public static final String UPLOAD_URL = "http://donasi.tegaronbangkit.id/api/register";

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestStoragePermission();

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonRegis = findViewById(R.id.buttonRegis);
        imageKtp = findViewById(R.id.imageKtp);

        editTextNamaLengkap = findViewById(R.id.editTextNamaLengkap);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        editTextNoKtp = findViewById(R.id.editTextNoKtp);
        editTextNoHp = findViewById(R.id.editTextNoHp);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        //final PermissionManager permissionManager = new PermissionManager();

        buttonChoose.setOnClickListener((View v) -> {
            showFileChooser();

            //captureFromCamera();
        });

        buttonRegis.setOnClickListener((View v) -> {
            uploadMultipart();
        });
    }

    public void uploadMultipart() {
        //getting name for the image
        String tnamalengkap = editTextNamaLengkap.getText().toString().trim();
        String talamat = editTextAlamat.getText().toString().trim();
        String tnoktp = editTextNoKtp.getText().toString().trim();
        String tnohp = editTextNoHp.getText().toString().trim();
        String tusername = editTextUsername.getText().toString().trim();
        String tpassword = editTextPassword.getText().toString().trim();
        String tconfirmpass = editTextConfirmPassword.getText().toString().trim();

        if (tnamalengkap.isEmpty()) {
            editTextNamaLengkap.setError("Isi kolom nama lengkap");
            editTextNamaLengkap.requestFocus();
            return;
        }

        if (talamat.isEmpty()) {
            editTextAlamat.setError("Isi kolom alamat");
            editTextAlamat.requestFocus();
            return;
        }

        if (tnoktp.isEmpty()) {
            editTextNoKtp.setError("Isi kolom no KTP");
            editTextNoKtp.requestFocus();
            return;
        }

        if (tnohp.isEmpty()) {
            editTextNoHp.setError("Isi kolom No HP");
            editTextNoHp.requestFocus();
            return;
        }

        if (tusername.isEmpty()) {
            editTextUsername.setError("Isi kolom username");
            editTextUsername.requestFocus();
            return;
        }

        if (tpassword.isEmpty()) {
            editTextPassword.setError("Isi kolom password");
            editTextPassword.requestFocus();
            return;
        }

        if (tconfirmpass.isEmpty()) {
            editTextConfirmPassword.setError("Isi kolom password lagi");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (!tpassword.equals(tconfirmpass)) {
            editTextConfirmPassword.setError("Password harus sama");
            editTextConfirmPassword.requestFocus();
            return;
        }

        //getting the actual path of the image
        String path = getPath(filePath);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        //Create a file object using file path
        File file = new File(path);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("fotoktp", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        //RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "asdad");

        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), tusername);
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), tpassword);
        RequestBody nohp = RequestBody.create(MediaType.parse("multipart/form-data"), tnohp);
        RequestBody namalengkap = RequestBody.create(MediaType.parse("multipart/form-data"), tnamalengkap);
        RequestBody alamat = RequestBody.create(MediaType.parse("multipart/form-data"), talamat);
        RequestBody nomorktp = RequestBody.create(MediaType.parse("multipart/form-data"), tnoktp);
        //
        Call<DefaultResponse> call = api.createUser(pic, username, password, nohp, namalengkap, alamat, nomorktp);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body() != null){
                    DefaultResponse defaultResponse = response.body();

                    if (defaultResponse.getSuccess()) {

                        Helper.infoDialog(RegisterActivity.this, "Tunggu Verifikasi", defaultResponse.getMessage());

                    } else {
                        Helper.warningDialog(RegisterActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                } else {
                    Helper.warningDialog(RegisterActivity.this, "Kesalahan", "Respon kosong");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Helper.warningDialog(RegisterActivity.this, "Kesalahan", "Terjadi kesalaha saat login");
            }
        });
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageKtp.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
