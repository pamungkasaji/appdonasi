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

public interface AuthClient {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @Multipart
    @POST("register")
    Call<DefaultResponse> createUser(
            @PartMap Map<String, RequestBody> user,
            @Part MultipartBody.Part partimage
    );

    @GET("logout")
    Call<DefaultResponse> userLogout(
            @Header("Authorization") String token
    );
}
