package com.gentlehealthcare.mobilecare.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.view.IDocOrdersView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表的 presenter的控制层，完成交互
 */
public class DocOrdersPresenter
    implements
      PatientModel.OnLoadPatientListListener,
      DocOrdersModel.changeOrderStatusListener,
      DocOrdersModel.bantchExecuteListener,
      DocOrdersModel.loadAllNotExecuteListener {

  private IDocOrdersView view;
  private IDocOrdersModel docOrdersModel;
  private PatientModel patientModel;
  private int refresh = 0;

  public DocOrdersPresenter(IDocOrdersView view) {
    this.view = view;
    docOrdersModel = new DocOrdersModel();
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
      patientModel.syncDeptPatientTable(url, this);
    } else {
      if (bean.isEmpty()) {
        String str = GroupInfoSave.getInstance(context).get();
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        String url = UrlConstant.GetSyncDeptPatient()
            + syncDeptPatientBean.toString();
        view.showProgressDialog();
        patientModel.syncDeptPatientTable(url, this);
      } else {
        view.setPatient(bean);
        view.setPatientInfo(bean.get(position));
      }
    }

  }

  public void changeOrderStatus(String changeStatus, String planorderNo,
      String username, String patId, String planTime, String orderType,
      String status, String templateId, String orderClass,
      String resultFlag, int typeFlag) {
    view.showProgressDialog();
    docOrdersModel.changeOrderStatus(changeStatus, planorderNo, username,
        patId, planTime, orderType, status, templateId, orderClass,
        resultFlag, typeFlag, this);
  }

  public void setPatients(SyncPatientBean syncPatientBean) {
    view.setPatientInfo(syncPatientBean);
  }

  public void loadOrders(String username, String patId, String planTime,
      String orderType, String status, String templateId,
      final String orderClass, final String result, final int type) {
    GlobalConstant.isCurrentShowAllNotExecute = false;
    GlobalConstant.isLoadAllNotExecuteOrToday = false;
    view.showProgressDialog();
    docOrdersModel.DoLoadOrder(username, patId, planTime, orderType,
        status, templateId, orderClass,
        new DocOrdersModel.OnLoadOrdersListListener() {

          @Override
          public void onOrderSuccess(List<OrderListBean> list) {
            if (!TextUtils.isEmpty(GlobalConstant.BATCH_NO)) {
              view.setListData(findTestOrders(list));
            } else {
              view.setListData(list);
            }
            view.completeOnRefresh();
            view.dismissProgressDialog();
          }

          @Override
          public void onOrderFailure(String msg, Exception e) {
            view.dismissProgressDialog();
            if (TextUtils.equals(msg, "数据解析异常")) {
              view.showToast("数据解析异常");
            }
            view.completeOnRefresh();
          }
        });
  }

  public void loadExecutingOrders(String username, final String patCode, String wardCode) {
    docOrdersModel.DoLoadExecutingOrder(username, patCode, wardCode,
        new DocOrdersModel.OnLoadExecutingOrdersListListener() {
          @Override
          public void onOrderExecutingSuccess(List<TipBean> list) {
            view.toMsgAct(list, patCode);
          }

          @Override
          public void onOrderExecutingFailure(String msg, Exception e) {
            view.showToast("病人执行中医嘱加载失败");
          }
        });
  }

  public void receiveData() {
    view.receiveDate();
  }

  public void showFilterDialog() {
    view.showFilterButton();
  }

  public void showPatients() {
    view.showPatients();
  }

  public void showFilterText(String time, String type, String status,
      String orderClass) {
    view.setFilterText(time, type, status, orderClass);
  }

  public void patralRecord(String patId, String username) {
    docOrdersModel.patrolRecord(patId, username, new DocOrdersModel.patrolRecordListener() {
      @Override
      public void patrolRecordSuccess(String result, String msg) {

      }

      @Override
      public void patrolRecordFailed(String msg, Exception e) {
        view.showToast("巡视记录失败");
      }
    });
  }

  public void executeOrder(String username, String patId, String planId,
      String wardCode, String nurseInOperate, final String planOrderNo) {
    view.showProgressDialog();
    docOrdersModel.executionOrder(username, patId, planId, wardCode,
        nurseInOperate, planOrderNo,
        new DocOrdersModel.ExecuteOrderListener() {

          @Override
          public void onExecuteSuccess(String status) {
            if (status.equals("success")) {
              view.executeOrderSuccess(planOrderNo);
            } else {
              view.showToast("数据异常");
            }
          }

          @Override
          public void onExecuteFailure(String msg, Exception e) {
            view.showToast("执行失败");
            view.dismissProgressDialog();
          }
        });
  }

  public void batchExecuted(String username, String patId, String warCode,
      String planTime, String orderType, String status,
      String templateId, final String orderClass, final String result,
      final int type, List<OrderListBean> list) {
    view.showProgressDialog();
    String planorderNo = "";
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).isBatch()) {
        planorderNo += list.get(i).getPlanOrderNo() + "&planOrderNo=";
      }
    }
    docOrdersModel.batchExecuted(username, patId, warCode, planorderNo,
        planTime, orderType, status, templateId, orderClass, result,
        type, this);
  }

  public void loadAllNotExecute(String patid, String performStatus) {
    GlobalConstant.isLoadAllNotExecuteOrToday = true;
    view.showProgressDialog();
    docOrdersModel.loadAllNotExecute(patid, performStatus, this);
  }

  public void batchExecutedOnlyOne(String username, String patId,
      String warCode, String planTime, String orderType, String status,
      String templateId, final String orderClass, final String result,
      final int type, List<OrderListBean> list) {
    view.showProgressDialog();
    String planorderNo = "";
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).isBatch()) {
        planorderNo += list.get(i).getPlanOrderNo() + "&planOrderNo=";
      }
    }
    docOrdersModel.batchExecuted(username, patId, warCode, planorderNo,
        planTime, orderType, status, templateId, orderClass, result,
        type, this);
  }

  public List<OrderListBean> findTestOrders(List<OrderListBean> list) {
    List<OrderListBean> testordersList = new ArrayList<OrderListBean>();
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPlanBatchNo() != null
          && list.get(i).getPlanBatchNo()
              .equals(GlobalConstant.BATCH_NO)) {
        testordersList.add(list.get(i));
      }
    }
    GlobalConstant.BATCH_NO = null;
    return testordersList;
  }

  @Override
  public void onSuccess(List<SyncPatientBean> list) {
    view.dismissProgressDialog();
    if (CollectionsTool.isEmpty(list)) {
      view.showToast("该病区暂无患者");
    } else {
      if (refresh == 1000) {
        view.dismissProgressDialog();
        view.setPatientInfo(list.get(0));
        view.setPatient(list);
      } else {
        view.dismissProgressDialog();
        view.setPatientInfo(list.get(0));
        view.setPatient(list);
      }
    }
  }

  @Override
  public void onFailure(String msg, Exception e) {

  }

  public void getBloodDonorCode(String patId, final String scanCode, String planOrderNo) {
    view.showProgressDialog();
    docOrdersModel.getBloodDonorCode(patId, scanCode, planOrderNo,
        new DocOrdersModel.getBlood() {
          @Override
          public void onBloodSuccess(List<BloodProductBean2> result,
              String msg) {
            view.dismissProgressDialog();
            view.intentTrans(result, msg, scanCode);
          }

          @Override
          public void onBloodFail(String msg, Exception e) {
            view.dismissProgressDialog();
            view.showToast(msg);
          }
        });
  }

  public void callProcedureBill(String patId, String patCode, int visitId, String relatedBarCode,
      String userName) {
    docOrdersModel.callProcedureBill(patId, patCode, visitId, relatedBarCode, userName,
        new DocOrdersModel.CallProcedureBillListener() {
          @Override
          public void billSuccess(String code, String cost, String count) {
            view.showBillDialog(code, cost, count);
          }

          @Override
          public void billFailure(String code, String msg, Exception e) {
            view.showBillDialog(code, msg, null);
          }
        });
  }

  public void commRec(String userName, String barCode, String deviceId) {
    docOrdersModel.commRec(userName, barCode, deviceId);
  }

  @Override
  public void changeStatusSuccess(String msg, String planorderNo,
      String username, String patId, String planTime, String orderType,
      String status, String templateId, String orderClass,
      String resultFlag, int typeFlag) {
    if (msg.equals("success")) {
      view.dismissProgressDialog();
      loadOrders(UserInfo.getUserName(), patId, planTime, orderType,
          status, templateId, orderClass, resultFlag, typeFlag);
    }

  }

  @Override
  public void changeStatusFailure(String msg, Exception e) {
    view.dismissProgressDialog();
  }

  @Override
  public void bantchExecuteSuccess(String msg, String username, String patId,
      String planTime, String orderType, String status,
      String templateId, String orderClass, String resultFlag,
      int typeFlag) {
    view.dismissProgressDialog();
    view.batchExecuteOrderSuccess();
    loadOrders(username, patId, planTime, orderType, status, templateId,
        orderClass, resultFlag, typeFlag);
  }

  @Override
  public void bantchExecuteFailure(String msg, Exception e) {
    view.dismissProgressDialog();
    view.showToast("批量执行失败");
  }

  @Override
  public void loadAllNotExecuteSuccess(List<OrderListBean> list, String msg) {
    view.dismissProgressDialog();
    view.setListData(list);
  }

  @Override
  public void LoadAllNotExecuteFailed(String msg, Exception e) {
    view.dismissProgressDialog();
    view.showToast("网络异常");
  }
}
