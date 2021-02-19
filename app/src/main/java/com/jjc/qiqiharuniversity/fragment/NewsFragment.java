package com.jjc.qiqiharuniversity.fragment;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.activity.NewsDetailsActivity;
import com.jjc.qiqiharuniversity.adapter.NewsItemListAdapter;
import com.jjc.qiqiharuniversity.base.BaseFragment;
import com.jjc.qiqiharuniversity.bean.ListNewsResVO;
import com.jjc.qiqiharuniversity.bean.ListNewsVO;
import com.jjc.qiqiharuniversity.presenter.NewsPresenter;
import com.jjc.qiqiharuniversity.util.BannerLoader;
import com.jjc.qiqiharuniversity.view.ViewCallBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:新闻快讯模块
 */

public class NewsFragment extends BaseFragment<NewsPresenter, ListNewsVO> implements ViewCallBack<ListNewsVO> {

    private RecyclerView rv_news;
    private NewsItemListAdapter newsItemListAdapter;
    private ListNewsVO listNewsVO;

    private Banner banner;
    //定义图片集合
    private List<Integer> imgList = new ArrayList<>();
    private List<String> titleList = Arrays.asList("2021牛年大吉", "众志成城，战胜疫情", "齐齐哈尔大学冬日摄影");

    private RefreshLayout news_refresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initViews() {
        banner = view.findViewById(R.id.banner);
        imgList.add(R.drawable.banner_img1);
        imgList.add(R.drawable.banner_img2);
        imgList.add(R.drawable.banner_img3);
        //设置图片加载器
        banner.setImageLoader(new BannerLoader());
        //显示图片
        banner.setImages(imgList);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置页码与标题
        banner.setBannerTitles(titleList);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //启动
        banner.start();

        rv_news = view.findViewById(R.id.rv_news);
        rv_news.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listNewsVO = new ListNewsVO();
        ListNewsResVO resVO = new ListNewsResVO();
        resVO.setType("top");
        //今日头条api的key
        resVO.setKey(getResources().getString(R.string.my_toutiao_key));
        presenter.newsParamMap.put("resVO", resVO);
        presenter.getData();

        news_refresh = view.findViewById(R.id.news_refresh);
        //是否在刷新的时候禁止列表的操作
//        news_refresh.setDisableContentWhenRefresh(false);
    }

    @Override
    protected void initListener() {
        if (newsItemListAdapter != null) {
            newsItemListAdapter.setListener((view, position) -> {
                String detailsUrl = listNewsVO.getResult().getData().get(position).getUrl();
                startActivity(new Intent(view.getContext(), NewsDetailsActivity.class)
                        .putExtra("url", detailsUrl));
            });
        }
        if (listNewsVO != null) {
            //下拉刷新
            news_refresh.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    listNewsVO = null;
                    presenter.getData();
                    refreshlayout.finishRefresh(true);//传入false表示刷新失败
                }
            });
        }
    }

    @Override
    protected NewsPresenter initPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    public void refreshView(int code, ListNewsVO data) {
        if (code == 1) {
            listNewsVO = data;
            newsItemListAdapter = new NewsItemListAdapter(view.getContext(), data);
            newsItemListAdapter.notifyDataSetChanged();
            rv_news.setAdapter(newsItemListAdapter);
            initListener();
        } else {
            Toast.makeText(getContext(), "获取失败，请检查网络或服务器出错", Toast.LENGTH_SHORT).show();
        }
    }
}