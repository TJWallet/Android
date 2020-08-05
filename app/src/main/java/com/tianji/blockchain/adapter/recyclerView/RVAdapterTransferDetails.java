package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.assets.TransferDetailsActivity;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.entity.TransferDetailsInfo;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RVAdapterTransferDetails extends BasicItemClickRecyclerViewAdapter<TransferRecode> {
    private Chain chain;
    private int decimal;

    public RVAdapterTransferDetails(Context context, List<TransferRecode> data, Chain chain) {
        this.context = context;
        this.data = data;
        this.chain = chain;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TransferDetailsViewHolder transferDetailsViewHolder = (TransferDetailsViewHolder) holder;
        if (data.get(position).getTokenDecimal() != null) {
            decimal = Integer.parseInt(data.get(position).getTokenDecimal());
        } else {
            decimal = 18;
        }
        if (data.get(position).isTransferInTo()) {
            if (data.get(position).isWait()) {
                if (data.get(position).getTransferType() == 3) {
                    transferDetailsViewHolder.img_icon.setImageResource(R.drawable.transfer_now);
                } else {
                    transferDetailsViewHolder.img_icon.setImageResource(R.drawable.collect_icon);
                }
            } else {
                transferDetailsViewHolder.img_icon.setImageResource(R.drawable.collect_icon);
            }

            transferDetailsViewHolder.tv_address.setText(data.get(position).getFrom());

            transferDetailsViewHolder.tv_amount.setText("+" + new BigDecimal(data.get(position).getValue()).divide(new BigDecimal(Math.pow(10, decimal)), 4, RoundingMode.DOWN).toString());

        } else {
            if (data.get(position).isWait()) {
                if (data.get(position).getTransferType() == 3) {
                    transferDetailsViewHolder.img_icon.setImageResource(R.drawable.transfer_now);
                } else {
                    transferDetailsViewHolder.img_icon.setImageResource(R.drawable.transfer_icon);
                }
            } else {
                transferDetailsViewHolder.img_icon.setImageResource(R.drawable.transfer_icon);
            }
            transferDetailsViewHolder.tv_address.setText(data.get(position).getTo());

            transferDetailsViewHolder.tv_amount.setText("-" + new BigDecimal(data.get(position).getValue()).divide(new BigDecimal(Math.pow(10, decimal)), 4, RoundingMode.DOWN).toString());

        }
        if (data.get(position).isWait()) {
            if (data.get(position).getTransferType() == 3) {
                transferDetailsViewHolder.tv_time.setText(context.getResources().getString(R.string.transfer_wait));
                transferDetailsViewHolder.tv_time.setTextColor(context.getResources().getColor(R.color.textMinor));
                transferDetailsViewHolder.progress.setVisibility(View.VISIBLE);
                if (((System.currentTimeMillis() / 1000) - data.get(position).getTimestamp()) < 1800) {
                    transferDetailsViewHolder.progress.setProgress((int) ((System.currentTimeMillis() / 1000) - data.get(position).getTimestamp()));
                } else {
                    transferDetailsViewHolder.progress.setProgress(1800);
                }
            } else if (data.get(position).getTransferType() == 4) {
                transferDetailsViewHolder.progress.setVisibility(View.GONE);
                transferDetailsViewHolder.tv_time.setTextColor(context.getResources().getColor(R.color.warning_red));
                transferDetailsViewHolder.tv_time.setText(context.getResources().getString(R.string.transfer_maybe_has_error));
            }

        } else {
            transferDetailsViewHolder.tv_time.setText(TimeUtils.timeStamp2Date(data.get(position).getTimestamp() * 1000, "yyyy-MM-dd HH:mm:ss"));
            transferDetailsViewHolder.progress.setVisibility(View.GONE);
            transferDetailsViewHolder.tv_time.setTextColor(context.getResources().getColor(R.color.textMinor));
        }

        transferDetailsViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, data.get(position));
            }
        });
        if (position == data.size() - 1) {
            transferDetailsViewHolder.view_line.setVisibility(View.GONE);
        } else {
            transferDetailsViewHolder.view_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_details, parent, false);
        TransferDetailsViewHolder holder = new TransferDetailsViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransferDetailsViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private ImageView img_icon;
        private TextView tv_address;
        private TextView tv_time;
        private TextView tv_amount;
        private View view_line;
        private ProgressBar progress;

        public TransferDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            view_line = itemView.findViewById(R.id.view_line);
            progress = itemView.findViewById(R.id.progress);

        }
    }
}
