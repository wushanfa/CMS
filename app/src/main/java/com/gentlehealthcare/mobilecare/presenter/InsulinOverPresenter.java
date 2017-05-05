package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.model.IInsulinOverviewModel;
import com.gentlehealthcare.mobilecare.model.impl.InsulinOverviewModel;
import com.gentlehealthcare.mobilecare.view.IInsulinOverviewView;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/18.
 */
public class InsulinOverPresenter {

    private IInsulinOverviewView view;
    private IInsulinOverviewModel overviewModel;

    public InsulinOverPresenter(IInsulinOverviewView view) {
        this.view = view;
        overviewModel = new InsulinOverviewModel();
    }

    public void getRecord(String patId, String planOrderNo) {
        view.showProgressDialog();
        overviewModel.loadRecord(patId, planOrderNo, new InsulinOverviewModel.onLoadRecord() {
            @Override
            public void onSuccess(List<ImplementationRecordBean> list) {
                view.dismissProgressDialog();
                view.addRecord(list);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                view.dismissProgressDialog();
                view.showToast("加载记录异常");
            }
        });
    }
}
