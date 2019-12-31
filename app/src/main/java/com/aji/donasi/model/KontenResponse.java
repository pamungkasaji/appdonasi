package com.aji.donasi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KontenResponse {

    private boolean error;

    @SerializedName("data")
    private List <Konten> konten;

    public KontenResponse(boolean error, List<Konten> konten) {
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
