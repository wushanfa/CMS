package com.gentlehealthcare.mobilecare.activity.cancelexecution;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.CancelExecutionPresenter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.ICancelException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class CancelExecutionAct extends BaseActivity implements ICancelException, OnClickListener {
    @ViewInject(R.id.edt_start_time)
    EditText edt_start_time;
    @ViewInject(R.id.edt_ps_jg)
    EditText edt_ps_jg;
    @ViewInject(R.id.edt_exception_dec)
    EditText edt_exception_dec;
    @ViewInject(R.id.btn_save)
    Button btn_save;
    @ViewInject(R.id.btn_back)
    Button btn_back;
    @ViewInject(R.id.btn_menu)
    Button btn_menu;
    @ViewInject(R.id.tv_pat_name)
    TextView tv_pat_name;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.tv_bed_label)
    TextView tv_bed_label;
    @ViewInject(R.id.tv_pat_code)
    TextView tv_pat_code;
    @ViewInject(R.id.btn_box)
    Button btn_box;
    @ViewInject(R.id.tv_order_title)
    TextView tv_order_title;
    @ViewInject(R.id.tv_order_time)
    TextView tv_order_time;
    TextView tv_order_context;
    @ViewInject(R.id.tv_dosage)
    TextView tv_dosage;
    @ViewInject(R.id.rl_ps_jg)
    RelativeLayout rl_ps_jg;
    @ViewInject(R.id.rl_rcception_dec)
    RelativeLayout rl_rcception_dec;
    @ViewInject(R.id.imageView_line3)
    ImageView imageView_line3;
    @ViewInject(R.id.tv_order_start_time)
    TextView tv_order_start_time;
    @ViewInject(R.id.tv_order_operation)
    TextView tv_order_operation;
    @ViewInject(R.id.rl_order_start_time)
    RelativeLayout rl_order_start_time;
    @ViewInject(R.id.rl_order_end_time)
    RelativeLayout rl_order_end_time;
    @ViewInject(R.id.tv_order_end_time)
    TextView tv_order_end_time;
    @ViewInject(R.id.tv_order_end_operation)
    TextView tv_order_end_operation;
    @ViewInject(R.id.tv_order_start)
    TextView tv_order_start;
    /**
     * 从医嘱界面传递的参数
     */
    private OrderListBean orderListBean;
    private SyncPatientBean patientBean;
    private CancelExecutionPresenter presenter = null;
    private int lineCount = 0;
    private String exceptionCode = "A1";
    private String exceptionText = "患者原因";
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_execution);
        ViewUtils.inject(this);
        tv_order_context = (TextView) findViewById(R.id.tv_order_context);
        rl_ps_jg.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        rl_rcception_dec.setOnClickListener(this);
        btn_menu.setVisibility(View.INVISIBLE);
        btn_back.setOnClickListener(this);
        presenter = new CancelExecutionPresenter(this);
        presenter.receiveData();
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    public void receiveData() {
        orderListBean = (OrderListBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERLISTBEAN);
        patientBean = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        presenter.setOrderContext(orderListBean);
        presenter.setPatientInfo(patientBean);
    }

    @Override
    public void setPatientInfo(SyncPatientBean syncPatientBean) {
        tv_bed_label.setCompoundDrawables(null, null, null, null);
        tv_bed_label.setText(patientBean.getBedLabel() + getString(R.string.bed));
        tv_pat_name.setText(patientBean.getName());
        tv_sex.setText(patientBean.getSex());
        tv_pat_code.setText(patientBean.getPatCode());
    }

    @Override
    public void setOrdersContext(OrderListBean orderContext) {
        if ("1".equals(orderContext.getStopAttr())) {
            btn_box.setBackgroundResource(R.drawable.btn_check_cancel);
        } else {
            if (orderContext.getPerformStatus().equals("9")) {
                btn_box.setBackgroundResource(R.drawable.btn_finish);
            } else if (orderContext.getPerformStatus().equals("0")) {
                btn_box.setBackgroundResource(R.drawable.btn_check_off_holo_light);
            } else if (orderContext.getPerformStatus().equals("1")) {
                btn_box.setBackgroundResource(R.drawable.btn_exceting);
            } else if (orderContext.getPerformStatus().equals("-1")) {
                btn_box.setBackgroundResource(R.drawable.btn_exception);
            }
        }
        tv_order_time.setText(DateTool.todayTomorryYesterday(orderContext.getStartTime()));
        if (TextUtils.isEmpty(orderContext.getNursingDesc())) {
            tv_order_title.setText(StringTool.dateToTime(orderContext.getStartTime()));
        } else {
            tv_order_title.setText(StringTool.dateToTime(orderContext.getStartTime()) + "  " + orderContext
                    .getNursingDesc());
        }
        if ("1".equals(orderContext.getStopAttr())) {
            tv_order_context.setTextColor(Color.RED);
            tv_order_title.setTextColor(Color.RED);
        }
        tv_order_context.setText(StringTool.replaceStr(orderContext.getOrderText()));
        if (TextUtils.isEmpty(orderContext.getDosage())) {
            tv_dosage.setText("");
        } else {
            String dosage = StringTool.replaceStr(orderContext.getDosage());
            String nullToSpace = dosage.replace("null", "");
            tv_dosage.setText(nullToSpace);
        }
        if (TextUtils.isEmpty(orderContext.getSkinTest())) {
            rl_ps_jg.setVisibility(View.INVISIBLE);
            imageView_line3.setVisibility(View.INVISIBLE);
        } else {
            if ("0".equals(orderContext.getSkinTest())) {
                rl_ps_jg.setVisibility(View.INVISIBLE);
                imageView_line3.setVisibility(View.INVISIBLE);
            }
        }
        edt_start_time.setText(DateTool.getCurrDateTimeS());
        if ("1".equals(orderListBean.getRepeatIndicator())) {
            tv_order_start.setText("医嘱开始时间");
            tv_order_end_time.setText("医嘱结束时间");
            tv_order_operation.setText(orderListBean.getOrderedDoctor());
            tv_order_start_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStartDateTime() + ""));
            if (!TextUtils.isEmpty(orderListBean.getStopDateTime())) {
                tv_order_end_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStopDateTime()));
            } else {
                tv_order_end_time.setText(" ");
            }
            if (!TextUtils.isEmpty(orderListBean.getStopDoctor())) {
                tv_order_end_operation.setText(orderListBean.getStopDoctor());
            } else {
                tv_order_end_operation.setText("");
            }
        } else {
            tv_order_start.setText("医嘱时间");
            tv_order_start_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStartDateTime() + ""));
            tv_order_operation.setText(orderListBean.getOrderedDoctor());
            rl_order_end_time.setVisibility(View.GONE);
        }
    }

    @Override
    public void showExceptionDic(final List<IntraDontExcuteBean> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        int length = list.size();
        if (length > 0) {
            String[] dict = new String[length];
            for (int i = 0; i < length; i++) {
                dict[i] = list.get(i).getItemName();
            }
            builder.setItems(dict, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exceptionCode = list.get(which).getItemCode();
                    exceptionText = list.get(which).getItemName();
                    edt_exception_dec.setText(exceptionText);
                }
            }).setCancelable(true);
            builder.show();
        }
    }

    @Override
    public void cancelExecutionCallBack(String msg) {
        if (GlobalConstant.SUCCEED.equals(msg)) {
            backToOrders();
            showToast("医嘱取消成功");
        } else {
            showToast("医嘱取消失败");
        }
    }

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

    @Override
    public void showToast(String msg) {
        ShowToast(msg);
    }

    @Override
    public void handException() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_ps_jg:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                final String[] dict = new String[]{"阴性(-)", "阳性(+)"};

                builder.setItems(dict, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_ps_jg.setText(dict[which]);
                    }
                }).setCancelable(true);
                builder.show();
                break;
            case R.id.rl_rcception_dec:
                presenter.loadVariationDict();
                break;
            case R.id.btn_save:
                if (orderListBean.getWardCode().equals(UserInfo.getWardCode())) {
                    if (orderListBean.getStopAttr().equals("1")) {
                        showToast("此医嘱已取消");
                    } else {
                        if ("0".equals(orderListBean.getPerformStatus())||"U".equals(orderListBean.getPerformStatus())) {
                            presenter.cancelExecution(UserInfo.getUserName(), UserInfo.getWardCode(),
                                    orderListBean.getOrderId(), orderListBean.getPlanOrderNo(), "取消", exceptionCode, exceptionText, patientBean
                                            .getPatId());
                        } else {
                            if ("9".equals(orderListBean.getPerformStatus()) || "1".equals(orderListBean.getPerformStatus()) || "-1".equals(orderListBean.getPerformStatus()) || "U".equals(orderListBean.getPerformStatus())) {
                                if (UserInfo.getCapability() == null || UserInfo.getCapability() == "") {
                                    showToast("用户权限字段为空");
                                } else {
                                    if (UserInfo.getCapability().equals("6")) {
                                        presenter.cancelExecution(UserInfo.getUserName(), UserInfo.getWardCode(),
                                                orderListBean.getOrderId(), orderListBean.getPlanOrderNo(),
                                                "取消", exceptionCode, exceptionText,
                                                patientBean.getPatId());
                                    } else {
                                        showToast("您没有权限取消医嘱");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    showToast("非本护理单元执行记录");
                }
                break;
            case R.id.btn_back:
                backToOrders();
                break;
        }
    }

    private void backToOrders() {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        setResult(GlobalConstant.RESUlt_CODE, intent);
        GlobalConstant.tempEventId = "";
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToOrders();
        }
        return super.onKeyDown(keyCode, event);
    }
}
