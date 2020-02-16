package com.aji.donasi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import com.aji.donasi.R;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuatKontenActivity extends AppCompatActivity implements View.OnClickListener{

    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView gambar;
    private EditText editTextJudul, editTextDeskripsi, editTextTarget, editTextLamaDonasi, editTextNoRek;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    public static final String UPLOAD_URL = "http://donasi.tegaronbangkit.id/api/konten";

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buatkonten);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        editTextJudul = findViewById(R.id.editTextJudul);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        editTextTarget = findViewById(R.id.editTextTarget);
        editTextLamaDonasi = findViewById(R.id.editTextLamaDonasi);
        editTextNoRek = findViewById(R.id.editTextNoRek);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting name for the image
        String tjudul = editTextJudul.getText().toString().trim();
        String tdeskripsi = editTextDeskripsi.getText().toString().trim();
        String ttarget = editTextTarget.getText().toString().trim();
        String tlamadonasi = editTextLamaDonasi.getText().toString().trim();
        String tnorek = editTextNoRek.getText().toString().trim();
        String tid_user = "1";

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

        //getting the actual path of the image
        String path = getPath(filePath);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);
        //Create a file object using file path
        File file = new File(path);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        //RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "asdad");

        RequestBody judul = RequestBody.create(MediaType.parse("multipart/form-data"), tjudul);
        RequestBody deskripsi = RequestBody.create(MediaType.parse("multipart/form-data"), tdeskripsi);
        RequestBody target = RequestBody.create(MediaType.parse("multipart/form-data"), ttarget);
        RequestBody lama_donasi = RequestBody.create(MediaType.parse("multipart/form-data"), tlamadonasi);
        RequestBody nomorrekening = RequestBody.create(MediaType.parse("multipart/form-data"), tnorek);
        RequestBody id_user = RequestBody.create(MediaType.parse("multipart/form-data"), tid_user);
        //

        Call<DefaultResponse> call = api.createKonten(pic, judul, deskripsi, target, lama_donasi, nomorrekening, id_user);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                //Toast.makeText(BuatActivity.this, "berhasil", Toast.LENGTH_LONG).show();

                //if (response.code() == 201) {

                    DefaultResponse dr = response.body();
                    Toast.makeText(BuatKontenActivity.this, dr.getMessage(), Toast.LENGTH_LONG).show();

                //}
//                else if (response.code() == 422) {
//                    Toast.makeText(BuatActivity.this, "Username sudah digunakan", Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(BuatKontenActivity.this,"Registrasi gagal", Toast.LENGTH_LONG).show();
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
                gambar.setImageBitmap(bitmap);

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


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
    }

}
