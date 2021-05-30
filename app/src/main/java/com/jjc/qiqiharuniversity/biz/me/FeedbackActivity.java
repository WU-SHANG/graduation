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
import com.jjc.qiqiharuniversity.biz.me.model.Feedback;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends BaseActivity {

    private static final String TAG = FeedbackActivity.class.getSimpleName();
    private final List<String> typeList = Arrays.asList("体验", "BUG", "投诉", "其他");

    private NiceSpinner niceSpinner;
    private EditText etAdvice;
    private TextView tvSend;
    private ImageView ivMask;
    private String mType = "体验";

    @Override
    public int getRootLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("意见反馈");
        etAdvice = findViewById(R.id.et_feedback_advice);
        tvSend = findViewById(R.id.btn_feedback_send);
        ivMask = findViewById(R.id.iv_feedback_mask);

        niceSpinner = findViewById(R.id.ns_feedback);
        niceSpinner.attachDataSource(typeList);
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                mType = (String) parent.getItemAtPosition(position);
            }
        });

        etAdvice.addTextChangedListener(mTextWatcher);

        tvSend.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvSend.getId())) {
                return;
            }
            if (TextUtils.isEmpty(etAdvice.getText().toString().trim())) {
                ToastManager.show(this, "内容不能为空");
                return;
            }
            sendFeedback();
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
            if (TextUtils.isEmpty(etAdvice.getText().toString().trim())) {
                tvSend.setTextColor(getColor(R.color.gray_cdcdcd));
                ivMask.setVisibility(View.VISIBLE);
            } else {
                tvSend.setTextColor(getColor(R.color.white));
                ivMask.setVisibility(View.GONE);
            }
        }
    };

    private void sendFeedback() {
        Feedback feedback = new Feedback();
        feedback.setType(mType);
        feedback.setContent(etAdvice.getText().toString().trim());
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    ToastManager.show(FeedbackActivity.this, "发送成功");
                    finish();
                } else {
                    ToastManager.show(FeedbackActivity.this, "发送失败；" + e.getErrorCode());
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });
    }
}
