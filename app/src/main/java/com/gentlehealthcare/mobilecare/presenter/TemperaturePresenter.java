package com.gentlehealthcare.mobilecare.presenter;

import android.content.Context;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.ITemperatureModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.model.impl.TemperatureModel;
import com.gentlehealthcare.mobilecare.model.impl.TemperatureModel.SaveSignListener;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;

import java.util.List;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 体温单 presenter的控制层，完成交互
 */
public class TemperaturePresenter {

    private ITemperatureView view;
    private ITemperatureModel iTemperatureModel;
    private PatientModel patientModel;

    public TemperaturePresenter(ITemperatureView view) {
        this.view = view;
        iTemperatureModel = new TemperatureModel();
    }

    public void getPatients(Context context, final int position) {
        patientModel = new PatientModel();
        final List<SyncPatientBean> bean = UserInfo.getDeptPatient();
        if (bean.isEmpty() || position == 1024) {
            String str = GroupInfoSave.getInstance(context).get();
            SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
            syncDeptPatientBean.setWardCode(str);
            String url = UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString();
            view.showProgressDialog("病人加载成功");
            patientModel.syncDeptPatientTable(url, new PatientModel.OnLoadPatientListListener() {
                @Override
                public void onSuccess(List<SyncPatientBean> list) {
                    view.dismissProgressDialog();
                    if (CollectionsTool.isEmpty(list)) {
                        view.showToast("该病区暂无患者");
                    } else {
                        view.setPatientInfo(list.get(0));
                        view.setPatient(bean);
                    }
                }

                @Override
                public void onFailure(String msg, Exception e) {

                }
            });
        } else {
            view.setPatientInfo(bean.get(position));
        }
    }
    public void setPatients(SyncPatientBean syncPatientBean) {
        view.setPatientInfo(syncPatientBean);
    }
    public void showPatients() {
        view.showPatients();
    }

    public void setPatientInfo(SyncPatientBean patientInfo) {
        view.setPatientInfo(patientInfo);
    }

    public void showKey(String[] strs, int flag) {
        view.showKey(strs, flag);
    }

    public void initialSrc() {
        view.initialSrc();
    }

    public void saveTemperature(String patId, String patCode, String visitId, String time, String temperature, String temperatureType, String pulse, String heartRate, String breathing, String bloodPressure, String bloodSugar, String pain) {
        iTemperatureModel.saveData(patId, patCode, visitId, time, temperature, temperatureType, pulse, heartRate, breathing, bloodPressure, bloodSugar, pain, new SaveSignListener() {
            @Override
            public void onSuccess(String msg) {
                // TODO Auto-generated method stub
                view.showToast("保存成功");
            }

            @Override
            public void onFailure(String msg, Exception e) {
                // TODO Auto-generated method stub
                view.showToast("保存失败,请检查网络");
            }
        });
    }


}
