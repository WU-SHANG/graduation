package com.jjc.qiqiharuniversity.activity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjc.qiqiharuniversity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:新闻item的详情
 */

public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.wv_details)
    WebView wvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String url = getIntent().getStringExtra("url");
        wvDetails.loadUrl(url);
    }
}
