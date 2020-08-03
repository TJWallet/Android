package com.tianji.blockchain.adapter.basic;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CustomRecyclerViewAdapter<T> extends BasicItemClickRecyclerViewAdapter<T> {

    protected boolean isHasTail = false;

    protected ITail tail;

    /*** 正常内容 ***/
    protected final static int TYPE_CONTENT = 0;
    /*** 页尾 ***/
    protected final static int TYPE_TAIL = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TAIL && isHasTail) {
            if (tail != null) {
                return tail.getTypeTailViewHolder(parent, viewType);
            } else {
                return getTypeContentViewHolder(parent, viewType);
            }
        } else {
            return getTypeContentViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHasTail && getItemViewType(position) == TYPE_TAIL) {
            if (tail != null) {
                tail.onBindTailViewHolder(holder, position);
            }
        } else {
            onBindContentViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHasTail && position == data.size()) {
            return TYPE_TAIL;
        }
        return TYPE_CONTENT;
    }

    public void settail(ITail tail) {
        if (tail != null) {
            this.tail = tail;
            isHasTail = true;
        }
    }

    protected abstract void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType);
}
