package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.gentlehealthcare.mobilecare.constant.SharedPreferenceConstant;

/**
 * 数据操作
 */

public class SharedPreferenceUtils {

    //保存时间结点
    public static void saveTimePoint(Context context,String timePoint){
        SharedPreferences preferences = context.getSharedPreferences
                (SharedPreferenceConstant.TIME_POINT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TIME_POINT",timePoint);
        editor.apply();
    }

    //获取时间结点
    public static String getTimePoint(Context context){
        SharedPreferences preferences = context.getSharedPreferences
                (SharedPreferenceConstant.TIME_POINT,Context.MODE_PRIVATE);
        return preferences.getString("TIME_POINT","");
    }
}
