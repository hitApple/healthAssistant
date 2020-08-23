package com.example.app3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class health_checkup extends BaseActivity {

    private Button submit;
    private TextView user_time;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_checkup);

        submit = findViewById(R.id.healthcheckup_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交信息
            }
        });

        user_time = findViewById(R.id.user_time);
        user_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.datepicker).setVisibility(View.VISIBLE);

            }
        });

    }


}

