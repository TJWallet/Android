package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;

import java.util.List;

public class RVAdapterBasicSelectDialog extends BasicItemClickRecyclerViewAdapter<String> {
    private String[] data;

    public RVAdapterBasicSelectDialog(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SelectDialogViewHolder selectDialogViewHolder = (SelectDialogViewHolder) holder;
        final String content = data[position];
        selectDialogViewHolder.tv_content.setText(content);

        selectDialogViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(selectDialogViewHolder.root_view, position, content);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basic_select, parent, false);
        SelectDialogViewHolder holder = new SelectDialogViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class SelectDialogViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_content;
        private RelativeLayout root_view;

        public SelectDialogViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            root_view = itemView.findViewById(R.id.root_view);
        }
    }
}
