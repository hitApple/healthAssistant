package com.example.app3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.app3.utils.PickCityUtil;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    private int year1, monthOfYear1, dayOfMonth1;
    private String sex;
    private  CircleImageView head_person;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_checkup);
/*        mProvince = (TextView)findViewById(R.id.province);
        mCity = (TextView)findViewById(R.id.city);
        mCounty = (TextView)findViewById(R.id.county);*/

        head_person = findViewById(R.id.head_person);
        if(new File(me.picPath).exists()) {
            head_person.setImageURI(Uri.parse(me.picPath));
        }
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
                                year1 = year;
                                monthOfYear1 = monthOfYear;
                                dayOfMonth1 = dayOfMonth;
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

        findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthCheckUp.this, me.class));
            }
        });
        findViewById(R.id.boy).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                sex = ((RadioButton)findViewById(R.id.boy)).getText().toString();
            }
        });
        findViewById(R.id.girl).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                sex = ((RadioButton)findViewById(R.id.girl)).getText().toString();
            }
        });
        ButterKnife.bind(this);
        PickCityUtil.initPickView(this);
        findViewById(R.id.province_city_county).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity();
            }
        });
/*        mCity.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }

    private void getCity(){
        PickCityUtil.showCityPickView(HealthCheckUp.this, new PickCityUtil.ChooseCityListener() {
            @Override
            public void chooseCity(String s) {
/*                ((TextView)mCity).setTextColor(Color.parseColor("#333333"));
                ((TextView)mCounty).setTextColor(Color.parseColor("#333333"));
                ((TextView)mProvince).setTextColor(Color.parseColor("#333333"));*/
                if(s.split("_").length==2){
/*                    ((TextView)mProvinceBaseDateEntity).setText(s.split("_")[0]);
                    ((TextView)mCounty).setText(s.split("_")[0]);
                    ((TextView)mCity).setText(s.split("_")[1]);*/
                    ((TextView)findViewById(R.id.province_city_county)).setText(s.split("_")[0]+"_"+s.split("_")[0]+"_"+s.split("_")[1]);
                }else{
/*                    ((TextView)mProvince).setText(s.split("_")[0]);
                    ((TextView)mCounty).setText(s.split("_")[1]);
                    ((TextView)mCity).setText(s.split("_")[2]);*/
                    ((TextView)findViewById(R.id.province_city_county)).setText(s.split("_")[0]+"_"+s.split("_")[1]+"_"+s.split("_")[2]);
                }
            }
        });
    }

    public void getDate(){
        List<HealthCheckUpTable> list = LitePal.findAll(HealthCheckUpTable.class);
        HealthCheckUpTable he = list.get(0);
        ((CircleImageView)findViewById(R.id.head_person)).setImageBitmap(getBitmapFromByte(he.pic));
        ((EditText)findViewById(R.id.user_name)).setText(he.name);
        ((TextView)findViewById(R.id.user_time)).setText(he.birthday);
        //sex;
        ((TextView)findViewById(R.id.province_city_county)).setText(he.place);
        ((EditText)findViewById(R.id.user_blood_type)).setText(he.bloodtype);
        ((EditText)findViewById(R.id.user_systolic_lood_pressure)).setText(he.user_systolic_lood_pressure);
        ((EditText)findViewById(R.id.user_diastolic_blood_pressure)).setText(he.user_diastolic_blood_pressure);
        ((EditText)findViewById(R.id.user_height)).setText(he.user_height);
        ((EditText)findViewById(R.id.user_weight)).setText(he.user_weight);
        ((EditText)findViewById(R.id.user_heartbeats_per_minute)).setText(he.user_heartbeats_per_minute);
        ((EditText)findViewById(R.id.user_number_of_urination_per_day)).setText(he.user_number_of_urination_per_day);
        ((EditText)findViewById(R.id.user_daily_sleep_time)).setText(he.user_daily_sleep_time);
        ((EditText)findViewById(R.id.user_what_are_the_current_diseases)).setText(he.user_what_are_the_current_diseases);


    }

    public  void saveData() throws IOException {
        HealthCheckUpTable hcut = new HealthCheckUpTable();
        hcut.setAll(getBitmapByte(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(me.picPath))),
                ((EditText)findViewById(R.id.user_name)).getText().toString(),
                ((TextView)findViewById(R.id.user_time)).getText().toString(),
                sex,
                ((TextView)findViewById(R.id.province_city_county)).getText().toString(),
                ((EditText)findViewById(R.id.user_blood_type)).getText().toString(),
                ((EditText)findViewById(R.id.user_systolic_lood_pressure)).getText().toString(),
                ((EditText)findViewById(R.id.user_diastolic_blood_pressure)).getText().toString(),
                ((EditText)findViewById(R.id.user_height)).getText().toString(),
                ((EditText)findViewById(R.id.user_weight)).getText().toString(),
                ((EditText)findViewById(R.id.user_heartbeats_per_minute)).getText().toString(),
                ((EditText)findViewById(R.id.user_number_of_urination_per_day)).getText().toString(),
                ((EditText)findViewById(R.id.user_daily_sleep_time)).getText().toString(),
                ((EditText)findViewById(R.id.user_what_are_the_current_diseases)).getText().toString());

        hcut.save();
/*        hcut.setPic(getBitmapByte(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(me.picPath))));
        hcut.setName(((EditText)findViewById(R.id.user_name)).getText().toString());
        hcut.setBirthday(((TextView)findViewById(R.id.user_time)).getText().toString());
        hcut.setSex(sex);
        *//*hcut.setAge(((TextView)findViewById(R.id.user_age)).getText().toString());*//*
        hcut.setPlace(((TextView)findViewById(R.id.province_city_county)).getText().toString());*/

/*        //openFileOutput("site.txt", Context.MODE_WORLD_READABLE);
        List<E> list  = new ArrayList<>();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(me.picPath));
        list.add((E) getBitmapByte(bitmap));
        list.add((E) ((EditText)findViewById(R.id.user_name)).getText().toString());
        list.add((E) (String.valueOf(year1) + "/" + String.valueOf(monthOfYear1) + "/" + String.valueOf(dayOfMonth1)));
        list.add((E) (sex));
        list.add((E) ((TextView)findViewById(R.id.province_city_county)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_blood_type)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_systolic_lood_pressure)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_diastolic_blood_pressure)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_height)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_weight)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_heartbeats_per_minute)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_number_of_urination_per_day)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_daily_sleep_time)).getText().toString());
        list.add((E) ((EditText)findViewById(R.id.user_what_are_the_current_diseases)).getText().toString());*/
    }

    public byte[] getBitmapByte(Bitmap bitmap){
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
         try {
                 out.flush();
                 out.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         return out.toByteArray();
    }


     public Bitmap getBitmapFromByte(byte[] temp){
         if(temp != null){
                 Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                 return bitmap;
             }else{
                 return null;
             }
     }



}
