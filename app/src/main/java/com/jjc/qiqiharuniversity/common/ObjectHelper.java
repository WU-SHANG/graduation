package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class ObjectHelper {

    public static boolean isIllegal(Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof CharSequence) {
            LogHelper.i("zhangyi","(CharSequence) object ： " + (CharSequence) object);
            return TextUtils.isEmpty((CharSequence) object);
        } else if (object instanceof List) {
            return ((List) object).size() == 0;
        }

        return false;
    }

    public static String format(@NonNull Context context, int resId, Object... args) {
        return String.format(context.getResources().getString(resId), args);
    }

    public static int strToInt(String value) {
        try {
            if (isIllegal(value)) {
                return 0;
            }

            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Object deepCopy(Object src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File[] concatFilesAll(List<File[]> rest) {

        int totalLength = 0;

        for (File[] array : rest) {
            totalLength += array.length;
        }

        File[] result = new File[totalLength];

        int offset = 0;

        for (File[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

}
