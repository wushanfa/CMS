package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;

/**
 * Created by zhiwei on 2015/11/16.
 */
public class PutListBean implements Serializable {
    private String patternId;
    private String itemNo;

    public String getPatternId() {

        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
}
