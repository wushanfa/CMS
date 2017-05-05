package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by zyy on 2016/5/26.
 */
public class LocalSave {
    private static final String PREFERENCE_NAME = "local";
    private static SharedPreferences sp = null;

    public static LocalSave getInstance(Context context) {
        sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new LocalSave();
    }

    public void save(Map<String, String> map) {
        if (map == null || map.keySet().size() <= 0)
            return;
        SharedPreferences.Editor editor = sp.edit();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }

    public Map<String, String> get() {
        return (Map<String, String>) sp.getAll();
    }

    public static boolean saveStringData(Context context, String key, String value) {
        if (context == null || key == null || value == null) {
            return false;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, AESCrypt.encrypt(value));
        return editor.commit();
    }

    public static boolean saveBooleanData(Context context, String key, boolean value) {
        if (context == null || key == null) {
            return false;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBooleanData(Context context, String key) {
        boolean value = false;
        if (context == null || key == null) {
            return value;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        value = sp.getBoolean(key, false);
        return value;
    }

    public static String getStringData(Context context, String key) {
        String value = null;
        if (context == null || key == null) {
            return value;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        if (sp.getString(key, null) != null) {
            value = AESCrypt.decrypt(sp.getString(key, null));
        }
        return value;

    }

    public static boolean deleteData(Context context, String key) {
        if (context == null || key == null) {
            return false;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean clearAllData(Context context) {
        if (context == null) {
            return false;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        return editor.clear().commit();

    }
    
    /**
     * 保存List
     * @param key
     * @param datalist
     */
    public static <T> boolean setDataList(Context context,String key, List<T> datalist) {
        if (context == null || key == null) {
            return false;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(key, AESCrypt.encrypt(strJson));
        return editor.commit();

    }

    /**
     * 获取List
     * @param key
     * @return
     */
    public static <T> List<T> getDataList(Context context,String key) {
        List<T> dataList=new ArrayList<T>();
        String value = null;
        if (context == null || key == null) {
            return dataList;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        if (sp.getString(key, null) != null) {
            value = AESCrypt.decrypt(sp.getString(key, null));
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(value, new TypeToken<List<BarcodeDict>>() {
        }.getType());
        return dataList;
    }

}
