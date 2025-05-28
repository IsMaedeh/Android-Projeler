package com.example.finalprojesicamerax3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//
//import com.cloudinary.android.MediaManager;
//import com.cloudinary.android.callback.ErrorInfo;
//import com.cloudinary.android.callback.UploadCallback;
//import com.squareup.picasso.Picasso;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.CloudinaryRequest;
import com.cloudinary.android.MediaManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Gallery2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ImageItem> imageList;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery2);

//        initCloudinary();  // Initialize Cloudinary

        recyclerView = findViewById(R.id.recycler_view);
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);


//      stringArrayList.add("https://res.cloudinary.com/dwsu45b3y/image/upload/v1746434360/50%20lira20250505_113920.png");
        fetchLast10CloudinaryImage();
    }

    private void fetchLast10CloudinaryImage() {
        String cloudName = "dwsu45b3y";
        String apiKey = "954292498755932";
        String apiSecret = "jXKSRo_vlCbyXOob791iAUPBT6U";

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(apiKey, apiSecret))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cloudinary.com/v1_1/" + cloudName + "/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CloudinaryApiService apiService = retrofit.create(CloudinaryApiService.class);

        Call<CloudinaryResponse> call = apiService.getImages(10);

        call.enqueue(new Callback<CloudinaryResponse>() {
            @Override
            public void onResponse(Call<CloudinaryResponse> call, Response<CloudinaryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<ImageItem> imageItems = new ArrayList<>();
                    for (CloudinaryResponse.ImageData image: response.body().getResources()) {
                        String url = image.getSecureUrl();
                        String name = image.getPublicId(); // Get the image name
                        String[] parts = name.split(" ");
                        name = parts[0] + " " + parts[1];
                        imageItems.add(new ImageItem(url, name));
                    }
                    imageList.clear(); // Clear any existing data
                    imageList.addAll(imageItems); // Add new imageItems
                    imageAdapter.notifyDataSetChanged(); //Notify adapter to update the RecyclerView

                    Log.d("Cloudinary", "Images fetched: " + imageItems.size());
                } else {
                    Log.e("Cloudinary", "Images failed: " + response.code());
                }

//                Log.d("Cloudinary", new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<CloudinaryResponse> call, Throwable t) {
                Log.e("Cloudinary", "Api call failed", t);
            }
        });


    }

//    public void initCloudinary() {
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", "dwsu45b3y");  // Replace with your Cloudinary cloud name
//        config.put("api_key", "954292498755932");       // Replace with your API key
//        config.put("api_secret", "jXKSRo_vlCbyXOob791iAUPBT6U"); // Replace with your API secret
//        MediaManager.init(this, config);           // Initialize Cloudinary
//    }
}