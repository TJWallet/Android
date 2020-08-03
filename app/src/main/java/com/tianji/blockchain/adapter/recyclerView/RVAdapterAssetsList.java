package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RVAdapterAssetsList extends BasicItemClickRecyclerViewAdapter<AssetsDetailsItemEntity> {
    private String address;
    //    final List<AssetsDetailsItemEntity> recodeList = new ArrayList<>();
    private boolean isShowSwitch;

    private Map<Integer, Boolean> switchMap = new HashMap<>();
    private Chain chain;

    public RVAdapterAssetsList(Context context, List<AssetsDetailsItemEntity> data, String address, Chain chain, boolean isShowSwitch) {
        this.context = context;
        this.data = data;
        this.address = address;
        this.isShowSwitch = isShowSwitch;
        this.chain = chain;
    }

    private void initMap() {
        for (int i = 0; i < data.size(); i++) {
            switchMap.put(i, false);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder assetsListViewHolder, final int position) {
        AssetsListViewHolder holder = (AssetsListViewHolder) assetsListViewHolder;
        holder.tv_chain_name.setText(data.get(position).getAssetsName());
        if (data.get(position).getAssetsName().equals("ETH")) {
            holder.img_icon.setImageResource(R.drawable.eth_icon_selected);
        } else {
            LogUtils.log("搜索之后需要显示ICON == " + data.get(position).getIconUrl());
            ImageLoader.getInstance().displayImage(data.get(position).getIconUrl(), holder.img_icon);
        }
        holder.tv_complete_name.setText("(" + data.get(position).getAssetsCompleteName() + ")");


        if (isShowSwitch) {
            if (data.get(position).isChoosed()) {
                switchMap.put(position, true);
            } else {
                switchMap.put(position, false);
            }

            holder.sw_assets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!data.get(position).getAssetsName().equals("ETH")) {
                        data.get(position).setChoosed(isChecked);
                        switchMap.put(position, isChecked);
                        String key = address + chain;
                        if (isChecked) {
                            AssetsListSharedPreferences.getInstance(context).addAssetes(key, data.get(position));
                        } else {
                            AssetsListSharedPreferences.getInstance(context).removeAssets(key, data.get(position));
                        }
                    } else {
                        holder.sw_assets.setChecked(true);
                    }
                }
            });

            holder.sw_assets.setChecked(switchMap.get(position));
        } else {
            holder.sw_assets.setVisibility(View.GONE);
        }

        if (position == data.size() - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }

        if (listener != null) {
            holder.root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, position, data.get(position));
                }
            });

        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_assets, parent, false);
        AssetsListViewHolder holder = new AssetsListViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AssetsListViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private ImageView img_icon;
        private TextView tv_chain_name;
        private TextView tv_complete_name;
        private Switch sw_assets;
        private View view_line;

        public AssetsListViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_chain_name = itemView.findViewById(R.id.tv_chain_name);
            tv_complete_name = itemView.findViewById(R.id.tv_complete_name);
            sw_assets = itemView.findViewById(R.id.sw_assets);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
