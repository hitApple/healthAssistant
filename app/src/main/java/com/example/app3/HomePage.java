package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private ImageView homepage;
    private ImageView find;
    private ImageView plus;
    private ImageView link;
    private ImageView me;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,HomePage.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,HomePage_find.class));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.GONE);
                findViewById(R.id.plus).setVisibility(View.VISIBLE);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,link.class));
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,me.class));
            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.plus).setVisibility(View.GONE);
            }
        });

        Intent intent = getIntent();
        try {
            memberWelcome(intent.getStringExtra("phone"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private static final String[] memberPhones = {
            "15689712036", "19861807360", "13184116753","15689711359","13954159704"
    };

    private void memberWelcome(String phone) throws InterruptedException {
        if (phone == null){
            return;
        }
        for (String string : memberPhones){
            if (phone.equals(string)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomePage.this);
                dialog.setTitle("欢迎！");
                dialog.setMessage("检测到您为健康助手的测试人员\n本程序在这里诚挚的欢迎您!\n感谢您为健康助手所做的突出贡献!");
                dialog.setCancelable(true);
                dialog.setPositiveButton("谢谢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;
            }
        }
    }
}
