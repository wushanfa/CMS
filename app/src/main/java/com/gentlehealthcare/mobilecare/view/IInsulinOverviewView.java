package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/18.
 */
public interface IInsulinOverviewView {

    void showProgressDialog();

    void dismissProgressDialog();

    void addRecord(List<ImplementationRecordBean> beans);

    void showToast(String str);
}
