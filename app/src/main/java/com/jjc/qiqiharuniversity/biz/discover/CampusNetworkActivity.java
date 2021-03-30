package com.jjc.qiqiharuniversity.biz.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;

/**
 * Author jiajingchao
 * Created on 2021/3/30
 * Description:办理校园网模块
 */
public class CampusNetworkActivity extends BaseActivity {

    private TextView tvNew, tvOld;
    WebView webView;

    @Override
    public int getRootLayout() {
        return R.layout.activity_campus_network;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("办理校园网");

        webView = findViewById(R.id.wv_campus_network);
//        WebSettings settings = webView.getSettings();
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setDomStorageEnabled(true);
//        settings.setDefaultTextEncodingName("UTF-8");
//        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
//        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
//        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
//        settings.setAllowFileAccessFromFileURLs(false);
//        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
//        settings.setAllowUniversalAccessFromFileURLs(false);
//        //开启JavaScript支持
//        settings.setJavaScriptEnabled(true);
//        // 支持缩放
//        settings.setSupportZoom(true);
//        settings.setDomStorageEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(BizHttpConstants.CAMPUS_NETWORK_TIP_URL);

        tvNew = findViewById(R.id.tv_new);
        tvOld = findViewById(R.id.tv_old);
        tvNew.setOnClickListener(v -> {
            Uri uri = Uri.parse(BizHttpConstants.CAMPUS_NETWORK_NEW_URL);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        });
        tvOld.setOnClickListener(v -> {
            Uri uri = Uri.parse(BizHttpConstants.CAMPUS_NETWORK_OLD_URL);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
        });
    }

}