package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("usertype")
    @Expose
    private String usertype;
    @SerializedName("namalengkap")
    @Expose
    private String namalengkap;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("nomorktp")
    @Expose
    private String nomorktp;
    @SerializedName("nohp")
    @Expose
    private String nohp;
    @SerializedName("fotoktp")
    @Expose
    private String fotoktp;
    @SerializedName("is_verif")
    @Expose
    private Integer isVerif;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public User(Integer id, String username, String namalengkap) {
        this.id = id;
        this.username = username;
        this.namalengkap = namalengkap;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorktp() {
        return nomorktp;
    }

    public void setNomorktp(String nomorktp) {
        this.nomorktp = nomorktp;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getFotoktp() {
        return fotoktp;
    }

    public void setFotoktp(String fotoktp) {
        this.fotoktp = fotoktp;
    }

    public Integer getIsVerif() {
        return isVerif;
    }

    public void setIsVerif(Integer isVerif) {
        this.isVerif = isVerif;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}