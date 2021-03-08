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
 * Created on 2021/3/8
 * Description:预约选座模块
 */
public class SeatReservationWebViewActivity extends BaseActivity {

    WebView webView;
    // 根据class名称获取div数组
    private final String getClassFun = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
    // 更改特定div的css属性
    private final String hideOtherFun = "javascript:function hideOther() {getClass(document,'qr_code_pc')[0].style.display='none';getClass(document,'meta_content')[0].style.display='none';getClass(document,'rich_media_area_extra')[0].style.display='none';}";

    @Override
    public int getRootLayout() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("预约选座");
        webView = findViewById(R.id.wv_content);
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogHelper.i("SeatReservationActivity", "onPageFinished");
                view.loadUrl(getClassFun);
                view.loadUrl(hideOtherFun);
                view.loadUrl("javascript:hideOther();");
            }
        });
        webView.loadUrl(BizHttpConstants.SeatReservation_URL);
    }
}
