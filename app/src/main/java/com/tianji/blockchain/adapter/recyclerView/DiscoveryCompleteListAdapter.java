package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;

import java.util.List;

/**
 * author:jason
 **/
public class DiscoveryCompleteListAdapter extends ItemClickableAdapter<DappEntity> {
    private int displayNumber = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup layoutContainer;
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvSummary;
        public TextView tvType;
        public TextView tvPlatform;
        public View vBorder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layoutContainer = itemView.findViewById(R.id.layoutContainer);
            this.ivIcon = itemView.findViewById(R.id.ivIcon);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvSummary = itemView.findViewById(R.id.tvSummary);
            this.tvType = itemView.findViewById(R.id.tvType);
            this.tvPlatform = itemView.findViewById(R.id.tvPlatform);
            this.vBorder = itemView.findViewById(R.id.vBorder);
        }
    }

    public DiscoveryCompleteListAdapter(Context context, List<DappEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoveryCompleteListAdapter(Context context, List<DappEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappEntity itemData = this.data.get(position);
        DiscoveryCompleteListAdapter.ViewHolder itemView = (DiscoveryCompleteListAdapter.ViewHolder) holder;

//        ImageLoader.getInstance().displayImage(itemData.getIcon(), itemView.ivIcon);
        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
        if (WalletApplication.lang.equals("")) {
            itemView.tvName.setText(itemData.getName());
            itemView.tvSummary.setText(itemData.getSummary());
            itemView.tvType.setText(itemData.getTypeName());
            itemView.tvPlatform.setText(itemData.getPlatformName());
        } else {
            itemView.tvName.setText(itemData.getNameEn());
            itemView.tvSummary.setText(itemData.getSummaryEn());
            itemView.tvType.setText(itemData.getTypeNameEn());
            itemView.tvPlatform.setText(itemData.getPlatformNameEn());
        }

        if (itemData.getTypeColor() != null) {
            itemView.tvType.setTextColor(Color.parseColor(itemData.getTypeColor()));
        }


        if (position == this.data.size() - 1) {
            // 最后一个item去掉底部线条
            itemView.vBorder.setVisibility(View.GONE);
            int paddingTop = itemView.layoutContainer.getPaddingTop();
            itemView.layoutContainer.setPadding(0, paddingTop, 0, 0);
        }
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber && position == displayNumber - 1) {
            // 当限制了显示个数的情况下，显示的最后一个item去掉底部线条
            itemView.vBorder.setVisibility(View.GONE);
            int paddingTop = itemView.layoutContainer.getPaddingTop();
            itemView.layoutContainer.setPadding(0, paddingTop, 0, 0);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryCompleteListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_complete, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
