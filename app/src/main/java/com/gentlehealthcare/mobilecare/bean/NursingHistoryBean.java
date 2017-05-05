package com.gentlehealthcare.mobilecare.bean;

import java.io.Serializable;

/**
 * Created by Zyy on 2016/8/3.
 * 类说明：对应cp_pat_nursing_rec
 */

public class NursingHistoryBean implements Serializable {


    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getNurseInOperate() {
        return nurseInOperate;
    }

    public void setNurseInOperate(String nurseInOperate) {
        this.nurseInOperate = nurseInOperate;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    String logTime ;
    String performDesc ;
    String nurseInOperate ;

}
