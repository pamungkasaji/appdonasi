package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonaturResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Donatur> data = null;
    @SerializedName("donatur")
    @Expose
    private Donatur donatur;

    public String getMessage() {
        return message;
    }

    public List<Donatur> getData() {
        return data;
    }

    public Donatur getDonatur() {
        return donatur;
    }

}