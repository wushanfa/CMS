package com.gentlehealthcare.mobilecare.activity.implementation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ImplementationRecordAct extends BaseActivity implements View
        .OnClickListener {
    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.btn_box)
    private Button btn_box;
    @ViewInject(R.id.tv_order_title)
    private TextView tv_order_title;

    private TextView tv_order_context;
    @ViewInject(R.id.tv_dosage)
    private TextView tv_dosage;

    @ViewInject(R.id.tv_pat_name)
    private TextView tv_pat_name;
    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_pat_code)
    private TextView tv_pat_code;

    @ViewInject(R.id.btn_menu)
    private Button btn_menu;
    @ViewInject(R.id.navbar_rg)
    private RadioGroup navbar_rg;
    @ViewInject(R.id.tv_date)
    private TextView tv_date;
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
    int mLastFragmentTag = 1;
    SyncPatientBean patientBean = null;
    OrderListBean orderListBean = null;
    private ProgressDialog progressDialog;

    private int lineCount = 0;

    public static final int INDEX_TAG = 1;
    public static final int RECORD_TAG = 2;
    public static final int INTRODUCTION_TAG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_implementation_record);
        ViewUtils.inject(this);
        tv_order_context = (TextView) findViewById(R.id.tv_order_context);
        btn_menu.setVisibility(View.INVISIBLE);
        ReceiveData();
        intial();
        navbar_rg.check(R.id.rb_xq);
    }

    private void ReceiveData() {
        orderListBean = (OrderListBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERLISTBEAN);
        patientBean = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        setPatientInfo(patientBean);
        setOrdersContext(orderListBean);
    }

    private void intial() {
        tv_bed_label.setCompoundDrawables(null, null, null, null);
        btn_back.setOnClickListener(this);
        navbar_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_xq:
                        intialFra(INDEX_TAG);
                        break;
                    case R.id.rb_jl:
                        if (Integer.parseInt(orderListBean.getPerformStatus()) > 0 && Integer.parseInt
                                (orderListBean.getTemplateType()) == 0) {
                            intialFra(RECORD_TAG);
                        }
                        break;
                    case R.id.rb_sm:
                        intialFra(INTRODUCTION_TAG);
                        break;
                }
            }
        });

        if (findViewById(R.id.fl_container) != null) {
            IndexFra indexFra = (IndexFra) ImplementationFraFactory.getInstanceByIndex(1);
            Bundle bundle = new Bundle();
            bundle.putString(GlobalConstant.KEY_PATID, patientBean.getPatId());
            bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
            indexFra.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, indexFra, INDEX_TAG + "")
                    .addToBackStack(null)
                    .commit();
            mLastFragmentTag = INDEX_TAG;

        }
    }

    private void intialFra(int checkedId) {

        if (checkedId != mLastFragmentTag) {
            Fragment lastFragment = getSupportFragmentManager().findFragmentByTag(mLastFragmentTag + "");
            Bundle bundle = new Bundle();
            bundle.putString(GlobalConstant.KEY_PATID, patientBean.getPatId());
            bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
            if (getSupportFragmentManager().findFragmentByTag(checkedId + "") != null) {
                Fragment fragmentNow = getSupportFragmentManager().findFragmentByTag(checkedId + "");
                getSupportFragmentManager().beginTransaction()
                        .show(fragmentNow)
                        .hide(lastFragment)
                        .commit();
            } else {
                Fragment fragment = ImplementationFraFactory.getInstanceByIndex(checkedId);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_container, fragment, checkedId + "")
                        .addToBackStack(null)
                        .hide(lastFragment)
                        .commit();
            }
            mLastFragmentTag = checkedId;
        }
    }

    @Override
    protected void resetLayout() {

    }

    public void setPatientInfo(SyncPatientBean syncPatientBean) {
        tv_bed_label.setText(patientBean.getBedLabel() + getString(R.string.bed));
        tv_pat_name.setText(patientBean.getName());
        tv_sex.setText(patientBean.getSex());
        tv_pat_code.setText(patientBean.getPatCode());
    }


    public void setOrdersContext(OrderListBean orderListBean) {
        if("1".equals(orderListBean.getStopAttr())){
            btn_box.setBackgroundResource(R.drawable.btn_check_cancel);
        }else{
            if (orderListBean.getPerformStatus().equals("9")) {
                btn_box.setBackgroundResource(R.drawable.btn_finish);
            } else if (orderListBean.getPerformStatus().equals("1")) {
                btn_box.setBackgroundResource(R.drawable.btn_exceting);
            } else if (orderListBean.getPerformStatus().equals("0")) {
                btn_box.setBackgroundResource(R.drawable.btn_check_off_holo_light);
            } else if (orderListBean.getPerformStatus().equals("-1")) {
                btn_box.setBackgroundResource(R.drawable.btn_exception);
            }
        }
        if ("1".equals(orderListBean.getStopAttr())) {
            tv_order_context.setTextColor(Color.RED);
            tv_order_title.setTextColor(Color.RED);
        }
        String title = StringTool.dateToTime(orderListBean.getStartTime().toString());
        String nursingDesc = orderListBean.getNursingDesc();
        if (TextUtils.isEmpty(nursingDesc)) {
            tv_order_title.setText(title);
        } else {
            tv_order_title.setText(title + " " + nursingDesc);
        }
        tv_order_context.setText(StringTool.replaceStr(orderListBean.getOrderText()));
        if (TextUtils.isEmpty(orderListBean.getDosage())) {
            tv_dosage.setText("");
        } else {
            String dosage = StringTool.replaceStr(orderListBean.getDosage());
            String nullToSpace = dosage.replace("null", "");
            tv_dosage.setText(nullToSpace);
        }
        tv_date.setText(DateTool.todayTomorryYesterday(orderListBean.getStartTime()));
        if ("1".equals(orderListBean.getRepeatIndicator())) {
            tv_order_start.setText("医嘱开始时间");
            tv_order_end_time.setText("医嘱结束时间");
            tv_order_operation.setText(orderListBean.getOrderedDoctor());
            tv_order_start_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStartDateTime() + ""));
            if (!TextUtils.isEmpty(orderListBean.getStopDateTime())) {
                tv_order_end_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStopDateTime()));
            } else {
                //tv_order_end_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStopDateTime()));
                tv_order_end_time.setText(" ");

            }
            if (!TextUtils.isEmpty(orderListBean.getStopDoctor())) {
                tv_order_end_operation.setText(orderListBean.getStopDoctor());
            } else {
                tv_order_end_operation.setText(" ");
            }
        } else {
            tv_order_start.setText("医嘱时间");
            tv_order_start_time.setText(StringTool.monthDayTimeNoSecond(orderListBean.getStartDateTime() + ""));
            tv_order_operation.setText(orderListBean.getOrderedDoctor());
            rl_order_end_time.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
                setResult(GlobalConstant.RESUlt_CODE, intent);
                GlobalConstant.tempEventId = "";
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
            setResult(GlobalConstant.RESUlt_CODE, intent);
            GlobalConstant.tempEventId = "";
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
