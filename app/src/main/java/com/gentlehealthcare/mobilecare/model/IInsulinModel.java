package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.InsulinModel;

/**
 * Created by zhiwei on 2016/5/14.
 */
public interface IInsulinModel {

    void getSite(String patId, InsulinModel.onLoadSite onLoadSite);

    void updateSite(String siteId, String patId, String status, String itemNo, String planId, InsulinModel.onUpdateSite
            onUpdateSite);

    void reloadInjection(String patId, InsulinModel.onReloadSite onReloadSite);
}
