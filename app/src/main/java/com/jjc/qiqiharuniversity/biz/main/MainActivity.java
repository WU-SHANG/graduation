package com.jjc.qiqiharuniversity.biz.main;


import android.os.Bundle;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.PageManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.view.TextRoundProgressDialog;

import org.greenrobot.eventbus.Subscribe;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class MainActivity extends BaseActivity {

    private TextRoundProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageManager.addFragment(new MainFragment(), getSupportFragmentManager(), R.id.main_container);

        EventBusManager.register(this);

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

}