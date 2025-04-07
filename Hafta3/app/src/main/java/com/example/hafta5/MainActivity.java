package com.example.hafta5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private long startTime = 0;
    public int oran1 = 0, oran2 = 0, oran3 = 0;

    private final String[] cities = {"Istanbul", "Ankara", "Izmir"};
    private final int[] plateNumbers = {34, 6, 35};  // License plates for Istanbul, Ankara, Izmir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt = (TextView) findViewById(R.id.textView);
        Button basla = (Button) findViewById(R.id.buttonStart);
        Button onayla = (Button) findViewById(R.id.buttonConfirm);

        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);

        EditText editText = (EditText) findViewById(R.id.editText);

        //sb
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sb.setMax(81);
                int oran = android.graphics.Color.rgb(oran3, oran2, i);
                txt.setText(String.valueOf(i));
                oran1 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                startTime = System.currentTimeMillis();

            }
        });

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editText.getText().toString().trim(); // Get the city name
                int seekBarValue = sb.getProgress();

                // Check if the city entered matches the plate number
                boolean isValidCity = false;
                int correctPlateNumber = 0;
                for (int i = 0; i < cities.length; i++) {
                    if (userInput.equalsIgnoreCase(cities[i])) {
                        isValidCity = true;
                        correctPlateNumber = plateNumbers[i];
                        break;
                    }
                }

                // If city is valid and SeekBar value matches the plate number
                if (isValidCity && seekBarValue == correctPlateNumber) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    long elapsedTimeInSeconds = elapsedTime / 1000;  // Convert milliseconds to seconds
                    startTime = 0;

                    // Pass the data to MainActivity2
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("editTextValue", userInput);  // Pass the city name
                    intent.putExtra("elapsedTimeInSeconds", elapsedTimeInSeconds);  // Pass the elapsed time in seconds
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Yanlis", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
