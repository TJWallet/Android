package com.tianji.blockchain.fragment.find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.dappdetail.DappDetailActivity;
import com.tianji.blockchain.activity.dapplist.DappListActivity;
import com.tianji.blockchain.activity.searchdapp.SearchDappActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRankingListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRankingListSkeletonAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRecentListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRecommendSimpleListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoverySimpleListSkeletonAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryTopicListAdapter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.ChoosePlatformDialog;
import com.tianji.blockchain.entity.BannerEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappPlatformEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:jason
 **/
public class FindFragment extends BaseFragment implements BasicMvpInterface, View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView svMainContent;
    private Banner bannerTop;
    private TextView tvGoSearch;
    private ImageButton btnScanCode;
    private LinearLayout layoutRecentUse;
    private Button btnMoreRecent;
    private ViewGroup layoutRecentNoData;
    private Button btnAddRecent;
    private Button btnMoreRecommend;
    private Button btnMoreRanking;
    private LinearLayout layoutLoadMore;
    private ImageView ivBannerPlaceholder;
    private LinearLayout layoutPoweredBy;
    private TextView tvWebsite;
    private TextView tvRecentTitle;
    private TextView tvRecommendTitle;
    private TextView tvRankingTitle;

    private boolean initStatus = false;
    private boolean hasMoreData = true;
    private int pageIndex = 1;
    private boolean isLocked = false;
    private boolean isRefreshing = false;

    private RecyclerView rvRecommendSkeletonList;
    private RecyclerView rvRankingSkeletonList;

    private RecyclerView rvRecentList;
    private RecyclerView rvRecommendList;
    private RecyclerView rvRankingList;
    private RecyclerView rvTopicList;

    private List<BannerEntity> bannerList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private List<RecentHistoryEntity> recentList = new ArrayList<>();
    private List<String> recommendSkeletonList = new ArrayList<>();
    private List<RecommendEntity> recommendList = new ArrayList<>();
    private List<String> rankingSkeletonList = new ArrayList<>();
    private List<DappEntity> rankingList = new ArrayList<>();
    private List<DappTopicEntity> topicList = new ArrayList<>();

    private ChoosePlatformDialog platformDialog;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_find;
    }

    private void init() {
        this.setData();

        this.recommendSkeletonList.clear();
        for (int i = 0; i < 8; i++) {
            this.recommendSkeletonList.add("placeholder");
        }
        this.rvRecommendSkeletonList.getAdapter().notifyDataSetChanged();

        this.rankingSkeletonList.clear();
        for (int i = 0; i < 5; i++) {
            this.rankingSkeletonList.add("placeholder");
        }
        this.rvRankingSkeletonList.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case 1: {
                this.bannerTop.setVisibility(View.VISIBLE);
                this.ivBannerPlaceholder.setVisibility(View.GONE);

                this.bannerList.clear();
                List<BannerEntity> temp = (List<BannerEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.bannerList.addAll(temp);
                }
                this.updateBanner(this.bannerList);
            }
            break;
            case 2: {
                this.recentList.clear();
                List<RecentHistoryEntity> temp = (List<RecentHistoryEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.layoutRecentUse.setVisibility(View.VISIBLE);
                    this.rvRecentList.setVisibility(View.VISIBLE);
                    this.layoutRecentNoData.setVisibility(View.GONE);

                    this.recentList.addAll(temp);
                    this.rvRecentList.getAdapter().notifyDataSetChanged();
                } else {
                    this.rvRecentList.setVisibility(View.GONE);
                    this.layoutRecentNoData.setVisibility(View.VISIBLE);
                }
            }
            break;
            case 3: {
                this.rvRecommendSkeletonList.setVisibility(View.GONE);
                this.rvRecommendList.setVisibility(View.VISIBLE);

                this.recommendList.clear();
                List<RecommendEntity> temp = (List<RecommendEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.recommendList.addAll(temp);
                }
                this.rvRecommendList.getAdapter().notifyDataSetChanged();
            }
            break;
            case 4: {
                this.rvRankingSkeletonList.setVisibility(View.GONE);
                this.rvRankingList.setVisibility(View.VISIBLE);

                this.rankingList.clear();
                List<DappEntity> temp = (List<DappEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.rankingList.addAll(temp);
                }
                this.rvRankingList.getAdapter().notifyDataSetChanged();
            }
            break;
            case 5: {
                if (isRefreshing) {
                    this.topicList.clear();
                }
                this.layoutLoadMore.setVisibility(View.GONE);
                this.isLocked = false;

                List<DappTopicEntity> temp = (List<DappTopicEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.topicList.addAll(temp);
                } else {
                    this.hasMoreData = false;
                }
                this.rvTopicList.getAdapter().notifyDataSetChanged();
            }
            break;
            case 6: {
                this.isRefreshing = false;
            }
            break;
        }
    }

    private void loadMore() {
        if (this.hasMoreData && !isLocked && !isRefreshing && this.topicList != null && this.topicList.size() > 0) {
            this.isLocked = true;
            this.pageIndex++;
            this.layoutLoadMore.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<>();
            params.put("action", "fetchTopicList");
            params.put("pageIndex", pageIndex + "");
            this.presenter.getData(params);
        }
    }

    @Override
    protected void initView() {
        this.swipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout);
        this.svMainContent = this.findViewById(R.id.svMainContent);
        this.tvGoSearch = this.findViewById(R.id.tvGoSearch);
        this.btnScanCode = this.findViewById(R.id.btnScanCode);
        this.bannerTop = this.findViewById(R.id.bannerTop);
        this.btnMoreRecent = this.findViewById(R.id.btnMoreRecent);
        this.rvRecentList = this.findViewById(R.id.rvRecentList);
        this.layoutRecentNoData = this.findViewById(R.id.layoutRecentNoData);
        this.btnAddRecent = this.findViewById(R.id.btnAddRecent);
        this.btnMoreRecommend = this.findViewById(R.id.btnMoreRecommend);
        this.rvRecommendList = this.findViewById(R.id.rvRecommendList);
