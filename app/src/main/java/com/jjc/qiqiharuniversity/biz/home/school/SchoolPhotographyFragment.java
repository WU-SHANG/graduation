package com.jjc.qiqiharuniversity.biz.home.school;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewFragment;
import com.jjc.qiqiharuniversity.common.component.NetFailComponent;
import com.jjc.qiqiharuniversity.common.component.NetFailFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/1
 * Description:摄影天地模块
 */
public class SchoolPhotographyFragment extends BaseWebViewFragment {

    // 根据class名称获取div数组
    private final String getClassFun = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
    // 更改特定div的css属性
    private final String hideOtherFun = "javascript:function hideOther() {" +
            "getClass(document,'header')[0].style.display='none';" +
            "getClass(document,'ny_dqwz')[0].style.display='none';" +
            "getClass(document,'footer')[0].style.display='none';}";

    @Override
    public String getUrl() {
        return BizHttpConstants.SCHOOL_SYTD_URL;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebSetting() {
        mWebView.getSettings().setJavaScriptEnabled(true);// 设置支持javascript脚本
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogHelper.i("SchoolCultureFragment", "onPageFinished");
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
}
