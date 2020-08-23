package com.example.app3;

public class Doctor {

    private String name = "医生： 暂无信息";
    private String phone = "电话： 暂无信息";
    private String department = "科室： 暂无信息";
    private String description = "简介： 暂无信息";
    private String loginStatus = "离线";

    public Doctor(String name, String phone, String department, String description, String loginStatus){
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.description = description;
        this.loginStatus = loginStatus;
    }

    public String getName() {
        return "医生： " + name;
    }

    public String getPhone() {
        return "电话： " + phone;
    }

    public String getDepartment() {
        return "科室： " + department;
    }

    public String getDescription() {
        return "简介： " + description;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
