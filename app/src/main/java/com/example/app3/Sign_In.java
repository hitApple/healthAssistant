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
import java.util.Timer;
import java.util.TimerTask;

public class Sign_In extends BaseActivity {

    private EditText phone;
    private EditText password;
    private EditText password2;
    private EditText vCode;
    private TextView toLogIn;
    private Button userSignIn;
    private Button sendSms;
    private Boolean isSendSms = false;
    private Timer timer;

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

                if (password.getText().toString().equals("")){
                    Toast.makeText(Sign_In.this, "密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkPasswordFormat(password.getText().toString())){
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
                sendSms.setEnabled(false);
                sendSms.setText("等待1分钟");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                        Sign_In.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendSms.setEnabled(true);
                                sendSms.setText("发送验证码");
                            }
                        });
                        if (timer != null){
                            timer.cancel();
                        }
                    }
                }.start();

                timer = new Timer();
                timer.schedule(new CountDownTimerMasker(), 1000L, 1000L);
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

    private boolean checkPasswordFormat(String password){
        if (password.length() < 8){
            Toast.makeText(Sign_In.this, "密码不能少于8位！", Toast.LENGTH_SHORT).show();
            return false;
        }
        char[] passwordChars = password.toCharArray();
        boolean hasNumber = false;
        boolean hasLetter = false;
        for (char c : passwordChars){
            if (Character.isDigit(c)){
                hasNumber = true;
            }
            if (Character.isLetter(c)){
                hasLetter = true;
            }
        }
        if (!hasNumber || !hasLetter){
            Toast.makeText(Sign_In.this, "密码必须包括字母和数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class CountDownTimerMasker extends TimerTask{

        int countDown = 60;

        @Override
        public void run() {
            Sign_In.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDown--;
                    String smsText = "(" + countDown + ")秒";
                    sendSms.setText(smsText);
                }
            });
        }
    }

}