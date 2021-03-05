package com.jjc.qiqiharuniversity.biz.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.common.BizSPConstants;
import com.jjc.qiqiharuniversity.common.CropManager;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.ImageManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.SPManager;
import com.jjc.qiqiharuniversity.common.UIHandler;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.view.ChooseCollegeDialog;
import com.jjc.qiqiharuniversity.common.view.ChooseGenderDialog;
import com.jjc.qiqiharuniversity.common.view.ChooseImageDialog;


import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:用户个人信息
 */
public class UserInfoActivity extends BaseActivity {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private static final int AVATAR_SIZE = 480;
    private RelativeLayout rlAvatar, rlNickName, rlGender, rlCollege, rlIntroduction;
    private ImageView ivAvatar;
    private EditText etNickname;
    private TextView tvGender, tvCollege, tvIntroduction;
    private ChooseImageDialog chooseImageDialog;
    private ChooseGenderDialog chooseGenderDialog;
    private ChooseCollegeDialog chooseCollegeDialog;
    private CropManager cropManager;
    private final List<String> collegeList = Arrays.asList("计算机与控制工程学院", "化工学院", "理学院", "音乐学院", "外国语学院",
            "经管学院", "体育学院", "食品学院", "轻纺学院");

    @Override
    public int getRootLayout() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("个人信息");
        rlAvatar = findViewById(R.id.rl_avatar);
        rlNickName = findViewById(R.id.rl_nickname);
        rlGender = findViewById(R.id.rl_gender);
        rlCollege = findViewById(R.id.rl_college);
        rlIntroduction = findViewById(R.id.rl_introduction);
        ivAvatar = findViewById(R.id.iv_info_avatar);
        etNickname = findViewById(R.id.et_nickname);
        tvGender = findViewById(R.id.tv_gender);
        tvCollege = findViewById(R.id.tv_college);
        tvIntroduction = findViewById(R.id.tv_introduction);

        chooseImageDialog = new ChooseImageDialog();
        rlAvatar.setOnClickListener(v -> {
            if (chooseImageDialog != null && !chooseImageDialog.isAdded()) {
                chooseImageDialog.show(getSupportFragmentManager(), ChooseImageDialog.class.getSimpleName());
            }
        });
        chooseImageDialog.setOnChooseListener(() -> {
            cropManager.pickFromGallery();
        });

        chooseGenderDialog = new ChooseGenderDialog();
        rlGender.setOnClickListener(v -> {
            if (chooseGenderDialog != null && !chooseGenderDialog.isAdded()) {
                chooseGenderDialog.show(getSupportFragmentManager(), ChooseGenderDialog.class.getSimpleName());
            }
        });
        chooseGenderDialog.setOnChooseListener(new ChooseGenderDialog.onChooseListener() {
            @Override
            public void onMaleClick() {
                tvGender.setText("男");
            }
            @Override
            public void onFemaleClick() {
                tvGender.setText("女");
            }
        });

        chooseCollegeDialog = new ChooseCollegeDialog();
        rlCollege.setOnClickListener(v -> {
            if (chooseCollegeDialog != null && !chooseCollegeDialog.isAdded()) {
                chooseCollegeDialog.show(getSupportFragmentManager(), ChooseGenderDialog.class.getSimpleName());
            }
        });
        chooseCollegeDialog.setDataList(collegeList);
        chooseCollegeDialog.setOnChooseListener(position -> tvCollege.setText(collegeList.get(position)));
    }

    @Override
    public void initData() {
        initAvatar();

        if (!LoginController.isLogin()) {
            return;
        }

        handleAvatar();
        String nickname = SPManager.getInstance().getString(this, BizSPConstants.KEY_USER_NICKNAME, null);
        String gender = SPManager.getInstance().getString(this, BizSPConstants.KEY_USER_GENDER, null);
        String college = SPManager.getInstance().getString(this, BizSPConstants.KEY_USER_COLLEGE, null);
        String introduction = SPManager.getInstance().getString(this, BizSPConstants.KEY_USER_INTRODUCTION, null);
        if (!TextUtils.isEmpty(nickname)) {
            etNickname.setText(nickname);
        }
        if (!TextUtils.isEmpty(gender)) {
            tvGender.setText(gender);
        }
        if (!TextUtils.isEmpty(college)) {
            tvCollege.setText(college);
        }
        if (!TextUtils.isEmpty(introduction)) {
            tvIntroduction.setText(introduction);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TextUtils.isEmpty(LoginController.getUserId())) {
            return;
        }

        SPManager.getInstance().putString(this, BizSPConstants.KEY_USER_NICKNAME, etNickname.getText().toString());
        SPManager.getInstance().putString(this, BizSPConstants.KEY_USER_GENDER, tvGender.getText().toString());
        SPManager.getInstance().putString(this, BizSPConstants.KEY_USER_COLLEGE, tvCollege.getText().toString());
        SPManager.getInstance().putString(this, BizSPConstants.KEY_USER_INTRODUCTION, tvIntroduction.getText().toString());

        EventBusManager.postSticky(new EventBusEvents.UpdateUserInfoEvent());
    }

    private void initAvatar() {
        LogHelper.i(TAG, "initAvatar");
        cropManager = new CropManager(this);
        cropManager.setRatio(CropManager.RATIO_1_1);
        cropManager.setMaxWidthAndHeight(AVATAR_SIZE, AVATAR_SIZE);
        cropManager.setSaveFilePath(LoginController.getAvatarLocalPath());
        cropManager.setCropListener(new CropManager.CropListener() {
            @Override
            public void onCropFile(final String filePath) {
                LogHelper.i(TAG, "onCropFile " + filePath);
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogHelper.i(TAG, "onCropFile -> run()");
                        handleAvatar();
                    }
                });
            }
        });
    }

    private void handleAvatar() {
        File file = new File(LoginController.getAvatarLocalPath());
        if (file.exists()) {
            LogHelper.i(TAG, "handleAvatar() -> file.exists()");
            ImageManager.loadCircleByFile(this, file, ivAvatar);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (cropManager != null) {
            cropManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
