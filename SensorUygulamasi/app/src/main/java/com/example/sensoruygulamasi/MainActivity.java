package com.example.sensoruygulamasi;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer, gyroscope, humidity, light, pressure, proximity, thermometer;
    private Button btnAccelerometer, btnCompass, btnGyroscope, btnHumidity, btnLight, btnMagnometer, btnPressure, btnProximity, btnThermometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Butonları alıyoruz
        btnAccelerometer = findViewById(R.id.btnAccelerometer);
        btnCompass = findViewById(R.id.btnCompass);
        btnGyroscope = findViewById(R.id.btnGyroscope);
        btnHumidity = findViewById(R.id.btnHumidity);
        btnLight = findViewById(R.id.btnLight);
        btnMagnometer = findViewById(R.id.btnMagnometer);
        btnPressure = findViewById(R.id.btnPressure);
        btnProximity = findViewById(R.id.btnProximity);
        btnThermometer = findViewById(R.id.btnThermometer);

        // SensorManager oluşturuluyor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Sensörleri alıyoruz
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        thermometer = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Butonlara tıklanabilirlik ekliyoruz
        btnAccelerometer.setOnClickListener(v -> activateSensor(accelerometer, "İvmeölçer aktif!"));
        btnCompass.setOnClickListener(v -> activateSensor(magnetometer, "Pusula aktif!"));
        btnGyroscope.setOnClickListener(v -> activateSensor(gyroscope, "Jiroskop aktif!"));
        btnHumidity.setOnClickListener(v -> activateSensor(humidity, "Nem Sensörü aktif!"));
        btnLight.setOnClickListener(v -> activateSensor(light, "Işık Sensörü aktif!"));
        btnMagnometer.setOnClickListener(v -> activateSensor(magnetometer, "Manyetometre aktif!"));
        btnPressure.setOnClickListener(v -> activateSensor(pressure, "Basınç Sensörü aktif!"));
        btnProximity.setOnClickListener(v -> activateSensor(proximity, "Yakınlık Sensörü aktif!"));
        btnThermometer.setOnClickListener(v -> activateSensor(thermometer, "Sıcaklık Sensörü aktif!"));
    }

    private void activateSensor(Sensor sensor, String toastMessage) {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Sensör bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // İvmeölçer işlemleri
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // İvmeölçer verilerini işleyin
            Toast.makeText(MainActivity.this, "İvmeölçer: X=" + x + ", Y=" + y + ", Z=" + z, Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Manyetik alan (pusula) işlemleri
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // Manyetik alan verilerini işleyin
            Toast.makeText(MainActivity.this, "Manyetik Alan: X=" + x + ", Y=" + y + ", Z=" + z, Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Jiroskop işlemleri
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // Jiroskop verilerini işleyin
            Toast.makeText(MainActivity.this, "Jiroskop: X=" + x + ", Y=" + y + ", Z=" + z, Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            // Nem sensörü işlemleri
            float humidity = event.values[0];
            // Nem verisini işleyin
            Toast.makeText(MainActivity.this, "Nem: " + humidity + "%", Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Işık sensörü işlemleri
            float lightIntensity = event.values[0];
            // Işık yoğunluğunu işleyin
            Toast.makeText(MainActivity.this, "Işık Yoğunluğu: " + lightIntensity + " lux", Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            // Basınç sensörü işlemleri
            float pressure = event.values[0];
            // Basınç verisini işleyin
            Toast.makeText(MainActivity.this, "Basınç: " + pressure + " hPa", Toast.LENGTH_SHORT).show();
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            // Yakınlık sensörü işlemleri
            float distance = event.values[0];
            if (distance < event.sensor.getMaximumRange()) {
                // Nesne sensöre yakın
                Toast.makeText(MainActivity.this, "Nesne Yakın", Toast.LENGTH_SHORT).show();
            } else {
                // Nesne sensörden uzak
                Toast.makeText(MainActivity.this, "Nesne Uzak", Toast.LENGTH_SHORT).show();
            }
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            // Sıcaklık sensörü işlemleri
            float temperature = event.values[0];
            // Sıcaklık verisini işleyin
            Toast.makeText(MainActivity.this, "Sıcaklık: " + temperature + " °C", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Sensör doğruluğu değiştiğinde yapılacak işlem
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Sensör dinleyicisini kayıttan kaldırıyoruz
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sensör dinleyicisini yeniden kaydediyoruz
    }
}
