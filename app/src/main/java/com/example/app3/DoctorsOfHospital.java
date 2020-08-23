package com.example.app3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DoctorsOfHospital extends BaseActivity {

    private static final String TAG = "DoctorsOfHospital";

    private List<Doctor> testDoctorList;
    private RecyclerView doctorRecyclerView;
    private Timer timer;

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
                "我是骨科相关的医生张广权，点击信息即可与我通话", "离线");
        testDoctorList = new ArrayList<>();
        testDoctorList.add(doctor);
        doctorRecyclerView = (RecyclerView) findViewById(R.id.doctors_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorRecyclerView.setLayoutManager(layoutManager);
        doctorRecyclerView.setAdapter(new DoctorAdapter(testDoctorList));

    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new checkLoginTimer(testDoctorList, doctorRecyclerView), 0L, 10000L);
    }

    class checkLoginTimer extends TimerTask {

        List<Doctor> doctorList;
        RecyclerView recyclerView;

        public checkLoginTimer(List<Doctor> doctorList, RecyclerView recyclerView){
            this.doctorList = doctorList;
            this.recyclerView = recyclerView;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket("39.96.71.56", 8003);
                socket.setSoTimeout(500);
//                Socket socket = new Socket("10.0.2.2", 8000);
                OutputStream outputStream = socket.getOutputStream();
                StringBuilder builder = new StringBuilder();
                for (Doctor doctor : doctorList){
                    builder.append(doctor.getPhone().substring(4));
                    builder.append("\n");
                }
                outputStream.write(builder.toString().getBytes("UTF-8"));
                outputStream.flush();
                InputStream is = socket.getInputStream();
                byte[] bytes = new byte[1024];
                for (int i = 0; i < 3; i++) {
                    int n = -1;
                    try {
                        n = is.read(bytes);
                    } catch (Exception e) {
                        continue;
                    }
                    if(n != -1) {
                        try {
                            boolean changed = false;
                            char[] loginStatuses = new String(bytes, 0, n,
                                    "UTF-8").toCharArray();
                            for (int j = 0; j < doctorList.size(); j++){
                                Doctor doctor = doctorList.get(j);
                                if (doctor.getLoginStatus().equals("离线") && loginStatuses[j] != '0'){
                                    doctor.setLoginStatus("在线");
                                    doctorList.set(j, doctor);
                                    changed = true;
                                } else if (doctor.getLoginStatus().equals("在线") && loginStatuses[j] == '0'){
                                    doctor.setLoginStatus("离线");
                                    doctorList.set(j, doctor);
                                    changed = true;
                                }
                            }
                            if (changed){
                                DoctorsOfHospital.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        recyclerView.setAdapter(new DoctorAdapter(doctorList));
                                    }
                                });
                            }

//                            if (changed){
//                                new Handler().post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        List<Doctor> tempList = new ArrayList<>(doctorList);
//                                        recyclerView.setAdapter(new DoctorAdapter(tempList));
//                                        try {
//                                            recyclerView.getAdapter().notifyDataSetChanged();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });

                            break;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return;
                        } catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                            return;
                        } catch (Exception e){
                            e.printStackTrace();
                            return;
                        }
                    }

                }
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}