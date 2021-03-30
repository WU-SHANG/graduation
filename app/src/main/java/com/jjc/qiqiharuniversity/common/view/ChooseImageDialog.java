package com.jjc.qiqiharuniversity.common.view;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jjc.qiqiharuniversity.R;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:选择相册图片的底部弹窗
 */
public class ChooseImageDialog extends AnimDialogFragment {

    private TextView tvTop, tvCancel;
    private String topText;
    private onChooseListener listener;

    @Override
    public int getRootLayout() {
        return R.layout.layout_choose_image_dialog;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getDialogAnimationRes() {
        return R.style.animate_dialog;
    }

    @Override
    protected void initView(View view) {
        tvTop = view.findViewById(R.id.tv_top);
        tvCancel = view.findViewById(R.id.tv_cancel);
        if (!TextUtils.isEmpty(topText)) {
            tvTop.setText(topText);
        }

        tvTop.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTopClick();
            }
            dismissAllowingStateLoss();
        });


        tvCancel.setOnClickListener(v -> dismissAllowingStateLoss());
    }

    public void setText(String topText) {
        this.topText = topText;
    }

    public void setOnChooseListener(onChooseListener listener) {
        this.listener = listener;
    }

    public interface onChooseListener {
        void onTopClick();
    }
}
