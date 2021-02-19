package com.jjc.qiqiharuniversity.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.adapter.HomePagerAdapter;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:首页
 */
public class HomeFragment extends Fragment {

    private View view;
    private TabLayout home_tab_layout;
    private ViewPager home_view_pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initViewPager();
        return view;
    }

    private void initView() {
        home_tab_layout = view.findViewById(R.id.home_tab_layout);
        home_view_pager = view.findViewById(R.id.home_view_pager);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        final PagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager(), home_tab_layout.getTabCount());
        //ViewPager设置Adapter
        home_view_pager.setAdapter(adapter);

        //为ViewPager添加页面改变监听
        home_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(home_tab_layout));

        //为TabLayout添加Tab选择监听
        home_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                home_view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}