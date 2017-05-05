package com.gentlehealthcare.mobilecare.tool;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhiwei on 2016/6/7.
 */

public class UrlTool {

    public static String transWord(String params) {
        return transWord(params, "utf-8");
    }

    public static String transWord(String params, String type) {
        String result = "";
        if (TextUtils.isEmpty(params)) {
            return result;
        } else {
            try {
               result= URLEncoder.encode(params, type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    public static String[] transWords(String... params) {
        String[] results = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            results[i] = transWord(params[i]);
        }
        return results;
    }
}
