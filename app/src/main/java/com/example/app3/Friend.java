package com.example.app3;

public class Friend {

    private String name = "姓名： 暂无信息";
    private String tel = "电话： 暂无信息";

    public String getName() {
        return "昵称： " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return "电话： " + tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}