package com.tianji.blockchain.component.pinnedheader;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public abstract class PinnedHeaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public abstract boolean isPinnedPosition(int position);

    public RecyclerView.ViewHolder onCreatePinnedViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType);
    }

    public void onBindPinnedViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position);
    }
}
