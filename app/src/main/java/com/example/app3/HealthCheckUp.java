package com.example.app3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app3.utils.PickCityUtil;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

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
        if(new File(MainActivity.mPicPath).exists()) {
            head_person.setImageURI(Uri.parse(me.picPath));
            head_person.setBackgroundDrawable(null);
        }

        submit = findViewById(R.id.healthcheckup_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            setData();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public  void setData() throws IOException {
        List<HealthCheckUpTable> DateList = LitePal.where("phone = ?",
                MainActivity.mPhone).find(HealthCheckUpTable.class);
        if( DateList!=null &&  DateList.size()>0 &&  DateList.get(0)!=null){
            HealthCheckUpTable sin = DateList.get(0);

            ((CircleImageView)findViewById(R.id.head_person)).setImageURI(Uri.parse(me.picPath));
            ((EditText)findViewById(R.id.user_name)).setText(sin.getName());
            ((TextView)findViewById(R.id.user_time)).setText(sin.getBirthday());
            ((TextView)findViewById(R.id.province_city_county)).setText(sin.getPlace());
            ((EditText)findViewById(R.id.user_blood_type)).setText(sin.getBloodtype());
            ((EditText)findViewById(R.id.user_systolic_lood_pressure)).setText(sin.getUser_systolic_lood_pressure());
            ((EditText)findViewById(R.id.user_diastolic_blood_pressure)).setText(sin.getUser_diastolic_blood_pressure());
            ((EditText)findViewById(R.id.user_height)).setText(sin.getUser_height());
            ((EditText)findViewById(R.id.user_weight)).setText(sin.getUser_weight());
            ((EditText)findViewById(R.id.user_heartbeats_per_minute)).setText(sin.getUser_heartbeats_per_minute());
            ((EditText)findViewById(R.id.user_number_of_urination_per_day)).setText(sin.getUser_number_of_urination_per_day());
            ((EditText)findViewById(R.id.user_daily_sleep_time)).setText(sin.getUser_daily_sleep_time());
            ((EditText)findViewById(R.id.user_what_are_the_current_diseases)).setText(sin.getUser_what_are_the_current_diseases());
        }


    }
    public  void saveData() throws IOException {
        HealthCheckUpTable healthCheckUpTable = new HealthCheckUpTable();

        List<HealthCheckUpTable> DateList = LitePal.where("phone = ?",
                MainActivity.mPhone).find(HealthCheckUpTable.class);
        for(HealthCheckUpTable checkUpTable : DateList){
            checkUpTable.delete();
        }



        healthCheckUpTable.setPhone(MainActivity.mPhone);
        healthCheckUpTable.setAll(
                ((EditText)findViewById(R.id.user_name)).getText().toString(),
                ((TextView)findViewById(R.id.user_time)).getText().toString(), sex,
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

        healthCheckUpTable.save();
        Toast.makeText(HealthCheckUp.this, "保存完毕!", Toast.LENGTH_SHORT).show();
        finish();
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
