package com.example.mqttpro.bean;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class THdataBean {

    @SerializedName("Temperature")
    private double Temperature;

    @SerializedName("Humidity")
    private double Humidity;

    @SerializedName("alarmFlag")
    private int alarmFlag;
    @SerializedName("ledFlag")
    private int ledFlag;

    @SerializedName("beepFlag")
    private int beepFlag;

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public int getLedFlag() {
        return ledFlag;
    }

    public void setLedFlag(int ledFlag) {
        this.ledFlag = ledFlag;
    }

    public int getBeepFlag() {
        return beepFlag;
    }

    public void setBeepFlag(int beepFlag) {
        this.beepFlag = beepFlag;
    }

    public int getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(int alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    @Override
    public String toString() {
        return "THdataBean{" +
                "Temperature=" + Temperature +
                ", Humidity=" + Humidity +
                ", alarmFlag=" + alarmFlag +
                ", ledFlag=" + ledFlag +
                ", beepFlag=" + beepFlag +
                '}';
    }

    /**
     * Int类型强转换String类型
     * @param number 传入数据
     * @return 返回字符串
     */
    public static String getIntegerString(int number){
        String numberStr = null;
        numberStr = Integer.toString(number);
        return numberStr;

    }

    /*
     * 如果是小数，保留一位，非小数，保留整数
     * @param number
     */
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.0"); //.0表示保留一位小数，三位小数就是三个0，以此类推
            numberStr = df.format(number);
        }
        return numberStr;
    }
}
