package com.jjc.qiqiharuniversity.biz.me;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginActivity;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.common.BizSPConstants;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.ImageManager;
import com.jjc.qiqiharuniversity.common.SPManager;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.UIHandler;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;


/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:
 */
public class MineFragment extends BaseFragment {

    private TextView tvName;
    private RelativeLayout rlFeedBack, rlAbout, rlSetting;
    private ImageView ivAvatar;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBusManager.register(this);
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvName = view.findViewById(R.id.tv_name);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        rlFeedBack = view.findViewById(R.id.rl_feedback);
        rlAbout = view.findViewById(R.id.rl_about);
        rlSetting = view.findViewById(R.id.rl_setting);
        tvName.setOnClickListener(v -> {
            if (!LoginController.isLogin()) {
                LoginActivity.start(getContext(), LoginActivity.class);
            } else {
                UserInfoActivity.start(getContext(), UserInfoActivity.class);
            }
        });

        if (LoginController.isLogin()) {
            tvName.setText(LoginController.getUserNickname());
            handleAvatar();
        }

        ivAvatar.setOnClickListener(v -> {
            if (!LoginController.isLogin()) {
                LoginActivity.start(getContext(), LoginActivity.class);
                ToastManager.show(getContext(), "请先登录");
            } else {
                UserInfoActivity.start(getContext(), UserInfoActivity.class);
            }
        });

        rlAbout.setOnClickListener(v -> {
            AboutActivity.start(getContext(), AboutActivity.class);
        });

        rlSetting.setOnClickListener(v -> {
            SettingActivity.start(getContext(), SettingActivity.class);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    @Subscribe
    public void onEvent(EventBusEvents.LoginSuccessEvent event) {
        UIHandler.post(() -> {
            tvName.setText(SPManager.getInstance().getString(getContext(), BizSPConstants.KEY_USER_NICKNAME, ""));
            handleAvatar();
        });
    }

    @Subscribe
    public void onEvent(EventBusEvents.UpdateUserInfoEvent event) {
        UIHandler.post(() -> {
            tvName.setText(SPManager.getInstance().getString(getContext(), BizSPConstants.KEY_USER_NICKNAME, ""));
            handleAvatar();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Subscribe
    public void onEvent(EventBusEvents.LogoutEvent event) {
        UIHandler.post(() -> {
            ivAvatar.setImageDrawable(getResources().getDrawable(R.drawable.icon_avatar));
            tvName.setText("点击登录");
        });
    }

    private void handleAvatar() {
        File file = new File(LoginController.getAvatarLocalPath());
        if (file.exists()) {
            ImageManager.loadCircleByFile(getActivity(), file, ivAvatar);
        }
    }

}