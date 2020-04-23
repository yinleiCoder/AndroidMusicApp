package com.yinlei.yinleimusic.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * 音乐模型
 */
public class MusicSourceModel  extends RealmObject {

    private RealmList<AlbumModel> album;
    private RealmList<MusicModel> hot;

    public RealmList<AlbumModel> getAlbum() {
        return album;
    }

    public void setAlbum(RealmList<AlbumModel> album) {
        this.album = album;
    }

    public RealmList<MusicModel> getHot() {
        return hot;
    }

    public void setHot(RealmList<MusicModel> hot) {
        this.hot = hot;
    }
}
