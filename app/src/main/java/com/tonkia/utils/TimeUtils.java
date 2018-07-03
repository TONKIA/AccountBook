package com.tonkia.utils;

public class TimeUtils {
    public static String recordTime(long time) {
        int second = (int) (time / 1000 % 60);
        int minute = (int) (time / 1000 / 60);
        return (minute < 10 ? "0" + minute : "" + minute) + ":" + (second < 10 ? "0" + second : "" + second);
    }
}
