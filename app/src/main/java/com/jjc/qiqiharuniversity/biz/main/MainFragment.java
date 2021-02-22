package com.jjc.qiqiharuniversity.biz.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.jjc.qiqiharuniversity.R;

import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.common.EventBusManager;


/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String[] titles = {"首页", "发现", "我的"};
    private int[] icons = {R.drawable.selector_tab_home, R.drawable.selector_tab_discover, R.drawable.selector_tab_mine};
    private ViewPager viewPager;
    private MainActivity mainActivity;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainActivity.hideStatusBarMode(false, getResources().getColor(R.color.white));
        EventBusManager.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        viewPager = view.findViewById(R.id.vp_main);
        MainFragmentAdapter adapter = new MainFragmentAdapter(getChildFragmentManager(), titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = view.findViewById(R.id.tl_main);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewPager);

        //noinspection ConstantConditions
        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        //noinspection ConstantConditions
        tabLayout.getTabAt(1).setCustomView(getTabView(1));
        //noinspection ConstantConditions
        tabLayout.getTabAt(2).setCustomView(getTabView(2));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mainActivity.hideStatusBarMode(false, getResources().getColor(R.color.darkblue));
                } else if (tab.getPosition() == 1) {
                    mainActivity.hideStatusBarMode(false, getResources().getColor(R.color.darkblue));
                } else if (tab.getPosition() == 2) {
                    mainActivity.hideStatusBarMode(false, getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void fetchData(boolean refreshCoachInfo) {

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab, null);
        TextView title = view.findViewById(R.id.tv_tab);
        if (title.getVisibility() == View.VISIBLE) {
            title.setText(titles[position]);
        }
        ImageView icon = view.findViewById(R.id.iv_tab);
        icon.setImageResource(icons[position]);
        return view;
    }
}
