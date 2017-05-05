package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

/**
 * Created by ouyang on 2015/3/20.
 */
public class CheckMedicineBean {
    private int type = 0;//0 给药 1胰岛素
    private String patId;
    private String planOrderNo;//医嘱执行任务id
    private String relatedBarcode;//瓶签
    private String injectionTool;//注射针类型
    private String checkMode;//核对方式 manually手动
    private boolean result;
    private String msg;

    private String medicineId;
    private String siteCode;//区域代码
    private String siteNo;//

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

    @Override
    public String toString() {
        return "?username=" + UserInfo.getUserName() + "&siteCode=" + getSiteCode() + "&siteNo=" + siteNo + "&injectionTool=" + injectionTool + "&relatedBarcode=" + relatedBarcode + "&planOrderNo=" + planOrderNo + "&patId=" + getPatId() + "&checkMode=" + getCheckMode()+"&eventId="+getEventId();
    }

    public String getMedicineId() {
        return medicineId == null ? "" : medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSiteCode() {
        return siteCode == null ? "" : siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteNo() {
        return siteNo == null ? "" : siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }


    public String GetCodeByName(String name) {
        if (name.equals("左上臂"))
            return "A";
        else if (name.equals("右上臂"))
            return "B";
        else if (name.equals("左腹"))
            return "C";
        else if (name.equals("右腹"))
            return "D";
        else if (name.equals("左大腿"))
            return "E";
        else if (name.equals("右大腿"))
            return "F";
        else if (name.equals("左外臀"))
            return "G";
        else
            return "H";

    }


    public String getRelatedBarcode() {
        return relatedBarcode;
    }

    public void setRelatedBarcode(String relatedBarcode) {
        this.relatedBarcode = relatedBarcode;
    }

    public String getPlanOrderNo() {
        return planOrderNo;
    }

    public void setPlanOrderNo(String planOrderNo) {
        this.planOrderNo = planOrderNo;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getInjectionTool() {
        return injectionTool;
    }

    public void setInjectionTool(String injectionTool) {
        this.injectionTool = injectionTool;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCheckMode() {
        return checkMode;
    }

    public void setCheckMode(String checkMode) {
        this.checkMode = checkMode;
    }
}
