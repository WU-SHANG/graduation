package com.jjc.qiqiharuniversity.biz.me;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.biz.login.UserModel;
import com.jjc.qiqiharuniversity.common.CountDownHelper;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.UIHandler;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

import java.util.Objects;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:绑定手机号-输入验证码
 */
public class InputVerificationCodeFragment extends BaseFragment {

    private static final String TAG = InputVerificationCodeFragment.class.getSimpleName();

    private TextView tvPhoneText;
    private TextView tvAgainAcquire;
    private CountDownHelper countDownHelper;
    private TextView tvCountDown;
    private String phoneNumber;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_input_verification_code;
    }

    public static InputVerificationCodeFragment newInstance(String phoneNumber) {
        InputVerificationCodeFragment fragment = new InputVerificationCodeFragment();
        Bundle args = new Bundle();
        args.putString("user_phone_number", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvPhoneText.setText("验证码已发送至86 " + phoneNumber);
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initTitleBar(view);
        titleBarView.setTransparent();
        MobileVerificationCodeView codeView = (MobileVerificationCodeView) view.findViewById(R.id.v_verification_code);
        tvPhoneText = view.findViewById(R.id.tv_show_phone_text);
        tvAgainAcquire = view.findViewById(R.id.tv_again_acquire);
        tvCountDown = view.findViewById(R.id.tv_cout_down);
        countDownHelper = new CountDownHelper();
        countDownTime();
        titleBarView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBusEvents.BackSendCode event = new EventBusEvents.BackSendCode();
                EventBusManager.post(event);
            }
        });
        phoneNumber = getArguments().getString("user_phone_number");
        tvPhoneText.setText("验证码已发送至86 " + phoneNumber);
        tvAgainAcquire.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvAgainAcquire.getId())) {
                return;
            }
            LogHelper.i(TAG, "phoneNumber == " + phoneNumber);
            if (ObjectHelper.isIllegal(phoneNumber)) {
                return;
            }

            getVerifyCode();

        });

        codeView.setOnInputListener(new MobileVerificationCodeView.OnInputListener() {
            @Override
            public void onSuccess(String code) {
                LogHelper.i(TAG, "phoneNumber == " + phoneNumber);
                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(code)) {
                    return;
                }

                checkVerifyCode(code);

            }

            @Override
            public void onInput() {
            }
        });
        codeView.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(codeView, InputMethodManager.RESULT_SHOWN);
        });

        countDownTime();
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        BmobSMS.requestSMSCode(phoneNumber, "qiqihar", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    EventBusEvents.SendCode event = new EventBusEvents.SendCode();
                    event.lastSendMs = System.currentTimeMillis();
                    event.isFromVerification = true;
                    event.phoneNum = phoneNumber;
                    EventBusManager.post(event);
                    countDownTime();
                    ToastManager.show(getContext(), "发送验证码成功");
                    LogHelper.i(TAG, "smsId == " + smsId);
                } else {
                    if (e.getErrorCode() == 10010) {
                        ToastManager.show(getContext(), "发送验证码间隔过短，请稍后再试");
                    } else {
                        ToastManager.show(getContext(), "发送验证码失败：" + e.getErrorCode());
                    }
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * 检查验证码，成功就更新手机号信息，并退出页面
     */
    private void checkVerifyCode(String code) {
        BmobSMS.verifySmsCode(phoneNumber, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    UserModel user = BmobUser.getCurrentUser(UserModel.class);
                    user.setMobilePhoneNumber(phoneNumber);
                    user.setMobilePhoneNumberVerified(Boolean.TRUE);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                LoginController.setUserMobilePhone(phoneNumber);
                                ToastManager.show(getContext(), "绑定成功");
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                ToastManager.show(getContext(), "绑定失败：" + e.getErrorCode());
                                LogHelper.i(TAG, e.getMessage());
                            }
                        }
                    });
                } else {
                    if (e.getErrorCode() == 207) {
                        ToastManager.show(getContext(), "验证码错误");
                    } else {
                        ToastManager.show(getContext(), "验证码验证失败：" + e.getErrorCode());
                        LogHelper.i(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void countDownTime() {
        tvAgainAcquire.setVisibility(View.GONE);
        tvCountDown.setVisibility(View.VISIBLE);
        if (countDownHelper != null) {
            countDownHelper.start(
                    60 * CountDownHelper.DEFAULT_INTERVAL,
                    CountDownHelper.DEFAULT_INTERVAL, new CountDownHelper.CountDownListener() {
                        @Override
                        public void onTick(final long millisUntilFinished) {
                            UIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String text = (millisUntilFinished / CountDownHelper.DEFAULT_INTERVAL) + "秒后重新获取";
                                    tvCountDown.setText(text);
                                }
                            });
                        }

                        @Override
                        public void onFinish() {
                            UIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvCountDown.setVisibility(View.GONE);
                                    tvAgainAcquire.setVisibility(View.VISIBLE);
                                    tvCountDown.setText("获取验证码");
                                }
                            });
                        }
                    });
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
        if (countDownHelper != null) {
            countDownHelper.stop();
            countDownHelper = null;
        }
    }
}
