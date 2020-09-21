package com.example.app3;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomePage_find extends BaseActivity  {

    private RelativeLayout homepage;
    private RelativeLayout find;
    private RelativeLayout plus;
    private RelativeLayout link;
    private RelativeLayout me;
    private ImageView homepage_find_searchicon;
    private EditText homepage_find_search;
    private boolean isWeatherAccept = true;
    private WeatherSearch weatherSearch;
    TextView weatherTextView;
    private Boolean isTF = true;
    private List<String> permissionList = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherSearch = new WeatherSearch(HomePage_find.this);
        setContentView(R.layout.homepage_find);

        homepage_find_search = findViewById(R.id.homepage_find_search);
        weatherTextView = findViewById(R.id.homepage_weather);
        getPermissions();

        homepage_find_searchicon = findViewById(R.id.homepage_find_search_image);
        homepage_find_searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                Intent intent = new Intent(HomePage_find.this, HomePage.class);
                intent.putExtra("searchText", homepage_find_search.getText().toString());
                homepage_find_search.setText("");
                startActivity(intent);
            }
        });

        setItem1();

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);

        homepage.setBackgroundColor(-3355444);
        find.setBackgroundColor(-1);
        link.setBackgroundColor(-1);
        me.setBackgroundColor(-1);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* startActivity(new Intent(HomePage_find.this,HomePage.class));*/

            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(HomePage_find.this, HomePage.class));


            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isTF){
                    isTF = true;
                    plus.animate().rotation(90);
                    /*                findViewById(R.id.plus_ui2).setVisibility(View.VISIBLE);*/
                    DisplayMetrics metric = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metric);
                    int screenH = metric.heightPixels;
                    findViewById(R.id.plus_ui2).clearAnimation();
                    ObjectAnimator.ofFloat(findViewById(R.id.plus_ui2),
                            "translationY",
                            (float) (MainActivity.mScreenHeight/2))
                            .setDuration(500)
                            .start();

                }else{
                    isTF = false;
                    plus.animate().rotation(-90);
                    //               findViewById(R.id.plus_ui2).setVisibility(View.VISIBLE);
                    DisplayMetrics metric = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metric);
                    int screenH = metric.heightPixels;
                    findViewById(R.id.plus_ui2).clearAnimation();
                    ObjectAnimator.ofFloat(findViewById(R.id.plus_ui2),
                            "translationY",
                            (float) (-MainActivity.mScreenHeight/2))
                            .setDuration(500)
                            .start();


                }
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this,ContactBaiduMap.class));


            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, com.example.app3.me.class));


            }
        });

    }

    private void setItem1(){

        HealthCheckUpTable healthCheckUpTable = LitePal.where("phone = ?",
                MainActivity.mPhone).findFirst(HealthCheckUpTable.class);

        if(healthCheckUpTable == null){
            return;
        }

        String weight = "体重: " +  healthCheckUpTable.getUser_weight();
        String diastolic = "舒张压: " + healthCheckUpTable.getUser_diastolic_blood_pressure();
        String systolic = "收缩压: " + healthCheckUpTable.getUser_systolic_lood_pressure();
        String heartbeats = "心跳次数: " + healthCheckUpTable.getUser_heartbeats_per_minute();
        String sleepTime = "睡眠时间: " + healthCheckUpTable.getUser_daily_sleep_time();
        String urination = "小便次数: " + healthCheckUpTable.getUser_number_of_urination_per_day();

        TextView text1 = findViewById(R.id.item1_text1);
        TextView text2 = findViewById(R.id.item1_text2);
        TextView text3 = findViewById(R.id.item1_text3);
        TextView text4 = findViewById(R.id.item1_text4);
        TextView text5 = findViewById(R.id.item1_text5);
        TextView text6 = findViewById(R.id.item1_text6);

        text1.setText(weight);
        text2.setText(diastolic);
        text3.setText(systolic);
        text4.setText(heartbeats);
        text5.setText(sleepTime);
        text6.setText(urination);
    }

    /**
     * 退出程序
     */

/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    long boo = 0;
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - boo) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                boo = System.currentTimeMillis();
            } else {
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                ActivityCollector.finishAll();
                return true;
            }
        }
        return false;
    }

    /**
     * 进行各种权限的申请
     */
    private void getPermissions(){
        if (ContextCompat.checkSelfPermission(HomePage_find.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(HomePage_find.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(HomePage_find.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(HomePage_find.this, permissions, 1);
        } else {
            weatherSearch.mLocationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isWeatherAccept = false;
                    Toast.makeText(this, "没有得到获取天气的权限，无法显示天气信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (isWeatherAccept){
            weatherSearch.mLocationClient.start();
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
