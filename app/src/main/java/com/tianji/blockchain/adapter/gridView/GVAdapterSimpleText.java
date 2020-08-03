package com.tianji.blockchain.adapter.gridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.createwallet.VerificationMnemonicActivity;

import java.util.List;

public class GVAdapterSimpleText extends BaseAdapter {
    private List<VerificationMnemonicActivity.MnemonicSelectorEntity> mList;
    private LayoutInflater inflater;
    private Context context;

    public GVAdapterSimpleText(Context context, List<VerificationMnemonicActivity.MnemonicSelectorEntity> mList) {
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
        GVAdapterSimpleText.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_simple_text, null);
            holder = new GVAdapterSimpleText.ViewHolder();
            holder.tv_word = convertView.findViewById(R.id.tv_word);
            convertView.setTag(holder);
        } else {
            holder = (GVAdapterSimpleText.ViewHolder) convertView.getTag();
        }
        if (mList.get(position).isSelected()) {
            holder.tv_word.setTextColor(context.getResources().getColor(R.color.tertiary));
        } else {
            holder.tv_word.setTextColor(context.getResources().getColor(R.color.textMajor));
        }
        holder.tv_word.setText(mList.get(position).getMnemonic());
        return convertView;
    }

    class ViewHolder {
        TextView tv_word;
    }
}
