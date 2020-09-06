package com.example.app3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.app3.utils.PickCityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;


public class HealthCheckUp extends BaseActivity {

    private Button submit;

    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;

    private TextView mProvince;
    private TextView mCity;
    private TextView mCounty;

    private List<String> reasonList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_checkup);
        mProvince = (TextView)findViewById(R.id.province);
        mCity = (TextView)findViewById(R.id.city);
        mCounty = (TextView)findViewById(R.id.county);

        submit = findViewById(R.id.healthcheckup_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交信息
            }
        });
        findViewById(R.id.user_time).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(HealthCheckUp.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                System.out.println("年-->" + year + "月-->"
                                        + monthOfYear + "日-->" + dayOfMonth);
                                ((TextView)findViewById(R.id.user_time)).setText(year + "/" + monthOfYear + "/"
                                        + dayOfMonth);
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


        ButterKnife.bind(this);
        PickCityUtil.initPickView(this);
        mProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity();
            }
        });
        mCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity();
            }
        });
        mCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity();
            }
        });

    }

    private void getCity(){
        PickCityUtil.showCityPickView(HealthCheckUp.this, new PickCityUtil.ChooseCityListener() {
            @Override
            public void chooseCity(String s) {
                ((TextView)mCity).setTextColor(Color.parseColor("#333333"));
                ((TextView)mCounty).setTextColor(Color.parseColor("#333333"));
                ((TextView)mProvince).setTextColor(Color.parseColor("#333333"));
                if(s.split("_").length==2){
                    ((TextView)mProvince).setText(s.split("_")[0]);
                    ((TextView)mCounty).setText(s.split("_")[0]);
                    ((TextView)mCity).setText(s.split("_")[1]);
                }else{
                    ((TextView)mProvince).setText(s.split("_")[0]);
                    ((TextView)mCounty).setText(s.split("_")[1]);
                    ((TextView)mCity).setText(s.split("_")[2]);
                }
            }
        });
    }



}
