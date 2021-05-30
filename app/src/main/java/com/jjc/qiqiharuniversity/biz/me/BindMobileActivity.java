package com.jjc.qiqiharuniversity.biz.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;

/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:绑定手机号页
 */
public class BindMobileActivity extends BaseActivity {

    private static final String TAG = BindMobileActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private InputMobileNumberFragment inputMobileNumberFragment;
    private InputVerificationCodeFragment inputVerificationCodeFragment;
    private String phoneNumber;

    @Override
    public int getRootLayout() {
        return R.layout.activity_bind_mobile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BindMobileActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        initInputMobileNumberFragment();
        showInputMobileNumberFragment();
    }

    private void initInputMobileNumberFragment() {
        if (inputMobileNumberFragment == null) {
            inputMobileNumberFragment = InputMobileNumberFragment.newInstance();
        }
    }

    private void initInputVerificationCodeFragment() {
        if (inputVerificationCodeFragment == null) {
            inputVerificationCodeFragment = InputVerificationCodeFragment.newInstance(phoneNumber);
        }
    }

    private void showInputMobileNumberFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (inputMobileNumberFragment != null) {
            if (!inputMobileNumberFragment.isAdded()) {
                transaction.add(R.id.fl_bind_mobile_container, inputMobileNumberFragment);
                transaction.commitAllowingStateLoss();
            } else {
                if (inputVerificationCodeFragment != null && inputVerificationCodeFragment.isAdded()) {
                    transaction.remove(inputVerificationCodeFragment);
                    inputVerificationCodeFragment = null;
                }
                transaction.show(inputMobileNumberFragment);
                transaction.commitAllowingStateLoss();
            }
        } else {
            initInputMobileNumberFragment();
        }
    }

    private void showInputVerificationCodeFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (inputVerificationCodeFragment != null) {
            if (!inputVerificationCodeFragment.isAdded()) {
                transaction.add(R.id.fl_bind_mobile_container, inputVerificationCodeFragment);
                if (inputMobileNumberFragment != null && inputMobileNumberFragment.isAdded()) {
                    transaction.hide(inputMobileNumberFragment);
                }
                transaction.commitAllowingStateLoss();
            } else {
                if (inputMobileNumberFragment != null && inputMobileNumberFragment.isAdded()) {
                    transaction.hide(inputMobileNumberFragment);
                }
                transaction.show(inputVerificationCodeFragment);
                transaction.commitAllowingStateLoss();
            }
        } else {
            initInputVerificationCodeFragment();
        }
    }

    @Subscribe
    public void onEvent(final EventBusEvents.SendCode event) {
        phoneNumber = event.phoneNum;
        if (inputMobileNumberFragment != null) {
            inputMobileNumberFragment.setLastSendTimeMS(event.lastSendMs);
        }

        initInputVerificationCodeFragment();
        if (inputVerificationCodeFragment != null) {
            inputVerificationCodeFragment.setPhoneNumber(phoneNumber);
        }
        if (!event.isFromVerification) {
            showInputVerificationCodeFragment();
        }
    }

    @Subscribe
    public void onEvent(final EventBusEvents.BackSendCode event) {
        showInputMobileNumberFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

}
