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
 * Description:综合网站模块
 */
public class SchoolWebAPNFragment extends BaseFragment {

    WebView webView;
    LinearLayout llTitleBar;

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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        webView.loadUrl(BizHttpConstants.SCHOOL_ZHWZ_URL);
        // 刷新
        llTitleBar.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(llTitleBar.getId())) {
                return;
            }
            webView.loadUrl(BizHttpConstants.SCHOOL_ZHWZ_URL);
        });
    }
}
