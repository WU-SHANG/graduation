package com.jjc.qiqiharuniversity.common.view;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjc.qiqiharuniversity.R;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:选择性别的底部弹窗
 */
public class ChooseGenderDialog extends AnimDialogFragment {
    private LinearLayout llMale, llFemale;
    private onChooseListener listener;
    private TextView tvExit;

    @Override
    protected int getRootLayout() {
        return R.layout.layout_choose_gender_dialog;
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
        llMale = view.findViewById(R.id.ll_info_male);
        llFemale = view.findViewById(R.id.ll_info_female);
        tvExit = view.findViewById(R.id.tv_exit);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });


        llMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onMaleClick();
                }
                dismissAllowingStateLoss();
            }
        });
        llFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFemaleClick();
                }
                dismissAllowingStateLoss();
            }
        });


    }

    public void setOnChooseListener(onChooseListener listener) {
        this.listener = listener;
    }

    public interface onChooseListener {

        void onMaleClick();

        void onFemaleClick();

    }
}
