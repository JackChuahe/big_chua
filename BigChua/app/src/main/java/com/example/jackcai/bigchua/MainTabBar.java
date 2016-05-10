package com.example.jackcai.bigchua;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jackcai.bigchua.me.Me;
import com.example.jackcai.bigchua.news.News;
import com.example.jackcai.bigchua.pics.Pics;
import com.example.jackcai.bigchua.translate.Translate;
import com.example.jackcai.bigchua.videos.Videos;

import org.w3c.dom.Text;

import java.lang.reflect.Method;

public class MainTabBar extends FragmentActivity implements View.OnClickListener,View.OnLayoutChangeListener {
    private Me fragmentMe;
    private News fragmentNews;
    private Pics fragmentPics;
    private Videos fragmentVideos;
    private Translate fragmentTran;
    private int currentFragment = 0;

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    //定义控制底部菜单栏变化 的布局
    private FrameLayout newsFl, meFl, picsFl, videosFl,tranFl;

    // 定义图片组件对象
    private ImageView newsIv, meIv, picsIv, videosIv,tranIv;
    private TextView tvNews,tvPics,tvVideo,tvTran,tvMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_bar);

        initView();
        initData();
        clickNewsBtn(); //第一次让第一个新闻界面显示
    }

    //初始化界面
    void initView(){
        newsFl = (FrameLayout)findViewById(R.id.layout_news);
        meFl = (FrameLayout)findViewById(R.id.layout_me);
        picsFl = (FrameLayout) findViewById(R.id.layout_pics);
        videosFl = (FrameLayout)findViewById(R.id.layout_video);
        tranFl = (FrameLayout)findViewById(R.id.layout_translate);

        newsIv = (ImageView)findViewById(R.id.image_news);
        meIv = (ImageView)findViewById(R.id.image_me);
        picsIv = (ImageView)findViewById(R.id.image_pics);
        videosIv = (ImageView)findViewById(R.id.image_video);
        tranIv = (ImageView)findViewById(R.id.image_translate);

        tvMe = (TextView)findViewById(R.id.tv_me);
        tvNews = (TextView)findViewById(R.id.tv_news);
        tvVideo = (TextView)findViewById(R.id.tv_video);
        tvTran = (TextView)findViewById(R.id.tv_translate);
        tvPics = (TextView)findViewById(R.id.tv_pics);


        // 为监听软键盘弹起事件
        activityRootView = findViewById(R.id.main_tab_toor_layout);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;


    }

    //设置点击事件
    void initData(){
        newsFl.setOnClickListener(this);
        meFl.setOnClickListener(this);
        picsFl.setOnClickListener(this);
        videosFl.setOnClickListener(this);
        tranFl.setOnClickListener(this);

        //监听布局大小变化
        activityRootView.addOnLayoutChangeListener(this);
    }

    /*
    *响应点击事件
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击动态按钮
            case R.id.layout_news:
                clickNewsBtn();
                break;
            // 点击与我相关按钮
            case R.id.layout_pics:
                clickPicshBtn();
                break;
            // 点击我的空间按钮
            case R.id.layout_video:
                clickVideosBtn();
                break;
            // 点击更多按钮
            case R.id.layout_translate:
                clickTranBtn();
                break;
            case R.id.layout_me:
                clickMeBtn();
                break;
        }
    }

    //逐个实现点击事件的操作
    //点击新闻的时候
    void clickNewsBtn(){

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        hidenFragment(fragmentTransaction);
        if (fragmentNews == null){
            fragmentNews = new News();
            fragmentTransaction.add(R.id.frame_content,fragmentNews);
        }else{
            fragmentTransaction.show(fragmentNews);
        }
        fragmentTransaction.commit();

        newsIv.setSelected(true);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(false);



        tvNews.setTextColor(getResources().getColor(R.color.bottomtextselected));
        tvPics.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvVideo.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvTran.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvMe.setTextColor(getResources().getColor(R.color.bottomtextcolor));


        //设置当前fragment为第几页
        currentFragment = 0;

    }


    //点击图片新闻的时候
    void clickPicshBtn(){

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        hidenFragment(fragmentTransaction);
        if (fragmentPics == null){
            fragmentPics = new Pics();
            fragmentTransaction.add(R.id.frame_content,fragmentPics);
        }else{
            fragmentTransaction.show(fragmentPics);
        }

        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(true);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(false);

        tvNews.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvPics.setTextColor(getResources().getColor(R.color.bottomtextselected));
        tvVideo.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvTran.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvMe.setTextColor(getResources().getColor(R.color.bottomtextcolor));

        //设置当前fragment为第几页
        currentFragment = 1;
    }

    //点击视频的时候
    void clickVideosBtn(){

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        hidenFragment(fragmentTransaction);
        if (fragmentVideos == null){
            fragmentVideos = new Videos();
            fragmentTransaction.add(R.id.frame_content,fragmentVideos);
        }else{
            fragmentTransaction.show(fragmentVideos);
        }

        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(true);
        tranIv.setSelected(false);
        meIv.setSelected(false);

        tvNews.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvPics.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvVideo.setTextColor(getResources().getColor(R.color.bottomtextselected));
        tvTran.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvMe.setTextColor(getResources().getColor(R.color.bottomtextcolor));

        //设置当前fragment为第几页
        currentFragment = 2;
    }

    //点击翻译的时候
    void clickTranBtn(){

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        hidenFragment(fragmentTransaction);
        if (fragmentTran == null){
            fragmentTran = new Translate();
            fragmentTransaction.add(R.id.frame_content,fragmentTran);
        }else{
            fragmentTransaction.show(fragmentTran);
        }

        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(true);
        meIv.setSelected(false);

        tvNews.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvPics.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvVideo.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvTran.setTextColor(getResources().getColor(R.color.bottomtextselected));
        tvMe.setTextColor(getResources().getColor(R.color.bottomtextcolor));


        //设置当前fragment为第几页
        currentFragment = 3;
    }

    //点击我的时候
    void clickMeBtn(){
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        hidenFragment(fragmentTransaction);
        if (fragmentMe == null){
            fragmentMe = new Me();
            fragmentTransaction.add(R.id.frame_content,fragmentMe);
        }else{
            fragmentTransaction.show(fragmentMe);
        }

        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(true);

        tvNews.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvPics.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvVideo.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvTran.setTextColor(getResources().getColor(R.color.bottomtextcolor));
        tvMe.setTextColor(getResources().getColor(R.color.bottomtextselected));

        //设置当前fragment为第几页
        currentFragment = 4;
    }
    //监听物理按键事件

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (currentFragment){
            case 3:
                if(!fragmentTran.keyDown(keyCode,event)){
                    return  false;
                }else{
                    return super.onKeyDown(keyCode,event);
                }
                default:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("退出?放弃大波美女新闻?!");
                    builder.setCancelable(true);
                    DialogInterface.OnClickListener listener = new  DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case -2:
                                    MainTabBar.this.finish();
                                    break;
                                case -1:
                                    break;
                            }

                        }
                    };

                    builder.setPositiveButton("取消",listener);
                    builder.setNegativeButton("确定",listener);
                    builder.show();
                    break;
        }
        return false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        boolean isSoftKeyBoardPoped = false;
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){ //弹起
            isSoftKeyBoardPoped = true;

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){//关闭
            isSoftKeyBoardPoped = false;
        }


        switch (currentFragment){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                fragmentTran.windowSoftKeyBoardChanged(isSoftKeyBoardPoped);
            case 4:
                break;
        }
    }


    /**
     * 隐藏fragment
     * @param transaction
     */
    private void hidenFragment(FragmentTransaction transaction){
        if (fragmentNews != null){
            transaction.hide(fragmentNews);
        }

        if (fragmentPics != null){
            transaction.hide(fragmentPics);
        }

        if (fragmentVideos != null){
            transaction.hide(fragmentVideos);
        }

        if (fragmentTran != null){
            transaction.hide(fragmentTran);
        }

        if (fragmentMe != null){
            transaction.hide(fragmentMe);
        }
    }

// 监听软键盘弹起

}
