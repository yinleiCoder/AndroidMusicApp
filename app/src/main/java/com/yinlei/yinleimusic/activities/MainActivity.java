package com.yinlei.yinleimusic.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.adapters.MusicGridAdapter;
import com.yinlei.yinleimusic.adapters.MusicListAdapter;
import com.yinlei.yinleimusic.helps.RealmHelp;
import com.yinlei.yinleimusic.models.MusicSourceModel;
import com.yinlei.yinleimusic.views.GridSpaceItemDecoration;

public class MainActivity extends BaseActivity {

    private RecyclerView mRvGrid, mRvList;
    private MusicGridAdapter mGridAdapter;
    private MusicListAdapter mListAdapter;
    private RealmHelp mRealmHelp;
    private MusicSourceModel mMusicSourceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData(){
        mRealmHelp = new RealmHelp();
        mMusicSourceModel = mRealmHelp.getMusicSource();
    }


    private void initView() {
        initNavBar(false, "音阙诗听", true);
        mRvGrid = fd(R.id.rv_grid);
        mRvGrid.setLayoutManager(new GridLayoutManager(this, 3));
//        mRvGrid.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));//分割线默认只有1dp
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize),mRvGrid));//分割线默认只有1dp
        mRvGrid.setNestedScrollingEnabled(false);//设置不能滚动,由最外层的scrollview提供统一的滑动
        mGridAdapter = new MusicGridAdapter(this,mMusicSourceModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);

        /**
         * 1. 假如已知列表高度的情况下，可以在布局中把rv的高度直接定义上。
         * 2. 不知道列表高度的情况下，需要手动计算rv的高度
         */
        mRvList = fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvList.setNestedScrollingEnabled(false);//设置不能滚动
        mListAdapter = new MusicListAdapter(this,mRvList, mMusicSourceModel.getHot());
        mRvList.setAdapter(mListAdapter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelp.close();
    }
}
