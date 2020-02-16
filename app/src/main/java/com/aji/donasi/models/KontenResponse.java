package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KontenResponse {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("data")
    @Expose
    private List<Konten> konten;

    public KontenResponse(Boolean error, List<Konten> konten) {
        this.error = error;
        this.konten = konten;
    }

    public boolean isError() {
        return error;
    }

    public List<Konten> getKonten() {
        return konten;
    }
}
