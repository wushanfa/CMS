package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.InsulinOverviewModel;

/**
 * Created by zhiwei on 2016/5/18.
 */
public interface IInsulinOverviewModel {

    void loadRecord(String patId, String planOrderNo, final InsulinOverviewModel.onLoadRecord listener);
}
