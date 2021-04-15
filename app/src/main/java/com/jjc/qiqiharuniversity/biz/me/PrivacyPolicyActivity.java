package com.jjc.qiqiharuniversity.biz.me;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class PrivacyPolicyActivity extends BaseActivity {

    private TextView tvPrivacyPolicy;

    @Override
    public int getRootLayout() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("隐私政策");
        tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);

        InputStream input = getResources().openRawResource(R.raw.privacy_policy);	// 读取资源ID
        Scanner scan = new Scanner(input);
        StringBuffer buf = new StringBuffer();
        while (scan.hasNext()) {
            buf.append(scan.next()).append("\n");
        }
        scan.close();
        try {// 关闭输入流
            input.close() ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvPrivacyPolicy.setText(Html.fromHtml(buf.toString()));
    }
}
