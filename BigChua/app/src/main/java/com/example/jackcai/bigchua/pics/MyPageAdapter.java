package com.example.jackcai.bigchua.pics;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by JackCai on 2016/4/27.
 */
public class MyPageAdapter extends PagerAdapter {
    // 需要显示的视图
    private List<View> views ;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }

    public  MyPageAdapter(List<View> views){
        this.views = views;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
