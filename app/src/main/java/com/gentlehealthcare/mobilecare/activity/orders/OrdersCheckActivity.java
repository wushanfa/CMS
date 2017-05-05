package com.gentlehealthcare.mobilecare.activity.orders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.cancelexecution.CancelExecutionAct;
import com.gentlehealthcare.mobilecare.adapter.OrdersCheckAdapter;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.common.MyToast;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.OrdersCheckPresenter;
import com.gentlehealthcare.mobilecare.swipe.view.XListView;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtnNoicon;
import com.gentlehealthcare.mobilecare.view.IOrdersCheckView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zyy on 2016/5/5.
 */
public class OrdersCheckActivity extends BaseActivity
    implements
      IOrdersCheckView,
      OnClickListener,
      AdapterView.OnItemClickListener {

  /**
   * 医嘱核对界面
   */
  OrdersCheckPresenter presenter;
  @ViewInject(R.id.btn_back)
  private Button btn_back;
  @ViewInject(R.id.tv_time_status)
  private TextView tv_time_status;
  @ViewInject(R.id.tv_pat_name)
  private TextView tv_pat_name;
  @ViewInject(R.id.tv_sx)
  private TextView tv_sx;
  @ViewInject(R.id.tv_patient)
  private TextView tv_patient;
  @ViewInject(R.id.btn_menu)
  private Button btn_menu;
  @ViewInject(R.id.btn_execute)
  private RelativeLayout rl_execute;
  private RadioGroup rg_time;
  private RadioGroup rg_status;

  private RadioButton rb_today;
  private RadioButton rb_tomorry;
  private RadioButton rb_yestady;

  private RadioButton rb_status_all;

  private RadioButton rb_time_all;

  private RadioButton rb_havachecked;
  private RadioButton rb_notchecked;
  private RadioButton rb_cancel;

  private Button btn_all_not_execute;
  private Button btn_refresh;
  private Button btn_filter;
  @ViewInject(R.id.ll_sex_bed_patcode)
  private LinearLayout ll_sex_bed_patcode;
  private XListView lv_orders;
  private ProgressDialog progressDialog;
  private SyncPatientBean patientBean = null;
  private List<OrderListBean> orders = null;

  private String rgTime = "1";
  private String rgStatus = "3";

  private int position = 0;
  private boolean FLAG_REFRESH_OR_INTIAL = false;
  private MyPatientDialog dialog;
  List<SyncPatientBean> deptPatient;
  OrdersCheckAdapter ordersListAdapter = null;
  private Dialog dialogMenu;
  private boolean isScan = false;
  private Vibrator vibrator;
  private static String currentRelateBarcode = "";
  private static String currentProductcode = "";
  private List<OrderListBean> top = new ArrayList<>();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_orders_check);
    ViewUtils.inject(this);
    presenter = new OrdersCheckPresenter(this);
    patientBean = new SyncPatientBean();
    deptPatient = new ArrayList<SyncPatientBean>();
    initView();
    presenter.receiveData();
    presenter.getPatients(this, position);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (GlobalConstant.REQUEST_CODE == requestCode) {
      if (resultCode == GlobalConstant.RESUlt_CODE) {
        presenter.loadOrders(UserInfo.getUserName(),
            patientBean.getPatId(), rgTime, rgStatus);
      }
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  private void initView() {

    ll_sex_bed_patcode.setVisibility(View.INVISIBLE);
    lv_orders = (XListView) findViewById(R.id.lv_orders);
    lv_orders.setPullRefreshEnable(true);
    lv_orders.setPullLoadEnable(false);
    lv_orders.setRefreshTime(DateTool.getCurrDateTimeS());
    lv_orders.setXListViewListener(new XListView.IXListViewListener() {
      @Override
      public void onRefresh() {
        presenter.loadOrders(UserInfo.getUserName(),
            patientBean.getPatId(), rgTime, rgStatus);
        lv_orders.setRefreshTime(DateTool.getCurrDateTimeS());
      }

      @Override
      public void onLoadMore() {

      }
    });
    lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view,
          int i, long l) {
        if (GlobalConstant.isSwipeOpen) {
          cancelExecution(orders.get(i - 1));
        }
      }
    });
    btn_menu.setOnClickListener(this);
    btn_back.setOnClickListener(this);
    tv_sx.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_menu:
        showMenu();
        break;
      case R.id.btn_back:
        finish();
        break;
      case R.id.tv_sx:
        presenter.showFilterDialog();
        break;
      case R.id.btn_refresh:
        dialogMenu.dismiss();
        presenter.loadOrders(UserInfo.getUserName(),
            patientBean.getPatId(), rgTime, rgStatus);
        break;
      case R.id.btn_filter:
        dialogMenu.dismiss();
        presenter.showFilterDialog();
        break;
      default:
        break;
    }
  }

  @Override
  public void completeOnRefresh() {
    lv_orders.stopRefresh();
  }

  @Override
  public void findPatientByPatId(List<OrderListBean> orderListBeen) {
    // 记住当前条码
    currentRelateBarcode = orderListBeen.get(0).getRelatedBarcode();
    orders.clear();// 如果找到病人成功了，就清空当前病人的数据
    String patId = "";
    boolean flag = false;
    // 如果一个病人就直接核对并且确定下一步加载下一个病人
    if (orderListBeen.size() == 1) {
      if (printByAndCheckBy2IsNotOne(orderListBeen.get(0).getRelatedBarcode(), orderListBeen)) {
        presenter.checkOrder(UserInfo.getWardCode(), orderListBeen.get(0).getPatId(),
            orderListBeen.get(0).getRelatedBarcode(), "",
            UserInfo.getUserName(), null);
      }
      patId = orderListBeen.get(0).getPatId();
      for (int i = 0; i < deptPatient.size(); i++) {
        String locPatId = deptPatient.get(i).getPatId();
        if (patId.equals(locPatId)) {
          // 本地找到了
          setPatientInfo(deptPatient.get(i));
          break;
        }
      }
    } else if (orderListBeen.size() > 1) {// 如果是两个病人，不知道下一个要加载的是那个病人，只有根据产品码判断
        if(orderListBeen.get(0).getTemplateId().equals("AA-7")){//检验AA-7成组的检验
            if (printByAndCheckBy2IsNotOne(orderListBeen.get(0).getRelatedBarcode(), orderListBeen)) {
                presenter.checkOrder(UserInfo.getWardCode(), orderListBeen.get(0).getPatId(),
                        orderListBeen.get(0).getRelatedBarcode(), "",
                        UserInfo.getUserName(), null);
            }
            patId = orderListBeen.get(0).getPatId();
            for (int i = 0; i < deptPatient.size(); i++) {
                String locPatId = deptPatient.get(i).getPatId();
                if (patId.equals(locPatId)) {
                    // 本地找到了
                    setPatientInfo(deptPatient.get(i));
                    break;
                }
            }
        }else if(orderListBeen.get(0).getTemplateId().equals("AA-5")){//多袋输血
            // 添加到top
            top.clear();
            top.addAll(orderListBeen);
            // 提示扫描产品码二次确认
            tostMsg(false, "请扫描产品码二次确认");
        }else{//普通的多条记录的一组医嘱，一个人的
          if (printByAndCheckBy2IsNotOne(orderListBeen.get(0).getRelatedBarcode(), orderListBeen)) {
            presenter.checkOrder(UserInfo.getWardCode(), orderListBeen.get(0).getPatId(),
                    orderListBeen.get(0).getRelatedBarcode(), "",
                    UserInfo.getUserName(), null);
          }
          patId = orderListBeen.get(0).getPatId();
          for (int i = 0; i < deptPatient.size(); i++) {
            String locPatId = deptPatient.get(i).getPatId();
            if (patId.equals(locPatId)) {
              setPatientInfo(deptPatient.get(i));
              break;
            }
          }
        }

    }

  }

  @Override
  public void loadCurrentPatientCheckOrders() {
    presenter.loadOrders(UserInfo.getUserName(), patientBean.getPatId(),
        rgTime, rgStatus);
  }

  @Override
  public void loadCheckOrdersByPatId(String patId) {
    presenter.loadOrders(UserInfo.getUserName(), patId,
        rgTime, rgStatus);
  }

  @Override
  public void checkOrdersFailed() {
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
    vibrator.vibrate(pattern, -1);
  }

  @Override
  public void showExceptionDialog(String title, String msg) {
    // 未找到该条码对应患者信息
    tostMsg(true, "未找到该条码对应患者信息");
  }

  @Override
  public void showMsg2(boolean flag, String msg) {
    tostMsg(flag, msg);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {}

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (GlobalConstant.ISBATCH) {
        GlobalConstant.ISBATCH = false;
        GlobalConstant.countOfBatchOrders = 0;
      } else {
        return super.onKeyDown(keyCode, event);
      }
    }
    return false;
  }

  /**
   * 展示progress
   */
  @Override
  public void showProgressDialog() {

    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
    }
    progressDialog.setMessage(getString(R.string.loadingdata));
    progressDialog.show();
  }

  @Override
  public void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  /**
   * 获取筛选 时间 频次 执行状态 流程 textView的内容
   */
  @Override
  public void setFilterText(String date, String status) {
    StringBuffer stringBuffer = new StringBuffer();
    if (TextUtils.equals("0", date)) {
      stringBuffer.append("昨天");
    } else if (TextUtils.equals("1", date)) {
      stringBuffer.append("今天");
    } else if (TextUtils.equals("2", date)) {
      stringBuffer.append("明天");
    } else if (TextUtils.equals("3", date)) {
      stringBuffer.append("全部");
    }
    stringBuffer.append(" ");
    if (TextUtils.equals("3", status)) {
      stringBuffer.append("全部");
    } else if (TextUtils.equals("1", status)) {
      stringBuffer.append("已核对");
    } else if (TextUtils.equals("2", status)) {
      stringBuffer.append("取消");
    } else if (TextUtils.equals("0", status)) {
      stringBuffer.append("未核对");
    }
    tv_time_status.setText(stringBuffer.toString());
  }

  /**
   * 展示筛选按钮的展示框
   */
  @Override
  public void showFilterButton() {
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate(R.layout.dialog_filter_check,
        (ViewGroup) findViewById(R.id.ll_dialog));
    rg_time = (RadioGroup) layout.findViewById(R.id.rg_time);
    rb_today = (RadioButton) layout.findViewById(R.id.rb_today);
    rb_tomorry = (RadioButton) layout.findViewById(R.id.rb_tomorry);
    rb_yestady = (RadioButton) layout.findViewById(R.id.rb_yestady);
    rb_time_all = (RadioButton) layout.findViewById(R.id.rb_time_all);

    rg_status = (RadioGroup) layout.findViewById(R.id.rg_status);
    rb_havachecked = (RadioButton) layout.findViewById(R.id.rb_havachecked);
    rb_notchecked = (RadioButton) layout.findViewById(R.id.rb_notchecked);
    rb_status_all = (RadioButton) layout.findViewById(R.id.rb_status_all);
    rb_cancel = (RadioButton) layout.findViewById(R.id.rb_cancel);

    if (rgTime.equals("1")) {
      rg_time.check(rb_today.getId());
    } else if (rgTime.equals("0")) {
      rg_time.check(rb_yestady.getId());
    } else if (rgTime.equals("2")) {
      rg_time.check(rb_tomorry.getId());
    } else if (rgTime.equals("3")) {
      rg_time.check(rb_time_all.getId());
    }

    if (rgStatus.equals("1")) {
      rg_status.check(rb_havachecked.getId());
    } else if (rgStatus.equals("0")) {
      rg_status.check(rb_notchecked.getId());
    } else if (rgStatus.equals("2")) {
      rg_status.check(rb_cancel.getId());
    } else if (rgStatus.equals("3")) {
      rg_status.check(rb_status_all.getId());
    }

    rg_status
        .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
              case R.id.rb_status_all:
                rgStatus = "3";
                break;
              case R.id.rb_havachecked:
                rgStatus = "1";
                break;
              case R.id.rb_notchecked:
                rgStatus = "0";
                break;
              case R.id.rb_cancel:
                rgStatus = "2";
                break;
            }
          }
        });
    rg_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.rb_today:
            rgTime = "1";
            break;
          case R.id.rb_tomorry:
            rgTime = "2";
            break;
          case R.id.rb_yestady:
            rgTime = "0";
            break;
          case R.id.rb_time_all:
            rgTime = "3";
            break;
        }
      }
    });
    new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
        .setView(layout)
        .setCancelable(true)
        .setPositiveButton(getString(R.string.make_sure),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog,
                  int which) {
                presenter.loadOrders(UserInfo.getUserName(),
                    patientBean.getPatId(), rgTime,
                    rgStatus);
                presenter.showFilterText(rgTime, rgStatus);
              }
            })
        .show();
  }

  /**
   * 设置病人的基本信息
   */
  @Override
  public void setPatientInfo(SyncPatientBean patientbean) {
    this.patientBean = patientbean;
    tv_pat_name.setText("执行核对");
    String bedNum = patientbean.getBedLabel();
    if (bedNum.contains("床")) {
      tv_patient.setText(bedNum + ";" + patientbean.getName() + ";"
          + patientbean.getSex());
    } else {
      tv_patient.setText(bedNum + "床;" + patientbean.getName() + ";"
          + patientbean.getSex());
    }
    presenter.loadOrders(UserInfo.getUserName(), patientBean.getPatId(),
        rgTime, rgStatus);
  }

  @Override
  public void setListData(List<OrderListBean> orderListBeans) {

    if (FLAG_REFRESH_OR_INTIAL && !orderListBeans.isEmpty()) {
      lv_orders.stopRefresh();
      orders.clear();
      orders = orderListBeans;
      ordersListAdapter.notifyDataSetChanged(orders);
    } else {
      lv_orders.stopRefresh();
      orders = orderListBeans;
      if (!StringTool.isEmpty(currentRelateBarcode)) {
        for (int i = 0; i < orders.size(); i++) {
          OrderListBean order = orders.get(i);
          if (currentRelateBarcode.equals(order.getRelatedBarcode())) {
            orders.remove(i);
            orders.add(0, order);
            break;
          }
        }
      }
      lv_orders.setAdapter(ordersListAdapter = new OrdersCheckAdapter(
          this, orders));
      ordersListAdapter.notifyDataSetChanged(orders);
      if (orders.isEmpty()) {
        if (LinstenNetState.isLinkState(this)) {
          MyToast myToast = new MyToast(this, 0,
              patientBean.getName() + getString(R.string.thepatientnoorders), 3 * 1000);
          myToast.showToast();
        } else {
          toErrorAct();
        }
      }
    }
  }

  @Override
  public void setPatient(List<SyncPatientBean> list) {
    deptPatient.clear();
    deptPatient.addAll(list);
  }

  @Override
  public void showToast(String msg) {
    ShowToast(msg);
  }

  @Override
  protected void resetLayout() {
    RelativeLayout root = (RelativeLayout) findViewById(R.id.root_doctororder);
    SupportDisplay.resetAllChildViewParam(root);
  }

  private void toErrorAct() {
    Intent intent = new Intent();
    intent.setClass(this, ErrorAct.class);
    startActivity(intent);
  }

  /**
   * 红外扫描获取的值
   * 
   * @param result
   */
  public void DoCameraResult(String result) {
    presenter.commRec(UserInfo.getUserName(), result, DeviceTool.getDeviceId(this));
    List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
    barcodeDicts = LocalSave.getDataList(getApplicationContext(),
        GlobalConstant.KEY_BARCODE);
    String barcodeName = "";
    for (int i = 0; i < barcodeDicts.size(); i++) {
      if (patternCode(barcodeDicts.get(i).getRegularExp(), result)) {
        barcodeName = barcodeDicts.get(i).getBarcodeName();
        break;
      }
    }
    if (barcodeName.equals("PAT_BARCODE")) {// 病人条码
      boolean flag = true;
      for (int i = 0; i < deptPatient.size(); i++) {
        if (result.equals(deptPatient.get(i).getPatBarcode())) {
          presenter.setPatients(deptPatient.get(i));
          flag = false;
          break;
        }
      }
      if (flag) {
        tostMsg(true, "未找到该患者");
      }
    } else if (barcodeName.equals("BLOOD_PRODUCT_CODE")) {// 产品码
      if (!CollectionsTool.isEmpty(top)) {
        if (top.get(0).getTemplateId().equals("AA-5")) {
          // 此时top里面是码相同病人的所有信息
          checkOrderByRelatedBarCode2(currentRelateBarcode, result, top);
        } else {
          tostMsg(false, "请先扫描血袋码核对");
        }
      } else {
        tostMsg(false, "请扫描血袋码核对");
      }

    } else {// 医嘱码包括血袋码
      currentRelateBarcode = result;// 记住当前扫描的医嘱条码，为置顶使用
      if (!isCurrentPatientOrderTest(result)) {// 当前病人的医嘱码
        presenter.findPatId(result, UserInfo.getUserName(), UserInfo.getWardCode());
      }
    }

  }

  private void loadCheckOrdersByRelatedBarCode2(String patId) {
    String locPatId = patId;
    for (int i = 0; i < deptPatient.size(); i++) {
      String patIdStr = deptPatient.get(i).getPatId();
      if (patIdStr.equals(locPatId)) {
        // 本地找到了
        setPatientInfo(deptPatient.get(i));
        break;
      }
    }

  }

  private void checkOrderByRelatedBarCode2(String arg0, String arg1, List<OrderListBean> top) {

    for (int i = 0; i < top.size(); i++) {
      String performStatus = top.get(i).getPerformStatus();
      String relatedBarCode = top.get(i).getRelatedBarcode();
      String relatedBarCode2 = top.get(i).getRelatedBarcode2();
      String patIdStr = top.get(i).getPatId();
      if (relatedBarCode.equals(arg0) && relatedBarCode2.equals(arg1)) {
        loadCheckOrdersByRelatedBarCode2(patIdStr);
        if ("0".equals(performStatus)) {
          tostMsg(true, "该医嘱条码已核对");
        } else if ("U".equals(performStatus)) {
          String patId = top.get(i).getPatId();
          if (printByAndCheckBy2IsNotOne(arg0, orders)) {
            presenter.checkOrder(UserInfo.getWardCode(), patId, top.get(i).getRelatedBarcode(),
                top.get(i).getRelatedBarcode2(), UserInfo.getUserName(),
                top.get(i).getPlanOrderNo());
          }
        } else {
          tostMsg(true, "请查看医嘱状态");
        }
        break;
      }
    }
    top.clear();
  }

  private boolean printByAndCheckBy2IsNotOne(String barCode,
      List<OrderListBean> orders) {
    if (CollectionsTool.isEmpty(orders)) {
      return true;
    }
    boolean flag = false;
    List<OrderListBean> ordersNew = new ArrayList<OrderListBean>();
    ordersNew = orders;
    for (int i = 0; i < ordersNew.size(); i++) {
      // 找到相同的
      if (barCode.equals(ordersNew.get(i).getRelatedBarcode())) {
        if (!TextUtils.isEmpty(ordersNew.get(i).getPrintBy())) {
          if (ordersNew.get(i).getPrintBy()
              .equals(UserInfo.getUserName())) {
            // 显示对话框
            final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon = new AlertDialogTwoBtnNoicon(
                this);
            alertDialogTwoBtnNoicon.setTitle("提示");
            alertDialogTwoBtnNoicon.setMessage("摆药与核对不能为同一人,请更换账户");
            alertDialogTwoBtnNoicon.setRightButton("确认",
                new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                  }
                });
            alertDialogTwoBtnNoicon.setLeftButton(" ",
                new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                  }
                });
            flag = false;
          } else {
            flag = true;
          }
        } else {
          flag = true;
        }
        break;
      } else {// 没有找到
        // 扫了另一个病人医嘱码，orders还是上一个病人医嘱数据，此时没找到也应当返回true
        flag = true;
      }
    }
    return flag;
  }


  /**
   * 判断是否是当前病人的医嘱
   *
   * @param barCodeResult
   * @return
   */
  private boolean isCurrentPatientOrderTest(String barCodeResult) {
    String str=barCodeResult;
    boolean flag = true;
    List<OrderListBean> others = new ArrayList<>();
    others.clear();
    if (orders.isEmpty()) {
      flag = false;
    } else {
      top.clear();
      for (int i = 0; i < orders.size(); i++) {
        if(TextUtils.isEmpty(orders.get(i).getRelatedBarcode())){
          continue;
        }else{
          String barCode = orders.get(i).getRelatedBarcode();
          if (barCode.equals(str)) {
            top.add(orders.get(i));
          } else {
            others.add(orders.get(i));
          }
        }
      }
      // top医嘱码相同的医嘱
      if (top.size() == 1) {
        currentRelateBarcode = str;// 记住当前扫描的医嘱条码，为置顶使用
        String performStatus = top.get(0).getPerformStatus();
        if ("0".equals(performStatus)) {
          tostMsg(true, "该医嘱条码已核对");
          flag = true;
        } else if ("U".equals(performStatus)) {
          String patId = top.get(0).getPatId();
          if (patientBean.getPatId().equals(patId)) {
            flag = true;
            if (printByAndCheckBy2IsNotOne(str, top)) {
              presenter.checkOrder(UserInfo.getWardCode(), patientBean.getPatId(),
                  top.get(0).getRelatedBarcode(), "",
                  UserInfo.getUserName(), top.get(0)
                      .getPlanOrderNo());
            }
          } else {
            flag = false;
          }
        } else if ("1".equals(performStatus)) {
          tostMsg(true, "此医嘱正在执行中");
        } else if ("9".equals(performStatus)) {
          tostMsg(true, "此医嘱已执行完毕");
        } else if ("-1".equals("此医嘱已取消")) {
          tostMsg(true, "此医嘱已取消");
        } else {
          flag = true;// 如果状态不满足核对条件，无论是不是当前病人都提示医嘱状态不对
          tostMsg(false, "请查看医嘱状态");
        }
      } else if (top.size() > 1) {
        flag = true;
        // 请再次扫描产品码
        if (top.get(0).getTemplateId().equals("AA-7")) {// 成组的批量执行检验
          String performStatus = top.get(0).getPerformStatus();
          if ("0".equals(performStatus)) {
            tostMsg(true, "该医嘱条码已核对");
          } else if ("U".equals(performStatus)) {
            String patId = top.get(0).getPatId();
            if (patientBean.getPatId().equals(patId)) {
              if (printByAndCheckBy2IsNotOne(str, top)) {
                presenter.checkOrder(UserInfo.getWardCode(), patientBean.getPatId(),
                    top.get(0).getRelatedBarcode(), "",
                    UserInfo.getUserName(), "");
              }
            } else {
              flag = false;
            }
          } else if ("1".equals(performStatus)) {
            tostMsg(true, "此医嘱正在执行中");
          } else if ("9".equals(performStatus)) {
            tostMsg(true, "此医嘱已执行完毕");
          } else if ("-1".equals("此医嘱已取消")) {
            tostMsg(true, "此医嘱已取消");
          } else {
            flag = true;// 如果状态不满足核对条件，无论是不是当前病人都提示医嘱状态不对
            tostMsg(false, "请查看医嘱状态");
          }
        } else {
          tostMsg(false, "请扫描血袋产品码二次确认");
        }
      } else {
        currentRelateBarcode = str;// 记住当前扫描的医嘱条码，为置顶使用
        flag = false;
      }
    }
    return flag;
  }


  private void tostMsg(boolean isVibration, String msg) {
    if (isVibration) {
      checkOrdersFailed();
    }
    MyToast myToast = new MyToast(this, 0, msg, 3 * 1000);
    myToast.showToast();
  }

  private boolean patternCode(String patternStr, String matcherStr) {
    Pattern p = Pattern.compile(patternStr.trim());
    Matcher m = p.matcher(matcherStr.trim());
    boolean b = m.matches();
    return b;
  }

  @Override
  public void setSelection(int position, String value,
      String planOrderNoOrReqId) {
    if (planOrderNoOrReqId.equals(GlobalConstant.KEY_PLANORDERNO)) {
      for (int i = 0; i < orders.size(); i++) {
        orders.get(i).setSelect(false);
        if (orders.get(i).getPlanOrderNo().equals(value)) {
          orders.get(i).setSelect(true);
          continue;
        }
      }
    } else if (planOrderNoOrReqId.equals(GlobalConstant.KEY_REQID)) {
      for (int i = 0; i < orders.size(); i++) {
        orders.get(i).setSelect(false);
        if (orders.get(i).getRelatedBarcode().equals(value)) {
          orders.get(i).setSelect(true);
          continue;
        }
      }
    }
    ordersListAdapter.notifyDataSetChanged();
  }

  @Override
  public void receiveDate() {
    position = getIntent().getIntExtra(GlobalConstant.KEY_POSITION, 0);
  }

  public void cancelExecution(OrderListBean orderListBean) {
    GlobalConstant.isSwipeOpen = false;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    bundle.putSerializable(GlobalConstant.KEY_PATIENT, patientBean);
    bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
    intent.putExtras(bundle);
    intent.setClass(OrdersCheckActivity.this, CancelExecutionAct.class);
    startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
  }

  private void showMenu() {
    dialogMenu = new Dialog(this);
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate(R.layout.layout_dialog,
        (ViewGroup) findViewById(R.id.ll_dialog_menu));

    // setContentView可以设置为一个View也可以简单地指定资源ID
    // LayoutInflater
    // li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
    // View v=li.inflate(R.layout.dialog_layout, null);
    // dialog.setContentView(v);
    dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogMenu.setContentView(layout);
    btn_all_not_execute = (Button) layout
        .findViewById(R.id.btn_all_not_execute);
    btn_all_not_execute.setOnClickListener(this);
    btn_refresh = (Button) layout.findViewById(R.id.btn_refresh);
    btn_refresh.setOnClickListener(this);
    btn_filter = (Button) layout.findViewById(R.id.btn_filter);
    btn_filter.setOnClickListener(this);
    /*
     * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
     * 对象,这样这可以以同样的方式改变这个Activity的属性.
     */
    Window dialogWindow = dialogMenu.getWindow();
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);

    /*
     * lp.x与lp.y表示相对于原始位置的偏移.
     * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
     * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
     * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
     * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
     * 当参数值包含Gravity.CENTER_HORIZONTAL时
     * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
     * 当参数值包含Gravity.CENTER_VERTICAL时
     * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
     * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
     * Gravity.CENTER_VERTICAL.
     * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
     * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
     * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
     */
    lp.x = 40; // 新位置X坐标
    lp.y = 82; // 新位置Y坐标
    lp.width = 360; // 宽度
    lp.height = 400; // 高度
    lp.alpha = 0.7f; // 透明度

    // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
    // dialog.onWindowAttributesChanged(lp);
    dialogWindow.setAttributes(lp);

    /*
     * 将对话框的大小按屏幕大小的百分比设置
     */
    // WindowManager m = getWindowManager();
    // Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
    // WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
    // 获取对话框当前的参数值
    // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
    // p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
    // dialogWindow.setAttributes(p);

    dialogMenu.show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    top.clear();
  }
}
