package com.tianji.blockchain.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicNetCheckActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.dialog.ShowContentDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.dialog.UpDateDialog;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.fragment.article.ArticleFragment;
import com.tianji.blockchain.fragment.find.FindFragment;
import com.tianji.blockchain.fragment.main.MainFragment;
import com.tianji.blockchain.fragment.wallet.WalletFragment;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.DialogUtils;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BasicNetCheckActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final int DEFAULT_WALLET = 0;

    private static final int TYPE_PASSWORD_ERR = 2;

    private static final int CHECK_WALLET = 4;


    private WalletInfo walletInfo;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager view_pager;


    private RelativeLayout rl_tabbar_wallet, rl_tabbar_article, rl_tabbar_find, rl_tabbar_main;
    private ImageView img_tabbar_wallet, img_tabbar_article, img_tabbar_find, img_tabbar_main;
    private TextView tv_tabbar_wallet, tv_tabbar_article, tv_tabbar_find, tv_tabbar_main;
    private FragmentManager fragmentManager;
    private Fragment walletFragment, findFragment, mainFragment, articleFragment;


    private Gson gson;

    private UpDateDialog dateDialog;


    /***激活流程相关提示***/
    private ShowContentDialog illegalSerialDialog;
    private String serialNumberRecord;
    private TipsDialog initNoNetWorkDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWritePermission();
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void setData() {
        if (presenter == null) presenter = new MainPresenter(this, this);
    }

    @Override
    protected void handleMsg(Message msg) {
        swipeRefreshLayout.setRefreshing(false);
        switch (msg.what) {
            case DEFAULT_WALLET:
                WalletInfo info = (WalletInfo) msg.obj;
                LogUtils.log(className + " -- 发出获取到钱包的广播 DEFAULT_WALLET");
                Intent startTypeIntent = new Intent(IntentFilterConstant.MAINACTIVITY_START_TYPE);
                if (WalletApplication.getCurrentWallet() != null) {
                    startTypeIntent.putExtra("_walletInfo", WalletApplication.getCurrentWallet());
                } else {
                    startTypeIntent.putExtra("_walletInfo", info);
                }
                sendBroadcast(startTypeIntent);
                break;

            case TYPE_PASSWORD_ERR:
                showToast(R.string.password_wrong);
                swipeRefreshLayout.setRefreshing(false);
                break;

            case CHECK_WALLET:
                WalletInfo walletInfo = (WalletInfo) msg.obj;
                LogUtils.log(className + " -- 发出获取到钱包的广播 DEFAULT_WALLET");
                Intent checkTypeIntent = new Intent(IntentFilterConstant.MAINACTIVITY_START_TYPE);
                if (WalletApplication.getCurrentWallet() != null) {
                    checkTypeIntent.putExtra("_walletInfo", WalletApplication.getCurrentWallet());
                } else {
                    checkTypeIntent.putExtra("_walletInfo", walletInfo);
                }
                sendBroadcast(checkTypeIntent);
                //为了切换钱包能跳转到首页
                if (view_pager != null) {
                    view_pager.setCurrentItem(0);
                }
                break;
        }
    }

    @Override
    protected void initReceiver() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        LogUtils.log(className + " -- 首页获取到的钱包类型 == " + walletInfo.getStorageSaveType());
        fragmentManager = this.getSupportFragmentManager();

        rl_tabbar_wallet = findViewById(R.id.rl_tabbar_wallet);
        rl_tabbar_article = findViewById(R.id.rl_tabbar_article);
        rl_tabbar_find = findViewById(R.id.rl_tabbar_find);
        rl_tabbar_main = findViewById(R.id.rl_tabbar_main);
        view_pager = findViewById(R.id.view_pager);

        rl_tabbar_wallet.setOnClickListener(this);
        rl_tabbar_article.setOnClickListener(this);
        rl_tabbar_find.setOnClickListener(this);
        rl_tabbar_main.setOnClickListener(this);

        img_tabbar_wallet = findViewById(R.id.img_tabbar_wallet);
        img_tabbar_article = findViewById(R.id.img_tabbar_article);
        img_tabbar_find = findViewById(R.id.img_tabbar_find);
        img_tabbar_main = findViewById(R.id.img_tabbar_main);
        tv_tabbar_wallet = findViewById(R.id.tv_tabbar_wallet);
        tv_tabbar_article = findViewById(R.id.tv_tabbar_article);
        tv_tabbar_find = findViewById(R.id.tv_tabbar_find);
        tv_tabbar_main = findViewById(R.id.tv_tabbar_main);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);

        gson = new Gson();

        if (walletFragment == null) {
            walletFragment = new WalletFragment();
        }
        if (findFragment == null) {
            findFragment = new FindFragment();
        }
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }

        if (articleFragment == null) {
            articleFragment = new ArticleFragment();
        }
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(walletFragment);
        LogUtils.log(className + " -- 当前语言是 = " + WalletApplication.lang);
        if (WalletApplication.lang.equals("")) {
            rl_tabbar_article.setVisibility(View.VISIBLE);
            fragmentList.add(articleFragment);
        }
        fragmentList.add(findFragment);
        fragmentList.add(mainFragment);

        view_pager.setAdapter(new SectionsPagerAdapter(fragmentManager, fragmentList));
        view_pager.addOnPageChangeListener(this);
        view_pager.setOffscreenPageLimit(3);

        tv_tabbar_wallet.setTextColor(getResources().getColor(R.color.text_selected));

        initNoNetWorkDialog = DialogUtils.buildHardwareNoNetworkDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initNoNetWorkDialog.isShowing()) {
                    initNoNetWorkDialog.dismiss();
                }
            }
        });
        //首页检测自更新，只检测一次
        if (!WalletApplication.isCheckUpdata()) {
            if (WalletApplication.getApp().isDeveloperVersion()) {
                HttpVolley.getInstance(this).HttpVolleyGet(Constant.HttpUrl.updateUrl + "?flag=1", new HttpVolley.VolleyCallBack() {
                    @Override
                    public void onSuccess(String data) {
                        LogUtils.log(className + " -- 灰度测试版更新请求数据 =" + data);
                        UpdateEntity entity = gson.fromJson(data, UpdateEntity.class);
                        WalletApplication.setCheckUpdata(true);

                        long code = CommonUtils.getAppVersionCode(MainActivity.this);
                        if (code < entity.getVersionCode()) {
                            dateDialog = new UpDateDialog(MainActivity.this, entity);
                            dateDialog.show();
                        }
                    }

                    @Override
                    public void onFail(VolleyError error) {

                    }
                });
            } else {
                HttpVolley.getInstance(this).HttpVolleyGet(Constant.HttpUrl.updateUrl, new HttpVolley.VolleyCallBack() {
                    @Override
                    public void onSuccess(String data) {
                        UpdateEntity entity = gson.fromJson(data, UpdateEntity.class);
                        WalletApplication.setCheckUpdata(true);

                        long code = CommonUtils.getAppVersionCode(MainActivity.this);
                        if (code < entity.getVersionCode()) {
                            dateDialog = new UpDateDialog(MainActivity.this, entity);
                            dateDialog.show();
                        }
                    }

                    @Override
                    public void onFail(VolleyError error) {

                    }
                });
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.log(className + " -- onResume chain" + walletInfo.getChain());
        Message message = Message.obtain();
        message.what = DEFAULT_WALLET;
        message.obj = walletInfo;
        mHandler.sendMessage(message);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tabbar_wallet:
                view_pager.setCurrentItem(0);
                setBottomImg(R.id.img_tabbar_wallet);
                break;
            case R.id.rl_tabbar_article:
                if (WalletApplication.lang.equals("")) {
                    view_pager.setCurrentItem(1);
                    setBottomImg(R.id.img_tabbar_article);
                }
                break;
            case R.id.rl_tabbar_find:
                if (WalletApplication.lang.equals("")) {
                    view_pager.setCurrentItem(2);
                    setBottomImg(R.id.img_tabbar_find);
                } else {
                    view_pager.setCurrentItem(1);
                    setBottomImg(R.id.img_tabbar_find);
                }
                break;
            case R.id.rl_tabbar_main:
                if (WalletApplication.lang.equals("")) {
                    view_pager.setCurrentItem(3);
                    setBottomImg(R.id.img_tabbar_main);
                } else {
                    view_pager.setCurrentItem(2);
                    setBottomImg(R.id.img_tabbar_main);
                }
                break;
        }
    }


    /**
     * 修改底部tabbar的图片文字颜色
     *
     * @param id
     */
    private void setBottomImg(int id) {
        if (id == R.id.img_tabbar_wallet) {
            img_tabbar_wallet.setImageResource(R.drawable.wallet_selected);
            img_tabbar_article.setImageResource(R.drawable.icon_article_normal);
            img_tabbar_find.setImageResource(R.drawable.find_normal);
            img_tabbar_main.setImageResource(R.drawable.main_normal);
            tv_tabbar_wallet.setTextColor(getResources().getColor(R.color.text_selected));
            tv_tabbar_article.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_find.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_main.setTextColor(getResources().getColor(R.color.text_normal));
        } else if (id == R.id.img_tabbar_find) {
            img_tabbar_wallet.setImageResource(R.drawable.wallet_normal);
            img_tabbar_article.setImageResource(R.drawable.icon_article_normal);
            img_tabbar_find.setImageResource(R.drawable.find_selected);
            img_tabbar_main.setImageResource(R.drawable.main_normal);
            tv_tabbar_wallet.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_article.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_find.setTextColor(getResources().getColor(R.color.text_selected));
            tv_tabbar_main.setTextColor(getResources().getColor(R.color.text_normal));
        } else if (id == R.id.img_tabbar_article) {
            img_tabbar_wallet.setImageResource(R.drawable.wallet_normal);
            img_tabbar_article.setImageResource(R.drawable.icon_article_selected);
            img_tabbar_find.setImageResource(R.drawable.find_normal);
            img_tabbar_main.setImageResource(R.drawable.main_normal);
            tv_tabbar_wallet.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_article.setTextColor(getResources().getColor(R.color.text_selected));
            tv_tabbar_find.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_main.setTextColor(getResources().getColor(R.color.text_normal));
        } else if (id == R.id.img_tabbar_main) {
            img_tabbar_wallet.setImageResource(R.drawable.wallet_normal);
            img_tabbar_article.setImageResource(R.drawable.icon_article_normal);
            img_tabbar_find.setImageResource(R.drawable.find_normal);
            img_tabbar_main.setImageResource(R.drawable.main_selected);
            tv_tabbar_wallet.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_article.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_find.setTextColor(getResources().getColor(R.color.text_normal));
            tv_tabbar_main.setTextColor(getResources().getColor(R.color.text_selected));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (view_pager.getCurrentItem() == 2) {
            LogUtils.log(className + " -- 发现页面扫码");
            this.findFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            LogUtils.log(className + " -- 资产界面扫码");
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        LogUtils.log("首页扫描内容" + bundle.getString("result"));
                        String result = bundle.getString("result");
                        Intent intent = new Intent(IntentFilterConstant.MAIN_ACTIVITY_GET_QRCODE_RESULT);
                        intent.putExtra("_qrcodeResult", result);
                        sendBroadcast(intent);
                    }
                    break;
            }
        }
    }

    /***viewPage切换监听***/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setBottomImg(R.id.img_tabbar_wallet);
                break;
            case 1:
                if (WalletApplication.lang.equals("")) {
                    setBottomImg(R.id.img_tabbar_article);
                } else {
                    setBottomImg(R.id.img_tabbar_find);
                }

                break;
            case 2:
                if (WalletApplication.lang.equals("")) {
                    setBottomImg(R.id.img_tabbar_find);
                } else {
                    setBottomImg(R.id.img_tabbar_main);
                }

                break;
            case 3:
                if (WalletApplication.lang.equals("")) {
                    setBottomImg(R.id.img_tabbar_main);
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onNetChange(NetUtils.NetworkStatus status) {
        if (!WalletApplication.getApp().isNetWorkFirstShowed()) {
            if (status == NetUtils.NetworkStatus.NETWORK_NONE) {
                tipsDialog.show();
                WalletApplication.getApp().setNetWorkFirstShowed(true);
            }
        }
    }

    /***viewPage 适配器***/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public SectionsPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void requestWritePermission() {
        if (!selfPermissionGranted(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INSTALL_PACKAGES};
            ActivityCompat.requestPermissions(this, permission, Constant.PermissionsCode.WRITE_PERMISSION);
        }
    }

    public static boolean selfPermissionGranted(Context context, String permission) {
        int targetSdkVersion = 0;
        boolean ret;

        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
            LogUtils.i("selfPermissionGranted targetSdkVersion=" + targetSdkVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                ret = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                ret = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        } else {
            return true;
        }
        LogUtils.i("selfPermissionGranted permission:" + permission + " grant:" + ret);
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PermissionsCode.CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                } else {
                    LogUtils.log("相机权限申请失败");
                }
                break;
        }
    }


}
