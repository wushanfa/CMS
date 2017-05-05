package com.gentlehealthcare.mobilecare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.bloodbagnuclearcharge.BloodBagNuclearChargeAct;
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.home.AddPioRecActivity;
import com.gentlehealthcare.mobilecare.activity.home.DoctorOrdersAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.home.OrdersActivity;
import com.gentlehealthcare.mobilecare.activity.home.PIOActivity;
import com.gentlehealthcare.mobilecare.activity.home.PioRecordsActivity;
import com.gentlehealthcare.mobilecare.activity.home.ThreeTestActivity;
import com.gentlehealthcare.mobilecare.activity.intravenous.IntravenousAct;
import com.gentlehealthcare.mobilecare.activity.notification.StatusScanningAct;
import com.gentlehealthcare.mobilecare.activity.orders.DocOrdersActivity;
import com.gentlehealthcare.mobilecare.activity.orders.OrdersCheckActivity;
import com.gentlehealthcare.mobilecare.activity.patient.PatientAct;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 红外扫描广播监听 Created by zyy on 16/6/13.
 */
public class CameraDevicerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (ActivityControlTool.currentActivity instanceof BaseActivity
//                && intent.getAction().equalsIgnoreCase("df.scanservice.result")) {
//            String result = intent.getStringExtra("result");
        if (ActivityControlTool.currentActivity instanceof BaseActivity) {
            String result = intent.getStringExtra("data");
            if (result.contains("REG")) {
                result = result.substring(3);
            }
            result.trim();
            List<SyncPatientBean> syncPatientBeans = UserInfo.getMyPatient();
            SyncPatientBean patientBean = new SyncPatientBean();
            for (SyncPatientBean syncPatientBean : syncPatientBeans) {
                if (syncPatientBean.getPatCode().equals(result)) {
                    patientBean = syncPatientBean;
                    break;
                }
            }
            if (patientBean != null) {
                syncPatientBeans = UserInfo.getDeptPatient();
                for (SyncPatientBean syncPatientBean : syncPatientBeans) {
                    if (syncPatientBean.getPatCode().equals(result)) {
                        patientBean = syncPatientBean;
                        break;
                    }
                }
            }
            if (patientBean != null) {
                if (ActivityControlTool.currentActivity instanceof FlowAct) {
                    // 在给药模块，扫描患者身份
                    ((FlowAct) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                } else if (ActivityControlTool.currentActivity instanceof InsulinFlowAct) {
                    // 在胰岛素模块，扫描患者身份
                    ((InsulinFlowAct) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    // 输血模块，扫描患者身份
                } else if (ActivityControlTool.currentActivity instanceof TransfusionActivity) {
                    ((TransfusionActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                } else if (ActivityControlTool.currentActivity instanceof PIOActivity) {
                    ((PIOActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    CCLog.i("PIOActivity界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof PioRecordsActivity) {
                    CCLog.i("PioRecordsActivity界面扫描值>>>>>", result);
                    ((PioRecordsActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                } else if (ActivityControlTool.currentActivity instanceof ThreeTestActivity) {
                    ((ThreeTestActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    CCLog.i("ThreeTestActivity界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof DoctorOrdersAct) {
                    ((DoctorOrdersAct) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    CCLog.i("DoctorOrdersFra界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof AddPioRecActivity) {
                    ((AddPioRecActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    CCLog.i("AddPioRecActivity界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof OrdersActivity) {
                    ((OrdersActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result);
                    CCLog.i("OrdersActivity界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof BloodSugarActivity) {
                    ((BloodSugarActivity) ActivityControlTool.currentActivity)
                            .DoCameraResult(result.trim());
                    CCLog.i("BloodSugarActivity界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof StatusScanningAct) {
                    ((StatusScanningAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("StatusScanningAct界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof PatientAct) {
                    ((PatientAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("PatientAct界面扫描值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof DocOrdersActivity) {
                    if (StringTool.numberOfChar(result, "*") == 3) {
                        String[] patBarCode = result.split("\\*");
                        String strPatBarCode = patBarCode[0] + "*" + patBarCode[1] + "*";
                        ((DocOrdersActivity) ActivityControlTool.currentActivity).DoCameraResult(strPatBarCode);
                        CCLog.i("DocOrdersActivity界面扫描值>>>>>", strPatBarCode);
                    } else {
                        ((DocOrdersActivity) ActivityControlTool.currentActivity).DoCameraResult(result);
                        CCLog.i("DocOrdersActivity界面扫描值>>>>>", result);
                    }
                } else if (ActivityControlTool.currentActivity instanceof OrdersCheckActivity) {
                    ((OrdersCheckActivity) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("OrdersCheckActivity扫描的值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof IntravenousAct) {
                    ((IntravenousAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("IntravenousAct扫描的值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof HomeAct) {
                    ((HomeAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("HomeAct扫描的值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof BloodBagNuclearChargeAct) {
                    ((BloodBagNuclearChargeAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("BloodBagNuclearChargeAct扫描的值>>>>>", result);
                } else if (ActivityControlTool.currentActivity instanceof PatrolAct) {
                    ((PatrolAct) ActivityControlTool.currentActivity).DoCameraResult(result);
                    CCLog.i("PatrolAct扫描的值>>>>>", result);
                } else {
                    Toast.makeText(context, R.string.standardoperation, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        }
    }

    /**
     * 匹配条码规则
     */
    public boolean partternCode(String strMatch, String strPattern) {
        if (strPattern != null) {
            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(strMatch);
            boolean b = m.matches();
            if (b) {
                return true;
            } else {
                return false;
            }
        } else {
            CCLog.i("血袋码编码规则错误");
            return false;
        }


    }
}
