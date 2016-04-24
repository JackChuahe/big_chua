package com.example.jackcai.bigchua.videos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.example.jackcai.bigchua.common.LoadMoreHolder;
import com.example.jackcai.bigchua.utils.DownLoadImage;
import com.example.jackcai.bigchua.utils.DownLoadImageable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Videos extends Fragment implements AdapterView.OnItemClickListener {
    private  ListView listView;
    private  MyVideosAdapter myVideosAdapter;
    private  List<VideoModel> modelList;
    private ProgressBar progressBar;
    private static final String URL = "http://c.m.163.com/nc/video/home/" ;//10-20.html";
    private int currentPage = 0;
    private ProgressBar pbLoadMore;
    private LinearLayout lyLoadMore;
    private boolean isLoading = false;
    private boolean isFirstLoading = true;
    private static  final int  MAX_NUM = 40;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_videos, container, false);

        listView = (ListView)view.findViewById(R.id.videos_list_view);
        progressBar = (ProgressBar)view.findViewById(R.id.video_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        LayoutInflater footInflater = LayoutInflater.from(getContext());
        View footView = footInflater.inflate(R.layout.list_view_more,null);
        pbLoadMore = (ProgressBar) footView.findViewById(R.id.list_item_load_more_progressbar);
        lyLoadMore = (LinearLayout)footView.findViewById(R.id.list_item_load_more_button);

        registerForContextMenu(listView);
        listView.addFooterView(footView);
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }


    //获取数据
    public void loadData(){
        if (currentPage >= MAX_NUM){
            Toast.makeText(getContext(),"没有更多了!",Toast.LENGTH_SHORT).show();
            pbLoadMore.setVisibility(View.GONE);
            lyLoadMore.setVisibility(View.VISIBLE);
            return;
        }

        String url = URL + currentPage + "-"+ (currentPage + 10)+".html";
        AsynGetData asynGetData = new AsynGetData(this);
        asynGetData.execute(url);
    }

    public void setModelList(List<VideoModel> modelList) {
        if(this.modelList != null){
            this.modelList.addAll(modelList);
        }else {
            this.modelList = modelList;
        }
        currentPage += this.modelList.size();
    }


    /**
     * 加载数据完成
     */
    public void loadDataFinished(){
        if (isFirstLoading){
            progressBar.setVisibility(View.GONE);
            isFirstLoading = false;
        }else{

            pbLoadMore.setVisibility(View.GONE);
            lyLoadMore.setVisibility(View.VISIBLE);

        }
        //已经有适配器了
        if(myVideosAdapter != null){
            myVideosAdapter.notifyDataSetChanged();
            return;
        }
        myVideosAdapter = new MyVideosAdapter(modelList,getContext());
        listView.setAdapter(myVideosAdapter);
        listView.setOnItemClickListener(this);

        isLoading = false;     //加载结束
    }

    /**
     * 视频项被点击后
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == modelList.size() && !isLoading){

            pbLoadMore.setVisibility(View.VISIBLE);
            lyLoadMore.setVisibility(View.GONE);

            loadData();
            return;
        }else if(position == modelList.size() && isLoading)return;

        VideoModel model = modelList.get(position);
        if (model.getVideoUrl() != null){
            Uri uri = Uri.parse(model.getVideoUrl());
            //调用系统自带的播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,"video/mp4");
            startActivity(intent);
        }
    }

    /**
     * 监听长按事件
     */
//    @Override
//    public boolean onLongClick(View v) {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setType("text/*");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, contentEditText.getText().toString());
//        startActivity(sendIntent);
//        return false;
//    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("分享&收藏").setHeaderIcon(R.drawable.sns_weixin_timeline_icon);
        menu.add(0,0,0,"分享视频");
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
        VideoModel model = modelList.get(menuInfo.position);



        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra("分享自BigChua客户端\n"+Intent.EXTRA_TEXT, model.getVideoUrl() + "\n"+model.getVideoTitle());
        startActivity(sendIntent);
        return super.onContextItemSelected(item);
    }

}


class MyVideosAdapter extends BaseAdapter implements DownLoadImageable{
    private List<VideoModel> modelList = new ArrayList<VideoModel>();

    private Map<String,Bitmap>bitmapSmallLogoCache = new HashMap<String ,Bitmap>();
    private Map<String,Bitmap> bitmapMapImgCache = new HashMap<String,Bitmap>();


    private LayoutInflater inflater ;
    private static final  int LOGO_TYPE = 0;
    private  static final int BIG_IMG_TYPE = 1;
    private Context context;
    private static  Bitmap videoImageBmp ;
    private static  Bitmap smallLogoImageBmp ;

