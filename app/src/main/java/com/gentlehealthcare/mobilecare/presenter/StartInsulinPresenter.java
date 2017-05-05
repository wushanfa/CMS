package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.model.IStartInsulinModel;
import com.gentlehealthcare.mobilecare.model.impl.StartInsulinModel;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.view.IStartInsulinView;

/**
 * Created by zhiwei on 2016/5/16.
 */
public class StartInsulinPresenter {

    private IStartInsulinView startInsulinView;
    private IStartInsulinModel startInsulinModel;


    public StartInsulinPresenter(IStartInsulinView startInsulinView) {
        this.startInsulinView = startInsulinView;
        startInsulinModel = new StartInsulinModel();
    }

    public void startInsulin(String patId, String planOrderNo, String planId, int status, int itemNo, String
            siteId) {
        startInsulinView.showProgressDialog();
        startInsulinModel.startInsulin(patId, planOrderNo, planId, status, itemNo, siteId, new StartInsulinModel
                .startInsulinInterface() {
            @Override
            public void startSuccess() {
                startInsulinView.dismissProgressDialog();
                startInsulinView.finishActivity();
            }

            @Override
            public void startFail(String msg, Exception e) {
                startInsulinView.dismissProgressDialog();
                CCLog.e(msg + ";Exception:" + e.getMessage());
            }
        });
    }

    public void getBlood(String patId) {
        startInsulinView.showProgressDialog();
        startInsulinModel.getBlood(patId, new StartInsulinModel.getBloodInterface() {
            @Override
            public void getSuccess(String blood) {
                startInsulinView.dismissProgressDialog();
                startInsulinView.setBlood(blood);
            }

            @Override
            public void getFail(String msg, Exception e) {
                startInsulinView.dismissProgressDialog();
                CCLog.e(msg + ";Exception:" + e.getMessage());
            }
        });
    }

}
