package com.jjc.qiqiharuniversity.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.view.TitleBarView;


/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public abstract class BaseFragment extends Fragment {

    public TitleBarView titleBarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getRootLayout() == 0 ? R.layout.empty_default : getRootLayout(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view, savedInstanceState);
    }

    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void initData() {

    }

    public abstract int getRootLayout();

    public void removeCurrent() {
        if (getParentFragment() != null) {
            getParentFragment().getChildFragmentManager().popBackStack();
        }
    }

    public void initTitleBar(View view) {
        titleBarView = view.findViewById(R.id.title_bar);
    }

    public void release() {

    }
}
