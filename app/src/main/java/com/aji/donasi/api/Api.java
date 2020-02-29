package com.aji.donasi.api;

import com.aji.donasi.models.DefaultResponse;
import com.aji.donasi.models.DonaturResponse;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.aji.donasi.models.LoginResponse;
import com.aji.donasi.models.PerkembanganResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //ini
    @GET("konten")
    Call<KontenResponse> getKonten();

    @GET("konten/{id}")
    Call<KontenResponse> getKontenDetail(@Path("id") int id);

    @GET("konten/{id}/donatur")
    Call<DonaturResponse> getDonatur(@Path("id") int id);

    @GET("konten/{id}/perkembangan")
    Call<PerkembanganResponse> getPerkembangan(@Path("id") int id);

    @Multipart
    @POST("konten")
    Call<DefaultResponse> createKonten(
            @Query("token") String token,
            @Part MultipartBody.Part partimage,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("target") RequestBody target,
            @Part("lama_donasi") RequestBody lama_donasi,
            @Part("nomorrekening") RequestBody nomorrekening
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
            @Part("alamat") RequestBody alamat,
            @Part("nomorktp") RequestBody nomorktp
    );

    @GET("user/me/konten")
    Call<KontenResponse> getKontenUser(
            @Query("token") String token
    );

    @GET("user/me/donatur")
    Call<DonaturResponse> getDonaturUser(
            @Query("token") String token
    );

//    @FormUrlEncoded
//    @POST("register")
//    Call<DefaultResponse> createUser(
//            @Field("username") String username,
//            @Field("password") String password
//            //@Field("school") String school
//    );

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );




    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<LoginResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("email") String email
    );

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(@Path("id") int id);

}
