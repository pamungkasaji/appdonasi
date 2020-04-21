package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Perpanjangan {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_konten")
    @Expose
    private Integer idKonten;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("jumlah_hari")
    @Expose
    private Integer jumlahHari;
    @SerializedName("alasan")
    @Expose
    private String alasan;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getJumlahHari() {
        return jumlahHari;
    }

    public String getAlasan() {
        return alasan;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
