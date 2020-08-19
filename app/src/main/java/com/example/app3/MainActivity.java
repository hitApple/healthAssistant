package com.example.app3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button SignIn;
    private Button LogIn;

    private ImageView eyeview_on;
    private ImageView eyeview_off;
    private EditText password;
    private EditText user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        importDatabases();

        List<HospitalWeb> list = LitePal.findAll(HospitalWeb.class);
        Log.d(TAG, "hospitalName: " + list.get(0).getName());

        user = findViewById(R.id.user);
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
                String[] result = null;
                try {
                    LoginClient loginClient = new LoginClient();
                    loginClient.sendLogin(user.getText().toString(), password.getText().toString());
                    long beginTime = System.currentTimeMillis();
                    while (!loginClient.isFinish()){
                        if (System.currentTimeMillis() - beginTime > 5000){
                            Toast.makeText(MainActivity.this, "连接超时，请重试",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    result = loginClient.getResultString().split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (Integer.parseInt(result[1]) != 0){
                    Toast.makeText(MainActivity.this, result[0], Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, result[0], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    intent.putExtra("phone", user.getText().toString());
                    startActivity(intent);
                }
            }
        });

        //注册
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,Sign_In.class));
            }
        });

        /*********************************测试用button**************************************/
        Button button1 = (Button) findViewById(R.id.jumpToBaiduMap);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactBaiduMap.class));
            }
        });
        Button button2 = (Button) findViewById(R.id.jumpToCalorieView);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CalorieView.class));
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


    private static final String[] memberPhones = {
            "15689712036", "19861807360", "13184116753","15689711359","13954159704"
    };

    private void memberWelcome(String phone) throws InterruptedException {
        for (String string : memberPhones){
            if (phone.equals(string)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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