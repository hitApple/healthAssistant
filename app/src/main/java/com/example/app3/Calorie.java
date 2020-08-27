package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class Calorie extends LitePalSupport {

    private String phone;
    private String foodName;
    private int calorieAmount;
    private int year;
    private int month;
    private int day;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
