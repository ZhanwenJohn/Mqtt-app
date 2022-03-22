package com.example.mqttpro.bean;

import com.google.gson.annotations.SerializedName;

public class ThLimiteBean {
    @SerializedName("temp_up")
    private int temp_up;

    @SerializedName("temp_low")
    private int temp_low;

    @SerializedName("humi_up")
    private int humi_up;

    @SerializedName("humi_low")
    private int humi_low;

    public int getTemp_up() {
        return temp_up;
    }

    public void setTemp_up(int temp_up) {
        this.temp_up = temp_up;
    }

    public int getTemp_low() {
        return temp_low;
    }

    public void setTemp_low(int temp_low) {
        this.temp_low = temp_low;
    }

    public int getHumi_up() {
        return humi_up;
    }

    public void setHumi_up(int humi_up) {
        this.humi_up = humi_up;
    }

    public int getHumi_low() {
        return humi_low;
    }

    public void setHumi_low(int humi_low) {
        this.humi_low = humi_low;
    }

    @Override
    public String toString() {
        return "ThLimiteBean{" +
                "temp_up=" + temp_up +
                ", temp_low=" + temp_low +
                ", humi_up=" + humi_up +
                ", humi_low=" + humi_low +
                '}';
    }
}
