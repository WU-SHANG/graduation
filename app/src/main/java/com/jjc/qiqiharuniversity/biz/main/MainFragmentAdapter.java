package com.jjc.qiqiharuniversity.biz.main;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.jjc.qiqiharuniversity.biz.home.HomeFragment;
import com.jjc.qiqiharuniversity.biz.me.MineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    private String[] mTitleArray;

    public MainFragmentAdapter(FragmentManager fm, String[] titleArray) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MineFragment());
        mFragmentList.add(new MineFragment());
        mTitleArray = titleArray;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleArray[position];
    }
}
