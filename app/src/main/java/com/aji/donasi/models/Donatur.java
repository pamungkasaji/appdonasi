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

    public void setIdKonten(Integer idKonten) {
        this.idKonten = idKonten;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getIsAnonim() {
        return isAnonim;
    }

    public void setIsAnonim(Integer isAnonim) {
        this.isAnonim = isAnonim;
    }

    public Integer getIsDiterima() {
        return isDiterima;
    }

    public void setIsDiterima(Integer isDiterima) {
        this.isDiterima = isDiterima;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Konten getKonten() {
        return konten;
    }

    public void setKonten(Konten konten) {
        this.konten = konten;
    }
}
