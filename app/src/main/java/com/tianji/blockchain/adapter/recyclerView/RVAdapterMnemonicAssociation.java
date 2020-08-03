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

import java.util.List;

public class RVAdapterMnemonicAssociation extends BasicItemClickRecyclerViewAdapter<String> {

    public RVAdapterMnemonicAssociation(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MnemonicAssViewHolder mnemonicAssViewHolder = (MnemonicAssViewHolder) holder;
        mnemonicAssViewHolder.tv_mnemonic_ass.setText(data.get(position));
        mnemonicAssViewHolder.tv_mnemonic_ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position, data.get(position));
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mnemonic_association, parent, false);
        MnemonicAssViewHolder holder = new MnemonicAssViewHolder(view);
        return holder;
    }

    public class MnemonicAssViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_mnemonic_ass;

        public MnemonicAssViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mnemonic_ass = itemView.findViewById(R.id.tv_mnemonic_ass);
        }
    }
}
