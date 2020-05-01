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

public interface PerkembanganClient {

    @GET("konten/{id}/perkembangan")
    Call<PerkembanganResponse> getPerkembangan(@Path("id") int id);

    @Multipart
    @POST("konten/{id}/perkembangan")
    Call<DefaultResponse> createPerkembanganImage(
            @Path("id") int id,
            @Header("Authorization") String token,
            @Part MultipartBody.Part partimage,
            @PartMap() Map<String, RequestBody> partMap
    );

    @Multipart
    @POST("konten/{id}/perkembangan")
    Call<DefaultResponse> createPerkembangan(
            @Path("id") int id,
            @Header("Authorization") String token,
            @PartMap() Map<String, RequestBody> partMap
    );
}
