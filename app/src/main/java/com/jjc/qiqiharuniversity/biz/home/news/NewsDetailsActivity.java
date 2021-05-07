package com.jjc.qiqiharuniversity.biz.home.news;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseWebViewActivity;


/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:新闻item的详情
 */

public class NewsDetailsActivity extends BaseWebViewActivity {

    WebView wvDetails;

    @Override
    public int getRootLayout() {
        return R.layout.activity_news_details;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("新闻快讯");
        wvDetails = findViewById(R.id.wv_news_details);
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            wvDetails.loadUrl(url);
        } else {
            ToastManager.show(this, "无法访问");
        }
    }
}
