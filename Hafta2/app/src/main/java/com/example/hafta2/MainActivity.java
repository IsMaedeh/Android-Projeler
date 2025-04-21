package com.example.hafta2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] sehirler = {"Istanbul", "Ankara", "Izmir", "Bursa", "Antalya", "Konya",
            "Adana", "Trabzon", "Gaziantep", "Kayseri"};
    String[] plakalar = {"34", "06", "35", "16", "07", "42", "01", "61", "27", "38"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list1 = findViewById(R.id.list1);
        ListView list2 = findViewById(R.id.list2);

        // Plaka listesini karıştır
        List<String> plakaListesi = new ArrayList<>(Arrays.asList(plakalar));
        Collections.shuffle(plakaListesi);

        // Adaptörleri oluştur ve ata
        ArrayAdapter<String> sehirAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sehirler);
        ArrayAdapter<String> plakaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, plakaListesi);

        list1.setAdapter(sehirAdapter);
        list2.setAdapter(plakaAdapter);

        list1.setOnItemClickListener((adapterView, view, i, l) -> {
            String secilenSehir = sehirler[i];
            String dogruPlaka = plakalar[i];
            String rastgelePlaka = plakaListesi.get(i);

            String durum = dogruPlaka.equals(rastgelePlaka) ? "Doğru ✅" : "Yanlış ❌";

            Toast.makeText(this,
                    secilenSehir + " - " + dogruPlaka + " - " + rastgelePlaka + " - " + durum,
                    Toast.LENGTH_SHORT).show();

            if (durum == "Doğru") {
                // Yeni sayfaya geçiş
                Intent intent = new Intent(this, ekraniki.class);
                intent.putExtra("dogruPlaka", dogruPlaka);
                intent.putExtra("durum", durum);
                startActivity(intent);
            }

        });
    }
}
