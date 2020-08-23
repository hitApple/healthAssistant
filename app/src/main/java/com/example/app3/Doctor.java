package com.example.app3;

public class Doctor {

    private String name = "暂无信息";
    private String phone = "暂无信息";
    private String department = "暂无信息";
    private String description = "暂无信息";

    public Doctor(String name, String phone, String department, String description){
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getDescription() {
        return description;
    }
}
