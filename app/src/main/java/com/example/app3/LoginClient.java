package com.example.app3;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LoginClient {


    private static final String PASS_USER_MATCH = "用户验证通过，欢迎！";
    private static final String ERROR_USER_NO_EXIST = "该手机号还未注册！";
    private static final String ERROR_PASSWORD_NO_MATCH = "密码不匹配！请核对密码后重试";

    private static final String HOST = "39.96.71.56";
    private static final int PORT = 8001;

    private boolean finish = false;
    private String resultString = null;
    /**
     * 用户点击登录时调用的方法，用于检测是否登录成功，用split方法分离返回的字符串，第一个元素为信息，第二个
     * 为信息代码，返回0说明正确，返回其他值说明错误
     * @param phone 用户登录时填入的手机号
     * @param password 用户登录时填入的密码
     * @throws IOException
     */
    public void sendLogin(final String phone, final String password) throws IOException{
        final String[][] resultString1 = new String[1][2];
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
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
                                resultString1[0] = returnCodeToString(code).split("\n");
                                resultString = resultString1[0][0] + "\n" + resultString1[0][1];
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
//        Socket socket = new Socket("127.0.0.1", 8000);
    }

    /**
     * 内部方法，用于把从服务器接收到的代码转换为对应信息，无需额外调用
     * @param code 服务器传来的返回信息代码
     * @return 对应的服务器返回信息字符串+信息代码
     */
    private static String returnCodeToString(int code){
        switch (code){
            case 30:
                return PASS_USER_MATCH + "\n" + 0;
            case 31:
                return ERROR_USER_NO_EXIST + "\n" + 1;
            case 32:
                return ERROR_PASSWORD_NO_MATCH + "\n" + 2;
            default:
                return "UNKNOWN ERROR!" + "\n" + 9;
        }
    }

    public boolean isFinish() {
        return finish;
    }

    public String getResultString() {
        return resultString;
    }

    /****************************************用于测试**********************************************/

    //    public static void main(String[] args) throws IOException {
//        System.out.println(sendLogin("15689712036", "yu74174112"));
//
//    }

    /****************************************用于测试**********************************************/
}