//        this.btnChoosePlatform = this.findViewById(R.id.btnChoosePlatform);
        this.btnMoreRanking = this.findViewById(R.id.btnMoreRanking);
        this.rvRankingList = this.findViewById(R.id.rvRankingList);
        this.rvTopicList = this.findViewById(R.id.rvTopicList);
        this.layoutLoadMore = this.findViewById(R.id.layoutLoadMore);
        this.rvRankingSkeletonList = findViewById(R.id.rvRankingSkeletonList);
        this.rvRecommendSkeletonList = findViewById(R.id.rvRecommendSkeletonList);
        this.layoutRecentUse = findViewById(R.id.layoutRecentUse);
        this.ivBannerPlaceholder = findViewById(R.id.ivBannerPlaceholder);
        this.layoutPoweredBy = findViewById(R.id.layoutPoweredBy);
        this.tvWebsite = this.findViewById(R.id.tvWebsite);
        this.tvWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tvWebsite.getPaint().setAntiAlias(true);
        this.tvRecentTitle = findViewById(R.id.tvRecentTitle);
        this.tvRecommendTitle = findViewById(R.id.tvRecommendTitle);
        this.tvRankingTitle = findViewById(R.id.tvRankingTitle);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (isRefreshing) {
                        return;
                    }
                    isRefreshing = true;
                    pageIndex = 1;
                    hasMoreData = true;
                    invokeInitInterface(false, false);
                }
            }
        });

        this.bindBannerData(this.bannerTop);

        this.svMainContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    loadMore();
                }
            }
        });
        this.rvRecentList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.rvRecentList.setNestedScrollingEnabled(false);
        DiscoveryRecentListAdapter recentAdapter = new DiscoveryRecentListAdapter(getContext(), this.recentList, 4);
        this.rvRecentList.setAdapter(recentAdapter);
        recentAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                RecentHistoryEntity data = (RecentHistoryEntity) itemData;
                if (data.getMid() != null) {
                    RecentHistoryEntity entity = new RecentHistoryEntity();
                    entity.setDescription(data.getDescription());
                    entity.setIcon(data.getIcon());
                    entity.setType(data.getType());
                    entity.setTypeColor(data.getTypeColor());
                    entity.setSummary(data.getSummary());
                    entity.setPlatformName(data.getPlatformName());
                    entity.setMid(data.getMid());
                    entity.setLink(data.getLink());
                    entity.setDescriptionEn(data.getDescriptionEn());
                    entity.setSummaryEn(data.getSummaryEn());
                    entity.setTypeEn(data.getTypeEn());
                    entity.setPlatformNameEn(data.getPlatformNameEn());
                    RecentHistorySharedPreferences.getInstance(getContext()).pushHistory(entity);

                    Intent intent = new Intent(getContext(), DappDetailActivity.class);
                    intent.putExtra("mid", data.getMid());
                    startActivity(intent);
                } else {
//                    Intent intent = new Intent(getContext(), CommonWebviewActivity.class);
//                    intent.putExtra("data", data.getDescription());
//                    startActivity(intent);

                    RecentHistoryEntity entity = new RecentHistoryEntity();
                    entity.setDescription(data.getDescription());
                    entity.setSummary(data.getSummary());
                    entity.setIcon(data.getIcon());
                    entity.setLink(data.getLink());
                    entity.setDescriptionEn(data.getDescriptionEn());
                    entity.setSummaryEn(data.getSummaryEn());
                    entity.setTypeEn(data.getTypeEn());
                    entity.setPlatformNameEn(data.getPlatformNameEn());
                    RecentHistorySharedPreferences.getInstance(getContext()).pushHistory(entity);

                    Uri uri = Uri.parse(data.getLink().trim());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        this.rvRecommendSkeletonList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.rvRecommendSkeletonList.setNestedScrollingEnabled(false);
        this.rvRecommendSkeletonList.setAdapter(new DiscoverySimpleListSkeletonAdapter(getContext(), this.recommendSkeletonList));

        this.rvRecommendList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.rvRecommendList.setNestedScrollingEnabled(false);
        DiscoveryRecommendSimpleListAdapter recommendAdapter = new DiscoveryRecommendSimpleListAdapter(getContext(), this.recommendList, 8);
        this.rvRecommendList.setAdapter(recommendAdapter);
        recommendAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                RecommendEntity data = (RecommendEntity) itemData;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setMid(data.getMid());
                entity.setDescription(data.getTitle());
                entity.setIcon(data.getIcon());
                entity.setType(data.getTypeName());
                entity.setTypeColor(data.getTypeColor());
                entity.setSummary(data.getSubtitle());
                entity.setPlatformName(data.getPlatformName());
                entity.setLink(data.getLink());
                entity.setDescriptionEn(data.getTitleEn());
                entity.setSummaryEn(data.getSubtitleEn());
                entity.setTypeEn(data.getTypeNameEn());
                entity.setPlatformNameEn(data.getPlatformNameEn());
                RecentHistorySharedPreferences.getInstance(getContext()).pushHistory(entity);

                if (data.getMid() == null) {
                    String link = data.getLink();
                    if (link != null && link.trim().length() > 0) {
                        Uri uri = Uri.parse(link.trim());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getContext(), DappDetailActivity.class);
                    intent.putExtra("mid", data.getMid());
                    startActivity(intent);
                }
            }
        });

        this.rvRankingSkeletonList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rvRankingSkeletonList.setNestedScrollingEnabled(false);
        this.rvRankingSkeletonList.setAdapter(new DiscoveryRankingListSkeletonAdapter(getContext(), this.rankingSkeletonList));

        this.rvRankingList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rvRankingList.setNestedScrollingEnabled(false);
        DiscoveryRankingListAdapter rankingAdapter = new DiscoveryRankingListAdapter(getContext(), this.rankingList, 5);
        this.rvRankingList.setAdapter(rankingAdapter);
        rankingAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                DappEntity data = (DappEntity) itemData;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                entity.setDescription(data.getName());
                entity.setIcon(data.getIcon());
                entity.setType(data.getTypeName());
                entity.setTypeColor(data.getTypeColor());
                entity.setSummary(data.getSummary());
                entity.setPlatformName(data.getPlatformName());
                entity.setMid(data.getMid());
                entity.setDescriptionEn(data.getNameEn());
                entity.setSummaryEn(data.getSummaryEn());
                entity.setTypeEn(data.getTypeNameEn());
                entity.setPlatformNameEn(data.getPlatformNameEn());
                RecentHistorySharedPreferences.getInstance(getContext()).pushHistory(entity);

                Intent intent = new Intent(getContext(), DappDetailActivity.class);
                intent.putExtra("mid", data.getMid());
                startActivity(intent);
            }
        });

        this.rvTopicList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.rvTopicList.setNestedScrollingEnabled(false);
        this.rvTopicList.setAdapter(new DiscoveryTopicListAdapter(getContext(), this.topicList));

        this.btnScanCode.setOnClickListener(this);
        this.btnMoreRecent.setOnClickListener(this);
        this.btnAddRecent.setOnClickListener(this);
        this.btnMoreRecommend.setOnClickListener(this);
