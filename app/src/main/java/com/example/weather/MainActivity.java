package com.example.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.adapter.FutureWea;
import com.example.weather.bean.DayWeatherBean;
import com.example.weather.bean.WeatherBean;
import com.example.weather.util.NetUtil;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;
    private TextView tvWeather,tvTemLowHigh,tvWin,tvDate,tvupdateTime,tvlike;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWea futureWea;
    private String cityName,tem,weat;

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String weather = (String) msg.obj;
                Log.d("fan","---weather---"+weather);
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                Log.d("fan","---解析----"+ weatherBean.toString());
                updateUiOfWeather(weatherBean);
            }
        }
    };

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if (weatherBean == null){
            return;
        }
        List<DayWeatherBean> data = weatherBean.getData();
        DayWeatherBean todayWeather = data.get(0);
        if (todayWeather == null){
            return;
        }
        String temLow = todayWeather.getTem_night();
        String temHigh = todayWeather.getTem_day();
        tem = temLow + "°C~" + temHigh + "°C";
        tvTemLowHigh.setText(tem);

        tvupdateTime.setText(weatherBean.getUpdate_time());
        tvWeather.setText(todayWeather.getWea());
        weat = todayWeather.getWea();
        tvDate.setText(todayWeather.getDate());
        tvWin.setText(todayWeather.getWin()+todayWeather.getWin_speed());
        ivWeather.setImageResource(getImageOfWea(todayWeather.getWea_img()));

        data.remove(0);
        //未来天气
        futureWea = new FutureWea(this,data);
        rlvFutureWeather.setAdapter(futureWea);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rlvFutureWeather.setLayoutManager(layoutManager);
        SharedPreferences sharedPreferences= getSharedPreferences("cityName", MODE_PRIVATE);
        String name=sharedPreferences.getString("city","");
        String tem=sharedPreferences.getString("tem","");
        String wea=sharedPreferences.getString("wea","");
        tvlike.setText(name +  "   " + wea +"  " + tem );
    }
    private int getImageOfWea(String weaStr){
        //xue、lei、shachen、wu、bingbao、yun、yu、yin、qing
        int result = 0;
        switch (weaStr){
            case "xue" : result = R.drawable.biz_plugin_weather_daxue;break;
            case "lei" : result = R.drawable.biz_plugin_weather_leizhenyu;break;
            case "shachen" : result = R.drawable.biz_plugin_weather_shachenbao;break;
            case "wu" : result = R.drawable.biz_plugin_weather_wu;break;
            case "bingbao" : result = R.drawable.biz_plugin_weather_leizhenyubingbao;break;
            case "yun" : result = R.drawable.biz_plugin_weather_duoyun;break;
            case "yu" : result = R.drawable.biz_plugin_weather_dayu;break;
            case "yin" : result = R.drawable.biz_plugin_weather_yin;break;
            case "qing" : result = R.drawable.biz_plugin_weather_qing;break;
            default:result = R.drawable.biz_plugin_weather_qing;break;
        }
    return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private  void initView(){

        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this,R.layout.sp_item_layout,mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCity = mCities[position];
                cityName = selectedCity;
                getWeatherOfCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvWeather = findViewById(R.id.tv_weather);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWin = findViewById(R.id.tv_win);
        tvDate = findViewById(R.id.tv_date);
        tvupdateTime = findViewById(R.id.tv_updateTime);
        ivWeather = findViewById(R.id.iv_weather);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
        tvlike = findViewById(R.id.like);
    }

    private void getWeatherOfCity(String selectedCity) {
        //开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                String weatherOfCity = NetUtil.getWeatherOfCity(selectedCity);
                //Handler传数据给主线程
                Message message = Message.obtain();
                message.what = 0;
                message.obj = weatherOfCity;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void like(View view) {
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("cityName",MODE_PRIVATE);
        //获取Editor对象的引用
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //将获取过来的值放入文件
        editor.putString("city", cityName);
        editor.putString("wea",weat);
        editor.putString("tem", tem);
        // 提交数据
        editor.commit();
        Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
    }

    public void refresh(View view) {
        getWeatherOfCity(cityName);
        Toast.makeText(this,"刷新成功",Toast.LENGTH_SHORT).show();
    }
}