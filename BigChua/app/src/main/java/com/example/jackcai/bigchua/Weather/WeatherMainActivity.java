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
import android.widget.ImageView;
import android.widget.TextView;
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
public class WeatherMainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private MyPageAdapter adapter;
    private List<View>views = new ArrayList<View>();
    private List<WeatherModel> weatherModels;
    public ProgressDialog progressDialog;
    private final static int [] WEATHERS = {R.mipmap.big_rain_weather,R.mipmap.cloud_weather,R.mipmap.lightning,R.mipmap.night_clouds,R.mipmap.rain_weather,R.mipmap.snow_,R.mipmap.strong_wind_,R.mipmap.sun_smile,R.mipmap.sw_wind_,R.mipmap.temperature_,R.mipmap.tornado,R.mipmap.wind};
    private ImageView ivGoBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        initWindow();
        getWindow().setFlags(Window.FEATURE_NO_TITLE, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = (ViewPager)findViewById(R.id.weather_view_pager);
        ivGoBack = (ImageView)findViewById(R.id.go_back);

        ivGoBack.setOnClickListener(this);
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


        View centerView = inflater.inflate(R.layout.weather_center_layout,null);
        views.add(centerView);

        View view = inflater.inflate(R.layout.detail_weather,null);
        views.add(view);



        adapter = new MyPageAdapter(views);
        viewPager.setAdapter(adapter);

        MyAysnWeatherTask aysnWeatherTask = new MyAysnWeatherTask(this);
        aysnWeatherTask.execute("成都");
    }

    public void callBack(List<WeatherModel> models ,CenterCity city){
        progressDialog.dismiss();
        if (models != null){
            weatherModels = models;
            WeatherList weatherList = new WeatherList(views.get(1),this,weatherModels);
            setCenter(city);
        }
    }

    /**
     *
     * @param center
     */
    private void setCenter(CenterCity center){
        View view = views.get(0);
        TextView tvLow = (TextView) view.findViewById(R.id.low_tempture);
        TextView tvHigh = (TextView) view.findViewById(R.id.high_tempture);
        TextView tvCenterTemp = (TextView) view.findViewById(R.id.weather_center_temper_tv);
        TextView tvAir = (TextView) view.findViewById(R.id.weather_center_air_tv);
        TextView tvWind = (TextView) view.findViewById(R.id.weather_center_wind_tv);
        TextView tvDayTime = (TextView) view.findViewById(R.id.weather_center_dayTime_tv);
        TextView tvhumidity = (TextView) view.findViewById(R.id.weather_center_humidity_tv);
        TextView tvDress = (TextView) view.findViewById(R.id.weather_center_dress_tv);
        TextView tvExres = (TextView) view.findViewById(R.id.weather_center_exrise_tv);
        ImageView ivIcon = (ImageView)view.findViewById(R.id.weather_center_icon_iv);

        tvLow.setText(weatherModels.get(0).getLow()+"℃");
        tvHigh.setText(center.getTempture());
        tvCenterTemp.setText(weatherModels.get(0).getHigh()+"");
        tvAir.setText(center.getAirCondition());
        tvWind.setText(center.getWind());
        tvDayTime.setText(center.getWeather().replace("℃",""));
        tvhumidity.setText(center.getHumidity());
        tvDress.setText(center.getDressing());
        tvExres.setText(center.getExercise());
        ivIcon.setImageResource(WEATHERS[center.getDayTime()]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back:
                finish();
                break;
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
            centerCity.setColdIndex(result.getJSONObject(0).getString("coldIndex"));
            centerCity.setDressing(result.getJSONObject(0).getString("dressingIndex"));
            centerCity.setExercise(result.getJSONObject(0).getString("exerciseIndex"));
            centerCity.setHumidity(result.getJSONObject(0).getString("humidity"));
            centerCity.setTempture(result.getJSONObject(0).getString("temperature"));
            centerCity.setWind(result.getJSONObject(0).getString("wind"));
            setDayTime(centerCity,result.getJSONObject(0).getString("weather"));
            centerCity.setWeather(result.getJSONObject(0).getString("weather"));
            JSONArray future = result.getJSONObject(0).getJSONArray("future");



            WeatherModel model0 = new WeatherModel();
            JSONObject object1 = future.getJSONObject(0);
            try {


                model0.setDate(object1.getString("date"));
                setDayTime(model0,object1.getString("dayTime"));
                setNight(model0,object1.getString("night"));
                setLowHigh(model0,object1.getString("temperature"));
                setWeek(model0,object1.getString("week"));

            }catch (JSONException e){
                if (model0.getDayTime() == 0){
                    setNight(model0,object1.getString("night"));
                }
                setLowHigh(model0,object1.getString("temperature"));
                setWeek(model0,object1.getString("week"));
            }
            weatherModels.add(model0);

            int j = future.length();
            for (int i = 1; i < j; ++i){
                JSONObject object = future.getJSONObject(i);
                WeatherModel model = new WeatherModel();
                model.setDate(object.getString("date"));
                setDayTime(model,object.getString("dayTime"));
                setNight(model,object.getString("night"));
                setLowHigh(model,object.getString("temperature"));
                setWeek(model,object.getString("week"));
                weatherModels.add(model);
            }
            activity.callBack(weatherModels,centerCity);
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
        if (value.length > 1)
            model.setLow(Integer.parseInt(value[1].trim()));
        else {
            model.setLow(15);
        }
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

    private void setDayTime(CenterCity model,String con){
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
    private String coldIndex;
    private String dressing;
    private String exercise;
    private String humidity;
    private String tempture;
    private int dayTime;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    private String weather;

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public String getHumidity() {

        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTempture() {
        return tempture;
    }

    public void setTempture(String tempture) {
        this.tempture = tempture;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    private String wind;

    public String getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(String airCondition) {
        this.airCondition = airCondition;
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