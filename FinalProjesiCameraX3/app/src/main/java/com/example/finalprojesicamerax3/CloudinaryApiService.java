package com.example.finalprojesicamerax3;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Cloudinary API isteklerini tanımlayan arayüz.
 * Retrofit kullanılarak API çağrıları yapılır.
 */
public interface CloudinaryApiService {

    // Cloudinary'den resim listesi çeker.
    @GET("resources/image")
    Call<CloudinaryResponse> getImages(
            @Query("max_results") int maxResults
    );

    // Cloudinary'den resim silme isteği gönderir.
    @FormUrlEncoded
    @POST("image/destroy")
    Call<DeleteResponse> deleteImage(
            @Field("public_id") String publicId,
            @Field("timestamp") long timestamp,
            @Field("api_key") String apiKey,
            @Field("signature") String signature
    );
}
