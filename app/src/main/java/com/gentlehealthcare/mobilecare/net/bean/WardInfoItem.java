package com.gentlehealthcare.mobilecare.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author ouyang
 * @ClassName: WardInfoItem
 * @Description: 病房实体类
 * @date 2015年3月6日 下午2:14:59
 */
public class WardInfoItem {
    @SerializedName("ITEM_NAME")
    private String wardName;//病房名称
    @SerializedName("DEPT_CODE")
    private String wardCode;//病房编号

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }


}
