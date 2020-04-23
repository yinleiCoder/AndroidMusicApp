package com.yinlei.yinleimusic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.activities.WelcomeActivity;
import com.yinlei.yinleimusic.helps.MediaPlayerHelp;
import com.yinlei.yinleimusic.models.MusicModel;

/**
 * 1. 通过service连接playmusicview和mediaplayhelp
 * 2. playmusicview -- 通过service
 *          2.1 播放、暂停音乐
 *          2.2 启动、绑定、解除绑定service
 * 3. mediaplayerhelp -- service
 *             3.1 播放、暂停音乐
 *             3.2 监听音乐播放完成、停止service
 */
public class MusicService extends Service {

    //    不可为 0
    public static final int NOTIFICATION_ID = 1;
    private MediaPlayerHelp mMediaPlayerHelp;
    private MusicModel mMusicModel;

    public MusicService() {
    }

    public class MusicBind extends Binder{
        /**
         * 设置音乐 MusicModel
         */
        public void setMusic(MusicModel musicModel){
            mMusicModel = musicModel;
            startForeground();
        }

        /**
         * 播放音乐
         */
        public void playMusic(){
            /**
             * 1. 当前的音乐是否已经在播放的音乐
             * 2.当前的音乐是已经在播放的音乐就直接执行start()
             * 3. 如果当前播放的音乐不是需要播放的音乐，就调用setPath()
             */
            if(mMediaPlayerHelp.getPath() != null  && mMediaPlayerHelp.getPath().equals(mMusicModel.getPath())){
                mMediaPlayerHelp.start();
            }else{
                mMediaPlayerHelp.setPath(mMusicModel.getPath());
                mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSelf();
                    }
                });
            }
        }


        /**
         * 暂停播放音乐
         */
        public void stopMusic(){
            mMediaPlayerHelp.pause();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayerHelp= MediaPlayerHelp.getInstance(this);
    }

    /**
     * 系统默认不允许不可见的后台服务播放音乐
     * Notification
     */
    /**
     * 设置服务在前台可见
     */
    private void startForeground(){
        /**
         * 创建Notifiaction
         */
        /**
         * 通知栏点击跳转的intent
         */
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, WelcomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);


        /**
         * 创建Notification
         */
        Notification notification = null;
        /**
         * android API 26 以上 NotificationChannel 特性适配
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = createNotificationChannel();
            notification = new Notification.Builder(this, channel.getId())
                    .setContentTitle(mMusicModel.getName())
                    .setContentText(mMusicModel.getAuthor())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        } else {
            notification = new Notification.Builder(this)
                    .setContentTitle(mMusicModel.getName())
                    .setContentText(mMusicModel.getAuthor())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentIntent(pendingIntent)
                    .build();
        }



        /**
         * 设置 notification 在前台展示
         */
        startForeground(NOTIFICATION_ID, notification);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel () {
        String channelId = "yinlei";
        String channelName = "yinleiMusicService";
        String Description = "yinleiMusic";
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(Description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setShowBadge(false);

        return channel;

    }
}
