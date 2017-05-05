package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.IntravenousModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.view.IIntravenousExecuteView;
import com.gentlehealthcare.mobilecare.view.IIntravenousPatrolView;
import com.gentlehealthcare.mobilecare.view.IIntravenousSealingView;
import com.gentlehealthcare.mobilecare.view.IIntravenousView;

import java.util.List;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 静脉给药 presenter的控制层，完成交互
 */
public class IntravenousPresenter implements IntravenousModel.saveNextPatrolTimeListener, IntravenousModel
        .startIntravenousListener, IntravenousModel.saveIntravenousPatrolListener, IntravenousModel
        .saveIntravenousExceptionListener, IntravenousModel.loadVariationDictListener {
    private IIntravenousView iIntravenousView;
    private IIntravenousExecuteView intravenousExecuteView;
    private IIntravenousPatrolView intravenousPatrolView;
    private IIntravenousSealingView iIntravenousSealingView;
    private IntravenousModel intravenousModel;

    public IntravenousPresenter(IIntravenousView iIntravenousView, IIntravenousExecuteView view,
                                IIntravenousPatrolView intravenousPatrolView, IIntravenousSealingView
                                        iIntravenousSealingView) {
        this.iIntravenousView = iIntravenousView;
        this.intravenousExecuteView = view;
        this.intravenousPatrolView = intravenousPatrolView;
        this.iIntravenousView = iIntravenousView;
        this.iIntravenousSealingView = iIntravenousSealingView;
        intravenousModel = new IntravenousModel();
    }

    public void setPatientInfo(SyncPatientBean patient) {
        iIntravenousView.setPatientInfo(patient);
    }

    public void setOrderContext(OrderListBean orderListBean) {
        iIntravenousView.setOrderContext(orderListBean);
    }

    public void changeLayout() {
        intravenousPatrolView.changeLayout();
    }

    public void showDict(List<IntraDontExcuteBean> list) {
        intravenousPatrolView.showVariationDict(list);
    }

    public void loadVariationDict() {
        intravenousModel.loadVariationDict(this);
    }

    public void setSaveButtonbackGroud() {
        intravenousExecuteView.backToDoctorOrdersAct();
    }

    public void setNurseNameAndSealingTime(String name, String time, String dossage) {
        iIntravenousSealingView.setNurseNameAndSealingTime(name, time, dossage);
    }

    public void startIntravenous(String patId, String speed, String startTime, String injectionTool, String
            planorderno, String nextPatrolTime) {
        intravenousExecuteView.showProgressDialog();
        intravenousModel.startIntravenous(patId, speed, startTime, injectionTool, planorderno, nextPatrolTime,
                this);
    }

    public void saveNextPatrolTime(String patId, String username, String planOrderNo, String inspectionTime) {
        intravenousModel.saveNextPatrolTime(patId, username, planOrderNo, inspectionTime, this);
    }

    public void showSlideDateTimerPicker() {
        intravenousExecuteView.showSlideDateTimerPicker();
    }

    public void showSealSlideDateTimerPicker() {
        iIntravenousSealingView.showSlideDateTimerPicker();
    }

    public void showSlideDateTimerPickerInPatrol() {
        intravenousPatrolView.showSlideDateTimerPicker();
    }

    public void savePatrolInfo(String patId, String username, String planOrderNo, String msg, String
            dosage, String skinTestResult, int type) {
        intravenousPatrolView.showProgressDialog();
        intravenousModel.savePatrolInfo(patId, username, planOrderNo, msg, dosage, skinTestResult, type, this);
    }

    public void saveExceptionInfo(String patId, String username, String planOrderNo, String status, String
            completeDosage, String varianceCause, String varianceCauseDesc) {
        if (intravenousExecuteView != null) {
            intravenousExecuteView.showProgressDialog();
        } else if (iIntravenousSealingView != null) {
            iIntravenousSealingView.showProgressDialog();
        } else if (intravenousPatrolView != null) {
            intravenousPatrolView.showProgressDialog();
        }
        intravenousModel.saveExceptionInfo(patId, username, planOrderNo, status, completeDosage, varianceCause,
                varianceCauseDesc, this);
    }

    public void setExceptionText(String str) {
        intravenousPatrolView.setExceptionText(str);
    }

    public void showPSDialog() {
        intravenousPatrolView.showPiShi();
    }

    public void iIntravenousReceiveData() {
        iIntravenousView.receiveData();
    }

    public void setPatrolTime(String time) {
        intravenousPatrolView.setPatrolTime(time);
    }

    public void commRec(String userName, String barCode, String deviceId) {
        IDocOrdersModel docOrdersModel=new DocOrdersModel();
        docOrdersModel.commRec(userName, barCode, deviceId);
    }

    @Override
    public void saveSuccess(String msg) {

    }

    @Override
    public void saveFailure(String msg, Exception e) {

    }

    @Override
    public void startSuccess(String msg) {
        intravenousExecuteView.dismissProgressDialog();
        if (msg.equals("success")) {
            intravenousExecuteView.showToast("执行开始");
            setSaveButtonbackGroud();
            intravenousExecuteView.saveNextPatrolTime();
        } else {
            intravenousExecuteView.showToast("执行失败");
        }
    }

    @Override
    public void startFailure(String msg, Exception e) {
        intravenousExecuteView.dismissProgressDialog();
        intravenousExecuteView.handException();
    }

    @Override
    public void saveIntravenousExceptionSuccess(String msg) {
        if (intravenousExecuteView != null) {
            intravenousExecuteView.dismissProgressDialog();
        } else if (iIntravenousSealingView != null) {
            iIntravenousSealingView.dismissProgressDialog();
            iIntravenousSealingView.showToast("已结束");
            iIntravenousSealingView.finishActivity();
        } else if (intravenousPatrolView != null) {
            intravenousPatrolView.dismissProgressDialog();
            intravenousPatrolView.showToast("已结束");
            intravenousPatrolView.setSaveButtonbackGroud();
        }
    }

    @Override
    public void saveIntravenousExceptionFailure(String msg, Exception e) {
        if (intravenousExecuteView != null) {
            intravenousExecuteView.dismissProgressDialog();
            intravenousExecuteView.handException();
        } else if (iIntravenousSealingView != null) {
            iIntravenousSealingView.dismissProgressDialog();
            iIntravenousSealingView.handException();
        } else if (intravenousPatrolView != null) {
            intravenousPatrolView.dismissProgressDialog();
            intravenousPatrolView.handException();
        }
    }

    @Override
    public void saveIntravenousPatrolSuccess(String msg) {
        GlobalConstant.tempEventId = msg;
        intravenousPatrolView.dismissProgressDialog();
        intravenousPatrolView.showToast("巡视完成");
        intravenousPatrolView.setSaveButtonbackGroud();
    }

    @Override
    public void saveIntravenousPatrolFailure(String msg, Exception e) {
        intravenousPatrolView.dismissProgressDialog();
        intravenousPatrolView.handException();
    }

    @Override
    public void loadVariationDictSuccess(List<IntraDontExcuteBean> list) {
        intravenousPatrolView.addVariationDict(list);
    }

    @Override
    public void loadVariationDictFailure(String msg, Exception e) {
        intravenousPatrolView.handException();
    }
}
