package com.hulk.delivery.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulk.delivery.R;
import com.hulk.delivery.entity.Videos;

import java.util.List;

/**
 * Created by hulk-out on 2018/3/19.
 */

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {
    private List<Videos> videos;

    public ManagementAdapter(List<Videos> videos) {
        this.videos = videos;
    }

    /**
     * 创建ViewHolder的布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ManagementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_management, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 通过ViewHolder将数据绑定到界面上进行显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ManagementAdapter.ViewHolder holder, final int position) {
        holder.videos_title.setText(videos.get(position).getTitle());

        //为btn_share btn_readMore cardView设置点击事件
//            holder.mCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, LevelsFragment.class);
//                    intent.putExtra("Videos", videos.get(position));
//                    context.startActivity(intent);
//                }
//            });

//            holder.videos_share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//                    intent.putExtra(Intent.EXTRA_TEXT, videos.get(position).getDesc());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(Intent.createChooser(intent, videos.get(position).getTitle()));
//                }
//            });
//
//            holder.videos_readMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, LevelsFragment.class);
//                    intent.putExtra("News", videos.get(position));
//                    context.startActivity(intent);
//                }
//            });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView videos_title;

        public ViewHolder(View itemView) {
            super(itemView);
            videos_title = (TextView) itemView.findViewById(R.id.videos_title);
        }
    }
}
