package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Sign_In extends BaseActivity {

    private EditText phone;
    private EditText password;
    private EditText password2;
    private EditText vCode;
    private TextView toLogIn;
    private Button userSignIn;
    private Button sendSms;
    private Boolean isSendSms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        phone = findViewById(R.id.user_sign_in);
        password = findViewById(R.id.user_sign_in_password);
        password2 = findViewById(R.id.user_sign_in_password2);
        vCode = findViewById(R.id.user_sign_in_vCode);

        //切换到登录界面
        toLogIn = findViewById(R.id.toLogIn);
        toLogIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Sign_In.this,MainActivity.class));
            }
        });

        sendSms = findViewById(R.id.send_sms_button);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password.getText().toString();
                if (password.getText().toString().equals("")){
                    Toast.makeText(Sign_In.this, "密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(password.getText().toString()).equals(password2.getText().toString())){
                    Toast.makeText(Sign_In.this, "您两次输入的密码不一致！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] result = null;
                try {
                    RegisterClient registerClient = new RegisterClient();
                    registerClient.sendSms(phone.getText().toString());
                    long beginTime = System.currentTimeMillis();
                    while (!registerClient.isFinish()){
                        if (System.currentTimeMillis() - beginTime > 5000){
                            Toast.makeText(Sign_In.this, "连接超时，请重试",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    result = registerClient.getResultString1().split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (Integer.parseInt(result[1]) != 0){
                    Toast.makeText(Sign_In.this, result[0], Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Sign_In.this, result[0], Toast.LENGTH_SHORT).show();
                    isSendSms = true;
                }
            }
        });

        //点击注册按钮
        userSignIn = findViewById(R.id.sign_in_button);
        userSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSendSms){
                    Toast.makeText(Sign_In.this, "您还没有发送验证码！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] result = null;
                try {
                    RegisterClient registerClient = new RegisterClient();
                    registerClient.sendCode(phone.getText().toString(),
                            password.getText().toString(), vCode.getText().toString());
                    long beginTime = System.currentTimeMillis();
                    while (!registerClient.isFinish()){
                        if (System.currentTimeMillis() - beginTime > 5000){
                            Toast.makeText(Sign_In.this, "连接超时，请重试",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    result = registerClient.getResultString2().split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (Integer.parseInt(result[1]) != 0){
                    Toast.makeText(Sign_In.this, result[0], Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Sign_In.this, result[0], Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Sign_In.this, MainActivity.class));
                }

            }
        });
    }
}