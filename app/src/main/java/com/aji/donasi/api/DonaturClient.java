package com.aji.donasi.api;

import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.LoginResponse;
import com.aji.donasi.models.PerkembanganResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
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

public interface DonaturClient {

    @GET("konten/{id}/donatur")
    Call<DonaturResponse> getDonatur(@Path("id") int id);

    @Multipart
    @POST("konten/{id}/donatur")
    Call<DefaultResponse> sendDonation(
            @Path("id") int id,
            @Part MultipartBody.Part partimage,
            @Part("nama") RequestBody nama,
            @Part("jumlah") RequestBody jumlah,
            @Part("nohp") RequestBody nohp,
            @Part("is_anonim") RequestBody is_anonim
    );

    @GET("user/me/donatur")
    Call<DonaturResponse> getDonaturUser(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PUT("konten/{id_konten}/donatur/{id}/approve")
    Call<DefaultResponse> approveDonasi(
            @Path("id_konten") int id_konten,
            @Path("id") int id,
            @Header("Authorization") String token,
            @Field("is_diterima") int is_diterima
    );

    @DELETE("konten/{id_konten}/donatur/{id}/disapprove")
    Call<DefaultResponse> disapproveDonasi(
            @Path("id_konten") int id_konten,
            @Path("id") int id,
            @Header("Authorization") String token
    );
}
