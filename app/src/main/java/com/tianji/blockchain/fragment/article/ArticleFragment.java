package com.tianji.blockchain.fragment.article;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.dappdetail.DappDetailActivity;
import com.tianji.blockchain.activity.dapplist.DappListActivity;
import com.tianji.blockchain.activity.searchdapp.SearchDappActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.ArticleListAdapter;
import com.tianji.blockchain.adapter.recyclerView.ArticleListSkeletonAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRankingListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRankingListSkeletonAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRecentListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRecommendSimpleListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoverySimpleListSkeletonAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryTopicListAdapter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.component.pinnedheader.PinnedHeaderAdapter;
import com.tianji.blockchain.component.pinnedheader.PinnedHeaderItemDecoration;
import com.tianji.blockchain.component.pinnedheader.PinnedHeaderRecyclerView;
import com.tianji.blockchain.dialog.ChoosePlatformDialog;
import com.tianji.blockchain.dialog.IPFSAddressListDialog;
import com.tianji.blockchain.entity.ArticleByDateEntity;
import com.tianji.blockchain.entity.ArticleItemEntity;
import com.tianji.blockchain.entity.ArticlePagerEntity;
import com.tianji.blockchain.entity.BannerEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappPlatformEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
import com.tianji.blockchain.entity.ErrEntity;
import com.tianji.blockchain.entity.IpfsItemEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.entity.RecommendEntity;
import com.tianji.blockchain.fragment.basic.BaseFragment;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:jason
 **/
public class ArticleFragment extends BaseFragment implements BasicMvpInterface, View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private PinnedHeaderRecyclerView rvArticleList;
    private PinnedHeaderRecyclerView rvArticleSkeletonList;
    private ArticleListAdapter adapter;


    private boolean IS_WITH_FOOTER = false;

    private boolean initStatus = false;
    private boolean hasMoreData = true;
    private String startIndex = "0";
    private boolean isLocked = false;
    private boolean isRefreshing = false;
    private boolean dateBindStatus = false;
    private int max = 50;

    private List<ArticleByDateEntity> listData = new ArrayList<>();
    private List<ArticleByDateEntity> skeletonListData = new ArrayList<>();

    private IPFSAddressListDialog ipfsAddressListDialog;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_article;
    }

    private void init() {
        ArticleByDateEntity skeletonItem = new ArticleByDateEntity();
        skeletonItem.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        List<ArticleItemEntity> skeletonArticleList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ArticleItemEntity temp = new ArticleItemEntity();
            temp.setMid("0");
            temp.setTitle("placeholder");
            temp.setContent("placeholder");
            temp.setDatetime("placeholder");
            skeletonArticleList.add(temp);
        }

        skeletonItem.setArticleList(skeletonArticleList);
        skeletonListData.add(skeletonItem);
        this.rvArticleSkeletonList.getAdapter().notifyDataSetChanged();

        this.setData();
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case 1: {
                if (this.isRefreshing) {
                    this.listData.clear();
                    this.isRefreshing = false;
                    this.dateBindStatus = false;
                }
                this.isLocked = false;

                this.rvArticleList.setVisibility(View.VISIBLE);
                this.rvArticleSkeletonList.setVisibility(View.GONE);

                ArticlePagerEntity response = (ArticlePagerEntity) msg.obj;
//                this.startIndex = response.getLastId();

                if (response != null) {
                    if (response.getUpdateCount() > 0 && response.getUpdateCount() < 50) {
                        String content = String.format(getResources().getString(R.string.update_article_toast), response.getUpdateCount() + "");
                        showToast(content);
                    }
                    List<ArticleByDateEntity> data = response.getData();
                    if (data != null && data.size() > 0) {
                        for (ArticleByDateEntity item : data) {
                            List<ArticleItemEntity> list = item.getArticleList();

                            if (listData.size() > 0) {
                                boolean isAdded = false;
                                ArticleByDateEntity current = null;

                                for (ArticleByDateEntity mItem : listData) {
                                    if (mItem.getDate() != null && item.getDate() != null && mItem.getDate().equals(item.getDate())) {
                                        isAdded = true;
                                        current = mItem;
                                    }
                                }
                                if (isAdded) {
                                    if (list != null && list.size() > 0) {
                                        if (current.getArticleList() == null) {
                                            current.setArticleList(new ArrayList<ArticleItemEntity>());
                                        }
                                        current.getArticleList().addAll(list);
                                    }
                                } else {
                                    listData.add(item);
                                }
                            } else {
                                listData.add(item);
                            }
                        }
                    }
                }
                if (this.adapter != null) {
                    this.adapter.toggleState(ArticleListAdapter.STATE_COMPLETE);
                }
                this.rvArticleList.getAdapter().notifyDataSetChanged();
            }
            break;
        }
    }

    private void loadMore() {
        if (this.listData != null) {
            int total = 0;
            if (this.listData.size() > 0) {
                for (ArticleByDateEntity item : this.listData) {
                    List<ArticleItemEntity> list = item.getArticleList();
                    if (list != null && list.size() > 0) {
                        total += list.size();
                    }
                }
            }
            if (total >= max) return;
        }

        if (this.hasMoreData && !isLocked && !isRefreshing && this.listData != null && this.listData.size() > 0) {
            this.isLocked = true;
            if (this.adapter != null) {
                this.adapter.toggleState(ArticleListAdapter.STATE_LOADING);
            }
            Map<String, String> params = new HashMap<>();
            params.put("action", "fetchArticleList");
            params.put("startIndex", startIndex + "");
            params.put("pageSize", "10");
            this.presenter.getData(params);
        }
    }

    @Override
    protected void initView() {
        this.swipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout);
        this.rvArticleList = this.findViewById(R.id.rvArticleList);
        this.rvArticleSkeletonList = this.findViewById(R.id.rvArticleSkeletonList);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (isRefreshing) {
                        return;
                    }
                    isRefreshing = true;
