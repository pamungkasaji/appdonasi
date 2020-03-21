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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.LoginResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

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

public class BeriDonasiActivity extends AppCompatActivity{

    private ImageView gambar;
    private TextInputLayout et_nama, et_jumlah, et_nohp;
    private CheckBox anonim;
    private String tis_anonim = "0";
    private String filePath;
    private ProgressBar progressBar;
    private static final String TAG = "BeriDonasiActivity";

    //EventBus
    private int id_konten;
    private String nomorrekening;
    private Konten kontenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beridonasi);

        EventBus.getDefault().register(this);

        //Initializing views
        Button buttonChoose = findViewById(R.id.buttonChoose);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        et_nama = findViewById(R.id.et_nama);
        et_jumlah = findViewById(R.id.et_jumlah);
        et_nohp = findViewById(R.id.et_nohp);
        anonim = findViewById(R.id.anonim);
        TextView norek = findViewById(R.id.norek);

        progressBar = findViewById(R.id.progBar);
        progressBar.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Beri Donasi");
            getSupportActionBar().setSubtitle(kontenMessage.getJudul());
        }

        id_konten = kontenMessage.getId();
        nomorrekening = kontenMessage.getNomorrekening();

        norek.setText(nomorrekening);

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonUpload.setOnClickListener((View v) -> {
            beriDonasi();
        });

        anonim.setOnClickListener((View v) -> {
            if (anonim.isChecked()) {
                tis_anonim = "1";
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void beriDonasi() {
        //getting name for the image
        String tnama = et_nama.getEditText().getText().toString();
        String tjumlah = et_jumlah.getEditText().getText().toString();
        String tnohp = et_nohp.getEditText().getText().toString();

        if (tnama.isEmpty()) {
            et_nama.setError("Isi nama lengkap");
            et_nama.requestFocus();
            return;
        } else {
            et_nama.setError(null);
        }

        if (tjumlah.isEmpty()) {
            et_jumlah.setError("Isi jumlah donasi");
            et_jumlah.requestFocus();
            return;
        } else {
            et_jumlah.setError(null);
        }

        if (tnohp.isEmpty()) {
            et_nohp.setError("Isi kontak yang bisa dihubungi");
            et_nohp.requestFocus();
            return;
        } else {
            et_nohp.setError(null);
        }

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("bukti", file.getName(), fileReqBody);

        RequestBody nama = RequestBody.create(MediaType.parse("multipart/form-data"), tnama);
        RequestBody jumlah = RequestBody.create(MediaType.parse("multipart/form-data"), tjumlah);
        RequestBody nohp = RequestBody.create(MediaType.parse("multipart/form-data"), tnohp);
        RequestBody is_anonim = RequestBody.create(MediaType.parse("ultipart/form-data"), tis_anonim);

        Call<DefaultResponse> call = api.sendDonation(id_konten, pic, nama, jumlah, nohp, is_anonim);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null")
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialog(BeriDonasiActivity.this, "Berhasil", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null")
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
                Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", "Pemberian donasia gagal");
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KontenMessage event) {
        kontenMessage = event.konten;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
