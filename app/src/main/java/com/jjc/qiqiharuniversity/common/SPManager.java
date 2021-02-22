package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: SharedPreference常用方法封装类
 */
public class SPManager {

    private static final String SP_NAME_DEFAULT = "CoachAI";

    private static volatile SPManager mInstance;
    private Map<String, SharedPreferences> mCache = new HashMap<>();

    public static SPManager getInstance() {
        if (mInstance == null) {
            synchronized (SPManager.class) {
                if (mInstance == null) {
                    mInstance = new SPManager();
                }
            }
        }
        return mInstance;
    }

    public String getString(Context context, String spName, String key, String defaultValue) {
        return getSPF(context, spName).getString(key, defaultValue);
    }

    //默认方法
    public String getString(Context context, String key, String defaultValue) {
        return getSPF(context).getString(key, defaultValue);
    }

    public void putString(Context context, String spName, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.putString(key, value);
        editor.apply();
    }

    //默认方法
    public void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    public void putJSONObject(Context context, String key, JSONObject value) {
        putString(context, key, value.toString());
    }

    public JSONObject getJSONObject(Context context, String key) {
        String string = getString(context, key, "");
        try {
            JSONObject jsonObject = new JSONObject(string);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //默认方法
    public boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSPF(context).getBoolean(key, defaultValue);
    }

    //默认方法
    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public long getLong(Context context, String key, long defaultValue) {
        return getSPF(context).getLong(key, defaultValue);
    }

    public int getInt(Context context, String key, int defaultValue) {
        return getSPF(context).getInt(key, defaultValue);
    }

    public void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        editor.apply();
    }

    public void remove(Context context, String spName, String key) {
        SharedPreferences.Editor editor = getEditor(context, spName);
        editor.remove(key);
        editor.apply();
    }

    public SharedPreferences getSPF(Context context, @NonNull String spName) {
        SharedPreferences spf;

        if (mCache.containsKey(spName)) {
            spf = mCache.get(spName);
            if (spf != null) {
                return spf;
            }
        }

        spf = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        mCache.put(spName, spf);
        return spf;
    }

    //默认方法
    public SharedPreferences getSPF(Context context) {
        SharedPreferences spf;

        if (mCache.containsKey(SP_NAME_DEFAULT)) {
            spf = mCache.get(SP_NAME_DEFAULT);
            if (spf != null) {
                return spf;
            }
        }

        spf = context.getSharedPreferences(SP_NAME_DEFAULT, Context.MODE_PRIVATE);
        mCache.put(SP_NAME_DEFAULT, spf);
        return spf;
    }

    public SharedPreferences.Editor getEditor(Context context, String spName) {
        return getSPF(context, spName).edit();
    }

    //默认方法
    public SharedPreferences.Editor getEditor(Context context) {
        return getSPF(context).edit();
    }

    public void setCountMax(Context context, String key, int maxValue) {
        SPManager.getInstance().putInt(context, key, maxValue);
    }

    public boolean isCountMax(Context context, String key, int maxValue) {
        if (getInt(context, key, 0) == maxValue) {
            return true;
        }
        return false;
    }

    public void setCount(Context context, String key, int value) {
        int times = getInt(context, key, 0) + value;
        putInt(context, key, times);
    }
}
