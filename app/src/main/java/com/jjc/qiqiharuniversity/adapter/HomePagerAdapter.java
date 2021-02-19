package com.jjc.qiqiharuniversity.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jjc.qiqiharuniversity.fragment.NewsFragment;
/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:首页的不同模块页的适配器
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    //fragment的数量
    int nNumOfTabs;
    public HomePagerAdapter(FragmentManager fm, int nNumOfTabs)
    {
        super(fm);
        this.nNumOfTabs=nNumOfTabs;
    }

    /**
     * 重写getItem方法
     *
     * @param position 指定的位置
     * @return 特定的Fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                NewsFragment tab1=new NewsFragment();
                return tab1;
            case 1:
                NewsFragment tab2=new NewsFragment();
                return tab2;
            case 2:
                NewsFragment tab3=new NewsFragment();
                return tab3;
            case 3:
                NewsFragment tab4=new NewsFragment();
                return tab4;
            case 4:
                NewsFragment tab5=new NewsFragment();
                return tab5;
            case 5:
                NewsFragment tab6=new NewsFragment();
                return tab6;
            case 6:
                NewsFragment tab7=new NewsFragment();
                return tab7;
            case 7:
                NewsFragment tab8=new NewsFragment();
                return tab8;
        }
        return null;
    }

    /**
     * 重写getCount方法
     *
     * @return fragment的数量
     */
    @Override
    public int getCount() {
        return nNumOfTabs;
    }


}
