package com.tonkia.vo;


import org.litepal.crud.LitePalSupport;

import java.util.Calendar;

public class DealRecord extends LitePalSupport {

    private int id;
    private float cost;
    private long time;
    private String itemName;
    private int type;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private int year;
    private int month;
    private int week;
    private int dayOfWeek;
    private int dayOfMonth;

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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void initTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        week = c.get(Calendar.WEEK_OF_YEAR);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(year + ":" + month + ":" + week + ":" + dayOfWeek + ":" + dayOfMonth);
    }
}
