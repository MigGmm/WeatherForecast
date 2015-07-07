package com.miguelgarcia.android.weatherforecast.object;

import android.graphics.Bitmap;

/**
 * Class with weather information.
 *
 * @author Miguel Francisco García del Morala Muñoz.
 */
public class ForecastDay {
    private String location;
    private Bitmap weatherIcon;
    private String weekDay;
    private String tempC;
    private String tempF;
    private String weatherDesc;
    private String humidity;
    private String precipitation;
    private String pressure;
    private String windSpeedKmh;
    private String windSpeedMph;
    private String direction;

    // Constructor.
    public ForecastDay() {}

    // Getters.
    public String getLocation() {
        return location;
    }

    public Bitmap getWeatherIcon() {
        return weatherIcon;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getTempC() {
        return tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public String getWindSpeedMph() {
        return windSpeedMph;
    }

    public String getDirection() {
        return direction;
    }

    // Setters.
    public void setLocation(String location) {
        this.location = location;
    }

    public void setWeatherIcon(Bitmap weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC + "ºC";
    }

    public void setTempF(String tempF) {
        this.tempF = tempF + "ºF";
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity + "%";
    }

    public void setPrecipitation(String precipitation) { this.precipitation = precipitation + " mm"; }

    public void setPressure(String pressure) {
        this.pressure = pressure + " hPa";
    }

    public void setWindSpeedKmh(String windSpeed) {
        this.windSpeedKmh = windSpeed + " Km/h";
    }

    public void setWindSpeedMph(String windSpeed) {
        this.windSpeedMph = windSpeed + " mph";
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


}
