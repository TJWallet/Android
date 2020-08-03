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
import com.tianji.blockchain.entity.InformationEntity;
import com.tianji.blockchain.utils.TimeUtils;

import java.util.List;

public class RVAdapterInformationList extends BasicItemClickRecyclerViewAdapter<InformationEntity> {

    public RVAdapterInformationList(Context context, List<InformationEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        InformationViewHolder informationViewHolder = (InformationViewHolder) holder;
        final InformationEntity entity = data.get(position);

        informationViewHolder.tv_title.setText(entity.getTitle());
        informationViewHolder.tv_content.setText(entity.getContent());
        informationViewHolder.tv_time.setText(TimeUtils.timeStamp2Date(entity.getCreatedAt(), "MM-dd HH:mm"));

        if (position == data.size() - 1) {
            informationViewHolder.view_line.setVisibility(View.GONE);
        } else {
            informationViewHolder.view_line.setVisibility(View.VISIBLE);
        }

        informationViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, entity);
            }
        });
        if (entity.isReaded()) {
            informationViewHolder.view_readed.setVisibility(View.GONE);
        } else {
            informationViewHolder.view_readed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_information_sys, parent, false);
        InformationViewHolder holder = new InformationViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InformationViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_content;
        private View view_line;
        private View view_readed;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            view_line = itemView.findViewById(R.id.view_line);
            view_readed = itemView.findViewById(R.id.view_readed);
        }
    }
}
