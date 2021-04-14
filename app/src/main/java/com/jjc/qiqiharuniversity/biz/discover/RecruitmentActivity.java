package com.jjc.qiqiharuniversity.biz.discover;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

/**
 * Author jiajingchao
 * Created on 2021/4/12
 * Description:就业讯息入口界面
 */
public class RecruitmentActivity extends BaseActivity {

    private static final int ENTRANCE_DOUBLE_CHOOSE = 1;
    private static final int ENTRANCE_PREACH = 2;
    private TextView tvDoubleChoose, tvPreach;

    @Override
    public int getRootLayout() {
        return R.layout.activity_recruitment;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("就业讯息");
        tvDoubleChoose = findViewById(R.id.tv_double_choose);
        tvPreach = findViewById(R.id.tv_preach);
        tvDoubleChoose.setOnClickListener(view -> {
            RecruitmentWebViewActivity.start(RecruitmentActivity.this, ENTRANCE_DOUBLE_CHOOSE);
        });
        tvPreach.setOnClickListener(view -> {
            RecruitmentWebViewActivity.start(RecruitmentActivity.this, ENTRANCE_PREACH);
        });
    }
}
