package com.jjc.qiqiharuniversity.common;

import com.jjc.qiqiharuniversity.biz.home.news.ListNewsVO;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class EventBusEvents {

    public static class LoginSuccessEvent {

    }

    public static class LogoutEvent {

    }

    /**
     * 是否展示loading
     */
    public static class LoadingEvent {
        public boolean isLoading;
    }

    public static class GetNewsListSuccessEvent {
        public ListNewsVO listNewsVO;
    }
}
