package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/28
 * Description:校园地图模块
 */
public class SchoolMapWebViewActivity extends BaseActivity {

    WebView webView;
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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("校园全景图");
        webView = findViewById(R.id.wv_content);
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogHelper.i("SchoolMapWebViewActivity", "onPageFinished");
                view.loadUrl(getClassFun);
                view.loadUrl(hideOtherFun);
                view.loadUrl("javascript:hideOther();");
            }
        });
        webView.loadUrl(BizHttpConstants.Map_URL);
    }

    @Override
    public void onBackPressed() {
        webView.goBack();
    }
}