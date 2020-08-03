package com.tianji.blockchain.adapter.basic;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface ITail {

    void onBindTailViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    RecyclerView.ViewHolder getTypeTailViewHolder(@NonNull ViewGroup parent, int viewType);
}
