package com.jjc.qiqiharuniversity.biz.me;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

/**
 * Author jiajingchao
 * Created on 2021/3/27
 * Description:关于我们
 */
public class AboutActivity extends BaseActivity {

    @Override
    public int getRootLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("关于我们");
    }
}
