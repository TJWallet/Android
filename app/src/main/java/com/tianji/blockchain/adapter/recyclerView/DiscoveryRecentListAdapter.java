package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/** author:jason **/
public class DiscoveryRecentListAdapter extends ItemClickableAdapter<RecentHistoryEntity> {
    private int displayNumber = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivIcon = itemView.findViewById(R.id.ivIcon);
            this.tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public DiscoveryRecentListAdapter(Context context, List<RecentHistoryEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoveryRecentListAdapter(Context context, List<RecentHistoryEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentHistoryEntity itemData = this.data.get(position);
        DiscoveryRecentListAdapter.ViewHolder itemView = (DiscoveryRecentListAdapter.ViewHolder)holder;

        if (itemData.getIcon() == null) {
            itemView.ivIcon.setImageResource(R.drawable.img_default);
        } else {
//        ImageLoader.getInstance().displayImage(itemData.getIcon(), itemView.ivIcon);
            ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
        }
        itemView.tvName.setText(itemData.getDescription() == null ? itemData.getLink() : itemData.getDescription());
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryRecentListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_simple, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
