package com.jjc.qiqiharuniversity.common;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Author jiajingchao
 * Created on 2021/4/16
 * Description: 日期时间帮助类
 */
public class DateHelper {

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD = "yyyy年MM月dd日";
    public static final String FORMAT_YYYY_MM_DD_V2 = "yyyy-MM-dd";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_DD = "MM月dd日";

    @SuppressLint("SimpleDateFormat")
    public static String getStringDate(@NonNull String fromDate,
                                       @NonNull String fromFormat,
                                       @NonNull String toFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(fromFormat);
        try {
            Date date = formatter.parse(fromDate);
            formatter = new SimpleDateFormat(toFormat);
            return formatter.format(date);
        } catch (Exception e) {
            // Empty
        }

        return fromDate;
    }

    public static String minToHHMM(@NonNull String min) {
        if (ObjectHelper.isIllegal(min)) {
            return "";
        }

        try {
            int minInt = Integer.parseInt(min);
            int h = minInt / 60;
            int m = minInt % 60;

            if (h > 0 && m == 0) {
                return h + "小时";
            } else if (h == 0 && m > 0) {
                return m + "分钟";
            } else if (h > 0 && m > 0) {
                return h + "小时" + m + "分钟";
            } else {
                return "0";
            }

        } catch (NumberFormatException e) {
            return "";
        }
    }

    public static long lastYearTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTimeInMillis();
    }

    public static String getStringDateByTimestamp(long timestamp,
                                                  @NonNull String format) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = new Date(timestamp);
            return formatter.format(date);
        } catch (Exception e) {
            // Empty
        }

        return "";
    }

    public static long stringDateToTimestamp(String date, String format) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date d = formatter.parse(date);
            return d.getTime();
        } catch (Exception e) {
            // Empty
        }

        return 0;
    }

    public static long getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String transform(long time) {
        if (time != 0) {
            long min = (time) / 60;
            long second = (time) % 60;
            //shi< 10 ? ("0" + shi) : shi)判断时否大于10时的话就执行shi,否则执行括号中的
            return min + ":" + (second < 10 ? ("0" + second) : second);
        }
        return "0:00";
    }

    public static String getTimeBySecond(int second) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            return formatter.format(second);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
