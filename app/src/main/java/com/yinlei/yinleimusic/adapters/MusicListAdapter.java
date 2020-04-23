package com.yinlei.yinleimusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.activities.PlayMusicActivity;
import com.yinlei.yinleimusic.models.MusicModel;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private Context mContext;
    private View mItemview;
    private RecyclerView mRv;
    private boolean isCalcaulationRvHeight;
    private List<MusicModel> mDataSource;

    public MusicListAdapter(Context context, RecyclerView recyclerView,List<MusicModel> mDataSource){
        mContext = context;
        mRv = recyclerView;
        this.mDataSource = mDataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemview = LayoutInflater.from(mContext).inflate(R.layout.item_list_music, parent, false);
        return new ViewHolder(mItemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setRecyclerViewHeight();

        final MusicModel musicModel = mDataSource.get(position);


        Glide.with(mContext)
//                .load("https://cdn.pixabay.com/photo/2016/03/09/09/13/water-1245677_1280.jpg")
                .load(musicModel.getPoster())
                .into(holder.ivIcon);
        holder.mTvName.setText(musicModel.getName());
        holder.mTvAuthor.setText(musicModel.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.MUSIC_ID, musicModel.getMusicId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    /**
     * 设置RV的线性布局定义高度，
     * 因为scrollview嵌套rv肯定会造成运算偏差，导致rv的内容显示不完全。
     * 1. 获取itemview的高度
     * 2. 获取itemview的数量
     * 3. rv的高度= itemviewheight * itemfviewNumber
     */
    private void setRecyclerViewHeight(){

        if(isCalcaulationRvHeight || mRv == null) return;
        isCalcaulationRvHeight = true;
        //获取itemviwe的高度
        RecyclerView.LayoutParams itemViewLp = (RecyclerView.LayoutParams) mItemview.getLayoutParams();
        //获取itemview的数量
        int itemCount = getItemCount();
        //rv的高度= itemviewheight * itemfviewNumber
        int recyclerViewHeight = itemViewLp.height * itemCount;

        //设置rv的高度
        LinearLayout.LayoutParams rvLp = (LinearLayout.LayoutParams) mRv.getLayoutParams();
        rvLp.height = recyclerViewHeight;
        mRv.setLayoutParams(rvLp);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        View itemView;
        TextView mTvName,mTvAuthor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivIcon = itemView.findViewById(R.id.iv_icon);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
