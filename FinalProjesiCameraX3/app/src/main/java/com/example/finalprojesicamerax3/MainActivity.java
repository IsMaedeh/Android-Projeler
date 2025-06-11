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
import java.util.HashMap;
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
import com.example.finalprojesicamerax3.ml.MobilModeli2;
import com.example.finalprojesicamerax3.ml.MobilModeli3;
import com.example.finalprojesicamerax3.ml.ModelUnquant;
import com.example.finalprojesicamerax3.ml.ModelUnquant2;
import com.example.finalprojesicamerax3.ml.MobilModeli;
import com.example.finalprojesicamerax3.ml.ModelUnquant3;
import com.example.finalprojesicamerax3.ml.TurkliraModelFloat16;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;
import org.checkerframework.common.subtyping.qual.Bottom;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

// MainActivity sınıfı: Uygulamanın ana ekranı.
 public class MainActivity extends AppCompatActivity {

    // Tanımlar:
    Bitmap image;                       // Fotoğrafı saklayacak bitmap.
    String predResult = "Buradayim";    // Öntanımlı tahmin sonucu.
    int imageSize = 224;                // Resim boyutu (224x224) - model için.
    TextToSpeech textToSpeech;          // Yazıyı sese çevirme motoru.
    GestureDetector gestureDetector;    // Dokunma hareketlerini algılayıcı.
    Button btnGallery;                  // Galeri butonu.
    private PermissionManager permissionManager;    // İzin yöneticisi.
    private  String[] permissions = {               // Gerekli izinler.
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private  ImageCapture imgCap;       // Resim çekme nesnesi (eski kullanım).

     // YENI KAMERA AYARI
     private PreviewView previewView;   // Kameradan canlı görüntü almak için.
     private ImageCapture imageCapture; // Yeni kamera API'si için fotoğraf çekme nesnesi.
     private String currentPhotoPath;  // Çekilen fotoğrafın yolu.

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);    // Kenardan kenara tasarım.
        setContentView(R.layout.activity_main);         // Ekran tasarımı yükleniyor.


        //TTS
        // Text to Speech ayarlanıyor.
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.forLanguageTag("tr-TR"));;

                    // Dil desteği kontrolü.
                    if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Dil destekleniyor", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // TTS GestureDetector (Çift tıklama algılayıcı)
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
           @Override
           public boolean onDoubleTap(MotionEvent e) {
               clickPhoto();    // Çift tıklamada fotoğraf çek.
               return true;
           }
        });

        // Galeri butonu tanımı ve tıklama olayı.
        btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gallery2.class);
                startActivity(intent);
            }
        });

        // İzin yöneticisi başlatılıyor.
        permissionManager = PermissionManager.getInstance(this);
        previewView = findViewById(R.id.previewView);   // Kamera önizlemesi.



        // Gerekli izinler var mı kontrol ediliyor.
        if (!permissionManager.checkPermissions(permissions)) {
            permissionManager.askPermissions(this, permissions, 100);
        } else {
            // Varsa kamerayı aç.
            openCamera();
        }

    }

    // Verilen Bitmap tipindeki görüntüyü model giriş boyutuna göre ölçeklendirir,
    // ön işler, TensorFlow Lite modeliyle sınıflandırır ve sonuçları alır.
    @SuppressLint("DefaultLocale")
    public void classifyImage(Bitmap image) {
        try {
            // Görüntü ARGB_8888 formatına çevrilir.
            image = image.copy(Bitmap.Config.ARGB_8888, true);
            // Modelin beklediği boyut (224x224) ayarlanır.
            int imageSize = 224; // Model giriş boyutuna göre ayarla

            // Model yükle
            ModelUnquant2 model = ModelUnquant2.newInstance(getApplicationContext());

            // Bitmap boyutlandır
            Bitmap scaledImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

            // TensorBuffer giriş oluştur
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            scaledImage.getPixels(intValues, 0, scaledImage.getWidth(), 0, 0, scaledImage.getWidth(), scaledImage.getHeight());

            // Görüntünün her pikseli RGB formatında normalize edilerek ByteBuffer içine yazılır.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];

                    int R = (val >> 16) & 0xFF;
                    int G = (val >> 8) & 0xFF;
                    int B = val & 0xFF;

                    byteBuffer.putFloat(R * (1.f / 255.f));
                    byteBuffer.putFloat(G * (1.f / 255.f));
                    byteBuffer.putFloat(B * (1.f / 255.f));

                }
            }


            inputFeature0.loadBuffer(byteBuffer);

            // Model çalıştır
            ModelUnquant2.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // En yüksek olasılığa sahip sınıf belirlenir.
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
            // Tahmin sonucu predResult değişkenine atanır.
            predResult = classes[maxPos];

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));
            }
            // Olasılık değerleri loglanır.
            Log.d("ML Result", s.toString());

            // Model kapatılır.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     // TTS
     // Kullanıcının dokunma olaylarını yakalar ve bu olayları gestureDetector'a ileterek işlenmesini sağlar.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Olay gestureDetector tarafından işlendi ise true, değilse üst sınıf işlemi çağrılır.
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // TTS Function to read text aloud
    // Verilen metni cihazın Text-to-Speech (TTS) motoru kullanarak sesli olarak okur.
    private  void speakText(String data) {
        textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    // TTS
    // Activity kapatılırken TTS kaynaklarını serbest bırakır (durdurur ve kapatır).
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Kullanıcı izin isteğine yanıt verdiğinde çağrılır.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                      @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // İzin kodu 100 ise, izin sonuçları işlenir ve kamera açılır.
        if (requestCode == 100) {
            permissionManager.handlePermissionResult(MainActivity.this, 100, permissions,
                    grantResults);

            //permission granted
            openCamera();
        }
    }

     // YENI KAMERA AYARI
     // Yeni kamera API'si (CameraX) kullanılarak arka kamera açılır ve görüntü önizlemesi başlatılır.
     private void openCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

         cameraProviderFuture.addListener(() -> {
             try {
                 // CameraProvider alınır.
                 ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                 // Preview tanımla
                 Preview preview = new Preview.Builder()
                         .build();

                 // ImageCapture tanımla (yüksek çözünürlük)
                 imageCapture = new ImageCapture.Builder()
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

                 // Yeni bindingleri ekle (Yeni kamera bağlamaları yapılır.)
                 cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, ContextCompat.getMainExecutor(this));
     }

     // YENI KAMERA AYARI
     // Kameradan fotoğraf çeker, dosyaya kaydeder, döndürme sorunlarını giderir,
     // resmi sınıflandırmaya gönderir ve sonucu seslendirir.
     // Son olarak resmi Cloudinary servisine yükler.
     private void clickPhoto() {


         if (imageCapture == null) {
             Toast.makeText(this, "Camera is not ready", Toast.LENGTH_SHORT).show();
             return;
         }

        // Fotoğraf dosyası oluşturulur.
         File photoFile = createImageFile(); // Bu fonksiyon currentPhotoPath'ı atamalı
         currentPhotoPath = photoFile.getAbsolutePath();

         if (!photoFile.exists()) {
             Toast.makeText(MainActivity.this, "Fotoğraf dosyası bulunamadı.", Toast.LENGTH_SHORT).show();
             return;
         }

         ImageCapture.OutputFileOptions outputFileOptions =
                 new ImageCapture.OutputFileOptions.Builder(photoFile).build();

         Executor executor = ContextCompat.getMainExecutor(this);

         // Fotoğraf çekilir
         imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
             @Override
             public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                 Toast.makeText(MainActivity.this, "Photo saved: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();


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

                 // Döndürme açısı düzeltilir.
                 try {
                     bitmap = fixRotation(currentPhotoPath, bitmap);
                     displayBitmap = fixRotation(currentPhotoPath, displayBitmap);
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 }

                 // classifyImage() ile sınıflandırılır.
                 classifyImage(displayBitmap);

                 // Tahmin sonucu speakText() ile seslendirilir.
                  speakText(predResult);



                 // Fotoğraf byte dizisine çevrilip Cloudinary'ye yüklenir.
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
     // Geçici bir fotoğraf dosyası oluşturur ve dosya yolunu döner.
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
    // Fotoğrafın EXIF verilerindeki oryantasyona göre resmi uygun açıda döndürür.
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

         // Döndürülmüş bitmap.
         return rotatedBitmap;
     }

     // Verilen byte dizisi olarak resmi Cloudinary bulut servisine yükler.
     private void uploadToCloudinary(byte[] imageBytes, String fileName) {
        // Cloudinary yapılandırılır.
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dwsu45b3y",
                "api_key", "954292498755932",
                "api_secret", "jXKSRo_vlCbyXOob791iAUPBT6U"
        );
        Cloudinary cloudinary = new Cloudinary(config);
        // Yükleme arka planda bir iş parçacığında yapılır.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Başarı/başarısızlık durumu kullanıcıya bildirilir.
                try {
                    // Fotoğraf yükleme
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

    // Dosya yolundaki resmi Android galeriye ekler.
    public static void addImageToGallery(final String filePath, final Context context) {

        // Resim için metadata oluşturur.
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + "png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
        //        values.put(MediaStore.MediaColumns.DATA, filePath);

        try {
            // Insert image metadata into MediaStore (MediaStore içerik sağlayıcısına ekler ve resmi sıkıştırarak yazar.)
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
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

}