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
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.RelevantArticleEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/**
 * author:jason
 **/
public class DappRelevantArticleListAdapter extends ItemClickableAdapter<RelevantArticleEntity> {
    private int displayNumber = 0;

    public DappRelevantArticleListAdapter(Context context, List<RelevantArticleEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DappRelevantArticleListAdapter(Context context, List<RelevantArticleEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivThumbnail;
        public TextView tvTitle;
        public TextView tvCreateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvCreateTime = itemView.findViewById(R.id.tvCreateTime);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappRelevantArticleListAdapter.ViewHolder itemView = (ViewHolder) holder;
        RelevantArticleEntity itemData = this.data.get(position);

//        ImageLoader.getInstance().displayImage(itemData.getThumbnail(), itemView.ivThumbnail);
        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_article_default)).loadImage(context, itemView.ivThumbnail, itemData.getThumbnail());
        itemView.tvTitle.setText(itemData.getTitle());
        itemView.tvCreateTime.setText(itemData.getCreateTime());
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DappRelevantArticleListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dapp_relevant_article, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
