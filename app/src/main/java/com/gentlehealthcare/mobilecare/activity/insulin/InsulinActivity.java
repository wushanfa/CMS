package com.gentlehealthcare.mobilecare.activity.insulin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class InsulinActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.fl_container)
    private FrameLayout fl_container;

    @ViewInject(R.id.rd_first)
    private RadioButton rd_first;
    @ViewInject(R.id.rd_second)
    private RadioButton rd_second;
    @ViewInject(R.id.rd_third)
    private RadioButton rd_third;
    @ViewInject(R.id.rd_fourth)
    private RadioButton rd_fourth;
    @ViewInject(R.id.rd_fifth)
    private RadioButton rd_fifth;

    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_pat_code)
    private TextView tv_pat_code;
    @ViewInject(R.id.btn_menu)
    private Button btn_menu;
    @ViewInject(R.id.btn_box)
    private Button btn_box;
    @ViewInject(R.id.tv_order_title)
    private TextView tv_order_title;
    private TextView tv_order_context;
    @ViewInject(R.id.tv_dosage)
    private TextView tv_dosage;
    @ViewInject(R.id.tv_pat_name)
    private TextView tv_pat_name;
    @ViewInject(R.id.tv_date)
    private TextView tv_date;

    private int lineCount = 0;

    /**
     * 从医嘱界面传递的参数
     */
    private OrderListBean orderListBean;
    private SyncPatientBean patientBean;
    private String status;

    /**
     * fragment管理
     */
    private FragmentManager fragmentManager;
    private OverviewFragment overviewFragment = new OverviewFragment();
    private InjectionFragment injectionFragment = new InjectionFragment();
    private PatrolFragment patrolFragment = new PatrolFragment();
    /**
     * 进度条
     */
    private ProgressDialog progressDialog;

    private boolean ischeckSecond = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_flow_container);
        ViewUtils.inject(this);

        tv_order_context = (TextView) findViewById(R.id.tv_order_context);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.datasaving));

        orderListBean = (OrderListBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERLISTBEAN);
        patientBean = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        fragmentManager = getSupportFragmentManager();

        initView();

        status = orderListBean.getPerformStatus();
        //判断装载哪个fragment
        if (status.equals(GlobalConstant.NOT_PERFORM)) {
            Bundle noPerform = new Bundle();
            noPerform.putString("patId", patientBean.getPatId());
            noPerform.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
            injectionFragment.setArguments(noPerform);
            fragmentManager.beginTransaction().replace(R.id.fl_container, injectionFragment)
                    .commit();
            rd_first.setChecked(true);
            rd_second.setChecked(false);
            rd_second.setClickable(false);
        } else if (status.equals(GlobalConstant.PERFORMING)) {
            //TODO overviewFragment，现在先不跳转，等确定不需要这个界面的时候，可以删掉下面的代码
//            Bundle performing = new Bundle();
//            performing.putString("patId", patientBean.getPatId());
//            performing.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
//            overviewFragment.setArguments(performing);
//            fragmentManager.beginTransaction().replace(R.id.fl_container, overviewFragment).commit();
//            rd_first.setClickable(false);
//            rd_first.setChecked(false);
//            rd_second.setChecked(false);
            //TODO 替换成如下代码
            Bundle performing = new Bundle();
            performing.putString("patId", patientBean.getPatId());
            performing.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
            performing.putString("time", (String) orderListBean.getEventEndTime());
            patrolFragment.setArguments(performing);
            fragmentManager.beginTransaction().replace(R.id.fl_container, patrolFragment).commit();
            rd_first.setChecked(false);
            rd_second.setChecked(true);
            rd_first.setClickable(false);
        } else if (status.equals(GlobalConstant.PERFORMED)) {
            Bundle performing = new Bundle();
            performing.putString("patId", patientBean.getPatId());
            performing.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
            overviewFragment.setArguments(performing);
            fragmentManager.beginTransaction().replace(R.id.fl_container, overviewFragment).commit();
            rd_first.setClickable(false);
            rd_second.setClickable(false);
            rd_first.setChecked(false);
            rd_second.setChecked(false);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rd_second:
//                if (!ischeckSecond) {
//                    Bundle performing = new Bundle();
//                    performing.putString("patId", patientBean.getPatId());
//                    performing.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
//                    performing.putString("time", (String) orderListBean.getEventEndTime());
//                    patrolFragment.setArguments(performing);
//                    fragmentManager.beginTransaction().replace(R.id.fl_container, patrolFragment).addToBackStack
//                            (null).commit();
//                    rd_first.setChecked(false);
//                    rd_second.setChecked(true);
//                    ischeckSecond = rd_second.isChecked();
//                }
                break;
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
                setResult(GlobalConstant.RESUlt_CODE, intent);
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
            setResult(GlobalConstant.RESUlt_CODE, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        rd_first.setText("给药");
        rd_second.setText("巡视");
        rd_third.setText("");
        rd_fourth.setText("");
        rd_fifth.setText("");
        rd_first.setOnClickListener(this);
        rd_second.setOnClickListener(this);
        btn_menu.setVisibility(View.GONE);
        btn_back.setOnClickListener(this);
        tv_sex.setText(patientBean.getSex());
        tv_bed_label.setText(patientBean.getBedLabel() + "床");
        tv_pat_code.setText(patientBean.getPatCode());
        tv_pat_name.setText(patientBean.getName());

        tv_order_title.setText(StringTool.pieceSection(orderListBean));
        String OrderContext = StringTool.replaceStr(orderListBean.getOrderText());
        tv_order_context.setText(OrderContext);

        if(TextUtils.isEmpty(orderListBean.getDosage())){
            tv_dosage.setText("");
        }else{
            String dosage=StringTool.replaceStr(orderListBean.getDosage());
            String nullToSpace=dosage.replace("null","");
            tv_dosage.setText(nullToSpace);
        }
//        tv_order_context.setOnLayoutListener(orderListBean, new JustifyTextView.OnLayoutListener() {
//            @Override
//            public void onLayouted(OrderListBean bean, TextView view) {
//                lineCount = view.getLineCount();
//                String orderDosage = StringTool.replaceStrForLine(bean, lineCount);
//                tv_dosage.setText(orderDosage);
//            }
//        });
        if (TextUtils.equals(orderListBean.getPerformStatus(), "0")) {
            btn_box.setBackgroundResource(R.drawable.btn_check_off_holo_light);
        } else if (TextUtils.equals(orderListBean.getPerformStatus(), "1")) {
            btn_box.setBackgroundResource(R.drawable.btn_exceting);
        } else if (TextUtils.equals(orderListBean.getPerformStatus(), "9")) {
            btn_box.setBackgroundResource(R.drawable.btn_finish);
        } else if (TextUtils.equals(orderListBean.getPerformStatus(), "-1")) {
            btn_box.setBackgroundResource(R.drawable.btn_exception);
        }
        tv_date.setText(DateTool.todayTomorryYesterday(orderListBean.getStartTime()));
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_flow);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
