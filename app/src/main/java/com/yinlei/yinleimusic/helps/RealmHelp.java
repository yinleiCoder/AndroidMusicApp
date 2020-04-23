package com.yinlei.yinleimusic.helps;

import android.content.Context;

import com.yinlei.yinleimusic.migration.Migration;
import com.yinlei.yinleimusic.models.AlbumModel;
import com.yinlei.yinleimusic.models.MusicModel;
import com.yinlei.yinleimusic.models.MusicSourceModel;
import com.yinlei.yinleimusic.models.UserModel;
import com.yinlei.yinleimusic.utils.DataUtils;

import java.io.FileNotFoundException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Realm数据库的帮助类
 */
public class RealmHelp {


    private Realm mRealm;


    public RealmHelp(){
        mRealm = Realm.getDefaultInstance();//引用计数
    }


    /**
     * Realm数据库发生结构性变化(模型或模型中的字段发生了新增或删除修改)的时候，就需要对数据库进行迁移。
     */

    /**
     * 告诉realm数据需要迁移，并为realm设置最新的配置
     */
    public static void migration(){
        RealmConfiguration configuration = getRealmConf();
        Realm.setDefaultConfiguration(configuration);
        try {
            Realm.migrateRealm(configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回RealmConfiguration
     * @return
     */
    private static RealmConfiguration getRealmConf(){
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new Migration())
                .build();
    }

    /**
     * 关闭数据库
     */
    public void close(){
        if(mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }

    /**
     * 保存用户信息
     */
    public void saveUser(UserModel userModel){
        mRealm.beginTransaction();
        mRealm.insert(userModel);
//        mRealm.insertOrUpdate(userModel);
        mRealm.commitTransaction();
    }


    /**
     * 返回所有的用户
     */
    public List<UserModel> getAllUser(){
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        RealmResults<UserModel> results = query.findAll();
        return results;
    }

    /**
     * 验证用户信息
     */
    public boolean validateUser(String phone, String password){
        boolean result = false;
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        query = query.equalTo("phone", phone).equalTo("password",password);
        UserModel userModel = query.findFirst();
        if(userModel!=null){
            result =  true;
        }
        return result;
    }

    /**
     * 获取当前用户
     */
    public UserModel getUser(){
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        UserModel userModel = query.equalTo("phone", UserHelper.getInstance().getPhone()).findFirst();
        return userModel;
    }

    /**
     * 修改密码
     */
    public void changePassword(String password){
        UserModel userModel = getUser();
        mRealm.beginTransaction();
        userModel.setPassword(password);
        mRealm.commitTransaction();
    }

    /**
     * 1. 用户登录，存放数据
     * 2. 退出登录，删除数据
     */
    /**
     * 保存音乐元数据
     */
    public void setMusicSource(Context context){
        // 拿到资源文件中的数据
        String musicSourceJson = DataUtils.getJsonFromAssets(context,"DataSource.json");
        mRealm.beginTransaction();
        mRealm.createObjectFromJson(MusicSourceModel.class,musicSourceJson);
        mRealm.commitTransaction();
    }

    /**
     * 删除音乐元数据
     * 1. 利用RealmResult delete
     * 2.Realm delete删除这个模型下所有的数据
     */
    public void removeMusicSource(){
        mRealm.beginTransaction();
        mRealm.delete(MusicSourceModel.class);
        mRealm.delete(MusicModel.class);
        mRealm.delete(AlbumModel.class);
        mRealm.commitTransaction();
    }


    /**
     * 返回音乐元数据
     */
    public MusicSourceModel getMusicSource(){
        return mRealm.where(MusicSourceModel.class).findFirst();
    }

    /**
     * 返回歌单
     */
    public AlbumModel getAlbum(String albumId){
        return mRealm.where(AlbumModel.class).equalTo("albumId",albumId).findFirst();
    }

    /**
     * 返回音乐列表
     */
    public MusicModel getMusic(String musicId){
        return mRealm.where(MusicModel.class).equalTo("musicId",musicId).findFirst();
    }
}
