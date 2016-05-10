package com.example.jackcai.bigchua.UserVerify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jackcai.bigchua.MainTabBar;
import com.example.jackcai.bigchua.R;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ly;
    public Button btnLogin;
    private EditText edtUE;
    private EditText edtPwd;
    private CheckBox checkBox;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");

        ly = (LinearLayout)findViewById(R.id.linear_layout_btn_register);
        btnLogin = (Button)findViewById(R.id.login_btn_login);

        edtPwd = (EditText)findViewById(R.id.login_edt_pwd);
        edtUE = (EditText)findViewById(R.id.login_edt_username);
        checkBox = (CheckBox)findViewById(R.id.login_check_box_remeber);

        btnLogin.setOnClickListener(this);
        ly.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_layout_btn_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn_login:
                btnLogin.setEnabled(false);
                login();
                break;
        }
    }

    public void login(){
        String useremail = edtUE.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();

        if ( useremail.equals("") ||  pwd.equals("")){
            Toast toast =  Toast.makeText(this,"请填入用户名密码!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            btnLogin.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录,请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        MyAsynTask myAsynTask = new MyAsynTask(this);
        myAsynTask.execute(useremail,pwd);
    }

    /**
     * 准备跳转
     */
    public void perpareActivity(String username){
        SharedPreferences sharedPreferences = getSharedPreferences("remeber_user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("useremail",edtUE.getText().toString().trim());
        editor.putString("pwd",edtPwd.getText().toString().trim());
        boolean isCheck = checkBox.isChecked();
        if (checkBox.isChecked()){
            //记住密码
            editor.putBoolean("isRemeber",true);
        }else {
            editor.putBoolean("isRemeber",false);
        }
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, MainTabBar.class);
        intent.putExtra("useremail",edtUE.getText().toString().trim());
        intent.putExtra("pwd",edtPwd.getText().toString().trim());
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }
}



class MyAsynTask extends AsyncTask<String ,Void,String>{
    private static  final String LOGIN_URL = "/Login.ashx";
    private LoginActivity loginActivity;
    private static String HOST ;
    public MyAsynTask(LoginActivity activity){
        this.loginActivity = activity;
        HOST = loginActivity.getResources().getString(R.string.host);
    }

    @Override
    protected String doInBackground(String... params) {
        String useremail = params[0];
        String pwd = params[1];


        HttpClient client = new HttpClient();
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 8086, "http");

        PostMethod postMethod = new PostMethod(LOGIN_URL);
        String c = null;

        postMethod.addRequestHeader(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
        postMethod.addRequestHeader("Accept-Language", "zh-cn");
        postMethod.addRequestHeader("Cache-Control", "no-cache");
        postMethod.addRequestHeader("Connection", "Keep-Alive");
        postMethod.addRequestHeader("Host", HOST);

        postMethod.setRequestBody(new NameValuePair[] {
                new NameValuePair("useremail", useremail),
                new NameValuePair("pwd", pwd)
        });

        try {
            client.executeMethod(postMethod);
            c = postMethod.getResponseBodyAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        postMethod.releaseConnection();

        return c;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Toast.makeText(loginActivity,s,Toast.LENGTH_SHORT).show();
        try {
            JSONObject object = new JSONObject(s);
            String isOk = object.getString("isOk");
            loginActivity.progressDialog.dismiss();
            loginActivity.btnLogin.setEnabled(true);

            if (isOk.equals("1")){
                String username = object.getString("username");
                loginActivity.perpareActivity(username);
            }else{
                Toast toast =  Toast.makeText(loginActivity,"用户名或密码错误!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                return;
            }
        } catch (JSONException e) {
            Toast toast =  Toast.makeText(loginActivity,"网络或服务器错误!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }
}

