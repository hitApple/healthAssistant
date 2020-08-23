package com.example.app3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoUser;

public class VideoCall extends BaseActivity {

    String[] permissionNeeded = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};



    private static final long appID = 3774117024L;  // 请通过官网注册获取，格式为 123456789L
    private static final String appSign = "7a7110bdb45b750adc3c16848b4b5bfa78a5be44df35b3417d38d2c3c66286b4";
    private static ZegoExpressEngine engine;

    private String userID = null;
    private String room = null;
    private String stream = null;
    private String stream2 = null;

    private Button muteButton;
    private Button microButton;

    private boolean useFrontCamera = true;

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
        } catch (Exception e) {
            finish();
            e.printStackTrace();
            return;
        }


        engine.setEventHandler(new IZegoEventHandler(){
            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                super.onRoomUserUpdate(roomID, updateType, userList);
            }
        });



        Button startButton = findViewById(R.id.start_video);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZegoUser user = new ZegoUser(userID);
                engine.loginRoom(room, user);
                engine.startPublishingStream(stream);
                engine.startPreview(new ZegoCanvas(findViewById(R.id.my_camera)));
                engine.startPlayingStream(stream2,
                        new ZegoCanvas(findViewById(R.id.other_side_camera)));

            }
        });

        muteButton = findViewById(R.id.mute);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engine.muteMicrophone(false );
                muteButton.setVisibility(View.GONE);
                microButton.setVisibility(View.VISIBLE);
            }
        });

        microButton = findViewById(R.id.micro);
        microButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engine.muteMicrophone(true);
                muteButton.setVisibility(View.VISIBLE);
                microButton.setVisibility(View.GONE);
            }
        });

        Button changeButton = findViewById(R.id.change_camera);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useFrontCamera){
                    engine.useFrontCamera(false);
                    useFrontCamera = false;
                } else {
                    engine.useFrontCamera(true);
                    useFrontCamera = true;
                }
            }
        });

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