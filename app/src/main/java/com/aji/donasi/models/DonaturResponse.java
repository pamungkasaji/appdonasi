package com.aji.donasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonaturResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Donatur> data = null;
    @SerializedName("donatur")
    @Expose
    private Donatur donatur;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Donatur> getData() {
        return data;
    }

    public void setData(List<Donatur> data) {
        this.data = data;
    }

    public Donatur getDonatur() {
        return donatur;
    }

}