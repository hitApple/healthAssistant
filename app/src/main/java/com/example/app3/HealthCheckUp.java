package com.example.app3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import java.io.ByteArrayOutputStream;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_checkup);
/*        mProvince = (TextView)findViewById(R.id.province);
        mCity = (TextView)findViewById(R.id.city);
        mCounty = (TextView)findViewById(R.id.county);*/

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
/*                    ((TextView)mProvince).setText(s.split("_")[0]);
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

    public <E> void saveData() throws IOException {
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
        list.add((E) ((EditText)findViewById(R.id.user_what_are_the_current_diseases)).getText().toString());

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
