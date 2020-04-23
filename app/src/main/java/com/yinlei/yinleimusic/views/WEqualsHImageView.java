package com.yinlei.yinleimusic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自定义imageview,使得宽高相等，平分屏幕
 */
public class WEqualsHImageView  extends AppCompatImageView {
    public WEqualsHImageView(Context context) {
        super(context);
    }

    public WEqualsHImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WEqualsHImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        //获取view的宽度
//        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 获取view模式：match_parent wrap_content 具体dp
//        int mode = MeasureSpec.getMode(widthMeasureSpec);


    }
}
