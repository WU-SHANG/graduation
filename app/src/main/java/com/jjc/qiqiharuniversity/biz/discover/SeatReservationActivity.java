package com.jjc.qiqiharuniversity.biz.discover;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

    private static final String SEAT_IMAGE_URL = "https://mmbiz.qpic.cn/sz_mmbiz_png/FdIxoZ0xGPFibkk6tk9wnjqLbzbCiarG5p73uqTc5vtOr0amFib6siaam3n0q3XAY3libxnsnVI2wr93y9bLfGicrySg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1";

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
            SavePicUtils.save(SeatReservationActivity.this, SEAT_IMAGE_URL);
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
