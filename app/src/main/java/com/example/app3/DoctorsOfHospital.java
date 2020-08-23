package com.example.app3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorsOfHospital extends AppCompatActivity {

    private static final String TAG = "DoctorsOfHospital";

    private List<Doctor> testDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_of_hospital);

        String hospitalName = getIntent().getStringExtra("医院名称");
        if (hospitalName == null || hospitalName.equals("")){
            finish();
            return;
        }

        TextView hospitalTittle = (TextView) findViewById(R.id.list_hospital_name);
        hospitalTittle.setText(HospitalWebView.hospitalName);

        Doctor doctor = new Doctor("张广权", "15600000000", "骨科",
                "我是骨科相关的医生张广权，点击信息即可与我通话");
        testDoctorList = new ArrayList<>();
        testDoctorList.add(doctor);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.doctors_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new DoctorAdapter(testDoctorList));
    }


}