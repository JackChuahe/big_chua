package com.example.jackcai.bigchua.common;

/**
 * Created by JackCai on 2016/4/23.
 */
public class TranslateModel {
    private String searchTitle;
    private String searchResult;
    private String time;

    public TranslateModel(String searchTitle ,String searchResult ){
        this.searchResult = searchResult;
        this.searchTitle = searchTitle;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public String getTime() {
        return time;
    }
}
