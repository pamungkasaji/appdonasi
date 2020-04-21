package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donatur {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_konten")
    @Expose
    private Integer idKonten;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("is_anonim")
    @Expose
    private Integer isAnonim;
    @SerializedName("is_diterima")
    @Expose
    private Integer isDiterima;
    @SerializedName("jumlah")
    @Expose
    private Integer jumlah;
    @SerializedName("bukti")
    @Expose
    private String bukti;
    @SerializedName("nohp")
    @Expose
    private String nohp;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("konten")
    @Expose
    private Konten konten;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdKonten() {
        return idKonten;
    }

    public String getJudul() {
        return judul;
    }

    public String getNama() {
        return nama;
    }

    public Integer getIsAnonim() {
        return isAnonim;
    }

    public Integer getIsDiterima() {
        return isDiterima;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public String getBukti() {
        return bukti;
    }

    public String getNohp() {
        return nohp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Konten getKonten() {
        return konten;
    }
}
