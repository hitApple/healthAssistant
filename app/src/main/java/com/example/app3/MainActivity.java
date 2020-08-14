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


        toLogIn = findViewById(R.id.toLogIn);
        toLogIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.SignInUI).setVisibility(View.GONE);
                findViewById(R.id.Log_In).setVisibility(View.VISIBLE);
            }
        });
        password =  findViewById(R.id.password);
        eyeview_on = findViewById(R.id.eyeview_on);
        eyeview_off = findViewById(R.id.eyeview_off);
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
        //登录按钮
        LogIn = findViewById(R.id.LogIn);
        LogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HomePage_find.class));
            }
        });
        //注册
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.Log_In).setVisibility(View.GONE);
                findViewById(R.id.SignInUI).setVisibility(View.VISIBLE);
            }
        });

        /*********************************测试用button**************************************/
        Button button = (Button) findViewById(R.id.jumpToBaiduMap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactBaiduMap.class));
            }
        });

        /*********************************测试用button**************************************/
    }


    /**
     * 将存储在assets文件夹中的数据库文件移动到相应位置
    **/
    private void importDatabases(){
        File file = new File("/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + "com.example.app3" + "/databases" + "/HospitalWebsite.db");
        if (!file.isFile()){
            new DBManager(this).openDatabase();
        }

    }

}