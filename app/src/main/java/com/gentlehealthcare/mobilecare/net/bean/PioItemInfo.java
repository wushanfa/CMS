package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by ouyang on 15/7/3.
 */
public class PioItemInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2L;

    private String pioId;
    private Integer itemNo;
    private String itemClass;
    private String itemCode;
    private String itemName;
    private String relatedItemCode;
    private String pioClass;
    private String itemAlias;
    private String patId;
    private String patCode;
    private String visitId;
    private String problemCode;
    private String problemName;
    private String logBy;
    private String logTime1;
    private String logBy2;
    private String logTime2;
    private boolean isChecked;

    public String getPioId() {
        return pioId;
    }

    public void setPioId(String pioId) {
        this.pioId = pioId;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRelatedItemCode() {
        return relatedItemCode;
    }

    public void setRelatedItemCode(String relatedItemCode) {
        this.relatedItemCode = relatedItemCode;
    }

    public String getPioClass() {
        return pioClass;
    }

    public void setPioClass(String pioClass) {
        this.pioClass = pioClass;
    }

    public String getItemAlias() {
        return itemAlias;
    }

    public void setItemAlias(String itemAlias) {
        this.itemAlias = itemAlias;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getLogBy() {
        return logBy;
    }

    public void setLogBy(String logBy) {
        this.logBy = logBy;
    }

    public String getLogTime1() {
        return logTime1;
    }

    public void setLogTime1(String logTime1) {
        this.logTime1 = logTime1;
    }

    public String getLogBy2() {
        return logBy2;
    }

    public void setLogBy2(String logBy2) {
        this.logBy2 = logBy2;
    }

    public String getLogTime2() {
        return logTime2;
    }

    public void setLogTime2(String logTime2) {
        this.logTime2 = logTime2;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
