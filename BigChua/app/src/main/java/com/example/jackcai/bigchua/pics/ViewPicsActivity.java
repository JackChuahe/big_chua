package com.example.jackcai.bigchua.pics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.jackcai.bigchua.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPicsActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager vp;
    private List<View> views;
    private MyPageAdapter adapter;
    private List<PicItemModel> picItemModelList = new ArrayList<PicItemModel>();
    private static Bitmap defaultImageBmp ;
    private TextView tvTitle;
    private LinearLayout btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.multi_pics_view_activity);
        getWindow().setFlags(Window.FEATURE_NO_TITLE, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //为得到的窗体设置状态标识
        tvTitle = (TextView)findViewById(R.id.item_pic_title_bar);
        btnBack = (LinearLayout)findViewById(R.id.view_pics_back);
        btnBack.setOnClickListener(this);
        //initData();
        initViewPager();
    }

    private void initViewPager(){

        if (defaultImageBmp == null){
            defaultImageBmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.my_ads);
        }

        vp = (ViewPager)findViewById(R.id.pics_viewPager);
        views = new ArrayList<View>();
       // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        String title = intent.getStringExtra("title");
        tvTitle.setText(title);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        try
        {
            JSONArray imgs = new JSONArray(json);
            for (int i = 0; i < imgs.length();i++){
                JSONObject obj = imgs.getJSONObject(i);
                View view = inflater.inflate(R.layout.activity_view_pics_page_item,null);
                TextView tv_detail = (TextView)view.findViewById(R.id.pics_detail_tv);
                String desc = obj.getString("desc");
                tv_detail.setText(desc);
                PicItemModel model = new PicItemModel(obj.getString("img"),(ImageView)view.findViewById(R.id.pics_image_iv),desc);
                picItemModelList.add(model);
                views.add(view);
            }
        }catch (JSONException e){
        }
        View view = inflater.inflate(R.layout.activity_view_pics_page_item,null);
        TextView tv_detail = (TextView)view.findViewById(R.id.pics_detail_tv);
        ImageView imageView = (ImageView)view.findViewById(R.id.pics_image_iv);
        tv_detail.setText("广告招商中...详情请咨询QQ601825672,或前往川师计科学院面谈");
        imageView.setImageBitmap(defaultImageBmp);
        views.add(view);

        adapter = new MyPageAdapter(views);
        vp.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_pics_back:
                finish();
                break;
        }
    }


//    private void initData(){
//        sliderLayout = (SliderLayout)findViewById(R.id.pics_imgs_slider);
//        textView= (TextView)findViewById(R.id.pic_title);
//
//        Intent intent = getIntent();
//        String json = intent.getStringExtra("json");
//        title = intent.getStringExtra("title");
//        sliderLayout.setCustomAnimation(new DescriptionAnimation());
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//        sliderLayout.stopAutoCycle();
//        try
//        {
//            JSONArray imgs = new JSONArray(json);
//            for (int i = 0; i < imgs.length();i++){
//                JSONObject obj = imgs.getJSONObject(i);
//                TextSliderView textSliderView = new TextSliderView(this);
//                textSliderView.description(title);
//                textSliderView.image(obj.getString("img"));
//                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
//                detail.add(obj.getString("desc"));
//                sliderLayout.addSlider(textSliderView);
//            }
//        }catch (JSONException e){
//        }
//        sliderLayout.addOnPageChangeListener(this);
//
//    }


}




class MyPageAdapter extends PagerAdapter{
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