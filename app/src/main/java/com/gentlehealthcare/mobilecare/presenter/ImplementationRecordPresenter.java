package com.gentlehealthcare.mobilecare.presenter;

import android.view.View;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.model.IImplementationRecordModel;
import com.gentlehealthcare.mobilecare.model.impl.ImplementationRecordModel;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
import com.gentlehealthcare.mobilecare.view.IImplementationRecordInputView;
import com.gentlehealthcare.mobilecare.view.IImplementationRecordView;

import java.util.List;

/**
 * Created by Zyy on 2016/5/9.
 * 类说明：
 */
public class ImplementationRecordPresenter implements ImplementationRecordModel
        .OnLoadImplementationRecordListener, ImplementationRecordModel.LoadVariationDict,
        ImplementationRecordModel.LoadAppraislListener {
    private IImplementationRecordView iImplementationRecordView;
    private IImplementationRecordModel iImplementationRecordModel;
    private IImplementationRecordInputView iImplementationRecordInputView;

    public ImplementationRecordPresenter(IImplementationRecordView view, IImplementationRecordInputView arg1) {
        iImplementationRecordView = view;
        iImplementationRecordModel = new ImplementationRecordModel();
        iImplementationRecordInputView = arg1;
    }

    public void intial(View view) {
        iImplementationRecordView.intial(view);
    }

    public void receiveData() {
        iImplementationRecordView.receiveData();
    }

    public void loaddata(String patId, String planOrderNo) {
        iImplementationRecordView.showProgressDialog();
        iImplementationRecordModel.loadRecord(patId, planOrderNo, this);
    }

    public void saveRecord(String patID, String username, String planOrderNo, String status, String recTime, String
            skinResult, String exception, String exceptionCode, String appraise, String eventId) {
        iImplementationRecordModel.saveRecord(patID, username, planOrderNo, status, recTime, skinResult,
                exception, exceptionCode, appraise, eventId, new ImplementationRecordModel.SaveRecordListener() {
                    @Override
                    public void saveRecordSuccess(String msg) {
                        GlobalConstant.tempEventId = msg;
                        iImplementationRecordInputView.showToast("记录成功");
                    }

                    @Override
                    public void saveRecordFailure(String msg, Exception e) {
                        iImplementationRecordInputView.showToast("记录失败");
                    }
                });
    }

    public void loadVariationDict() {
        iImplementationRecordModel.loadVariationDict(this);
    }

    public void loadAppraisalDict() {
        iImplementationRecordModel.loadAppraislDict(this);
    }

    public void changeLayout(boolean flag) {
        iImplementationRecordView.changeLayout(flag);
    }

    public void showPsDialog() {
        iImplementationRecordInputView.showPsDialog();
    }

    @Override
    public void onSuccess(List<ImplementationRecordBean> list) {
        iImplementationRecordView.dismissProgressDialog();
        iImplementationRecordView.addRecord(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        iImplementationRecordView.dismissProgressDialog();
        iImplementationRecordView.showLoadFial();
    }

    @Override
    public void loadVariationDictSuccess(List<IntraDontExcuteBean> list) {
        iImplementationRecordInputView.showExceptionDialog(list);
    }

    @Override
    public void loadVariationDictFailure(String msg, Exception e) {

    }

    @Override
    public void loadAppraislSuccess(List<LoadAppraislRecordBean> list) {
        iImplementationRecordInputView.showAppraisalDialog(list);
    }

    @Override
    public void loadAppraislFailure(String msg, Exception e) {
        //iImplementationRecordInputView.showAppraisalDialog(list);
    }
}
