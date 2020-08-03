package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.aboutus.HelpCenterPresenter;
import com.tianji.blockchain.activity.aboutus.HelpDetailsActivity;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.entity.QuestionColumnEntity;
import com.tianji.blockchain.entity.QuestionItemEntity;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterQuestionColumn extends BasicItemClickRecyclerViewAdapter<QuestionColumnEntity> {
    private RVAdapterQuestionList adapter;
    private ColumnViewHolder holder;

    public RVAdapterQuestionColumn(Context context, List<QuestionColumnEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder questionColumnViewHolder, final int position) {
        holder = (ColumnViewHolder) questionColumnViewHolder;
        final QuestionColumnEntity entity = data.get(position);
        holder.tv_title.setText(entity.getTitle());
        List<QuestionItemEntity> questionItemEntityList = new ArrayList<>();
        questionItemEntityList.clear();
        if (entity.getQuestionListEntity().getTotalElements() > 5) {
            holder.ll_more.setVisibility(View.VISIBLE);
            holder.ll_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, position, entity.getQuestionListEntity());
                }
            });

            for (int i = 0; i < 5; i++) {
                questionItemEntityList.add(entity.getQuestionListEntity().getContent().get(i));
            }
            adapter = new RVAdapterQuestionList(context, questionItemEntityList, 0);
            adapter.setItemClickListener(questionListener);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setAdapter(adapter);
        } else {
            holder.ll_more.setVisibility(View.GONE);
            for (int i = 0; i < entity.getQuestionListEntity().getContent().size(); i++) {
                questionItemEntityList.add(entity.getQuestionListEntity().getContent().get(i));
            }
            adapter = new RVAdapterQuestionList(context, questionItemEntityList, 0);
            adapter.setItemClickListener(questionListener);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_column_list, parent, false);
        ColumnViewHolder holder = new ColumnViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ColumnViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private TextView tv_title;
        private LinearLayout ll_more;

        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.questionRecyclerView);
            tv_title = itemView.findViewById(R.id.tv_item_question_title);
            ll_more = itemView.findViewById(R.id.ll_more);
        }
    }


    public OnItemClickListener questionListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object obj) {
            QuestionItemEntity entity = (QuestionItemEntity) obj;
            Intent intent = new Intent(context, HelpDetailsActivity.class);
            intent.putExtra("_questionItemEntity", entity);
            context.startActivity(intent);
            LogUtils.log("点击了第" + position + "个问题" + entity.getTitle());
        }
    };
}
