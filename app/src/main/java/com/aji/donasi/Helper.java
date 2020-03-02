package com.aji.donasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class Helper {

    public static final String IMAGE_URL_KONTEN = "http://donasisosial.xyz/images/konten/";
    public static final String IMAGE_URL_DONATUR = "http://donasisosial.xyz/images/donatur/";
    public static final int TERIMA_DONASI = 1;

    public static void infoDialog(final FragmentActivity activity, String title,
                                     String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_done_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", v -> {})
                .show();
    }

    public static void warningDialog(final FragmentActivity activity, String title,
                                      String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_error_outline_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", v -> {})
                .show();
    }

}
