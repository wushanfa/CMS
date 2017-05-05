package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author dzw
 */
public class LoadIcuBBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回时参数
     */
    private String itemNo;
    private String columnCode;
    private String classCode;
    private String columnName;
    private String className;
    private List<LoadIcuBResultBean> options;

    @Override
    public String toString() {
        return "";
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<LoadIcuBResultBean> getOptions() {
        return options;
    }

    public void setOptions(List<LoadIcuBResultBean> options) {
        this.options = options;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
}
