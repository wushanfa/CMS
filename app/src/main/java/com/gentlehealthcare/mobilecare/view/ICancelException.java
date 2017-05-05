package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by Zyy on 2016/6/15.
 * 类说明：取消执行接口
 */

public interface ICancelException {
    void receiveData();
    void setPatientInfo(SyncPatientBean syncPatientBean);
    void setOrdersContext(OrderListBean orderListBean);
    void showExceptionDic(List<IntraDontExcuteBean> list);
    void cancelExecutionCallBack(String msg);
    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();
    void showToast(String msg);
    void handException();
}
