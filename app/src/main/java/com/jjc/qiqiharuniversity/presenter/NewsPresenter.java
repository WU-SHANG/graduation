package com.jjc.qiqiharuniversity.presenter;


import com.jjc.qiqiharuniversity.base.BasePresenter;
import com.jjc.qiqiharuniversity.bean.ListNewsResVO;
import com.jjc.qiqiharuniversity.manager.NewsManager;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

import java.util.HashMap;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */

public class NewsPresenter extends BasePresenter {

    public NewsPresenter(ViewCallBack viewCallBack) {
        super(viewCallBack);
    }

    public HashMap<String, ListNewsResVO> newsParamMap = new HashMap<>();

    public void getData() {
        mBeanManager = new NewsManager(mViewCallBack);
        ((NewsManager) mBeanManager).resVOHashMap = newsParamMap;
        mBeanManager.getData();
    }
}
