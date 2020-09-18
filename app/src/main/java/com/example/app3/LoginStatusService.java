package com.example.app3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class LoginStatusService extends Service {

    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        timer.schedule(new SocketTimer(), 0L, 10000L);

        return super.onStartCommand(intent, flags, startId);
    }

    static class SocketTimer extends TimerTask {
        @Override
        public void run() {
            try {
                Socket socket = new Socket("39.96.71.56", 8002);
//                Socket socket = new Socket("10.0.2.2", 8000);
                socket.setSoTimeout(2000);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(MainActivity.mPhone.getBytes("UTF-8"));
                outputStream.flush();
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
                        String[] resultStrings = new String(bytes, 0, n, "UTF-8")
                                .split("\n");
                        if (!resultStrings[0].equals("40")){
                            if(resultStrings.length >= 3){
                                ActivityCollector.showDialog(resultStrings[1], resultStrings[2], resultStrings[3]);

                            }
                        }
                        break;
                    }
                }
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Socket socket = new Socket("39.96.71.56", 8002);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((MainActivity.mPhone + "\n" + "1").getBytes("UTF-8"));
                    outputStream.flush();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
