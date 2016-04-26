package com.example.jackcai.bigchua.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.UserVerify.LoginActivity;
import com.example.jackcai.bigchua.utils.DownLoadImage;
import com.example.jackcai.bigchua.utils.DownLoadImageable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class News extends Fragment implements AdapterView.OnItemClickListener , BaseSliderView.OnSliderClickListener{

    private ListView listView;
    private List<NewsModel> newsModelList;
    private List<AdsModel> adsModelList;
    private MyNewsListAdapter adapter ;
    private ProgressBar loadPb;
    private static String URL_HEAD = "http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html?from=toutiao&size=20&passport=&devId=tJ9WJmNCbemaYAkn9hmvNg%3D%3D&lat=30T6CPAny3Qjfrm2aZ0Iyg%3D%3D&lon=TdJ6mqRwrgAvXDYm6qorxw%3D%3D&version=5.4.3&net=wifi&ts=1450750816&sign=JYtJRfoZ%2FZUob1Ul08iHiZVsutvrccBfEGkTdNMwqyl48ErR02zJ6%2FKXOnxX046I&encryption=1&canal=baidu_news";
    private static String URL_NEWS_LIST = "http://r.inews.qq.com/getQQNewsUnreadList?uid=9bbb976dec28f6ae&omgbizid=bb4c10d2d92de041fc6a894637deac8ac5700050210b16&Cookie=%20lskey%3D%3B%20luin%3D%3B%20skey%3D%3B%20uin%3D%3B%20logintype%3D0%20&qn-rid=391487196&store=5&hw=TiantianVM_TianTian&devid=950790062947205&qn-sig=46f4c335f6311193104bf7d6218161b4&user_chlid=news_news_finance%2Cnews_news_ent%2Cnews_news_sports%2Cnews_news_tech%2Cnews_news_ssh%2Cnews_news_mil%2Cnews_news_lad&screen_width=600&mac=67%253Afd%253A0e%253A46%253A86%253A40&last_time=1450752215&chlid=news_news_top&appver=18_android_4.8.7&qqnetwork=wifi&forward=0&page=0&last_id=NEW2015122201243000&mid=fb7dd60a37d004a54e059fd9613257b8f2457515&imsi=310260835531535&omgid=13b925aa2f9a5446a1fabb239e0df59cb60a0010210b16&apptype=android&screen_height=1024";
    private SliderLayout sliderLayout;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_news, container, false);
        listView = (ListView)view.findViewById(R.id.news_list_view);
        loadPb = (ProgressBar)view.findViewById(R.id.news_list_pb);
        loadPb.setVisibility(View.VISIBLE);


        LayoutInflater headerInflater= LayoutInflater.from(getContext());
        View headerView = headerInflater.inflate(R.layout.news_img_slider,null);
        relativeLayout = (RelativeLayout) headerView.findViewById(R.id.header_frame);
        sliderLayout = (SliderLayout) headerView.findViewById(R.id.news_imgs_slider);


        listView.addHeaderView(relativeLayout);
        loadData();
        return view;
    }

    public void loadData(){
        AsynGetNewsData loadHead = new AsynGetNewsData(this,1);
        loadHead.execute(URL_HEAD);
        AsynGetNewsData loadNewslist = new AsynGetNewsData(this,0);
        loadNewslist.execute(URL_NEWS_LIST);
    }
    /**
     * 设置新闻数据
     * @param modeList
     * @param adsModelList
     */
    public void setModelList(List<NewsModel> modeList,List<AdsModel> adsModelList,int type){
        if (type == 0){
            if (this.newsModelList != null){
                this.newsModelList.addAll(modeList);
            }else{
                this.newsModelList = modeList;
            }
        }else{
            if (this.adsModelList != null){
                this.adsModelList.addAll(adsModelList);
            }else
            {
                this.adsModelList = adsModelList;
            }
        }
    }

    public void loadDataFinished( int type){
        if (type == 0){//新闻内容加载完成
            if (adapter != null){
                adapter.notifyDataSetChanged();
                return;
            }
            loadPb.setVisibility(View.GONE);
            adapter = new MyNewsListAdapter(newsModelList,getContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }else {//头部加载完成
            loadHeader();
        }
    }


    public void loadHeader(){
        sliderLayout.setClickable(true);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        for (AdsModel model : adsModelList) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView.description(model.getTitle());
            textSliderView.image(model.getImgSrc());
            textSliderView.setOnSliderClickListener(this);
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.stopAutoCycle();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == newsModelList.size()){
            return;
        }//
        Intent intent = new Intent(getContext(),WebBrowser.class);
        intent.putExtra("title",newsModelList.get(position).getTitle());
        intent.putExtra("url",newsModelList.get(position).getContentUrl());
        startActivity(intent);
    }

    /**
     * 滑动项被点击
     * @param slider
     */
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getContext(),"dsaad",Toast.LENGTH_SHORT).show();
    }
}


/**
 *
 */
class MyNewsListAdapter extends BaseAdapter implements DownLoadImageable{
    private List<NewsModel> modelList = new ArrayList<NewsModel>();
    private LayoutInflater inflater;
    private Map<String,Bitmap> bitmapImgCache = new HashMap<String ,Bitmap>();
    private Context context;
    private static  Bitmap defaultBmpLoadWait ;
    private static Bitmap defaultType;


