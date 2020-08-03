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
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.component.RoundImageView;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/** author:jason **/
public class DappScreenshotListAdapter extends ItemClickableAdapter<String> {
    public DappScreenshotListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundImageView ivScreenshot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivScreenshot = itemView.findViewById(R.id.ivScreenshot);
        }
    }
    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappScreenshotListAdapter.ViewHolder itemView = (ViewHolder) holder;
        String itemData = this.data.get(position);
//        ImageLoader.getInstance().displayImage(itemData, itemView.ivScreenshot);
        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_screenshot_default)).loadImage(context, itemView.ivScreenshot, itemData);
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DappScreenshotListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dapp_screenshot, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
