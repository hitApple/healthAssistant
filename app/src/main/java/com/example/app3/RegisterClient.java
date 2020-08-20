package com.example.app3;

import android.util.Log;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RegisterClient {

    private static final String TAG = "RegisterClient";

    //服务器对应信息字符串
    private static final String PASS_NO_EXISTED = "成功发送验证码！请注意查收";
    private static final String ERROR_EXISTED = "该手机号已被注册";
    private static final String PASS_CODE_MATCH = "注册成功！";
    private static final String ERROR_NO_CODE_MATCH = "验证码错误，请核对后输入！";
    private static final String ERROR_TIMES_OUT = "验证码尝试次数达最大，请重新发送验证码";
    private static final String ERROR_NO_EXISTED = "您可能还未发送验证码，或服务器还未更新，请核对后再试";

    //地址和端口号
    private static final String HOST = "39.96.71.56";
    private static final int PORT = 8000;

    private String resultString1;
    private String resultString2;
    private boolean finish = false;

    /**
     * 用于点击按钮“发送验证码”时调用，用split方法分离返回的字符串，第一个元素为信息，第二个
     * 为信息代码，返回0说明正确，返回其他值说明错误
     * @param phone 注册时填的电话号
     * @return 服务器返回的信息，字符串类型，直接显示给用户即可
     * @throws IOException
     */
    public void sendSms(final String phone) throws IOException{
        final String[][] tempStrings = new String[1][2];
        new Thread(){
            @Override
            public void run() {
                super.run();
               try{
                   Socket socket = new Socket(HOST, PORT);
                   socket.setSoTimeout(5000);
                   OutputStream outputStream = socket.getOutputStream();
                   outputStream.write((phone).getBytes("UTF-8"));
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
                    Socket socket = new Socket("39.96.71.56", 8000);
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
    }

    /**
     * 内部方法，用于把从服务器接收到的代码转换为对应信息，无需额外调用
     * @param code 服务器传来的返回信息代码
     * @return 对应的服务器返回信息字符串+信息代码
     */
    private static String returnCodeToString(int code){
        switch (code){
            case 10:
                return PASS_NO_EXISTED + "\n" + 0;
            case 11:
                return ERROR_EXISTED + "\n" + 1;
            case 20:
                return PASS_CODE_MATCH + "\n" + 0;
            case 21:
                return ERROR_NO_EXISTED + "\n" + 1;
            case 22:
                return ERROR_TIMES_OUT + "\n" + 2;
            case 23:
                return ERROR_NO_CODE_MATCH + "\n" + 3;
            default:
                return "UNKNOWN ERROR!" + "\n" + 9;
        }
    }

    public String getResultString1() {
        return resultString1;
    }

    public String getResultString2() {
        return resultString2;
    }

    public boolean isFinish() {
        return finish;
    }

    /****************************************用于测试**********************************************/

//    public static void main(String[] args) throws IOException {
//        System.out.println(sendSms("15689712036"));
//        System.out.println(sendCode("15689712036", "yu74174112", "390659"));
//    }

    /****************************************用于测试**********************************************/
}
