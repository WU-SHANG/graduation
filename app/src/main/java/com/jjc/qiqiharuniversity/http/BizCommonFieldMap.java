package com.jjc.qiqiharuniversity.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public class BizCommonFieldMap {

    public static Map<String, String> buildCommonFieldMap() {
        Map<String, String> map = new HashMap<>();
//        put(map, BizHttpConstants.TOKEN, LoginController.getToken());
        return map;
    }

    public static void put(Map<String, String> map, String key, String value) {
        if (map == null) {
            return;
        }

        if (key == null) {
            return;
        }

        if (value == null) {
            value = "";
        }

        map.put(key, value);
    }

}
