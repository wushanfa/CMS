package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.insulin.InjectionFragment;
import com.gentlehealthcare.mobilecare.activity.insulin.OverviewFragment;
import com.gentlehealthcare.mobilecare.activity.insulin.PatrolFragment;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 静脉给药流程
 * author:zyy
 */
public class IntravenousAct extends BaseActivity implements IIntravenousView, View.OnClickListener {

    @ViewInject(R.id.fl_container)
    private FrameLayout fl_container;

    @ViewInject(R.id.rd_gy)
    private RadioButton rd_gy;
    @ViewInject(R.id.rd_xs)
    private RadioButton rd_xs;
    @ViewInject(R.id.rd_fg)
    private RadioButton rd_fg;
    @ViewInject(R.id.rd_sm)
    private RadioButton rd_sm;


    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_pat_code)
    private TextView tv_pat_code;
    @ViewInject(R.id.tv_pat_name)
    private TextView tv_pat_name;
    @ViewInject(R.id.btn_menu)
    private Button btn_menu;
    @ViewInject(R.id.btn_box)
    private Button btn_box;
    @ViewInject(R.id.tv_order_title)
    private TextView tv_order_title;
    private TextView tv_order_context;
    @ViewInject(R.id.tv_dosage)
    private TextView tv_dosage;
    @ViewInject(R.id.navbar_rg)
    private RadioGroup navbar_rg;
    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;

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
    /**
     * 1 给药
     * 2 巡视
     * 3 封管
     * 4 说明
     */
    private int whichOne = 1;
    IntravenousPresenter intravenousPresenter = null;
    private int lineCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intravenous2);
        ViewUtils.inject(this);
        tv_order_context = (TextView) findViewById(R.id.tv_order_context);
        btn_back.setOnClickListener(this);
        intravenousPresenter = new IntravenousPresenter(this, null, null, null);
        intravenousPresenter.iIntravenousReceiveData();
        btn_menu.setVisibility(View.INVISIBLE);
        tv_bed_label.setCompoundDrawables(null, null, null, null);
        whichOneFra(whichOne);
    }


    private void whichOneFra(int whichOne) {
        fragmentManager = getSupportFragmentManager();
        switch (whichOne) {
            case 1:
                navbar_rg.check(rd_gy.getId());
                intialFra(rd_gy.getId());
                break;
            case 2:
                navbar_rg.check(rd_xs.getId());
                intialFra(rd_xs.getId());
                break;
            case 3:
                navbar_rg.check(rd_fg.getId());
                intialFra(rd_fg.getId());
                break;
            case 4:
                navbar_rg.check(rd_sm.getId());
                intialFra(rd_sm.getId());
                break;
        }

        navbar_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                intialFra(checkedId);
            }
        });
    }

    private void intialFra(int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = IntravenousFraFactory.getInstanceByIndex(checkedId);
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.KEY_PATID, patientBean.getPatId());
        bundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
        fragment.setArguments(bundle);
        transaction.replace(R.id.fl_container, fragment);
        transaction.commit();
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    public void setPatientInfo(SyncPatientBean patient) {
        tv_bed_label.setText(patientBean.getBedLabel() + getString(R.string.bed));
        tv_pat_name.setText(patientBean.getName());
        tv_sex.setText(patientBean.getSex());
        tv_pat_code.setText(patientBean.getPatCode());
    }

    @Override
    public void setOrderContext(final OrderListBean orderContext) {
        if (orderContext.getPerformStatus().equals("9")) {
            btn_box.setBackgroundResource(R.drawable.btn_finish);
        } else if (orderContext.getPerformStatus().equals("0")) {
            btn_box.setBackgroundResource(R.drawable.btn_check_off_holo_light);
        } else if (orderContext.getPerformStatus().equals("1")) {
            btn_box.setBackgroundResource(R.drawable.btn_exceting);
        } else if (orderContext.getPerformStatus().equals("-1")) {
            btn_box.setBackgroundResource(R.drawable.btn_exception);
        }
        tv_order_time.setText(DateTool.todayTomorryYesterday(orderContext.getStartTime()));
        tv_order_title.setText(StringTool.pieceSection(orderContext));
        tv_order_context.setText(StringTool.replaceStr(orderContext.getOrderText()));
        if(TextUtils.isEmpty(orderContext.getDosage())){
            tv_dosage.setText("");
        }else{
            String dosage=StringTool.replaceStr(orderContext.getDosage());
            String nullToSpace=dosage.replace("null","");
            tv_dosage.setText(nullToSpace);
        }
    }

    @Override
    public void receiveData() {
        orderListBean = (OrderListBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERLISTBEAN);
        patientBean = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        intravenousPresenter.setPatientInfo(patientBean);
        intravenousPresenter.setOrderContext(orderListBean);
        String status = orderListBean.getPerformStatus();
        if (TextUtils.equals(status, "0")) {
            whichOne = 1;
        } else if (TextUtils.equals(status, "1")) {
            whichOne = 2;
        } else if (TextUtils.equals(status, "9")) {
            whichOne = 3;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
                setResult(GlobalConstant.RESUlt_CODE, intent);
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
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        intravenousPresenter.commRec(UserInfo.getUserName(), result, DeviceTool.getDeviceId(this));
        Fragment currentFra = getSupportFragmentManager().findFragmentById(R.id
                .fl_container);
        if (currentFra instanceof IntravExecuteFra) {
            IntravExecuteFra intravExecuteFra = (IntravExecuteFra) currentFra;
            intravExecuteFra.setScanResult(result);
        }else if(currentFra instanceof IntravPatrolFra){
            IntravPatrolFra intravPatrolFra = (IntravPatrolFra) currentFra;
            intravPatrolFra.setScanResult(result);
        }else if(currentFra instanceof IntravSealingFra){
            IntravSealingFra intravSealingFra = (IntravSealingFra) currentFra;
            intravSealingFra.setScanResult(result);
        }
    }
}
