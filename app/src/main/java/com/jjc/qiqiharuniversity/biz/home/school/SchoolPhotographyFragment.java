package com.jjc.qiqiharuniversity.biz.home.school;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/1
 * Description:摄影天地模块
 */
public class SchoolPhotographyFragment extends BaseFragment {

    WebView webView;
    LinearLayout llTitleBar;
    // 根据class名称获取div数组
    private final String getClassFun = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
    // 更改特定div的css属性
    private final String hideOtherFun = "javascript:function hideOther() {getClass(document,'header')[0].style.display='none';getClass(document,'ny_dqwz')[0].style.display='none';getClass(document,'footer')[0].style.display='none';}";

    @Override
    public int getRootLayout() {
        return R.layout.fragment_school;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        webView = view.findViewById(R.id.wv_school);
        llTitleBar = view.findViewById(R.id.ll_school_title_bar);
        webView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogHelper.i("SchoolPhotographyFragment", "onPageFinished");
                view.loadUrl(getClassFun);
                view.loadUrl(hideOtherFun);
                view.loadUrl("javascript:hideOther();");
            }
        });
        webView.loadUrl(BizHttpConstants.SCHOOL_SYTD_URL);
        // 刷新
        llTitleBar.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(llTitleBar.getId())) {
                return;
            }
            webView.loadUrl(BizHttpConstants.SCHOOL_SYTD_URL);
        });
    }
}
