package com.yinlei.yinleimusic.views;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 重写分割线
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridSpaceItemDecoration(int space, RecyclerView parent){
        mSpace = space;
        getRecyclerViewOffsets(parent);//只调用一次
    }

    /**
     * 设置每一个itemview的偏移量
     * 这个方法会每个itemview调用一次带来性能开销，不推荐
     * @param outRect item矩形边界
     * @param view itemview
     * @param parent Recyclerview
     * @param state Recyclerview的状态
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;

        //判断当前的item是不是每行的第一个item
//        if(parent.getChildLayoutPosition(view) % 3 == 0){//这个方法行不通，会导致高度不一致，因为left=0,图片就是原图大小，剩下的是lef=space,高度改变了。
//            outRect.left = 0;
//        }

    }

    private void getRecyclerViewOffsets(RecyclerView parent){
        //view margin:
        //margin 为正：view会距离边界产生一个距离
        //margin 为负：view会超出边界产生一个距离
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.leftMargin = -mSpace;
        parent.setLayoutParams(layoutParams);
    }
}

