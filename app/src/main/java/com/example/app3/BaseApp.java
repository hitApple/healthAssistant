package com.example.app3;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseApp extends org.litepal.LitePalApplication {
    private static BaseApp mApp;
    private static Activity sActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d("YWK",activity+"onActivityCreated");
            }


            @Override
            public void onActivityStarted(Activity activity) {
                Log.d("YWK",activity+"onActivityStarted");


            }

            @Override
            public void onActivityResumed(Activity activity) {
                sActivity=activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Context getAppContext() {
        return mApp;
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }

    public static Activity getActivity(){
        return sActivity;
    }
}