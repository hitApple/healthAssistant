package com.example.app3;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        boolean hasStopService = false;
        for(Activity activity : activities){
            if (!activity.isFinishing()){
                if (!hasStopService){
                    activity.stopService(new Intent(activity, LoginStatusService.class));
                    hasStopService = true;
                }
                activity.finish();
            }
        }
    }

    public static void showDialog(final String sendPhone, final String recvPhone, final String description){
        final Activity activity = BaseApp.getActivity();
        if (activity.getLocalClassName().equals("MainActivity")){
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("视频通话邀请");
                dialog.setMessage("用户" + sendPhone + "想邀请您参与视频通话，" + "原因是：\n" + description
                        + "是否接受?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, VideoCall.class);
                        intent.putExtra("user", recvPhone);
                        intent.putExtra("room", "room_" + sendPhone);
                        intent.putExtra("stream1", "stream_" + recvPhone);
                        intent.putExtra("stream2", "stream_" + sendPhone);
                        activity.startActivity(intent);
                    }
                });
                dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
    }


}
