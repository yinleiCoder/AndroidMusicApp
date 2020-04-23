package com.yinlei.yinleimusic.helps;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * MediaPlay帮助类
 * 写为单例模式
 *
 * 音乐播放的3种方式
 * 1. 直接在acitivity中去创建播放音乐,音乐与activity绑定，activity运行时播放音乐，推出时停止播放
 * 2，通过全局单例类与application绑定。application运行时播放音乐，application被杀死时时停止播放
 * 3. 通过service进行音乐播放
 *
 */
public class MediaPlayerHelp  {

    private static MediaPlayerHelp instance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private OnMediaPlayerHelperListener onMediaPlayerHelperListener;
    private String myPath;

    public static MediaPlayerHelp getInstance(Context context){
        if(instance == null){
            synchronized (MediaPlayerHelp.class){
                if (instance == null){
                    instance = new MediaPlayerHelp(context);
                }
            }
        }

        return instance;
    }

    private MediaPlayerHelp(Context context){
        mContext = context;
        mMediaPlayer = new MediaPlayer();

    }

    /**
     * 1.setPath: 设置当前需要播放的音乐
     * 2.start: 播放音乐
     * 3.pause: 暂停播放
     */

    public void setPath(String path){
        /**
         * 1. 音乐正在播放，需要重置音乐播放状态
         * 2. 设置播放音乐路径
         * 3. 准备播放
         */
        /**
         * 当音乐进行切换的时候，入如果音乐处于播放状态，那么久去重置音乐的播放状态。
         * 没有处于播放状态，久不会去重置播放状态(错误逻辑)
         */
        //音乐正在播放或者切换了音乐，就重置音乐播放状态。
        if(mMediaPlayer.isPlaying() || !path.equals(myPath)){
            mMediaPlayer.reset();
        }
        myPath = path;


        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();//异步加载
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(onMediaPlayerHelperListener != null){
                    onMediaPlayerHelperListener.onPrepared(mp);
                }
            }
        });
        //监听音乐播放完成
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(onMediaPlayerHelperListener!= null){
                    onMediaPlayerHelperListener.onCompletion(mp);
                }
            }
        });
    }

    public void setOnMediaPlayerHelperListener(OnMediaPlayerHelperListener onMediaPlayerHelperListener) {
        this.onMediaPlayerHelperListener = onMediaPlayerHelperListener;
    }

    public interface OnMediaPlayerHelperListener{
        void onPrepared(MediaPlayer mp);
        void onCompletion(MediaPlayer mp);
    }

    /**
     * 返回正在播放的音乐路径
     * @return
     */
    public String getPath(){
        return myPath;
    }

    /**
     * 播放音乐
     */

    public void start(){
        if(mMediaPlayer.isPlaying()) return;
        mMediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    public void pause(){
        mMediaPlayer.pause();
    }
}
