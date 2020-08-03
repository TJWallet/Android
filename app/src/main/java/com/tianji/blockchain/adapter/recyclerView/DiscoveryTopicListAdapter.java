package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.dappdetail.DappDetailActivity;
import com.tianji.blockchain.activity.dapplist.DappListActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author:jason
 **/
public class DiscoveryTopicListAdapter extends ItemClickableAdapter<DappTopicEntity> {
    public DiscoveryTopicListAdapter(Context context, List<DappTopicEntity> data) {
        this.context = context;
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTopicTitle;
        public Button btnTopicMore;
        public RecyclerView rvTopicItemList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
            btnTopicMore = itemView.findViewById(R.id.btnTopicMore);
            rvTopicItemList = itemView.findViewById(R.id.rvTopicItemList);
        }
    }

    private void viewMore(DappTopicEntity itemData) {
        if (itemData.getItems() != null && itemData.getItems().size() > 0) {
            Intent intent = new Intent(context, DappListActivity.class);
            Bundle bundle = new Bundle();
            if (WalletApplication.lang.equals("")) {
                bundle.putString("title", itemData.getTopicName());
            } else {
                bundle.putString("title", itemData.getTopicNameEN());
            }
            bundle.putString("from", "topic");
            bundle.putSerializable("data", (ArrayList<DappEntity>) itemData.getItems());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DiscoveryTopicListAdapter.ViewHolder itemView = (DiscoveryTopicListAdapter.ViewHolder) holder;
        final DappTopicEntity itemData = this.data.get(position);
        if (WalletApplication.lang.equals("")) {
            itemView.tvTopicTitle.setText(itemData.getTopicName());
        } else {
            itemView.tvTopicTitle.setText(itemData.getTopicNameEN());
        }

        itemView.btnTopicMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMore(itemData);
            }
        });
        itemView.tvTopicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMore(itemData);
            }
        });
        itemView.rvTopicItemList.setLayoutManager(new LinearLayoutManager(context));
        itemView.rvTopicItemList.setNestedScrollingEnabled(false);
        DiscoveryCompleteListAdapter topicItemAdapter = new DiscoveryCompleteListAdapter(context, itemData.getItems(), 5);
        itemView.rvTopicItemList.setAdapter(topicItemAdapter);
        topicItemAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                DappEntity data = (DappEntity) itemData;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                entity.setDescription(data.getName());
                entity.setSummary(data.getSummary());
                entity.setPlatformName(data.getPlatformName());
                entity.setIcon(data.getIcon());
                entity.setType(data.getTypeName());
                entity.setTypeColor(data.getTypeColor());
                entity.setMid(data.getMid());
                entity.setPlatformNameEn(data.getPlatformNameEn());
                entity.setTypeEn(data.getTypeNameEn());
                entity.setDescriptionEn(data.getNameEn());
                entity.setSummaryEn(data.getSummaryEn());
                RecentHistorySharedPreferences.getInstance(context).pushHistory(entity);

                Intent intent = new Intent(context, DappDetailActivity.class);
                intent.putExtra("mid", data.getMid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoveryTopicListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discovery_topic, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
