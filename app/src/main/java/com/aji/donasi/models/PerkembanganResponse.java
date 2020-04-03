package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PerkembanganResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Perkembangan> data = null;

    public String getMessage() {
        return message;
    }

    public List<Perkembangan> getData() {
        return data;
    }
}
