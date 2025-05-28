package com.example.finalprojesicamerax3;

import static androidx.core.util.TypedValueCompat.dpToPx;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Kamera ile ilgili importlar
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCapture.Builder;
//import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
//import androidx.camera.core.PreviewConfig;
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
import android.support.media.ExifInterface;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;

import org.checkerframework.common.subtyping.qual.Bottom;
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
//    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
//    TextureView textureView;

     private PermissionManager permissionManager;
    private  String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private  ImageCapture imgCap;

     // YENI KAMERA AYARI
     private PreviewView previewView;
     private ImageCapture imageCapture;
     private String currentPhotoPath;  // Fotoğrafın yolu burada saklanacak

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
//        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        btnGallery = findViewById(R.id.btnGallery);
//
//        //Initialize behaviyor
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

//        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@androidx.annotation.NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    // Set marginTop = 0
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btnGallery.getLayoutParams();
//                    params.topMargin = 0;
//                    btnGallery.setLayoutParams(params);
//
//                    // Delay and animate marginTop to -30 after 1.5 sec
//                    btnGallery.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            int marginPx = myDpToPx(-30); // convert -20dp to px
//
//                            ValueAnimator anim = ValueAnimator.ofInt(0, marginPx);
//                            anim.setDuration(300); // duration in milliseconds
//                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                @Override
//                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                                    int animatedMargin = (int) valueAnimator.getAnimatedValue();
//                                    ViewGroup.MarginLayoutParams updatedParams = (ViewGroup.MarginLayoutParams) btnGallery.getLayoutParams();
//                                    updatedParams.topMargin = animatedMargin;
//                                    btnGallery.setLayoutParams(updatedParams);
//                                }
//                            });
//                            anim.start();
//                        }
//                    }, 1500); // Delay in milliseconds
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//        // Set the peek height (how much of the button is visible when hidden)
//        btnGallery.post(new Runnable() {
//            @Override
//            public void run() {
//                int buttonHeight = btnGallery.getHeight();
//                // Let's make about 30% of button visible
//                bottomSheetBehavior.setPeekHeight(buttonHeight / 3);
//            }
//        });
//
//        // Make it hideable when dragged back down
//        bottomSheetBehavior.setHideable(false);

        // Button click _ open Gallery2

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gallery2.class);
                startActivity(intent);
            }
        });

        permissionManager = PermissionManager.getInstance(this);
        //textureView = findViewById(R.id.textureViewID );
        previewView = findViewById(R.id.previewView);



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

    // Helper method to convert dp to px
     private int myDpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
     }

    // Prediction : model
    @SuppressLint("DefaultLocale")
    public void classifyImage(Bitmap image) {
        try {
            int imageSize = 224; // Model giriş boyutuna göre ayarla

            // Model yükle
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Bitmap boyutlandır
            Bitmap scaledImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

            // TensorBuffer giriş oluştur
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            scaledImage.getPixels(intValues, 0, scaledImage.getWidth(), 0, 0, scaledImage.getWidth(), scaledImage.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Model çalıştır
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"5 lira", "10 lira", "20 lira", "50 lira", "100 lira", "200 lira"};
            predResult = classes[maxPos];

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));
            }

            // Örnek: confidence.setText(s.toString());
            Log.d("ML Result", s.toString());

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                      @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            permissionManager.handlePermissionResult(MainActivity.this, 100, permissions,
                    grantResults);

            //permission granted
            openCamera();
        }
    }
     // ESKI KAMERA AYARI
