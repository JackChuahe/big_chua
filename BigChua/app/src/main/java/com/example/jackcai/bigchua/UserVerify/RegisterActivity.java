package com.example.jackcai.bigchua.UserVerify;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jackcai.bigchua.R;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtUseremail;
    private EditText edtPwd1;
    private EditText edtPwd2;
    private EditText edtUsername;
    private EditText edtVercode;
    private Button btnVercode;
    private Button btnRegi;

    private static Timer mTimer;
    private static TimerTask mTimerTask;
    //private String code = "1";
    private  ProgressDialog progressDialog;
    private static String code = "*/-/**/94.0321.21**/\'s__--__--__--__--__--__--__--__--__----__";


    private final  static int  MAX_TIME = 200;
    private int countTime = MAX_TIME;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    btnVercode.setText(countTime+"s");
                    countTime-- ;
                    break;
                case 1:
                    btnVercode.setText("点击获取验证码");
                    countTime = MAX_TIME;
                    btnVercode.setEnabled(true);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");

        edtUseremail = (EditText)findViewById(R.id.register_edt_useremail);
        edtUsername = (EditText)findViewById(R.id.register_edt_username);
        edtPwd1 = (EditText)findViewById(R.id.register_edt_pwd1);
        edtPwd2 = (EditText)findViewById(R.id.register_edt_pwd2);
        edtVercode = (EditText)findViewById(R.id.register_edt_vercode);
        btnVercode = (Button)findViewById(R.id.register_btn_getVecode);
        btnRegi = (Button)findViewById(R.id.register_btn_register);

        btnRegi.setOnClickListener(this);
        btnVercode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_getVecode:
                getVeryCode();
                break;
            case R.id.register_btn_register:
                register();
                break;
        }
    }

    /**
     * 开始注册
     */
    private void register(){
        String useremail = edtUseremail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String pwd1 = edtPwd1.getText().toString().trim();
        String pwd2 = edtPwd2.getText().toString().trim();
        String vercode = edtVercode.getText().toString().trim();

        if (useremail.indexOf("@") == -1){
            ///邮箱格式不正确
            Toast toast =  Toast.makeText(this,"邮箱格式不正确!请确认!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return ;
        }

        if (!pwd1.equals(pwd2)){
            //密码不匹配
            Toast toast =  Toast.makeText(this,"两次密码不匹配！请检查！",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }

        if (!vercode.equals(code)){
            //验证码不匹配
            Toast toast =  Toast.makeText(this,"验证码不正确！请检查！",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在处理,请稍等...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //开始注册

        AsynRegisterTask asynRegisterTask = new AsynRegisterTask(this);
        asynRegisterTask.execute(useremail,pwd1,username);
    }

    /**
     * 注册回调函数
     * @param content
     */
    public void regCallback(String content){
        progressDialog.dismiss();
        if (content.equals("1")){

            //成功
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("注册成功！");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new  DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RegisterActivity.this.finish();
                }
            });
            builder.show();
        }else if(content.equals("0")){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("注册失败！该用户已经被使用！");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", null);
        }else{
            //网络出错
            Toast toast =  Toast.makeText(this,"网络或服务器出错!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
    public void getVeryCode(){
        String useremail = edtUseremail.getText().toString().trim();
        if (useremail.equals("") || edtUsername.getText().toString().trim().equals("") || edtPwd1.getText().toString().trim().equals("") || edtPwd2.getText().toString().trim().equals("")){
            Toast.makeText(this,"请填写必要信息!",Toast.LENGTH_SHORT).show();
            return;
        }
        btnVercode.setEnabled(false);
        MyAsynGetCode asynGetCode = new MyAsynGetCode(this);
        asynGetCode.execute(useremail);
        init();
    }
    private void init() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if (countTime == 0) {
                    handler.sendEmptyMessage(1);
                    mTimer.cancel();
                }

            }
        };
        //开始一个定时任务
        mTimer.schedule(mTimerTask,0,1000);
    }

    /**
     * 设置验证码
     * @param code
     */
    public void setVerCode(String code){
        this.code = code;
    }

    /**
     * 重置验证码按钮
     */
    public void terminateTimer(){
        mTimer.cancel();
        btnVercode.setText("点击获取验证码");
        btnVercode.setEnabled(true);
    }
}

class  MyAsynGetCode extends AsyncTask<String,Void,String>{
    private RegisterActivity activity;
    private static  final String LOGIN_URL = "/chapter.ashx?useremail=";
    private static  String HOST;

    public MyAsynGetCode(RegisterActivity activity){
        this.activity = activity;
        HOST = activity.getResources().getString(R.string.host);
    }
    @Override
    protected String doInBackground(String... params) {
        String userEmail = params[0];
        String c = "";

        HttpClient client = new HttpClient();
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 8086, "http");

        GetMethod getMethod = new GetMethod(LOGIN_URL+userEmail);

        getMethod.addRequestHeader(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
        getMethod.addRequestHeader("Accept-Language", "zh-cn");
        getMethod.addRequestHeader("Cache-Control", "no-cache");
        getMethod.addRequestHeader("Connection", "Keep-Alive");
        getMethod.addRequestHeader("Host", HOST);


        try {
            client.executeMethod(getMethod);
            c = getMethod.getResponseBodyAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        getMethod.releaseConnection();

        return c;
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject object = new JSONObject(s);
            String code = object.getString("verCode");
            activity.setVerCode(code);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast =  Toast.makeText(activity,"网络或服务出错!请确认!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            activity.terminateTimer();
            return;
        }
        super.onPostExecute(s);
    }
}

class AsynRegisterTask extends AsyncTask<String ,Void,String>{
    private RegisterActivity activity;
    private static    String HOST;
    private static  final String RE_URL = "/signup.ashx";

    public AsynRegisterTask(RegisterActivity activity){
        this.activity = activity;
        HOST = activity.getResources().getString(R.string.host);
    }

    @Override
    protected String doInBackground(String... params) {
        String useremail = params[0];
        String pwd = params[1];
        String username = params[2];



        HttpClient client = new HttpClient();
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 8086, "http");

        PostMethod postMethod = new PostMethod(RE_URL);
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
                new NameValuePair("pwd", pwd),
                new NameValuePair("username",username)
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
        if (s != null){
            activity.regCallback(s);
        }else{
            activity.regCallback("-1");
        }
    }
}
