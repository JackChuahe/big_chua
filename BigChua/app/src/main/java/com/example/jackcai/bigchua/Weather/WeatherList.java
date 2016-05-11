package com.example.jackcai.bigchua.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.common.WeatherModel;

import java.util.List;


/**
 * Created by JackCai on 2016/5/11.
 */
public class WeatherList  {
    private List<WeatherModel> weatherModels;
    private View view;
    private WeatherMainActivity context;
    private ListView listView;
    private MyAdapter adapter;



    public WeatherList(View view,WeatherMainActivity context,List<WeatherModel> list){
        this.view = view;
        this.context = context;
        this.weatherModels = list;
        listView = (ListView)view.findViewById(R.id.detail_list_view);

        adapter = new MyAdapter(weatherModels,context);
        listView.setAdapter(adapter);
        //context.progressDialog.dismiss();
    }
}

class MyAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    List<WeatherModel> list;
    private final static int [] WEATHERS = {R.mipmap.big_rain_weather,R.mipmap.cloud_weather,R.mipmap.lightning,R.mipmap.night_clouds,R.mipmap.rain_weather,R.mipmap.snow_,R.mipmap.strong_wind_,R.mipmap.sun_smile,R.mipmap.sw_wind_,R.mipmap.temperature_,R.mipmap.tornado,R.mipmap.wind};


    public MyAdapter(List<WeatherModel> list,Context context){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.detail_weater_item,null);
            holder.ivIcon = (ImageView)convertView.findViewById(R.id.weather_item_icon);
            holder.tvDate = (TextView)convertView.findViewById(R.id.weather_item_date);
            holder.lyProgress =  (LinearLayout)convertView.findViewById(R.id.wather_item_progress);
            holder.tvHigh = (TextView)convertView.findViewById(R.id.weather_item_high);
            holder.tvLow = (TextView)convertView.findViewById(R.id.weather_item_low);
            holder.tvWeek = (TextView)convertView.findViewById(R.id.weather_item_week);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        WeatherModel model  = list.get(position);
        holder.ivIcon.setImageResource(WEATHERS[model.getDayTime()]);
        holder.tvDate.setText(model.getDate());
        holder.tvWeek.setText(model.getWeak());
        holder.tvLow.setText(model.getLow()+"℃");
        holder.tvHigh.setText(model.getHigh()+"℃");

        android.view.ViewGroup.LayoutParams lp = holder.lyProgress.getLayoutParams();
        lp.width = model.getHigh()*5;
        holder.lyProgress.setLayoutParams(lp);

        return convertView;
    }

    class Holder{
        ImageView ivIcon;
        TextView tvWeek;
        TextView tvDate;
        TextView tvLow;
        TextView tvHigh;
        LinearLayout lyProgress;
    }
}