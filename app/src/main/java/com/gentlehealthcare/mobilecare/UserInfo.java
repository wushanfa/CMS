package com.gentlehealthcare.mobilecare;

import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SystemAttentionInfo;
import com.gentlehealthcare.mobilecare.net.bean.WardInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息数据绑定
 */
public class UserInfo {
    private static String userName;// 登录帐号
    private static String name;//护理真实姓名
    private static List<SystemAttentionInfo> attentions;// 注意事项
    private static String wardCode;// 病房编号
    private static String key;//密钥KEY
    private static List<WardInfoItem> wardList;//病房列表
    private static String deptName;//部门名称
    private static String capability;//用户等级

    private static List<SyncPatientBean> myPatient = new ArrayList<SyncPatientBean>();//我的病人
    private static List<SyncPatientBean> deptPatient = new ArrayList<SyncPatientBean>();//部门病人

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static List<SystemAttentionInfo> getAttentions() {
        return attentions;
    }

    public static void setAttentions(List<SystemAttentionInfo> attentions) {
        UserInfo.attentions = attentions;
    }

    public static String getWardCode() {
        return wardCode;
    }

    public static void setWardCode(String wardCode) {
        UserInfo.wardCode = wardCode;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserInfo.name = name;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        UserInfo.key = key;
    }

    public static List<WardInfoItem> getWardList() {
        return wardList;
    }

    public static void setWardList(List<WardInfoItem> wardList) {
        UserInfo.wardList = wardList;
    }

    public static String getDeptName() {
        return deptName == null ? "" : deptName;
    }

    public static String getCapability() {
        return capability;
    }

    public static void setCapability(String capability) {
        UserInfo.capability = capability;
    }

    public static void setDeptName(String deptName) {
        UserInfo.deptName = deptName;
    }

    public static List<SyncPatientBean> getMyPatient() {
        return myPatient;
    }

    public static void setMyPatient(List<SyncPatientBean> myPatient) {
        UserInfo.myPatient.clear();
        if (myPatient != null && myPatient.size() > 0)
            UserInfo.myPatient.addAll(myPatient);
    }

    public static List<SyncPatientBean> getDeptPatient() {
        return deptPatient;
    }

    public static void setDeptPatient(List<SyncPatientBean> deptPatient) {
        UserInfo.deptPatient.clear();
        if (deptPatient != null && deptPatient.size() > 0)
            UserInfo.deptPatient.addAll(deptPatient);
    }
}
