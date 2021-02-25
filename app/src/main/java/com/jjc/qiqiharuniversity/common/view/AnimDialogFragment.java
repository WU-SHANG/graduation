package com.jjc.qiqiharuniversity.common.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.common.base.BaseDialogFragment;
import com.jjc.qiqiharuniversity.common.util.ScreenUtils;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:动画弹窗
 */
public abstract class AnimDialogFragment extends BaseDialogFragment {

    private static final String TAG = AnimDialogFragment.class.getSimpleName();
    private static final float DEFAULT_DIMAMOUNT = 0.5F;

    protected abstract int getRootLayout();

    protected abstract void initView(View view);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getRootLayout() > 0) {
            view = inflater.inflate(getRootLayout(), container, false);
        }
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //去除Dialog默认头部
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(isCancelableOutside());
        if (dialog.getWindow() != null && getDialogAnimationRes() > 0) {
            dialog.getWindow().setWindowAnimations(getDialogAnimationRes());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置窗体背景色透明
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置宽高
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (getDialogWidth() > 0) {
                layoutParams.width = getDialogWidth();
            } else {
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //透明度
            layoutParams.dimAmount = getDimAmount();
            //位置
            layoutParams.gravity = getGravity();
            window.setAttributes(layoutParams);
        }

    }

    //默认弹窗位置在下边
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    //默认宽高为包裹内容
    public int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getMarginLeft() {
        return 0;
    }

    public int getMarginRight() {
        return 0;
    }

    public int getDialogWidth() {
        return ScreenUtils.getScreenWidth(getContext()) - getMarginLeft() - getMarginRight();
    }

    //默认透明度为0.4
    public float getDimAmount() {
        return DEFAULT_DIMAMOUNT;
    }

    protected boolean isCancelableOutside() {
        return true;
    }

    //获取弹窗显示动画,子类实现
    protected int getDialogAnimationRes() {
        return 0;
    }

    public int getMaxHeight() {
        return Math.round(ScreenUtils.getScreenHeight(getContext()) * 2f / 3f);
    }
}
