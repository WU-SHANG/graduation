package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/28
 * Description:校园地图模块
 */
public class SchoolMapWebViewActivity extends BaseWebViewActivity {

    // 根据class名称获取div数组
    private final String getClassFun = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
    // 更改特定div的css属性
    private final String hideOtherFun = "javascript:function hideOther() {getClass(document,'header_warp')[0].style.display='none';" +
            "getClass(document,'qjjxCurrent')[0].style.display='none';" +
            "getClass(document,'qjjxDesc')[0].style.display='none';" +
            "getClass(document,'qjjxConR fr')[0].style.display='none';" +
            "getClass(document,'qjjxCon03')[0].style.display='none';" +
            "getClass(document,'footer')[0].style.display='none';}";

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public String getUrl() {
        return BizHttpConstants.MAP_URL;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebSetting() {
        mWebView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogHelper.i("SchoolMapWebViewActivity", "onPageFinished");
                view.loadUrl(getClassFun);
                view.loadUrl(hideOtherFun);
                view.loadUrl("javascript:hideOther();");
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
        titleBarView.setCenterText("校园全景图");
    }

    @Override
    public void onBackPressed() {
        mWebView.goBack();
    }
}