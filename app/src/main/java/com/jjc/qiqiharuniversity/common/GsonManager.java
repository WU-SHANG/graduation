package com.jjc.qiqiharuniversity.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Author jiajingchao
 * Created on 2021/4/16
 * Description: Gson管理类
 */
public class GsonManager {

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> T fromJsonArray(String json, Type type) {
        try {
            return new Gson().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static String getAsString(JsonObject jsonObject, String key) {
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.has(key)) {
            if (jsonObject.get(key).getAsString() != null) {
                return jsonObject.get(key).getAsString();
            }
        }
        return null;
    }

    public static boolean getAsBoolean(JsonObject jsonObject, String key) {
        if (jsonObject == null) {
            return false;
        }
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsBoolean();
        }
        return false;
    }

    public static JsonObject getAsJsonObject(JsonObject jsonObject, String key) {
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.has(key)) {
            if (jsonObject.get(key).getAsJsonObject() != null) {
                return jsonObject.get(key).getAsJsonObject();
            }
        }
        return null;
    }

    public static <T> T fromJson(JsonElement json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
