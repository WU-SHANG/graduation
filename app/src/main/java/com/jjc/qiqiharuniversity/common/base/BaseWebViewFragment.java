package com.jjc.qiqiharuniversity.common.base;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

public abstract class BaseWebViewFragment extends BaseFragment {

    public LinearLayout llTitleBar;
    public WebView mWebView;
    public NetFailComponent netFailComponent;
    public RelativeLayout rlNetRefresh;
    public LoadingHelper loadingHelper;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_school;
    }

    public abstract String getUrl();

    public void initWebSetting() {

    }

    public void initOtherView() {

    }

    public boolean isClearCache() {
        return false;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mWebView = view.findViewById(R.id.wv_school);
        initWebSetting();
        initSchoolTitleBar(view);
        initNetFail(view);
        initLoading();
        initOtherView();
        mWebView.loadUrl(getUrl());
    }

    private void initSchoolTitleBar(View view) {
        llTitleBar = view.findViewById(R.id.ll_school_title_bar);
        llTitleBar.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(llTitleBar.getId())) {
                return;
            }
            showLoading();
            mWebView.loadUrl(getUrl());
        });
    }

    private void initNetFail(View view) {
        rlNetRefresh = view.findViewById(R.id.rl_net_refresh);
        netFailComponent = new NetFailComponent();
        netFailComponent.setRefreshListener(new NetFailFragment.RefreshListener() {
            @Override
            public void refresh() {
                showLoading();
                mWebView.loadUrl(getUrl());
                hideNetFail();
            }
        });
        netFailComponent.add(getChildFragmentManager(), R.id.rl_net_refresh);
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
            loadingHelper.show(getChildFragmentManager());
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
            netFailComponent.remove(getChildFragmentManager());
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
