package com.tianji.blockchain.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.component.pinnedheader.PinnedHeaderAdapter;
import com.tianji.blockchain.entity.ArticleByDateEntity;
import com.tianji.blockchain.entity.ArticleItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** author:jason **/
public class ArticleListSkeletonAdapter extends PinnedHeaderAdapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_ITEM = 2;
    private static final int VIEW_TYPE_FOOTER = 3;

    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_COMPLETE = 2;

    private int state = STATE_IDLE;

    private class DataPointer {
        int parentIndex;
        int index;
    }

    private void analyzePointer() {
        if (this.pointers == null) {
            this.pointers = new ArrayList<>();
        }
        this.pointers.clear();

        if (this.mData != null && this.mData.size() > 0) {
            for (int i = 0; i < this.mData.size(); i ++) {
                ArticleByDateEntity item = this.mData.get(i);
                List<ArticleItemEntity> list = item.getArticleList();

                DataPointer parent = new DataPointer();
                parent.parentIndex = -1;
                parent.index = i;
                this.pointers.add(parent);

                if (list != null && list.size() > 0) {
                    for (int j = 0; j < list.size(); j ++) {
                        DataPointer child = new DataPointer();
                        child.parentIndex = i;
                        child.index = j;
                        this.pointers.add(child);
                    }
                }
            }
        }
    }

    private List<ArticleByDateEntity> mData;
    private List<DataPointer> pointers;

    public ArticleListSkeletonAdapter(List<ArticleByDateEntity> data) {
        this.mData = data;
        this.analyzePointer();
        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                analyzePointer();
                super.onChanged();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_group_title, parent, false));
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_footer, parent, false));
        } else {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_list_skeleton, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            DataPointer pointer = this.pointers.get(position);

            HeaderHolder itemView = (HeaderHolder) holder;
            ArticleByDateEntity itemData = this.mData.get(pointer.index);

            String datetimeString = itemData.getDate();
            Date datetimeValue = null;
            try {
                datetimeValue = new SimpleDateFormat("yyyy-MM-dd").parse(datetimeString);
            } catch(ParseException exception) {}
            if (datetimeValue != null) {
                itemView.tvDatetime.setText(datetimeString);

                Calendar mc = Calendar.getInstance();
                mc.setTime(datetimeValue);
                int dayOfWeek = mc.get(Calendar.DAY_OF_WEEK);
                switch(dayOfWeek) {
                    case Calendar.SUNDAY:
                        itemView.tvDayOfWeek.setText("星期天");
                        break;
                    case Calendar.MONDAY:
                        itemView.tvDayOfWeek.setText("星期一");
                        break;
                    case Calendar.TUESDAY:
                        itemView.tvDayOfWeek.setText("星期二");
                        break;
                    case Calendar.WEDNESDAY:
                        itemView.tvDayOfWeek.setText("星期三");
                        break;
                    case Calendar.THURSDAY:
                        itemView.tvDayOfWeek.setText("星期四");
                        break;
                    case Calendar.FRIDAY:
                        itemView.tvDayOfWeek.setText("星期五");
                        break;
                    case Calendar.SATURDAY:
                        itemView.tvDayOfWeek.setText("星期六");
                        break;
                }
            }
        } else if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            FooterHolder itemView = (FooterHolder) holder;
            if (this.state == STATE_IDLE) {
                itemView.layoutLoadMore.setVisibility(View.GONE);
                itemView.layoutNoMoreData.setVisibility(View.GONE);
            } else if (this.state == STATE_COMPLETE) {
                itemView.layoutLoadMore.setVisibility(View.GONE);
                itemView.layoutNoMoreData.setVisibility(View.VISIBLE);
            } else if (this.state == STATE_LOADING) {
                itemView.layoutLoadMore.setVisibility(View.VISIBLE);
                itemView.layoutNoMoreData.setVisibility(View.GONE);
            }
        } else {
            DataPointer pointer = this.pointers.get(position);

            ItemHolder itemView = (ItemHolder) holder;
            ArticleItemEntity itemData = this.mData.get(pointer.parentIndex).getArticleList().get(pointer.index);
        }
    }

    /**
     * 切换状态
     * @param state
     */
    public void toggleState(int state) {
        this.state = state;
    }

    @Override
    public int getItemCount() {
        return pointers == null ? 0 : pointers.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == pointers.size()) {
            return VIEW_TYPE_FOOTER;
        } else if (pointers.get(position).parentIndex == -1) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public boolean isPinnedPosition(int position) {
        return getItemViewType(position) == VIEW_TYPE_HEADER;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView tvDayOfWeek;
        public TextView tvDatetime;

        HeaderHolder(View itemView) {
            super(itemView);
            this.tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            this.tvDatetime = itemView.findViewById(R.id.tvDatetime);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        ItemHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutLoadMore;
        public LinearLayout layoutNoMoreData;

        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            this.layoutLoadMore = itemView.findViewById(R.id.layoutLoadMore);
            this.layoutNoMoreData = itemView.findViewById(R.id.layoutNoMoreData);
        }
    }
}
