package com.aji.donasi.models;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
