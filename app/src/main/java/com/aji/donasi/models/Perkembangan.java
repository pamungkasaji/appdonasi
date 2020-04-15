package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Perkembangan {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_konten")
    @Expose
    private Integer idKonten;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("pengeluaran")
    @Expose
    private Integer pengeluaran;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public Integer getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(Integer pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
