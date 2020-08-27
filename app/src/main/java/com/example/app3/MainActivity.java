package com.example.app3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {    private static final String TAG = "MainActivity";
    public static String mPhone = "15689712036";;
    private Button SignIn;
    private Button LogIn;

    private ImageView eyeview_on;
    private ImageView eyeview_off;
    private EditText password;
    private EditText user;
    private CheckBox rememberPassword;
    private CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        importDatabases();
        Connector.getDatabase();

        user = findViewById(R.id.user);
        password =  findViewById(R.id.password);

//        Log.d(TAG, "onCreate: " + "MainActivity".equals(this.getLocalClassName()));

        SharedPreferences preferences = getSharedPreferences("user_info",   MODE_PRIVATE);
        user.setText(preferences.getString("user",""));
        password.setText(preferences.getString("password", ""));

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
                startLogin();
            }
        });

        //注册
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,Sign_In.class));
            }
        });

        //checkbox
        rememberPassword = findViewById(R.id.remember_password);
        autoLogin = findViewById(R.id.auto_login);

        if (preferences.getBoolean("check_remember", false)){
            rememberPassword.setChecked(true);
        } else {
            rememberPassword.setChecked(false);
        }

        if (preferences.getBoolean("check_auto", false)){
            autoLogin.setChecked(true);
        } else {
            autoLogin.setChecked(false);
        }

        if (preferences.getBoolean("auto", false)){
            startLogin();
        }

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

    private void saveUserInfo(){
        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        editor.putString("user", user.getText().toString());
        if (rememberPassword.isChecked()){
            editor.putString("password", password.getText().toString());
            editor.putBoolean("check_remember", true);
        } else {
            editor.putString("password", "");
            editor.putBoolean("check_remember", false);
        }
        if (autoLogin.isChecked()){
            editor.putBoolean("auto", true);
            editor.putBoolean("check_auto", true);
        } else {
            editor.putBoolean("auto", false);
            editor.putBoolean("check_auto", false);
        }
        editor.apply();
    }

    private void startLogin(){
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
            saveUserInfo();
            mPhone = user.getText().toString();
            startService(new Intent(MainActivity.this, LoginStatusService.class));
            Toast.makeText(MainActivity.this, result[0], Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomePage_find.class);
            intent.putExtra("phone", user.getText().toString());
            finish();
            startActivity(intent);

        }
    }

}