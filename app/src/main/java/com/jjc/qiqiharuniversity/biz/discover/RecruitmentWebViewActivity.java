package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Author jiajingchao
 * Created on 2021/3/3
 * Description:就业讯息详情页
 */
public class RecruitmentWebViewActivity extends BaseActivity {

    private static final String ENTRANCE = "entrance";
    private static final int ENTRANCE_DOUBLE_CHOOSE = 1;
    private static final int ENTRANCE_PREACH = 2;

    WebView webView;
    private NetFailComponent netFailComponent;
    private RelativeLayout rlNetRefresh;
    private LoadingHelper loadingHelper;
    private int entrance;

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    public static void start(Context context, int entrance) {
        Intent intent = new Intent(context, RecruitmentWebViewActivity.class);
        intent.putExtra(ENTRANCE, entrance);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        webView = findViewById(R.id.wv_content);
        rlNetRefresh = findViewById(R.id.rl_net_refresh);
        initNetFail();
        loadingHelper = new LoadingHelper();
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
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

        entrance = getIntent().getIntExtra(ENTRANCE, 0);
        if (entrance == ENTRANCE_DOUBLE_CHOOSE) {
            titleBarView.setCenterText("双选会讯息");
            webView.loadUrl(BizHttpConstants.RECRUITMENT_DOUBLE_CHOOSE_URL);
        } else if (entrance == ENTRANCE_PREACH) {
            titleBarView.setCenterText("宣讲会讯息");
            webView.loadUrl(BizHttpConstants.RECRUITMENT_PREACH_URL);
        } else {
            ToastManager.show(this, "页面出错");
        }

    }

    private void initNetFail() {
        netFailComponent = new NetFailComponent();
        netFailComponent.setRefreshListener(new NetFailFragment.RefreshListener() {
            @Override
            public void refresh() {
                if (entrance == ENTRANCE_DOUBLE_CHOOSE) {
                    webView.loadUrl(BizHttpConstants.RECRUITMENT_DOUBLE_CHOOSE_URL);
                } else if (entrance == ENTRANCE_PREACH) {
                    webView.loadUrl(BizHttpConstants.RECRUITMENT_PREACH_URL);
                } else {
                    ToastManager.show(RecruitmentWebViewActivity.this, "页面出错");
                }
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
}