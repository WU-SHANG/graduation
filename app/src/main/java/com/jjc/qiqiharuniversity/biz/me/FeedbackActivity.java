package com.jjc.qiqiharuniversity.biz.me;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

public class FeedbackActivity extends BaseActivity {

    private EditText etTop, etAdvice;
    private TextView tvSend;
    private ImageView ivMask;
    private boolean isClickable;//发送按钮是否可以点击

    @Override
    public int getRootLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("意见反馈");
        etTop = findViewById(R.id.et_feedback_top);
        etAdvice = findViewById(R.id.et_feedback_advice);
        tvSend = findViewById(R.id.btn_feedback_send);
        ivMask = findViewById(R.id.iv_feedback_mask);

        etTop.addTextChangedListener(mTextWatcher);
        etAdvice.addTextChangedListener(mTextWatcher);

        tvSend.setOnClickListener(v -> {
            if (isClickable) {
                ToastManager.show(this, "发送成功");
                finish();
            } else {
                ToastManager.show(this, "请输入相关内容");
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(etTop.getText().toString().trim()) || TextUtils.isEmpty(etAdvice.getText().toString().trim())) {
                isClickable = false;
                tvSend.setTextColor(getColor(R.color.light_gray));
                ivMask.setVisibility(View.VISIBLE);
            } else {
                isClickable = true;
                tvSend.setTextColor(getColor(R.color.white));
                ivMask.setVisibility(View.GONE);
            }
        }
    };
}
