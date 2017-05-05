package com.gentlehealthcare.mobilecare.presenter;

import android.content.Context;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.bean.orders.DictCommonBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.NursingPatrolModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.view.INursingPatrolView;

import java.util.List;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 护理巡视 presenter的控制层
 */
public class NursingPatrolPresenter {

    private INursingPatrolView view;
    private NursingPatrolModel nursingPatrolModel;
    private PatientModel patientModel;
    private int refresh = 0;

    public NursingPatrolPresenter(INursingPatrolView view) {
        this.view = view;
        nursingPatrolModel = new NursingPatrolModel();
    }

    public void initalSrc(){
        view.initialSrc();
    }

    public void getPatrolDict(){
        nursingPatrolModel.getPatrolDict("u rl", new NursingPatrolModel.OnLoadPatrolDictListener() {
            @Override
            public void onSuccess(List<DictCommonBean> list) {
                view.setPatrolDict(list);
            }

            @Override
            public void onFailure(String msg, Exception e) {

            }
        });
    }

    public void commRec(String userName, String barCode, String deviceId) {
        IDocOrdersModel docOrdersModel=new DocOrdersModel();
        docOrdersModel.commRec(userName, barCode, deviceId);
    }

    public void getPatients(Context context, int position) {
        patientModel = new PatientModel();
        final List<SyncPatientBean> bean = UserInfo.getDeptPatient();
        if (position == 1000) {
            refresh = 1000;
            String str = GroupInfoSave.getInstance(context).get();
            SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
            syncDeptPatientBean.setWardCode(str);
            String url = UrlConstant.GetSyncDeptPatient()
                    + syncDeptPatientBean.toString();
            view.showProgressDialog();
            patientModel.syncDeptPatientTable(url, new PatientModel.OnLoadPatientListListener() {
                @Override
                public void onSuccess(List<SyncPatientBean> list) {
                    if (refresh == 1000) {
                        view.dismissProgressDialog();
                        view.setPatient(list);
                    } else {
                        view.dismissProgressDialog();
                        view.setPatientInfo(list.get(0));
                        view.setPatient(list);
                    }
                }

                @Override
                public void onFailure(String msg, Exception e) {

                }
            });
        } else {
            if (bean.isEmpty()) {
                String str = GroupInfoSave.getInstance(context).get();
                SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
                syncDeptPatientBean.setWardCode(str);
                String url = UrlConstant.GetSyncDeptPatient()
                        + syncDeptPatientBean.toString();
                view.showProgressDialog();
                patientModel.syncDeptPatientTable(url, new PatientModel.OnLoadPatientListListener() {
                    @Override
                    public void onSuccess(List<SyncPatientBean> list) {
                        if (refresh == 1000) {
                            view.dismissProgressDialog();
                            view.setPatient(list);
                        } else {
                            view.dismissProgressDialog();
                            view.setPatientInfo(list.get(0));
                            view.setPatient(list);
                        }
                    }

                    @Override
                    public void onFailure(String msg, Exception e) {

                    }
                });
            } else {
                view.setPatient(bean);
                view.setPatientInfo(bean.get(position));
            }
        }

    }


    public void setPatients(SyncPatientBean syncPatientBean) {
        view.setPatientInfo(syncPatientBean);
    }

    public void loadOrders(String username, String patId, String planTime,
                           String orderType, String status, String templateId,
                           final String orderClass) {
        nursingPatrolModel.DoLoadOrder(username, patId, planTime, orderType, status, templateId, orderClass, new NursingPatrolModel.OnLoadOrdersListListener() {
            @Override
            public void onOrderSuccess(List<OrderListBean> list) {
                view.setOrderContext(list);
            }

            @Override
            public void onOrderFailure(String msg, Exception e) {

            }
        });
    }

    public void showPatients() {
        view.showPatients();
    }


    public void saveNursingPatrol(String patId,String wardCode, String performType,String userName) {
        view.showProgressDialog();
        nursingPatrolModel.saveNursingPatrol(patId,wardCode, performType, userName, new NursingPatrolModel.SaveNursingPatrolListener() {
            @Override
            public void onSuccess(boolean msg) {
                view.dismissProgressDialog();
                if (msg){
                    view.showToast(GlobalConstant.SAVE_SUCCEED);
                }
            }

            @Override
            public void onFailure(String msg, Exception e) {
                view.dismissProgressDialog();
            }
        });
    }
}
