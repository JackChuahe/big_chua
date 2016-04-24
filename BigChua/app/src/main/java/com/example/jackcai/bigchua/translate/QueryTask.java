package com.example.jackcai.bigchua.translate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

/**
 * Created by JackCai on 2016/4/23.
 */
public class QueryTask extends AsyncTask<String,Void,String> {

    Context context;
    TextView textView;
    Translate translate;

  //  private static  final  String QUERY_URL = "http://fanyi.youdao.com/translate?smartresult=dict&smartresult=rule&smartresult=ugc&sessionFrom=null";
    private  static final  String HOST = "fanyi.youdao.com";
    private static final  String POS_PATH = "/translate?smartresult=dict&smartresult=rule&smartresult=ugc&sessionFrom=null";

    public QueryTask(Context context,Translate translate, TextView tvResult){
        this.context = context;
        this.textView = tvResult;
        this.translate = translate;
    }

    @Override
    protected String doInBackground(String... params) {
        String queryContent = params[0];
        String queryResult ;

        HttpClient client = new HttpClient();
        String returnStream = null;
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 80, "http");

        PostMethod post = new PostMethod(POS_PATH);
        post.addRequestHeader(
                "Accept",
                "application/json, text/javascript, */*; q=0.01");
        post.addRequestHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-GB;q=0.5,en;q=0.3n");
        post.addRequestHeader("Cache-Control", "no-cache");
        post.addRequestHeader("Connection", "Keep-Alive");
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        post.addRequestHeader("Host", HOST);
        post.addRequestHeader("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.3; WOW64; Trident/7.0; .NET4.0E; .NET4.0C; .NET CLR 3.5.30729; .NET CLR 2.0.50727; .NET CLR 3.0.30729)");

        post.setRequestBody(new NameValuePair[] {
                new NameValuePair("action", "FY_BY_CLICKBUTTON"),
                new NameValuePair("doctype", "json"),
                new NameValuePair("i", queryContent),
                new NameValuePair("keyfrom", "fanyi.web"),
                new NameValuePair("type", "AUTO"),
                new NameValuePair("typoResult", "true"),
                new NameValuePair("ue", "UTF-8"),
                new NameValuePair("xmlVersion", "1.8")
        });

        try {
            client.executeMethod(post);
            queryResult = post.getResponseBodyAsString();

        }catch (Exception e){
           // Toast.makeText(context,"网络请求失败！",Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
            post.releaseConnection();
            return null;
        }
        post.releaseConnection();
        return queryResult;
    }


    @Override
    protected void onPostExecute(String s) {
        if (s != null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("translateResult");
                JSONArray jsonArrayInner = jsonArray.getJSONArray(0);
                JSONObject translateResultJsonObj = jsonArrayInner.getJSONObject(jsonArrayInner.length()-1);
                textView.setText(translateResultJsonObj.getString("tgt"));
                //smart result
                try {
                    JSONObject smartResuklt = jsonObject.getJSONObject("smartResult");
                    if(smartResuklt != null){
                        JSONArray smartArray = smartResuklt.getJSONArray("entries");
                        String smartContent = "";
                        for(int i = 0; i < smartArray.length() ;i++){
                            smartContent += smartArray.getString(i)+"\n";
                        }
                        textView.append(smartContent);
                    }
                }catch (JSONException e){
                    //
                }

            }catch (JSONException e){
                Toast.makeText(context,"解析数据出错!",Toast.LENGTH_SHORT).show();
                textView.setText("");
                return;
            }
        }else{

            textView.setText("");
            return;
        }
        translate.addContentToDB();
        super.onPostExecute(s);
    }
}
