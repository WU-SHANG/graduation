package com.jjc.qiqiharuniversity.biz.discover;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.ImageManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.SavePicUtils;
import com.jjc.qiqiharuniversity.common.view.ChooseImageDialog;

/**
 * Author jiajingchao
 * Created on 2021/3/8
 * Description:预约选座模块
 * todo 改版
 */
public class SeatReservationActivity extends BaseActivity {

    private ImageView ivSeat;
    private ChooseImageDialog chooseDialog;

    @Override
    public int getRootLayout() {
        return R.layout.activity_seat_reservation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("预约选座");
        ivSeat = findViewById(R.id.iv_seat);
        ImageManager.load(this, getString(R.string.seat_reservation_image_url), ivSeat);

        chooseDialog = new ChooseImageDialog();
        chooseDialog.setText("保存图片");
        chooseDialog.setOnChooseListener(() -> {
            requestNeedPermission();
            SavePicUtils.save(SeatReservationActivity.this, getString(R.string.seat_reservation_image_url));
        });
        ivSeat.setOnLongClickListener(v -> {
            chooseDialog.show(getSupportFragmentManager(), SeatReservationActivity.class.getSimpleName());
            return false;
        });
    }

    private void requestNeedPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }
}