//    private void openCamera() {
//        if (textureView == null) {
//            Log.e("CameraX", "TextureView is not initialized.");
//            return;
//        }
//
//        CameraX.unbindAll();
//
//        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
//        Size screen = new Size(textureView.getWidth(), textureView.getHeight());
//
//        PreviewConfig pConfig = new PreviewConfig.Builder()
//                .setTargetAspectRatio(aspectRatio)
//                .setTargetResolution(screen)
//                .build();
//        Preview preview = new Preview(pConfig);
//
//        preview.setOnPreviewOutputUpdateListener(output -> {
//            ViewGroup parent = (ViewGroup) textureView.getParent();
//            parent.removeView(textureView);
//            parent.addView(textureView, 0);
//            textureView.setSurfaceTexture(output.getSurfaceTexture());
//            updateTransform();
//        });
//
//        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
//                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
//                .build();
//
//        imgCap = new ImageCapture(imageCaptureConfig);
//
//        // Bind to lifecycle only when the camera preview is ready
//        CameraX.bindToLifecycle(this, preview, imgCap);
//    }

     // YENI KAMERA AYARI
     private void openCamera() {
         ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

         cameraProviderFuture.addListener(() -> {
             try {
                 ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                 // Preview tanımla
                 Preview preview = new Preview.Builder()
                         .build();

                 // ImageCapture tanımla (yüksek çözünürlük)
                 imageCapture = new ImageCapture.Builder()
                         // Belirli bir çözünürlük istiyorsan:
                         // .setTargetResolution(new Size(1920, 1080))
                         // veya aspect ratio'yu ayarla:
                         // .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                         // Ayrıca rotasyon ayarı:
                         .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                         .build();

                 // Kamera seçimi (arka kamera)
                 CameraSelector cameraSelector = new CameraSelector.Builder()
                         .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                         .build();

                 // Preview'u PreviewView'a bağla
                 preview.setSurfaceProvider(previewView.getSurfaceProvider());

                 // Önceki bindingleri kaldır
                 cameraProvider.unbindAll();

                 // Yeni bindingleri ekle
                 cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, ContextCompat.getMainExecutor(this));
     }

// ESKI KAMERA AYARI

//    private  void  updateTransform() {
//        Matrix mx = new Matrix();
//        float w = textureView.getMeasuredWidth();
//        float h = textureView.getMeasuredHeight();
//
//        float cX = w / 2f;
//        float cY = h / 2f;
//
//        int rotationDgr;
//        int rotation = (int) textureView.getRotation();
//
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                rotationDgr = 0;
//                break;
//            case  Surface.ROTATION_90:
//                rotationDgr = 90;
//                break;
//            case Surface.ROTATION_180:
//                rotationDgr = 180;
//                break;
//            case Surface.ROTATION_270:
//                rotationDgr = 270;
//                break;
//            default:
//                return;
//        }
//
//        mx.postRotate((float) rotationDgr, cX, cY);
//        textureView.setTransform(mx);
//    }

     // ESKI KAMERA AYARI

//    private void clickPhoto() {
//
//        if (imgCap == null) {
//            Log.e("CameraX", "ImageCapture is not initialized.");
//            Toast.makeText(this, "Kamera henüz hazır değil!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        File file =
//                new File(getExternalFilesDir(null), System.currentTimeMillis() + ".png");
//
//        imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
//            @Override
//            public void onImageSaved(@NonNull File file) {
//                String msg = "Resim çekme başarısız oldu: " + file.getAbsolutePath();
//                // Decode and display the bitmap
//                image = BitmapFactory.decodeFile(file.getPath());
//
//                // Prediction: Model
//                int dimension = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
//
//                // Saying result
//                speakText(predResult);
//
//                // Convert the image to a byte array
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                byte[] byteArray = byteArrayOutputStream.toByteArray();
//
//                // Upload to Cloudinary with predResult as the file name
//                String fileName = predResult + " " + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//                uploadToCloudinary(byteArray, fileName);
//
//                // Optionally show the success message
//                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                //addImageToGallery(file.getPath(), MainActivity.this);
//            }
//
//            @Override
//            public void onError(@NonNull ImageCapture.UseCaseError useCaseError,
//                                @NonNull String message,
//                                @Nullable Throwable cause) {
//                String msg = "Resim çekme başarısız oldu: " + message;
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//                if (cause != null) cause.printStackTrace();
//
//            }
//        });
//    }

     // YENI KAMERA AYARI
     private void clickPhoto() {

         if (imageCapture == null) {
             Toast.makeText(this, "Camera is not ready", Toast.LENGTH_SHORT).show();
             return;
         }

         File photoFile = createImageFile(); // Bu fonksiyon currentPhotoPath'ı atamalı
         currentPhotoPath = photoFile.getAbsolutePath();
//         File photoFile = new File(currentPhotoPath);  // Fotoğrafın kaydedildiği gerçek dosya

         if (!photoFile.exists()) {
             Toast.makeText(MainActivity.this, "Fotoğraf dosyası bulunamadı.", Toast.LENGTH_SHORT).show();
             return;
         }


         ImageCapture.OutputFileOptions outputFileOptions =
                 new ImageCapture.OutputFileOptions.Builder(photoFile).build();


         Executor executor = ContextCompat.getMainExecutor(this);
         imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
             @Override
             public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                 Toast.makeText(MainActivity.this, "Photo saved: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();


                 Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

                 if (bitmap == null) {
                     Toast.makeText(MainActivity.this, "Fotoğraf yüklenemedi.", Toast.LENGTH_SHORT).show();
                     Log.d("DEBUG", "currentPhotoPath: " + currentPhotoPath);
                     return;
                 }

                 // Resim varsa işlemleri yap
                 // 💡 İsteğe göre boyutlandır
                 int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                 // Sadece gösterme için küçültme yap
                 Bitmap displayBitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                 displayBitmap = Bitmap.createScaledBitmap(displayBitmap, imageSize, imageSize, false);

                 // Modeli çalıştır
                 classifyImage(displayBitmap);



                 // Saying result
                 speakText(predResult);

                 try {
                     bitmap = fixRotation(currentPhotoPath, bitmap);
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 }

                 // Convert the image to a byte array
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                 if (!compressed) {
                     Toast.makeText(MainActivity.this, "Fotoğraf sıkıştırma başarısız.", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 byte[] byteArray = byteArrayOutputStream.toByteArray();

                 // Upload to Cloudinary with predResult as the file name
                 String fileName = predResult + " " + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                 uploadToCloudinary(byteArray, fileName);
                 
             }

             @Override
             public void onError(@NonNull ImageCaptureException exception) {
                 Toast.makeText(MainActivity.this, "Photo capture failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });
     }

     // YENI KAMERA AYARI
     private File createImageFile() {
         File image = null;
         try {
             String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
             String imageFileName = "JPEG_" + timeStamp + "_";
             File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
             image = File.createTempFile(
                     imageFileName,  /* prefix */
                     ".jpg",         /* suffix */
                     storageDir      /* directory */
             );
             currentPhotoPath = image.getAbsolutePath();
         } catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(this, "Dosya oluşturulamadı!", Toast.LENGTH_SHORT).show();
         }

         return image;
     }

    // YENI KAMERA AYARI
     public Bitmap fixRotation(String photoPath, Bitmap bitmap) throws IOException {

         ExifInterface exif = new ExifInterface(photoPath);
         int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
         Log.d("DEBUG", "**************************Exif orientation: " + orientation);

         int rotationDegrees = 0;
         switch (orientation) {
             case ExifInterface.ORIENTATION_ROTATE_90:
                 rotationDegrees = 90;
                 break;
             case ExifInterface.ORIENTATION_ROTATE_180:
                 rotationDegrees = 180;
                 break;
             case ExifInterface.ORIENTATION_ROTATE_270:
                 rotationDegrees = 270;
                 break;
             default:
                 rotationDegrees = 0;
         }

         if (rotationDegrees == 0) return bitmap;

         Matrix matrix = new Matrix();
         matrix.postRotate(rotationDegrees);
         Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
         if (rotatedBitmap != bitmap) {
             bitmap.recycle();
         }
         return rotatedBitmap;
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
                            Toast.makeText(getApplicationContext(), "Yükleme başarılı", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("Cloudinary", "Upload failed", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Yükleme başarısız oldu", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

//    public static void addImageToGallery(final String filePath, final Context context) {
//
//        ContentValues values = new ContentValues();
//
//        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + "png");
//        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
//        //        values.put(MediaStore.MediaColumns.DATA, filePath);
//
//        try {
//            // Insert image metadata into MediaStore
//            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (uri != null) {
//                try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                    outputStream.flush();
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Gallery", "Error saving image: " + e.getMessage());
//        }
//        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//    }

}