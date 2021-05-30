package com.jjc.qiqiharuniversity.biz.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginConstants;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.biz.login.UserModel;
import com.jjc.qiqiharuniversity.common.CropManager;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.ImageManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.UIHandler;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;
import com.jjc.qiqiharuniversity.common.view.ChooseCollegeDialog;
import com.jjc.qiqiharuniversity.common.view.ChooseGenderDialog;
import com.jjc.qiqiharuniversity.common.view.ChooseImageDialog;


import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:用户个人信息
 */
public class UserInfoActivity extends BaseActivity {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private static final int AVATAR_SIZE = 480;
    private RelativeLayout rlAvatar, rlMobilePhone, rlNickName, rlGender, rlCollege, rlIntroduction;
    private ImageView ivAvatar;
    private EditText etNickname;
    private TextView tvMobilePhone, tvGender, tvCollege, etIntroduction;
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
        titleBarView.setRightText("保存");
        titleBarView.setOnRightClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(titleBarView.getId())) {
                return;
            }
            saveUserInfo();
        });
        rlAvatar = findViewById(R.id.rl_avatar);
        rlMobilePhone = findViewById(R.id.rl_mobile_phone);
        rlNickName = findViewById(R.id.rl_nickname);
        rlGender = findViewById(R.id.rl_gender);
        rlCollege = findViewById(R.id.rl_college);
        rlIntroduction = findViewById(R.id.rl_introduction);
        ivAvatar = findViewById(R.id.iv_info_avatar);
        tvMobilePhone = findViewById(R.id.tv_mobile_phone);
        etNickname = findViewById(R.id.et_nickname);
        tvGender = findViewById(R.id.tv_gender);
        tvCollege = findViewById(R.id.tv_college);
        etIntroduction = findViewById(R.id.et_introduction);

        chooseImageDialog = new ChooseImageDialog();
        rlAvatar.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(rlAvatar.getId())) {
                return;
            }
            if (chooseImageDialog != null && !chooseImageDialog.isAdded()) {
                chooseImageDialog.show(getSupportFragmentManager(), ChooseImageDialog.class.getSimpleName());
            }
        });
        chooseImageDialog.setOnChooseListener(() -> {
            cropManager.pickFromGallery();
        });

        rlMobilePhone.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(rlMobilePhone.getId())) {
                return;
            }
            BindMobileActivity.start(this);
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
        String phone = LoginController.getUserMobilePhone();
        String nickname = LoginController.getUserNickname();
        String gender = LoginController.getUserGender();
        String dept = LoginController.getUserDept();
        String introduce = LoginController.getUserIntroduce();
        if (!TextUtils.isEmpty(phone)) {
            tvMobilePhone.setText(phone);
        }
        if (!TextUtils.isEmpty(nickname)) {
            etNickname.setText(nickname);
        }
        if (!TextUtils.isEmpty(gender)) {
            tvGender.setText(gender);
        }
        if (!TextUtils.isEmpty(dept)) {
            tvCollege.setText(dept);
        }
        if (!TextUtils.isEmpty(introduce)) {
            etIntroduction.setText(introduce);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void saveUserInfo() {
        if (TextUtils.isEmpty(LoginController.getObjectId())) {
            return;
        }

        SharedPreferences.Editor editor = LoginController.getEditor();
        editor.putString(LoginConstants.NICKNAME, etNickname.getText().toString());
        editor.putString(LoginConstants.GENDER, tvGender.getText().toString());
        editor.putString(LoginConstants.MOBILE_PHONE, tvMobilePhone.getText().toString());
        editor.putString(LoginConstants.DEPT, tvCollege.getText().toString());
        editor.putString(LoginConstants.INTRODUCE, etIntroduction.getText().toString());
        editor.apply();

        UserModel user = BmobUser.getCurrentUser(UserModel.class);
        user.setNickname(etNickname.getText().toString());
        user.setGender(tvGender.getText().toString());
        user.setDept(tvCollege.getText().toString());
        user.setIntroduce(etIntroduction.getText().toString());
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastManager.show(UserInfoActivity.this, "保存成功");
                    finish();
                } else {
                    ToastManager.show(UserInfoActivity.this, "保存失败：" + e.getErrorCode());
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });

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
