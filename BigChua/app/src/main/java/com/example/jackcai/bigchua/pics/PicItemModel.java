package com.example.jackcai.bigchua.pics;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.jackcai.bigchua.utils.DownLoadImage;
import com.example.jackcai.bigchua.utils.DownLoadImageable;

/**
 * Created by JackCai on 2016/4/26.
 */
public class PicItemModel implements DownLoadImageable{
    private String imgUrl;
    private String desc;
    private ImageView imageView;

    public PicItemModel(String url,ImageView imageView,String desc){
        this.imgUrl = url;
        this.desc = desc;
        this.imageView = imageView;
        startDownloadImg();
    }

    private void startDownloadImg(){
        DownLoadImage downLoadImage = new DownLoadImage(null,this,0,imgUrl,0);
        downLoadImage.execute(imgUrl);
    }

    @Override
    public void completedAsynImage(int position, Bitmap bitmap, String srcUrl, int type) {
        imageView.setImageBitmap(bitmap);
    }
}
