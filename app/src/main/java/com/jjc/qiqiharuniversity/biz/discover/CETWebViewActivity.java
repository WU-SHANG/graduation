package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/3
 * Description:四六级查询模块
 */
public class CETWebViewActivity extends BaseActivity {

    WebView webView;

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("四六级查询");
        ToastManager.show(CETWebViewActivity.this, "即将跳转到浏览器");
        webView = findViewById(R.id.wv_content);
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogHelper.i("CETWebViewActivity", "url == " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();// 接受所有网站的证书
            }
        });

        webView.loadUrl(BizHttpConstants.CET_URL);
    }

}