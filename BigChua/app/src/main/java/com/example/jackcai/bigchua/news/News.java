package com.example.jackcai.bigchua.news;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.jackcai.bigchua.pics.Pics;
import com.example.jackcai.bigchua.pics.PicsModel;
import com.example.jackcai.bigchua.pics.ViewPicsActivity;
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
    private List<PicsModel> adsModelList;
    private MyNewsListAdapter adapter ;
    private ProgressBar loadPb;
    private static String URL_HEAD = "http://yuetu.3g.qq.com/photo/s?aid=action_api&module=photo&action=photo4_channel_list&_t=1461895675346&cl_channel=manual&cl_page=1&cl_size=10&cl_openAd=0";
    private static String URL_NEWS_LIST = "http://r.inews.qq.com/getQQNewsUnreadList?uid=9bbb976dec28f6ae&omgbizid=bb4c10d2d92de041fc6a894637deac8ac5700050210b16&Cookie=%20lskey%3D%3B%20luin%3D%3B%20skey%3D%3B%20uin%3D%3B%20logintype%3D0%20&qn-rid=391487196&store=5&hw=TiantianVM_TianTian&devid=950790062947205&qn-sig=46f4c335f6311193104bf7d6218161b4&user_chlid=news_news_finance%2Cnews_news_ent%2Cnews_news_sports%2Cnews_news_tech%2Cnews_news_ssh%2Cnews_news_mil%2Cnews_news_lad&screen_width=600&mac=67%253Afd%253A0e%253A46%253A86%253A40&last_time=1450752215&chlid=news_news_top&appver=18_android_4.8.7&qqnetwork=wifi&forward=0&page=";
    private static String URL_NEWS_LIST_B = "&last_id=NEW2015122201243000&mid=fb7dd60a37d004a54e059fd9613257b8f2457515&imsi=310260835531535&omgid=13b925aa2f9a5446a1fabb239e0df59cb60a0010210b16&apptype=android&screen_height=1024";
    private SliderLayout sliderLayout;
    private RelativeLayout relativeLayout;
    private int currentPage = 0;
    private static final int MAX_PAGE = 10;
    private boolean isLoading = false;

    private ProgressBar pbLoadMore;
    private LinearLayout lyLoadMore;

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

        LayoutInflater footInflater = LayoutInflater.from(getContext());
        View footView = footInflater.inflate(R.layout.list_view_more,null);
        pbLoadMore = (ProgressBar) footView.findViewById(R.id.list_item_load_more_progressbar);
        lyLoadMore = (LinearLayout)footView.findViewById(R.id.list_item_load_more_button);

        listView.addFooterView(footView);
        listView.addHeaderView(relativeLayout);

        //注册弹出菜单
        registerForContextMenu(listView);
        loadData();
        return view;
    }

    public void loadData(){
        if (currentPage >= MAX_PAGE){
            Toast.makeText(getContext(),"没有更多了!",Toast.LENGTH_SHORT).show();
            return;
        }
        isLoading = true;
        AsynDownLoadHeadPics loadHead = new AsynDownLoadHeadPics(this,1);
        loadHead.execute(URL_HEAD);
        AsynGetNewsData loadNewslist = new AsynGetNewsData(this,0);
        loadNewslist.execute(URL_NEWS_LIST+currentPage+URL_NEWS_LIST_B);
        if (currentPage != 0 ){
            lyLoadMore.setVisibility(View.GONE);
            pbLoadMore.setVisibility(View.VISIBLE);
        }
        ++currentPage;

    }
    /**
     * 设置新闻数据
     * @param modeList
     * @param adsModelList
     */
    public void setModelList(List<NewsModel> modeList, List<PicsModel> adsModelList, int type){
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
        isLoading = false;
        if (type == 0){//新闻内容加载完成
            if (adapter != null){
                adapter.notifyDataSetChanged();
                lyLoadMore.setVisibility(View.VISIBLE);
                pbLoadMore.setVisibility(View.GONE);
                return;
            }
            loadPb.setVisibility(View.GONE);
            adapter = new MyNewsListAdapter(newsModelList,getContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            if (newsModelList.size() == 0)return;
            getNotification(newsModelList.get(1));
        }else {//头部加载完成
            loadHeader();
        }
    }


    public void loadHeader(){

        sliderLayout.setClickable(true);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        for (PicsModel model : adsModelList) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView.description(model.getTitle());
            textSliderView.image(model.getHeadImgUrl());
            textSliderView.setOnSliderClickListener(this);
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.startAutoCycle();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == newsModelList.size()+1 && !isLoading){//点击了最后一项
            loadData();
            return;
        }else if (position == newsModelList.size()+1 && isLoading){
            return;
        }
        Intent intent = new Intent(getContext(),WebBrowser.class);
        intent.putExtra("title",newsModelList.get(position-1).getTitle());
        intent.putExtra("url",newsModelList.get(position-1).getContentUrl());
        startActivity(intent);
    }

    /**
     * 滑动项被点击
     * @param slider
     */
    @Override
    public void onSliderClick(BaseSliderView slider) {
        PicsModel model = adsModelList.get(sliderLayout.getCurrentPosition());
        Intent intent = new Intent(getActivity(),ViewPicsActivity.class);
        intent.putExtra("json" ,model.getImgList().toString());
        intent.putExtra("title",model.getTitle());
        startActivity(intent);
    }

    /**
     * 注册分享事件
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"分享新闻");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();  //获得AdapterContextMenuInfo,以此来获得选择的listview项目
        if (menuInfo.position > newsModelList.size() || menuInfo.position < 1)return false;
        NewsModel model = newsModelList.get(menuInfo.position);


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getContentUrl());
        startActivity(sendIntent);
        return super.onContextItemSelected(item);
    }

    /**
     * 推送通知 最新新闻
     */
    public void getNotification(NewsModel model){

        final NotificationManager nm = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getActivity());

        //参数
        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.pics_travel)).getBitmap();
        String info = model.getDesc();

        //action
        Intent intent = new Intent(getContext(),WebBrowser.class);
        intent.putExtra("title",model.getTitle());
        intent.putExtra("url",model.getContentUrl());

        builder.setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.pics_travel)
                .setContentTitle(model.getTitle())
                .setContentText(info)
                .setTicker(model.getTitle())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(getActivity(), 0, intent, 0));
        final Notification n = builder.build();
        //Toast.makeText(getActivity(),"notification",Toast.LENGTH_SHORT).show();
        nm.notify(model.getTitle().hashCode(),n);
    }
}


