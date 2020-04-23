package com.yinlei.yinleimusic.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.yinlei.yinleimusic.R;

public class BaseActivity extends Activity {

    private ImageView mIvBack, mIvMe;
    private TextView mTvTile;


    /**
     * findViewById
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T fd(@IdRes int id){
        return findViewById(id);
    }

    /**
     * 初始化通用NavigationBar
     * @param isShowBack
     * @param title
     * @param isShowMe
     */
    protected void initNavBar(boolean isShowBack, String title, boolean isShowMe){
        mIvBack = fd(R.id.iv_back);
        mTvTile = fd(R.id.tv_title);
        mIvMe = fd(R.id.iv_me);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        mIvMe.setVisibility(isShowMe ? View.VISIBLE : View.GONE);
        mTvTile.setText(title);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//后退操作 即： 物理返回键
            }
        });

        mIvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this, MeActivity.class));
            }
        });
    }


}
