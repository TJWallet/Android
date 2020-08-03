package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.component.pinnedheader.PinnedHeaderAdapter;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.ArticleByDateEntity;
import com.tianji.blockchain.entity.ArticleItemEntity;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.ErrEntity;
import com.tianji.blockchain.entity.IpfsItemEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.fragment.article.IPFSAddressClickListener;
import com.tianji.blockchain.fragment.article.IPFSAddressListListener;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author:jason
 **/
public class ArticleListAdapter extends PinnedHeaderAdapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_ITEM = 2;
    private static final int VIEW_TYPE_FOOTER = 3;

    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_COMPLETE = 2;

    private int state = STATE_IDLE;
    private Context context;

    private Gson gson;

    private IPFSAddressClickListener listener;
    private TipsDialog tipsDialog;

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
            for (int i = 0; i < this.mData.size(); i++) {
                ArticleByDateEntity item = this.mData.get(i);
                List<ArticleItemEntity> list = item.getArticleList();

                DataPointer parent = new DataPointer();
                parent.parentIndex = -1;
                parent.index = i;
                this.pointers.add(parent);

                if (list != null && list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
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

    public ArticleListAdapter(Context context, List<ArticleByDateEntity> data, IPFSAddressClickListener listener) {
        this.mData = data;
        this.context = context;
        this.listener = listener;
        gson = new Gson();
        this.analyzePointer();
        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                analyzePointer();
                super.onChanged();
            }
        });
        DialogEntity entity = new DialogEntity(context.getResources().getString(R.string.ipfs_help), context.getResources().getString(R.string.ipfs_help_desc), context.getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipsDialog != null) {
                    if (tipsDialog.isShowing()) {
                        tipsDialog.dismiss();
                    }
                }
            }
        });
        tipsDialog = new TipsDialog(context, entity, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_group_title, parent, false));
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_footer, parent, false));
        } else {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_list, parent, false));
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
            itemView.ll_help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipsDialog.show();
                }
            });
            try {
                datetimeValue = new SimpleDateFormat("yyyy-MM-dd").parse(datetimeString);
            } catch (ParseException exception) {
            }
            if (datetimeValue != null) {
                itemView.tvDatetime.setText(datetimeString);

                Calendar mc = Calendar.getInstance();
                mc.setTime(datetimeValue);
                int dayOfWeek = mc.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case Calendar.SUNDAY:
                        itemView.tvDayOfWeek.setText(R.string.sunday);
                        break;
                    case Calendar.MONDAY:
                        itemView.tvDayOfWeek.setText(R.string.monday);
                        break;
                    case Calendar.TUESDAY:
                        itemView.tvDayOfWeek.setText(R.string.tuesday);
                        break;
                    case Calendar.WEDNESDAY:
                        itemView.tvDayOfWeek.setText(R.string.wednesday);
                        break;
                    case Calendar.THURSDAY:
                        itemView.tvDayOfWeek.setText(R.string.thersday);
                        break;
                    case Calendar.FRIDAY:
                        itemView.tvDayOfWeek.setText(R.string.friday);
                        break;
                    case Calendar.SATURDAY:
                        itemView.tvDayOfWeek.setText(R.string.saturday);
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

            ArticleItemEntity itemData = this.mData.get(pointer.parentIndex).getArticleList().get(pointer.index);
            ItemHolder itemView = (ItemHolder) holder;
            itemView.tvTime.setText(itemData.getDatetime());
            itemView.tvItemTitle.setText(itemData.getTitle());
            itemView.tvItemSummary.setText(itemData.getContent());
            if (itemData.getIpfs_hash() != null) {
                itemView.tv_ipfs.setVisibility(View.VISIBLE);
                String ipfs = String.format(context.getResources().getString(R.string.ipfs_address), itemData.getIpfs_hash());
                itemView.tv_ipfs.setText(ipfs);
            } else {
                itemView.tv_ipfs.setVisibility(View.GONE);
            }


            itemView.tv_ipfs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(itemData.getIpfs_hash());
//                    LogUtils.log("获取IPFS地址列表的参数id =" + itemData.getMid());
//                    listener.requestStart();
//                    Api.getInstance().getIpfsAddressList(false, itemData.getMid(), new HttpVolley.VolleyCallBack() {
//                        @Override
//                        public void onSuccess(String data) {
//                            LogUtils.log("获取到的IPFS的 JSON = " + data);
//                            try {
//                                JSONObject rootObj = new JSONObject(data);
//                                int status = rootObj.optInt("Status");
//                                if (status != 1) {
//                                    ErrEntity errEntity = gson.fromJson(data, ErrEntity.class);
//                                    listener.requestFail(errEntity);
//                                } else {
//                                    JSONObject resultObj = rootObj.optJSONObject("Result");
//                                    JSONArray dataArray = resultObj.optJSONArray("data");
//                                    List<IpfsItemEntity> ipfsItemEntityList = gson.fromJson(dataArray.toString(), new TypeToken<List<IpfsItemEntity>>() {
//                                    }.getType());
//                                    listener.requestSuccess(ipfsItemEntityList);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFail(VolleyError error) {
//                            listener.requestFail(error);
//                        }
//                    });
                }
            });
        }
    }

    /**
     * 切换状态
     *
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
        private LinearLayout ll_help;

        HeaderHolder(View itemView) {
            super(itemView);
            this.tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            this.tvDatetime = itemView.findViewById(R.id.tvDatetime);
            this.ll_help = itemView.findViewById(R.id.ll_help);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView tvTime;
        public TextView tvItemTitle;
        public TextView tvItemSummary;
        private TextView tv_ipfs;

        ItemHolder(View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.tvTime);
            this.tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            this.tvItemSummary = itemView.findViewById(R.id.tvItemSummary);
            this.tv_ipfs = itemView.findViewById(R.id.tv_ipfs);
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
