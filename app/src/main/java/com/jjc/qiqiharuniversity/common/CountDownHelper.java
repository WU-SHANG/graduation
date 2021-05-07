package com.jjc.qiqiharuniversity.common;

import android.os.CountDownTimer;

/**
 * Author jiajingchao
 * Created on 2021/4/16
 * Description: 倒计时帮助类
 */
public class CountDownHelper {

    private static final String TAG = CountDownHelper.class.getSimpleName();
    public static final long DEFAULT_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private long remain;
    private long interval;
    private CountDownListener countDownListener;

    public interface CountDownListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    public void start(long millisInFuture, long countDownInterval, final CountDownListener listener) {
        LogHelper.i(TAG, "start millisInFuture:" + millisInFuture);

        remain = millisInFuture;
        interval = countDownInterval;
        countDownListener = listener;

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remain = millisUntilFinished;
                if (listener != null) {
                    listener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                remain = 0;
                if (listener != null) {
                    listener.onFinish();
                }
            }
        };

        countDownTimer.start();
    }

    public void stop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void resume() {
        if (isRunning() || remain <= 0) {
            return;
        }

        LogHelper.i("resume");

        start(remain, interval, countDownListener);
    }

    public void pause() {
        stop();
    }

    public boolean isRunning() {
        return countDownTimer != null;
    }

}
