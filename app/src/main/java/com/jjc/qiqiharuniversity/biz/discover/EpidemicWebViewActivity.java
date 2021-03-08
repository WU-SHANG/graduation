package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/8
 * Description:疫情数据模块
 */
public class EpidemicWebViewActivity extends BaseActivity {

    WebView webView;

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("实时疫情数据");
        webView = findViewById(R.id.wv_content);
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(BizHttpConstants.TENCENT_EPIDEMIC_DATA_API);
    }
}
