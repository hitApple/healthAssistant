package com.example.app3;


public class BaseDateEntity {

    /** 年 */
    public int     year;
    /** 月 */
    public int     month;
    /** 日 */
    public int     day;

    public BaseDateEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public BaseDateEntity() {

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
