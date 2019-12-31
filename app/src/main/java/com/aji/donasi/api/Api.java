package com.aji.donasi.api;

import com.aji.donasi.model.KontenResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("konten")
    Call<KontenResponse> getKonten();
}
