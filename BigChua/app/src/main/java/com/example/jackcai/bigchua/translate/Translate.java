package com.example.jackcai.bigchua.translate;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.common.TranslateModel;
import com.example.jackcai.bigchua.utils.TranslateDB;

import java.util.ArrayList;
import java.util.List;

public class Translate extends Fragment implements View.OnClickListener ,AdapterView.OnItemClickListener {

    private Button btn_translate;
    private EditText edtInput;
    private TextView tvResult;
    private ListView listView;
    private MyListViewAdapter myListViewAdapter;
    private boolean isListViewShow = true;
    private RelativeLayout searchPanel ;
    private View view;
    private QueryTask queryTask;
    private TranslateDB trDB ; // 新建一个db类
    private List<TranslateModel> list;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_translate,container,false);


        //绑定内容
        btn_translate = (Button)view.findViewById(R.id.dictionart_btn_translate);
        edtInput = (EditText)view.findViewById(R.id.dictionart_edt_input);
        tvResult = (TextView)view.findViewById(R.id.dictionart_tv_result);
        progressBar = (ProgressBar)view.findViewById(R.id.translate_progressbar);

        searchPanel = (RelativeLayout)view. findViewById(R.id.dictionary_result_panel);

        listView = (ListView)view.findViewById(R.id.dictionart_list_view);
        Context context = getContext();
        trDB = new TranslateDB(getContext());
        loadSearchRecord(); //加载记录
        btn_translate.setOnClickListener(this);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*
    暂时从写死的方式加载数据  后期从数据库中加载
    * */
    void loadSearchRecord(){
        list = trDB.viewRecord();
        myListViewAdapter = new MyListViewAdapter(list,getContext());
        listView.setAdapter(myListViewAdapter);
        listView.setOnItemClickListener(this);

    }


    public  boolean keyDown(int keyCode , KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !isListViewShow){
            showList();
            return false;
        }
        return true;
    }
        /*
    * 设置搜索的内容
    * */

    void setInputAndResult(String input,String result){
        edtInput.setText(input);
        tvResult.setText(result);
    }

    //显示列表记录
    void showList(){
        listView.setVisibility(View.VISIBLE);
        searchPanel.setVisibility(View.GONE);
        isListViewShow = true;
    }

    //显示搜索结果面板
    void showSearchPanel(){
        listView.setVisibility(View.GONE);
        searchPanel.setVisibility(View.VISIBLE);
        isListViewShow = false;
    }
    /*
        button 点击事件 进行翻译工作
        * */
    @Override
    public void onClick(View v) {
        String searhContent = edtInput.getText().toString().trim();
        if (searhContent.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);
            // 隐藏列表视图
            if (isListViewShow){
                showSearchPanel();
            }
            //翻译
            startTranslate(searhContent);

        }else{
            Toast.makeText(getContext(),"请输入要翻译的英文内容",Toast.LENGTH_SHORT).show();
        }

    }

    //开始翻译
    private void startTranslate(String searchContent){
        //TODO
        queryTask = new QueryTask(getContext(),this,tvResult);
        queryTask.execute(searchContent);
    }

    /*
    加入数据到数据库
    * */
    public void addContentToDB(){
        trDB.addRecord(edtInput.getText().toString().trim(),tvResult.getText().toString());
        list = trDB.viewRecord();
        myListViewAdapter.setList(list);
        myListViewAdapter.notifyDataSetChanged();
        System.gc();
        progressBar.setVisibility(View.GONE);
    }


    /*
    列表项被选中
    * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TranslateModel model = list.get(position);
        setInputAndResult(model.getSearchTitle(),model.getSearchResult());
        showSearchPanel();
    }

    /*
    响应是否弹起了软键盘
    * */
    public void windowSoftKeyBoardChanged(boolean isPoped){
        if (isPoped && !isListViewShow){
            searchPanel.setVisibility(View.GONE);
        }
        if(!isPoped && !isListViewShow){
            searchPanel.setVisibility(View.VISIBLE);
        }
    }
}





/**
 *
 * list view 的适配器
 */

class MyListViewAdapter extends BaseAdapter {
    private List<TranslateModel> list;
    private LayoutInflater inflater;
    public  MyListViewAdapter(List<TranslateModel> list,Context context){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<TranslateModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.translate_record_list_item,null);
            holder.tvTitle = (TextView)convertView.findViewById(R.id.dictionary_listitem_title);
            holder.detail = (TextView)convertView.findViewById(R.id.dictionary_listitem_content);
            convertView.setTag(holder);
        }else
        {
            holder = (Holder)convertView.getTag();
        }

        holder.tvTitle.setText(list.get(position).getSearchTitle());
        holder.detail.setText(list.get(position).getSearchResult());
        return convertView;
    }

    class Holder{
        TextView tvTitle;
        TextView detail;
    }
}
