package com.example.app3;

import android.app.Activity;
import android.content.Intent;

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

}
