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
import android.widget.Toast;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.cloudinary.android.MediaManager;
//import com.cloudinary.android.callback.ErrorInfo;
//import com.cloudinary.android.callback.UploadCallback;
//import com.squareup.picasso.Picasso;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.CloudinaryRequest;
import com.cloudinary.android.MediaManager;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String cloudName = "dwsu45b3y";
    private static final String apiKey = "954292498755932";
    private static final String apiSecret = "jXKSRo_vlCbyXOob791iAUPBT6U";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery2);

//        initCloudinary();  // Initialize Cloudinary

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycler_view);
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);


//      stringArrayList.add("https://res.cloudinary.com/dwsu45b3y/image/upload/v1746434360/50%20lira20250505_113920.png");
        swipeRefreshLayout.setOnRefreshListener(this::fetchLast10CloudinaryImage);

        // Initial fetch
        swipeRefreshLayout.setRefreshing(true);
        fetchLast10CloudinaryImage();

    }

    private CloudinaryApiService createCloudinaryService() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(apiKey, apiSecret))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cloudinary.com/v1_1/" + cloudName + "/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swipeRefreshLayout.setRefreshing(false);
        return retrofit.create(CloudinaryApiService.class);
    }

    private void fetchLast10CloudinaryImage() {
        CloudinaryApiService apiService = createCloudinaryService();
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

    //To Delte Image
    public void deleteImage(ImageItem imageItem, int position) {
        // Extract the Cloudinary public ID from the image URL
        String publicId = extractPublicId(imageItem.getImageUrl());

        if (publicId != null) {

            // Create the Cloudinary service instance
            CloudinaryApiService deleteApi = createCloudinaryService();

            long timestamp = System.currentTimeMillis() / 1000;
            String signature = generateSignature(publicId, timestamp, apiSecret);

            // Call Cloudinary's delete API (POST request to /resources/image/destroy)
            deleteApi.deleteImage(publicId, timestamp, apiKey, signature).enqueue(new Callback<DeleteResponse>() {
                @Override
                public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                    if(response.isSuccessful()) {
                        // Remove the image from the list
                        imageList.remove(position);
                        // Notify adapter that item is removed
                        imageAdapter.notifyItemRemoved(position);
                        Toast.makeText(Gallery2.this, "Fotoğraf silindi!", Toast.LENGTH_SHORT).show();
                        //Log.d("Delete", "Success: " + new Gson().toJson(response.body()));
                        //fetchLast10CloudinaryImage();
                    } else {
                        Log.d("Delete", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<DeleteResponse> call, Throwable t) {
                    Log.d("Delete", "Failed: " + t.getMessage());
                }
            });

        } else {
            // Handle the case when publicId is null (optional)
            Toast.makeText(Gallery2.this, "Hatalı görsel URL'si.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper to get publicId from Cloiudinary URL
    private String extractPublicId(String imageUrl) {
        try {
            Uri uri = Uri.parse(imageUrl);
            String path = uri.getPath();
            String[] parts = path.split("/");
            String filename = parts[parts.length - 1];
            return filename.substring(0, filename.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateSignature(String publicId, long timestamp, String apiSecret) {
        String data = "public_id=" + publicId + "&timestamp=" + timestamp + apiSecret;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


//    public void initCloudinary() {
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", "dwsu45b3y");  // Replace with your Cloudinary cloud name
//        config.put("api_key", "954292498755932");       // Replace with your API key
//        config.put("api_secret", "jXKSRo_vlCbyXOob791iAUPBT6U"); // Replace with your API secret
//        MediaManager.init(this, config);           // Initialize Cloudinary
//    }
}