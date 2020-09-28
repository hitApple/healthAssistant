package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalorieResult extends BaseActivity {

    private int sumCalorieAmount = 0;
    private int exceptCalorieAmount = 1000;
    private TextView tvDate;
    private CalendarRecycleView<BaseDateEntity> rcDate;
    private RelativeLayout rlSignK;
    private ArrayList<BaseDateEntity> list;
    private RecyclerView rcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_finished);

        Button continueWriteDown = findViewById(R.id.continue_write_down);
        continueWriteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalorieResult.this, CalorieView.class));
            }
        });
        Button backToHomePage = findViewById(R.id.back_to_homepage);
        backToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalorieResult.this, HomePage_find.class));
            }
        });



        TextView calorieResult = findViewById(R.id.today_result);
        TextView sumCalorie = findViewById(R.id.sum_calorie);
        TextView isAchievedText = findViewById(R.id.isAchieved);

        HealthCheckUpTable healthCheckUpTable = LitePal.where("phone = ?",
                MainActivity.mPhone).findFirst(HealthCheckUpTable.class);

        try {
            String calorie2 = healthCheckUpTable == null ? "" : healthCheckUpTable.getUser_Cal();
            if (calorie2 == null || calorie2.trim().equals("")){
                exceptCalorieAmount = -1;
            } else {
                exceptCalorieAmount = Integer.parseInt(healthCheckUpTable.getUser_Cal());
            }
        } catch (NumberFormatException e) {
            exceptCalorieAmount = -1;
        }

        String year = null;
        String month = null;
        String day = null;
        Intent intent = getIntent();
        Calendar calendar = Calendar.getInstance();

        if (intent != null && intent.getExtras() != null){
            year = String.valueOf(intent.getIntExtra("year", calendar.get(Calendar.YEAR)));
            month = String.valueOf(intent.getIntExtra("month", calendar.get(Calendar.MONTH)));
            day = String.valueOf(intent.getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH)));
        } else {
            year = String.valueOf(calendar.get(Calendar.YEAR));
            month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }
        calorieResult.setText(getCaloriesText(year, month, day));
        String sumCalorieString = "总共：" + sumCalorieAmount + " 卡路里";
        sumCalorie.setText(sumCalorieString);
        TextView exceptCalorie = findViewById(R.id.exception_calorie);
        String exceptCalorieString = "";
        if (exceptCalorieAmount == -1){
            exceptCalorieString = "未填写卡路里目标";
            exceptCalorie.setText(exceptCalorieString);
            isAchievedText.setText("   未填写   ");
        } else {
            exceptCalorieString = "期望值为：" + exceptCalorieAmount;
            exceptCalorie.setText(exceptCalorieString);
            if (sumCalorieAmount <= exceptCalorieAmount){
                isAchievedText.setText("   达标   ");
            } else {
                isAchievedText.setText("   未达标   ");
            }
        }

        Button calendarImage = findViewById(R.id.calorie_sign_in);
        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findViewById(R.id.full).setVisibility(View.GONE);
                findViewById(R.id.activity_calendar_sign_in).setVisibility(View.VISIBLE);
                initView();
                initData();
                initEvent();

//                Calendar calendar = Calendar.getInstance();
//                SignInTable da = new SignInTable();
//                da.setPhone(MainActivity.mPhone);
//                da.setYearMonthDay(2020, 9,8);
//                da.save();
/*                  da.setYearMonthDay(2020, 9,7);
                    da.save();*/
                List<SignInTable> DateList = LitePal.where("phone = ?",
                        MainActivity.mPhone).find(SignInTable.class);

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




    }

    private String getCaloriesText(String year, String month, String day){

        List<Calorie> calories = LitePal.select("foodName", "CalorieAmount")
                                        .where("phone = ? and year = ? and month = ? and day = ?", MainActivity.mPhone, year, month, day)
                                        .find(Calorie.class);
        StringBuilder builder = new StringBuilder();
        for (Calorie calorie : calories){
            builder.append(calorie.getFoodName());
            builder.append("     ");
            builder.append(calorie.getCalorieAmount());
            builder.append("\n");
            sumCalorieAmount += calorie.getCalorieAmount();
        }
        return builder.toString();
    }

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
        LinearLayoutManager manager = new LinearLayoutManager(CalorieResult.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcList.setLayoutManager(manager);
        SignAwardListAdapter adapter = new SignAwardListAdapter(listX, CalorieResult.this);
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
                Intent intent = new Intent(CalorieResult.this, CalorieResult.class);
                intent.putExtra("year", dateEntity.year);
                intent.putExtra("month", dateEntity.month);
                intent.putExtra("day", dateEntity.day);
                startActivity(intent);
            }
        });

        rlSignK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sumCalorieAmount < exceptCalorieAmount){
                    Calendar calendar = Calendar.getInstance();
                    list.add(new BaseDateEntity(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),calendar.get(Calendar.DAY_OF_MONTH)));
                    rcDate.initRecordList(list);

                    SignInTable da = new SignInTable();
                    da.setPhone(MainActivity.mPhone);
                    da.setYearMonthDay(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1),calendar.get(Calendar.DAY_OF_MONTH));
                    da.save();
                    Toast.makeText(CalorieResult.this, "签到成功 " , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalorieResult.this, "很遗憾，您今天没有达到目标 " , Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}