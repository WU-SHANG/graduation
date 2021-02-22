package com.jjc.qiqiharuniversity.biz.home.news;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;
import com.jjc.qiqiharuniversity.http.BizHttpConstants;
import com.jjc.qiqiharuniversity.http.BizHttpService;
import com.jjc.qiqiharuniversity.util.BannerLoader;
import com.jjc.qiqiharuniversity.util.HttpProvider;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:新闻快讯模块
 */

public class NewsFragment extends BaseFragment {

    private RecyclerView rvNews;
    private NewsItemListAdapter newsItemListAdapter;
    private ListNewsVO listNewsVO;
    private ListNewsResVO resVO;

    private Banner banner;
    // 轮播图列表
    private List<Integer> imgList = new ArrayList<>();
    private List<String> titleList = Arrays.asList("2021牛年大吉", "众志成城，战胜疫情", "齐齐哈尔大学冬日摄影");

    private RefreshLayout news_refresh;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        banner = view.findViewById(R.id.banner);
        imgList.add(R.drawable.banner_img1);
        imgList.add(R.drawable.banner_img2);
        imgList.add(R.drawable.banner_img3);
        // 设置图片加载器
        banner.setImageLoader(new BannerLoader());
        // 显示图片
        banner.setImages(imgList);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置页码与标题
        banner.setBannerTitles(titleList);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();

        rvNews = view.findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(view.getContext()));

        resVO = new ListNewsResVO();
        resVO.setType("top");
        // 今日头条api的key
        resVO.setKey(getResources().getString(R.string.my_toutiao_key));
        getNewsList(resVO);

        news_refresh = view.findViewById(R.id.news_refresh);
        // 是否在刷新的时候禁止列表的操作
        news_refresh.setDisableContentWhenRefresh(false);
    }

    protected void initListener() {
        if (newsItemListAdapter != null) {
            newsItemListAdapter.setListener((view, position) -> {
                String detailsUrl = listNewsVO.getResult().getData().get(position).getUrl();
                startActivity(new Intent(view.getContext(), NewsDetailsActivity.class)
                        .putExtra("url", detailsUrl));
            });
        }

        // 下拉刷新
        news_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                listNewsVO = null;
                getNewsList(resVO);
                refreshlayout.finishRefresh(true);//传入false表示刷新失败
            }
        });

    }

    /**
     * RxJava + Retrofit异步访问头条接口获取新闻列表
     * @param resVO
     */
    private void getNewsList(ListNewsResVO resVO) {
        BizHttpService httpService = HttpProvider.http(BizHttpConstants.JUHE_DATA_API).create(BizHttpService.class);
        httpService.newsList(resVO.getType(), resVO.getKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListNewsVO>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ListNewsVO resp) {
                        EventBusEvents.GetNewsListSuccessEvent event = new EventBusEvents.GetNewsListSuccessEvent();
                        event.listNewsVO = resp;
                        EventBusManager.post(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe
    public void onEvent(EventBusEvents.GetNewsListSuccessEvent event) {
        listNewsVO = event.listNewsVO;
        if (ObjectHelper.isIllegal(listNewsVO)) {
            ToastManager.show(getContext(), "获取失败，请检查网络或服务器出错");
            return;
        }
        newsItemListAdapter = new NewsItemListAdapter(getContext(), listNewsVO);
        newsItemListAdapter.notifyDataSetChanged();
        rvNews.setAdapter(newsItemListAdapter);
        initListener();
    }

}