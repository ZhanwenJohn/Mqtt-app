package com.example.mqttpro.user;

public class ThDataUser {

    private String Temperature;
    private String Humidity;

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    @Override
    public String toString() {
        return "ThDataUser{" +
                "Temperature='" + Temperature + '\'' +
                ", Humidity='" + Humidity + '\'' +
                '}';
    }

    public void getTemperature(double temperature) {
    }
}
