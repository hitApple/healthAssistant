package com.example.app3;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private Boolean isTF = true;
    private List<String> permissionList = new ArrayList<>();

    private static int[] weatherItems;
    public TextView[] weathersTextViews = new TextView[5];
    private static int[] weatherItems2;
    public TextView[] weathersTextViews2 = new TextView[5];
    private static int[] weatherItems3;
    public TextView[] weathersTextViews3 = new TextView[5];
    private static int[] healthCheckupLayoutsIds;
    private ConstraintLayout[] healthCheckUpLayouts = new ConstraintLayout[6];

    private TextView showMore;
    private boolean isShowMore = false;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherSearch = new WeatherSearch(HomePage_find.this);
        setContentView(R.layout.homepage_find);

        setItem1();

        initViews();

        showMore = findViewById(R.id.show_more);
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowMore){
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 30, 0, 0);
                    findViewById(R.id.homepage_find_health_check_up).setLayoutParams(layoutParams);
                    showMore.setText("收起");
                    isShowMore = true;
                } else{
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            1150);
                    layoutParams.setMargins(0, 30, 0, 0);
                    findViewById(R.id.homepage_find_health_check_up).setLayoutParams(layoutParams);
                    showMore.setText("展开");
                    isShowMore = false;
                }
            }
        });

        homepage_find_search = findViewById(R.id.homepage_find_search);
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


        findViewById(R.id.exit_login).setVisibility(View.GONE);

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
                    plus.animate().rotation(-90);
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
                    plus.animate().rotation(45);
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
        findViewById(R.id.addInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage_find.this, HealthCheckUp.class));
            }
        });

        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setVideoPath("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");

        videoView.start();

    }


    private void setItem1(){

        HealthCheckUpTable healthCheckUpTable = LitePal.where("phone = ?",
                MainActivity.mPhone).findFirst(HealthCheckUpTable.class);

        if(healthCheckUpTable == null){
            return;
        }

        String[] healthCheckUpTexts = new String[6];

        healthCheckUpTexts[0] = healthCheckUpTable.getUser_weight() + "kg";
        healthCheckUpTexts[1] = healthCheckUpTable.getUser_diastolic_blood_pressure() + "mmHg";
        healthCheckUpTexts[2] = healthCheckUpTable.getUser_systolic_lood_pressure() + "mmHg";
        healthCheckUpTexts[3] = healthCheckUpTable.getUser_heartbeats_per_minute() + "/min";
        healthCheckUpTexts[4] = healthCheckUpTable.getUser_number_of_urination_per_day() + "/day";
        healthCheckUpTexts[5] = healthCheckUpTable.getUser_daily_sleep_time() + "/day";

        final int[] healthCheckUpTextViewIds = new int[]{R.id.me_list1_2, R.id.me_list2_2,
                R.id.me_list3_2, R.id.me_list4_2, R.id.me_list5_2, R.id.me_list6_2};

        TextView[] healthCheckUpTextViews = new TextView[6];

        for (int i = 0; i < healthCheckUpTextViews.length; i ++ ){
            healthCheckUpTextViews[i] = findViewById(healthCheckUpTextViewIds[i]);
            healthCheckUpTextViews[i].setText(healthCheckUpTexts[i]);
        }

        final int[] healthCheckUpIds = new int[]{R.id.check_out1, R.id.check_out2, R.id.check_out3,
                R.id.check_out4, R.id.check_out5, R.id.check_out6};

    }

    private void initViews(){
        healthCheckupLayoutsIds = new int[]{R.id.me_list1, R.id.me_list2, R.id.me_list3,
                R.id.me_list4, R.id.me_list5, R.id.me_list6};

        for (int i = 0; i < healthCheckUpLayouts.length; i++){
            healthCheckUpLayouts[i] = findViewById(healthCheckupLayoutsIds[i]);
            healthCheckUpLayouts[i].setAlpha(0);
        }

        weatherItems = new int[]{R.id.highAndLow, R.id.place, R.id.nowWeather, R.id.directionOfWind,
                R.id.recommendation};
        weatherItems2 = new int[]{R.id.highAndLow2, R.id.place2, R.id.nowWeather2, R.id.directionOfWind2,
                R.id.recommendation2};
        weatherItems3 = new int[]{R.id.highAndLow3, R.id.place3, R.id.nowWeather3, R.id.directionOfWind3,
                R.id.recommendation3};
        for (int i = 0; i < weatherItems.length; i++){
            weathersTextViews[i] = findViewById(weatherItems[i]);
            weathersTextViews2[i] = findViewById(weatherItems2[i]);
            weathersTextViews3[i] = findViewById(weatherItems3[i]);
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < healthCheckUpLayouts.length; i++){
                    final int temp = i;
                    HomePage_find.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator.ofFloat(healthCheckUpLayouts[temp],
                                    "alpha", 0, 1)
                                    .setDuration(1000)
                                    .start();
                        }
                    });
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ObjectAnimator.ofFloat(bgImage,
//                        "alpha", 1, 0)
//                        .setDuration(1000)
//                        .start();
//            }
//        });
    }

    long boo = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - boo) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                boo = System.currentTimeMillis();
            } else {
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
