package com.example.hafta2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView list1 = (ListView) findViewById(R.id.list1);
        ListView list2 = (ListView) findViewById(R.id.list2);

        String[] sehirler = {"Istanbul", "Ankara", "Izmir", "Bursa", "Antalya", "Konya", "Adana", "Trabzon", "Gaziantep", "Kayseri"};
        String[] plakalar = {"34", "06", "35", "16", "07", "42", "01", "61", "27", "38"};
        // Diziyi listeye çevir ve karıştır
        List<String> plakaListesi = Arrays.asList(plakalar);
        Collections.shuffle(plakaListesi);




        ArrayAdapter<String> sehirlistesi = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, sehirler);

        ArrayAdapter<String> plakalistesi = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, plakaListesi);


        list1.setAdapter(sehirlistesi);
        list2.setAdapter(plakalistesi);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Seçilen şehri al
                String secilenSehir = sehirlistesi.getItem(i);
                // Toast.makeText(MainActivity.this, secilenSehir, Toast.LENGTH_LONG).show();

                // Doğru plaka ve rastgele plaka değişkenleri
                String orijinalPlaka = "";
                String rastgelePlaka = plakaListesi.get(i);
                String durum = "";

                for (int j = 0; j < sehirler.length; j++) {
                    if (sehirler[j].equals(sehirlistesi.getItem(i))){
                        //String sehir = sehirler[j];
                        orijinalPlaka = plakalar[j];
                        //rastgelePlaka = plakalistesi.getItem(i);

                        // Doğru veya Yanlış kontrolü
                        durum = orijinalPlaka.equals(rastgelePlaka) ? "Doğru ✅" : "Yanlış ❌";

                        break; // Doğru eşleşme bulunduğunda döngüden çık
                    }

                }
                Toast.makeText(MainActivity.this, secilenSehir + " - " + orijinalPlaka + " - " + rastgelePlaka + " - " + durum, Toast.LENGTH_SHORT).show();

//                // İkinci sayfaya veri gönderme
                  Intent intent = new Intent(getApplicationContext(), ekraniki.class);
//                intent.putExtra("dogruPlaka", orijinalPlaka);
//                intent.putExtra("durum", durum);
                  startActivity(intent);
            }
        });
    }
}

