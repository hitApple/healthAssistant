package com.example.app3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class plus  extends BaseActivity{
    private ImageView healthcheckup;
    private ImageView healthreport;
    private ImageView symptomconsultation;
    private ImageView symptomprediction;
    private Button cross;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus);

        healthcheckup = findViewById(R.id.healthcheckup);
        healthreport = findViewById(R.id.healthreport);
        symptomconsultation = findViewById(R.id.symptomconsultation);
        symptomprediction = findViewById(R.id.symptomprediction);
        cross = findViewById(R.id.cross);
        healthcheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        healthreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        symptomconsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        symptomprediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
