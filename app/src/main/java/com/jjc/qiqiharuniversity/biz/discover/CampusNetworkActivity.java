package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/30
 * Description:办理校园网模块
 */
public class CampusNetworkActivity extends BaseActivity {

    private TextView tvNew, tvOld;
    WebView webView;
    private NetFailComponent netFailComponent;
    private RelativeLayout rlNetRefresh;

    @Override
    public int getRootLayout() {
        return R.layout.activity_campus_network;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("办理校园网");
        webView = findViewById(R.id.wv_campus_network);
        rlNetRefresh = findViewById(R.id.rl_net_refresh);
        initNetFail();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient(){
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
        webView.loadUrl(BizHttpConstants.CAMPUS_NETWORK_TIP_URL);

        tvNew = findViewById(R.id.tv_new);
        tvOld = findViewById(R.id.tv_old);
        tvNew.setOnClickListener(v -> {
            Uri uri = Uri.parse(BizHttpConstants.CAMPUS_NETWORK_NEW_URL);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        });
        tvOld.setOnClickListener(v -> {
            Uri uri = Uri.parse(BizHttpConstants.CAMPUS_NETWORK_OLD_URL);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        });
    }

    private void initNetFail() {
        netFailComponent = new NetFailComponent();
        netFailComponent.setRefreshListener(new NetFailFragment.RefreshListener() {
            @Override
            public void refresh() {
                webView.loadUrl(BizHttpConstants.CAMPUS_NETWORK_TIP_URL);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (netFailComponent != null) {
            netFailComponent.remove(getSupportFragmentManager());
            netFailComponent = null;
        }
    }

}