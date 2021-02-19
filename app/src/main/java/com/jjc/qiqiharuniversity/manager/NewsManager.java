package com.jjc.qiqiharuniversity.manager;

import com.jjc.qiqiharuniversity.activity.MainActivity;
import com.jjc.qiqiharuniversity.adapter.NewsItemListAdapter;
import com.jjc.qiqiharuniversity.base.BaseBeanManager;
import com.jjc.qiqiharuniversity.bean.ListNewsResVO;
import com.jjc.qiqiharuniversity.bean.ListNewsVO;
import com.jjc.qiqiharuniversity.constant.ApiConfig;
import com.jjc.qiqiharuniversity.presenter.NewsPresenter;
import com.jjc.qiqiharuniversity.util.HttpProvider;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:
 */
public class NewsManager extends BaseBeanManager {

    public HashMap<String, ListNewsResVO> resVOHashMap = new HashMap<>();

    public NewsManager(ViewCallBack modelCallBack) {
        super(modelCallBack);
    }

    @Override
    public void getData() {
        ListNewsResVO resVO = resVOHashMap.get("resVO");
        if (resVO != null){
            getNewsList(resVO);
        } else {
            mViewCallBack.refreshView(0, null);
        }
    }

    /**
     * RxJava + Retrofit异步访问头条接口获取新闻列表
     * @param resVO
     */
    private void getNewsList(ListNewsResVO resVO) {
        HttpApi httpApi = HttpProvider.http(ApiConfig.BASE_URL).create(HttpApi.class);
        httpApi.newsList(resVO.getType(), resVO.getKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListNewsVO>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ListNewsVO resp) {
                        mViewCallBack.refreshView(1, resp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewCallBack.refreshView(0, null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
