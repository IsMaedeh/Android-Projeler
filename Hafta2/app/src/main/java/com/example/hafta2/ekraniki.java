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

public class ekraniki extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ekran2);

        TextView text = (TextView) findViewById(R.id.textView);
        // Intent'ten veriyi al
        Intent intent = getIntent();
        String durum = intent.getStringExtra("durum");

        // Veriyi TextView'e yazdır
        text.setText(durum);



    }
}