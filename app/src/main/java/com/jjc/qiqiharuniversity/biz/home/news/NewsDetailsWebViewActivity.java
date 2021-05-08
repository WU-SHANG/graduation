package com.jjc.qiqiharuniversity.biz.home.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewActivity;


/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:新闻item的详情
 */

public class NewsDetailsWebViewActivity extends BaseWebViewActivity {

    private static final String KEY_URL = "url";

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public String getUrl() {
        return getIntent().getStringExtra(KEY_URL);
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, NewsDetailsWebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void initWebSetting() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
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
    }

    @Override
    public void initOtherView() {
        super.initOtherView();
        initTitleBar();
        titleBarView.setCenterText("新闻快讯");
    }
}