    public MyVideosAdapter(List<VideoModel> list, Context context ){
        this.modelList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        if(videoImageBmp == null){
            videoImageBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.biz_pic_wait_down_img);
        }
        if (smallLogoImageBmp == null){
            smallLogoImageBmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.biz_input_pic_camera_icon);
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

         Holder holder = null;
        if (convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.videos_list_view_item,null);
            holder.title = (TextView)convertView.findViewById(R.id.video_title);
            holder.videoImg = (ImageView)convertView.findViewById(R.id.video_video_img);
            holder.playCoutDetail = (TextView)convertView.findViewById(R.id.play_count_detail);
            holder.smallLogo = (ImageView)convertView.findViewById(R.id.video_type_logo);
            holder.commentCount = (TextView)convertView.findViewById(R.id.comment_count);
            holder.videoTYpe = (TextView)convertView.findViewById(R.id.video_type);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        VideoModel model = modelList.get(position);
        holder.title.setText(model.getVideoTitle());
        holder.videoTYpe.setText(model.getVideoType());
        holder.commentCount.setText(model.getCommentCount());
        holder.playCoutDetail.setText(model.getPlayCountDetail());
        holder.videoImg.setImageResource(R.drawable.biz_pic_wait_down_img);
        holder.smallLogo.setImageResource(R.drawable.biz_input_pic_camera_icon);
        //大图片已经有缓存

        if(bitmapMapImgCache.containsKey(model.getImgUrl())) {
            Bitmap bitmap = bitmapMapImgCache.get(model.getImgUrl());
            holder.videoImg.setImageBitmap(bitmap);
        }else{
            //进行异步网络加载
            bitmapMapImgCache.put(model.getImgUrl(),videoImageBmp);
            DownLoadImage loadImage = new DownLoadImage(context,this,position,model.getImgUrl(),BIG_IMG_TYPE);
            loadImage.execute(model.getImgUrl());
        }
        //小图片是否已经有缓存了
        if (bitmapSmallLogoCache.containsKey(model.getVideoTypeLogo())){
            Bitmap logoBitmap = bitmapSmallLogoCache.get(model.getVideoTypeLogo());
            holder.smallLogo.setImageBitmap(logoBitmap);
        }else{
            //进行异步加载
            bitmapSmallLogoCache.put(model.getVideoTypeLogo(),smallLogoImageBmp);
            DownLoadImage loadImage = new DownLoadImage(context,this,position,model.getVideoTypeLogo(),LOGO_TYPE);
            loadImage.execute(model.getVideoTypeLogo());
        }
        return convertView;
    }

    @Override
    public void completedAsynImage(int position, Bitmap bitmap, String srcUrl, int type) {
        switch (type){
            case BIG_IMG_TYPE:
                bitmapMapImgCache.put(srcUrl,bitmap);
               // imageViewList.get(position).setImageBitmap(bitmap);
                break;
            case LOGO_TYPE:
                bitmapSmallLogoCache.put(srcUrl,bitmap);
                //smallLogoList.get(position).setImageBitmap(bitmap);
                break;
        }
        notifyDataSetChanged();

    }

    /**
     * 内部类
     */
    class Holder{
        TextView title;
        ImageView videoImg;
        ImageView smallLogo;
        TextView playCoutDetail;
        TextView videoTYpe;
        TextView commentCount;
    }



}


class AsynGetData extends AsyncTask<String , Void,String>{
    private List<VideoModel> modelList = new ArrayList<VideoModel>();
    private Videos videos;

    // constructor
    public AsynGetData(Videos videos){
        this.videos = videos;
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
        if (s != null){
            parseJsonData(s);
            videos.setModelList(modelList);
            videos.loadDataFinished();// 加载数据完成后 回调数据
        }else{
            Toast.makeText(videos.getContext(),"加载错误!",Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(s);
    }

    /**
     * 解析json数据成一个一个的模型
     * @param data
     */
    private void parseJsonData(String data){

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray videoList = jsonObject.getJSONArray("videoList");
            for(int i = 0; i < videoList.length();i++){
                JSONObject item = videoList.getJSONObject(i);
                VideoModel model = new VideoModel(item.getString("title"),item.getString("cover"),item.getString("mp4_url"),item.getString("topicImg"),item.getString("replyCount"),item.getString("topicName"));
                int length = item.getInt("length");
                int playCount = item.getInt("playCount");

                int minutes = length / 60;
                int seconds = length%60;
                String detail = "";
                if((""+minutes).length() < 2){
                    detail += "0";
                }
                 detail =  minutes + ":" ;
                if ((seconds+"").length() < 2){
                    detail += "0";
                }
                detail += seconds +" / ";

                detail += String.format("%.1f",(float)playCount/10000);
                detail += "万播放";

                model.setPlayCountDetail(detail);

                modelList.add(model);
            }

        }catch (JSONException e){
            //
            System.err.println(e.getMessage());
        }
    }
}