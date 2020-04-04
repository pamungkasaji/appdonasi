package com.aji.donasi.api;

import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.LoginResponse;
import com.aji.donasi.models.PerkembanganResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("konten")
    Call<KontenResponse> getKonten();

//    @GET("konten/{id}")
//    Call<KontenResponse> getKontenDetail(@Path("id") int id);

    @GET("konten/{id}/donatur")
    Call<DonaturResponse> getDonatur(@Path("id") int id);

    @GET("konten/{id}/perkembangan")
    Call<PerkembanganResponse> getPerkembangan(@Path("id") int id);

    @GET("konten/judul/{keyword}")
    Call<KontenResponse> searchKonten (@Path("keyword") String keyword);

    @Multipart
    @POST("konten")
    Call<DefaultResponse> createKonten(
            @Header("Authorization") String token,
            @Part MultipartBody.Part partimage,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("target") RequestBody target,
            @Part("lama_donasi") RequestBody lama_donasi,
            @Part("nomorrekening") RequestBody nomorrekening,
            @Part("bank") RequestBody bank
    );

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

    @Multipart
    @POST("konten/{id}/perkembangan")
    Call<DefaultResponse> createPerkembangan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Part MultipartBody.Part partimage,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi
    );

    @Multipart
    @POST("konten/{id}/perkembangan")
    Call<DefaultResponse> createPerkembangan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Part MultipartBody.Part partimage,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("pengeluaran") RequestBody pengeluaran
    );

    @FormUrlEncoded
    @POST("konten/{id}/perpanjangan")
    Call<DefaultResponse> perpanjangan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Field("jumlah_hari") String jumlah_hari,
            @Field("alasan") String alasan
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @Multipart
    @POST("register")
    Call<DefaultResponse> createUser(
            @Part MultipartBody.Part partimage,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("nohp") RequestBody nohp,
            @Part("namalengkap") RequestBody namalengkap,
            @Part("nomorktp") RequestBody nomorktp,
            @Part("alamat") RequestBody alamat
    );

    @GET("user/me/konten")
    Call<KontenResponse> getKontenUser(
            @Header("Authorization") String token
            //@Query("token") String token
    );

    @GET("user/me/donatur")
    Call<DonaturResponse> getDonaturUser(
            @Header("Authorization") String token
    );

    @GET("user/me/konten/{id}")
    Call<KontenResponse> isUser (
            @Path("id") int id,
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
