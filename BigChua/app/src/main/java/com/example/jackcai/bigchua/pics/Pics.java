package com.example.jackcai.bigchua.pics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.utils.DownLoadImage;
import com.example.jackcai.bigchua.utils.DownLoadImageable;
import com.example.jackcai.bigchua.videos.VideoModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Pics extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<PicsModel> modelList;
    private MyPicsAdatper myPicsAdatper;
    private ProgressBar pbLoadMore;
    private LinearLayout lyLoadMore;
    private ProgressBar progressBar;
    private static String URL_F = "http://info.3g.qq.com/g/photo/photo3/api/api.jsp?action=index_entry_list%2Cphoto4_channel_list&_t=1450753533729&cl_channel=manual&cl_page=";
    private static String URL_B= "&cl_size=";
    private static String URL_L = "&cl_openAd=0";
    private boolean isLoading = false;
    private int currentPage = 1;
    private int contentSize = 10;
    private boolean isFirstLoad = true;
    private static int MAX_PAGE = 10;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_pics, container, false);
        listView = (ListView)view.findViewById(R.id.pics_list_view);
        progressBar = (ProgressBar)view.findViewById(R.id.pics_list_pb);

        LayoutInflater footInflater = LayoutInflater.from(getContext());
        View footView = footInflater.inflate(R.layout.list_view_more,null);
        pbLoadMore = (ProgressBar) footView.findViewById(R.id.list_item_load_more_progressbar);
        lyLoadMore = (LinearLayout)footView.findViewById(R.id.list_item_load_more_button);
        listView.addFooterView(footView);
        progressBar.setVisibility(View.VISIBLE);
        registerForContextMenu(listView);
        loadData();
        return view;
    }

    //从网络加载数据
    public void loadData(){

        String url = URL_F + currentPage + URL_B + contentSize + URL_L;
        AsynDownLoadPics asynDownLoadPics = new AsynDownLoadPics(this);
        asynDownLoadPics.execute(url);
        ++currentPage;
    }

    public void setModelList(List<PicsModel> list){
        if(this.modelList != null){
            this.modelList.addAll(list);

        }else{
            this.modelList = list;
        }
    }

    public void loadDataFinished(){
        if(isFirstLoad){
            progressBar.setVisibility(View.GONE);
            isFirstLoad = false;
        }else {
            lyLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.setVisibility(View.GONE);
        }

        isLoading = false;//加载结束了
        if(myPicsAdatper != null){
            myPicsAdatper.notifyDataSetChanged();
            return;
        }
        myPicsAdatper = new MyPicsAdatper(modelList,getContext());
        listView.setAdapter(myPicsAdatper);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == modelList.size() && !isLoading){
            if (currentPage >= MAX_PAGE){
                Toast.makeText(getContext(),"已经加载够多了!先认真看看吧！",Toast.LENGTH_SHORT).show();
                return;
            }

            pbLoadMore.setVisibility(View.VISIBLE);
            lyLoadMore.setVisibility(View.GONE);

            lyLoadMore.setVisibility(View.GONE);
            pbLoadMore.setVisibility(View.VISIBLE);

            isLoading = true;
            loadData();
            return;
        }else if(position == modelList.size() && isLoading)return;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("分享&收藏").setHeaderIcon(R.drawable.sns_weixin_timeline_icon);
        menu.add(0,0,0,"分享图片新闻");
    }

    /**
     * 分享视频链接
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();  //获得AdapterContextMenuInfo,以此来获得选择的listview项目
        if (menuInfo.position >= modelList.size())return false;
        PicsModel model = modelList.get(menuInfo.position);



        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, model.getShareUrl());
        startActivity(sendIntent);
        return super.onContextItemSelected(item);
    }
}


class MyPicsAdatper extends BaseAdapter implements DownLoadImageable {
    private List<PicsModel> modelList;
    private Context context;
    private Map<String,Bitmap> bitmapMapImgCache = new HashMap<String,Bitmap>();
    private LayoutInflater inflater ;
    private static  Bitmap defaultImageBmp ;

    public MyPicsAdatper(List<PicsModel> list, Context context ){
        this.modelList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        if(defaultImageBmp == null){
            defaultImageBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.biz_pic_wait_down_img);
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
        PicHolder holder = null;
        if (convertView == null){
            holder = new PicHolder();
            convertView = inflater.inflate(R.layout.pics_list_item,null);
            holder.titleTv =  (TextView)convertView.findViewById(R.id.pic_title);
            holder.headImgIv = (ImageView)convertView.findViewById(R.id.pics_pic_img);
            holder.commetCoutTv = (TextView)convertView.findViewById(R.id.comment_count);
            holder.favorCountTv = (TextView)convertView.findViewById(R.id.favor_count);
            convertView.setTag(holder);

        }else{
            holder = (PicHolder)convertView.getTag();
        }

        PicsModel model = modelList.get(position);
        holder.titleTv.setText(model.getTitle());
        holder.favorCountTv.setText(model.getFavorCount());
        holder.commetCoutTv.setText(model.getCommentCount());
        if(bitmapMapImgCache.containsKey(model.getHeadImgUrl())){
            holder.headImgIv.setImageBitmap(bitmapMapImgCache.get(model.getHeadImgUrl()));
        }else{
            //启动异步下载
            bitmapMapImgCache.put(model.getHeadImgUrl(),defaultImageBmp);
            holder.headImgIv.setImageBitmap(defaultImageBmp);
            DownLoadImage loadImage = new DownLoadImage(context,this,position,model.getHeadImgUrl(),0);
            loadImage.execute(model.getHeadImgUrl());
        }

        return convertView;
    }

    @Override
    public void completedAsynImage(int position, Bitmap bitmap, String srcUrl, int type) {
        bitmapMapImgCache.put(srcUrl,bitmap);
        notifyDataSetChanged();
    }

    class PicHolder{
        TextView titleTv;
        ImageView headImgIv;
        TextView commetCoutTv;
        TextView favorCountTv;
    }
}

//异步下载请求
class AsynDownLoadPics extends AsyncTask<String,Void,String>{
    private List<PicsModel> modelList = new ArrayList<PicsModel>();
    private Pics pics;

    public AsynDownLoadPics(Pics pics){
        this.pics = pics;
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
            pics.setModelList(modelList);
            pics.loadDataFinished();// 加载数据完成后 回调数据
        }else{
            Toast.makeText(pics.getContext(),"加载错误!",Toast.LENGTH_SHORT).show();
        }
    }

    void parseJsonData(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject root = jsonObject.getJSONObject("photo4_channel_list");
            JSONObject dataObj = root.getJSONObject("data");
            JSONArray list = dataObj.getJSONArray("list");
            for(int i = 0; i < list.length() ;i++){
                PicsModel model = new PicsModel();
                JSONObject obj = list.getJSONObject(i);
                if(obj.getBoolean("is_ad") || obj.getBoolean("is_pos"))continue;
                model.setTitle(obj.getString("title"));
                model.setCommentCount(obj.getString("comment"));
                model.setFavorCount(obj.getString("favor"));
                model.setHeadImgUrl(obj.getString("coverimg"));
                model.setShareUrl(obj.getString("shareUrl"));
                model.setImgList(obj.getJSONArray("imglist"));
                modelList.add(model);
            }

        }catch (JSONException e){
            Toast.makeText(pics.getContext(),"解析数据失败",Toast.LENGTH_SHORT).show();
        }

    }
}