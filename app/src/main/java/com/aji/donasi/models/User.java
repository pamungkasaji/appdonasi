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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getUsertype() {
        return usertype;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNomorktp() {
        return nomorktp;
    }

    public String getNohp() {
        return nohp;
    }

    public String getFotoktp() {
        return fotoktp;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}