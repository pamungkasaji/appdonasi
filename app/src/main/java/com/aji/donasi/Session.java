package com.aji.donasi;

import android.content.Context;
import android.content.SharedPreferences;

import com.aji.donasi.models.User;

public class Session {

    private static final String SHARED_PREF_NAME = "datauser";
    private static final String SHARED_PREF_TOKEN = "takon";

    private static Session mInstance;
    private Context mCtx;

    private Session(Context mCtx) {
        this.mCtx = mCtx;
    }


    public static synchronized Session getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new Session(mCtx);
        }
        return mInstance;
    }


    public void saveUser(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("namalengkap", user.getNamalengkap());

        editor.apply();

    }

    public void saveToken(String token) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", token);

        editor.apply();

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("namalengkap", null)
        );
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences sharedPreferencesToken = mCtx.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorToken = sharedPreferencesToken.edit();
        editorToken.clear();
        editorToken.apply();
    }

}
