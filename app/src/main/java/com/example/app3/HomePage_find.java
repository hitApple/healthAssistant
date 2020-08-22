package com.example.app3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage_find extends AppCompatActivity {

    private RelativeLayout homepage;
    private RelativeLayout find;
    private RelativeLayout plus;
    private RelativeLayout link;
    private RelativeLayout me;
    private ImageView homepage_find_searchicon;
    private EditText homepage_find_search;
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_find);
/*        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
   //     findViewById(R.id.full).getLayoutParams().width= (int) (width*0.9F);
 //       findViewById(R.id.qwerty).getLayoutParams().height= (int) (height*0.6F);
*//*
        findViewById(R.id.qwerty).getLayoutParams().width= (int) (width*0.6F);
        findViewById(R.id.xinagmu1).getLayoutParams().width= (int) (width*0.7F);
        findViewById(R.id.homepage_find_bottom).getLayoutParams().width= (int) (width*0.9F);

*//*

        findViewById(R.id.homepage).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.homepage).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.find).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.find).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.plus).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.plus).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.link).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.link).getLayoutParams().height= (int) (width/7);
        findViewById(R.id.me).getLayoutParams().width= (int) (width/7);
        findViewById(R.id.me).getLayoutParams().height= (int) (width/7);*/





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

        homepage.setBackgroundColor(-1);
        find.setBackgroundColor(-3355444);
        link.setBackgroundColor(-1);
        me.setBackgroundColor(-1);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,HomePage.class));
/*                homepage.setBackgroundColor(-3355444);
                find.setBackgroundColor(-1);
                link.setBackgroundColor(-1);
                me.setBackgroundColor(-1);*/

            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    /*          startActivity(new Intent(HomePage_find.this,HomePage_find.class));*/
/*                homepage.setBackgroundColor(-1);
                find.setBackgroundColor(-3355444);
                link.setBackgroundColor(-1);
                me.setBackgroundColor(-1);*/

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
/*                homepage.setBackgroundColor(-1);
                find.setBackgroundColor(-1);
                link.setBackgroundColor(-3355444);
                me.setBackgroundColor(-1);*/

            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,me.class));
/*                homepage.setBackgroundColor(-1);
                find.setBackgroundColor(-1);
                link.setBackgroundColor(-1);
                me.setBackgroundColor(-3355444);*/

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
    /**
     * 退出程序
     */
    long boo = 0;
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - boo) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                boo = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

}
