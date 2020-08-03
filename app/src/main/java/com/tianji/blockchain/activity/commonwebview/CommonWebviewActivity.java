package com.tianji.blockchain.activity.commonwebview;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CommonWebviewActivity extends BasicConnectShowActivity {
    private WebView webView;

    private String title;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("_title");
        url = getIntent().getStringExtra("_url");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(title);
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        webView = findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(url);
    }
}
