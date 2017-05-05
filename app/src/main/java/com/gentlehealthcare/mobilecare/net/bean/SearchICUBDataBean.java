package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiwei on 2016/1/6.
 */
public class SearchICUBDataBean implements Serializable {
    private int rowNo;
    private int rowNo2;
    private boolean isSave;
    private List<SearchICUBBean> icuList;

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getRowNo2() {
        return rowNo2;
    }

    public void setRowNo2(int rowNo2) {
        this.rowNo2 = rowNo2;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public List<SearchICUBBean> getIcuList() {
        return icuList;
    }

    public void setIcuList(List<SearchICUBBean> icuList) {
        this.icuList = icuList;
    }
}
