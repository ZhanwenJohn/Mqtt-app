package com.example.mqttpro.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class DataWeatherBean {

    @SerializedName("date")
    private String date;

    @SerializedName("week")
    private String week;

    @SerializedName("wea")
    private String wea;

    @SerializedName("tem")
    private String tem;

    @SerializedName("tem1")
    private String tem1;

    @SerializedName("tem2")
    private String tem2;

    @SerializedName("air")
    private String air;

    @SerializedName("air_level")
    private String air_level;

    @SerializedName("air_tips")
    private String air_tips;

    @SerializedName("win_speed")
    private String win_speed;

    @SerializedName("win")
    private String[] win;

    @SerializedName("wea_img")
    private String wea_img;

    public String getWea_img() {
        return wea_img;
    }

    public void setWea_img(String wea_img) {
        this.wea_img = wea_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getTem1() {
        return tem1;
    }

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getAir_level() {
        return air_level;
    }

    public void setAir_level(String air_level) {
        this.air_level = air_level;
    }

    public String getAir_tips() {
        return air_tips;
    }

    public void setAir_tips(String air_tips) {
        this.air_tips = air_tips;
    }

    public String getWin_speed() {
        return win_speed;
    }

    public void setWin_speed(String win_speed) {
        this.win_speed = win_speed;
    }

    public String[] getWin() {
        return win;
    }

    public void setWin(String[] win) {
        this.win = win;
    }

    @Override
    public String toString() {
        return "DataWeatherBean{" +
                "date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", wea='" + wea + '\'' +
                ", tem='" + tem + '\'' +
                ", tem1='" + tem1 + '\'' +
                ", tem2='" + tem2 + '\'' +
                ", air='" + air + '\'' +
                ", air_level='" + air_level + '\'' +
                ", air_tips='" + air_tips + '\'' +
                ", win_speed='" + win_speed + '\'' +
                ", win=" + Arrays.toString(win) +
                ", wea_img='" + wea_img + '\'' +
                '}';
    }
}
/*
"day":"13日（星期四）",
"date":"2022-01-13",
"week":"星期四",
"wea":"阴",
"wea_img":"yin",
"wea_day":"阴",
"wea_day_img":"yin",
"wea_night":"阴",
"wea_night_img":"yin",
"tem":"10℃",
"tem1":"10℃",
"tem2":"0℃",
"humidity":"26%",
"visibility":"30km",
"pressure":"891",
"win":[
"东南风",
"北风"
],
"win_speed":"<3级",
"win_meter":"4km\/h",
"sunrise":"08:04",
"sunset":"18:13",
"air":"53",
"air_level":"良",
"air_tips":"空气好，可以外出活动，除极少数对污染物特别敏感的人群以外，对公众没有危害！",
"alarm":{
"alarm_type":"",
"alarm_level":"",
"alarm_content":""
},
 */
