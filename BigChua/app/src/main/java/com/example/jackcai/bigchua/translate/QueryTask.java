package com.example.jackcai.bigchua.translate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    private static final  String GET_PATH = "/translate?keyfrom=deskdict.main&dogVersion=1.0&ue=utf8&i=";
    private static final String PATH_B = "&doctype=json&type=AUTO&xmlVersion=1.6&client=deskdict&id=9a0f8441df8c6c1a1&vendor=unknown&in=YoudaoDict_V6.3.67.7016_setup.1439368979&appVer=6.3.68.1111&appZengqiang=0&abTest=3&smartresult=dict&smartresult=rule";

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
        client.getParams().setHttpElementCharset("utf-8");
        client.getHostConfiguration().setHost(HOST, 80, "http");

        try {
            queryContent =  java.net.URLEncoder.encode(queryContent,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = GET_PATH + queryContent + PATH_B;
        GetMethod get = new GetMethod(path);
        get.addRequestHeader(
                "Accept",
                "*/*");
        get.addRequestHeader("User-Agent", "Youdao Desktop Dict (Windows 6.2.9200)");
        get.addRequestHeader("Host", HOST);
        get.addRequestHeader("Connection",
                "Keep-Alive");

        try {
            client.executeMethod(get);
            queryResult = get.getResponseBodyAsString();

        }catch (Exception e){
           // Toast.makeText(context,"网络请求失败！",Toast.LENGTH_SHORT).show();
            System.err.println(e.getMessage());
            get.releaseConnection();
            return null;
        }
        get.releaseConnection();
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
                    JSONObject smartResult = jsonObject.getJSONObject("smartResult");
                    if(smartResult != null){
                        JSONArray smartArray = smartResult.getJSONArray("entries");
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
