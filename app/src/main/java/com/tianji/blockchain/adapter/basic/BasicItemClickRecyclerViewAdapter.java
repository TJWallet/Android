package com.tianji.blockchain.adapter.basic;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.utils.LogUtils;

import java.util.List;

public abstract class BasicItemClickRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public OnItemClickListener listener;

    protected List<T> data;
    protected Context context;
    protected int lastDataSize = 0;

    public void updateData(List<T> data) {
        LogUtils.log("BasicItemClickRecyclerViewAdapter updateData");
        lastDataSize = this.data.size();
        this.data = data;
//        if (data != null && data.size() > lastDataSize) {
//            LogUtils.log("增量刷新");
//            notifyItemRangeChanged(lastDataSize, data.size());
//        } else {
        notifyDataSetChanged();
//        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getTypeContentViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindContentViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    protected abstract void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType);
}
