package com.example.jackcai.bigchua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
        fragmentNews = new News();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragmentNews);
        fragmentTransaction.commit();

        newsIv.setSelected(true);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(false);



        //设置当前fragment为第几页
        currentFragment = 0;

    }


    //点击图片新闻的时候
    void clickPicshBtn(){

        fragmentPics = new Pics();

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragmentPics);
        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(true);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(false);


        //设置当前fragment为第几页
        currentFragment = 1;
    }

    //点击视频的时候
    void clickVideosBtn(){

        fragmentVideos = new Videos();

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragmentVideos);
        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(true);
        tranIv.setSelected(false);
        meIv.setSelected(false);


        //设置当前fragment为第几页
        currentFragment = 2;
    }

    //点击翻译的时候
    void clickTranBtn(){


        fragmentTran = new Translate();

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragmentTran);
        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(true);
        meIv.setSelected(false);



        //设置当前fragment为第几页
        currentFragment = 3;
    }

    //点击我的时候
    void clickMeBtn(){
        fragmentMe = new Me();


        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragmentMe);
        fragmentTransaction.commit();

        newsIv.setSelected(false);
        picsIv.setSelected(false);
        videosIv.setSelected(false);
        tranIv.setSelected(false);
        meIv.setSelected(true);



        //设置当前fragment为第几页
        currentFragment = 4;
    }
    //监听物理按键事件

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (currentFragment){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                if(!fragmentTran.keyDown(keyCode,event)){
                    return  false;
                }else{
                    return super.onKeyDown(keyCode,event);
                }
            case 4:
                break;
        }
        return super.onKeyDown(keyCode, event);
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

// 监听软键盘弹起



}
