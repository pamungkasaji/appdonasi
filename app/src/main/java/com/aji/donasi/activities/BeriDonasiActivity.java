package com.aji.donasi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.api.DonaturClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.Konten;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    private String filePath = "";
    private Button buttonChoose, buttonUpload;
    private ProgressBar progressBar;
    private static final String TAG = "BeriDonasiActivity";

    //EventBus
    private int id_konten;
    private Konten kontenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beridonasi);

        EventBus.getDefault().register(this);

        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        et_nama = findViewById(R.id.et_nama);
        et_jumlah = findViewById(R.id.et_jumlah);
        et_nohp = findViewById(R.id.et_nohp);
        anonim = findViewById(R.id.anonim);
        TextView norek = findViewById(R.id.norek);
        TextView bank = findViewById(R.id.bank);
        TextView atasnama = findViewById(R.id.atasnama);

        progressBar = findViewById(R.id.progBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Beri Donasi");
        }

        id_konten = kontenMessage.getId();
        String nomorrekening = kontenMessage.getNomorrekening();
        String nama_bank = kontenMessage.getBank();
        String atas_nama = kontenMessage.getUser().getNamalengkap();

        norek.setText(nomorrekening);
        bank.setText(nama_bank);
        atasnama.setText(atas_nama);

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonUpload.setOnClickListener((View v) -> {
            if(validasi()){
                buttonUpload.setEnabled(false);
                Helper.showProgress(progressBar, BeriDonasiActivity.this);
                postDonasi();
            }
        });

        anonim.setOnClickListener((View v) -> {
            if (anonim.isChecked()) {
                tis_anonim = "1";
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private boolean validasi() {

        boolean check = true;

        if (!Helper.notEmpty(et_nama, "Nama")) check = false;
        if (!Helper.notEmpty(et_jumlah, "Jumlah")) check = false;
        if (!Helper.notEmpty(et_nohp, "No HP")) check = false;

        if (!Helper.validasiKarakter(et_nohp, 6, 16, "No HP")) check = false;

        if(filePath.equals("")){
            Toast.makeText(this, "Upload gambar", Toast.LENGTH_SHORT).show();
            buttonChoose.setError("Upload gambar");
            check = false;
        }else {
            buttonChoose.setError(null);
        }

        return check;
    }

    public void postDonasi() {

        String tnama = et_nama.getEditText().getText().toString();
        String tjumlah = et_jumlah.getEditText().getText().toString();
        String tnohp = et_nohp.getEditText().getText().toString();

        Retrofit retrofit = NetworkClient.getApiClient();
        DonaturClient donaturClient = retrofit.create(DonaturClient.class);

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("bukti", file.getName(), fileReqBody);

        Map<String, RequestBody> donatur = new HashMap<>();

        donatur.put("nama", RequestBody.create(tnama, MediaType.parse("multipart/form-data")));
        donatur.put("jumlah", RequestBody.create(tjumlah, MediaType.parse("multipart/form-data")));
        donatur.put("nohp", RequestBody.create(tnohp, MediaType.parse("multipart/form-data")));
        donatur.put("is_anonim", RequestBody.create(tis_anonim, MediaType.parse("multipart/form-data")));

        Call<DefaultResponse> call = donaturClient.sendDonation(id_konten, donatur, pic);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, BeriDonasiActivity.this);

                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialogFinish(BeriDonasiActivity.this, "Berhasil", defaultResponse.getMessage());

                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null");
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        buttonUpload.setEnabled(true);
                        Helper.hideProgress(progressBar, BeriDonasiActivity.this);
                        Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                buttonUpload.setEnabled(true);
                Helper.hideProgress(progressBar, BeriDonasiActivity.this);
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
        buttonChoose.setError(null);
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
