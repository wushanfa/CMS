package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhiwei on 2015/11/16.
 */
public class ICUResourceSave {
    private static final String name = "icuResource";
    private static SharedPreferences sp = null;

    public ICUResourceSave() {
    }

    public static ICUResourceSave getInstance(Context context) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return new ICUResourceSave();
    }

    /**
     * 先删除相同的key，然后保存
     *
     * @param map
     */
    public void save(Map<String, Object> map) {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = map.keySet();
        for (String s : set) {
            editor.remove(s);
            editor.putString(s, (String) map.get(s));
        }
        editor.commit();
    }

    /**
     * 先删除相同的key，然后保存
     *
     * @param map
     */
    public void saveStatus(Map<String, Object> map) {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = map.keySet();
        for (String s : set) {
            editor.remove(s);
            editor.putInt(s, (Integer) map.get(s));
        }
        editor.commit();
    }
    /**
     *
     * @param map
     */
    public void saveInt(Map<String, Object> map) {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = map.keySet();
        for (String s : set) {
            editor.remove(s);
            editor.putInt(s, (Integer) map.get(s));
        }
        editor.commit();
    }

    /**
     * 把list数组放入sharedPreferences
     *
     * @param key
     * @param list
     */
    public void save(String key, List<Map<String, Object>> list) {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            Set<String> set = map.keySet();
            JSONObject jsonObject = new JSONObject();
            for (String s : set) {
                try {
                    jsonObject.put(s, map.get(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, jsonArray.toString());
        editor.commit();
    }

    /**
     * 根据key获取某个值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return sp.getString(key, null);
    }

    /**
     * 根据key获取某个值
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    /**
     * 获得array数组
     *
     * @param key
     * @return
     */
    public List<Map<String, Object>> getArray(String key) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String result = sp.getString(key, null);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                JSONArray names = jsonObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = jsonObject.getString(name);
                        map.put(name, value);
                    }
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取所有的map
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 清除所有
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}
