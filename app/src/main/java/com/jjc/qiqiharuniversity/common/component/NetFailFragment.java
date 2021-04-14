package com.jjc.qiqiharuniversity.common.component;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

/**
 * Author jiajingchao
 * Created on 2021/4/13
 * Description:网络异常页
 */
public class NetFailFragment extends BaseFragment {
    private TextView tvRefresh;
    private RefreshListener refreshListener;
    private RelativeLayout rlBg;
    private int color;

    public NetFailFragment(){
    }

    @Override
    public int getRootLayout() {
        return R.layout.fragment_network_fail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRefresh = view.findViewById(R.id.tv_click_retry);
        rlBg = view.findViewById(R.id.rl_bg);
        rlBg.setBackgroundColor(color);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DisplayUtils.isFastDoubleClickNew(tvRefresh.getId())) {
                    return;
                }
                refreshListener.refresh();
            }
        });
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

    }

    public interface RefreshListener {
        void refresh();
    }

    public void setRefreshListener(RefreshListener listener) {
        refreshListener = listener;
    }

    public void setBgColor(int color) {
        this.color = color;
    }
}
