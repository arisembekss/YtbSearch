package com.dtech.ytbsearch.data;

/**
 * Created by lenovo on 09/08/2017.
 */

public class DataJson {
    private String etag, videoId, channelId, titleVid, urlVid;

    public DataJson(String etag, String videoId, String channelId, String titleVid, String urlVid) {
        this.etag = etag;
        this.videoId = videoId;
        this.channelId = channelId;
        this.titleVid = titleVid;
        this.urlVid = urlVid;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitleVid() {
        return titleVid;
    }

    public void setTitleVid(String titleVid) {
        this.titleVid = titleVid;
    }

    public String getUrlVid() {
        return urlVid;
    }

    public void setUrlVid(String urlVid) {
        this.urlVid = urlVid;
    }
}