//                    startIndex = "0";
                    if (listData != null && listData.size() > 0) {
                        startIndex = listData.get(0).getArticleList().get(0).getMid();
                    }
                    hasMoreData = true;
                    invokeInitInterface(false, false);
                }
            }
        });

        this.rvArticleSkeletonList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rvArticleSkeletonList.setAdapter(new ArticleListSkeletonAdapter(this.skeletonListData));
        this.rvArticleSkeletonList.addItemDecoration(new PinnedHeaderItemDecoration());

        this.rvArticleList.setLayoutManager(new LinearLayoutManager(getContext()));
        ArticleListAdapter articleAdapter = new ArticleListAdapter(getActivity(), this.listData, ipfsAddressClickListener);
        this.adapter = articleAdapter;
        this.rvArticleList.setAdapter(articleAdapter);
        this.rvArticleList.addItemDecoration(new PinnedHeaderItemDecoration());
        this.rvArticleList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (IS_WITH_FOOTER) {
                        loadMore();
                    }
                }
            }

//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int firstVisiblePosition = lm.findFirstVisibleItemPosition();
//
//                View firstVisibleView = lm.getChildAt(firstVisiblePosition);
//                PinnedHeaderAdapter adapter = (PinnedHeaderAdapter) recyclerView.getAdapter();
//                boolean isPinnedPosition = adapter.isPinnedPosition(firstVisiblePosition);
//
//                if (isPinnedPosition) {
//                    currentRecyclerHeader = firstVisibleView;
//                } else {
//                    if (currentRecyclerHeader != null && firstVisibleView != null) {
//                        TextView tvDatetime = currentRecyclerHeader.findViewById(R.id.tvDatetime);
//                        TextView tvTime = firstVisibleView.findViewById(R.id.tvTime);
//
//                        String originText = tvDatetime.getText().toString();
//                        if (originText.length() > "yyyy-MM-dd".length()) {
//                            originText = originText.substring(0, "yyyy-MM-dd".length());
//                        }
//                        originText += " " + tvTime.getText().toString();
//                        LogUtils.e(originText);
//                        tvDatetime.setText(originText);
//                        tvDatetime.setTextColor(Color.RED);
//                    }
//                }
//            }
        });
    }

    /***点击IPFS地址弹出DIALOG***/
    IPFSAddressClickListener ipfsAddressClickListener = new IPFSAddressClickListener() {
        @Override
        public void onClick(String hash) {
            if (ipfsAddressListDialog == null) {
                ipfsAddressListDialog = new IPFSAddressListDialog(getActivity(), R.style.Wallet_Manager_Dialog, hash);
            }
            ipfsAddressListDialog.show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);
        this.init();
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.rvArticleSkeletonList != null && this.rvArticleList != null) {
            if (this.listData != null && this.listData.size() > 0) {
                this.rvArticleList.setVisibility(View.VISIBLE);
                this.rvArticleSkeletonList.setVisibility(View.GONE);
            } else {
                this.rvArticleList.setVisibility(View.GONE);
                this.rvArticleSkeletonList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void invokeInitInterface(boolean fromCache, boolean isWithSkeleton) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "fetchArticleList");
        params.put("startIndex", startIndex);
        params.put("pageSize", "10");
        if (fromCache) {
            params.put("cache", "true");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRefreshing = true;
//                    startIndex = "0";
                    if (listData != null && listData.size() > 0) {
                        startIndex = listData.get(0).getArticleList().get(0).getMid();
                    }
                    hasMoreData = true;
                    invokeInitInterface(false, false);
                }
            }, 500);
        }
        presenter.getData(params);
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new ArticleFragmentPresenter(WalletApplication.getApp(), this);
        }

        if (!this.initStatus) {
            this.initStatus = true;
            this.invokeInitInterface(true, true);
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    public void getDataSuccess(Object data, int type) {
        Message msg = Message.obtain();
        msg.what = type;
        msg.obj = data;
        mHandler.sendMessage(msg);
    }

    @Override
    public void getDataFail(Object error, int type) {
    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {
        switch (type) {
            case 1: {
                // 获取快讯失败
                this.isLocked = false;
                this.isRefreshing = false;
                if (this.adapter != null) {
                    this.adapter.toggleState(ArticleListAdapter.STATE_IDLE);
                }
            }
            break;
        }
    }

    @Override
    public void onClick(View v) {
        final int resourceID = v.getId();
        switch (resourceID) {

        }
    }
}
