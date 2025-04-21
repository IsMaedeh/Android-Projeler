package com.example.hafta5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private ListView listView;
    private Button buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listView);
        buttonReturn = findViewById(R.id.buttonReturn);

        String editTextValue = getIntent().getStringExtra("editTextValue");
        long elapsedTimeInSeconds = getIntent().getLongExtra("elapsedTimeInSeconds", 0);

        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
        HashMap<String, String> data = new HashMap<>();
        data.put("editText", editTextValue);
        data.put("elapsedTimeInSeconds", String.valueOf(elapsedTimeInSeconds) + " sn");
        dataList.add(data);

        // Define the columns and their mappings
        String[] from = {"editText", "elapsedTimeInSeconds"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        // Create and set the adapter for the ListView
        SimpleAdapter adapter = new SimpleAdapter(this, dataList, android.R.layout.simple_list_item_2, from, to);
        listView.setAdapter(adapter);

        // Button click listener to return to MainActivity
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to MainActivity
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
