package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/4/12
 * Description:教资查询
 */
public class TeacherQualificationWebViewActivity extends BaseWebViewActivity {

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public String getUrl() {
        return BizHttpConstants.TEACHER_QUALIFICATION_URL;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebSetting() {
        mWebView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        mWebView.getSettings().setDomStorageEnabled(true);//开启DOM
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebChromeClient(new WebChromeClient());
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
        titleBarView.setCenterText("教资查询");
    }
}