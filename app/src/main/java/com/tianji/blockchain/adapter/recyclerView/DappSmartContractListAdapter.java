package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.entity.SmartContractEntity;

import java.util.List;

/** author:jason **/
public class DappSmartContractListAdapter extends ItemClickableAdapter<SmartContractEntity> {
    private int displayNumber = 0;

    public DappSmartContractListAdapter(Context context, List<SmartContractEntity> data) {
        this.context = context;
        this.data = data;
    }

    public DappSmartContractListAdapter(Context context, List<SmartContractEntity> data, int displayNumber) {
        this.context = context;
        this.data = data;
        this.displayNumber = displayNumber;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSmartContract;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSmartContract = itemView.findViewById(R.id.tvSmartContract);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DappSmartContractListAdapter.ViewHolder itemView = (ViewHolder) holder;
        SmartContractEntity itemData = this.data.get(position);

        String display = itemData.getDisplay() == null ? "" : itemData.getDisplay().replaceAll("\\*", ".");
        itemView.tvSmartContract.setText(display);
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DappSmartContractListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dapp_smart_contract, parent, false));
    }

    @Override
    public int getItemCount() {
        if (this.displayNumber > 0 && this.data.size() > this.displayNumber) {
            return this.displayNumber;
        }
        return this.data.size();
    }
}
