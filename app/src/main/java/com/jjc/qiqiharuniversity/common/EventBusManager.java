package com.jjc.qiqiharuniversity.common;

import org.greenrobot.eventbus.EventBus;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: eventBus工具类
 */

public class EventBusManager {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    public static void removeSticky(Object event){
        EventBus.getDefault().removeStickyEvent(event);
    }

}
