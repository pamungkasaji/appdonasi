package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KontenResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Konten> data = null;
    @SerializedName("konten")
    @Expose
    private Konten konten;

    public String getMessage() {
        return message;
    }

    public List<Konten> getData() {
        return data;
    }

    public Konten getKonten() {
        return konten;
    }
}
