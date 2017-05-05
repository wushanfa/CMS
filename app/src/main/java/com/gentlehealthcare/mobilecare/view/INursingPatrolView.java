package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.orders.DictCommonBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by Zyy on 2016/5/16.
 * 类说明：护理巡视视图层
 */
public interface INursingPatrolView {

    void setPatientInfo(SyncPatientBean patient);

    void setOrderContext(List<OrderListBean> orderContext);
    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();

    void receiveData();
    void initialSrc();
    void setPatrolDict(List<DictCommonBean> data);
    /**
     * 获取部门病人数据
     */
    void setPatient(List<SyncPatientBean> list);
    /**
     * 显示病人列表
     */
    void showPatients();

    void showToast(String msg);
}
