package com.example.finalprojesicamerax3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CloudinaryApiService {
    @GET("resources/image")
    Call<CloudinaryResponse> getImages(
            @Query("max_results") int maxResults
    );
}
