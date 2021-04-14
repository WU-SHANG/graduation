package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Author jiajingchao
 * Created on 2021/4/12
 * Description:齐大微博
 */
public class WeiboWebViewActivity extends BaseActivity {

    WebView webView;
    private NetFailComponent netFailComponent;
    private RelativeLayout rlNetRefresh;
    private LoadingHelper loadingHelper;

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("齐大微博");
        webView = findViewById(R.id.wv_content);
        rlNetRefresh = findViewById(R.id.rl_net_refresh);
        initNetFail();
        loadingHelper = new LoadingHelper();
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.getSettings().setDomStorageEnabled(true);//开启DOM
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingHelper.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    view.loadUrl("about:blank"); // 避免出现默认的错误界面
                    showNetFail();
                }
            }
        });
        webView.loadUrl(BizHttpConstants.WEIBO_URL);
    }

    private void initNetFail() {
        netFailComponent = new NetFailComponent();
        netFailComponent.setRefreshListener(new NetFailFragment.RefreshListener() {
            @Override
            public void refresh() {
                webView.loadUrl(BizHttpConstants.WEIBO_URL);
                hideNetFail();
            }
        });
        netFailComponent.add(getSupportFragmentManager(), R.id.rl_net_refresh);
    }

    private void showNetFail() {
        if (rlNetRefresh != null) {
            rlNetRefresh.setVisibility(View.VISIBLE);
        }
    }

    private void hideNetFail() {
        if (rlNetRefresh != null) {
            rlNetRefresh.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        if (loadingHelper != null && !loadingHelper.isShowing()) {
            loadingHelper.show(getSupportFragmentManager());
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
    }

    @Override
    public void onBackPressed() {
        webView.goBack();
    }
}