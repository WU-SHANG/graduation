package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/30
 * Description:办理校园网模块
 */
public class CampusNetworkActivity extends BaseWebViewActivity {

    private TextView tvNew, tvOld;

    @Override
    public int getRootLayout() {
        return R.layout.activity_campus_network;
    }

    @Override
    public String getUrl() {
        return BizHttpConstants.CAMPUS_NETWORK_TIP_URL;
    }

    @Override
    public int getWebViewId() {
        return R.id.wv_campus_network;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebSetting() {
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient(){
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
    }

    @Override
    public void initOtherView() {
        super.initOtherView();
        initTitleBar();
        titleBarView.setCenterText("办理校园网");

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
}