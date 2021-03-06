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
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.util.List;

/** author:jason **/
public class DiscoverySimpleListSkeletonAdapter extends ItemClickableAdapter<String> {
    private int displayNumber = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public DiscoverySimpleListSkeletonAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoverySimpleListSkeletonAdapter(Context context, List<String> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DiscoverySimpleListSkeletonAdapter.ViewHolder itemView = (DiscoverySimpleListSkeletonAdapter.ViewHolder)holder;
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoverySimpleListSkeletonAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_simple_skeleton, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
