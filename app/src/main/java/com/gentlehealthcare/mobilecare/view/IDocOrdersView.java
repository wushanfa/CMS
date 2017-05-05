package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表 view 的抽象类
 */
public interface IDocOrdersView {

    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();

    /**
     * 获取筛选 时间 频次 执行状态 流程 textView的内容
     */
    void setFilterText(String time, String frequency, String perform, String flow);


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
     * 显示病人列表
     */
    void showPatients();

    /**
     * 执行医嘱
     */
    void executeOrderSuccess(String result);

    /**
     * 获取部门病人数据
     */
    void setPatient(List<SyncPatientBean> list);

    void showToast(String msg);

    void intentTrans(List<BloodProductBean2> result, String bloodDonorCode, String scanCode);

    void setSelection(int position, String planOrderNo, String planOrderNoOrReqId);

    void receiveDate();

    void completeOnRefresh();

    void toMsgAct(List<TipBean> orders, String patCode);
    /**
     * 弹出计费提示框
     */
    void showBillDialog(String code, String cost, String count);
    /**
     * 批量执行医嘱
     */
    void batchExecuteOrderSuccess();

    /**
     * 扫码异常提示框
     */
    void showExceptionDialog(String title,String msg);
    /**
     * 提示框
     */
    void tostMsg(boolean isVibration, String msg);
}
