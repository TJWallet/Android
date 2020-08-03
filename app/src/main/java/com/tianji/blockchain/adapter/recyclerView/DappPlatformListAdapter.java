package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.DappPlatformEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

public class DappPlatformListAdapter extends ItemClickableAdapter<DappPlatformEntity> {
    public DappPlatformListAdapter(Context context, List<DappPlatformEntity> data) {
        this.context = context;
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layoutIconWrapper;
        public ImageView ivChecked;
        public ImageView ivIcon;
        public TextView tvPlatformName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layoutIconWrapper = itemView.findViewById(R.id.layoutIconWrapper);
            this.ivChecked = itemView.findViewById(R.id.ivChecked);
            this.ivIcon = itemView.findViewById(R.id.ivIcon);
            this.tvPlatformName = itemView.findViewById(R.id.tvPlatformName);
        }
    }
    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappPlatformListAdapter.ViewHolder itemView = (ViewHolder) holder;
        DappPlatformEntity itemData = this.data.get(position);

        itemView.tvPlatformName.setText(itemData.getName());
//        ImageLoader.getInstance().displayImage(itemData.getIcon(), itemView.ivIcon);
        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(context, itemView.ivIcon, itemData.getIcon());
        itemView.layoutIconWrapper.setBackgroundResource(R.drawable.shape_round_background);

        if (itemData.isSelected()) {
            itemView.ivChecked.setVisibility(View.VISIBLE);
        } else {
            itemView.ivChecked.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DappPlatformListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_choose_platform, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
