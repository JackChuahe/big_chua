package com.example.jackcai.bigchua.guidepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.example.jackcai.bigchua.MainTabBar;
import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.UserVerify.LoginActivity;

public class ActivityEntryPage extends AppCompatActivity {

    private RelativeLayout relativeLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_activity_entry_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        relativeLayout = (RelativeLayout)findViewById(R.id.entry_root) ;
        relativeLayout.setVisibility(View.VISIBLE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1500);
        relativeLayout.startAnimation(alphaAnimation);

        MyAsynWait asynWait = new MyAsynWait(this);
        asynWait.execute("");
    }

    /**
     * 跳转
     */
    public void skip(){
        relativeLayout = (RelativeLayout)findViewById(R.id.entry_root) ;
        relativeLayout.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1500);
        relativeLayout.startAnimation(alphaAnimation);

        SharedPreferences sharedPreferences = getSharedPreferences("first_use",MODE_PRIVATE);
        boolean isEntry = sharedPreferences.getBoolean("isEntry",false);


        if (isEntry){
            SharedPreferences preferences = getSharedPreferences("remeber_user",MODE_PRIVATE);
            boolean isRemeber = preferences.getBoolean("isRemeber",false);
            if (isRemeber){
                String useremail = preferences.getString("useremail","请登录");
                String username = preferences.getString("username","");
                String pwd = preferences.getString("pwd","");
                Intent intent = new Intent(this, MainTabBar.class);
                intent.putExtra("useremail",useremail);
                intent.putExtra("username",username);
                intent.putExtra("pwd",pwd);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(this,GuidePage.class);
            startActivity(intent);
        }
        finish();
    }
}

class MyAsynWait extends AsyncTask<String,Void,String >{

    private ActivityEntryPage activityEntryPage;
    public MyAsynWait(ActivityEntryPage activityEntryPage){
        this.activityEntryPage = activityEntryPage;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        activityEntryPage.skip();
        super.onPostExecute(s);
    }
}