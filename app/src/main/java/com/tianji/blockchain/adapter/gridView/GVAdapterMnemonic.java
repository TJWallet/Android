package com.tianji.blockchain.adapter.gridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.MnemoinicButtonEntity;

import java.util.List;

public class GVAdapterMnemonic extends BaseAdapter {
    private List<MnemoinicButtonEntity> mList;
    private LayoutInflater inflater;
    private Context context;

    public GVAdapterMnemonic(Context context, List<MnemoinicButtonEntity> mList) {
        this.mList = mList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mnemonic_block, null);
            holder = new ViewHolder();
            holder.tv_word = convertView.findViewById(R.id.tv_word);
            holder.tv_num_bg = convertView.findViewById(R.id.tv_num_bg);
            holder.root_view = convertView.findViewById(R.id.root_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_word.setText(mList.get(position).getTextContent());
        holder.tv_num_bg.setText((position + 1) + "");
        if (mList.get(position).isHasFocus()) {
            holder.root_view.setBackgroundColor(context.getResources().getColor(R.color.secondary));
        } else {
            holder.root_view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent15));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_word;
        TextView tv_num_bg;
        RelativeLayout root_view;
    }

}
