package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.model.impl.CancelExceptionModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.view.ICancelException;

import java.util.List;

/**
 * Created by Zyy on 2016/6/15.
 * 类说明：取消执行主导器
 */

public class CancelExecutionPresenter implements CancelExceptionModel.loadVariationDictListener, CancelExceptionModel.CancelExecutionListener {
    private ICancelException view = null;
    private CancelExceptionModel cancelExceptionModel = null;

    public CancelExecutionPresenter(ICancelException v) {
        this.view = v;
        cancelExceptionModel = new CancelExceptionModel();
    }

    public void receiveData() {
        view.receiveData();
    }

    public void setPatientInfo(SyncPatientBean patientInfo) {
        view.setPatientInfo(patientInfo);
    }

    public void setOrderContext(OrderListBean orderContext) {
        view.setOrdersContext(orderContext);
    }

    public void loadVariationDict() {
        cancelExceptionModel.loadVariationDict(this);
    }

    public void cancelExecution(String username, String warCode, String orderId, String planOrderNo, String performResult, String varianceCause, String varianceCauseDesc, String patId) {
        view.showProgressDialog();
        cancelExceptionModel.cancelExecution(username, warCode, orderId, planOrderNo, performResult, varianceCause, varianceCauseDesc, patId, this);
    }

    @Override
    public void loadVariationDictSuccess(List<IntraDontExcuteBean> list) {
        view.showExceptionDic(list);
    }

    @Override
    public void loadVariationDictFailure(String msg, Exception e) {

    }

    @Override
    public void CancelExecutionSuccess(String msg) {
        view.dismissProgressDialog();
        view.cancelExecutionCallBack(msg);
    }

    @Override
    public void CancelExecutionFailure(String msg, Exception e) {
        view.dismissProgressDialog();
        view.cancelExecutionCallBack(msg);
    }
}