/**
 *
 */
class MyNewsListAdapter extends BaseAdapter implements DownLoadImageable{
    private List<NewsModel> modelList = new ArrayList<NewsModel>();
    private LayoutInflater inflater;
    public Map<String,Bitmap> bitmapImgCache = new HashMap<String ,Bitmap>();
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
    private News news;
    private int type;

    public AsynGetNewsData(News news,int type) {
        this.news = news;
        this.type = type;
        modelList = new ArrayList<NewsModel>();
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
            news.setModelList(modelList,null,type);
            news.loadDataFinished(type);// 加载数据完成后 回调数据
        }else{
            Toast.makeText(news.getContext(),"加载错误!",Toast.LENGTH_SHORT).show();
        }
    }

    //解析json
    void parseJsonData(String json){
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



//异步下载请求
class AsynDownLoadHeadPics extends AsyncTask<String,Void,String>{
    private List<PicsModel> modelList = new ArrayList<PicsModel>();
    private News news;
    private int type;

    public AsynDownLoadHeadPics(News news,int type){
        this.news = news;
        this.type = type;
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
            news.setModelList(null,modelList,type);
            news.loadDataFinished(type);// 加载数据完成后 回调数据
        }else{
            Toast.makeText(news.getContext(),"加载错误!",Toast.LENGTH_SHORT).show();
        }
    }

    void parseJsonData(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject root = jsonObject.getJSONObject("photo4_channel_list");
            JSONObject dataObj = root.getJSONObject("data");
            JSONArray list = dataObj.getJSONArray("list");
            for(int i = 0; i < list.length() && i <= 5 ;i++){
                PicsModel model = new PicsModel();
                JSONObject obj = list.getJSONObject(i);
                if(obj.getInt("dataType") != 1||obj.getBoolean("is_ad") || obj.getBoolean("is_pos"))continue;
                model.setTitle(obj.getString("title"));
                model.setCommentCount(obj.getString("coralComment"));
                model.setFavorCount(obj.getString("favor"));
                model.setHeadImgUrl(obj.getString("coverimg"));
                model.setShareUrl(obj.getString("imageTagCn"));
                model.setImgList(obj.getJSONArray("imglist"));
                modelList.add(model);
            }

        }catch (JSONException e){
            Toast.makeText(news.getContext(),"解析数据失败",Toast.LENGTH_SHORT).show();
        }

    }
}
