package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

/**
 * Created by Zyy on 2016/5/16.
 * 类说明：
 */
public interface IIntravenousView {

    void setPatientInfo(SyncPatientBean patient);

    void setOrderContext(OrderListBean orderContext);

    void receiveData();
}
