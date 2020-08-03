package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.entity.AllObWalletEntity;
import com.tianji.blockchain.utils.LogUtils;

import java.util.List;

public class HardwareListAdapter extends BasicItemClickRecyclerViewAdapter<AllObWalletEntity> {

    public HardwareListAdapter(Context context, List<AllObWalletEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HardwareListViewHolder hardwareListViewHolder = (HardwareListViewHolder) holder;
        AllObWalletEntity entity = data.get(position);

        if (entity.isSelected()) {
            hardwareListViewHolder.img_hardware.setImageResource(setPosition(true, position));
            hardwareListViewHolder.line.setVisibility(View.VISIBLE);
        } else {
            hardwareListViewHolder.img_hardware.setImageResource(setPosition(false, position));
            hardwareListViewHolder.line.setVisibility(View.GONE);
        }

        hardwareListViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, entity);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hardware_list, parent, false);
        HardwareListViewHolder holder = new HardwareListViewHolder(view);
        return holder;
    }

    public int getItemCount() {
        return data.size();
    }

    public class HardwareListViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_hardware;
        private RelativeLayout root_view;
        private View line;

        public HardwareListViewHolder(@NonNull View itemView) {
            super(itemView);
            img_hardware = itemView.findViewById(R.id.img_hardware);
            root_view = itemView.findViewById(R.id.root_view);
            line = itemView.findViewById(R.id.line);
        }
    }

    private int setPosition(boolean isSelected, int position) {
        switch (position) {
            case 0:
                if (isSelected) {
                    return R.drawable.a_icon_selected;
                } else {
                    return R.drawable.a_icon_normal;
                }
            case 1:
                if (isSelected) {
                    return R.drawable.b_icon_selected;
                } else {
                    return R.drawable.b_icon_normal;
                }
            case 2:
                if (isSelected) {
                    return R.drawable.c_icon_selected;
                } else {
                    return R.drawable.c_icon_normal;
                }
            case 3:
                if (isSelected) {
                    return R.drawable.d_icon_selected;
                } else {
                    return R.drawable.d_icon_normal;
                }
            case 4:
                if (isSelected) {
                    return R.drawable.e_icon_selected;
                } else {
                    return R.drawable.e_icon_normal;
                }
            case 5:
                if (isSelected) {
                    return R.drawable.f_icon_selected;
                } else {
                    return R.drawable.f_icon_normal;
                }
            case 6:
                if (isSelected) {
                    return R.drawable.g_icon_selected;
                } else {
                    return R.drawable.g_icon_normal;
                }
            case 7:
                if (isSelected) {
                    return R.drawable.h_icon_selected;
                } else {
                    return R.drawable.h_icon_normal;
                }
            case 8:
                if (isSelected) {
                    return R.drawable.i_icon_selected;
                } else {
                    return R.drawable.i_icon_normal;
                }
            case 9:
                if (isSelected) {
                    return R.drawable.j_icon_selected;
                } else {
                    return R.drawable.j_icon_normal;
                }
        }
        return -1;
    }

}
