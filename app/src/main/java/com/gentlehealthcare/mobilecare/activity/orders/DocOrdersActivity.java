package com.gentlehealthcare.mobilecare.activity.orders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.cancelexecution.CancelExecutionAct;
import com.gentlehealthcare.mobilecare.activity.implementation.ImplementationRecordAct;
import com.gentlehealthcare.mobilecare.activity.insulin.InsulinActivity;
import com.gentlehealthcare.mobilecare.activity.intravenous.IntravenousAct;
import com.gentlehealthcare.mobilecare.activity.msg.MsgAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.adapter.OrdersListAdapter;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.common.MyToast;
import com.gentlehealthcare.mobilecare.common.ResultDiaLog;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.presenter.DocOrdersPresenter;
import com.gentlehealthcare.mobilecare.swipe.view.XListView;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.AlertDialogOrdersSelect;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtnNoicon;
import com.gentlehealthcare.mobilecare.view.IDocOrdersView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zyy on 2016/5/5.
 */
public class DocOrdersActivity extends BaseActivity
    implements
      IDocOrdersView,
      OnClickListener,
      AdapterView.OnItemClickListener {

  /**
   * 医嘱界面
   */
  DocOrdersPresenter presenter;
  @ViewInject(R.id.btn_back)
  private Button btn_back;
  @ViewInject(R.id.tv_time)
  private TextView tv_time;
  @ViewInject(R.id.tv_frequency)
  private TextView tv_frequency;
  @ViewInject(R.id.tv_perform)
  private TextView tv_perform;
  @ViewInject(R.id.tv_flow)
  private TextView tv_flow;
  @ViewInject(R.id.tv_pat_name)
  private TextView tv_pat_name;
  @ViewInject(R.id.tv_bed_label)
  private TextView tv_bed_label;
  @ViewInject(R.id.tv_pat_code)
  private TextView tv_pat_code;
  @ViewInject(R.id.tv_sx)
  private TextView tv_sx;
  @ViewInject(R.id.tv_orders)
  private TextView tv_orders;
  @ViewInject(R.id.tv_sex)
  private TextView tv_sex;
  @ViewInject(R.id.btn_menu)
  private Button btn_menu;
  @ViewInject(R.id.btn_execute)
  private Button btn_execute;
  @ViewInject(R.id.rl_execute)
  private RelativeLayout rl_execute;
  private RadioGroup rg_time;
  private RadioGroup rg_class;
  private RadioGroup rg_status;
  private RadioGroup rg_class_two;
  private RadioGroup rg_type;
  private RadioButton rb_today;
  private RadioButton rb_tomorry;
  private RadioButton rb_yestady;
  private RadioButton rb_long;
  private RadioButton rb_moment;
  private RadioButton rb_status_all;
  private RadioButton rb_execute;
  private RadioButton rb_executed;
  private RadioButton rb_type_all;
  private RadioButton rb_infusion;
  private RadioButton rb_inject;
  private RadioButton rb_class_all;
  private RadioButton rb_treatment;
  private RadioButton rb_time_all;
  private RadioButton rb_class_one;
  private RadioButton rb_class_two;
  private RadioButton rb_class_three;
  private Button btn_all_not_execute;
  private Button btn_refresh;
  private Button btn_filter;
  @ViewInject(R.id.ll_sex_bed_patcode)
  private LinearLayout ll_sex_bed_patcode;
  private XListView lv_orders;
  private ProgressDialog progressDialog;
  private SyncPatientBean patientBean = null;
  private List<OrderListBean> orders = null;
  private String rgType = "2";
  private String rgClass = "";
  private String rgTime = "1";
  private String rgStatus = "2";
  private int whichPatients = 0;
  private int position = 0;
  private boolean FLAG_REFRESH_OR_INTIAL = false;
  private boolean isClassTwo = false;
  private MyPatientDialog dialog;
  List<SyncPatientBean> deptPatient;
  OrdersListAdapter ordersListAdapter = null;
  private AlertDialogOrdersSelect alertDialogOrdersSelect;
  private Dialog dialogMenu;
  private boolean isRefreshPatient = false;
  private boolean isScan = false;
  private boolean isLoadAllNotExecute = false;
  private int ceilDate;
  private int floorDate;
  private static List<String> currentExecutePlanOrderNo;
  private ResultDiaLog resultDiaLog;
  private String currentRelatedBarCode;// 当前条码号用于计费接口
  List<OrderListBean> top = new ArrayList<>();
  private Vibrator vibrator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_nurse_orders);
    ViewUtils.inject(this);
    presenter = new DocOrdersPresenter(this);
    patientBean = new SyncPatientBean();
    deptPatient = new ArrayList<SyncPatientBean>();
    initView();
    presenter.receiveData();
    presenter.getPatients(this, position);
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    if (null == currentExecutePlanOrderNo) {
      currentExecutePlanOrderNo = new ArrayList<String>();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (GlobalConstant.isLoadAllNotExecuteOrToday) {
      presenter.loadAllNotExecute(patientBean.getPatId(), "0");
    } else {
      ordersListAdapter.notifyDataSetChanged();
      if (GlobalConstant.REQUEST_CODE == requestCode) {
        if (resultCode == GlobalConstant.RESUlt_CODE) {
          if (data.hasExtra(GlobalConstant.KEY_PLANBARCODE)) {
            String planOrderNo = data
                .getStringExtra(GlobalConstant.KEY_PLANBARCODE);
            presenter.loadOrders(UserInfo.getUserName(),
                patientBean.getPatId(), rgTime, rgType, rgStatus,
                "8", rgClass, planOrderNo,
                GlobalConstant.ORDER_PLAN_ORDER_NO);
          }
          if (data.hasExtra(GlobalConstant.KEY_REQID)) {
            String reqId = data
                .getStringExtra(GlobalConstant.KEY_REQID);
            presenter.loadOrders(UserInfo.getUserName(),
                patientBean.getPatId(), rgTime, rgType, rgStatus,
                "8", rgClass, reqId, GlobalConstant.ORDER_REQ_ID);
          }
        }
      }
    }
  }

  private void initView() {
    SharedPreferences sharedPreferences = getSharedPreferences(
        GlobalConstant.KEY_ORDER_LIMITED_TIME, Activity.MODE_PRIVATE);
    ceilDate = sharedPreferences.getInt(GlobalConstant.KEY_CEILING, 60);
    floorDate = sharedPreferences.getInt(GlobalConstant.KEY_FLOOR, 60);
    lv_orders = (XListView) findViewById(R.id.lv_orders);
    lv_orders.setPullRefreshEnable(true);
    lv_orders.setPullLoadEnable(false);
    lv_orders.setRefreshTime(DateTool.getCurrDateTimeS());
    lv_orders.setXListViewListener(new XListView.IXListViewListener() {
      @Override
      public void onRefresh() {
        FLAG_REFRESH_OR_INTIAL = true;
        if (!GlobalConstant.ISBATCH) {
          if (GlobalConstant.isCurrentShowAllNotExecute) {
            presenter.loadAllNotExecute(patientBean.getPatId(), "0");
          } else {
            presenter.loadOrders(UserInfo.getUserName(),
                patientBean.getPatId(), rgTime, rgType, rgStatus,
                "8", rgClass, null, GlobalConstant.ORDER_ORG);
          }
          FLAG_REFRESH_OR_INTIAL = false;
        } else {
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              completeOnRefresh();
            }
          }, 1000);
        }
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
    ll_sex_bed_patcode.setOnClickListener(this);
    tv_sx.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_menu:
        showMenu();
        break;
      case R.id.btn_back:
        if (GlobalConstant.ISBATCH) {
          GlobalConstant.ISBATCH = false;
          GlobalConstant.countOfBatchOrders = 0;
          changeLayoutOriginal();
        } else {
          finish();
        }
        break;
      case R.id.ll_sex_bed_patcode:
        presenter.showPatients();
        break;
      case R.id.tv_sx:
        if (!GlobalConstant.ISBATCH) {
          presenter.showFilterDialog();
        } else {
          selectAll();
        }
        break;
      case R.id.tv_orders:// 当取消按钮使用
        GlobalConstant.ISBATCH = false;
        GlobalConstant.countOfBatchOrders = 0;
        changeLayoutOriginal();
        break;
      case R.id.btn_execute:
        if (GlobalConstant.countOfBatchOrders == 0) {
          ShowToast("选择医嘱后执行");
        } else {
          final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon = new AlertDialogTwoBtnNoicon(
              this);
          alertDialogTwoBtnNoicon.setTitle("执行");
          alertDialogTwoBtnNoicon.setMessage("确定要执行所选"
              + GlobalConstant.countOfBatchOrders + "项工作吗？");
          alertDialogTwoBtnNoicon.setLeftButton(
              getString(R.string.cancel), new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                }
              });
          alertDialogTwoBtnNoicon.setRightButton("执行",
              new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                  presenter.batchExecuted(UserInfo.getUserName(),
                      patientBean.getPatId(),
                      UserInfo.getWardCode(), rgTime, rgType,
                      rgStatus, "8", rgClass, null, 100,
                      orders);
                  changeLayoutOriginal();
                  GlobalConstant.countOfBatchOrders = 0;
                }
              });
        }
        break;
      case R.id.btn_all_not_execute:
        dialogMenu.dismiss();
        isLoadAllNotExecute = true;
        presenter.loadAllNotExecute(patientBean.getPatId(), "0");
        break;
      case R.id.btn_refresh:
        dialogMenu.dismiss();
        presenter.loadOrders(UserInfo.getUserName(),
            patientBean.getPatId(), rgTime, rgType, rgStatus, "8",
            rgClass, null, GlobalConstant.ORDER_ORG);
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
  public void toMsgAct(List<TipBean> orders, String patCode) {
    if (orders != null && !orders.isEmpty()) {
      Intent intent = new Intent();
      intent.putExtra(GlobalConstant.KEY_BARCODE,
          patCode);
      intent.putExtra(GlobalConstant.KEY_EXECUTEING_ORDERS, (Serializable) orders);
      intent.setClass(DocOrdersActivity.this,
          MsgAct.class);
      startActivityForResult(intent,
          GlobalConstant.REQUEST_CODE);
    }
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
        changeLayoutOriginal();
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
  public void setFilterText(String time, String frequency, String perform,
      String flow) {

    if (TextUtils.equals("0", rgTime)) {
      tv_time.setText("昨天");
    } else if (TextUtils.equals("1", rgTime)) {
      tv_time.setText("今天");
    } else if (TextUtils.equals("2", rgTime)) {
      tv_time.setText("明天");
    } else if (TextUtils.equals("3", rgTime)) {
      tv_time.setText("全部");
    }

    if (TextUtils.equals("2", frequency)) {
      tv_frequency.setText("全部");
    } else if (TextUtils.equals("1", frequency)) {
      tv_frequency.setText("长期");
    } else if (TextUtils.equals("0", frequency)) {
      tv_frequency.setText("临时");
    }

    if (TextUtils.equals("2", perform)) {
      tv_perform.setText("全部");
    } else if (TextUtils.equals("0", perform)) {
      tv_perform.setText("未执行");
    } else if (TextUtils.equals("9", perform)) {
      tv_perform.setText("已执行");
    }

    if ("".equals(flow)) {
      tv_flow.setText("全部");
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item0").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item0").split("\\|\\|")[0]);
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item1").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item1").split("\\|\\|")[0]);
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item2").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item2").split("\\|\\|")[0]);
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item3").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item3").split("\\|\\|")[0]);
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item4").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item4").split("\\|\\|")[0]);
    } else if (TextUtils.equals(
        LocalSave.getStringData(DocOrdersActivity.this, "item5").split(
            "\\|\\|")[1],
        flow)) {
      tv_flow.setText(LocalSave.getStringData(DocOrdersActivity.this,
          "item5").split("\\|\\|")[0]);
    }

  }

  /**
   * 展示筛选按钮的展示框
   */
  @Override
  public void showFilterButton() {
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate(R.layout.dialog_filter,
        (ViewGroup) findViewById(R.id.ll_dialog));
    rg_time = (RadioGroup) layout.findViewById(R.id.rg_time);
    rb_today = (RadioButton) layout.findViewById(R.id.rb_today);
    rb_tomorry = (RadioButton) layout.findViewById(R.id.rb_tomorry);
    rb_yestady = (RadioButton) layout.findViewById(R.id.rb_yestady);
    rb_time_all = (RadioButton) layout.findViewById(R.id.rb_time_all);

    rg_status = (RadioGroup) layout.findViewById(R.id.rg_status);
    rb_execute = (RadioButton) layout.findViewById(R.id.rb_execute);
    rb_executed = (RadioButton) layout.findViewById(R.id.rb_executed);
    rb_status_all = (RadioButton) layout.findViewById(R.id.rb_status_all);

    rg_type = (RadioGroup) layout.findViewById(R.id.rg_type);
    rb_moment = (RadioButton) layout.findViewById(R.id.rb_moment);
    rb_long = (RadioButton) layout.findViewById(R.id.rb_long);
    rb_type_all = (RadioButton) layout.findViewById(R.id.rb_type_all);

    rg_class = (RadioGroup) layout.findViewById(R.id.rg_class);
    rb_class_all = (RadioButton) layout.findViewById(R.id.rb_class_all);
    rb_treatment = (RadioButton) layout.findViewById(R.id.rb_treatment);
    rb_infusion = (RadioButton) layout.findViewById(R.id.rb_infusion);
    rb_inject = (RadioButton) layout.findViewById(R.id.rb_inject);

    rg_class_two = (RadioGroup) layout.findViewById(R.id.rg_class_two);
    rb_class_one = (RadioButton) layout.findViewById(R.id.rb_class_one);
    rb_class_two = (RadioButton) layout.findViewById(R.id.rb_class_two);
    rb_class_three = (RadioButton) layout.findViewById(R.id.rb_class_three);

    String classOne = LocalSave.getStringData(DocOrdersActivity.this,
        "item0");
    String classTwo = LocalSave.getStringData(DocOrdersActivity.this,
        "item1");
    String classThree = LocalSave.getStringData(DocOrdersActivity.this,
        "item2");
    String classFour = LocalSave.getStringData(DocOrdersActivity.this,
        "item3");
    String classFive = LocalSave.getStringData(DocOrdersActivity.this,
        "item4");
    String classSix = LocalSave.getStringData(DocOrdersActivity.this,
        "item5");
    if (StringTool.isEmpty(classOne)) {
      rb_treatment.setVisibility(View.GONE);
    } else {
      rb_treatment.setText(classOne.split("\\|\\|")[0]);
    }
    if (StringTool.isEmpty(classTwo)) {
      rb_infusion.setVisibility(View.GONE);
    } else {
      rb_infusion.setText(classTwo.split("\\|\\|")[0]);
    }
    if (StringTool.isEmpty(classThree)) {
      rb_inject.setVisibility(View.GONE);
    } else {
      rb_inject.setText(classThree.split("\\|\\|")[0]);
    }
    if (StringTool.isEmpty(classFour)) {
      rb_class_one.setVisibility(View.GONE);
    } else {
      rb_class_one.setText(classFour.split("\\|\\|")[0]);
    }
    if (StringTool.isEmpty(classFive)) {
      rb_class_two.setVisibility(View.GONE);
    } else {
      rb_class_two.setText(classFive.split("\\|\\|")[0]);
    }
    if (StringTool.isEmpty(classSix)) {
      rb_class_three.setVisibility(View.GONE);
    } else {
      rb_class_three.setText(classSix.split("\\|\\|")[0]);
    }

    if (rgTime.equals("1")) {
      rg_time.check(rb_today.getId());
    } else if (rgTime.equals("0")) {
      rg_time.check(rb_yestady.getId());
    } else if (rgTime.equals("2")) {
      rg_time.check(rb_tomorry.getId());
    } else if (rgTime.equals("3")) {
      rg_time.check(rb_time_all.getId());
    }

    if (rgType.equals("0")) {
      rg_type.check(rb_moment.getId());
    } else if (rgType.equals("1")) {
      rg_type.check(rb_long.getId());
    } else if (rgType.equals("2")) {
      rg_type.check(rb_type_all.getId());
    }

    if (rgStatus.equals("9")) {
      rg_status.check(rb_executed.getId());
    } else if (rgStatus.equals("0")) {
      rg_status.check(rb_execute.getId());
    } else if (rgStatus.equals("2")) {
      rg_status.check(rb_status_all.getId());
    }

    if (!isClassTwo) {
      if ("".equals(rgClass)) {
        rg_class.check(rb_class_all.getId());
      } else if (rgClass.equals(LocalSave.getStringData(
          DocOrdersActivity.this, "item0").split("\\|\\|")[1])) {
        rg_class.check(rb_treatment.getId());
      } else if (rgClass.equals(LocalSave.getStringData(
          DocOrdersActivity.this, "item1").split("\\|\\|")[1])) {
        rg_class.check(rb_infusion.getId());
      } else if (rgClass.equals(LocalSave.getStringData(
          DocOrdersActivity.this, "item2").split("\\|\\|")[1])) {
        rg_class.check(rb_inject.getId());
      }
    } else {
      if (LocalSave.getStringData(DocOrdersActivity.this, "item3").split(
          "\\|\\|")[1].equals(rgClass)) {
        rg_class_two.check(rb_class_one.getId());
      } else if (rgClass.equals(LocalSave.getStringData(
          DocOrdersActivity.this, "item4").split("\\|\\|")[1])) {
        rg_class_two.check(rb_class_two.getId());
      } else if (rgClass.equals(LocalSave.getStringData(
          DocOrdersActivity.this, "item5").split("\\|\\|")[1])) {
        rg_class_two.check(rb_class_three.getId());
      }
    }

    rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.rb_type_all:
            rgType = "2";
            break;
          case R.id.rb_long:
            rgType = "1";
            break;
          case R.id.rb_moment:
            rgType = "0";
            break;
        }
      }
    });
    rg_status
        .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
              case R.id.rb_status_all:
                rgStatus = "2";
                break;
              case R.id.rb_execute:
                rgStatus = "0";
                break;
              case R.id.rb_executed:
                rgStatus = "9";
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
    rg_class.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (isClassTwo) {
          rb_class_one.setChecked(false);
          rb_class_two.setChecked(false);
          rb_class_three.setChecked(false);
        }
        isClassTwo = false;
        switch (checkedId) {
          case R.id.rb_class_all:
            rgClass = "";
            break;
          case R.id.rb_treatment:
            rgClass = LocalSave.getStringData(DocOrdersActivity.this,
                "item0").split("\\|\\|")[1];
            break;
          case R.id.rb_infusion:
            rgClass = LocalSave.getStringData(DocOrdersActivity.this,
                "item1").split("\\|\\|")[1];
            break;
          case R.id.rb_inject:
            rgClass = LocalSave.getStringData(DocOrdersActivity.this,
                "item2").split("\\|\\|")[1];
            break;
        }
      }
    });

    rg_class_two
        .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (!isClassTwo) {
              rb_class_all.setChecked(false);
              rb_treatment.setChecked(false);
              rb_infusion.setChecked(false);
              rb_inject.setChecked(false);
            }
            isClassTwo = true;
            switch (checkedId) {
              case R.id.rb_class_one:
                rgClass = LocalSave.getStringData(
                    DocOrdersActivity.this, "item3").split(
                        "\\|\\|")[1];
                break;
              case R.id.rb_class_two:
                rgClass = LocalSave.getStringData(
                    DocOrdersActivity.this, "item4").split(
                        "\\|\\|")[1];
                break;
              case R.id.rb_class_three:
                rgClass = LocalSave.getStringData(
                    DocOrdersActivity.this, "item5").split(
                        "\\|\\|")[1];
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
                    patientBean.getPatId(), rgTime, rgType,
                    rgStatus, "8", rgClass, null,
                    GlobalConstant.ORDER_ORG);
                presenter.showFilterText(rgTime, rgType,
                    rgStatus, rgClass);
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
    tv_pat_code.setText(patientbean.getPatCode());
    tv_pat_name.setText(patientbean.getName());
    tv_sex.setText(patientbean.getSex());
    tv_bed_label.setText(patientbean.getBedLabel() + "床");
    presenter.loadOrders(UserInfo.getUserName(), patientBean.getPatId(),
        rgTime, rgType, rgStatus, "8", rgClass, null,
        GlobalConstant.ORDER_ORG);
  }

  @Override
  public void setListData(List<OrderListBean> orderListBeans) {
    List<OrderListBean> temp = new ArrayList<OrderListBean>();
    temp.clear();
    if (!orderListBeans.isEmpty() && !currentExecutePlanOrderNo.isEmpty()) {
      for (int i = 0; i < currentExecutePlanOrderNo.size(); i++) {
        for (int j = 0; j < orderListBeans.size(); j++) {
          if (currentExecutePlanOrderNo.get(i).equals(orderListBeans.get(j).getPlanOrderNo())) {
            temp.add(orderListBeans.get(j));
            orderListBeans.remove(j);
          }
        }
      }

    }
    temp.addAll(orderListBeans);
    if (FLAG_REFRESH_OR_INTIAL) {
      lv_orders.stopRefresh();
      orders.clear();
      orders = temp;
      ordersListAdapter.notifyDataSetChanged(orders);
    } else {
      lv_orders.stopRefresh();
      orders = temp;
      lv_orders.setAdapter(ordersListAdapter = new OrdersListAdapter(
          this, orders));
      ordersListAdapter.notifyDataSetChanged(orders);
      if (isLoadAllNotExecute) {
        showLoadAll();
      }
      if (orders.isEmpty()) {
        if (LinstenNetState.isLinkState(this)) {
          tostMsg(false, patientBean.getName()
              + getString(R.string.thepatientnoorders));
        } else {
          toErrorAct();
        }
      }
    }
  }

  @Override
  public void showPatients() {
    dialog = new MyPatientDialog(DocOrdersActivity.this,
        new MyPatientDialog.MySnListener() {

          @Override
          public void myOnItemClick(View view, int position, long id) {

            presenter.setPatients(deptPatient.get(position));
            whichPatients = position;
            String patId = deptPatient.get(position).getPatId();
            if (!patId.equals(GlobalConstant.PATID)) {
              currentExecutePlanOrderNo.clear();
            }
            presenter.loadOrders(UserInfo.getUserName(), patId,
                rgTime, rgType, rgStatus, "8", rgClass, null,
                GlobalConstant.ORDER_ORG);
            dialog.dismiss();
          }

          @Override
          public void onRefresh() {
            isRefreshPatient = true;
            presenter.getPatients(DocOrdersActivity.this, 1000);// 1000标记用来刷新
            dialog.dismiss();
          }
        }, whichPatients, deptPatient);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.show();
  }

  @Override
  public void executeOrderSuccess(String planOrderNo) {
    // 医嘱执行成功后开始计费
    presenter.callProcedureBill(patientBean.getPatId(), patientBean.getPatCode(),
        Integer.parseInt(patientBean.getVisitId()), currentRelatedBarCode, UserInfo.getUserName());
    // ShowToast("医嘱执行成功"); who:zyy why:bill msg
    tostMsg(false,"医嘱执行成功"); //3-31
    if (GlobalConstant.isLoadAllNotExecuteOrToday) {
      isLoadAllNotExecute = true;
      presenter.loadAllNotExecute(patientBean.getPatId(), "0");
    } else {
      presenter.loadOrders(UserInfo.getUserName(), patientBean.getPatId(),
          rgTime, rgType, rgStatus, "8", rgClass, planOrderNo,
          GlobalConstant.ORDER_PLAN_ORDER_NO);
    }
  }

  @Override
  public void setPatient(List<SyncPatientBean> list) {
    deptPatient.clear();
    deptPatient.addAll(list);
    if (isRefreshPatient) {
      isRefreshPatient = false;
      showPatients();
    }
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

  public void toImplementationRecordAct(OrderListBean orderListBean) {
    Intent intent = new Intent();
    intent.setClass(DocOrdersActivity.this, ImplementationRecordAct.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable(GlobalConstant.KEY_PATIENT, patientBean);
    bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
    intent.putExtras(bundle);
    startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
  }

  public void executeOrder(final OrderListBean orderListBean) {
    if (!currentExecutePlanOrderNo.contains(orderListBean.getPlanOrderNo())) {
      currentExecutePlanOrderNo.add(orderListBean.getPlanOrderNo());
    }
    if (orderListBean.getScanMode().equals("2") && !isScan) {
      tostMsg(false,"请扫描医嘱码");
    } else {
      if ("2".equals(orderListBean.getScanMode2())) {
        // 必须扫描腕带
        if (StringTool.lastScanPatient(orderListBean.getScanMode(),
            orderListBean.getPatId())) {
          isScan = false;
          if ("1".equals(orderListBean.getStopAttr())) {
            tostMsg(true,"已经取消，不可执行");
          } else {
            if (StringTool.isEmpty(orderListBean.getTemplateId())
                || (orderListBean.getTemplateType().equals("0"))) {
              if (orderListBean.getPerformStatus().equals("9")) {
                presenter.loadOrders(UserInfo.getUserName(),
                    patientBean.getPatId(), rgTime, rgType,
                    rgStatus, "8", rgClass,
                    orderListBean.getPlanOrderNo(),
                    GlobalConstant.ORDER_PLAN_ORDER_NO);
              } else if (orderListBean.getPerformStatus().equals(
                  "0")) {

                if (orderListBean.getTemplateType().equals("0")
                    && !TextUtils.isEmpty(orderListBean
                        .getPlanBatchNo())) {
                  GlobalConstant.BATCH_NO = orderListBean
                      .getPlanBatchNo();
                }
                if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                  final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                      new AlertDialogTwoBtnNoicon(this);
                  alertDialogTwoBtnNoicon.setTitle("提示");
                  alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                  alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                      presenter.executeOrder(UserInfo.getUserName(),
                          orderListBean.getPatId(),
                          orderListBean.getPlanId(),
                          UserInfo.getWardCode(),
                          null,
                          orderListBean.getPlanOrderNo());
                    }
                  });
                  alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                    }
                  });
                } else {
                  presenter.executeOrder(UserInfo.getUserName(),
                      orderListBean.getPatId(),
                      orderListBean.getPlanId(),
                      UserInfo.getWardCode(),
                      null,
                      orderListBean.getPlanOrderNo());
                }

              } else if (orderListBean.getPerformStatus().equals(
                  "-1")) {
                //ShowToast("该医嘱已执行");
                tostMsg(true,"该医嘱已被执行");
              }

            } else if (orderListBean.getTemplateId().equals("AA-1")) {
              if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                    new AlertDialogTwoBtnNoicon(this);
                alertDialogTwoBtnNoicon.setTitle("提示");
                alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                  }
                });
                alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalConstant.KEY_PATIENT,
                        patientBean);
                    bundle.putSerializable(
                        GlobalConstant.KEY_ORDERLISTBEAN,
                        orderListBean);
                    intent.putExtras(bundle);
                    intent.setClass(DocOrdersActivity.this,
                        IntravenousAct.class);
                    startActivityForResult(intent,
                        GlobalConstant.REQUEST_CODE);
                  }
                });
              } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT,
                    patientBean);
                bundle.putSerializable(
                    GlobalConstant.KEY_ORDERLISTBEAN,
                    orderListBean);
                intent.putExtras(bundle);
                intent.setClass(DocOrdersActivity.this,
                    IntravenousAct.class);
                startActivityForResult(intent,
                    GlobalConstant.REQUEST_CODE);
              }
            } else if (orderListBean.getTemplateId().equals("AA-3")) {
              if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                    new AlertDialogTwoBtnNoicon(this);
                alertDialogTwoBtnNoicon.setTitle("提示");
                alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                  }
                });
                alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                    Intent intent = new Intent(DocOrdersActivity.this,
                        InsulinActivity.class);
                    intent.putExtra(GlobalConstant.KEY_ORDERLISTBEAN,
                        orderListBean);
                    intent.putExtra(GlobalConstant.KEY_PATIENT,
                        patientBean);
                    startActivityForResult(intent,
                        GlobalConstant.REQUEST_CODE);
                  }
                });
              } else {
                Intent intent = new Intent(DocOrdersActivity.this,
                    InsulinActivity.class);
                intent.putExtra(GlobalConstant.KEY_ORDERLISTBEAN,
                    orderListBean);
                intent.putExtra(GlobalConstant.KEY_PATIENT,
                    patientBean);
                startActivityForResult(intent,
                    GlobalConstant.REQUEST_CODE);
              }

            } else if (orderListBean.getTemplateId().equals("AA-4")) {
              if (orderListBean.getPerformStatus().equals("0")) {
                if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                  final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                      new AlertDialogTwoBtnNoicon(this);
                  alertDialogTwoBtnNoicon.setTitle("提示");
                  alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                  alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                    }
                  });
                  alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                      Intent intent = new Intent();
                      intent.putExtra(GlobalConstant.KEY_PATIENT,
                          patientBean);
                      intent.putExtra(GlobalConstant.KEY_PLANORDERNO,
                          orderListBean.getPlanOrderNo());
                      intent.setClass(DocOrdersActivity.this,
                          BloodSugarActivity.class);
                      startActivityForResult(intent,
                          GlobalConstant.REQUEST_CODE);
                    }
                  });
                } else {
                  Intent intent = new Intent();
                  intent.putExtra(GlobalConstant.KEY_PATIENT,
                      patientBean);
                  intent.putExtra(GlobalConstant.KEY_PLANORDERNO,
                      orderListBean.getPlanOrderNo());
                  intent.setClass(DocOrdersActivity.this,
                      BloodSugarActivity.class);
                  startActivityForResult(intent,
                      GlobalConstant.REQUEST_CODE);
                }
              }
            } else if (orderListBean.getTemplateId().equals("AA-5")) {
              if (TextUtils.isEmpty(orderListBean.getReqId())) {
                showToast("医嘱数据缺少reqId,联系运维");
              } else {
                final Intent transfusionIntent = new Intent(
                    DocOrdersActivity.this,
                    TransfusionActivity.class);
                if (orderListBean.getPerformStatus()
                    .equals("0")) {
                  if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                    final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                        new AlertDialogTwoBtnNoicon(this);
                    alertDialogTwoBtnNoicon.setTitle("提示");
                    alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                    alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        alertDialogTwoBtnNoicon.dismiss();
                      }
                    });
                    alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        alertDialogTwoBtnNoicon.dismiss();
                        transfusionIntent.putExtra(
                            GlobalConstant.KEY_PATIENT,
                            patientBean);
                        transfusionIntent.putExtra(
                            GlobalConstant.KEY_STATUS,
                            GlobalConstant.NOT_PERFORM);
                        transfusionIntent.putExtra(
                            GlobalConstant.KEY_REQID,
                            orderListBean.getRelatedBarcode());
                        transfusionIntent.putExtra(
                            GlobalConstant.KEY_FLAG,
                            GlobalConstant.KEY_PLANBARCODE);
                        transfusionIntent.putExtra(
                            GlobalConstant.KEY_FLAG_SCAN,
                            GlobalConstant.VALUE_FLAG_SCAN);
                        startActivityForResult(transfusionIntent,
                            GlobalConstant.REQUEST_CODE);
                      }
                    });
                  } else {
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_PATIENT,
                        patientBean);
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_STATUS,
                        GlobalConstant.NOT_PERFORM);
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_REQID,
                        orderListBean.getRelatedBarcode());
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_FLAG,
                        GlobalConstant.KEY_PLANBARCODE);
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_FLAG_SCAN,
                        GlobalConstant.VALUE_FLAG_SCAN);
                    startActivityForResult(transfusionIntent,
                        GlobalConstant.REQUEST_CODE);
                  }

                } else if (orderListBean.getPerformStatus()
                    .equals("1")) {
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_PATIENT,
                      patientBean);
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_STATUS,
                      GlobalConstant.PERFORMING);
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_REQID,
                      orderListBean.getRelatedBarcode());
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_FLAG,
                      GlobalConstant.KEY_PLANBARCODE);
                  transfusionIntent
                      .putExtra(
                          GlobalConstant.KEY_FLAG_SCAN_PATROL,
                          GlobalConstant.VALUE_FLAG_SCAN_PATROL);
                  startActivityForResult(transfusionIntent,
                      GlobalConstant.REQUEST_CODE);
                } else if ("9".equals(orderListBean.getPerformStatus())
                    || "-1".equals(orderListBean.getPerformStatus())) {
                  toImplementationRecordAct(orderListBean);
                } else {
                  ShowToast("核对后才能执行");
                }
              }
            }
          }

        } else {
          tostMsg(false,"请先扫描患者腕带");
        }
      } else {
        if ("1".equals(orderListBean.getStopAttr())) {
          tostMsg(true,"已经取消，不可执行");
        } else {
          // 可以不扫描腕带
          if (StringTool.isEmpty(orderListBean.getTemplateId())
              || (orderListBean.getTemplateType().equals("0"))) {

            if (orderListBean.getPerformStatus().equals("9")) {
              presenter.loadOrders(UserInfo.getUserName(),
                  patientBean.getPatId(), rgTime, rgType,
                  rgStatus, "8", rgClass,
                  orderListBean.getPlanOrderNo(),
                  GlobalConstant.ORDER_PLAN_ORDER_NO);
            } else if (orderListBean.getPerformStatus().equals("0")) {

              if (orderListBean.getTemplateType().equals("0")
                  && !TextUtils.isEmpty(orderListBean
                      .getPlanBatchNo())) {
                GlobalConstant.BATCH_NO = orderListBean
                    .getPlanBatchNo();
              }
              if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                    new AlertDialogTwoBtnNoicon(this);
                alertDialogTwoBtnNoicon.setTitle("提示");
                alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                  }
                });
                alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    alertDialogTwoBtnNoicon.dismiss();
                    presenter.executeOrder(UserInfo.getUserName(),
                        orderListBean.getPatId(),
                        orderListBean.getPlanId(),
                        UserInfo.getWardCode(),
                        null,
                        orderListBean.getPlanOrderNo());
                  }
                });
              } else {
                presenter.executeOrder(UserInfo.getUserName(),
                    orderListBean.getPatId(),
                    orderListBean.getPlanId(),
                    UserInfo.getWardCode(),
                    null,
                    orderListBean.getPlanOrderNo());
              }

            } else if (orderListBean.getPerformStatus().equals("-1")) {
              tostMsg(false,"该医嘱已被执行");
            }

          } else if (orderListBean.getTemplateId().equals("AA-1")) {
            if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
              final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                  new AlertDialogTwoBtnNoicon(this);
              alertDialogTwoBtnNoicon.setTitle("提示");
              alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
              alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                }
              });
              alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                  Intent intent = new Intent();
                  Bundle bundle = new Bundle();
                  bundle.putSerializable(GlobalConstant.KEY_PATIENT,
                      patientBean);
                  bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN,
                      orderListBean);
                  intent.putExtras(bundle);
                  intent.setClass(DocOrdersActivity.this,
                      IntravenousAct.class);
                  startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
                }
              });
            } else {
              Intent intent = new Intent();
              Bundle bundle = new Bundle();
              bundle.putSerializable(GlobalConstant.KEY_PATIENT,
                  patientBean);
              bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN,
                  orderListBean);
              intent.putExtras(bundle);
              intent.setClass(DocOrdersActivity.this,
                  IntravenousAct.class);
              startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
            }

          } else if (orderListBean.getTemplateId().equals("AA-3")) {
            if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
              final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                  new AlertDialogTwoBtnNoicon(this);
              alertDialogTwoBtnNoicon.setTitle("提示");
              alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
              alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                }
              });
              alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                  Intent intent = new Intent(DocOrdersActivity.this,
                      InsulinActivity.class);
                  intent.putExtra(GlobalConstant.KEY_ORDERLISTBEAN,
                      orderListBean);
                  intent.putExtra(GlobalConstant.KEY_PATIENT, patientBean);
                  startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
                }
              });
            } else {
              Intent intent = new Intent(DocOrdersActivity.this,
                  InsulinActivity.class);
              intent.putExtra(GlobalConstant.KEY_ORDERLISTBEAN,
                  orderListBean);
              intent.putExtra(GlobalConstant.KEY_PATIENT, patientBean);
              startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
            }

          } else if (orderListBean.getTemplateId().equals("AA-4")) {
            if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
              final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                  new AlertDialogTwoBtnNoicon(this);
              alertDialogTwoBtnNoicon.setTitle("提示");
              alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
              alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                }
              });
              alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  alertDialogTwoBtnNoicon.dismiss();
                  if (orderListBean.getPerformStatus().equals("0")) {
                    Intent intent = new Intent();
                    intent.putExtra(GlobalConstant.KEY_PATIENT, patientBean);
                    intent.putExtra(GlobalConstant.KEY_PLANORDERNO,
                        orderListBean.getPlanOrderNo());
                    intent.setClass(DocOrdersActivity.this,
                        BloodSugarActivity.class);
                    startActivityForResult(intent,
                        GlobalConstant.REQUEST_CODE);
                  }
                }
              });
            } else {
              if (orderListBean.getPerformStatus().equals("0")) {
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PATIENT, patientBean);
                intent.putExtra(GlobalConstant.KEY_PLANORDERNO,
                    orderListBean.getPlanOrderNo());
                intent.setClass(DocOrdersActivity.this,
                    BloodSugarActivity.class);
                startActivityForResult(intent,
                    GlobalConstant.REQUEST_CODE);
              }
            }

          } else if (orderListBean.getTemplateId().equals("AA-5")) {
            if (TextUtils.isEmpty(orderListBean.getReqId())) {
              showToast("医嘱数据缺少reqId,联系运维");
            } else {
              final Intent transfusionIntent = new Intent(
                  DocOrdersActivity.this,
                  TransfusionActivity.class);
              if (orderListBean.getPerformStatus().equals("0")) {
                if (isTimeOut(orderListBean.getStartDateTime(), floorDate, ceilDate)) {
                  final AlertDialogTwoBtnNoicon alertDialogTwoBtnNoicon =
                      new AlertDialogTwoBtnNoicon(this);
                  alertDialogTwoBtnNoicon.setTitle("提示");
                  alertDialogTwoBtnNoicon.setMessage("此医嘱已超过计划执行时间，是否继续？");
                  alertDialogTwoBtnNoicon.setLeftButton("否", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                    }
                  });
                  alertDialogTwoBtnNoicon.setRightButton("是", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialogTwoBtnNoicon.dismiss();
                      transfusionIntent.putExtra(
                          GlobalConstant.KEY_PATIENT, patientBean);
                      transfusionIntent.putExtra(
                          GlobalConstant.KEY_STATUS,
                          GlobalConstant.NOT_PERFORM);
                      transfusionIntent.putExtra(
                          GlobalConstant.KEY_REQID,
                          orderListBean.getRelatedBarcode());
                      transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                          GlobalConstant.KEY_PLANBARCODE);
                      transfusionIntent.putExtra(
                          GlobalConstant.KEY_FLAG_SCAN,
                          GlobalConstant.VALUE_FLAG_SCAN);
                      startActivityForResult(transfusionIntent,
                          GlobalConstant.REQUEST_CODE);
                    }
                  });
                } else {
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_PATIENT, patientBean);
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_STATUS,
                      GlobalConstant.NOT_PERFORM);
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_REQID,
                      orderListBean.getRelatedBarcode());
                  transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                      GlobalConstant.KEY_PLANBARCODE);
                  transfusionIntent.putExtra(
                      GlobalConstant.KEY_FLAG_SCAN,
                      GlobalConstant.VALUE_FLAG_SCAN);
                  startActivityForResult(transfusionIntent,
                      GlobalConstant.REQUEST_CODE);
                }

              } else if (orderListBean.getPerformStatus().equals("1")) {
                transfusionIntent.putExtra(
                    GlobalConstant.KEY_PATIENT, patientBean);
                transfusionIntent.putExtra(
                    GlobalConstant.KEY_STATUS,
                    GlobalConstant.PERFORMING);
                transfusionIntent.putExtra(
                    GlobalConstant.KEY_REQID,
                    orderListBean.getRelatedBarcode());
                transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                    GlobalConstant.KEY_PLANBARCODE);
                transfusionIntent.putExtra(
                    GlobalConstant.KEY_FLAG_SCAN_PATROL,
                    GlobalConstant.VALUE_FLAG_SCAN_PATROL);
                startActivityForResult(transfusionIntent,
                    GlobalConstant.REQUEST_CODE);
              } else if ("9".equals(orderListBean.getPerformStatus())
                  || "-1".equals(orderListBean.getPerformStatus())) {
                toImplementationRecordAct(orderListBean);
              } else {
                tostMsg(false,"核对后才能执行");
              }
            }
          }
        }
      }

    }

  }

  public void selectAll() {
    for (int i = 0; i < orders.size(); i++) {
      orders.get(i).setBatch(true);
    }
    GlobalConstant.countOfBatchOrders = orders.size();
    ordersListAdapter.notifyDataSetChanged();
  }

  public void delSelectAll() {
    for (int i = 0; i < orders.size(); i++) {
      if (orders.get(i).isBatch()) {
        orders.get(i).setBatch(false);
      }
    }
    ordersListAdapter.notifyDataSetChanged();
  }

  public void changeLayoutBatch() {
    if (GlobalConstant.PATID == null || GlobalConstant.PATID == "") {
      GlobalConstant.ISBATCH = false;
      //ShowToast("请先扫描患者腕带");
      tostMsg(false,"请先扫描患者腕带");
    } else {
      if (GlobalConstant.PATID.equals(orders.get(0).getPatId())) {
        btn_menu.setVisibility(View.INVISIBLE);
        tv_sx.setText("全选");
        rl_execute.setVisibility(View.VISIBLE);
        btn_execute.setOnClickListener(this);
        tv_orders.setText("取消");
        tv_orders.setOnClickListener(this);
        tv_time.setVisibility(View.INVISIBLE);
        tv_frequency.setVisibility(View.INVISIBLE);
        tv_perform.setVisibility(View.INVISIBLE);
        tv_flow.setVisibility(View.INVISIBLE);
        GlobalConstant.tempOrderListBean.addAll(orders);
        for (int i = orders.size() - 1; i >= 0; i--) {
          OrderListBean order = orders.get(i);
          if ("2".equals(order.getScanMode())
              || !order.getPerformStatus().equals("0")) {
            orders.remove(order);
          } else if ("1".equals(order.getTemplateType())) {
            orders.remove(order);
          }
        }
        ordersListAdapter.notifyDataSetChanged();
      } else {
        GlobalConstant.ISBATCH = false;
        //ShowToast("请先扫描患者腕带");
        tostMsg(false,"请先扫描患者腕带");
      }
    }
  }

  public void changeLayoutOriginal() {
    GlobalConstant.ISBATCH = false;
    orders.clear();
    orders.addAll(GlobalConstant.tempOrderListBean);
    delSelectAll();
    btn_menu.setVisibility(View.VISIBLE);
    tv_sx.setText("筛选");
    rl_execute.setVisibility(View.GONE);
    tv_orders.setText("医嘱");
    tv_time.setVisibility(View.VISIBLE);
    tv_frequency.setVisibility(View.VISIBLE);
    tv_perform.setVisibility(View.VISIBLE);
    tv_flow.setVisibility(View.VISIBLE);
    GlobalConstant.tempOrderListBean = new ArrayList<OrderListBean>();
  }

  /**
   * 红外扫描获取的值
   *
   * @param result
   */
  public void DoCameraResult(String result) {
    presenter.commRec(UserInfo.getUserName(), result, DeviceTool.getDeviceId(this));
    if (!result.equals(GlobalConstant.PATID)) {
      currentExecutePlanOrderNo.clear();
    }
    List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
    barcodeDicts = LocalSave.getDataList(getApplicationContext(), GlobalConstant.KEY_BARCODE);
    String barcodeName = "";
    for (int i = 0; i < barcodeDicts.size(); i++) {
      if (patternCode(barcodeDicts.get(i).getRegularExp(), result)) {
        barcodeName = barcodeDicts.get(i).getBarcodeName();
        break;
      }
    }
    if (barcodeName.equals("PAT_BARCODE")) {
      boolean flag = true;
      for (int i = 0; i < deptPatient.size(); i++) {
        if (result.equals(deptPatient.get(i).getPatBarcode())) {
          GlobalConstant.PATID = deptPatient.get(i).getPatId();
          presenter.setPatients(deptPatient.get(i));
          String patCodeResult = result;
          // 扫描腕带显示执行中医嘱
          // if (patCodeResult.contains("*")) {
          // patCodeResult = patCodeResult.split("\\*")[0];
          // }
          // presenter.loadExecutingOrders(UserInfo.getUserName(),patCodeResult,UserInfo.getWardCode());
          // 扫描一次腕带记录一次巡视
          // presenter.patralRecord(deptPatient.get(i).getPatId(), UserInfo.getUserName());
          flag = false;
          break;
        }
      }
      if (flag) {
        showExceptionDialog(getResources().getString(R.string.scan_excetion),
            getResources().getString(R.string.scan_patient_no));
      }
    } else if (barcodeName.equals("PLAN_BARCODE")) {// 医嘱码
      currentRelatedBarCode = result;
      boolean flag = true;
      int countOrders = 0;
      List<OrderListBean> list = new ArrayList<OrderListBean>();
      list.clear();
      isScan = true;
      for (int i = 0; i < orders.size(); i++) {
        if (result.equals(orders.get(i).getRelatedBarcode())) {
          flag = false;
          countOrders++;
          list.add(orders.get(i));
          currentExecutePlanOrderNo.clear();
          currentExecutePlanOrderNo.add(orders.get(i).getPlanOrderNo());
        }
      }
      if (countOrders == 1) {
        executeOrder(list.get(0));
      } else if (countOrders > 1) {
        for (int i = 0; i < list.size(); i++) {
          list.get(i).setBatch(true);
        }
        presenter.batchExecuted(UserInfo.getUserName(),
            patientBean.getPatId(), UserInfo.getWardCode(), rgTime,
            rgType, rgStatus, "8", rgClass, null, 100, list);
      }
      if (flag) {
        showExceptionDialog(getResources().getString(R.string.scan_excetion),
            getResources().getString(R.string.scan_drag_no));
      }
    } else if (barcodeName.equals("LAB_BARCODE")) {// 检验
      currentRelatedBarCode = result;
      boolean isExist = true;
      isScan = true;
      int countOrders = 0;
      List<OrderListBean> list = new ArrayList<OrderListBean>();
      list.clear();
      for (int i = 0; i < orders.size(); i++) {
        if (result.equals(orders.get(i).getRelatedBarcode())) {
          isExist = false;
          countOrders++;
          list.add(orders.get(i));
        }
      }
      if (countOrders == 1) {
        executeOrder(list.get(0));
      } else if (countOrders > 1) {
        for (int i = 0; i < list.size(); i++) {
          list.get(i).setBatch(true);
        }
        presenter.batchExecuted(UserInfo.getUserName(),
            patientBean.getPatId(), UserInfo.getWardCode(), rgTime,
            rgType, rgStatus, "8", rgClass, null, 100, list);
      }
      if (isExist) {
        showExceptionDialog(getResources().getString(R.string.scan_excetion),
            getResources().getString(R.string.scan_specimen_no));
      }
    } else if (barcodeName.equals("BLOOD_DONOR_CODE")) {// 血袋号
      currentRelatedBarCode = result;
      String planOrderNo = "";
      top.clear();
      for (int i = 0; i < orders.size(); i++) {
        if (orders.get(i).getRelatedBarcode().contains(result)) {
          top.add(orders.get(i));
        }
      }
      if (top.size() == 1) {
        planOrderNo = top.get(0).getPlanOrderNo();
        if ("1".equals(top.get(0).getStopAttr())) {
          MyToast myToast = new MyToast(this, 0, "该医嘱已取消不可执行", 3 * 1000);
          myToast.showToast();
        } else {
          presenter.getBloodDonorCode(patientBean.getPatId(), result, planOrderNo);
        }
      } else if (CollectionsTool.isEmpty(top)) {
        showExceptionDialog(getResources().getString(R.string.scan_excetion),
            getResources().getString(R.string.scan_barcode_no));
      } else {
        // 提示扫描产品码
        MyToast myToast = new MyToast(this, 0, "请扫描血袋产品码二次确认", 3 * 1000);
        myToast.showToast();
      }
    } else if (barcodeName.equals("BLOOD_PRODUCT_CODE")) {// 血袋号产品码
      if (top.size() > 1) {
        for (int j = 0; j < top.size(); j++) {
          if (top.get(j).getRelatedBarcode().equals(currentRelatedBarCode)
              && top.get(j).getRelatedBarcode2().equals(result)) {
            if ("1".equals(top.get(j).getStopAttr())) {
              MyToast myToast = new MyToast(this, 0, "该医嘱已取消不可执行", 3 * 1000);
              myToast.showToast();
            } else {
              presenter.getBloodDonorCode(patientBean.getPatId(), top.get(j).getRelatedBarcode(),
                  top.get(j).getPlanOrderNo());
            }
          }
        }
        top.clear();
      } else {
        showExceptionDialog(getResources().getString(R.string.scan_excetion),
            getResources().getString(R.string.scan_drag_no));
      }
    } else {
      showExceptionDialog(getResources().getString(R.string.scan_excetion),
          getResources().getString(R.string.scan_barcode_no));
    }

  }

  private boolean patternCode(String patternStr, String matcherStr) {
    Pattern p = Pattern.compile(patternStr.trim());
    Matcher m = p.matcher(matcherStr.trim());
    boolean b = m.matches();
    return b;
  }

  @Override
  public void intentTrans(List<BloodProductBean2> result,
      String bloodDonorCode, String scanCode) {
    if (result.size() > 1) {
      selectBlood(result);
    } else {
      boolean flag = true;
      OrderListBean order;
      for (int i = 0; i < orders.size(); i++) {
        order = orders.get(i);
        String relatedBarcodeAll = order.getRelatedBarcode();
        String bloodDonorCode1 = result.get(0).getBloodDonorCode();
        if (relatedBarcodeAll.contains(bloodDonorCode1)) {
          String relatedBarcode = order.getRelatedBarcode();
          BloodProductBean2 bean = result.get(0);
          Intent transfusionIntent = new Intent(DocOrdersActivity.this,
              TransfusionActivity.class);
          Bundle bundle = new Bundle();

          if ("0".equals(orders.get(i).getPerformStatus())) {

            transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                GlobalConstant.KEY_BLOODDONORCODE);
            transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                patientBean);
            transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
            transfusionIntent.putExtra(GlobalConstant.KEY_REQID, relatedBarcode);
            transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                GlobalConstant.NOT_PERFORM);
            transfusionIntent.putExtra(GlobalConstant.KEY_FLAG_SCAN,
                GlobalConstant.VALUE_FLAG_SCAN);
            transfusionIntent.putExtras(bundle);
            startActivityForResult(transfusionIntent,
                GlobalConstant.REQUEST_CODE);
          } else if ("1".equals(orders.get(i).getPerformStatus())) {
            if ("1".equals(bean.getBloodStatus())) {
              transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                  GlobalConstant.KEY_BLOODDONORCODE);
              transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                  patientBean);
              transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
              transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                  GlobalConstant.PERFORMING);
              transfusionIntent.putExtra(
                  GlobalConstant.KEY_FLAG_SCAN_PATROL,
                  GlobalConstant.VALUE_FLAG_SCAN_PATROL);
              startActivityForResult(transfusionIntent,
                  GlobalConstant.REQUEST_CODE);
            } else if ("0".equals(bean.getBloodStatus())) {
              transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                  GlobalConstant.KEY_BLOODDONORCODE);
              transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                  patientBean);
              transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
              transfusionIntent.putExtra(GlobalConstant.KEY_REQID, relatedBarcode);
              transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                  GlobalConstant.NOT_PERFORM);
              transfusionIntent.putExtra(GlobalConstant.KEY_FLAG_SCAN,
                  GlobalConstant.VALUE_FLAG_SCAN);
              transfusionIntent.putExtras(bundle);
              startActivityForResult(transfusionIntent,
                  GlobalConstant.REQUEST_CODE);
            } else {
              //showToast("输血已结束");
              tostMsg(false,"输血已结束");
            }

          } else if ("9".equals(orders.get(i).getPerformStatus())
              || "-1".equals(orders.get(i).getPerformStatus())) {
            toImplementationRecordAct(orders.get(i));
          } else {
            //showToast("核对后才能执行");
            tostMsg(false,"核对后才能执行");
          }
          flag = false;
          break;
        }
      }
      if (flag) {
        showToast(getString(R.string.nofindcode));
      }
    }
  }

  public void showLoadAll() {
    isLoadAllNotExecute = false;
    GlobalConstant.isCurrentShowAllNotExecute = true;
    tv_time.setText("全部未执行");
    tv_frequency.setText(GlobalConstant.DEFAULT_TEXT);
    tv_perform.setText(GlobalConstant.DEFAULT_TEXT);
    tv_flow.setText(GlobalConstant.DEFAULT_TEXT);
  }

  public void selectBlood(final List<BloodProductBean2> result) {
    alertDialogOrdersSelect =
        new AlertDialogOrdersSelect(this, new AlertDialogOrdersSelect.MySnListener() {
          @Override
          public void myOnItemClick(View view, int position, long id) {
            boolean flag = true;
            OrderListBean order;
            for (int i = 0; i < orders.size(); i++) {
              order = orders.get(i);
              String relatedBarcodeAll = order.getRelatedBarcode();
              String bloodDonorCode1 = result.get(position).getBloodDonorCode();
              if (relatedBarcodeAll.contains(bloodDonorCode1)) {
                String relatedBarcode = order.getRelatedBarcode();
                BloodProductBean2 bean = result.get(position);
                Intent transfusionIntent = new Intent(DocOrdersActivity.this,
                    TransfusionActivity.class);
                Bundle bundle = new Bundle();

                if ("0".equals(orders.get(i).getPerformStatus())) {

                  transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                      GlobalConstant.KEY_BLOODDONORCODE);
                  transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                      patientBean);
                  transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
                  transfusionIntent.putExtra(GlobalConstant.KEY_REQID, relatedBarcode);
                  transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                      GlobalConstant.NOT_PERFORM);
                  transfusionIntent.putExtra(GlobalConstant.KEY_FLAG_SCAN,
                      GlobalConstant.VALUE_FLAG_SCAN);
                  transfusionIntent.putExtras(bundle);
                  startActivityForResult(transfusionIntent,
                      GlobalConstant.REQUEST_CODE);
                } else if ("1".equals(orders.get(i).getPerformStatus())) {
                  if ("1".equals(bean.getBloodStatus())) {
                    transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                        GlobalConstant.KEY_BLOODDONORCODE);
                    transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                        patientBean);
                    transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
                    transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                        GlobalConstant.PERFORMING);
                    transfusionIntent.putExtra(
                        GlobalConstant.KEY_FLAG_SCAN_PATROL,
                        GlobalConstant.VALUE_FLAG_SCAN_PATROL);
                    startActivityForResult(transfusionIntent,
                        GlobalConstant.REQUEST_CODE);
                  } else if ("0".equals(bean.getBloodStatus())) {
                    transfusionIntent.putExtra(GlobalConstant.KEY_FLAG,
                        GlobalConstant.KEY_BLOODDONORCODE);
                    transfusionIntent.putExtra(GlobalConstant.KEY_PATIENT,
                        patientBean);
                    transfusionIntent.putExtra(GlobalConstant.KEY_BLOOD, bean);
                    transfusionIntent.putExtra(GlobalConstant.KEY_REQID, relatedBarcode);
                    transfusionIntent.putExtra(GlobalConstant.KEY_STATUS,
                        GlobalConstant.NOT_PERFORM);
                    transfusionIntent.putExtra(GlobalConstant.KEY_FLAG_SCAN,
                        GlobalConstant.VALUE_FLAG_SCAN);
                    transfusionIntent.putExtras(bundle);
                    startActivityForResult(transfusionIntent,
                        GlobalConstant.REQUEST_CODE);
                  } else {
                    //showToast("输血已结束");
                    tostMsg(false,"输血已结束");
                  }

                } else if ("9".equals(orders.get(i).getPerformStatus())
                    || "-1".equals(orders.get(i).getPerformStatus())) {
                  toImplementationRecordAct(orders.get(i));
                } else {
                  //showToast("核对后才能执行");
                  tostMsg(false,"核对后才能执行");
                }
                flag = false;
                break;
              }
            }
            if (flag) {
              showToast(getString(R.string.nofindcode));
            }
          }

        }, result);
    alertDialogOrdersSelect.show();

  }

  @Override
  public void setSelection(int position, String value,
      String planOrderNoOrReqId) {}

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
    intent.setClass(DocOrdersActivity.this, CancelExecutionAct.class);
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

  private boolean isTimeOut(String date1, int floorTime, int ceilTime) {
    Calendar calendar1 = Calendar.getInstance();
    calendar1.add(Calendar.MINUTE, ceilTime);
    Date dateCeil = calendar1.getTime();
    String ce = DateTool.formatDate(2, dateCeil);

    Calendar calendar2 = Calendar.getInstance();
    calendar2.add(Calendar.MINUTE, -floorTime);
    Date dateFloor = calendar2.getTime();
    String fl = DateTool.formatDate(2, dateFloor);

    if (StringTool.toDate(date1).before(dateCeil) && StringTool.toDate(date1).after(dateFloor)) {
      return false;
    } else {
      // return true;//过时 time:2017_3-21
      return false;

    }
  }

  @Override
  public void showBillDialog(String code, String cost, String count) {
    if (!code.equals("MNIS_ERROR")) {// 有计费
      if (code.equals("0")) {// 计费成功
        resultDiaLog = new ResultDiaLog(this, GlobalConstant.CORRECT, "计" + count + "项  ￥" + cost,
            R.style.MyDiaLog);
      } else if (code.equals("1")) {// 计费失败
        resultDiaLog = new ResultDiaLog(this, GlobalConstant.ERROR, cost, R.style.MyDiaLog);
      }
      resultDiaLog.show();
      new Handler().postDelayed(new Runnable() {
        public void run() {
          resultDiaLog.dismiss();
        }
      }, 2000);
    }
  }

  @Override
  public void batchExecuteOrderSuccess() {
    // 医嘱执行成功后开始计费
    presenter.callProcedureBill(patientBean.getPatId(), patientBean.getPatCode(),
        Integer.parseInt(patientBean.getVisitId()), currentRelatedBarCode, UserInfo.getUserName());
  }

  @Override
  public void showExceptionDialog(String title, String msg) {
    final AlertDialogOneBtn alertDialogOneBtn = new AlertDialogOneBtn(this);
    alertDialogOneBtn.setButton(getResources().getString(R.string.make_sure));
    alertDialogOneBtn.setTitle(title);
    alertDialogOneBtn.setMessage(msg);
    alertDialogOneBtn.show();
    alertDialogOneBtn.setButtonListener(true, new OnClickListener() {
      @Override
      public void onClick(View view) {
        alertDialogOneBtn.dismiss();
      }
    });
  }

  @Override
  public void tostMsg(boolean isVibration, String msg) {
    if (isVibration) {
      checkOrdersFailed();
    }
    MyToast myToast = new MyToast(this, 0, msg, 3 * 1000);
    myToast.showToast();
  }

  public void checkOrdersFailed() {
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
    vibrator.vibrate(pattern, -1);
  }
}
