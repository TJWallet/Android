package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.RecommendEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/**
 * author:jason
 **/
public class DiscoveryRecommendSimpleListAdapter extends ItemClickableAdapter<RecommendEntity> {
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

    public DiscoveryRecommendSimpleListAdapter(Context context, List<RecommendEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoveryRecommendSimpleListAdapter(Context context, List<RecommendEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecommendEntity itemData = this.data.get(position);
        DiscoveryRecommendSimpleListAdapter.ViewHolder itemView = (DiscoveryRecommendSimpleListAdapter.ViewHolder) holder;

        if (itemData.getIcon() == null) {
            itemView.ivIcon.setImageResource(R.drawable.img_default);
        } else {
//        ImageLoader.getInstance().displayImage(itemData.getIcon(), itemView.ivIcon);
            ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
        }
        if (WalletApplication.lang.equals("")) {
            itemView.tvName.setText(itemData.getTitle());
        } else {
            itemView.tvName.setText(itemData.getTitleEn());
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryRecommendSimpleListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_simple, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
