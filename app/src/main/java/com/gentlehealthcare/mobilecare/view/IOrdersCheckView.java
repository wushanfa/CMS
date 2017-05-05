package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表 view 的抽象类
 */
public interface IOrdersCheckView {

    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();

    /**
     * 获取筛选 时间  执行状态 textView的内容
     */
    void setFilterText(String time, String status);

    /**
     * 展示筛选按钮的展示框
     */
    void showFilterButton();

    /**
     * 设置病人的基本信息
     */
    void setPatientInfo(SyncPatientBean patientBean);

    /**
     * listView数据源
     */
    void setListData(List<OrderListBean> orderListBeans);

    /**
     * 获取部门病人数据
     */
    void setPatient(List<SyncPatientBean> list);

    void showToast(String msg);

    void setSelection(int position, String planOrderNo, String planOrderNoOrReqId);

    void receiveDate();

    void completeOnRefresh();

    void findPatientByPatId(List<OrderListBean> list);

    void loadCurrentPatientCheckOrders();

    void loadCheckOrdersByPatId(String patId);

    void checkOrdersFailed();
    /**
     * 扫码异常提示框
     */
    void showExceptionDialog(String title,String msg);

    void showMsg2(boolean flag,String msg);
}
