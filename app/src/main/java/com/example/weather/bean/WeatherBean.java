package com.example.weather.bean;

import java.util.List;

/**
 *     "cityid": "101010100",
 *     "city": "北京",
 *     "update_time": "2023-06-30 19:06:07",
 */
public class WeatherBean {

    private String cityid;
    private String city;
    private String update_time;
    private List<DayWeatherBean> data;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<DayWeatherBean> getData() {
        return data;
    }

    public void setData(List<DayWeatherBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "cityid='" + cityid + '\'' +
                ", city='" + city + '\'' +
                ", update_time='" + update_time + '\'' +
                ", data=" + data +
                '}';
    }
}
