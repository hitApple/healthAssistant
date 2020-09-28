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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.Calendar;
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
    public static String PATH = "http://cctvalih5ca.v.myalicdn.com/live/cctv13_2/index.m3u8";
    private VideoView videoView;

    private static final String TAG = "HomePage_find";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherSearch = new WeatherSearch(HomePage_find.this);
        setContentView(R.layout.homepage_find);

        setItem1();

        initViews();

//        final ScrollView scrollView = findViewById(R.id.homepage_find_scroll);
//        scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });

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

        findViewById(R.id.addInformation).setFocusable(true);
        findViewById(R.id.addInformation).setFocusableInTouchMode(true);
        findViewById(R.id.addInformation).requestFocus();

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

        initPlus();



        videoView = (VideoView) findViewById(R.id.videoView);
        final ImageView start = findViewById(R.id.startTV);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PATH.equals("")){
                    findViewById(R.id.startTV).setVisibility(View.GONE);
                    findViewById(R.id.stopTV).setVisibility(View.VISIBLE);
                    videoView.setVideoPath(PATH);
                    videoView.start();
                }
            }
        });
        findViewById(R.id.stopTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PATH.equals("")){
                    findViewById(R.id.startTV).setVisibility(View.VISIBLE);
                    findViewById(R.id.stopTV).setVisibility(View.GONE);
                    videoView.pause();
                }
            }
        });
        findViewById(R.id.quanping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage_find.this,VideoTV.class));
            }
        });

        final TextView btn_pop = findViewById(R.id.chooseTY);

        btn_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePage_find.this,btn_pop);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        TextView textView = findViewById(R.id.chooseTY);

                        switch (item.getItemId()){
                            case R.id.TY1:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
                                textView.setText("CCTV-1");

                                break;
                            case R.id.TY2:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv2hd.m3u8";
                                textView.setText("CCTV-2");

                                break;
                            case R.id.TY3:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8";
                                textView.setText("CCTV-3");

                                break;
                            case R.id.TY4:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv4hd.m3u8";
                                textView.setText("CCTV-4");

                                break;
                            case R.id.TY5:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8";
                                textView.setText("CCTV-5");

                                break;
                            case R.id.TY6:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8";
                                textView.setText("CCTV-6");

                                break;
                            case R.id.TY7:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv7hd.m3u8";
                                textView.setText("CCTV-7");

                                break;
                            case R.id.TY8:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv8hd.m3u8";
                                textView.setText("CCTV-8");

                                break;
                            case R.id.TY9:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv9hd.m3u8";
                                textView.setText("CCTV-9");

                                break;
                            case R.id.TY10:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv10hd.m3u8";
                                textView.setText("CCTV-10");

                                break;
                            case R.id.TY11:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv12hd.m3u8";
                                textView.setText("CCTV-12");

                                break;
                            case R.id.TY12:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv14hd.m3u8";
                                textView.setText("CCTV-14");

                                break;
                            case R.id.TY13:
                                PATH = "http://cctvalih5ca.v.myalicdn.com/live/cctv13_2/index.m3u8";
                                textView.setText("CCTV-13");

                                break;
                            case R.id.TY14:
                                PATH = "http://ivi.bupt.edu.cn/hls/cgtnhd.m3u8";
                                textView.setText("CGTN");

                                break;
                            case R.id.TY15:
                                PATH = "http://ivi.bupt.edu.cn/hls/cgtndochd.m3u8";
                                textView.setText("CGTN DOC");

                                break;
                            case R.id.TY16:
                                PATH = "http://ivi.bupt.edu.cn/hls/chchd.m3u8";
                                textView.setText("CHC高清");

                                break;

                        }

                        start.performClick();
                        return true;
                    }
                });
                popup.show();

            }
        });

    }


    private void setItem1(){

        HealthCheckUpTable healthCheckUpTable = LitePal.where("phone = ?",
                MainActivity.mPhone).findFirst(HealthCheckUpTable.class);

        String[] healthCheckUpTexts = new String[6];
        final int[] info = new int[8];
        final boolean isNull;
        boolean isNull1 = true;
        if(healthCheckUpTable == null){
            for(int i = 0; i < healthCheckUpTexts.length; i++){
                healthCheckUpTexts[i] = "未填写";
            }
        } else {
            for(int i = 0; i < healthCheckUpTexts.length; i++){
                healthCheckUpTexts[i] = "未填写";
                }
            try {
                  int age = Calendar.getInstance().get(Calendar.YEAR) -
                        Integer.parseInt(healthCheckUpTable.getBirthday().split("/")[0]);
                    info[0] = age;
                } catch (NumberFormatException ignored) {}
            try {
                 info[1] = Integer.parseInt(healthCheckUpTable.getUser_height());
              } catch (NumberFormatException ignored) {}
            try {
                 info[2] = Integer.parseInt(healthCheckUpTable.getUser_weight());
                 healthCheckUpTexts[0] = healthCheckUpTable.getUser_weight() + "kg";
            } catch (NumberFormatException ignored) {}
            try {
                info[3] = Integer.parseInt(healthCheckUpTable.getUser_diastolic_blood_pressure());
                healthCheckUpTexts[1] = healthCheckUpTable.getUser_diastolic_blood_pressure() + "mmHg";
            } catch (NumberFormatException ignored){}
            try {
                info[4] = Integer.parseInt(healthCheckUpTable.getUser_systolic_lood_pressure());
                healthCheckUpTexts[2] = healthCheckUpTable.getUser_systolic_lood_pressure() + "mmHg";
            } catch (NumberFormatException ignored){}
            try {
                info[5] = Integer.parseInt(healthCheckUpTable.getUser_heartbeats_per_minute());
                healthCheckUpTexts[3] = healthCheckUpTable.getUser_heartbeats_per_minute() + "/min";
            } catch (NumberFormatException ignored){}
            try {
                info[6] = Integer.parseInt(healthCheckUpTable.getUser_number_of_urination_per_day());
                healthCheckUpTexts[4] = healthCheckUpTable.getUser_number_of_urination_per_day() + "/day";
            } catch (NumberFormatException ignored){}
            try {
                info[7] = Integer.parseInt(healthCheckUpTable.getUser_daily_sleep_time());
                healthCheckUpTexts[5] = healthCheckUpTable.getUser_daily_sleep_time() + "h/day";
            } catch (NumberFormatException ignored){}

        }
        isNull = false;

        final int[] healthCheckUpTextViewIds = new int[]{R.id.me_list1_2, R.id.me_list2_2,
                R.id.me_list3_2, R.id.me_list4_2, R.id.me_list5_2, R.id.me_list6_2};

        TextView[] healthCheckUpTextViews = new TextView[6];

        for (int i = 0; i < healthCheckUpTextViews.length; i ++ ){
            healthCheckUpTextViews[i] = findViewById(healthCheckUpTextViewIds[i]);
            healthCheckUpTextViews[i].setText(healthCheckUpTexts[i]);
        }

        final int[] healthCheckUpTrianglesIds = new int[]{R.id.check_out1_1, R.id.check_out2_1,
                R.id.check_out3_1, R.id.check_out4_1, R.id.check_out5_1, R.id.check_out6_1};
        final ImageView[] healthCheckUpTriangles = new ImageView[6];
        for (int i = 0; i < healthCheckUpTriangles.length; i++){
            healthCheckUpTriangles[i] = findViewById(healthCheckUpTrianglesIds[i]);
        }
        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final float checkOutSize = findViewById(R.id.check_out1).getWidth();
                if (!isNull){
                    final int[] checkResults = checkHealth(info);
                    for (int i = 0; i < healthCheckUpTriangles.length; i++){
                        final int temp = i;
                        HomePage_find.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (checkResults[temp] == -1){
                                    ObjectAnimator.ofFloat(healthCheckUpTriangles[temp],
                                            "translationX", 0, checkOutSize / 6 - 20)
                                            .setDuration(1000)
                                            .start();
                                } else if (checkResults[temp] == 1){
                                    ObjectAnimator.ofFloat(healthCheckUpTriangles[temp],
                                            "translationX", 0, (checkOutSize / 6) * 5 - 20)
                                            .setDuration(1000)
                                            .start();
                                } else {
                                    ObjectAnimator.ofFloat(healthCheckUpTriangles[temp],
                                            "translationX", 0, checkOutSize / 2 - 20)
                                            .setDuration(1000)
                                            .start();
                                }

                            }
                        });
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (int i = 0; i < healthCheckUpTriangles.length; i++){
                        final int temp = i;
                        HomePage_find.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator.ofFloat(healthCheckUpTriangles[temp],
                                        "translationX", 0, checkOutSize / 2 - 20)
                                        .setDuration(1000)
                                        .start();
                            }
                        });
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }.start();

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

        final int[] listArrowsIds = new int[]{R.id.list1_arrow, R.id.list2_arrow, R.id.list3_arrow,
                R.id.list4_arrow, R.id.list5_arrow, R.id.list6_arrow};
        ImageView[] listArrows = new ImageView[6];
        for (int i = 0; i < listArrows.length; i++){
            listArrows[i] = findViewById(listArrowsIds[i]);
            listArrows[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomePage_find.this, HealthCheckUp.class));
                }
            });
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

    private void initPlus(){
        findViewById(R.id.plus_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, FriendView.class));
            }
        });

        findViewById(R.id.plus_calorie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, CalorieView.class));
            }
        });

        findViewById(R.id.plus_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, VideoTV.class));
            }
        });

        findViewById(R.id.plus_hospital).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, FavouritesHospitalView.class));
            }
        });

        findViewById(R.id.plus_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage_find.this, HealthCheckUp.class));
            }
        });
    }

    private int[] checkHealth(int[] info){
        int[] results = new int[6];
        int age = info[0];
        double height = (double) info[1] / (double) 100;
        double BMI = info[2] / (Math.pow(info[1] / 100, 2));
        if (info[2] == 0 || info[1] == 0){
            results[0] = 0;
        } else if (BMI < 18.5){
            results[0] = -1;
        } else if (BMI >= 24){
            results[0] = 1;
        } else{
            results[0] = 0;
        }

        if (info[3] == 0){
            results[1] = 0;
        } else if (info[3] < 60){
            results[1] = -1;
        } else if (info[3] > 89){
            results[1] = 1;
        } else {
            results[1] = 0;
        }

        if (info[4] == 0){
            results[2] = 0;
        } else if (info[4] < 90){
            results[2] = -1;
        } else if (info[4] > 139){
            results[2] = 1;
        } else {
            results[2] = 0;
        }


        if (info[5] == 0){
            results[3] = 0;
        } else if (age <= 5){
            if (info[5] < 100){
                results[3] = -1;
            } else if (info[5] > 140){
                results[3] = 1;
            } else{
                results[3] = 0;
            }
        } else{
            if (info[5] < 60){
                results[3] = -1;
            } else if (info[5] > 100){
                results[3] = 1;
            } else{
                results[3] = 0;
            }
        }

        if (info[6] == 0){
            results[4] = 0;
        } else if (info[6] < 6){
            results[4] = -1;
        } else if (info[6] > 8){
            results[4] = 1;
        } else {
            results[4] = 0;
        }

        if (info[7] == 0){
            results[5] = 0;
        } else if (info[7] < 7){
            results[5] = -1;
        } else if (info[7] > 9){
            results[5] = 1;
        } else {
            results[5] = 0;
        }
        return results;
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
