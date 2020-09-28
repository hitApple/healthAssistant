package com.example.app3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class FindPassword extends BaseActivity {

    //服务器对应信息字符串
    private static final String PASS_PHONE_EXIST = "成功发送验证码！请注意查收";
    private static final String ERROR_PHONE_FORMAT = "您输入的手机号格式不正确";
    private static final String ERROR_NO_EXIST = "该手机号还未注册，请先注册";
    private static final String PASS_FIND_MATCH = "重置密码成功！";
    private static final String ERROR_NO_FIND_MATCH = "验证码错误，请核对后输入！";
    private static final String ERROR_AGAIN_SEND = "您还未发送验证码，或验证码过期，请重新发送";



    //地址和端口号
    private static final String HOST = "39.96.71.56";
    private static final int PORT = 8004;

    private String resultString1;
    private String resultString2;
    private boolean finish = false;
    private EditText phone;
    private EditText password;
    private EditText password2;
    private EditText vCode;
    private boolean isSendSms = false;
    private boolean finish2 = false;
    private Timer timer;
    private Button findSendSmsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);

        password = findViewById(R.id.find_password);
        password2 = findViewById(R.id.find_password2);
        phone = findViewById(R.id.find_phone);
        vCode = findViewById(R.id.find_vCode);
        findSendSmsButton = findViewById(R.id.find_send_sms_button);

        findViewById(R.id.find_send_sms_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")){
                    Toast.makeText(FindPassword.this, "密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkPasswordFormat(password.getText().toString())){
                    return;
                }

                if (!(password.getText().toString()).equals(password2.getText().toString())){
                    Toast.makeText(FindPassword.this, "您两次输入的密码不一致！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] result = null;
                try {
                    sendSms(phone.getText().toString(), password.getText().toString());
                    long beginTime = System.currentTimeMillis();
                    while (!finish){
                        if (System.currentTimeMillis() - beginTime > 5000){
                            Toast.makeText(FindPassword.this, "连接超时，请重试",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    result = resultString1.split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (Integer.parseInt(result[1]) != 0){
                    Toast.makeText(FindPassword.this, result[0], Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(FindPassword.this, result[0], Toast.LENGTH_SHORT).show();
                    isSendSms = true;
                }
            }
        });

        findViewById(R.id.find_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSendSms){
                    Toast.makeText(FindPassword.this, "您还没有发送验证码！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] result = null;
                try {
                    String vCodeString = vCode.getText().toString();
                    StringBuilder builder = new StringBuilder();
                    if (vCodeString.length() < 6){
                        for (int i = 0; i < 6 - vCodeString.length(); i++){
                            builder.append("0");
                        }

                    }
                    builder.append(vCodeString);
                    sendCode(phone.getText().toString(),
                            password.getText().toString(), builder.toString());
                    long beginTime = System.currentTimeMillis();
                    while (!finish2){
                        if (System.currentTimeMillis() - beginTime > 5000){
                            Toast.makeText(FindPassword.this, "连接超时，请重试",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    result = resultString2.split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (Integer.parseInt(result[1]) != 0){
                    Toast.makeText(FindPassword.this, result[0], Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(FindPassword.this, result[0], Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindPassword.this, MainActivity.class));
                }
            }
        });

        findViewById(R.id.find_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 用于点击按钮“发送验证码”时调用，用split方法分离返回的字符串，第一个元素为信息，第二个
     * 为信息代码，返回0说明正确，返回其他值说明错误
     * @param phone 注册时填的电话号
     * @return 服务器返回的信息，字符串类型，直接显示给用户即可
     * @throws IOException
     */
    public void sendSms(final String phone, final String password) throws IOException{
        final String[][] tempStrings = new String[1][2];
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Socket socket = new Socket(HOST, PORT);
                    socket.setSoTimeout(5000);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((phone + "\n" + password).getBytes("UTF-8"));
                    outputStream.flush();
                    System.out.println(socket);

                    InputStream is = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    for (int i = 0; i < 3; i++) {
                        int n = -1;
                        try {
                            n = is.read(bytes);
                        } catch (Exception e) {
                            // TODO: handle exception
                            continue;
                        }
                        if(n != -1) {
                            try {
                                int code = Integer.parseInt(new String(bytes, 0, n, "UTF-8"));
                                tempStrings[0] = returnCodeToString(code).split("\n");
                                resultString1 = tempStrings[0][0] + "\n" + tempStrings[0][1];
                                finish = true;
                                break;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                    is.close();
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        findSendSmsButton.setEnabled(false);
        findSendSmsButton.setText("等待1分钟");
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
                FindPassword.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findSendSmsButton.setEnabled(true);
                        findSendSmsButton.setText("发送验证码");
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

    /**
     * 用于点击按钮“注册”时调用，（重点）必须保证用户已经点击过“发送验证码”才能调用本方法，否则可能报错（重点）
     * （重点）若点击“发送验证码”后返回的是错误信息，则不允许点击注册按钮（重点），用split方法分离返回的字符
     * 串，第一个元素为信息，第二个为信息代码，返回0说明正确，返回其他值说明错误
     * @param phone 注册时填的电话号
     * @param password 注册时填的密码
     * @param VCode 注册时填的验证码
     * @return 服务器返回的信息，字符串类型，直接显示给用户即可
     * @throws IOException
     */
    public void sendCode(final String phone, final String password, final String VCode) throws IOException{
        final String[][] tempStrings = new String[1][2];
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Socket socket = new Socket(HOST, PORT);
                    socket.setSoTimeout(500);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((phone + "\n" + password + "\n" + VCode).getBytes("UTF-8"));
                    outputStream.flush();
                    System.out.println(socket);

                    InputStream is = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    for (int i = 0; i < 3; i++) {
                        int n = -1;
                        try {
                            n = is.read(bytes);
                        } catch (Exception e) {
                            // TODO: handle exception
                            continue;
                        }
                        if(n != -1) {

                            try {
                                int code = Integer.parseInt(new String(bytes, 0, n, "UTF-8"));
                                tempStrings[0] = returnCodeToString(code).split("\n");
                                resultString2 = tempStrings[0][0] + "\n" + tempStrings[0][1];
                                finish2 = true;
                                break;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                    }
                    is.close();
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

//    private static final String PASS_PHONE_EXIST = "成功发送验证码！请注意查收";
//    private static final String ERROR_PHONE_FORMAT = "您输入的手机号格式不正确";
//    private static final String ERROR_NO_EXIST = "该手机号还未注册，请先注册";
//    private static final String PASS_FIND_MATCH = "重置密码成功！";
//    private static final String ERROR_NO_FIND_MATCH = "验证码错误，请核对后输入！";
//    private static final String ERROR_AGAIN_SEND = "您还未发送验证码，或验证码过期，请重新发送";

    /**
     * 内部方法，用于把从服务器接收到的代码转换为对应信息，无需额外调用
     * @param code 服务器传来的返回信息代码
     * @return 对应的服务器返回信息字符串+信息代码
     */
    private static String returnCodeToString(int code){
        switch (code){
            case 40:
                return PASS_PHONE_EXIST + "\n" + 0;
            case 41:
                return ERROR_PHONE_FORMAT + "\n" + 1;
            case 42:
                return ERROR_NO_EXIST + "\n" + 2;
            case 50:
                return PASS_FIND_MATCH + "\n" + 0;
            case 51:
                return ERROR_NO_FIND_MATCH + "\n" + 1;
            case 52:
                return ERROR_AGAIN_SEND + "\n" + 2;
            default:
                return "发生未知错误" + "\n" + 9;
        }
    }


    private boolean checkPasswordFormat(String password){
        if (password.length() < 8){
            Toast.makeText(FindPassword.this, "密码不能少于8位！", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(FindPassword.this, "密码必须包括字母和数字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class CountDownTimerMasker extends TimerTask {

        int countDown = 60;

        @Override
        public void run() {
            FindPassword.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDown--;
                    String smsText = "(" + countDown + ")秒";
                    findSendSmsButton.setText(smsText);
                }
            });
        }
    }
}