package com.example.iraapplication.domain;

import java.util.Calendar;
import java.util.Date;

public class SimpleDate {
    // 1..31
    public final int day;
    // 1..12
    public final int month;
    // e.g. 2020
    public final int year;

    public SimpleDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return new Date(calendar.getTimeInMillis());
    }
}
