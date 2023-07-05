package com.example.weather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {
    public static final String URL_WEATHER = "https://www.yiketianqi.com/free/week?unescape=1&appid=78438456&appsecret=uT6vNxFq";

    public static String doGet(String urlStr){
        String result="";
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        //连接网络
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            //从连接中读取数据二进制
            InputStream inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            //二进制流送入缓冲区
            bufferedReader = new BufferedReader(inputStreamReader);
            //从缓冲区逐行读取字符串
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null){
                connection.disconnect();
            }
            if (inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
    public static String getWeatherOfCity(String city){
        //拼接出天气URL
        String weatherUrl = URL_WEATHER+"&city="+city;
        Log.d("fan","----weatherUrl----"+weatherUrl);
        String weatherResult = doGet(weatherUrl);
        Log.d("fan","----weatherResult----"+weatherResult);
        return weatherResult;
    }
}
