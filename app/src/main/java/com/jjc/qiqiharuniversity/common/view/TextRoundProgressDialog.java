package com.jjc.qiqiharuniversity.common.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.UIHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class TextRoundProgressDialog extends BizDialogFragment {

    private Timer timer;

    private int currentProgress;

    @Override
    public int getRootLayout() {
        return R.layout.layout_text_round_progress_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextRoundProgress textRoundProgress = view.findViewById(R.id.textRoundProgress);
        textRoundProgress.setMax(10000);
        currentProgress = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentProgress > 8000) {
                            currentProgress = currentProgress + 1;
                        } else if (currentProgress > 6000) {
                            currentProgress = currentProgress + 2;
                        } else {
                            currentProgress = currentProgress + 3;
                        }

                        if (currentProgress > 9800) {
                            currentProgress = 9800;
                        }
                        textRoundProgress.setProgress(currentProgress);
                    }
                });
            }
        }, 0, 10);
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