//        this.btnChoosePlatform.setOnClickListener(this);
        this.btnMoreRanking.setOnClickListener(this);
        this.tvGoSearch.setOnClickListener(this);
        this.tvWebsite.setOnClickListener(this);
        this.tvRecentTitle.setOnClickListener(this);
        this.tvRecommendTitle.setOnClickListener(this);
        this.tvRankingTitle.setOnClickListener(this);

        this.platformDialog = new ChoosePlatformDialog(this.getContext(), R.style.BottomDialogTheme, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DappPlatformEntity currentPlatform = platformDialog.getCurrentPlatform();
                platformDialog.dismiss();
                rvRankingList.setVisibility(View.GONE);
                rvRankingSkeletonList.setVisibility(View.VISIBLE);

                Map<String, String> params = new HashMap<>();
                params.put("action", "fetchRankingList");
                params.put("currentPlatformID", currentPlatform.getMid());
                presenter.getData(params);
            }
        });
    }

    private void updateBanner(List<BannerEntity> data) {
        List<String> bindData = new ArrayList<>();
        for (BannerEntity item : data) {
            bindData.add(item.getThumbnail());
        }
        this.bannerTop.update(bindData);
    }

    private void bindBannerData(final Banner banner) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new ImageLoaderInterface() {
            @Override
            public void displayImage(Context context, Object path, View imageView) {
//                ImageLoader.getInstance().displayImage(path.toString(), (ImageView) imageView);
                ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_discovery_banner_default)).loadImage(getContext(), (ImageView) imageView, path.toString());
            }

            @Override
            public View createImageView(Context context) {
                ImageView view = new ImageView(context);
                return view;
            }
        });
        banner.setImages(urlList);
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);

        banner.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
