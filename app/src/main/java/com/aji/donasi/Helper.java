package com.aji.donasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class Helper {

    public static void infoDialog(final FragmentActivity activity, String title,
                                     String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_done_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Relax", v -> {})
                .show();
    }

    public static void warningDialog(final FragmentActivity activity, String title,
                                      String message) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_error_outline_white_24dp)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Relax", v -> {})
                .show();
    }

}
