package com.jjc.qiqiharuniversity.common.base;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;

public abstract class BaseWebViewActivity extends BaseActivity {

    public WebView mWebView;
    public NetFailComponent netFailComponent;
    public RelativeLayout rlNetRefresh;
    public LoadingHelper loadingHelper;

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    public abstract String getUrl();

    public int getWebViewId() {
        return R.id.wv_content;
    }

    public void initWebSetting() {

    }

    public void initOtherView() {

    }

    public boolean isClearCache() {
        return false;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mWebView = findViewById(getWebViewId());
        initWebSetting();
        initNetFail();
        initLoading();
        initOtherView();
        mWebView.loadUrl(getUrl());
    }

    private void initNetFail() {
        rlNetRefresh = findViewById(R.id.rl_net_refresh);
        netFailComponent = new NetFailComponent();
        netFailComponent.setRefreshListener(new NetFailFragment.RefreshListener() {
            @Override
            public void refresh() {
                mWebView.loadUrl(getUrl());
                hideNetFail();
            }
        });
        netFailComponent.add(getSupportFragmentManager(), R.id.rl_net_refresh);
    }

    private void initLoading() {
        loadingHelper = new LoadingHelper();
    }

    public void showNetFail() {
        if (rlNetRefresh != null) {
            rlNetRefresh.setVisibility(View.VISIBLE);
        }
    }

    public void hideNetFail() {
        if (rlNetRefresh != null) {
            rlNetRefresh.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        if (loadingHelper != null && !loadingHelper.isShowing()) {
            loadingHelper.show(getSupportFragmentManager());
        }
    }

    public void hideLoading() {
        if (loadingHelper != null) {
            loadingHelper.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (netFailComponent != null) {
            netFailComponent.remove(getSupportFragmentManager());
            netFailComponent = null;
        }
        if (loadingHelper != null) {
            loadingHelper.dismiss();
            loadingHelper = null;
        }
        if (isClearCache()) {
            mWebView.clearCache(true);
        }
    }
}
