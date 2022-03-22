package com.example.mqttpro.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherBean {

    @SerializedName("cityid")
    private String cityId;

    @SerializedName("city")
    private String cityName;

    @SerializedName("data")
    private List<DataWeatherBean> mDataWeatherBean;

    @SerializedName("update_time")
    private String update_time;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<DataWeatherBean> getmDataWeatherBean() {
        return mDataWeatherBean;
    }

    public void setmDataWeatherBean(List<DataWeatherBean> mDataWeatherBean) {
        this.mDataWeatherBean = mDataWeatherBean;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", mDataWeatherBean=" + mDataWeatherBean +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
/*
"cityid":"101161010",
"city":"陇南",
"cityEn":"longnan",
"country":"中国",
"countryEn":"China",
"update_time":"2022-01-13 16:08:11",
 */