package com.example.jackcai.bigchua.guidepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jackcai.bigchua.MainTabBar;
import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.pics.MyPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackCai on 2016/4/27.
 */
public class GuidePage extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private ViewPager vp;
    private MyPageAdapter adapter;
    private  List<Bitmap>  bitmapList  = new ArrayList<Bitmap>();
    private List<View> views = new ArrayList<View>();
    private ImageView btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnStart = (ImageView) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        initViewPager();
    }

    public void initViewPager(){
        Bitmap bitmapA  = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ga);
        Bitmap bitmapB  = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.gb);
        Bitmap bitmapC = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.gc);
        bitmapList.add(bitmapA);
        bitmapList.add(bitmapB);
        bitmapList.add(bitmapC);

        vp = (ViewPager)findViewById(R.id.guide_viewPager);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        for (Bitmap bitmap : bitmapList){
            View view = inflater.inflate(R.layout.guide_page_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.guide_page_item_iv);
            imageView.setImageBitmap(bitmap);
            views.add(view);
        }

        adapter = new MyPageAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == bitmapList.size()-1) {
            btnStart.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1500);
            btnStart.startAnimation(alphaAnimation);
        }else{
            btnStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("first_use",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isEntry",true);
        editor.commit();

        Intent intent = new Intent(this, MainTabBar.class);
        startActivity(intent);
        finish();
    }
}
