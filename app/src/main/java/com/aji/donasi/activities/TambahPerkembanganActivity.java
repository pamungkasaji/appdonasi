package com.aji.donasi.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aji.donasi.Helper;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class TambahPerkembanganActivity extends AppCompatActivity {

    //Declaring views
    private ImageView gambar;
    private EditText editTextJudul, editTextDeskripsi;
    private int id_konten;
    private static final String TAG = "TambahPerkembangan";
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahperkembangan);

        EventBus.getDefault().register(this);

        //Initializing views
        Button buttonChoose = findViewById(R.id.buttonChoose);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        editTextJudul = findViewById(R.id.editTextJudul);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonUpload.setOnClickListener((View v) -> {
            uploadMultipart();
        });
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting name for the image
        String tjudul = editTextJudul.getText().toString();
        String tdeskripsi = editTextDeskripsi.getText().toString();

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

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        String token = Session.getInstance(TambahPerkembanganActivity.this).getToken();
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);

        RequestBody judul = RequestBody.create(MediaType.parse("multipart/form-data"), tjudul);
        RequestBody deskripsi = RequestBody.create(MediaType.parse("multipart/form-data"), tdeskripsi);

        Call<DefaultResponse> call = api.createPerkembangan(id_konten, token, pic, judul, deskripsi);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();

                    if (defaultResponse.isSuccess()) {
                        Log.i(TAG, "Berhasil create");
                        Helper.infoDialog(TambahPerkembanganActivity.this, "Penambahan perkembangan sukses", defaultResponse.getMessage());
                    } else {
                        Log.w(TAG, "Gagal Create");
                        Helper.warningDialog(TambahPerkembanganActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                } else {
                    Log.w(TAG, "Body kosong");
                    Helper.warningDialog(TambahPerkembanganActivity.this, "Kesalahan", "Respon kosong");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                Helper.warningDialog(TambahPerkembanganActivity.this, "Kesalahan", "Pengajuan penggalangan dana gagal");
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        id_konten = event.id_konten;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
