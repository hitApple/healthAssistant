package com.example.app3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HospitalAdapter extends ArrayAdapter<Hospital> {

    private int resourceId;

    public HospitalAdapter(Context context, int textViewResourceId, List<Hospital> hospitalLIst){
        super(context, textViewResourceId, hospitalLIst);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Hospital hospital = getItem(position);
        View view;
//        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
//            viewHolder = new ViewHolder();
//            viewHolder.hospitalName = (TextView) view.findViewById(R.id.hospital_name);
//            viewHolder.hospitalTel = (TextView) view.findViewById(R.id.hospital_tel);
//            viewHolder.hospitalAddress = (TextView) view.findViewById(R.id.hospital_address);
//            viewHolder.hospitalDistance = (TextView) view.findViewById(R.id.hospital_distance);
//            viewHolder.hospitalTime = (TextView) view.findViewById(R.id.hospital_time);
//            view.setTag(viewHolder);
        } else{
            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
        }
        TextView hospitalName = (TextView) view.findViewById(R.id.hospital_name);
        TextView hospitalTel = (TextView) view.findViewById(R.id.hospital_tel);
        TextView hospitalAddress = (TextView) view.findViewById(R.id.hospital_address);
        TextView hospitalDistance = (TextView) view.findViewById(R.id.hospital_distance);
        TextView hospitalTime = (TextView) view.findViewById(R.id.hospital_time);
        if (hospital.getName() == null || hospital.getName().equals("")){
            hospitalTel.setText("医院名称：暂无信息");
        } else{
            hospitalName.setText(hospital.getName());
        }

        if (hospital.getTel() == null || hospital.getTel().equals("")){
            hospitalTel.setText("联系电话：暂无信息");
        } else{
            hospitalTel.setText("联系电话：" + hospital.getTel());
        }

        if (hospital.getAddress() == null || hospital.getAddress().equals("")){
            hospitalTel.setText("详细地址：暂无信息");
        } else{
            hospitalAddress.setText("详细地址：" + hospital.getAddress());
        }

        hospitalDistance.setText("距离：" + Integer.valueOf(hospital.getDistance()).toString() + "米");

        if (hospital.getTime() == null || hospital.getTime().equals("")){
            hospitalTime.setText("上班时间：暂无信息");
        } else{
            hospitalTime.setText("上班时间：" + hospital.getTime());
        }

        return view;
    }

//    private class ViewHolder{
//        TextView hospitalName;
//        TextView hospitalTel;
//        TextView hospitalAddress;
//        TextView hospitalDistance;
//        TextView hospitalTime;
//    }

}
