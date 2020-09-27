package com.example.app3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.constants.ZegoAECMode;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoUser;

public class VideoCall extends BaseActivity {

    String[] permissionNeeded = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};



    private static final long appID = 2403036570L;  // 请通过官网注册获取，格式为 123456789L
    private static final String appSign = "9964d2ed05c7194043660b97a15b2aa85706c64f6bd6d5256c1dfd524f5efa4c";
    private static ZegoExpressEngine engine;

    private String userID = null;
    private String room = null;
    private String stream = null;
    private String stream2 = null;
    private boolean recv = false;

    private ImageView muteButton;
    private ImageView exitButton;
    private EditText videoDescription;
    private ImageView changeButton;

    private boolean useFrontCamera = true;
    private boolean isMute = false;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        engine = ZegoExpressEngine.createEngine(appID, appSign, true, ZegoScenario.GENERAL,
                getApplication(), null);
        setContentView(R.layout.video_call);

        getPermissions();

        try {
            Intent intent = getIntent();
            userID = intent.getStringExtra("user");
            room = intent.getStringExtra("room");
            stream = intent.getStringExtra("stream1");
            stream2 = intent.getStringExtra("stream2");
            recv = intent.getBooleanExtra("recv", false);
        } catch (Exception e) {
            finish();
            e.printStackTrace();
            Toast.makeText(VideoCall.this, "遇到未知错误，请稍后重试", Toast.LENGTH_LONG)
                    .show();
            
            return;
        }

        PowerManager manager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.FULL_WAKE_LOCK ,
                getClass().getName());
        videoDescription = findViewById(R.id.video_call_description);

        engine.setEventHandler(new IZegoEventHandler(){
            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                super.onRoomUserUpdate(roomID, updateType, userList);
            }
        });
        engine.enableAEC(true);
        engine.enableHeadphoneAEC(true);
        engine.setAECMode(ZegoAECMode.MEDIUM);

        final Button submitButton = findViewById(R.id.description_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoDescription.getText().toString().trim().equals("")){
                    Toast.makeText(VideoCall.this, "您还没有输入描述信息！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ZegoUser zegoUser = new ZegoUser(userID);
                    engine.loginRoom(room, zegoUser);
                    engine.startPublishingStream(stream);
                    engine.startPreview(new ZegoCanvas(findViewById(R.id.my_camera)));
                    engine.startPlayingStream(stream2,
                            new ZegoCanvas(findViewById(R.id.other_side_camera)));
                    exitButton.setVisibility(View.VISIBLE);
                    changeButton.setVisibility(View.VISIBLE);
                    muteButton.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.GONE);
                    videoDescription.setVisibility(View.GONE);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Socket socket = new Socket("39.96.71.56", 8002);
//                                Socket socket = new Socket("10.0.2.2", 8000);
                                OutputStream outputStream = socket.getOutputStream();
                                outputStream.write((userID + "\n"
                                        + stream2.substring(7) + "\n"
                                        + videoDescription.getText().toString())
                                        .getBytes("UTF-8"));
                                outputStream.flush();
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });

        muteButton = findViewById(R.id.mute);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMute){
                    engine.muteMicrophone(false);
                    muteButton.setImageResource(R.drawable.mute_off);
                    isMute = false;
                } else {
                    engine.muteMicrophone(true);
                    muteButton.setImageResource(R.drawable.mute_on);
                    isMute = true;
                }
            }
        });

        exitButton = findViewById(R.id.exit_video);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changeButton = findViewById(R.id.change_camera);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useFrontCamera){
                    engine.useFrontCamera(false);
                    changeButton.setImageResource(R.drawable.camera_behind);
                    useFrontCamera = false;
                } else {
                    engine.useFrontCamera(true);
                    changeButton.setImageResource(R.drawable.camera_front);
                    useFrontCamera = true;
                }
            }
        });
        if (recv){
            videoDescription.setVisibility(View.GONE);
            ZegoUser zegoUser = new ZegoUser(userID);
            engine.loginRoom(room, zegoUser);
            engine.startPublishingStream(stream);
            engine.startPreview(new ZegoCanvas(findViewById(R.id.my_camera)));
            engine.startPlayingStream(stream2,
                    new ZegoCanvas(findViewById(R.id.other_side_camera)));
            exitButton.setVisibility(View.VISIBLE);
            changeButton.setVisibility(View.VISIBLE);
            muteButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire(30*60*1000L /*30 minutes*/);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWakeLock.isHeld()){
            mWakeLock.release();
        }
//        finish();
    }

    @Override
    protected void onDestroy() {
        engine.stopPreview();
        engine.stopPlayingStream(stream);
        engine.logoutRoom(room);
        engine.stopPublishingStream();
        ZegoExpressEngine.destroyEngine(null);
        super.onDestroy();
    }


    private void getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissionNeeded, 101);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Fail to request permission", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }
    }

}