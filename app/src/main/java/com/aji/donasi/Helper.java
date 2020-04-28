package com.aji.donasi;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static final String IMAGE_URL_KONTEN = "http://donasi.pamungkasaji.com/images/konten/";
    public static final String IMAGE_URL_DONATUR = "http://donasi.pamungkasaji.com/images/donatur/";
    public static final String IMAGE_URL_PERKEMBANGAN = "http://donasi.pamungkasaji.com/images/perkembangan/";
    public static final String VERIFIKASI = "verifikasi";
    public static final String SELESAI = "selesai";
    public static final String TUNGGU = "tunggu";
    public static final String DITOLAK = "ditolak";
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

    public static boolean notEmpty(TextInputLayout til, String field) {

        String text = til.getEditText().getText().toString().trim();

        // length 0 means there is no text
        if (text.length() == 0) {
            til.setError("isi " + field);
            return false;
        } else {
            til.setError(null);
        }

        return true;
    }

    public static boolean validasiKarakter(TextInputLayout til, int min, int maks, String field) {

        String text = til.getEditText().getText().toString().trim();

        if(!text.isEmpty()){
            if (text.length() < min) {
                til.setError(field + " minimal " + min + " karakter " );
                return false;
            } else if (text.length() > maks){
                til.setError(field + " maksimal " + maks + " karakter ");
                return false;
            }

            til.setError(null);

        } else {
            return false;
        }

        return true;
    }

    public static boolean minKarakter(TextInputLayout til, int min, String field) {

        String text = til.getEditText().getText().toString().trim();

        if(!text.isEmpty()){
            if (text.length() < min) {
                til.setError(field + " minimal " + min + " karakter " );
                return false;
            }

            til.setError(null);

        } else {
            return false;
        }

        return true;
    }

    public static boolean maksKarakter(TextInputLayout til, int maks, String field) {

        String text = til.getEditText().getText().toString().trim();

        if(!text.isEmpty()){
            if (text.length() > maks) {
                til.setError(field + " maksimal " + maks + " karakter " );
                return false;
            }

            til.setError(null);

        } else {
            return false;
        }

        return true;
    }

    public static void showProgress(final ProgressBar pb, final Activity activity){
        pb.setVisibility(View.VISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void hideProgress(final ProgressBar pb, final Activity activity){
        pb.setVisibility(View.GONE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    public static void warningDialogFinish(final FragmentActivity activity, String title,
                                     String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_error_outline_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", v -> {activity.finish();})
                .show();
    }
}
