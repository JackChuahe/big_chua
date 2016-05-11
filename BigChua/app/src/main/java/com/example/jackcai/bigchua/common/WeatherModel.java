package com.example.jackcai.bigchua.common;

/**
 * Created by JackCai on 2016/5/11.
 */
public class WeatherModel {
    private String date;
    private int dayTime;
    private int night;
    private int low;
    private int high;
    private String weak;

    public final static String [] WEEKS = {"MON","THU","WEN","THRE","FRI","STA","SUN"};
    public final static int BIG_RAIN = 0;
    public final static int CLOUD = 1;
    public final static int LIGHTING = 2;
    public final static int NIGTH_CLOUD = 3;
    public final static int RAIN = 4;
    public final static int SNOW = 5;
    public final static int BIG_WIND = 6;
    public final static int SUN = 7;
    public final static int SMALL_WIND = 8;
    public final static int HIGH_TEMP = 9;
    public final static int TORNATO = 10;
    public final static int WIND = 11;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public int getNight() {
        return night;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public String getWeak() {
        return weak;
    }

    public void setWeak(String weak) {
        this.weak = weak;
    }
}
