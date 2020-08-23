package com.example.app3;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<Doctor> mDoctorList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View doctorView;
        TextView doctorName;
        TextView doctorTel;
        TextView doctorDepartment;
        TextView doctorDescription;
        TextView doctorLoginStatus;

        public ViewHolder(View view){
            super(view);
            doctorView = view;
            doctorName = (TextView) view.findViewById(R.id.doctor_name);
            doctorTel = (TextView) view.findViewById(R.id.doctor_tel);
            doctorDepartment = (TextView) view.findViewById(R.id.doctor_department);
            doctorDescription = (TextView) view.findViewById(R.id.doctor_description);
            doctorLoginStatus = (TextView) view.findViewById(R.id.doctor_login_status);
        }
    }

    public DoctorAdapter(List<Doctor> doctorList){
        mDoctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent,
                false);
        final ViewHolder holder = new ViewHolder(view);
        holder.doctorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VideoCall.class);
                intent.putExtra("user", MainActivity.mPhone);
                intent.putExtra("room", "room_" + MainActivity.mPhone);
                intent.putExtra("stream1", "stream_" + MainActivity.mPhone);
                intent.putExtra("stream2", "stream_" +
                        holder.doctorTel.getText().toString().substring(4));
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctor = mDoctorList.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorTel.setText(doctor.getPhone());
        holder.doctorDepartment.setText(doctor.getDepartment());
        holder.doctorDescription.setText(doctor.getDescription());
        holder.doctorLoginStatus.setText(doctor.getLoginStatus());
    }

    @Override
    public int getItemCount() {
        return mDoctorList.size();
    }
}
