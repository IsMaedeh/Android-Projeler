package com.example.finalprojesicamerax3;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CloudinaryApiService {
    @GET("resources/image")
    Call<CloudinaryResponse> getImages(
            @Query("max_results") int maxResults
    );

    @FormUrlEncoded
    @POST("image/destroy")
    Call<DeleteResponse> deleteImage(
            @Field("public_id") String publicId,
            @Field("timestamp") long timestamp,
            @Field("api_key") String apiKey,
            @Field("signature") String signature
    );
}
