package com.example.jackcai.bigchua.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jackcai.bigchua.common.TranslateModel;
import com.example.jackcai.bigchua.translate.Translate;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by JackCai on 2016/4/23.
 */
public class TranslateDB {
    private SQLiteDatabase db;
    private Context context;
    public TranslateDB(Context context){
        this.context = context;
        initDataBase();
    }

    /*
init dataBase
* */
    void initDataBase(){
        db = context.openOrCreateDatabase("BigChua", Context.MODE_PRIVATE,null);
        db.execSQL("create table if not exists Translate(searchtitle varchar,searchresult varchar,searchtime REAL);");
    }


    //增加一条记录
    public boolean addRecord(String searchTitle,String searchResult){

        db.execSQL("delete from Translate where searchtitle = ?",new Object[]{searchTitle});
        db.execSQL("insert into Translate values(?,?,?)", new Object[]{searchTitle,searchResult,System.currentTimeMillis()});
        return true;
    }

    //显示记录
    public List<TranslateModel> viewRecord(){

        List<TranslateModel> list = new ArrayList<TranslateModel>();
        Cursor cursor = db.rawQuery("select * from Translate order by searchtime desc",null);
        while (cursor.moveToNext()){
            TranslateModel model = new TranslateModel(cursor.getString(0),cursor.getString(1));
            list.add(model);
        }
        return list;
    }
}