//                    BannerEntity item = list.get(position);
//                    Intent intent = new Intent(getContext(), CommonWebviewActivity.class);
//                    intent.putExtra("title", item.getTitle());
//                    startActivity(intent);
                        BannerEntity item = bannerList.get(position);
                        if ("0".equals(item.getRefID())) {
                            Uri uri = Uri.parse(item.getLink().trim());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), DappDetailActivity.class);
                            intent.putExtra("mid", item.getRefID());
                            startActivity(intent);
                        }
                    }
                }).start();
    }

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
        // 每次进入刷新最近使用列表
        if (this.initStatus) {
            Map<String, String> params = new HashMap<>();
            params.put("action", "fetchRecentList");
            presenter.getData(params);
        }

        this.checkViewStatus();
    }

    private void checkViewStatus() {
        if (this.recommendList != null && this.recommendList.size() > 0) {
            this.rvRecommendSkeletonList.setVisibility(View.GONE);
            this.rvRecommendList.setVisibility(View.VISIBLE);
        } else {
            this.rvRecommendSkeletonList.setVisibility(View.VISIBLE);
            this.rvRecommendList.setVisibility(View.GONE);
        }

        if (this.rankingList != null && this.rankingList.size() > 0) {
            this.rvRankingSkeletonList.setVisibility(View.GONE);
            this.rvRankingList.setVisibility(View.VISIBLE);
        } else {
            this.rvRankingSkeletonList.setVisibility(View.VISIBLE);
            this.rvRankingList.setVisibility(View.GONE);
        }
    }

    private void invokeInitInterface(boolean fromCache, boolean isWithSkeleton) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "init");
        if (fromCache) {
            params.put("cache", "true");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRefreshing = true;
                    pageIndex = 1;
                    hasMoreData = true;
                    invokeInitInterface(false, false);
                }
            }, 500);
        }
        presenter.getData(params);

        this.layoutPoweredBy.setVisibility(View.GONE);

        if (isWithSkeleton) {
            this.bannerTop.setVisibility(View.GONE);
            this.ivBannerPlaceholder.setVisibility(View.VISIBLE);

            rvRecommendList.setVisibility(View.GONE);
            rvRecommendSkeletonList.setVisibility(View.VISIBLE);

            rvRankingList.setVisibility(View.GONE);
            rvRankingSkeletonList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new FindFragmentPresenter(WalletApplication.getApp(), this);
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
            case 1:
                // TODO 广告获取失败
                break;
            case 3:
                showToast(R.string.get_recommend_fail_try_again);
                break;
            case 4:
                showToast(R.string.get_rank_fail_try_again);
                break;
            case 5:
                this.layoutLoadMore.setVisibility(View.GONE);
                this.isLocked = false;
                if (errCode == 0) {
                    this.hasMoreData = false;
                    this.layoutPoweredBy.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    String result = bundle.getString("result");
//                    Intent intent = new Intent(getContext(), CommonWebviewActivity.class);
//                    intent.putExtra("data", result);
//                    this.startActivity(intent);

                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if (result.indexOf("http://") == 0 || result.indexOf("https://") == 0) {
                        RecentHistoryEntity entity = new RecentHistoryEntity();
                        entity.setLink(result);
                        RecentHistorySharedPreferences.getInstance(getContext()).pushHistory(entity);

                        Uri uri = Uri.parse(result.trim());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        final int resourceID = v.getId();

        switch (resourceID) {
            case R.id.tvGoSearch: {
                Intent intent = new Intent(getActivity(), SearchDappActivity.class);
                this.startActivity(intent);
            }
            break;
            case R.id.btnScanCode: {
                // 扫描二维码
                CommonUtils.openScan(this.getActivity());
            }
            break;
            case R.id.btnMoreRecent:
            case R.id.tvRecentTitle: {
                LogUtils.log("点击 recent 跳转");
                if (this.recentList != null && this.recentList.size() > 0) {
                    Intent intent = new Intent(getActivity(), DappListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getResources().getString(R.string.recently_used));
                    bundle.putString("from", "recent");
                    bundle.putSerializable("data", (ArrayList<RecentHistoryEntity>) this.recentList);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                }
            }
            break;
            case R.id.btnAddRecent:
            case R.id.btnMoreRecommend:
            case R.id.tvRecommendTitle: {
                LogUtils.log("点击 recommend 跳转");
                if (this.recommendList != null && this.recommendList.size() > 0) {
                    Intent intent = new Intent(getActivity(), DappListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getResources().getString(R.string.recommend_this_week));
                    bundle.putString("from", "recommend");
                    bundle.putSerializable("data", (ArrayList<RecommendEntity>) this.recommendList);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                }
            }
            break;
//            case R.id.btnChoosePlatform: {
//                this.platformDialog.show();
//            }break;
            case R.id.btnMoreRanking:
            case R.id.tvRankingTitle: {
                LogUtils.log("点击 ranking 跳转");
                if (this.rankingList != null && this.rankingList.size() > 0) {
                    Intent intent = new Intent(getActivity(), DappListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getResources().getString(R.string.rank_list));
                    bundle.putString("from", "ranking");
                    bundle.putSerializable("data", (ArrayList<DappEntity>) this.rankingList);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                }
            }
            break;
            case R.id.tvWebsite: {
                Uri uri = Uri.parse("https://dapponline.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            break;
        }
    }
}
