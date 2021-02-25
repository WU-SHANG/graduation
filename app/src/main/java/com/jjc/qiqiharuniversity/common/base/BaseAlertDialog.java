package com.jjc.qiqiharuniversity.common.base;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.view.BizDialogFragment;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:基础弹窗
 */
public class BaseAlertDialog extends BizDialogFragment {

    private String mTitle;
    private String mContent;
    private String mConfirm;
    private String mNegative;
    private String mPositive;
    private int mResId;
    private String mLottiePath;
    private boolean mCancelOnTouchOutside = false;

    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivImg;
    private LottieAnimationView lavAnim;
    private TextView tvConfirm;
    private LinearLayout llBtns;
    private TextView tvNegative;
    private TextView tvPositive;
    private RelativeLayout rlContainer;

    @Override
    public int getRootLayout() {
        return R.layout.layout_base_alert_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initView(view);
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_dialog_title);
        tvContent = view.findViewById(R.id.tv_dialog_content);
        tvConfirm = view.findViewById(R.id.tv_dialog_confirm);
        llBtns = view.findViewById(R.id.ll_btns);
        tvNegative = view.findViewById(R.id.tv_dialog_negative);
        tvPositive = view.findViewById(R.id.tv_dialog_positive);
        ivImg = view.findViewById(R.id.iv_dialog_image);
        lavAnim = view.findViewById(R.id.lav_dialog_image);
        rlContainer = view.findViewById(R.id.rl_container);
        apply();
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPositive();
                }
                dismissAllowingStateLoss();
            }
        });

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onNegative();
                }
                dismissAllowingStateLoss();
            }
        });

        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPositive();
                }
                dismissAllowingStateLoss();
            }
        });

        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancelOnTouchOutside){
                    dismissAllowingStateLoss();
                }
            }
        });

    }

    public BaseAlertDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public BaseAlertDialog setContent(String content) {
        mContent = content;
        return this;
    }

    public BaseAlertDialog setImageRes(int resId) {
        mResId = resId;
        return this;
    }

    public BaseAlertDialog setConfirm(String confirm) {
        mConfirm = confirm;
        return this;
    }

    public BaseAlertDialog setNegativeAndPositive(String negative, String positive) {
        mNegative = negative;
        mPositive = positive;
        return this;
    }

    public BaseAlertDialog setLottieView(String assetPath) {
        mLottiePath = assetPath;
        return this;
    }

    public BaseAlertDialog setCancelOnTouchOutside(boolean cancelOnTouchOutside){
        this.mCancelOnTouchOutside = cancelOnTouchOutside;
        return this;
    }

    public interface OnAlertClickListener {
        void onNegative();

        void onPositive();
    }

    private OnAlertClickListener listener;

    public void setOnAlertClickListener(OnAlertClickListener listener) {
        this.listener = listener;
    }

    private void apply() {
        if (tvTitle != null) {
            if (TextUtils.isEmpty(mTitle)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                if (tvTitle.getVisibility() != View.VISIBLE) {
                    tvTitle.setVisibility(View.VISIBLE);
                }
                tvTitle.setText(mTitle);
            }
        }

        if (tvContent != null) {
            if (TextUtils.isEmpty(mContent)) {
                tvContent.setVisibility(View.GONE);
            } else {
                if (tvContent.getVisibility() != View.VISIBLE) {
                    tvContent.setVisibility(View.VISIBLE);
                }
                tvContent.setText(mContent);
            }
        }

        if (ivImg != null) {
            if (mResId == 0) {
                ivImg.setVisibility(View.GONE);
            } else {
                if (ivImg.getVisibility() != View.VISIBLE) {
                    ivImg.setVisibility(View.VISIBLE);
                }
                lavAnim.setVisibility(View.GONE);
                ivImg.setImageResource(mResId);
            }
        }
        if (tvConfirm != null) {
            if (TextUtils.isEmpty(mConfirm)) {
                tvConfirm.setVisibility(View.GONE);
            } else {
                if (tvConfirm.getVisibility() != View.VISIBLE) {
                    tvConfirm.setVisibility(View.VISIBLE);
                }
                if (llBtns != null) {
                    llBtns.setVisibility(View.GONE);
                }
                tvConfirm.setText(mConfirm);
            }
        }

        if (tvNegative != null && tvPositive != null) {
            if (TextUtils.isEmpty(mNegative) || TextUtils.isEmpty(mPositive)) {
                llBtns.setVisibility(View.GONE);
            } else {
                if (llBtns.getVisibility() != View.VISIBLE) {
                    llBtns.setVisibility(View.VISIBLE);
                }
                tvConfirm.setVisibility(View.GONE);
                tvNegative.setText(mNegative);
                tvPositive.setText(mPositive);
            }
        }
        if (lavAnim != null) {
            if (TextUtils.isEmpty(mLottiePath)) {
                lavAnim.setVisibility(View.GONE);
            } else {
                if (lavAnim.getVisibility() != View.VISIBLE) {
                    lavAnim.setVisibility(View.VISIBLE);
                }
                ivImg.setVisibility(View.GONE);
                lavAnim.setAnimation(mLottiePath);
                lavAnim.playAnimation();
            }
        }
    }
}
