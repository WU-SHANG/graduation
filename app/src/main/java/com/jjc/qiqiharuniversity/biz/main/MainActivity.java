package com.jjc.qiqiharuniversity.biz.main;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.common.BizSPConstants;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.PageManager;
import com.jjc.qiqiharuniversity.common.SPManager;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.view.TextRoundProgressDialog;

import org.greenrobot.eventbus.Subscribe;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextRoundProgressDialog loadingDialog;

    private long exitTime;//获取第一次点击返回键的系统时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageManager.addFragment(new MainFragment(), getSupportFragmentManager(), R.id.main_container);

        EventBusManager.register(this);

    }

    @Override
    public void initData() {
        if (LoginController.isLogin()) {
            LogHelper.i(TAG, "initData -> LoginSuccessEvent");
            EventBusManager.postSticky(new EventBusEvents.LoginSuccessEvent());
        }
    }

    @Override
    public int getRootLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    @Subscribe
    public void onEvent(EventBusEvents.LoadingEvent event) {
        if (event.isLoading) {
            if (loadingDialog == null) {
                loadingDialog = new TextRoundProgressDialog();
            }
            if (!loadingDialog.isAdded()) {
                loadingDialog.show(getSupportFragmentManager(), TextRoundProgressDialog.class.getSimpleName());
            }
        } else {
            if (loadingDialog != null) {
                loadingDialog.dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {  //系统时间减去exitTime
            ToastManager.show(MainActivity.this, "再按一次退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}