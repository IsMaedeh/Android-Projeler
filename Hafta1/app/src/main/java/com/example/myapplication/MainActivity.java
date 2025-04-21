package com.example.myapplication;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    MaterialButton buttonC;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);

        assignId(buttonC, R.id.buttonC);
        assignId(buttonDivide, R.id.buttonDivide);
        assignId(buttonMultiply, R.id.buttonMultiply);
        assignId(buttonPlus, R.id.buttonPlus);
        assignId(buttonMinus, R.id.buttonMinus);
        assignId(buttonEquals, R.id.buttonEquals);
        assignId(button0, R.id.button0);
        assignId(button1, R.id.button1);
        assignId(button2, R.id.button2);
        assignId(button3, R.id.button3);
        assignId(button4, R.id.button4);
        assignId(button5, R.id.button5);
        assignId(button6, R.id.button6);
        assignId(button7, R.id.button7);
        assignId(button8, R.id.button8);
        assignId(button9, R.id.button9);
        assignId(buttonDot, R.id.buttonDot);
    }


    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener((View.OnClickListener) this);
    }

//    @Override
//    public void onClick(View view) {
//        MaterialButton button = (MaterialButton) view;
//        String buttonText = button.getText().toString();
//        textView.setText(buttonText);
//
//        if (buttonText.equals("C")) {
//            textView.setText(""); // Clears the display
//        } else {
//            textView.append(buttonText); // Adds the number or operator to the display
//        }
//
//    }

    @Override
    public void onClick(View view) {
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = textView.getText().toString();

        if(buttonText.equals("=")) {
            textView.setText(textView.getText());
            return;
        }
        if(buttonText.equals("C")){
            textView.setText("");
            return;
        }else{
            dataToCalculate = dataToCalculate+buttonText;
        }
        textView.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if(!finalResult.equals("Err")){
            textView.setText(finalResult);
        }

    }

    String getResult(String data){
        try{
            Context context  = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult =  context.evaluateString(scriptable,data,"Javascript",1,null).toString();
            if(finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0","");
            }
            return finalResult;
        }catch (Exception e){
            return "Err";
        }
    }

}













