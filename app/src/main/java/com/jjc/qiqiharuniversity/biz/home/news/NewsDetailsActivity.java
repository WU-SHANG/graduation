package com.jjc.qiqiharuniversity.biz.home.news;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjc.qiqiharuniversity.R;


/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:新闻item的详情
 */

public class NewsDetailsActivity extends AppCompatActivity {

    WebView wvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        setContentView(R.layout.activity_news_details);
        wvDetails = findViewById(R.id.wv_news_details);
        initView();
    }

    private void initView() {
        String url = getIntent().getStringExtra("url");
        wvDetails.loadUrl(url);
    }
}
