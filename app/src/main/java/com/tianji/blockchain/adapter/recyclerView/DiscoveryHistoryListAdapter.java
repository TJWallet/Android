package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.sharepreferences.SearchHistorySharedPreferences;

import java.util.List;

/** author:jason **/
public class DiscoveryHistoryListAdapter extends ItemClickableAdapter<String> {
    private int displayNumber = 0;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;
        public ImageButton ibDeleteHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.ibDeleteHistory = itemView.findViewById(R.id.ibDeleteHistory);
        }
    }

    public DiscoveryHistoryListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    public DiscoveryHistoryListAdapter(Context context, List<String> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String itemData = this.data.get(position);
        DiscoveryHistoryListAdapter.ViewHolder itemView = (DiscoveryHistoryListAdapter.ViewHolder) holder;

        itemView.tvContent.setText(itemData);
        itemView.ibDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = data.get(position);
                data.remove(item);
                notifyDataSetChanged();

                SearchHistorySharedPreferences.getInstance(context).removeHistory(item);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryHistoryListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_history, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
