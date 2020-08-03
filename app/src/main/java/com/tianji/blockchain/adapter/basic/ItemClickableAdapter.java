package com.tianji.blockchain.adapter.basic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ItemClickableAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemClickListener {
        void onClickItem(View itemView, Object itemData, int position);
    }

    private OnItemClickListener itemClickListener;
    protected List<T> data;
    protected Context context;
    protected int lastDataSize = 0;

    public void updateData(List<T> data) {
        lastDataSize = this.data.size();
        this.data = data;
        if (data != null && data.size() > lastDataSize) {
            notifyItemRangeChanged(lastDataSize, data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getTypeContentViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (this.itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClickItem(holder.itemView, data.get(position), position);
                }
            });
        }
        onBindContentViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    protected abstract void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType);
}
