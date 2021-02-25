package com.jjc.qiqiharuniversity.common;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.jjc.qiqiharuniversity.common.base.BaseAlertDialog;


/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:弹窗管理类
 */
public class AlertManager {

    private static final String POSITIVE_BUTTON = "确定";
    private static final String NEGATIVE_BUTTON = "取消";
    private static final String CONFIRM_BUTTON = "我知道了";

    public interface AlertListener {
        void onPositive();

        void onNegative();
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            final AlertListener listener) {
        show(fragmentManager, title, "", CONFIRM_BUTTON, "", "", 0, "", false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String content,
                            boolean cancelOutside,
                            final AlertListener listener) {
        show(fragmentManager, "", content, CONFIRM_BUTTON, "", "", 0, "", cancelOutside, listener);
    }

    public static void showWithLottie(@NonNull FragmentManager fragmentManager,
                                      String title,
                                      String lottiePath,
                                      final AlertListener listener) {
        show(fragmentManager, title, "", CONFIRM_BUTTON, "", "", 0, lottiePath, false, listener);
    }

    public static void showWithLottieV2(@NonNull FragmentManager fragmentManager,
                                        String content,
                                        String lottiePath,
                                        final AlertListener listener) {
        show(fragmentManager, "", content, CONFIRM_BUTTON, "", "", 0, lottiePath, false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            String confirm,
                            final AlertListener listener) {
        show(fragmentManager, title, "", confirm, "", "", 0, "", false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            String negative,
                            String positive,
                            final AlertListener listener) {
        show(fragmentManager, title, "", "", negative, positive, 0, "", false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            String content,
                            String confirm,
                            boolean cancelOutside,
                            final AlertListener listener) {
        show(fragmentManager, title, content, confirm, "", "", 0, "", false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            String content,
                            String negative,
                            String positive,
                            final AlertListener listener) {
        show(fragmentManager, title, content, "", negative, positive, 0, "", false, listener);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            String title,
                            String content,
                            String confirm,
                            String negative,
                            String positive,
                            int resId,
                            String lottiePath,
                            boolean cancelOnTouchOutside,
                            final AlertListener listener) {
        BaseAlertDialog dialog = new BaseAlertDialog();
        dialog.setTitle(title)
                .setContent(content)
                .setConfirm(confirm)
                .setNegativeAndPositive(negative, positive)
                .setImageRes(resId)
                .setLottieView(lottiePath)
                .setCancelOnTouchOutside(cancelOnTouchOutside);
        dialog.setOnAlertClickListener(new BaseAlertDialog.OnAlertClickListener() {
            @Override
            public void onNegative() {
                if (listener != null) {
                    listener.onNegative();
                }
            }

            @Override
            public void onPositive() {
                if (listener != null) {
                    listener.onPositive();
                }
            }
        });
        dialog.show(fragmentManager, BaseAlertDialog.class.getSimpleName());
    }

}
