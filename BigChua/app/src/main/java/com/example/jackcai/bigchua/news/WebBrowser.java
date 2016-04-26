package com.example.jackcai.bigchua.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jackcai.bigchua.R;

/**
 * Created by JackCai on 2016/4/25.
 */
public class WebBrowser extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private ProgressBar loadPagePb;
    private TextView tvTitle;
    private LinearLayout btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_content_activity);
        getWindow().setFlags(Window.FEATURE_NO_TITLE, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = (WebView)findViewById(R.id.news_webView);
        loadPagePb = (ProgressBar)findViewById(R.id.news_content_pb);
        tvTitle = (TextView)findViewById(R.id.item_news_title_bar);
        btnBack = (LinearLayout)findViewById(R.id.view_news_back);
        btnBack.setOnClickListener(this);
        loadData();
    }



    public void loadData(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        tvTitle.setText(title);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_news_back:
                finish();
                break;
        }
    }
}
