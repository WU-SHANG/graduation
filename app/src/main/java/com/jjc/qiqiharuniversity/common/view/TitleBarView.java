package com.jjc.qiqiharuniversity.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;


/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class TitleBarView extends RelativeLayout {

    private TextView mTvCenter;
    private TextView mTvRight;
    private ImageView mIvRight;
    private ImageView mIvLeft;
    private View mVRoot;
    private ImageView mIvShadow;

    public TitleBarView(Context context) {
        super(context);
        init(context);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_titlebar, this);

        mVRoot = findViewById(R.id.v_title_bar_root);
        mTvCenter = findViewById(R.id.tv_title_bar_center);
        mTvRight = findViewById(R.id.tv_title_bar_right);
        mIvLeft = findViewById(R.id.iv_title_bar_left);
        mIvRight = findViewById(R.id.iv_title_bar_right);
        mIvShadow = findViewById(R.id.tv_title_bar_shadow);

        mIvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 默认实现
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    public void showTitleBarShadow(boolean show) {
        mIvShadow.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setCenterAlpha(float alpha) {
        mTvCenter.setAlpha(alpha);
    }

    public void setCenterText(String text) {
        mTvCenter.setText(text);
        mTvCenter.setVisibility(VISIBLE);
    }

    public void setCenterText(int res) {
        mTvCenter.setText(res);
        mTvCenter.setVisibility(VISIBLE);
    }

    public void setRightText(String text) {
        mTvRight.setText(text);
        mTvRight.setVisibility(VISIBLE);
    }

    public void setRightText(int res) {
        mTvRight.setText(res);
        mTvRight.setVisibility(VISIBLE);
    }

    public void setRightImage(int res) {
        mIvRight.setImageResource(res);
        mIvRight.setVisibility(VISIBLE);
    }

    public void setLeftImage(int res) {
        mIvLeft.setImageResource(res);
    }

    public void setOnCenterClickListener(@NonNull OnClickListener listener) {
        mTvCenter.setOnClickListener(listener);
    }

    public void setOnRightClickListener(@NonNull OnClickListener listener) {
        if (mTvRight.getVisibility() == View.VISIBLE) {
            mTvRight.setOnClickListener(listener);
        }
        if (mIvRight.getVisibility() == View.VISIBLE) {
            mIvRight.setOnClickListener(listener);
        }
    }

    public void setOnBackClickListener(@NonNull OnClickListener listener) {
        mIvLeft.setOnClickListener(listener);
    }

    public void hideBack() {
        mIvLeft.setVisibility(GONE);
    }

    public void showBack() {
        mIvLeft.setVisibility(VISIBLE);
    }

    public void setTransparent() {
        mVRoot.setBackgroundColor(Color.TRANSPARENT);
        mIvShadow.setAlpha(0f);
    }

    public void setLeftButtonColor(int color) {
        mIvLeft.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void setCenterTextColor(int color) {
        mTvCenter.setTextColor(color);
        mTvCenter.setVisibility(VISIBLE);
    }

    public void setLeftButtonWhite() {
        setLeftImage(R.drawable.titlebar_back_white);
    }

    public void setLeftButtonBlack() {
        setLeftImage(R.drawable.titlebar_back);
    }

    public void setLeftButtonAlpha(float alpha) {
        mIvLeft.setAlpha(alpha);
    }

    public void setRightButtonAlpha(float alpha) {
        mIvRight.setAlpha(alpha);
    }

    public void setBackgroundWhite() {
        mVRoot.setBackgroundColor(Color.WHITE);
        mIvShadow.setAlpha(1f);
    }

    public void setBackgroundAlpha(float alpha) {
        mVRoot.setAlpha(alpha);
    }

    public void setTitleBarShadowAlpha(float alpha) {
        mIvShadow.setAlpha(alpha);
    }

}
