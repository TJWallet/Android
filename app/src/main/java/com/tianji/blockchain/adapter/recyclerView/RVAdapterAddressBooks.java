package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.entity.AddressEntity;

import java.util.List;

public class RVAdapterAddressBooks extends BasicItemClickRecyclerViewAdapter<AddressEntity> {

    public RVAdapterAddressBooks(Context context, List<AddressEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AddressBooksViewHolder addressBooksViewHolder = (AddressBooksViewHolder) holder;
        AddressEntity entity = data.get(position);

        switch (entity.getChainType()) {
            case Constant.ChainType.CHAIN_TYPE_ETH:
                addressBooksViewHolder.img_icon.setImageResource(R.drawable.eth_icon_selected);
                break;
            case Constant.ChainType.CHAIN_TYPE_ACL:
                addressBooksViewHolder.img_icon.setImageResource(R.drawable.acl_icon_select);
                break;
            case Constant.ChainType.CHAIN_TYPE_BTC:
                addressBooksViewHolder.img_icon.setImageResource(R.drawable.btc);
                break;
        }
        addressBooksViewHolder.tv_address.setText(entity.getAddress());
        addressBooksViewHolder.tv_address_name.setText(entity.getAddressName());
        if (position == data.size() - 1) {
            addressBooksViewHolder.view_line.setVisibility(View.GONE);
        } else {
            addressBooksViewHolder.view_line.setVisibility(View.VISIBLE);
        }
        addressBooksViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, data.get(position));
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        AddressBooksViewHolder holder = new AddressBooksViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AddressBooksViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private ImageView img_icon;
        private TextView tv_address_name;
        private TextView tv_address;
        private View view_line;

        public AddressBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_address_name = itemView.findViewById(R.id.tv_address_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
