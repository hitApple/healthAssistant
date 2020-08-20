package com.example.app3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage_find extends AppCompatActivity {
    private ImageView homepage;
    private ImageView find;
    private ImageView plus;
    private ImageView link;
    private ImageView me;
    private ImageView homepage_find_searchicon;
    private EditText homepage_find_search;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_find);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
   //     findViewById(R.id.full).getLayoutParams().width= (int) (width*0.9F);
 //       findViewById(R.id.qwerty).getLayoutParams().height= (int) (height*0.6F);
/*
        findViewById(R.id.qwerty).getLayoutParams().width= (int) (width*0.6F);
        findViewById(R.id.xinagmu1).getLayoutParams().width= (int) (width*0.7F);
        findViewById(R.id.homepage_find_bottom).getLayoutParams().width= (int) (width*0.9F);

*/

        findViewById(R.id.homepage).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.homepage).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.find).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.find).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.plus).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.plus).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.link).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.link).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.me).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.me).getLayoutParams().height= (int) (width/7);





        homepage_find_searchicon = findViewById(R.id.homepage_find_searchicon);
        homepage_find_searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //搜索
            }
        });

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,HomePage.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(HomePage_find.this,HomePage_find.class));*/
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.GONE);
                findViewById(R.id.plus_ui).setVisibility(View.VISIBLE);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,link.class));
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,me.class));
            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.plus_ui).setVisibility(View.GONE);
            }
        });
    }

}
