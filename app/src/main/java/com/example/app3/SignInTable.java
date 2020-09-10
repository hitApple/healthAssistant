package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class SignInTable extends LitePalSupport {
    public int year;
    public int month;
    public int day;

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
    public void setYearMonthDay(int year, int month,int day){
        this.year = year ;
        this.month = month ;
        this.day = day ;
    }
}
