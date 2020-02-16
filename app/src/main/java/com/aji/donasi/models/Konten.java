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
    @SerializedName("id_kategori")
    @Expose
    private Integer idKategori;
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
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Konten(Integer id, Integer idUser, Integer idKategori, String judul, String deskripsi, String gambar, Integer target, Integer terkumpul, Integer lamaDonasi, String nomorrekening, String createdAt, String updatedAt) {
        this.id = id;
        this.idUser = idUser;
        this.idKategori = idKategori;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.target = target;
        this.terkumpul = terkumpul;
        this.lamaDonasi = lamaDonasi;
        this.nomorrekening = nomorrekening;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(Integer idKategori) {
        this.idKategori = idKategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getTerkumpul() {
        return terkumpul;
    }

    public void setTerkumpul(Integer terkumpul) {
        this.terkumpul = terkumpul;
    }

    public Integer getLamaDonasi() {
        return lamaDonasi;
    }

    public void setLamaDonasi(Integer lamaDonasi) {
        this.lamaDonasi = lamaDonasi;
    }

    public String getNomorrekening() {
        return nomorrekening;
    }

    public void setNomorrekening(String nomorrekening) {
        this.nomorrekening = nomorrekening;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}