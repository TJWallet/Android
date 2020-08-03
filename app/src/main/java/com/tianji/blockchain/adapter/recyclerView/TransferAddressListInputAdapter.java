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
import com.tianji.blockchain.btcApi.Transaction;
import com.tianji.blockchain.utils.MathUtils;


import java.util.List;

public class TransferAddressListInputAdapter extends BasicItemClickRecyclerViewAdapter<Transaction.Input> {

    public TransferAddressListInputAdapter(Context context, List<Transaction.Input> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TransferAddressListInputViewHolder holder = (TransferAddressListInputViewHolder) viewHolder;
        holder.tv_address.setText(data.get(position).getPrevAdresses());
        holder.tv_value.setText(MathUtils.doubleKeep8(data.get(position).getPrevValue()) + " BTC");
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_info_address_list, parent, false);
        TransferAddressListInputViewHolder holder = new TransferAddressListInputViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransferAddressListInputViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_address;
        private TextView tv_value;

        public TransferAddressListInputViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_value = itemView.findViewById(R.id.tv_value);
        }
    }
}
