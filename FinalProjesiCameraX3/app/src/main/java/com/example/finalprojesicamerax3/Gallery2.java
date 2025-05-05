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
//
//import com.cloudinary.android.MediaManager;
//import com.cloudinary.android.callback.ErrorInfo;
//import com.cloudinary.android.callback.UploadCallback;
//import com.squareup.picasso.Picasso;

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
//        button = findViewById(R.id.button);
        
    }
}