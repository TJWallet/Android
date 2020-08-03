package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/**
 * author:jason
 **/
public class DiscoveryRecentCompleteListAdapter extends ItemClickableAdapter<RecentHistoryEntity> {
    private int displayNumber = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutRoot;
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvSubtitle;
        public TextView tvItemType;
        public TextView tvPlatform;
        public View vBorder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layoutRoot = itemView.findViewById(R.id.layoutRoot);
            this.ivIcon = itemView.findViewById(R.id.ivIcon);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            this.tvItemType = itemView.findViewById(R.id.tvItemType);
            this.tvPlatform = itemView.findViewById(R.id.tvPlatform);
            this.vBorder = itemView.findViewById(R.id.vBorder);
        }
    }

    public DiscoveryRecentCompleteListAdapter(Context context, List<RecentHistoryEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoveryRecentCompleteListAdapter(Context context, List<RecentHistoryEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentHistoryEntity itemData = this.data.get(position);
        DiscoveryRecentCompleteListAdapter.ViewHolder itemView = (DiscoveryRecentCompleteListAdapter.ViewHolder) holder;
        boolean isDapp = itemData.getMid() != null;

        if (isDapp) {
//            ImageLoader.getInstance().displayImage(itemData.getIcon(), itemView.ivIcon);
            ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
            if (WalletApplication.lang.equals("")) {
                itemView.tvTitle.setText(itemData.getDescription());
                itemView.tvSubtitle.setText(itemData.getSummary());
                itemView.tvPlatform.setText(itemData.getPlatformName());
                itemView.tvItemType.setText(itemData.getType());
            } else {
                itemView.tvTitle.setText(itemData.getDescriptionEn());
                itemView.tvSubtitle.setText(itemData.getSummaryEn());
                itemView.tvPlatform.setText(itemData.getPlatformNameEn());
                itemView.tvItemType.setText(itemData.getTypeEn());
            }

            if (itemData.getTypeColor() != null) {
                if (itemData.getTypeColor().indexOf("#") == 0) {
                    itemView.tvItemType.setTextColor(Color.parseColor(itemData.getTypeColor()));
                }
            }
        } else {
            if (itemData.getIcon() == null) {
                itemView.ivIcon.setImageResource(R.drawable.img_default);
            } else {
                ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
            }

            if (WalletApplication.lang.equals("")) {
                itemView.tvTitle.setText(itemData.getDescription() == null ? context.getResources().getString(R.string.web_site_link) : itemData.getDescription());
                itemView.tvSubtitle.setText(itemData.getSummary() == null ? itemData.getLink() : itemData.getSummary());
                itemView.tvPlatform.setVisibility(View.GONE);
                itemView.tvItemType.setText(R.string.web_site);
            } else {
                itemView.tvTitle.setText(itemData.getDescriptionEn() == null ? context.getResources().getString(R.string.web_site_link) : itemData.getDescriptionEn());
                itemView.tvSubtitle.setText(itemData.getSummaryEn() == null ? itemData.getLink() : itemData.getSummaryEn());
                itemView.tvPlatform.setVisibility(View.GONE);
                itemView.tvItemType.setText(R.string.web_site);
            }

        }

        if (position == this.data.size() - 1) {
            itemView.vBorder.setVisibility(View.GONE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryRecentCompleteListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_recent_complete, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
