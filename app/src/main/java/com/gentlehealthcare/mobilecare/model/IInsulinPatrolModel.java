package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.InsulinPatrolModel;

/**
 * Created by zhiwei on 2016/5/17.
 */
public interface IInsulinPatrolModel {

    void uploadResult(String patId, String planOrderNo, String desc, InsulinPatrolModel.onUpLoadResult
            onUpLoadResult);
}
