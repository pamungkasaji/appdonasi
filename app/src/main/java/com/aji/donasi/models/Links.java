package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("donatur")
    @Expose
    private String donatur;
    @SerializedName("perkembangan")
    @Expose
    private String perkembangan;

    public String getDonatur() {
        return donatur;
    }

    public String getPerkembangan() {
        return perkembangan;
    }
}
