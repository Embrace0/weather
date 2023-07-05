package com.example.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.bean.DayWeatherBean;

import java.util.List;

public class FutureWea extends RecyclerView.Adapter<FutureWea.WeatherViewHolder> {
    private Context mContext;
    private List<DayWeatherBean> mWeatherBeans;

    public FutureWea(Context context,List<DayWeatherBean> weatherBeans) {
        this.mContext = context;
        this.mWeatherBeans = weatherBeans;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item_layout, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeatherBean weatherBean = mWeatherBeans.get(position);

        String temLow = weatherBean.getTem_night();
        String temHigh = weatherBean.getTem_day();
        String tem = temLow + "°C~" + temHigh + "°C";
        holder.tvTemLowHigh.setText(tem);
        holder.tvWeather.setText(weatherBean.getWea());
        holder.tvDate.setText(weatherBean.getDate());
        holder.tvWin.setText(weatherBean.getWin() + weatherBean.getWin_speed());
        holder.ivWeather.setImageResource(getImageOfWea(weatherBean.getWea_img()));
    }

    @Override
    public int getItemCount() {
        return (mWeatherBeans == null) ? 0 : mWeatherBeans.size();
    }

    class  WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView tvWeather,tvTemLowHigh,tvWin,tvDate;
        ImageView ivWeather;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeather = itemView.findViewById(R.id.tv_weather);
            tvTemLowHigh = itemView.findViewById(R.id.tv_tem_low_high);
            tvWin = itemView.findViewById(R.id.tv_win);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivWeather = itemView.findViewById(R.id.iv_weather);
        }


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
}
