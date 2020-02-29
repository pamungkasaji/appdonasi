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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BeriDonasiActivity extends AppCompatActivity{

    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView gambar;
    private EditText et_nama, et_jumlah;
    private CheckBox anonim;
    private String tis_anonim = "0";
    private int id_konten;
    private String nomorrekening;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beridonasi);

        EventBus.getDefault().register(this);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        gambar = findViewById(R.id.gambar);

        et_nama = findViewById(R.id.et_nama);
        et_jumlah = findViewById(R.id.et_jumlah);
        anonim = findViewById(R.id.anonim);
        TextView norek = findViewById(R.id.norek);
        norek.setText(nomorrekening);

        buttonChoose.setOnClickListener((View v) -> {
            showFileChooser();
        });

        buttonUpload.setOnClickListener((View v) -> {
            beriDonasi();
        });

        anonim.setOnClickListener((View v) -> {
            if (anonim.isChecked()) {
                tis_anonim = "1";
            }
        });
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void beriDonasi() {
        //getting name for the image
        String tnama = et_nama.getText().toString();
        String tjumlah = et_jumlah.getText().toString();

        if (tnama.isEmpty()) {
            et_nama.setError("Isi nama lengkap");
            et_nama.requestFocus();
            return;
        }

        if (tjumlah.isEmpty()) {
            et_jumlah.setError("Isi jumlah donasi");
            et_jumlah.requestFocus();
            return;
        }

        //getting the actual path of the image
        String path = getPath(filePath);

        Retrofit retrofit = NetworkClient.getApiClient();
        Api api = retrofit.create(Api.class);

        String token = Session.getInstance(BeriDonasiActivity.this).getToken();
        //Create a file object using file path
        File file = new File(path);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part pic = MultipartBody.Part.createFormData("bukti", file.getName(), fileReqBody);

        RequestBody nama = RequestBody.create(MediaType.parse("multipart/form-data"), tnama);
        RequestBody jumlah = RequestBody.create(MediaType.parse("multipart/form-data"), tjumlah);
        RequestBody is_anonim = RequestBody.create(MediaType.parse("ultipart/form-data"), tis_anonim);

        Call<DefaultResponse> call = api.sendDonation(id_konten, pic, nama, jumlah, is_anonim);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body() != null) {
                    DefaultResponse defaultResponse = response.body();

                    if (defaultResponse.getSuccess()) {

                        Helper.infoDialog(BeriDonasiActivity.this, "Berhasil", defaultResponse.getMessage());
                    } else {
                        Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", defaultResponse.getMessage());
                    }
                } else {
                    Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", "Silahkan coba submit lagi");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Helper.warningDialog(BeriDonasiActivity.this, "Kesalahan", "Pemberian donasia gagal");
            }
        });

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        id_konten = event.id_konten;
        nomorrekening = event.nomorrekening;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
