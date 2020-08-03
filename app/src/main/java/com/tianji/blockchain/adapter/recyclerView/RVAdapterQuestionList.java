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
import com.tianji.blockchain.entity.QuestionItemEntity;

import java.util.List;

public class RVAdapterQuestionList extends BasicItemClickRecyclerViewAdapter<QuestionItemEntity> {
    private int type;

    public RVAdapterQuestionList(Context context, List<QuestionItemEntity> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        QuestionItemViewHolder questionListViewHolder = (QuestionItemViewHolder) holder;
        final QuestionItemEntity entity = data.get(position);

        if (type == 0) {
            questionListViewHolder.tv_title.setText(entity.getTitle());
        } else {
            int i = position + 1;
            questionListViewHolder.tv_title.setText(i + ". " + entity.getTitle());
        }

        questionListViewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, entity);
            }
        });
    }

    public int getItemCount() {
        return data.size();
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_help_questions, parent, false);
        QuestionItemViewHolder holder = new QuestionItemViewHolder(view);
        return holder;
    }

    class QuestionItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public QuestionItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_question);
        }
    }
}
