package com.aji.donasi.api;

import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.LoginResponse;
import com.aji.donasi.models.PerkembanganResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface KontenClient {

    @GET("konten")
    Call<KontenResponse> getKonten();

    @Multipart
    @POST("konten")
    Call<DefaultResponse> createKonten(
            @Header("Authorization") String token,
            @PartMap Map<String, RequestBody> konten,
            @Part MultipartBody.Part partimage
    );

    @GET("konten/judul/{keyword}")
    Call<KontenResponse> searchKonten(@Path("keyword") String keyword);

    @GET("user/me/konten")
    Call<KontenResponse> getKontenUser(
            @Header("Authorization") String token
    );

    @GET("user/me/konten/{id}")
    Call<KontenResponse> isUser(
            @Path("id") int id,
            @Header("Authorization") String token
    );
}
