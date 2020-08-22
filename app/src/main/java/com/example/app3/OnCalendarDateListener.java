package com.example.app3;

import android.graphics.Point;

public interface OnCalendarDateListener {
    void onDateChange(Point nowCalendar, int startDay, int endDay, boolean startBelong, boolean endBelong);
    void onDateItemClick(DateEntity dateEntity);
}
