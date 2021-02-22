package com.jjc.qiqiharuniversity.biz.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:首页
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String[] titles = {"新闻快讯", "通知公告", "综合网站", "学校简介", "校园文化", "机构一览", "摄影天地", "学术理论"};
    private ViewPager viewPager;

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.home_tab_layout);
        viewPager = view.findViewById(R.id.home_view_pager);

        final HomeFragmentAdapter adapter = new HomeFragmentAdapter(getChildFragmentManager(), titles);
        //ViewPager设置Adapter
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(8);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewPager);
//        //为ViewPager添加页面改变监听
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        //为TabLayout添加Tab选择监听
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    public int getRootLayout() {
        return R.layout.fragment_home;
    }
}