    public MyNewsListAdapter(List<NewsModel> list, Context context){
        this.modelList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

        if(defaultBmpLoadWait == null){
            defaultBmpLoadWait = BitmapFactory.decodeResource(context.getResources(),R.drawable.biz_pic_wait_down_img);
        }

        if(defaultType == null){
            defaultType = BitmapFactory.decodeResource(context.getResources(),R.drawable.biz_search_result_special);
        }

    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsHolder holder = null;
        if (convertView  == null){
            holder = new NewsHolder();
            convertView = inflater.inflate(R.layout.news_list_item,null);
            holder.desc = (TextView)convertView.findViewById(R.id.news_item_desc);
            holder.fellow = (TextView)convertView.findViewById(R.id.news_item_fellow);
            holder.imageView = (ImageView)convertView.findViewById(R.id.news_item_img);
            holder.title = (TextView)convertView.findViewById(R.id.news_item_title);
            holder.imgType = (ImageView)convertView.findViewById(R.id.news_item_type);

            convertView.setTag(holder);
        }else {
            holder = (NewsHolder)convertView.getTag();
        }

        NewsModel model = modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDesc());
        if(position == 0){
            holder.imgType.setVisibility(View.VISIBLE);
            holder.imgType.setImageBitmap(defaultType);
            holder.fellow.setVisibility(View.GONE);
        }else{
            holder.imgType.setVisibility(View.GONE);
            holder.fellow.setText(model.getCommentCount());
            holder.fellow.setVisibility(View.VISIBLE);
        }
//加载图片
        if (bitmapImgCache.containsKey(model.getImgUrl())){
            holder.imageView.setImageBitmap(bitmapImgCache.get(model.getImgUrl()));
        }else{
            bitmapImgCache.put(model.getImgUrl(),defaultBmpLoadWait);
            holder.imageView.setImageBitmap(defaultBmpLoadWait);
            DownLoadImage downLoadImage = new DownLoadImage(context,this,position,model.getImgUrl(),0);
            downLoadImage.execute(model.getImgUrl());
        }

        return convertView;
    }

    @Override
    public void completedAsynImage(int position, Bitmap bitmap, String srcUrl, int type) {
        bitmapImgCache.put(srcUrl,bitmap);
        notifyDataSetChanged();
    }

    class NewsHolder{
        ImageView imageView;
        TextView title;
        TextView desc;
        TextView fellow;
        ImageView imgType;
    }
}

/**
 * 异步加载新闻数据
 */

class AsynGetNewsData extends AsyncTask<String,Void,String>{
    private List<NewsModel> modelList ;
    private List<AdsModel> adsModelList ;
    private News news;
    private int type;

    public AsynGetNewsData(News news,int type){
        this.news = news;
        this.type = type;
        if (type == 0){
            modelList = new ArrayList<NewsModel>();
        }else{
            adsModelList = new ArrayList<AdsModel>();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String requestUrl = params[0];
        URL url = null;
        StringBuilder content = new StringBuilder();
        try {
            url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line);
            }
            inputStream.close();
        }catch (MalformedURLException e){
            System.err.print(e.getMessage());
        }catch (IOException e){
            System.err.print(e.getMessage());
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null){
            parseJsonData(s);
            if (type == 0){
                news.setModelList(modelList,null,type);
            }else{
                news.setModelList(null,adsModelList,1);
            }
            news.loadDataFinished(type);// 加载数据完成后 回调数据
        }else{
            Toast.makeText(news.getContext(),"加载错误!",Toast.LENGTH_SHORT).show();
        }
    }

    //解析json
    void parseJsonData(String json){
        if (type == 1){ // 加载头条
            try{
                JSONObject root  = new JSONObject(json);
                JSONArray rootJson = root.getJSONArray("T1348647909107");
                JSONObject adsRoot = rootJson.getJSONObject(0);
                JSONArray adsArray = adsRoot.getJSONArray("ads");

                //获取滚动新闻
                for (int i = 0; i < adsArray.length() ;i++){
                    JSONObject obj = adsArray.getJSONObject(i);
                    AdsModel model = new AdsModel();
                    model.setImgSrc(obj.getString("imgsrc"));
                    model.setTitle(obj.getString("title"));
                    model.setUrl(obj.getString("url"));
                    adsModelList.add(model);
                }
                root = null;
                rootJson = null;
                adsRoot = null;
                adsArray =  null;
                System.gc();
            }catch (JSONException e){
                Toast.makeText(news.getContext(),"解析数据出错!",Toast.LENGTH_SHORT).show();
                System.err.println(e.getMessage());
            }
        }else{ // type == 0
            try{
                JSONObject root = new JSONObject(json);
                JSONArray newslist = root.getJSONArray("newslist");
                for (int i = 0; i < newslist.length();i++){
                    JSONObject obj = newslist.getJSONObject(i);
                    NewsModel model = new NewsModel();
                    model.setTitle(obj.getString("title"));
                    model.setCommentCount(obj.getString("pushCommentCount")+"跟帖");
                    model.setDesc(obj.getString("abstract"));
                    model.setContentUrl(obj.getString("url"));
                    JSONArray imgs = obj.getJSONArray("thumbnails");
                    model.setImgUrl(imgs.getString(0));
                    modelList.add(model);
                }

                root = null;
                newslist = null;
                System.gc();
            }catch (JSONException e){
                Toast.makeText(news.getContext(),"解析数据出错!",Toast.LENGTH_SHORT).show();
                System.err.println(e.getMessage());
            }
        }
    }
}
