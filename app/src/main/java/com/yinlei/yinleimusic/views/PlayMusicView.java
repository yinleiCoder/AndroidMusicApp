package com.yinlei.yinleimusic.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.helps.MediaPlayerHelp;
import com.yinlei.yinleimusic.models.MusicModel;
import com.yinlei.yinleimusic.services.MusicService;

public class PlayMusicView extends FrameLayout {

    private Context mContext;
    private View mView;
    private ImageView mIvIcon, mIvNeedle, mIvPlay;
    private Animation mPlayMusicAnim,mPlayNeedleAnim,mStopNeedleAnim;
    private FrameLayout mFlPlayMusic;
    private boolean isPlaying;
    private MediaPlayerHelp mMediaPlayerHelp;
    private String mPath;
    private Intent mServiceIntent;
    private boolean isBindService;
    private MusicService.MusicBind mMusicBind;
    private MusicModel mMusicModel;

    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    private void init(Context context){
        //MediaPlayer
        mContext = context;

        mView = LayoutInflater.from(mContext).inflate(R.layout.play_music, this, false);

        mIvIcon = mView.findViewById(R.id.iv_icon);
        mFlPlayMusic = mView.findViewById(R.id.fl_play_music);
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trugger();
            }
        });
        mIvNeedle = mView.findViewById(R.id.iv_needle);
        mIvPlay = mView.findViewById(R.id.iv_play);

        /**
         * 1. 定义所需要执行的动画：
         *     光盘转动的动画、指针指向光盘的动画、 指针离开光盘的动画
         * 2. startAnimation 让view执行我们定义的动画
         */
        mPlayMusicAnim = AnimationUtils.loadAnimation(mContext,R.anim.play_music_animation);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(mContext,R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(mContext,R.anim.stop_needle_anim);



        addView(mView);

        mMediaPlayerHelp = MediaPlayerHelp.getInstance(mContext);
    }
    /**
     * 切换播放状态
     */
    private void trugger(){
        if(isPlaying){
            stopMusic();
        }else{
            playMusic();
        }
    }


    /**
     * 播放音乐
     */
    public void playMusic(){
        isPlaying = true;
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

//        /**
//         * 1. 当前的音乐是否已经在播放的音乐
//         * 2.当前的音乐是已经在播放的音乐就直接执行start()
//         * 3. 如果当前播放的音乐不是需要播放的音乐，就调用setPath()
//         */
//        if(mMediaPlayerHelp.getPath() != null  && mMediaPlayerHelp.getPath().equals(path)){
//            mMediaPlayerHelp.start();
//        }else{
//            mMediaPlayerHelp.setPath(path);
//            mMediaPlayerHelp.setOnMediaPlayerHelperListener(new MediaPlayerHelp.OnMediaPlayerHelperListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mMediaPlayerHelp.start();
//                }
//            });
//        }
        startMusicService();
    }

    /**
     * 停止播放音乐
     */
    public void stopMusic(){
        isPlaying = false;
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

//        mMediaPlayerHelp.pause();
        if(mMusicBind!=null){
            mMusicBind.stopMusic();
        }
    }

    /**
     * 设置光盘中显示的音乐封面图片
     */
    public void setMusicIcon() {
        Glide.with(mContext)
                .load(mMusicModel.getPoster())
                .into(mIvIcon);
    }

    public void setMusic(MusicModel musicModel){
        mMusicModel = musicModel;
        setMusicIcon();
    }

    /**
     * 启动音乐服务
     */
    private void startMusicService(){
        //启动service
        if(mServiceIntent == null){
            mServiceIntent = new Intent(mContext, MusicService.class);
            mContext.startService(mServiceIntent);
        }else{
            mMusicBind.playMusic();
        }

        //绑定service
        if(!isBindService){
            isBindService = true;
            mContext.bindService(mServiceIntent, conn,Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 解除绑定
     */
    public void destory(){
        //如果已经绑定了服务就解除绑定
        if(isBindService){
            isBindService = false;
            mContext.unbindService(conn);
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
            mMusicBind.setMusic(mMusicModel);
            mMusicBind.playMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
