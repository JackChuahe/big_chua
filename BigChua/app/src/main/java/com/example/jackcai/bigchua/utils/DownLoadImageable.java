package com.example.jackcai.bigchua.utils;

import android.graphics.Bitmap;

/**
 * Created by JackCai on 2016/4/23.
 * 设置一个接口，用于下载所有的图片 的回调方法
 */
public interface DownLoadImageable {
    /**
     * 当异步图片下载完成后 回调该函数
     * @param position
     * @param bitmap
     * @param srcUrl
     * @param type   代表图片类型，这个由发起请求下载方确定
     */
    public void completedAsynImage(int position , Bitmap bitmap ,String srcUrl,int type);
}
