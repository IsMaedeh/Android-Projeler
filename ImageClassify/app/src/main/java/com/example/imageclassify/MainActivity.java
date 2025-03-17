package com.example.imageclassify;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;

//import com.example.imageclassify.MobilenetV110224Quant;
//import com.example.imageclassify.ml.MobilenetV110224Quant;
import com.example.imageclassify.ml.ModelUnquant;
import com.example.imageclassify.ml.TurkLirasimodeli;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button selectBtn, predictBtn, captureBtn;
    TextView result;
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // permission
        getPermission();

        List<String> labels = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labelsPara.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                labels.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert to an array if needed
        String[] labelsArray = labels.toArray(new String[0]);

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        captureBtn = findViewById(R.id.captureBtn);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                try {
//                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(MainActivity.this);
//
//                    // Creates inputs for reference.
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
//
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
//                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
//
//                    // Runs model inference and gets result.
//                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
//
//                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//                    result.setText(labelsArray[getMax(outputFeature0.getFloatArray())] + " ");
//
//                    // Releases model resources if no longer used.
//                    model.close();
//                } catch (IOException e) {
//                    // TODO Handle the exception
//                }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                try {
////                    // **Interpreter seçeneklerini belirleyelim**
////                    Model.Options options = new Model.Options.Builder()
////                            .setDevice(Model.Device.NNAPI)  // NNAPI kullan
////                            .setNumThreads(4)
////                            .build();
//
//
//                    // **Modeli yüklerken ayarları iletelim**
//                    TurkLirasimodeli model = TurkLirasimodeli.newInstance(MainActivity.this);
//
//                    // **Giriş verisini hazırla**
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 30, 30, 3}, DataType.FLOAT32);
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
//                    inputFeature0.loadBuffer(convertBitmapToByteBuffer(bitmap));
//
//                    // **Modeli çalıştır ve tahmini al**
//                    TurkLirasimodeli.Outputs outputs = model.process(inputFeature0);
//                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//                    // **En yüksek olasılığa sahip tahmini al**
//                    int predictedIndex = getMax(outputFeature0.getFloatArray());
//                    result.setText(labelsArray[predictedIndex] + " ");
//
//                    // **Modeli kapat (Kaynakları serbest bırak)**
//                    model.close();
//                } catch (IOException e) {
//                    e.printStackTrace();  // Hata varsa log'a yaz
//                }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                try {

                    ModelUnquant model = ModelUnquant.newInstance(MainActivity.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());



                    // Runs model inference and gets result.
                    ModelUnquant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    result.setText(labelsArray[getMax(outputFeature0.getFloatArray())] + " ");
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception

                }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });
    }


    //private
//    ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 30 * 30 * 3);
//        byteBuffer.order(ByteOrder.nativeOrder());
//
//        int[] intValues = new int[30*30];
//
//        for (int pixelValue : intValues) {
//            float r = ((pixelValue >> 16) & 0xFF) / 255.0f; // Normalize
//            float g = ((pixelValue >> 8) & 0xFF) / 255.0f;
//            float b = (pixelValue & 0xFF) / 255.0f;
//            byteBuffer.putFloat(r);
//            byteBuffer.putFloat(g);
//            byteBuffer.putFloat(b);
//        }
//        return byteBuffer;
//    }

    //private
//    int getMax(float[] arr) {
//        int maxIndex = 0;
//        float maxValue = arr[0];
//
//        for (int i = 1; i < arr.length; i++) {
//            if (arr[i] > maxValue) {
//                maxValue = arr[i];
//                maxIndex = i;
//            }
//        }
//        return  maxIndex;
//    }

    int getMax(float[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[max]) max = i;
        }
        return max;
    }

    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        if (requestCode == 11) {
            if(grantResults.length > 0){
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(data!=null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == 12){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}