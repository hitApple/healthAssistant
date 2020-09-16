package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class HealthCheckUpTable extends LitePalSupport {
    public String phone;
    public byte[] pic;
    public String name;
    public String birthday;
    public String sex;
    public String place;
    public String bloodtype;
    public String user_systolic_lood_pressure;
    public String user_diastolic_blood_pressure;
    public String user_height;
    public String user_weight;
    public String user_heartbeats_per_minute;
    public String user_number_of_urination_per_day;
    public String user_daily_sleep_time;
    public String user_what_are_the_current_diseases;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAll(
            String name,
            String birthday,
            String sex,
            String place,
            String bloodtype,
            String user_systolic_lood_pressure,
            String user_diastolic_blood_pressure,
            String user_height,
            String user_weight,
            String user_heartbeats_per_minute,
            String user_number_of_urination_per_day,
            String user_daily_sleep_time,
            String user_what_are_the_current_diseases) {
        this.pic = pic;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.place = place;
        this.bloodtype = bloodtype;
        this.user_systolic_lood_pressure = user_systolic_lood_pressure;
        this.user_diastolic_blood_pressure = user_diastolic_blood_pressure;
        this.user_height = user_height;
        this.user_weight = user_weight;
        this.user_heartbeats_per_minute = user_heartbeats_per_minute;
        this.user_number_of_urination_per_day = user_number_of_urination_per_day;
        this.user_daily_sleep_time = user_daily_sleep_time;
        this.user_what_are_the_current_diseases = user_what_are_the_current_diseases;

    }


    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

/*    public String getAge() {
        return age;
    }*/

/*    public void setAge(String age) {
        this.age = age;
    }*/

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getUser_systolic_lood_pressure() {
        return user_systolic_lood_pressure;
    }

    public void setUser_systolic_lood_pressure(String user_systolic_lood_pressure) {
        this.user_systolic_lood_pressure = user_systolic_lood_pressure;
    }

    public String getUser_diastolic_blood_pressure() {
        return user_diastolic_blood_pressure;
    }

    public void setUser_diastolic_blood_pressure(String user_diastolic_blood_pressure) {
        this.user_diastolic_blood_pressure = user_diastolic_blood_pressure;
    }

    public String getUser_height() {
        return user_height;
    }

    public void setUser_height(String user_height) {
        this.user_height = user_height;
    }

    public String getUser_weight() {
        return user_weight;
    }

    public void setUser_weight(String user_weight) {
        this.user_weight = user_weight;
    }

    public String getUser_heartbeats_per_minute() {
        return user_heartbeats_per_minute;
    }

    public void setUser_heartbeats_per_minute(String user_heartbeats_per_minute) {
        this.user_heartbeats_per_minute = user_heartbeats_per_minute;
    }

    public String getUser_number_of_urination_per_day() {
        return user_number_of_urination_per_day;
    }

    public void setUser_number_of_urination_per_day(String user_number_of_urination_per_day) {
        this.user_number_of_urination_per_day = user_number_of_urination_per_day;
    }

    public String getUser_daily_sleep_time() {
        return user_daily_sleep_time;
    }

    public void setUser_daily_sleep_time(String user_daily_sleep_time) {
        this.user_daily_sleep_time = user_daily_sleep_time;
    }

    public String getUser_what_are_the_current_diseases() {
        return user_what_are_the_current_diseases;
    }

    public void setUser_what_are_the_current_diseases(String user_what_are_the_current_diseases) {
        this.user_what_are_the_current_diseases = user_what_are_the_current_diseases;
    }


}
