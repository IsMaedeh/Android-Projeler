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

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Gallery2 extends AppCompatActivity {

    private static final String TAG = "Upload ###";
    private static int IMAGE_REQ = 1;
    private Uri imagePath;
    private ImageView imageView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery2);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        initConfig();

        imageView.setOnClickListener(v -> {
            requestPermissions();
            Log.d(TAG, ": " + "request permission");

        });

        button.setOnClickListener(v -> {

            Log.d(TAG, ": " + " button clicked");

            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Log.d(TAG, "onStart: " + "usuccess");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }
            }).dispatch();
        });


    }

    private void initConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "dwsu45b3y");
        config.put("api_key", "954292498755932");
        config.put("api_secret", "jXKSRo_vlCbyXOob791iAUPBT6U");
//        config.put("secure", true);
        MediaManager.init(this, config);
    }

    private void requestPermissions() {
        if(ContextCompat.checkSelfPermission(Gallery2.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            SelectImage();
        } else {
            ActivityCompat.requestPermissions(Gallery2.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQ);
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQ);
        someAcrivityResultLauncher.launch(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imagePath = data.getData();
//            Picasso.get().load(imagePath).into(imageView);
//        }
//    }

    ActivityResultLauncher<Intent> someAcrivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imagePath = data.getData();
                        Picasso.get().load(imagePath).into(imageView);
                    }
                }
            });
}