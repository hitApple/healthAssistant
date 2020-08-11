package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button SignIn;
    private Button LogIn;
    private TextView toLogIn ;
    private ImageView eyeview_on;
    private ImageView eyeview_off;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        importDatabases();

        List<HospitalWeb> list = LitePal.findAll(HospitalWeb.class);
        Log.d(TAG, "hospitalName: " + list.get(0).getName());

        SignIn = (Button)findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.Log_In).setVisibility(View.GONE);
                findViewById(R.id.SignInUI).setVisibility(View.VISIBLE);
            }
        });
        LogIn = (Button)findViewById(R.id.LogIn);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,health_checkup.class);
                startActivity(it);
            }
        });

        toLogIn = (TextView) findViewById(R.id.toLogIn);
        toLogIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.SignInUI).setVisibility(View.GONE);
                findViewById(R.id.Log_In).setVisibility(View.VISIBLE);
            }
        });
        password = (EditText) findViewById(R.id.password);
        eyeview_on = (ImageView) findViewById(R.id.eyeview_on);
        eyeview_off = (ImageView) findViewById(R.id.eyeview_off);
        eyeview_on.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.eyeview_on).setVisibility(View.GONE);
                findViewById(R.id.eyeview_off).setVisibility(View.VISIBLE);
                password.setInputType(0x81);//密码不可见
            }
        });
        eyeview_off.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.eyeview_off).setVisibility(View.GONE);
                findViewById(R.id.eyeview_on).setVisibility(View.VISIBLE);
                password.setInputType(0x90);//密码可见
            }
        });

        LogIn = findViewById(R.id.LogIn);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,HomePage_find.class) ;
                startActivity(in);
            }
        });

        Button button = (Button) findViewById(R.id.jumpToBaiduMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactBaiduMap.class));
            }
        });

    }

    private void importDatabases(){
        File file = new File("/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + "com.example.app3" + "/databases" + "/HospitalWebsite.db");
        if (!file.isFile()){
            new DBManager(this).openDatabase();
        }

    }

}