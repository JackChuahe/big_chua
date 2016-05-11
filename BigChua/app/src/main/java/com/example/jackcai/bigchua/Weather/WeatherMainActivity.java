package com.example.jackcai.bigchua.Weather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.common.SetStatusBarTextColor;
import com.example.jackcai.bigchua.common.WeatherModel;
import com.example.jackcai.bigchua.pics.MyPageAdapter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackCai on 2016/5/11.
 */
public class WeatherMainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyPageAdapter adapter;
    private List<View>views = new ArrayList<View>();
    private List<WeatherModel> weatherModels;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        initWindow();
        getWindow().setFlags(Window.FEATURE_NO_TITLE, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = (ViewPager)findViewById(R.id.weather_view_pager);
        progressDialog  = new ProgressDialog(this);
        initialData();
        SetStatusBarTextColor.setMiuiStatusBarDarkMode(this,true);
    }

    private void initWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    private void initialData(){
        progressDialog.setMessage("正在获取天气信息,Waiting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //设置内容 下载数据
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.detail_weather,null);
        views.add(view);


        adapter = new MyPageAdapter(views);
        viewPager.setAdapter(adapter);

        MyAysnWeatherTask aysnWeatherTask = new MyAysnWeatherTask(this);
        aysnWeatherTask.execute("成都");
    }

    public void callBack(List<WeatherModel> models){
        if (models != null){
            weatherModels = models;
            WeatherList weatherList = new WeatherList(views.get(0),this,weatherModels);
        }
    }
}

class MyAysnWeatherTask extends AsyncTask<String,Void,String>{
    private List<WeatherModel> weatherModels = new ArrayList<WeatherModel>();
    private WeatherMainActivity activity;
    private   static String GET_PATH = "/v1/weather/query?key=12a57feec48ca&city=";
    private static String URL_PROVONCE = "&province=";
    private static String HOST = "apicloud.mob.com";

    public MyAysnWeatherTask(WeatherMainActivity activity){
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {

        String queryContent = params[0];
        String queryResult ;

        HttpClient client = new HttpClient();
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 80, "http");

        try {
            queryContent =  java.net.URLEncoder.encode(queryContent,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String path = GET_PATH + queryContent + URL_PROVONCE;
        GetMethod get = new GetMethod(path);
        get.addRequestHeader(
                "Accept",
                "*/*");
        get.addRequestHeader("User-Agent", "Youdao Desktop Dict (Windows 6.2.9200)");
        get.addRequestHeader("Host", HOST);
        get.addRequestHeader("Connection",
                "Keep-Alive");

        try {
            client.executeMethod(get);
            queryResult = get.getResponseBodyAsString();

        }catch (Exception e){
            // Toast.makeText(context,"网络请求失败！",Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
            get.releaseConnection();
            return null;
        }
        get.releaseConnection();

        return queryResult;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            String msg = obj.getString("msg");
            if (!msg.equals("success")){
                Toast toast = Toast.makeText(activity,"数据加载错误！",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                return;
            }

            JSONArray result = obj.getJSONArray("result");
            CenterCity centerCity = new CenterCity();
            centerCity.setAirCondition(result.getJSONObject(0).getString("airCondition"));
            centerCity.setCity(result.getJSONObject(0).getString("city"));
            centerCity.setColdIndex(result.getJSONObject(0).getString("coldIndex"));
            centerCity.setDressing(result.getJSONObject(0).getString("dressingIndex"));
            centerCity.setExercise(result.getJSONObject(0).getString("exerciseIndex"));
            JSONArray future = result.getJSONObject(0).getJSONArray("future");

            int j = future.length();
            for (int i = 0; i < j; ++i){
                JSONObject object = future.getJSONObject(i);
                WeatherModel model = new WeatherModel();
                model.setDate(object.getString("date"));
                setDayTime(model,object.getString("dayTime"));
                setNight(model,object.getString("night"));
                setLowHigh(model,object.getString("temperature"));
                setWeek(model,object.getString("week"));
                weatherModels.add(model);
            }
            activity.callBack(weatherModels);
        } catch (JSONException e) {
            activity.progressDialog.dismiss();
           Toast toast = Toast.makeText(activity,"解析数据出错!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    private void setWeek(WeatherModel model ,String con){
        if (con.equals("今天")){
            model.setWeak("TOD");
        }else if (con.equals("星期一")){
            model.setWeak(WeatherModel.WEEKS[0]);
        }else if (con.equals("星期二")){
            model.setWeak(WeatherModel.WEEKS[1]);
        }else if (con.equals("星期三")){
            model.setWeak(WeatherModel.WEEKS[2]);
        }else if (con.equals("星期四")){
            model.setWeak(WeatherModel.WEEKS[3]);
        }else if (con.equals("星期五")){
            model.setWeak(WeatherModel.WEEKS[4]);
        }else if (con.equals("星期六")){
            model.setWeak(WeatherModel.WEEKS[5]);
        }else if (con.equals("星期日")){
            model.setWeak(WeatherModel.WEEKS[6]);
        }
    }
    private void setLowHigh(WeatherModel model,String con){
        String value[] = con.replace("°C","").split("/");
        model.setHigh(Integer.parseInt(value[0].trim()));
        model.setLow(Integer.parseInt(value[1].trim()));
    }
    private void setDayTime(WeatherModel model,String con){
        if (con.equals("晴")){
            model.setDayTime(WeatherModel.SUN);
        }else if (con.equals("中雨")){
            model.setDayTime(WeatherModel.RAIN);
        }else if(con.equals("小雨")){
            model.setDayTime(WeatherModel.RAIN);
        }else if (con.equals("阴")){
            model.setDayTime(WeatherModel.CLOUD);
        }else if (con.equals("多云")){
            model.setDayTime(WeatherModel.CLOUD);
        }else if (con.equals("阴天")){
            model.setDayTime(WeatherModel.NIGTH_CLOUD);
        }else if (con.equals("阵雨")){
            model.setDayTime(WeatherModel.BIG_RAIN);
        }else{
            model.setDayTime(WeatherModel.SMALL_WIND);
        }
    }

    private void setNight(WeatherModel model,String con){
        if (con.equals("晴")){
            model.setNight(WeatherModel.SUN);
        }else if (con.equals("中雨")){
            model.setNight(WeatherModel.RAIN);
        }else if(con.equals("小雨")){
            model.setNight(WeatherModel.RAIN);
        }else if (con.equals("阴")){
            model.setNight(WeatherModel.CLOUD);
        }else if (con.equals("多云")){
            model.setNight(WeatherModel.CLOUD);
        }else if (con.equals("阴天")){
            model.setNight(WeatherModel.NIGTH_CLOUD);
        }else if (con.equals("阵雨")){
            model.setNight(WeatherModel.BIG_RAIN);
        }else{
            model.setNight(WeatherModel.SMALL_WIND);
        }
    }
}
class CenterCity{
    private String airCondition;
    private String city;
    private String coldIndex;
    private String dressing;
    private String exercise;

    public String getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(String airCondition) {
        this.airCondition = airCondition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getColdIndex() {
        return coldIndex;
    }

    public void setColdIndex(String coldIndex) {
        this.coldIndex = coldIndex;
    }

    public String getDressing() {
        return dressing;
    }

    public void setDressing(String dressing) {
        this.dressing = dressing;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }
}