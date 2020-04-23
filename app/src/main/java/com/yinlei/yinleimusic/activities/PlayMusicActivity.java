package com.yinlei.yinleimusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.helps.RealmHelp;
import com.yinlei.yinleimusic.models.MusicModel;
import com.yinlei.yinleimusic.views.PlayMusicView;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayMusicActivity extends BaseActivity {

    public static final String MUSIC_ID = "musicId";
    private ImageView mIvBg;
    private PlayMusicView mPlayMusicView;
    private String mMusicId;
    private MusicModel mMusicModel;
    private RealmHelp mRealmHelp;
    private TextView mTvName,mTvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        //隐藏statusbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initData();
        initView();
        //高斯模糊：https://github.com/wasabeef/glide-transformations

    }

    private void initData(){
        mMusicId = getIntent().getStringExtra(MUSIC_ID);
        mRealmHelp = new RealmHelp();
        mMusicModel = mRealmHelp.getMusic(mMusicId);
    }

    private void initView(){
        mIvBg = fd(R.id.iv_bg);
        mTvName = fd(R.id.tv_name);
        mTvAuthor = fd(R.id.tv_author);

        //glide-transformations
        Glide.with(this)
//                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580484204625&di=474eb714956b7b16f85cc44421df9426&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-10-18%2F59e73398a2b12.jpg")
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());

        mPlayMusicView = fd(R.id.play_music_view);
//        mPlayMusicView.setMusicIcon(mMusicModel.getPoster());
//        mPlayMusicView.playMusic("http://47.103.18.122/yinlei/yinlei.mp3");
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
    }

    /**
     * 后退返回按钮点击事件
     * @param view
     */
    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destory();
        mRealmHelp.close();
    }
}
