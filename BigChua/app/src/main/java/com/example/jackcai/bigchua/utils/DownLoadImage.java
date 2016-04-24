package com.example.jackcai.bigchua.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JackCai on 2016/4/23.
 */
public class DownLoadImage extends AsyncTask<String,Void,Bitmap>{
    private Context context;
    private int position;
    private String srcUrl;
    private int type;
    private DownLoadImageable callBack;

    //constructor
    public DownLoadImage(Context context,DownLoadImageable callBack, int position , String srcUrl ,int type){
        this.context = context;
        this.srcUrl = srcUrl;
        this.type = type;
        this.position = position;
        this.callBack = callBack;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String imageUrl = params[0];
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }catch (MalformedURLException e){
            System.err.print(e.getMessage());
        }catch (IOException e){
            System.err.print(e.getMessage());
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap == null)return;
        callBack.completedAsynImage(position,bitmap,srcUrl,type);

    }
}
