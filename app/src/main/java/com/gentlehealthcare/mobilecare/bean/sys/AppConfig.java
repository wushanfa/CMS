package com.gentlehealthcare.mobilecare.bean.sys;

import java.io.Serializable;

/**
 * Created by Zyy on 2016/9/22.
 * 类说明：app配置数据
 */

public class AppConfig implements Serializable{
   private String  appName;
   private String  fileName;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String KEY) {
        this.key = KEY;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private String  section;
   private String  key;
   private String  value;
   private String  dbUser;
   private String  memo;
}
