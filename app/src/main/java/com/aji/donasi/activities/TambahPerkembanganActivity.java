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
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.api.PerkembanganClient;
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

public class TambahPerkembanganActivity extends AppCompatActivity {

    //Declaring views
    private ImageView gambar;
    private TextInputLayout editTextJudul, editTextDeskripsi, editTextPenggunaan;
    private TextView keterangan;
    private static final String TAG = "TambahPerkembangan";
    private String filePath = "";
    private ProgressBar progressBar;
    private Call<DefaultResponse> call;
    private Button buttonChoose, buttonUpload;
    private String token;

    //EventBus
    private int id_konten;
    private Konten kontenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahperkembangan);

        EventBus.getDefault().register(this);

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);
        editTextJudul = findViewById(R.id.editTextJudul);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        editTextPenggunaan = findViewById(R.id.editTextPenggunaan);
        keterangan = findViewById(R.id.keterangan);
        progressBar = findViewById(R.id.progBar);

        id_konten = kontenMessage.getId();
        token = Session.getInstance(TambahPerkembanganActivity.this).getToken();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tambah Perkembangan");
        }

        buttonChoose.setOnClickListener((View v) -> {
            captureImage();
        });

        buttonUpload.setOnClickListener((View v) -> {
            if(validasi()){
                buttonUpload.setEnabled(false);
                Helper.showProgress(progressBar, TambahPerkembanganActivity.this);
                postPerkembangan();
            }
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_info:
                if (checked)
                    keterangan.setText(getResources().getString(R.string.menambahkan_berita));
                    editTextJudul.setHint("Judul");
                    editTextDeskripsi.setHint("Deskripsi");
                    editTextJudul.setVisibility(View.VISIBLE);
                    editTextPenggunaan.setVisibility(View.GONE);
                break;
            case R.id.radio_penggunaan_dana:
                if (checked)
                    keterangan.setText(getResources().getString(R.string.menambahkan_penggunaan));
                    editTextDeskripsi.setHint("Rencana penggunaan dana");
                    editTextJudul.setVisibility(View.GONE);
                    editTextPenggunaan.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean validasi(){

        boolean check = true;

        if(editTextPenggunaan.getVisibility() == View.GONE) {
            if (!Helper.notEmpty(editTextJudul, "Judul")) check = false;
            if (!Helper.notEmpty(editTextDeskripsi, "Deskripsi")) check = false;
        } else {
            if (!Helper.notEmpty(editTextPenggunaan, "Jumlah Penggunaan Dana")) check = false;
            if (!Helper.notEmpty(editTextDeskripsi, "Rencana Penggunaan Dana")) check = false;
        }

        return check;
    }

    public void postPerkembangan() {

        String tjudul = editTextJudul.getEditText().getText().toString();
        String tpenggunaan = editTextPenggunaan.getEditText().getText().toString();
        String tdeskripsi = editTextDeskripsi.getEditText().getText().toString();

        Retrofit retrofit = NetworkClient.getApiClient();
        PerkembanganClient perkembanganClient = retrofit.create(PerkembanganClient.class);

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);

        Map<String, RequestBody> params = new HashMap<>();

        if(editTextPenggunaan.getVisibility() == View.GONE){
            params.put("judul", RequestBody.create(tjudul, MediaType.parse("multipart/form-data")));
            params.put("deskripsi", RequestBody.create(tdeskripsi, MediaType.parse("multipart/form-data")));
        } else {
            params.put("penggunaan_dana", RequestBody.create(tpenggunaan, MediaType.parse("multipart/form-data")));
            params.put("deskripsi", RequestBody.create(tdeskripsi, MediaType.parse("multipart/form-data")));
        }

        Call<DefaultResponse> call = perkembanganClient.createPerkembanganImage(id_konten, token, pic, params);

        if(filePath.equals("")){
            call = perkembanganClient.createPerkembangan(id_konten, token, params);
        }

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                Helper.hideProgress(progressBar, TambahPerkembanganActivity.this);
                if (response.isSuccessful() && response.body()!= null) {
                    Log.d(TAG, "respon sukses body not null");
                    DefaultResponse defaultResponse = response.body();
                    Helper.infoDialogFinish(TambahPerkembanganActivity.this, "Pemberitahuan", defaultResponse.getMessage());
                }
                else {
                    if (response.errorBody() != null) {
                        Log.d(TAG, "respon sukses errorBody not null");
                        Log.e(TAG, response.errorBody().toString());
                        Gson gson = new Gson();
                        DefaultResponse defaultResponse = gson.fromJson(response.errorBody().charStream(), DefaultResponse.class);
                        buttonUpload.setEnabled(true);
                        Helper.hideProgress(progressBar, TambahPerkembanganActivity.this);
                        Helper.warningDialog(TambahPerkembanganActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call,@NonNull Throwable t) {
                Log.e(TAG, "Request gagal");
                buttonUpload.setEnabled(true);
                Helper.hideProgress(progressBar, TambahPerkembanganActivity.this);
                Helper.warningDialog(TambahPerkembanganActivity.this, "Kesalahan", "Pengajuan penggalangan dana gagal");
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
