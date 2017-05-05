package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhiwei on 2015/11/16.
 */
public class ICUDataMethod {

    /**
     * icu a
     */
    private static List<Map<String, Object>> saveICUABeanArray = new ArrayList<Map<String, Object>>();
    private static List<Map<String, Object>> updateICUABeanArray = new ArrayList<Map<String, Object>>();
    private static List<Map<String, Object>> searchICUABeanArray = new ArrayList<Map<String, Object>>();
    /**
     * icu b
     */
    private static List<Map<String, Object>> saveICUBBeanArray = new ArrayList<Map<String, Object>>();
    private static List<Map<String, Object>> updateICUBBeanArray = new ArrayList<Map<String, Object>>();
    private static List<Map<String, Object>> searchICUBBeanArray = new ArrayList<Map<String, Object>>();

    public static void changeStorageStatus(Context context, int status) {
        Map<String, Object> map = new HashMap<String, Object>();
        switch (status) {
            case 1:
                map.put("storageStatus", GlobalConstant.SAVE_CONDITION);
                ICUResourceSave.getInstance(context).saveStatus(map);
                break;
            case 2:
                map.put("storageStatus", GlobalConstant.UPDATE_CONDITION);
                ICUResourceSave.getInstance(context).saveStatus(map);
                break;
            case 3:
                map.put("storageStatus", GlobalConstant.DELETE_CONDITION);
                ICUResourceSave.getInstance(context).saveStatus(map);
                break;
            default:
                break;
        }
    }

    /**
     * 获得status
     *
     * @param context
     * @return
     */
    public static int getStorageStatus(Context context) {
        Integer status = (Integer) ICUResourceSave.getInstance(context).getInt("storageStatus");
        return status;
    }

    /**
     * 0,没有保存过formNo,1保存过
     */
    public static void saveFlag(Context context, int flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (flag != 0 && getFlag(context) == 0) {
            map.put("flag", flag);
            ICUResourceSave.getInstance(context).saveInt(map);
        }
    }

    /**
     *
     */
    public static int getFlag(Context context) {
        int flag = ICUResourceSave.getInstance(context).getInt("flag");
        return flag;
    }

    /**
     * create storage icu a's map
     *
     * @param itemNo
     * @param vitalSignsValues
     * @param memo
     * @param patternId
     * @return
     */
    public static Map<String, Object> getSaveICUA(String itemNo, String vitalSignsValues, String memo,
                                                  String patternId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemNo", itemNo);
        map.put("vitalSignsValues", vitalSignsValues);
        map.put("memo", memo);
        map.put("patternId", patternId);
        return map;
    }

    /**
     * create common icu b's Map
     *
     * @param itemNo
     * @param valueCode
     * @param valueName
     * @param valueType
     * @param abnormalAttr
     * @return
     */
    public static Map<String, Object> getICUB(String itemNo, String valueCode, String valueName, String
            valueType, String abnormalAttr, String valueDesc, String columnName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemNo", itemNo);
        map.put("valueCode", valueCode);
        map.put("valueName", valueName);
        map.put("valueType", valueType);
        map.put("abnormalAttr", abnormalAttr);
        map.put("valueDesc", valueDesc);
        map.put("columnName", columnName);
        return map;
    }

    /**
     * create common icu b's Map partly
     *
     * @param itemNo
     * @param valueCode
     * @param valueName
     * @param valueType
     * @param abnormalAttr
     * @return
     */
    public static Map<String, Object> getICUBPartlyForContent(String itemNo, String valueCode, String
            valueName, String valueType, String abnormalAttr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemNo", itemNo);
        map.put("valueCode", valueCode);
        map.put("valueName", valueName);
        map.put("valueType", valueType);
        map.put("abnormalAttr", abnormalAttr);
        return map;
    }

    /**
     * save a
     *
     * @param map
     */
    public static void addSaveA(Map<String, Object> map) {
        int i = 0;
        for (Map<String, Object> sub : saveICUABeanArray) {
            if (sub.get("itemNo").equals(map.get("itemNo")) && sub.get("patternId").equals(map.get("patternId"))) {
                saveICUABeanArray.remove(sub);
                break;
            } else {
                i++;
            }
        }
        saveICUABeanArray.add(i, map);
    }

    public static void removeSaveA(Map<String, Object> map) {
        saveICUABeanArray.remove(map);
    }

