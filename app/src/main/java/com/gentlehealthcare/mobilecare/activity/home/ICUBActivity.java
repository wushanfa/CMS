package com.gentlehealthcare.mobilecare.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBTotalBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.ICUDataMethod;
import com.gentlehealthcare.mobilecare.tool.ICUResourceSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.HorizontalListView;
import com.gentlehealthcare.mobilecare.view.MySwitchNameDialog;
import com.gentlehealthcare.mobilecare.view.adapter.GridViewICUAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.ICUBTitleBarAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyy
 */
public class ICUBActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "ICUBctivity";
    /**
     * 导航栏
     */
    private HorizontalListView navbar_lv;
    private ICUBTitleBarAdapter adapter;
    private List<String> mytitle = new ArrayList<String>();
    @ViewInject(R.id.btn_back)
    private Button btn_back;
    /**
     * 护理措施
     */
    private NursingMeasure measure = new NursingMeasure();
    /**
     * 护理计划
     */
    private NursingPlan plan = new NursingPlan();
    /**
     * 护理评价
     */
    private NursingEvaluation evaluation = new NursingEvaluation();
    /**
     * 评估内容
     */
    private AssessMessage assessMessage = new AssessMessage();
    /**
     * 骨伤科情况
     */
    private Orthopedics orthopedics = new Orthopedics();
    /**
     * 脊柱外科
     */
    private Spine spine = new Spine();
    /**
     * 交班小结
     */
    private TurnOverDuty turnOverDuty = new TurnOverDuty();

    private TextView tv_head;
    public final static int RESULT_CODE = 123;
    /**
     * 切换病人
     */
    @ViewInject(R.id.ll_icu_patient_layout)
    private LinearLayout ll_icu_patient_layout;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    private MySwitchNameDialog nameDialog;
    private int whichPatient = 0;
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();
    /**
     * 从icuA中获得item
     */
    private LoadIcuBTotalBean totalBean = new LoadIcuBTotalBean();

    /**
     * 定义orgNUm防止fragment narBar重复点击
     */
    private int orgNum = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_icub);
        HidnGestWindow(true);
        ViewUtils.inject(this);
        whichPatient = getIntent().getIntExtra("whichPatient", 0);
        patients = (List<SyncPatientBean>) getIntent().getSerializableExtra("patients");
        totalBean = (LoadIcuBTotalBean) getIntent().getSerializableExtra("icuBItem");

        initalWiget();

        /** 给patientId赋值*/
        tv_parent.setText(patients.get(whichPatient).getName());
        if(patients.get(whichPatient).getBedLabel()!=null){
            tv_bed_label.setText(patients.get(whichPatient).getBedLabel() + "床");
        }else{
            tv_bed_label.setText("未分配");
        }
        tv_head.setBackgroundResource(R.drawable.b_title_bar_switch);
        adapter = new ICUBTitleBarAdapter(ICUBActivity.this, mytitle, 0);
        navbar_lv.setAdapter(adapter);

        /** 护理计划 并插入bundle*/
        Bundle dataH = new Bundle();
        dataH.putSerializable("plan", (Serializable) totalBean.getH());
        plan.setArguments(dataH);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container, plan) .commit();

        navbar_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (orgNum != position) {
                    adapter.setNum(position);
                    adapter.notifyDataSetChanged();
                    switchFragment(position);
                    orgNum = position;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ICUResourceSave.getInstance(ICUBActivity.this).clearAll();
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_icub);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 初始化其他组建
     */
    private void initalWiget() {

        /** 标题*/
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setOnClickListener(this);
        ll_icu_patient_layout.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        navbar_lv = (HorizontalListView) findViewById(R.id.navbar_lv);
        mytitle.add(0, "护理计划");
        mytitle.add(1, "护理措施");
        mytitle.add(2, "护理评价");
        mytitle.add(3, "评估内容");
        mytitle.add(4, "骨伤科情况");
        mytitle.add(5, "脊柱外科");
        mytitle.add(6, "交班小结");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head:
                ICUDataMethod.changeStorageStatus(ICUBActivity.this, GlobalConstant.SAVE_CONDITION);
                Intent intent = new Intent();
                intent.putExtra("returnWhichPatient", whichPatient);
                setResult(RESULT_CODE, intent);
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();

                finish();
                overridePendingTransition(R.anim.in_or_out_static, R.anim.in_or_out_static);
                break;
            case R.id.ll_icu_patient_layout:
                nameDialog = new MySwitchNameDialog(ICUBActivity.this, R.style.switchPatientsName, new
                        MySwitchNameDialog.mySwitchNameListener() {
                            @Override
                            public void myOnItemClick(View view, int position, long id, GridViewICUAdapter
                                    adapter, GridView gv_patient) {
                                tv_parent.setText(patients.get(position).getName());
                                tv_bed_label.setText(patients.get(position).getBedLabel() + "床");
                                whichPatient = position;

                                String orgTime = (String) ICUResourceSave.getInstance(ICUBActivity.this).get
                                        ("recordingTime");

                                ICUResourceSave.getInstance(ICUBActivity.this).clearAll();
                                ICUCommonMethod.clearEditText();
                                ICUCommonMethod.clearButton(ICUAThirdFragment.getBtn_zhiliao(),
                                        ICUAThirdFragment.getBtn_yaowu(),
                                        ICUAThirdFragment.getBtn_xueye(), ICUAThirdFragment.getBtn_weichang(),
                                        ICUAThirdFragment.getBtn_bengruyaowu(), ICUAThirdFragment
                                                .getBtn_chuliang());
                                ICUDataMethod.reStartUpdateA();
                                ICUDataMethod.reStartSaveA();
                                ICUDataMethod.reStartSearchA();
                                ICUDataMethod.reStartSaveB();
                                ICUDataMethod.reStartUpdateB();
                                ICUDataMethod.reStartSearchB();

                                /** 把patId放入sp中*/
                                Map<String, Object> sp = new HashMap<String, Object>();
                                sp.put("patId", patients.get(position).getPatId());
                                sp.put("recordingTime", orgTime);
                                ICUResourceSave.getInstance(ICUBActivity.this).save(sp);

                                ICUDataMethod.changeStorageStatus(ICUBActivity.this, GlobalConstant
                                        .SAVE_CONDITION);

                                adapter.setWhichOne(position);
                                adapter.notifyDataSetChanged();
                                gv_patient.setSelection(position);
                                nameDialog.dismiss();

                            }

                        }, patients, whichPatient);
                nameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nameDialog.show();
                nameDialog.getWindow().setLayout(720, 500);
                break;
            case R.id.btn_back:
                Map<String, Object> recordingTime = new HashMap<String, Object>();
                recordingTime.put("recordingTime", "");
                ICUResourceSave.getInstance(ICUBActivity.this).save(recordingTime);
                Intent intent2 = new Intent();
                intent2.putExtra("returnWhichPatient", whichPatient);
                setResult(RESULT_CODE, intent2);
                finish();
                overridePendingTransition(R.anim.in_or_out_static, R.anim.in_or_out_static);
                break;

            default:
                break;
        }
    }

    private void switchFragment(int num) {
        switch (num) {
            case 0:
                /** 护理计划 */
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataH = new Bundle();
                dataH.putSerializable("plan", (Serializable) totalBean.getH());
                plan.setArguments(dataH);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container, plan)
                        .commit();
                break;
            case 1:
                /** 护理措施*/
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataI = new Bundle();
                dataI.putSerializable("measure", (Serializable) totalBean.getI());
                measure.setArguments(dataI);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container, measure)
                        .commit();
                break;
            case 2:
                /** 护理评价*/
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataJ = new Bundle();
                dataJ.putSerializable("evaluation", (Serializable) totalBean.getJ());
                evaluation.setArguments(dataJ);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container, evaluation)
                        .commit();
                break;
            case 3:
                /** 评估内容 */
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataD = new Bundle();
                dataD.putSerializable("assessMessage", (Serializable) totalBean.getD());
                assessMessage.setArguments(dataD);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container,
                        assessMessage).commit();
                break;
            case 4:
                /** 骨伤科情况*/
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataG = new Bundle();
                dataG.putSerializable("orthopedics", (Serializable) totalBean.getG());
                orthopedics.setArguments(dataG);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container,
                        orthopedics)
                        .commit();
                break;
            case 5:
                /** 脊柱外科*/
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataF = new Bundle();
                dataF.putSerializable("spine", (Serializable) totalBean.getF());
                spine.setArguments(dataF);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container, spine)
                        .commit();
                break;
            case 6:
                /** 交班小结*/
                ICUDataMethod.reStartSaveB();
                ICUDataMethod.reStartUpdateB();
                ICUDataMethod.reStartSearchB();
                Bundle dataZ = new Bundle();
                dataZ.putSerializable("turnOverDuty", (Serializable) totalBean.getZ());
                turnOverDuty.setArguments(dataZ);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_icu_container,
                        turnOverDuty)
                        .commit();
                break;

            default:
                break;
        }
    }
}
