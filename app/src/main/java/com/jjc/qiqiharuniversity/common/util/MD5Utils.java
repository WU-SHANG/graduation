package com.jjc.qiqiharuniversity.common.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public class MD5Utils {

    /**
     * 32位MD5加密方法
     * 16位小写加密只需getMd5Value("xxx").substring(8, 24);即可
     */
    public static String getMd5Value(String sSecret) {
        try {
            if (TextUtils.isEmpty(sSecret)) {
                return "";
            }

            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuilder buf = new StringBuilder();
            byte[] b = bmd5.digest();// 加密
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMd5U32Value(String str) {
        String reStr = getMd5Value(str);
        if (!TextUtils.isEmpty(reStr)) {
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }

    /**
     * 方法1：生成随机数字和字母组合
     * @param length
     * @return
     */
    public static String getCharAndNumStr(int length) {
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            buf.append(charStr.charAt(index));
        }
        return buf.toString();
    }

}
