package com.aji.donasi;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.aji.donasi.api.Api;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.models.KontenResponse;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Helper {

    public static final String IMAGE_URL_KONTEN = "http://donasi.pamungkasaji.com/images/konten/";
    public static final String IMAGE_URL_DONATUR = "http://donasi.pamungkasaji.com/images/donatur/";
    public static final String IMAGE_URL_PERKEMBANGAN = "http://donasi.pamungkasaji.com/images/perkembangan/";
    public static final String VERIFIKASI = "verifikasi";
    public static final String SELESAI = "selesai";
    public static final String TUNGGU = "tunggu";
    public static final int TERIMA_DONASI = 1;


    public static String tanggal(String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;

    }

    public static String mataUang(int input){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double)input);
    }

    public static void infoDialog(final FragmentActivity activity, String title,
                                     String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_done_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", v -> {})
                .show();
    }

    public static void infoDialogFinish(final FragmentActivity activity, String title,
                                  String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_done_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", v -> {activity.finish();})
                .show();
    }

    public static void warningDialog(final FragmentActivity activity, String title,
                                      String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_error_outline_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", v -> {})
                .show();
    }
}