    public static List<Map<String, Object>> getSaveA() {
        return saveICUABeanArray;
    }

    public static void reStartSaveA() {
        saveICUABeanArray = new ArrayList<Map<String, Object>>();
    }

    /**
     * save b
     *
     * @param map
     */
    public static void addSaveB(Map<String, Object> map) {
        int i = 0;
        if (saveICUBBeanArray.isEmpty()) {
            for (Map<String, Object> sub : saveICUBBeanArray) {
                if (sub.get("itemNo").equals(map.get("itemNo"))) {
                    saveICUBBeanArray.remove(sub);
                    break;
                } else {
                    i++;
                }
            }
        }
        saveICUBBeanArray.add(i, map);
    }

    public static void removeSaveB(Map<String, Object> map) {
        saveICUBBeanArray.remove(map);
    }

    public static List<Map<String, Object>> getSaveB() {
        return saveICUBBeanArray;
    }

    public static void reStartSaveB() {
        saveICUBBeanArray = new ArrayList<Map<String, Object>>();
    }

    /**
     * update a
     *
     * @param map
     */
    public static void addUpdateA(Map<String, Object> map) {
        boolean caption = false;
        String newItemNo = (String) map.get("itemNo");
        String newPatternId = (String) map.get("patternId");
        for (int j = 0; j < updateICUABeanArray.size(); j++) {
            Map<String, Object> sub = updateICUABeanArray.get(j);
            String itemNo = (String) sub.get("itemNo");
            String patternId = (String) sub.get("patternId");
            if (itemNo.equals(newItemNo) && patternId.equals(newPatternId)) {
                updateICUABeanArray.remove(j);
                updateICUABeanArray.add(j, map);
                caption = true;
                break;
            }
        }
        if (!caption) {
            updateICUABeanArray.add(map);
        }
    }

    public static void removeUpdateA(Map<String, Object> map) {
        updateICUABeanArray.remove(map);
    }

    public static List<Map<String, Object>> getUpdateA() {
        return updateICUABeanArray;
    }

    public static void reStartUpdateA() {
        updateICUABeanArray = new ArrayList<Map<String, Object>>();
    }

    /**
     * update b
     *
     * @param map
     */
    public static void addUpdateB(Map<String, Object> map) {
        int i = 0;
        for (Map<String, Object> sub : updateICUBBeanArray) {
            if (sub.get("itemNo").equals(map.get("itemNo"))) {
                updateICUBBeanArray.remove(sub);
                break;
            } else {
                i++;
            }
        }
        updateICUBBeanArray.add(i, map);
    }

    public static void removeUpdateB(Map<String, Object> map) {
        updateICUBBeanArray.remove(map);
    }

    public static List<Map<String, Object>> getUpdateB() {
        return updateICUBBeanArray;
    }

    public static void reStartUpdateB() {
        updateICUBBeanArray = new ArrayList<Map<String, Object>>();
    }

    /**
     * search a
     *
     * @return
     */
    public static void addSearchA(Map<String, Object> map) {
        int i = 0;
        for (Map<String, Object> sub : searchICUABeanArray) {
            if (sub.get("itemNo").equals(map.get("itemNo"))) {
                searchICUABeanArray.remove(sub);
                break;
            } else {
                i++;
            }
        }
        searchICUABeanArray.add(i, map);
    }

    public static void removeSearchA(Map<String, Object> map) {
        searchICUABeanArray.remove(map);
    }

    public static List<Map<String, Object>> getSearchA() {
        return searchICUABeanArray;
    }

    public static void reStartSearchA() {
        searchICUABeanArray = new ArrayList<Map<String, Object>>();
    }

    /**
     * search b
     *
     * @param map
     */
    public static void addSearchB(Map<String, Object> map) {
        int i = 0;
        for (Map<String, Object> sub : searchICUBBeanArray) {
            if (sub.get("itemNo").equals(map.get("itemNo"))) {
                searchICUBBeanArray.remove(sub);
                break;
            } else {
                i++;
            }
        }
        searchICUBBeanArray.add(i, map);
    }

    public static void removeSearchB(Map<String, Object> map) {
        searchICUBBeanArray.remove(map);
    }

    public static List<Map<String, Object>> getSearchB() {
        return searchICUBBeanArray;
    }

    public static void reStartSearchB() {
        searchICUBBeanArray = new ArrayList<Map<String, Object>>();
    }
}
