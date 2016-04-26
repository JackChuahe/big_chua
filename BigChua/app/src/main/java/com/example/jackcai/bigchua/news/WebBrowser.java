package com.example.jackcai.bigchua.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.jackcai.bigchua.R;

/**
 * Created by JackCai on 2016/4/25.
 */
public class WebBrowser extends AppCompatActivity{

    private WebView webView;
    private ProgressBar loadPagePb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content_activity);
        webView = (WebView)findViewById(R.id.news_webView);
        loadPagePb = (ProgressBar)findViewById(R.id.news_content_pb);
        loadData();
    }



    public void loadData(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);
        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
        loadPagePb.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 50){
                    loadPagePb.setVisibility(View.GONE);
                }
            }
        });
    }

}
