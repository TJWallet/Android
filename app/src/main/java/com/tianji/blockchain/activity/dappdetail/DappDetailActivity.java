package com.tianji.blockchain.activity.dappdetail;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicMvpActivity;
import com.tianji.blockchain.activity.smartcontract.SmartContractListActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappRelevantArticleListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappScreenshotListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappSmartContractListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappSocialListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoverySimpleListAdapter;
import com.tianji.blockchain.dialog.IPFSAddressListDialog;
import com.tianji.blockchain.dialog.SecurityTipsDialog;
import com.tianji.blockchain.entity.DappDetailEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.RatingStatisticsEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.entity.RelevantArticleEntity;
import com.tianji.blockchain.entity.SmartContractEntity;
import com.tianji.blockchain.entity.SocialMediaEntity;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.utils.ImageLoaderHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:jason
 **/
public class DappDetailActivity extends BasicMvpActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private LinearLayout layoutShare;
    private ImageView ivDappDetail;
    private ImageView ivIcon;
    private TextView tvDappName;
    private TextView tvSummary;
    private TextView tvPlatform;
    private TextView tvType;
    private ImageView ivFollow;
    private TextView tvFollowCount;
    private TextView tvScore;
    private RatingBar rbScore;
    private TextView tvRatingCount;
    private Button btnOpenWebsite;
    private TextView tvDescription;
    private TextView tvUser24H;
    private TextView tvUser24HPercentage;
    private ImageView ivUser24HUp;
    private ImageView ivUser24HDown;
    private TextView tvVolume24H;
    private TextView tvVolume24HPercentage;
    private ImageView ivVolume24HUp;
    private ImageView ivVolume24HDown;
    private TextView tvTx24H;
    private TextView tvTx24HPercentage;
    private ImageView ivTx24HUp;
    private ImageView ivTx24HDown;
    private TextView tvVolume7D;
    private TextView tvVolume7DPercentage;
    private ImageView ivVolume7DUp;
    private ImageView ivVolume7DDown;
    private TextView tvTx7D;
    private TextView tvTx7DPercentage;
    private ImageView ivTx7DUp;
    private ImageView ivTx7DDown;
    private RecyclerView rvSocial;
    private Button btnToggle24H;
    private Button btnToggle7D;
    private RecyclerView rvScreenshot;
    private LinearLayout layoutMoreSmartContract;
    private Button btnMoreSmartContract;
    private RecyclerView rvSmartContract;
    private RecyclerView rvRelevantArticle;
    private RecyclerView rvRecommend;
    private LinearLayout layoutSocial;
    private LinearLayout layoutScreenshot;
    private LinearLayout layoutSmartContract;
    private LinearLayout layoutRelevantArticle;
    private LinearLayout layoutRecommend;
    private TextView tvWebsite;
    private TextView tv_ipfs_hash;

    private List<SocialMediaEntity> socialList = new ArrayList<>();
    private List<String> screenshotList = new ArrayList<>();
    private List<SmartContractEntity> smartContractList = new ArrayList<>();
    private List<RelevantArticleEntity> relevantArticleList = new ArrayList<>();
    private List<DappEntity> recommendDappList = new ArrayList<>();
    private DappDetailEntity detailData;

    private SecurityTipsDialog tipsDialog;
    private IPFSAddressListDialog ipfsAddressListDialog;

    private void bindData() {
        if (this.detailData == null) {
            return;
        }
        /***ipfs hash***/
        if (detailData.getIpfs_hash() == null) {
            tv_ipfs_hash.setVisibility(View.GONE);
        } else {
            tv_ipfs_hash.setVisibility(View.VISIBLE);
            String ipfsHash = String.format(getResources().getString(R.string.ipfs_address), detailData.getIpfs_hash());
            tv_ipfs_hash.setText(ipfsHash);
        }

        if (WalletApplication.lang.equals("")) {
            this.tvTitle.setText(this.detailData.getName());

            this.tvDappName.setText(this.detailData.getName());
            this.tvSummary.setText(this.detailData.getSummary());
            this.tvPlatform.setText(this.detailData.getPlatformName());
            this.tvType.setText(this.detailData.getTypeName());
            this.tvType.setTextColor(Color.parseColor(detailData.getTypeColor()));
            this.tvFollowCount.setText(this.detailData.getFollowNumber() + "");
            this.tvDescription.setText(this.detailData.getDescription());

            this.tvUser24H.setText(this.detailData.getUser24H() + "");
            this.tvUser24HPercentage.setText(this.detailData.getUser24HPercent());
        } else {
            this.tvTitle.setText(this.detailData.getNameEN());

            this.tvDappName.setText(this.detailData.getNameEN());
            this.tvSummary.setText(this.detailData.getSummaryEN());
            this.tvPlatform.setText(this.detailData.getPlatformNameEN());
            this.tvType.setText(this.detailData.getTypeNameEN());
            this.tvType.setTextColor(Color.parseColor(detailData.getTypeColor()));
            this.tvFollowCount.setText(this.detailData.getFollowNumber() + "");
            this.tvDescription.setText(this.detailData.getDescriptionEN());

            this.tvUser24H.setText(this.detailData.getUser24H() + "");
            this.tvUser24HPercentage.setText(this.detailData.getUser24HPercent());
        }

        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_banner_default)).loadImage(this, this.ivDappDetail, this.detailData.getThumbnail());
        ImageLoaderHelper.getInstance().once(new ImageLoaderHelper.ImageConfig(R.drawable.img_default)).loadImage(this, this.ivIcon, this.detailData.getIcon());

        if (this.detailData.getUser24HPercent() != null && this.detailData.getUser24HPercent().indexOf("-") != -1) {
            this.ivUser24HUp.setVisibility(View.GONE);
            this.ivUser24HDown.setVisibility(View.VISIBLE);
        } else {
            this.ivUser24HUp.setVisibility(View.VISIBLE);
            this.ivUser24HDown.setVisibility(View.GONE);
        }

        this.tvVolume24H.setText(this.detailData.getVolume24H());
        this.tvVolume24HPercentage.setText(this.detailData.getVolume24HPercent());
        if (this.detailData.getVolume24HPercent() != null && this.detailData.getVolume24HPercent().indexOf("-") != -1) {
            this.ivVolume24HUp.setVisibility(View.GONE);
            this.ivVolume24HDown.setVisibility(View.VISIBLE);
        } else {
            this.ivVolume24HUp.setVisibility(View.VISIBLE);
            this.ivVolume24HDown.setVisibility(View.GONE);
        }

        this.tvTx24H.setText(this.detailData.getTx24H() + "");
        this.tvTx24HPercentage.setText(this.detailData.getTx24HPercent());
        if (this.detailData.getTx24HPercent() != null && this.detailData.getTx24HPercent().indexOf("-") != -1) {
            this.ivTx24HUp.setVisibility(View.GONE);
            this.ivTx24HDown.setVisibility(View.VISIBLE);
        } else {
            this.ivTx24HUp.setVisibility(View.VISIBLE);
            this.ivTx24HDown.setVisibility(View.GONE);
        }

        this.tvVolume7D.setText(this.detailData.getVolume7D());
        this.tvVolume7DPercentage.setText(this.detailData.getVolume7DPercent());
        if (this.detailData.getVolume7DPercent() != null && this.detailData.getVolume7DPercent().indexOf("-") != -1) {
            this.ivVolume7DUp.setVisibility(View.GONE);
            this.ivVolume7DDown.setVisibility(View.VISIBLE);
        } else {
            this.ivVolume7DUp.setVisibility(View.VISIBLE);
            this.ivVolume7DDown.setVisibility(View.GONE);
        }

        this.tvTx7D.setText(this.detailData.getTx7D() + "");
        this.tvTx7DPercentage.setText(this.detailData.getTx7DPercent());
        if (this.detailData.getTx7DPercent() != null && this.detailData.getTx7DPercent().indexOf("-") != -1) {
            this.ivTx7DUp.setVisibility(View.GONE);
            this.ivTx7DDown.setVisibility(View.VISIBLE);
        } else {
            this.ivTx7DUp.setVisibility(View.VISIBLE);
            this.ivTx7DDown.setVisibility(View.GONE);
        }

        if (this.detailData.getSocialList() != null && this.detailData.getSocialList().size() > 0) {
            this.socialList.clear();
            this.layoutSocial.setVisibility(View.VISIBLE);
            this.socialList.addAll(this.detailData.getSocialList());
            this.rvSocial.getAdapter().notifyDataSetChanged();
        }

        if (this.detailData.getScreenshotList() != null && this.detailData.getScreenshotList().size() > 0) {
            this.screenshotList.clear();
            this.layoutScreenshot.setVisibility(View.VISIBLE);
            this.screenshotList.addAll(this.detailData.getScreenshotList());
            this.rvScreenshot.getAdapter().notifyDataSetChanged();
        }

        if (this.detailData.getSmartContractList() != null && this.detailData.getSmartContractList().size() > 0) {
            smartContractList.clear();
            this.layoutSmartContract.setVisibility(View.VISIBLE);
            if (this.detailData.getSmartContractList().size() <= 5) {
                this.layoutMoreSmartContract.setVisibility(View.GONE);
            }
            this.smartContractList.addAll(this.detailData.getSmartContractList());
            this.rvSmartContract.getAdapter().notifyDataSetChanged();
        }

        if (this.detailData.getRecommendDappList() != null && this.detailData.getRecommendDappList().size() > 0) {
            recommendDappList.clear();
            this.layoutRecommend.setVisibility(View.VISIBLE);
            this.recommendDappList.addAll(this.detailData.getRecommendDappList());
            this.rvRecommend.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setResourceId() {
        return R.layout.activity_dapp_detail;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case 1: {
                this.detailData = (DappDetailEntity) msg.obj;
                this.bindData();
            }
            break;
            case 2: {
                List<RelevantArticleEntity> result = (List<RelevantArticleEntity>) msg.obj;
                if (result != null && result.size() > 0) {
                    relevantArticleList.clear();
                    this.layoutRelevantArticle.setVisibility(View.VISIBLE);
                    this.relevantArticleList.addAll(result);
                    this.rvRelevantArticle.getAdapter().notifyDataSetChanged();
                }
            }
            break;
            case 3: {
                RatingStatisticsEntity result = (RatingStatisticsEntity) msg.obj;
                this.tvRatingCount.setText(result.getUserTotal());
                this.rbScore.setRating((int) Float.parseFloat(result.getRatingScore()));
                this.tvScore.setText(result.getRatingScore());
            }
            break;
        }
    }

    @Override
    protected void setData() {
        if (this.presenter == null) {
            this.presenter = new DappDetailPresenter(this.getApplicationContext(), this);
        }
        String mid = getIntent().getStringExtra("mid");
        if (mid != null) {
            Map<String, String> params = new HashMap<>();
            params.put("action", "fetchDetailData");
            params.put("cache", "true");
            params.put("mid", mid);
            this.presenter.getData(params);

            Map<String, String> articleParams = new HashMap<>();
            articleParams.put("action", "fetchRelevantArticle");
            articleParams.put("cache", "true");
            articleParams.put("mid", mid);
            this.presenter.getData(articleParams);

            Map<String, String> ratingParams = new HashMap<>();
            ratingParams.put("action", "fetchRatingStatistics");
            ratingParams.put("cache", "true");
            ratingParams.put("mid", mid);
            this.presenter.getData(ratingParams);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "fetchDetailData");
                    params.put("mid", mid);
                    presenter.getData(params);

                    Map<String, String> articleParams = new HashMap<>();
                    articleParams.put("action", "fetchRelevantArticle");
                    articleParams.put("mid", mid);
                    presenter.getData(articleParams);

                    Map<String, String> ratingParams = new HashMap<>();
                    ratingParams.put("action", "fetchRatingStatistics");
                    ratingParams.put("mid", mid);
                    presenter.getData(ratingParams);
                }
            }, 500);
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        this.ivBack = this.findViewById(R.id.ivBack);
        this.tvTitle = this.findViewById(R.id.tvTitle);
        this.layoutShare = this.findViewById(R.id.layoutShare);
        this.ivDappDetail = this.findViewById(R.id.ivDappDetail);
        this.ivIcon = this.findViewById(R.id.ivIcon);
        this.tvDappName = this.findViewById(R.id.tvDappName);
        this.tvSummary = this.findViewById(R.id.tvSummary);
        this.tvPlatform = this.findViewById(R.id.tvPlatform);
        this.tvType = this.findViewById(R.id.tvType);
        this.ivFollow = this.findViewById(R.id.ivFollow);
        this.tvFollowCount = this.findViewById(R.id.tvFollowCount);
        this.tvScore = this.findViewById(R.id.tvScore);
        this.rbScore = this.findViewById(R.id.rbScore);
        this.tvRatingCount = this.findViewById(R.id.tvRatingCount);
        this.btnOpenWebsite = this.findViewById(R.id.btnOpenWebsite);
        this.tvDescription = this.findViewById(R.id.tvDescription);
        this.tvUser24H = this.findViewById(R.id.tvUser24H);
        this.tvUser24HPercentage = this.findViewById(R.id.tvUser24HPercentage);
        this.ivUser24HUp = this.findViewById(R.id.ivUser24HUp);
        this.ivUser24HDown = this.findViewById(R.id.ivUser24HDown);
        this.tvVolume24H = this.findViewById(R.id.tvVolume24H);
        this.tvVolume24HPercentage = this.findViewById(R.id.tvVolume24HPercentage);
        this.ivVolume24HUp = this.findViewById(R.id.ivVolume24HUp);
        this.ivVolume24HDown = this.findViewById(R.id.ivVolume24HDown);
        this.tvTx24H = this.findViewById(R.id.tvTx24H);
        this.tvTx24HPercentage = this.findViewById(R.id.tvTx24HPercentage);
        this.ivTx24HUp = this.findViewById(R.id.ivTx24HUp);
        this.ivTx24HDown = this.findViewById(R.id.ivTx24HDown);
        this.tvVolume7D = this.findViewById(R.id.tvVolume7D);
        this.tvVolume7DPercentage = this.findViewById(R.id.tvVolume7DPercentage);
        this.ivVolume7DUp = this.findViewById(R.id.ivVolume7DUp);
        this.ivVolume7DDown = this.findViewById(R.id.ivVolume7DDown);
        this.tvTx7D = this.findViewById(R.id.tvTx7D);
        this.tvTx7DPercentage = this.findViewById(R.id.tvTx7DPercentage);
        this.ivTx7DUp = this.findViewById(R.id.ivTx7DUp);
        this.ivTx7DDown = this.findViewById(R.id.ivTx7DDown);
        this.rvSocial = this.findViewById(R.id.rvSocial);
        this.btnToggle24H = this.findViewById(R.id.btnToggle24H);
        this.btnToggle7D = this.findViewById(R.id.btnToggle7D);
        this.rvScreenshot = this.findViewById(R.id.rvScreenshot);
        this.layoutMoreSmartContract = this.findViewById(R.id.layoutMoreSmartContract);
        this.btnMoreSmartContract = this.findViewById(R.id.btnMoreSmartContract);
        this.rvSmartContract = this.findViewById(R.id.rvSmartContract);
        this.rvRelevantArticle = this.findViewById(R.id.rvRelevantArticle);
        this.rvRecommend = this.findViewById(R.id.rvRecommend);
        this.layoutSocial = this.findViewById(R.id.layoutSocial);
        this.layoutScreenshot = this.findViewById(R.id.layoutScreenshot);
        this.layoutSmartContract = this.findViewById(R.id.layoutSmartContract);
        this.layoutRelevantArticle = this.findViewById(R.id.layoutRelevantArticle);
        this.layoutRecommend = this.findViewById(R.id.layoutRecommend);
        this.tv_ipfs_hash = this.findViewById(R.id.tv_ipfs_hash);
        this.tvWebsite = this.findViewById(R.id.tvWebsite);
        this.tvWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tvWebsite.getPaint().setAntiAlias(true);

        this.ivBack.setOnClickListener(this);
        this.layoutShare.setOnClickListener(this);
        this.tv_ipfs_hash.setOnClickListener(this);
        this.btnOpenWebsite.setOnClickListener(this);
        this.btnToggle24H.setOnClickListener(this);
        this.btnToggle7D.setOnClickListener(this);
        this.btnMoreSmartContract.setOnClickListener(this);
        this.tvWebsite.setOnClickListener(this);

        GridLayoutManager socialLayoutManager = new GridLayoutManager(this, 5);
        this.rvSocial.setLayoutManager(socialLayoutManager);
        DappSocialListAdapter socialAdapter = new DappSocialListAdapter(this, this.socialList);
        this.rvSocial.setAdapter(socialAdapter);
        socialAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                SocialMediaEntity entity = (SocialMediaEntity) itemData;

                Uri uri = Uri.parse(entity.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        LinearLayoutManager screenshotLayoutManager = new LinearLayoutManager(this);
        screenshotLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.rvScreenshot.setLayoutManager(screenshotLayoutManager);
        this.rvScreenshot.setAdapter(new DappScreenshotListAdapter(this, this.screenshotList));

        LinearLayoutManager smartContractLayoutManager = new LinearLayoutManager(this);
        smartContractLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rvSmartContract.setLayoutManager(smartContractLayoutManager);
        this.rvSmartContract.setAdapter(new DappSmartContractListAdapter(this, this.smartContractList, 5));

        LinearLayoutManager relevantArticleLayoutManager = new LinearLayoutManager(this);
        relevantArticleLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rvRelevantArticle.setLayoutManager(relevantArticleLayoutManager);
        DappRelevantArticleListAdapter relevantArticleAdapter = new DappRelevantArticleListAdapter(this, this.relevantArticleList);
        this.rvRelevantArticle.setAdapter(relevantArticleAdapter);
        relevantArticleAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                RelevantArticleEntity articleEntity = (RelevantArticleEntity) itemData;
                String articleID = articleEntity.getMid();
                String linkURL = "https://dapponline.io/news-detail/" + articleID;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setLink(linkURL);
                RecentHistorySharedPreferences.getInstance(DappDetailActivity.this).pushHistory(entity);

                Uri uri = Uri.parse(linkURL.trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        GridLayoutManager recommendDappLayoutManager = new GridLayoutManager(this, 4);
        this.rvRecommend.setLayoutManager(recommendDappLayoutManager);
        DiscoverySimpleListAdapter recommendAdapter = new DiscoverySimpleListAdapter(this, this.recommendDappList, 8);
        this.rvRecommend.setAdapter(recommendAdapter);
        recommendAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
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
                RecentHistorySharedPreferences.getInstance(DappDetailActivity.this).pushHistory(entity);

                Intent intent = new Intent(DappDetailActivity.this, DappDetailActivity.class);
                intent.putExtra("mid", data.getMid());
                startActivity(intent);
            }
        });

        this.tipsDialog = new SecurityTipsDialog(this, R.style.BottomDialogTheme, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsDialog.dismiss();
                Uri uri = Uri.parse(detailData.getLink().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = type;
        msg.obj = object;
        msg.sendToTarget();
    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }

    @Override
    public void onClick(View v) {
        int resourceID = v.getId();
        switch (resourceID) {
            case R.id.ivBack: {
                this.finish();
            }
            break;
            case R.id.layoutShare: {
                String mid = getIntent().getStringExtra("mid");
                if (mid != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://dapponline.io/dapp-detail/" + mid);
                    shareIntent = Intent.createChooser(shareIntent, getResources().getString(R.string.select_share_type));
                    startActivity(shareIntent);
                }
            }
            break;
            case R.id.btnOpenWebsite: {
//                Intent intent = new Intent(this, CommonWebviewActivity.class);
//                intent.putExtra("data", this.detailData.getLink());
//                startActivity(intent);
                if (this.detailData != null) {
                    this.tipsDialog.show();
                }
            }
            break;
//            case R.id.btnToggle24H:{
//            }break;
//            case R.id.btnToggle7D:{
//            }break;
            case R.id.btnMoreSmartContract: {
                if (this.smartContractList != null && this.smartContractList.size() > 0) {
                    Intent intent = new Intent(this, SmartContractListActivity.class);
                    intent.putExtra("data", (ArrayList<SmartContractEntity>) this.smartContractList);
                    startActivity(intent);
                }
            }
            break;
            case R.id.tvWebsite: {
                Uri uri = Uri.parse("https://dapponline.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            break;
            case R.id.tv_ipfs_hash:
                if (detailData == null) return;
                if (ipfsAddressListDialog == null) {
                    ipfsAddressListDialog = new IPFSAddressListDialog(this, R.style.Wallet_Manager_Dialog, detailData.getIpfs_hash());
                }
                ipfsAddressListDialog.show();
                break;
        }
    }
}