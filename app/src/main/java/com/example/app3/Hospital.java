package com.example.app3;

public class Hospital {

    private String name;
    private String tel;
    private String address;
    private int distance;
    private String time;

    public Hospital(){}

    public Hospital(String name, String tel, String address, int distance, String time){
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.distance = distance;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public int getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
