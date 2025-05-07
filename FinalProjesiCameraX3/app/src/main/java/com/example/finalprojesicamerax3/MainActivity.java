package com.example.finalprojesicamerax3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCapture.Builder;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;

//import com.example.finalprojesicamerax3.ml.ModelUnquant;
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.finalprojesicamerax3.ml.ModelUnquant;
import com.example.finalprojesicamerax3.ml.ModelUnquant2;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

 public class MainActivity extends AppCompatActivity {

//    //Prediction
    Bitmap image;
    String predResult = "Buradayim";
    int imageSize = 224;
//    String[] labelsArray;

    // TTS
//    Button button;
//    EditText editText;
//    SeekBar seekBar;
    TextToSpeech textToSpeech;
    GestureDetector gestureDetector;


//    Button btnClickPhoto;
    Button btnGallery;
    TextureView textureView;

    private PermissionManager permissionManager;
    private  String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private  ImageCapture imgCap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // TTS
//        button = findViewById(R.id.button);
//        editText = findViewById(R.id.edittext);
//        seekBar = findViewById(R.id.seekbar);

        //TTS
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.forLanguageTag("tr-TR"));;

                    if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Dil destekleniyor", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // TTS On CLick
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String data = editText.getText().toString();
//
//                float speed = (float)  seekBar.getProgress() / 50;
//                if (speed < 0.1) speed = 0.1f;
//                textToSpeech.setSpeechRate(speed);
//                textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
//            }
//        });

        // TTS GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
           @Override
           public boolean onDoubleTap(MotionEvent e) {

               clickPhoto();
               return true;
           }
        });

//        btnClickPhoto = findViewById(R.id.btnClickPhoto);
        btnGallery = findViewById(R.id.btnGallery);

        // MainActivity.java to Gallery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gallery2.class);
                startActivity(intent);
            }
        });

        permissionManager = PermissionManager.getInstance(this);
        textureView = findViewById(R.id.textureViewID );



        // Request permissions first
        if (!permissionManager.checkPermissions(permissions)) {
            permissionManager.askPermissions(this, permissions, 100);
        } else {
            // Permissions already granted → Open camera immediately
            openCamera();
        }

//        btnClickPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //clickPhoto();
//                }
//        });
    }

    // Prediction : model
    @SuppressLint("DefaultLocale")
    public  void classifyImage(Bitmap image) {
        try {
//            ModelFruits
//            ModelUnquant2
//            TurkLirasimodeli
//              TurkisLiraModel2
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++){
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"5 lira", "10 lira", "20 lira", "50 lira", "100 lira", "200 lira"};
//            String[] classes = {"apple", "banana", "orange"};

            predResult = classes[maxPos];

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));

            }

//            confidence.setText(s.toString());

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }


     // TTS
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // TTS Function to read text aloud
    private  void speakText(String data) {
//        String data = "iki kere sayfaya bastiniz";
//        float speed = (float) seekBar.getProgress() / 50;
//        if (speed < 0.1) speed = 0.1f;
//
//        textToSpeech.setSpeechRate(speed);
        textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    // TTS
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    @Override
    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                      @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            permissionManager.handlePermissionResult(MainActivity.this, 100, permissions,
                    grantResults);

            //permission granted
            openCamera();
        }
    }

    private void openCamera() {
        if (textureView == null) {
            Log.e("CameraX", "TextureView is not initialized.");
            return;
        }

        CameraX.unbindAll();

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());

        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetResolution(screen)
                .build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(output -> {
            ViewGroup parent = (ViewGroup) textureView.getParent();
            parent.removeView(textureView);
            parent.addView(textureView, 0);
            textureView.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        imgCap = new ImageCapture(imageCaptureConfig);

        // Bind to lifecycle only when the camera preview is ready
        CameraX.bindToLifecycle(this, preview, imgCap);
    }


    private  void  updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case  Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    private void clickPhoto() {

        if (imgCap == null) {
            Log.e("CameraX", "ImageCapture is not initialized.");
            Toast.makeText(this, "Camera is not ready yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file =
                new File(getExternalFilesDir(null), System.currentTimeMillis() + ".png");

        imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {
                String msg = "Pic captured at " + file.getAbsolutePath();
                // Decode and display the bitmap
                image = BitmapFactory.decodeFile(file.getPath());

                // Prediction: Model
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);

                // Saying result
                speakText(predResult);

                // Convert the image to a byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Upload to Cloudinary with predResult as the file name
                String fileName = predResult + " " + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                uploadToCloudinary(byteArray, fileName);

                // Optionally show the success message
                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                //addImageToGallery(file.getPath(), MainActivity.this);
            }

            @Override
            public void onError(@NonNull ImageCapture.UseCaseError useCaseError,
                                @NonNull String message,
                                @Nullable Throwable cause) {
                String msg = "Pic capture failed : " + message;
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                if (cause != null) cause.printStackTrace();

            }
        });
    }

    private void uploadToCloudinary(byte[] imageBytes, String fileName) {
        // Cloudinary configuration
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dwsu45b3y",
                "api_key", "954292498755932",
                "api_secret", "jXKSRo_vlCbyXOob791iAUPBT6U"
        );
        Cloudinary cloudinary = new Cloudinary(config);

//        try {
//            // Upload the image
//            Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap("public_id", fileName));
//            Log.d("Cloudinary", "Upload successful: " + uploadResult);
//            Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e("Cloudinary", "Upload failed", e);
//            Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
//        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Upload the image
                    Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap("public_id", fileName));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Cloudinary", "upload successful" + uploadResult);
                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("Cloudinary", "Upload failed", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + "png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
        //        values.put(MediaStore.MediaColumns.DATA, filePath);

        try {
            // Insert image metadata into MediaStore
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Gallery", "Error saving image: " + e.getMessage());
        }
//        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

}