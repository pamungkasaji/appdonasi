package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Konten {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("target")
    @Expose
    private Integer target;
    @SerializedName("terkumpul")
    @Expose
    private Integer terkumpul;
    @SerializedName("lama_donasi")
    @Expose
    private Integer lamaDonasi;
    @SerializedName("nomorrekening")
    @Expose
    private String nomorrekening;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("perpanjangan")
    @Expose
    private Perpanjangan perpanjangan;

    public Konten(String judul, String deskripsi, Integer target, Integer lamaDonasi, String nomorrekening, String bank) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.target = target;
        this.lamaDonasi = lamaDonasi;
        this.nomorrekening = nomorrekening;
        this.bank = bank;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public Integer getTarget() {
        return target;
    }

    public Integer getTerkumpul() {
        return terkumpul;
    }

    public Integer getLamaDonasi() {
        return lamaDonasi;
    }

    public String getNomorrekening() {
        return nomorrekening;
    }

    public String getBank() {
        return bank;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Links getLinks() {
        return links;
    }

    public User getUser() {
        return user;
    }

    public Perpanjangan getPerpanjangan() {
        return perpanjangan;
    }

}