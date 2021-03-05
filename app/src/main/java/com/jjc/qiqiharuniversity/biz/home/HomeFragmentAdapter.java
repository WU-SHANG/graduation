package com.jjc.qiqiharuniversity.biz.home;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jjc.qiqiharuniversity.biz.home.news.NewsFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolCultureFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolInstitutionsFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolNewsFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolNoticeFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolPhotographyFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolProfileFragment;
import com.jjc.qiqiharuniversity.biz.home.school.SchoolWebAPNFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:首页的不同模块页的适配器
 */
public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    private String[] mTitleArray;

    public HomeFragmentAdapter(FragmentManager fm, String[] titleArray)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NewsFragment());
        mFragmentList.add(new SchoolNoticeFragment());
        mFragmentList.add(new SchoolWebAPNFragment());
        mFragmentList.add(new SchoolNewsFragment());
        mFragmentList.add(new SchoolProfileFragment());
        mFragmentList.add(new SchoolCultureFragment());
        mFragmentList.add(new SchoolInstitutionsFragment());
        mFragmentList.add(new SchoolPhotographyFragment());
        mTitleArray = titleArray;
    }

    /**
     * 重写getItem方法
     *
     * @param position 指定的位置
     * @return 特定的Fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * 重写getCount方法
     *
     * @return fragment的数量
     */
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
