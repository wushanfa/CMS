package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousExecuteView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药执行界面
 */
@SuppressLint("ValidFragment")
public class IntravExecuteFra extends BaseFragment
    implements
      View.OnClickListener,
      IIntravenousExecuteView {


  private Button btn_back;
  private EditText et_sp;
  private EditText edt_start_time;
  private EditText edt_end_time;
  private EditText edt_next_patral_time;
  private Button btn_save;
  private ProgressDialog progressDialog = null;
  private IntravenousPresenter intravenousPresenter;
  private String patId = null;
  private OrderListBean orderListBean;
  private String dateString = "";
  String speed = "";
  private boolean isEnd = false;
  private SlideDateTimeListener listener = new SlideDateTimeListener() {
    @Override
    public void onDateTimeSet(Date date) {
      dateString = DateTool.formatDate(GlobalConstant.DATE_TIME_SIMPLE, date);
      if (isEnd) {
        edt_end_time.setText(dateString);
      } else {
        edt_next_patral_time.setText(dateString);
      }
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fra_intrayexecute, null);
    intravenousPresenter = new IntravenousPresenter(null, this, null, null);
    intialSource(view);
    getData();
    return view;
  }

  private void intialSource(View view) {
    edt_start_time = (EditText) view.findViewById(R.id.edt_start_time);
    et_sp = (EditText) view.findViewById(R.id.et_sp);
    edt_end_time = (EditText) view.findViewById(R.id.edt_end_time);
    edt_next_patral_time = (EditText) view.findViewById(R.id.edt_next_patral_time);
    btn_save = (Button) view.findViewById(R.id.btn_save);
    btn_save.setOnClickListener(this);
    btn_back = (Button) view.findViewById(R.id.btn_back);
    edt_start_time.setText(DateTool.getCurrDateTimeS());
    edt_start_time.setOnClickListener(this);
    edt_end_time.setText(dateString);
    edt_end_time.setOnClickListener(this);
    edt_next_patral_time.setText("");
    edt_next_patral_time.setOnClickListener(this);
  }

  private void getData() {
    patId = getArguments().getString(GlobalConstant.KEY_PATID);
    orderListBean =
        (OrderListBean) getArguments().getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
    if (TextUtils.isEmpty(orderListBean.getSpeed())) {
      et_sp.setText("");// BUG 公司库出现乱码
    } else {
      speed = orderListBean.getSpeed();
      et_sp.setText(speed);
      et_sp.setSelection(et_sp.getText().length());
      et_sp.getSelectionStart();
    }

    if (!TextUtils.isEmpty(orderListBean.getInspectionTime())) {
      edt_next_patral_time.setText(orderListBean.getInspectionTime());
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_back:
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        getActivity().finish();
        break;
      case R.id.btn_save:
        execute();
        break;
      case R.id.edt_start_time:
        // intravenousPresenter.showSlideDateTimerPicker();
        break;
      case R.id.edt_end_time:
        isEnd = true;
        intravenousPresenter.showSlideDateTimerPicker();
        break;
      case R.id.edt_next_patral_time:
        isEnd = false;
        intravenousPresenter.showSlideDateTimerPicker();
        break;
    }
  }

  @Override
  protected void resetLayout(View view) {

  }

  @Override
  public void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(getActivity());
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

  @Override
  public void saveNextPatrolTime() {
    intravenousPresenter.saveNextPatrolTime(patId, UserInfo.getUserName(),
        orderListBean.getPlanOrderNo(),
        edt_next_patral_time.getText().toString());
  }

  @Override
  public void showToast(String msg) {
    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void handException() {

  }

  @Override
  public void backToDoctorOrdersAct() {
    Intent intent = new Intent();
    intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
    getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
    getActivity().finish();
  }

  @Override
  public void showSlideDateTimerPicker() {
    new SlideDateTimePicker.Builder(getFragmentManager())
        .setListener(listener)
        .setInitialDate(new Date())
        // .setMinDate(0)
        // .setMaxDate(maxDate)
        .setIs24HourTime(true)
        .setTheme(SlideDateTimePicker.HOLO_LIGHT)
        .setIndicatorColor(Color.parseColor("#2ba3d5"))
        .build()
        .show();
  }

  public void setScanResult(String result) {
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
        GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
    String planBarcode = sharedPreferences.getString(
        GlobalConstant.KEY_PLANBARCODE, "");
    if (patternCode(planBarcode, result)) {
      if (result.equals(orderListBean.getRelatedBarcode())) {
        execute();
      } else {
        showToast("请扫描当前医嘱码");
      }
    } else {
      showToast("请扫描医嘱码");
    }

  }

  private boolean patternCode(String patternStr, String matcherStr) {
    Pattern p = Pattern.compile(patternStr.trim());
    Matcher m = p.matcher(matcherStr.trim());
    boolean b = m.matches();
    return b;
  }

  private void execute() {
    if (TextUtils.equals(orderListBean.getPerformStatus(), "0")) {
      // 2017-3-24 目前有乱码问题，暂时不取滴速值
       speed = et_sp.getText().toString();
       intravenousPresenter.startIntravenous(patId, speed, dateString, orderListBean
       .getInjectionTool(), orderListBean.getPlanOrderNo(), edt_next_patral_time.getText()
       .toString());
    } else if (TextUtils.equals(orderListBean.getPerformStatus(), "1")) {
      showToast("该药品已被执行");
    } else if (TextUtils.equals(orderListBean.getPerformStatus(), "9")) {
      showToast("该药品已执行结束");
    } else if (TextUtils.equals(orderListBean.getPerformStatus(), "-1")) {
      showToast("该药品已结束");
    }
  }

}
