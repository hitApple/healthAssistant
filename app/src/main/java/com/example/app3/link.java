package com.example.app3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class link extends AppCompatActivity {
    private ImageView homepage;
    private ImageView find;
    private ImageView plus;
    private ImageView link;
    private ImageView me;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        findViewById(R.id.homepage).getLayoutParams().width= width/7;
        findViewById(R.id.homepage).getLayoutParams().height= width/7;
        findViewById(R.id.find).getLayoutParams().width= width/7;
        findViewById(R.id.find).getLayoutParams().height= width/7;
        findViewById(R.id.plus).getLayoutParams().width= width/7;
        findViewById(R.id.plus).getLayoutParams().height= width/7;
        findViewById(R.id.link).getLayoutParams().width= width/7;
        findViewById(R.id.link).getLayoutParams().height= width/7;
        findViewById(R.id.me).getLayoutParams().width= width/7;
        findViewById(R.id.me).getLayoutParams().height= width/7;

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(link.this,HomePage.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(link.this,HomePage_find.class));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(link.this,plus.class));
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(link.this,link.class));
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(link.this,me.class));
            }
        });
    }
}
