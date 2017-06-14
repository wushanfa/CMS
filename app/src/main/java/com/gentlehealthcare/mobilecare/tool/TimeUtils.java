package com.gentlehealthcare.mobilecare.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class TimeUtils {
    //获取当前的系统时间YYYY-MM-dd HH:MI:SS (24小时制)
    public static String getCurrentTime(String currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd "+currentTime+":00", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
