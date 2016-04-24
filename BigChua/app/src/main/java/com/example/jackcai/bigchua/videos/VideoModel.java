package com.example.jackcai.bigchua.videos;

/**
 * Created by JackCai on 2016/4/23.
 */
public class VideoModel {
    private String videoTitle;
    private String imgUrl;
    private String videoUrl;
    private String videoType;
    private String videoTypeLogo;
    private String playCountDetail;
    private String commentCount;

    public VideoModel(String videoTitle,String imgUrl,String videoUrl,String videoTypeLogo ,String commentCount,String videoType){
        this.videoTitle = videoTitle;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.videoTypeLogo = videoTypeLogo;
        this.commentCount = commentCount;
        this.videoType = videoType;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoType() {
        return videoType;
    }

    public String getVideoTypeLogo() {
        return videoTypeLogo;
    }

    public String getPlayCountDetail() {
        return playCountDetail;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public void setVideoTypeLogo(String videoTypeLogo) {
        this.videoTypeLogo = videoTypeLogo;
    }

    public void setPlayCountDetail(String playCountDetail) {
        this.playCountDetail = playCountDetail;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
