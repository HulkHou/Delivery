package com.hulk.delivery.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 13/8/2017.
 */

public class Videos implements Serializable {
    //视频图片，标题，播放次数，收藏次数
    private String title;
    private int photoId;
    private String playTimes;
    private String collectionTimes;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public Videos(String title, int photoId, String playTimes, String collectionTimes) {
        this.title = title;
        this.photoId = photoId;
        this.playTimes = playTimes;
        this.collectionTimes = collectionTimes;
    }

    public Videos(String title, int photoId) {
        this.title = title;
        this.photoId = photoId;
    }

    public Videos(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;

    }

    public String getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(String playTimes) {
        this.playTimes = playTimes;
    }

    public String getCollectionTimes() {
        return collectionTimes;
    }

    public void setCollectionTimes(String collectionTimes) {
        this.collectionTimes = collectionTimes;
    }
}
