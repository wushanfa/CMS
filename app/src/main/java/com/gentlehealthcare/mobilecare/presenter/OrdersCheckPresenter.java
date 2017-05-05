package com.gentlehealthcare.mobilecare.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.IOrdersCheckModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.OrdersCheckModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.view.IOrdersCheckView;

import java.util.List;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 执行核对医嘱列表的 presenter的控制层，完成交互
 */
public class OrdersCheckPresenter
    implements
      PatientModel.OnLoadPatientListListener {

  private IOrdersCheckView view;
  private IOrdersCheckModel ordersCheckModel;
  private PatientModel patientModel;
  // private int refresh = 0;

  public OrdersCheckPresenter(IOrdersCheckView view) {
    this.view = view;
    ordersCheckModel = new OrdersCheckModel();
  }

  public void getPatients(Context context, int position) {
    patientModel = new PatientModel();
    // final List<SyncPatientBean> bean = UserInfo.getDeptPatient();
    // if (bean.isEmpty()) {
    // String str = GroupInfoSave.getInstance(context).get();
    // SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
    // syncDeptPatientBean.setWardCode(str);
    // String url = UrlConstant.GetSyncDeptPatient()
    // + syncDeptPatientBean.toString();
    // view.showProgressDialog();
    // patientModel.syncDeptPatientTable(url, this);
    // } else {
    // view.setPatient(bean);
    // view.setPatientInfo(bean.get(position));
    // }

    String str = GroupInfoSave.getInstance(context).get();
    SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
    syncDeptPatientBean.setWardCode(str);
    String url = UrlConstant.GetSyncDeptPatient()
        + syncDeptPatientBean.toString();
    view.showProgressDialog();
    patientModel.syncDeptPatientTable(url, this);
    // List<SyncPatientBean> bean = UserInfo.getDeptPatient();
    // view.setPatient(bean);
    // view.setPatientInfo(bean.get(position));
  }

  public void setPatients(SyncPatientBean syncPatientBean) {
    view.setPatientInfo(syncPatientBean);
  }

  public void findPatId(final String barCode, String userName, String wardCode) {
    view.showProgressDialog();
    ordersCheckModel.findPatId(barCode, userName, wardCode,
        new OrdersCheckModel.findPatIdListener() {
          @Override
          public void findPatIdSuccess(List<OrderListBean> list) {
            if (CollectionsTool.isEmpty(list)) {
              view.showMsg2(true, "该条码没有对应的患者");
            }else{
                view.findPatientByPatId(list);//如果找出两个病人怎么办？
            }
          }

          @Override
          public void findPatIdFailure(String msg, Exception e) {
            view.checkOrdersFailed();
            if ("no".equals(msg)) {
              view.showExceptionDialog("扫码异常", "扫描条码不符");
            } else {
              view.showExceptionDialog("扫码异常", "扫描条码不符");
            }
          }
        });
    view.dismissProgressDialog();
  }

  public void checkOrder(String wardCode, final String patId, String relatedBarcode,
                         String relatedBarcode2, String checkBy, String planOrderNo) {
    view.showProgressDialog();
    ordersCheckModel.checkOrder(wardCode, patId, relatedBarcode, relatedBarcode2, checkBy,
        planOrderNo, new OrdersCheckModel.checkOrderListener() {
          @Override
          public void checkOrderSuccess(String flag) {

            if (GlobalConstant.HAVE_BEEN_IMPLEMENTED.equals(flag)) {// 已执行
              view.showMsg2(true, "此医嘱已执行完毕");
            } else if (GlobalConstant.IMPLEMENTATION.equals(flag)) {// 执行中
              view.showMsg2(true, "此医嘱正在执行中");
            } else if (GlobalConstant.HAVE_CHECKED.equals(flag)) {// 已核对
              view.showMsg2(true, "此医嘱已核对");
            } else if (GlobalConstant.HAVE_BEEN_CANCEL.equals(flag)) {// 已取消
              view.showMsg2(true, "此医嘱已取消");
            } else if (GlobalConstant.SUCCEED.equals(flag)) {// 核对成功
              //如果核对成功，加载刚才核对的的病人的医嘱信息
              //view.loadCurrentPatientCheckOrders();
              view.loadCheckOrdersByPatId(patId);
            }
          }

          @Override
          public void checkOrderFailure(String msg, Exception e) {
            view.dismissProgressDialog();
            view.checkOrdersFailed();
            view.showToast("请检查医嘱信息后再核对");
          }
        });
  }

  public void loadOrders(String username, String patId, String planTime, String status) {
    GlobalConstant.isCurrentShowAllNotExecute = false;
    GlobalConstant.isLoadAllNotExecuteOrToday = false;
    view.showProgressDialog();
    ordersCheckModel.DoLoadOrder(username, patId, planTime,
        status, new OrdersCheckModel.OnLoadOrdersListListener() {

          @Override
          public void onOrderSuccess(List<OrderListBean> list) {
            view.setListData(list);
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

  public void commRec(String userName, String barCode, String deviceId) {
    IDocOrdersModel docOrdersModel = new DocOrdersModel();
    docOrdersModel.commRec(userName, barCode, deviceId);
  }

  public void receiveData() {
    view.receiveDate();
  }

  public void showFilterDialog() {
    view.showFilterButton();
  }

  public void showFilterText(String time, String status) {
    view.setFilterText(time, status);
  }

  @Override
  public void onSuccess(List<SyncPatientBean> list) {
    view.dismissProgressDialog();
    // 判断list是否为空
    if (CollectionsTool.isEmpty(list)) {
      view.showToast("该病区暂无患者");
    } else {
      view.setPatientInfo(list.get(0));
      view.setPatient(list);
    }

  }

  @Override
  public void onFailure(String msg, Exception e) {

  }

}
