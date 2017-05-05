package com.gentlehealthcare.mobilecare.bean;

import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;

import java.util.List;

/**
 * 完成给药实体类
 * Created by ouyang on 2015-04-29.
 */
public class MedicineListBean {
    private String groupname;
    private List<TB_MedicineInfo> list;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<TB_MedicineInfo> getList() {
        return list;
    }

    public void setList(List<TB_MedicineInfo> list) {
        this.list = list;
    }
}
