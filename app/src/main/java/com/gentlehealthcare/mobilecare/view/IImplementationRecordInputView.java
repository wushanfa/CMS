package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/15.
 */
public interface IImplementationRecordInputView {


    void showToast(String msg);


    void showProgressDialog();

    void dismissProgressDialog();

    void showPsDialog();

    void showExceptionDialog(List<IntraDontExcuteBean> list);

    void showAppraisalDialog(List<LoadAppraislRecordBean> list);



}
