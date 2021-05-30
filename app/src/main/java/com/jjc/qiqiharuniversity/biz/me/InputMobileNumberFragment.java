package com.jjc.qiqiharuniversity.biz.me;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;


import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:绑定手机号-输入手机号
 */
public class InputMobileNumberFragment extends BaseFragment {

    private static final String TAG = InputMobileNumberFragment.class.getSimpleName();

    private EditText etMobileNumber;
    private GradientDrawable myShape;
    private long lastSendTimeMS = 0;
    private boolean noText;
    private Typeface typeface;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_input_mobile_number;
    }

    public static InputMobileNumberFragment newInstance() {
        InputMobileNumberFragment fragment = new InputMobileNumberFragment();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        etMobileNumber.setFocusable(true);
        etMobileNumber.setFocusableInTouchMode(true);
        etMobileNumber.requestFocus();
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initTitleBar(view);
        titleBarView.setTransparent();
        titleBarView.showBack();

        TextView tvTitle = view.findViewById(R.id.tv_bind_mobile_title);
        TextView tvSubTitle = view.findViewById(R.id.tv_bind_mobile_sub_title);
        if (!TextUtils.isEmpty(LoginController.getUserMobilePhone())) {
            tvTitle.setText("更换手机号");
            tvSubTitle.setText(String.format("原手机号：%s", LoginController.getUserMobilePhone()));
        }
        TextView tvNum = view.findViewById(R.id.tv_num);
        etMobileNumber = view.findViewById(R.id.et_bind_mobile_number);
        etMobileNumber.addTextChangedListener(watcher);
        TextView tvSendCode = view.findViewById(R.id.tv_send_code);
        myShape = (GradientDrawable) tvSendCode.getBackground();

        etMobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        SpannableString s = new SpannableString("请输入11位手机号码");
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(14, true);
        s.setSpan(textSize, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etMobileNumber.setHint(s);
        etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "DIN_Alternate_Bold.ttf");

        tvNum.setTypeface(typeface);
        tvSendCode.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvSendCode.getId())) {
                return;
            }
            if (ObjectHelper.isIllegal(etMobileNumber.getText().toString())) {
                return;
            }
            if (etMobileNumber.length() < 11) {
                ToastManager.show(getActivity(), "请输入正确的手机号");
                return;
            }
            LogHelper.i(TAG, "lastSendTimeMS == " + lastSendTimeMS + " System.currentTimeMillis() == " + System.currentTimeMillis());
            if (lastSendTimeMS != 0 && System.currentTimeMillis() - lastSendTimeMS < 60 * 1000) {
                ToastManager.show(getActivity(), "发送验证码间隔过短，请稍后再试");
                return;
            }

            getVerifyCode();
        });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        String mobileNumber = etMobileNumber.getText().toString();
        BmobSMS.requestSMSCode(mobileNumber, "qiqihar", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    lastSendTimeMS = System.currentTimeMillis();
                    EventBusEvents.SendCode event = new EventBusEvents.SendCode();
                    event.phoneNum = etMobileNumber.getText().toString();
                    event.lastSendMs = lastSendTimeMS;
                    event.isFromVerification = false;
                    EventBusManager.post(event);
                    ToastManager.show(getContext(), "发送验证码成功，短信ID：" + smsId);
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

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
            if (s.length() == 0) {
                noText = true;
                etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                etMobileNumber.setTypeface(Typeface.DEFAULT);

            } else if (noText) {
                noText = false;
                etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                etMobileNumber.setTypeface(typeface);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
            if (s.length() == 11) {
                etMobileNumber.setTypeface(typeface);
                myShape.setColor(getContext().getResources().getColor(R.color.theme_blue));
                etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            } else if (s.length() > 0) {
                etMobileNumber.setTypeface(typeface);
                myShape.setColor(getContext().getResources().getColor(R.color.gray_cccccc));
                etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            } else if (s.length() == 0) {
                etMobileNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }

        }
    };

    public void setLastSendTimeMS(long lastSendTimeMS) {
        this.lastSendTimeMS = lastSendTimeMS;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
