package com.example.app3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




public class HomePage extends BaseActivity {
    private RelativeLayout homepage;
    private RelativeLayout find;
    private RelativeLayout plus;
    private RelativeLayout link;
    private RelativeLayout me;

    private ImageView homepage_sign_in;


    private TextView tvDate;
    private CalendarRecycleView<BaseDateEntity> rcDate;
    private RelativeLayout rlSignK;
    private ArrayList<BaseDateEntity> list;
    private RecyclerView rcList;


    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        homepage_sign_in = findViewById(R.id.homepage_sign_in);
        homepage_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    findViewById(R.id.full).setVisibility(View.GONE);
                    findViewById(R.id.activity_calendar_sign_in).setVisibility(View.VISIBLE);
                    initView();
                    initData();
                    initEvent();
/*                    Calendar calendar = Calendar.getInstance();
                    SignInTable da = new SignInTable();
                    da.setYearMonthDay(2020, 9,8);
                    da.save();
*//*                  da.setYearMonthDay(2020, 9,7);
                    da.save();*/
                  List<SignInTable> DateList = LitePal.findAll(SignInTable.class);

                    for (SignInTable sin : DateList) {
                        list.add(new BaseDateEntity(sin.getYear(),sin.getMonth(),sin.getDay()));
                    }
/*                  list.add(new BaseDateEntity(2020, 9,8));
                    list.add(new BaseDateEntity(2020, 9,7));*/
                    rcDate.initRecordList(list);
                }
            });

        findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.activity_calendar_sign_in).setVisibility(View.GONE);
            }
        });


        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);

        homepage.setBackgroundColor(-1);
        find.setBackgroundColor(-3355444);
        link.setBackgroundColor(-1);
        me.setBackgroundColor(-1);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(HomePage.this,HomePage_find.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.GONE);
                findViewById(R.id.plus_ui).setVisibility(View.VISIBLE);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,ContactBaiduMap.class));
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePage.this, com.example.app3.me.class));
            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.plus_ui).setVisibility(View.GONE);
            }
        });

//        Intent intent = getIntent();
//        try {
//            memberWelcome(intent.getStringExtra("phone"));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
/*        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;*/

        setWidthAndHeight();

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
                closeKeyboard();
            }
        }.start();



    }

    public void setWidthAndHeight(){

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        findViewById(R.id.eightsystem).measure(w, h);
        int height = findViewById(R.id.eightsystem).getMeasuredHeight();
        int width = findViewById(R.id.eightsystem).getMeasuredWidth();

        findViewById(R.id.homepage_urinary_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_urinary_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_digestive_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_digestive_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_circulatory_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_circulatory_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_endocrine_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_endocrine_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_nervous_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_nervous_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_reproductive_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_reproductive_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_respiratory_system).getLayoutParams().width = width/5;
        findViewById(R.id.homepage_respiratory_system).getLayoutParams().height = width/5;

        findViewById(R.id.homepage_exercise_system).getLayoutParams().width = width/4;
        findViewById(R.id.homepage_exercise_system).getLayoutParams().height = width/5;

    }



//    private static final String[] memberPhones = {
//            "15689712036", "19861807360", "13184116753","15689711359","13954159704"
//    };


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

//    private void memberWelcome(String phone) throws InterruptedException {
//        if (phone == null){
//            return;
//        }
//        for (String string : memberPhones){
//            if (phone.equals(string)){
//                AlertDialog.Builder dialog = new AlertDialog.Builder(HomePage.this);
//                dialog.setTitle("欢迎！");
//                dialog.setMessage("检测到您为健康助手的测试人员\n本程序在这里诚挚的欢迎您!\n感谢您为健康助手所做的突出贡献!");
//                dialog.setCancelable(true);
//                dialog.setPositiveButton("谢谢", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                dialog.show();
//                break;
//            }
//        }
//    }

    private void initView() {
        tvDate = findViewById(R.id.tv_date);
        rcDate = findViewById(R.id.recycle_view);
        rlSignK = findViewById(R.id.rl_sign_k);
        rcList = findViewById(R.id.rc_list);

    }

    private void initData() {
        list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        tvDate.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
        list.add(new BaseDateEntity(2019, 9, 12));
        list.add(new BaseDateEntity(2019, 9, 15));
        list.add(new BaseDateEntity(2019, 9, 17));
        rcDate.initRecordList(list);


        List<String> listX = new ArrayList<>();
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        listX.add("1");
        LinearLayoutManager manager = new LinearLayoutManager(HomePage.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcList.setLayoutManager(manager);
        SignAwardListAdapter adapter = new SignAwardListAdapter(listX, HomePage.this);
        rcList.setAdapter(adapter);
    }

    private void initEvent() {

        rcDate.setOnCalendarDateListener(new OnCalendarDateListener() {
            @Override
            public void onDateChange(Point nowCalendar, int startDay, int endDay, boolean startBelong, boolean endBelong) {
                tvDate.setText(nowCalendar.x + "年" + nowCalendar.y + "月");
            }

            @Override
            public void onDateItemClick(DateEntity dateEntity) {
                Toast.makeText(HomePage.this, "点击日期 " + dateEntity.date, Toast.LENGTH_SHORT).show();
            }
        });

        rlSignK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                list.add(new BaseDateEntity(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e("XXP", calendar.get(Calendar.YEAR)+"--"+(calendar.get(Calendar.MONTH) + 1)+"---"+calendar.get(Calendar.DAY_OF_MONTH));
                rcDate.initRecordList(list);

                SignInTable da = new SignInTable();
                da.setYearMonthDay(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),calendar.get(Calendar.DAY_OF_MONTH));
                da.save();

                Toast.makeText(HomePage.this, "点击签到 " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置可以点击日期的事件记录
    private ArrayList<BaseDateEntity> initRecordList() {
        ArrayList<BaseDateEntity> list = new ArrayList<>();

        return list;
    }

    /**
     * 调用之后可以关闭键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
