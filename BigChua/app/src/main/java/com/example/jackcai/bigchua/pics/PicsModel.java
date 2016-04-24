package com.example.jackcai.bigchua.pics;

import org.json.JSONArray;

/**
 * Created by JackCai on 2016/4/24.
 */
public class PicsModel {
    private JSONArray imgList;
    private String commentCount ;
    private String title;
    private String favorCount ;
    private String headImgUrl;
    private String shareUrl;



    public JSONArray getImgList() {
        return imgList;
    }

    public void setImgList(JSONArray imgList) {
        this.imgList = imgList;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(String favorCount) {
        this.favorCount = favorCount;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
