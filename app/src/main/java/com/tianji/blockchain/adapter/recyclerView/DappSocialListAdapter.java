package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.SocialMediaEntity;

import java.util.List;

/** author:jason **/
public class DappSocialListAdapter extends ItemClickableAdapter<SocialMediaEntity> {
    public DappSocialListAdapter(Context context, List<SocialMediaEntity> data) {
        this.context = context;
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSocialIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivSocialIcon = itemView.findViewById(R.id.ivSocialIcon);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappSocialListAdapter.ViewHolder itemView = (ViewHolder) holder;
        SocialMediaEntity itemData = this.data.get(position);

        itemView.ivSocialIcon.setImageResource(itemData.getIconResourceID());
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DappSocialListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dapp_social, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
