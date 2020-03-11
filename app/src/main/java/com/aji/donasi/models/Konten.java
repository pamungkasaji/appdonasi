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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("perpanjangan")
    @Expose
    private Perpanjangan perpanjangan;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Perpanjangan getPerpanjangan() {
        return perpanjangan;
    }

    public void setPerpanjangan(Perpanjangan perpanjangan) {
        this.perpanjangan = perpanjangan;
    }

}