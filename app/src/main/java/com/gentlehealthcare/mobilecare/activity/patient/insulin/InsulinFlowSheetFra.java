package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.patient.blood.BloodExeptionFra;
import com.gentlehealthcare.mobilecare.activity.patient.blood.BloodFra;
import com.gentlehealthcare.mobilecare.activity.patient.common.IdentityScanFra;
import com.gentlehealthcare.mobilecare.activity.patient.common.MedicineNotExecutionFra;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.NotExecutionFra;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.constant.StateConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.AuthenticationBean;
import com.gentlehealthcare.mobilecare.net.bean.BloodTestValCheckBean;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;
import com.gentlehealthcare.mobilecare.net.bean.CompleteExecuteBean;
import com.gentlehealthcare.mobilecare.net.bean.CompleteGlucoseValueCheckBean;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteMedicineInfo;
import com.gentlehealthcare.mobilecare.net.bean.LoadIntravsSnapshotBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatrolBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.AuthenticationRequest;
import com.gentlehealthcare.mobilecare.net.impl.CheckBloodTestValRequest;
import com.gentlehealthcare.mobilecare.net.impl.CheckMedicineRequest;
import com.gentlehealthcare.mobilecare.net.impl.CompleteExecuteRequest;
import com.gentlehealthcare.mobilecare.net.impl.CompleteGlucoseValueCheckReq;
import com.gentlehealthcare.mobilecare.net.impl.ExecuteMedicineRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadIntravsSnapshotRequest;
import com.gentlehealthcare.mobilecare.net.impl.PatrolRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DesUtil;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.AutoCallBackTextView;
import com.gentlehealthcare.mobilecare.view.CustomEditTextDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;

/**
 * 类说名：胰岛素身份扫描页面
 * Created by ouyang on 2015/5/27.
 */
@SuppressLint("ValidFragment")
public class InsulinFlowSheetFra extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    private SyncPatientBean patient;
    private AutoCallBackTextView tvInfo;
    private Button btn_bottom_left;
    private Button btn_bottom_right, btn_bottom_center;
    private Map<String, String> saveMap = null;
    private FlowConstant.PatientFlow pFlow;
    private ProgressDialog progressDialog;
    private Fragment currentFra = null;
    private String site;
    private RadioGroup rg_tab;
    private RadioButton rg_tab_NotPerformed;//待执行
    private RadioButton rg_tab_performing;//执行中
    private OrderItemBean orderItemBean;
    private AlertDialogTwoBtn alertDialog;
    private String siteCode;
    private String siteNo;
    private AlertDialogTwoBtn dialog = null;
    private CustomEditTextDialog customEditTextDialog = null;
    private FlowConstant.PatientFlow flow;
    private static FlowConstant.PatientFlow saveflow = null;
    private String eventId;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_flow);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private InsulinFlowSheetFra() {
    }

    public InsulinFlowSheetFra(SyncPatientBean patient, FlowConstant.PatientFlow flow, OrderItemBean
            orderItemBean) {
        super();
        this.patient = patient;
        this.flow = flow;
        this.orderItemBean = orderItemBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveMap = new HashMap<String, String>();
        pFlow = FlowConstant.PatientFlow.IDENTITY_CONFIRM;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flow, null);
        btn_bottom_left = (Button) view.findViewById(R.id.btn_left);
        btn_bottom_right = (Button) view.findViewById(R.id.btn_right);
        btn_bottom_center = (Button) view.findViewById(R.id.btn_center);
        tvInfo = (AutoCallBackTextView) view.findViewById(R.id.tv_info);
        tvInfo.setContentStr(getPatientInfo());
        tvInfo.setDetailStr(getPatientInfoDetail());
        tvInfo.collection();
        btn_bottom_left.setVisibility(View.GONE);
        btn_bottom_right.setText("扫描");
        btn_bottom_left.setOnClickListener(this);
        btn_bottom_right.setOnClickListener(this);
        btn_bottom_right.setOnLongClickListener(this);
        rg_tab_NotPerformed = (RadioButton) view.findViewById(R.id.rg_tab_NotPerformed);
        rg_tab_performing = (RadioButton) view.findViewById(R.id.rg_tab_performing);

        rg_tab = (RadioGroup) view.findViewById(R.id.rg_tab);

        String str = "";
        str = saveMap.get("leftBtnVisible");
        if (str != null && !"".equals(str)) {
            int visible = Integer.parseInt(str);
            if (visible == View.VISIBLE)
                btn_bottom_left.setVisibility(View.VISIBLE);
            else
                btn_bottom_left.setVisibility(View.INVISIBLE);
        }
        str = saveMap.get("leftBtnStr");
        if (str != null && !"".equals(str))
            btn_bottom_left.setText(str);

        str = saveMap.get("rightBtnVisible");
        if (str != null && !"".equals(str)) {
            int visible = Integer.parseInt(str);
            if (visible == View.VISIBLE)
                btn_bottom_right.setVisibility(View.VISIBLE);
            else
                btn_bottom_right.setVisibility(View.INVISIBLE);
        }
        str = saveMap.get("rightBtnStr");
        if (str != null && !"".equals(str))
            btn_bottom_right.setText(str);
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_left.setText("取消");
        btn_bottom_right.setText("扫描");
        btn_bottom_left.setOnClickListener(this);
        btn_bottom_right.setOnClickListener(this);
        btn_bottom_center.setOnClickListener(this);
        btn_bottom_center.setVisibility(View.GONE);
        rg_tab.check(R.id.rg_tab_NotPerformed);
        rg_tab_NotPerformed.setTextColor(Color.BLACK);
        rg_tab_performing.setTextColor(Color.GRAY);
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                if (fragment instanceof DoctorOrderListFra) {
                    DoctorOrderListFra mlistFra = (DoctorOrderListFra) fragment;
                    switch (arg1) {
                        case R.id.rg_tab_NotPerformed:
                            rg_tab_NotPerformed.setTextColor(Color.BLACK);
                            rg_tab_performing.setTextColor(Color.GRAY);
                            mlistFra.SwitchTab(MedicineConstant.STATE_WAITING);
                            btn_bottom_right.setVisibility(View.VISIBLE);
                            btn_bottom_right.setText("执行");
                            btn_bottom_left.setText("不执行");
                            btn_bottom_left.setVisibility(View.INVISIBLE);
                            pFlow = FlowConstant.PatientFlow.MEDICINE_LIST;
                            orderItemBean = mlistFra.getOrderItemBean();
                            break;
                        case R.id.rg_tab_performing:
                            rg_tab_NotPerformed.setTextColor(Color.GRAY);
                            rg_tab_performing.setTextColor(Color.BLACK);
                            mlistFra.SwitchTab(MedicineConstant.STATE_EXECUTING);
                            btn_bottom_left.setText("结束");
                            btn_bottom_left.setVisibility(View.INVISIBLE);
                            pFlow = FlowConstant.PatientFlow.MEDICINE_END;

                            btn_bottom_right.setText("巡视");
                            orderItemBean = mlistFra.getOrderItemBean();
                            break;

                        default:
                            break;
                    }
                }

            }
        });

        if (flow == null) {
            IdentityScanFra scanFragment = new IdentityScanFra(null);
            getChildFragmentManager().beginTransaction().add(R.id.fl_flow, scanFragment).commit();
            pFlow = FlowConstant.PatientFlow.IDENTITY_CONFIRM;
        } else {
            if (flow == FlowConstant.PatientFlow.MEDICINE_LIST) {

                DoctorOrderListFra listFra = new DoctorOrderListFra(patient, MedicineConstant.STATE_WAITING);
                listFra.setCheckButtonId(orderItemBean.getTemplateId() == null || orderItemBean.getTemplateId()
                        .equals("AA-3") ? 0 : 1);
                listFra.setCurrentPlanNo(orderItemBean.getPlanOrderNo());
                btn_bottom_center.setText("刷新");
                rg_tab.check(R.id.rg_tab_NotPerformed);
                rg_tab_NotPerformed.setTextColor(Color.BLACK);
                rg_tab_performing.setTextColor(Color.GRAY);
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, listFra).commit();
                pFlow = FlowConstant.PatientFlow.MEDICINE_LIST;
                btn_bottom_right.setText("扫描");
                btn_bottom_right.setVisibility(View.VISIBLE);
                btn_bottom_left.setVisibility(View.VISIBLE);
                btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
                btn_bottom_left.setText("不执行");

                pFlow = flow;
            } else if (flow == FlowConstant.PatientFlow.PATROL_COUNT_DOWN) {
                DoMedicineEnd();
                pFlow = flow;
            } else if (flow == FlowConstant.PatientFlow.BLOOD_TEST) {
                //血糖检测
                DoBloodTest(StateConstant.STATE_EXECUTING);
                pFlow = flow;
            }
        }
        DoLoadInsulinSnapshotRequest();
        return view;
    }

    private void updateFlow() {
        if (getActivity().getClass().getName().equals(InsulinFlowAct.class.getName())) {
            ((InsulinFlowAct) getActivity()).updateFlow(pFlow);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveMap.put("leftBtnVisible", btn_bottom_left.getVisibility() + "");
        saveMap.put("leftBtnStr", btn_bottom_left.getText().toString());
        saveMap.put("rightBtnVisible", btn_bottom_right.getVisibility() + "");
        saveMap.put("rightBtnStr", btn_bottom_right.getText().toString());
        saveMap.put("leftBtnColor", btn_bottom_left.getTag() + "");
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getPatientInfo
     * @Description: 获取病人信息
     */
    public String getPatientInfo() {
        if(patient.getBedLabel()!=null){
            return "床号: " + patient.getBedLabel() + "\n" + "姓名: " + patient.getName();
        }else{
            return "床号: " + "未分配" + "\n" + "姓名: " + patient.getName();
        }
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getPatientInfoDetail
     * @Description: 获取病人详细信息
     */
    public String getPatientInfoDetail() {

        return getPatientInfo();
    }

    public void doGestUp() {
        
        tvInfo.collection();
    }

    public void doGestDown() {
        
        tvInfo.expansion();
    }

    /**
     * 当选择最近用餐时间对话框提示,Back键返回到上一页
     *
     * @return
     */
    public boolean isShowing() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof BloodFra)
            return ((BloodFra) currentFra).isShowing();
        else
            return false;
    }

    /**
     * 快速键
     */
    public void doOnClick() {
        if (progressDialog != null && progressDialog.isShowing())
            return;
        switch (pFlow) {
            case IDENTITY_CONFIRM:
                //病人身份扫描
                ShowToast("请使用红外扫描核对患者");
                break;
            case EXPLAIN:
                //说明
                DoDoctorOrderList();
                break;
            case CHOOSESITE:
                //药品核对
                DoMedicineCheck();
                break;
            case MEDICINE_CHECK:
                //
                DoMedicineCheckReq();
                break;
            case MEDICINE_LIST:
                DoBloodTestAlert();
                break;
            case MEDICINE_INTRODUCTION:
                //药品注射
                DoMedicineCheckReq();
                break;
            case MEDICINE_INJECTION:
                DoInjectionMedicineReq();
                break;
            case INJECTIONSITE:
                InsulinPatrolFra insulinPlaceFra = new InsulinPatrolFra();
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinPlaceFra).commit();
                break;
            case MEDICINE_COUT_DOWN:
                DoMedicineCoutDown();
                break;
            case MEDICINE_END:
                currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                if (currentFra instanceof DoctorOrderListFra) {
                    orderItemBean = ((DoctorOrderListFra) currentFra).getOrderItemBean();
                    if (orderItemBean == null) {
                        Toast.makeText(getActivity(), "暂无药品可供执行该操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!orderItemBean.getTemplateId().equals(TemplateConstant.BLOOD_TEST.GetTemplateId()))
                    DoMedicineEnd();
                else
                    DoBloodTest(StateConstant.STATE_EXECUTING);
                break;
            case PATROL_COUNT_DOWN:
                //执行中中倒计时
                if (btn_bottom_right.getVisibility() != View.VISIBLE) {
                    Toast.makeText(getActivity(), "请先扫描药物!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DoRecordResult();
                break;
            case PATROL_RECORD:
                DoExecutionEnd();
                break;
            case MEDICINE_CANCEL:
                DoDoctorOrderList();
                break;
            case BLOOD_TEST:
                //血糖检测
                DoBloodTestValue();
                break;
            case BLOOD_EXECPITON:
                DoBloodExecption();
                break;
        }
    }

    /**
     * 血糖检测异常
     */
    private void DoBloodExecption() {
        progressDialog.setMessage("正在提交数据,请稍后..");
        progressDialog.show();
        final CompleteGlucoseValueCheckBean completeGlucoseValueCheckBean = new CompleteGlucoseValueCheckBean();
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        int result = ((BloodExeptionFra) currentFra).check();
        String txt = "";
        if (result == 0)
            txt = "停止执行";
        else if (result == 1)
            txt = "间隔2小时再执行";
        else if (result == 2)
            txt = "照常执行";
        else
            txt = "待医师回复";
        completeGlucoseValueCheckBean.setPerformDesc(txt);
        completeGlucoseValueCheckBean.setPatId(patient.getPatId());
        completeGlucoseValueCheckBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        completeGlucoseValueCheckBean.setGlucoseVal(((BloodExeptionFra) currentFra).getValue());
        RequestManager.connection(new CompleteGlucoseValueCheckReq(getActivity(), new
                IRespose<CompleteGlucoseValueCheckBean>() {
                    @Override
                    public void doResult(CompleteGlucoseValueCheckBean completeGlucoseValueCheckBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        progressDialog.dismiss();
                        CompleteGlucoseValueCheckBean completeGlucoseValueCheckBean = new Gson().fromJson(result,
                                CompleteGlucoseValueCheckBean.class);
                        if (completeGlucoseValueCheckBean.isResult()) {
                            currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                            if (currentFra instanceof BloodExeptionFra) {
                                int tag = ((BloodExeptionFra) currentFra).check();
                                if (tag == 2) {
                                    //照常执行
                                    DoDoctorOrderList();
                                } else {
                                    getActivity().finish();
                                }
                            }
                        } else {
                            ShowToast(completeGlucoseValueCheckBean.getMsg());
                        }
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState)
                            Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                        else {
                            toErrorAct();
                        }
                    }
                }, 0, true, completeGlucoseValueCheckBean));

    }
    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }
    /**
     * 返回上一步流程
     */
    public void backSheet() {
        if (pFlow == FlowConstant.PatientFlow.IDENTITY_CONFIRM) {
            alertDialog = new AlertDialogTwoBtn(getActivity());
            alertDialog.setTitle("提示");
            alertDialog.setMessage("是否回到主界面");
            alertDialog.setLeftButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setRightButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), HomeAct.class));
                    alertDialog.dismiss();
                    getActivity().finish();

                }
            });
        } else if (pFlow == FlowConstant.PatientFlow.EXPLAIN) {
            //回到确定患者身份
            IdentityScanFra scanFragment = new IdentityScanFra(null);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, scanFragment).commit();
            pFlow = FlowConstant.PatientFlow.IDENTITY_CONFIRM;
            btn_bottom_right.setText("扫描");
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_center.setVisibility(View.GONE);
            ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.IDENTITY_CONFIRM);
        } else if (pFlow == FlowConstant.PatientFlow.MEDICINE_LIST || pFlow == FlowConstant.PatientFlow
                .MEDICINE_END) {
            String content = "";
//            IdentityScanSuccessFra confirmFragment = new IdentityScanSuccessFra(null, 1);
//            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, confirmFragment).commit();
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_right.setVisibility(View.VISIBLE);
            btn_bottom_center.setVisibility(View.GONE);
//            btn_bottom_right.setText("确认");
            pFlow = FlowConstant.PatientFlow.EXPLAIN;
            updateFlow();
            ((InsulinFlowAct) getActivity()).updateFlow(pFlow);
        } else if (pFlow == FlowConstant.PatientFlow.MEDICINE_PATORL || pFlow == FlowConstant.PatientFlow
                .PATROL_COUNT_DOWN || pFlow == FlowConstant.PatientFlow.NOT_EXECUTION || pFlow == FlowConstant
                .PatientFlow.MEDICINE_COUT_DOWN || pFlow == FlowConstant.PatientFlow.SEALING || pFlow ==
                FlowConstant.PatientFlow.BLOOD_TEST) {
            //回到药品列表界面
            DoDoctorOrderList();
        } else if (pFlow == FlowConstant.PatientFlow.CHOOSESITE) {
            if (btn_bottom_right.getVisibility() == View.VISIBLE) {
                InsulinPlaceFra insulinPlaceFra = new InsulinPlaceFra(patient);
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinPlaceFra).commit();
                pFlow = FlowConstant.PatientFlow.CHOOSESITE;
                btn_bottom_center.setVisibility(View.GONE);
                btn_bottom_left.setVisibility(View.INVISIBLE);
                btn_bottom_right.setVisibility(View.INVISIBLE);
                btn_bottom_right.setText("确定");
                ((InsulinFlowAct) getActivity()).updateFlow(pFlow);
            } else {
                //回到药品列表界面
                DoDoctorOrderList();
            }
        } else if (pFlow == FlowConstant.PatientFlow.MEDICINE_INTRODUCTION) {
            //进入注射部位确定界面
            DoInjectionSite(siteCode, siteNo);
        } else if (pFlow == FlowConstant.PatientFlow.MEDICINE_INJECTION) {
            //回到给要界面
            InsulinCheckMedicineFra insulinCheckMedicineFra = new InsulinCheckMedicineFra(getSite(), patient,
                    orderItemBean);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinCheckMedicineFra).commit();
            pFlow = FlowConstant.PatientFlow.MEDICINE_INTRODUCTION;
            ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.MEDICINE_INJECTION);
            btn_bottom_left.setText("取消");
            btn_bottom_left.setVisibility(View.VISIBLE);
            btn_bottom_right.setVisibility(View.VISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_right.setText("给药");
            btn_bottom_center.setVisibility(View.GONE);
        } else if (pFlow == FlowConstant.PatientFlow.PATROL_RECORD) {
            DoMedicineEnd();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_center:
                switch (pFlow) {
                    case MEDICINE_LIST:
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (currentFra instanceof DoctorOrderListFra) {
                            ((DoctorOrderListFra) currentFra).refreshMedicineList();
                        }
                        break;
                }
                break;
            case R.id.btn_left:
                switch (pFlow) {
                    case MEDICINE_LIST:
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (currentFra instanceof DoctorOrderListFra) {
                            orderItemBean = ((DoctorOrderListFra) currentFra).getOrderItemBean();
                            if (orderItemBean == null) {
                                Toast.makeText(getActivity(), "暂无药品可供执行该操作", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        btn_bottom_center.setVisibility(View.GONE);
                        //不执行
                        NotExecutionFra notExecution = new NotExecutionFra(null);

                        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, notExecution).commit();
                        btn_bottom_left.setVisibility(View.INVISIBLE);
                        btn_bottom_center.setVisibility(View.GONE);
                        btn_bottom_right.setVisibility(View.VISIBLE);
                        btn_bottom_right.setText("确认");
                        pFlow = FlowConstant.PatientFlow.NOT_EXECUTION;
                        break;

                    case CHOOSESITE:
                    case MEDICINE_INJECTION:
                    case MEDICINE_INTRODUCTION:
                    case MEDICINE_PATORL:
                    case MEDICINE_CHECK:
                        //取消  不给药
                        MedicineNotExecutionFra infusionNotExecutFragment = new MedicineNotExecutionFra(null);

                        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow,
                                infusionNotExecutFragment).commit();
                        btn_bottom_left.setVisibility(View.INVISIBLE);
                        btn_bottom_right.setVisibility(View.VISIBLE);
                        btn_bottom_right.setText("回清单");
                        pFlow = FlowConstant.PatientFlow.MEDICINE_CANCEL;
                        break;
                    case PATROL_COUNT_DOWN:
                        //执行中中倒计时
                        DoPatrolFraReq();
                        break;
                    case MEDICINE_END:
                        DoRecordResult();
                        break;
                }
                break;
            case R.id.btn_right:
                doOnClick();
                break;
        }
    }

    /**
     * 执行结束
     */
    private void DoExecutionEnd() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof InsulinPatrolFra) {
            Toast.makeText(getActivity(), ((InsulinPatrolFra) currentFra).getTxtStr(), Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("正在提交数据...");
            progressDialog.show();
            CompleteExecuteBean completeExecuteBean = new CompleteExecuteBean();
            completeExecuteBean.setPatId(patient.getPatId());
            completeExecuteBean.setPerformDesc(((InsulinPatrolFra) currentFra).getTxtStr());
            completeExecuteBean.setType(1);
            completeExecuteBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
            RequestManager.connection(new CompleteExecuteRequest(getActivity(), new
                    IRespose<CompleteExecuteBean>() {
                        @Override
                        public void doResult(CompleteExecuteBean completeExecuteBean, int id) {
                        }

                        @Override
                        public void doResult(String result) throws KeyObsoleteException {
                            progressDialog.dismiss();
                            CompleteExecuteBean completeExecuteBean = new Gson().fromJson(result,
                                    CompleteExecuteBean
                                    .class);
                            if (completeExecuteBean.getResult()) {
                                getActivity().finish();
                            } else {
                                ShowToast(completeExecuteBean.getMsg());
                            }
                        }

                        @Override
                        public void doException(Exception e, boolean networkState) {
                            progressDialog.dismiss();
                            if (networkState)
                                Toast.makeText(getActivity(), "执行异常", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), getString(R.string.netstate_content), Toast
                                        .LENGTH_SHORT)
                                        .show();
                            progressDialog.dismiss();
                        }
                    }, 0, true, completeExecuteBean));


        }
    }

    /**
     * 观察 巡视 请求
     */
    private void DoPatrolFraReq() {
        customEditTextDialog = new CustomEditTextDialog(getActivity(), R.style.myDialogTheme);
        customEditTextDialog.setContent(new String[]{"记录内容:"});
        customEditTextDialog.setTitle("提示");
        customEditTextDialog.setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customEditTextDialog.dismiss();
            }
        });
        customEditTextDialog.setRightButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customEditTextDialog.dismiss();
                progressDialog.setMessage("正在提交数据，请稍后...");
                progressDialog.show();
                PatrolBean patrolBean = new PatrolBean();
                patrolBean.setPerformDesc(customEditTextDialog.getText()[0]);
//                patrolBean.setPlanId(getMedicineAllId());
                patrolBean.setType(1);
                RequestManager.connection(new PatrolRequest(getActivity(), new IRespose<PatrolBean>() {
                    @Override
                    public void doResult(PatrolBean patrolBean, int id) {
                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        progressDialog.dismiss();
                        if (UserInfo.getKey() != null && !"".equals(UserInfo.getKey())) {
                            try {
                                result = DesUtil.decrypt(result, UserInfo.getKey());

                                Gson gson = new Gson();
                                Type type = new TypeToken<List<ExecuteMedicineInfo>>() {
                                }.getType();
                                List<ExecuteMedicineInfo> list = gson.fromJson(result, type);
                                if (list != null && list.size() > 0) {
                                    ExecuteMedicineInfo executeMedicineInfo = list.get(0);
                                    if (executeMedicineInfo.getResult().equals("success")) {
                                        Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //执行注射失败
                                        Toast.makeText(getActivity(), "执行注射操作失败（部分药品正在注射或者已注射)", Toast
                                                .LENGTH_SHORT).show();
                                    }
                                }
                            } catch (BadPaddingException e) {
                                throw new KeyObsoleteException("密钥过期");
                            } catch (Exception e) {
                                if (LinstenNetState.isLinkState(getActivity())) {
                                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                            .LENGTH_SHORT).show();
                                }else{
                                    toErrorAct();
                                }
                            }
                        }
                    }


                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState)
                            Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                        else {
                            toErrorAct();
                        }
                    }
                }, 0, true, patrolBean));
            }

        });
        customEditTextDialog.show();
    }

    /**
     * 执行中中倒计时
     */
    private void DoRecordResult() {
        InsulinPatrolFra insulinPatrolFra = new InsulinPatrolFra();
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinPatrolFra).commit();
        pFlow = FlowConstant.PatientFlow.PATROL_RECORD;
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_center.setVisibility(View.GONE);
        btn_bottom_right.setText("确认");
    }

    /**
     * 待执行 药品注射-跳转到新巡视倒计时  如果还有待执行工作,点击确定跳转药品列表,否则退出
     */
    private void DoMedicineCoutDown() {
        int waitSum = 0;
        if (waitSum > 0) {
            DoDoctorOrderList();
        } else {
            getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);

            getActivity().finish();
        }

    }

    /**
     * 医嘱列表
     */
    private void DoDoctorOrderList() {
        DoctorOrderListFra doctorOrderListFra = new DoctorOrderListFra(patient, MedicineConstant.STATE_WAITING);
        btn_bottom_center.setText("刷新");
        btn_bottom_right.setText("执行");
        btn_bottom_left.setText("不执行");
        rg_tab.check(R.id.rg_tab_NotPerformed);
        rg_tab_NotPerformed.setTextColor(Color.BLACK);
        rg_tab_performing.setTextColor(Color.GRAY);
        btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
        pFlow = FlowConstant.PatientFlow.MEDICINE_LIST;
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, doctorOrderListFra).commit();
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_right.setVisibility(View.VISIBLE);
        ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.CHOOSESITE);
    }


    /**
     * 执行 巡视记录
     */
    public void DoMedicineEnd() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof DoctorOrderListFra) {
            orderItemBean = ((DoctorOrderListFra) currentFra).getOrderItemBean();
            if (orderItemBean == null) {
                ShowToast("请选择一项药品信息");
                return;
            }
            InsulinPatrolCountDownFra countDownFragment = new InsulinPatrolCountDownFra(patient, orderItemBean,
                    true);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, countDownFragment).commit();
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_right.setVisibility(View.INVISIBLE);
            btn_bottom_right.setText("确认");
            btn_bottom_left.setText("观察");
            btn_bottom_center.setVisibility(View.INVISIBLE);
            ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.PATROL_RECORD);
            ((InsulinFlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
            pFlow = FlowConstant.PatientFlow.PATROL_COUNT_DOWN;
        }

    }

    /**
     * 药品信息核对
     */
    private void DoMedicineCheck() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof InjectionSiteFra) {

            setSite(((InjectionSiteFra) currentFra).getSite());
            InsulinCheckMedicineFra insulinCheckMedicineFra = new InsulinCheckMedicineFra(getSite(), patient,
                    orderItemBean);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinCheckMedicineFra).commit();
            pFlow = FlowConstant.PatientFlow.MEDICINE_CHECK;
            ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.MEDICINE_INJECTION);
            btn_bottom_left.setText("取消");
            btn_bottom_left.setVisibility(View.VISIBLE);
            btn_bottom_right.setVisibility(View.VISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_right.setText("核对");
            btn_bottom_center.setVisibility(View.GONE);

        } else if (currentFra instanceof InsulinPlaceFra) {
            ((InsulinPlaceFra) currentFra).doOnClick();
        }
    }

    /**
     * 执行核对药品接口
     */
    private void DoMedicineCheckReq() {
        progressDialog.setMessage("正在提交数据，请稍后...");
        progressDialog.show();
        CheckMedicineBean checkMedicineBean = new CheckMedicineBean();
        checkMedicineBean.setPatId(patient.getPatId());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setType(1);
        RequestManager.connection(new CheckMedicineRequest(getActivity(), new IRespose<CheckMedicineBean>() {
            @Override
            public void doResult(CheckMedicineBean checkMedicineBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                CheckMedicineBean checkMedicineBean = new Gson().fromJson(result, CheckMedicineBean.class);
                if (checkMedicineBean.getResult()) {
                    ((InsulinFlowAct) getActivity()).updateFlow(pFlow);
                    btn_bottom_right.setText("注射");
                    pFlow = FlowConstant.PatientFlow.MEDICINE_INJECTION;
                } else {
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }

            }
        }, 0, true, checkMedicineBean));

    }

    /**
     * 执行 注射 请求
     */
    public void DoInjectionMedicineReq() {
        //组合药品处理
        progressDialog.setMessage("正在进行注射操作,请销等...");
        progressDialog.show();
        CheckMedicineBean checkMedicineBean = new CheckMedicineBean();
        checkMedicineBean.setType(1);
        checkMedicineBean.setSiteCode(siteCode);
        checkMedicineBean.setSiteNo(siteNo);
        checkMedicineBean.setPatId(patient.getPatId());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setEventId(eventId);
        RequestManager.connection(new ExecuteMedicineRequest(getActivity(), new IRespose<CheckMedicineBean>() {
            @Override
            public void doResult(CheckMedicineBean checkMedicineBean, int id) {
            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                CheckMedicineBean checkMedicineBean = new Gson().fromJson(result, CheckMedicineBean.class);
                if (checkMedicineBean.getResult()) {
                    //提交记录巡视间隔时间
                    InsulinPatrolCountDownFra copyPatrolCountDownFra = new InsulinPatrolCountDownFra(patient,
                            orderItemBean, false);
                    getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, copyPatrolCountDownFra)
                            .commit();
                    btn_bottom_left.setVisibility(View.INVISIBLE);
                    btn_bottom_right.setText("确认");
                    btn_bottom_left.setText("观察");
                    ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.PATROL_RECORD);
                    pFlow = FlowConstant.PatientFlow.MEDICINE_COUT_DOWN;
                    DoLoadInsulinSnapshotRequest();
                } else {
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, checkMedicineBean));
    }


    /**
     * 弹框提示是否血糖检测
     */
    public void DoBloodTestAlert() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof DoctorOrderListFra) {
            orderItemBean = ((DoctorOrderListFra) currentFra).getOrderItemBean();
            if (orderItemBean == null) {
                Toast.makeText(getActivity(), "暂无医嘱可供执行该操作.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isShowBloodTestDialog = false;
            if (orderItemBean == null || orderItemBean.getTemplateId() == null || "".equals(orderItemBean
                    .getTemplateId())) {
                isShowBloodTestDialog = true;
            } else if (orderItemBean.getTemplateId().equals(TemplateConstant.INSULIN.GetTemplateId())) {
                isShowBloodTestDialog = true;
            } else if (orderItemBean.getTemplateId().equals(TemplateConstant.BLOOD_TEST.GetTemplateId())) {
                isShowBloodTestDialog = false;
            }
            if (isShowBloodTestDialog) {
                currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                if (currentFra instanceof DoctorOrderListFra) {
//                        dialog = new AlertDialogTwoBtn(getActivity());
//                        dialog.setTitle("提示");
//                        dialog.setMessage("是否需要进行血糖检测？");
//                        dialog.setLeftButton("否", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
                    DoExplain();
//                                dialog = null;
//
//                            }
//                        });
//                        dialog.setRightButton("是", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                                dialog = null;
//                                    DoBloodTest(StateConstant.STATE_WAITING);
//
//                            }
//                        });
                }
            } else {
                DoBloodTest(StateConstant.STATE_WAITING);
            }

        }
    }

    private void DoBloodTest(final StateConstant stateConstant) {
        BloodFra bloodFra = new BloodFra(orderItemBean, patient, stateConstant);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, bloodFra).commit();
        btn_bottom_center.setVisibility(View.GONE);
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_right.setVisibility(View.VISIBLE);
        btn_bottom_right.setText("确定");
        pFlow = FlowConstant.PatientFlow.BLOOD_TEST;
    }


    /**
     * 血糖检测
     */
    public void DoBloodTestValue() {
        progressDialog.setMessage("正在提交数据,请稍后..");
        progressDialog.dismiss();
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof BloodFra) {
            final String value = ((BloodFra) currentFra).GetBloodValue();
            if (value.equals("")) {
                Toast.makeText(getActivity(), "请输入血糖值", Toast.LENGTH_SHORT).show();
            } else {
                BloodTestValCheckBean bloodTestValCheckBean = new BloodTestValCheckBean();
                bloodTestValCheckBean.setGlucoseVal(value);
                bloodTestValCheckBean.setPatId(patient.getPatId());
                bloodTestValCheckBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
                RequestManager.connection(new CheckBloodTestValRequest(getActivity(), new
                        IRespose<BloodTestValCheckBean>() {
                            @Override
                            public void doResult(BloodTestValCheckBean bloodTestValCheckBean, int id) {

                            }

                            @Override
                            public void doResult(String result) throws KeyObsoleteException {
                                progressDialog.dismiss();
                                BloodTestValCheckBean bloodTestValCheckBean = new Gson().fromJson(result,
                                        BloodTestValCheckBean.class);
                                if (bloodTestValCheckBean.isResult()) {
                                    DoLoadInsulinSnapshotRequest();
                                    patient.setDoctorInCharge(bloodTestValCheckBean.getDoctorInCharge());
                                    if (bloodTestValCheckBean.isCheckResult()) {
                                        getActivity().finish();
                                    } else {
                                        //异常
                                        DoBloodTestExecption(value);
                                    }
                                } else {
                                    ShowToast(bloodTestValCheckBean.getMsg());
                                }
                            }

                            @Override
                            public void doException(Exception e, boolean networkState) {
                                progressDialog.dismiss();
                                if (networkState)
                                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                                else {
                                    toErrorAct();
                                }
                            }
                        }, 0, true, bloodTestValCheckBean));
            }

        }
    }

    /**
     * 血糖检测异常处理
     *
     * @param value
     */
    public void DoBloodTestExecption(String value) {
        BloodExeptionFra bloodExeptionFra = new BloodExeptionFra(patient, value);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, bloodExeptionFra).commit();
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_right.setVisibility(View.VISIBLE);
        btn_bottom_right.setText("确认");
        pFlow = FlowConstant.PatientFlow.BLOOD_EXECPITON;

    }


    /**
     * 注射位置选择
     */
    public void DoExplain() {
        InsulinPlaceFra insulinPlaceFra = new InsulinPlaceFra(patient);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, insulinPlaceFra).commit();
        pFlow = FlowConstant.PatientFlow.CHOOSESITE;
        btn_bottom_center.setVisibility(View.GONE);
        btn_bottom_left.setVisibility(View.INVISIBLE);
        btn_bottom_right.setVisibility(View.INVISIBLE);
        btn_bottom_right.setText("确定");
        ((InsulinFlowAct) getActivity()).updateFlow(pFlow);
    }

    public void DoInjectionSite(String siteCode, String siteNo) {
        this.siteCode = siteCode;
        this.siteNo = siteNo;
        InjectionSiteFra injectionSiteFra = new InjectionSiteFra(siteCode, siteNo, patient.getPatId(),
                orderItemBean);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, injectionSiteFra).commit();
        pFlow = FlowConstant.PatientFlow.CHOOSESITE;
        btn_bottom_right.setVisibility(View.VISIBLE);

    }


    /**
     * 患者身份核实处理
     */
    private void DoIdentityConfirm(boolean isCheckResult) {
        progressDialog.setMessage("正在核对身份,请稍后..");
        progressDialog.show();
        AuthenticationBean authenticationBean = new AuthenticationBean();
        authenticationBean.setPatId(patient.getPatId());
        authenticationBean.setEventAttr(isCheckResult ? "1" : "-1");
        authenticationBean.setPatCode(patient.getPatCode());
        authenticationBean.setTemplateId(TemplateConstant.MEDICINE.GetTemplateId());

        RequestManager.connection(new AuthenticationRequest(getActivity(), new IRespose<AuthenticationBean>() {
            @Override
            public void doResult(AuthenticationBean authenticationBean, int id) {
            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                progressDialog.dismiss();
                AuthenticationBean authenticationBean = new Gson().fromJson(result, AuthenticationBean.class);
                if (authenticationBean.isResult()) {
//                    IdentityScanSuccessFra confirmFragment = new IdentityScanSuccessFra(patient, 0);
//                    getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, confirmFragment).commit();
                    eventId=authenticationBean.getEventId();
                    btn_bottom_left.setVisibility(View.INVISIBLE);
                    btn_bottom_right.setVisibility(View.VISIBLE);
//                    btn_bottom_right.setText("确认");
//                    pFlow = FlowConstant.PatientFlow.EXPLAIN;
                    pFlow = FlowConstant.PatientFlow.MEDICINE_LIST;
                    DoDoctorOrderList();
//                    ((InsulinFlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.EXPLAIN);
                } else {
                    ShowToast(authenticationBean.getMsg());
                }
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, authenticationBean));

    }


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void showBottomBtn() {
        btn_bottom_right.setVisibility(View.VISIBLE);
        btn_bottom_left.setVisibility(View.INVISIBLE);
    }

    public void updateRightText(String rightStr) {
        btn_bottom_right.setText(rightStr);
    }


    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        if (pFlow == FlowConstant.PatientFlow.IDENTITY_CONFIRM) {
            //病人身份扫描
            DoIdentityConfirm(result.equals(patient.getPatCode()));
        } else if (pFlow == FlowConstant.PatientFlow.MEDICINE_LIST) {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
            if (fragment instanceof DoctorOrderListFra) {
                DoctorOrderListFra mlistFra = (DoctorOrderListFra) fragment;
                if (!mlistFra.isShow()) {
                    mlistFra.setCurrentPlanNo(result);
                    mlistFra.refreshMedicineList();
                    mlistFra.setPosition(result);
                }
            }
        }

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                switch (pFlow) {
                    case IDENTITY_CONFIRM:
                    case MEDICINE_INTRODUCTION:
                        //病人身份扫描  弹框 可手动输入Code
                        final EditText editText = new EditText(getActivity());
                        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                                .MATCH_PARENT, 100);
                        editText.setLayoutParams(lp);
                        editText.setText(patient.getPatCode());
                        editText.setHint("手动输入病人腕带编号");
                        new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false).setView
                                (editText).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (editText.getText().toString().equals("")) {
                                    ShowToast("请输入编号值");
                                } else {
                                    if (pFlow == FlowConstant.PatientFlow.IDENTITY_CONFIRM)
                                        DoIdentityConfirm(editText.getText().toString().equals(patient
                                                .getPatCode()));
//                                    else if (pFlow== FlowConstant.PatientFlow.MEDICINE_INTRODUCTION)
//                                        DoIntroduction(editText.getText().toString());
                                }
                            }
                        }).setPositiveButton("取消", null).create().show();
                        ShowToast("请使用红外扫描核对患者");
                        break;

                }
                break;
        }
        return false;
    }


    /**
     * 获取待执行和执行中数量
     */
    public void DoLoadInsulinSnapshotRequest() {
        LoadIntravsSnapshotBean loadIntravsSnapshotBean = new LoadIntravsSnapshotBean();
        loadIntravsSnapshotBean.setPatId(patient.getPatId());
        loadIntravsSnapshotBean.setType(1);
        RequestManager.connection(new LoadIntravsSnapshotRequest(getActivity(), new
                IRespose<LoadIntravsSnapshotBean>() {
                    @Override
                    public void doResult(LoadIntravsSnapshotBean loadIntravsSnapshotBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        LoadIntravsSnapshotBean loadIntravsSnapshotBean = new Gson().fromJson(result,
                                LoadIntravsSnapshotBean.class);
                        CCLog.i("胰岛素快照数据>>>" + result.toString());
                        updateTab(loadIntravsSnapshotBean.getPerformTask(), loadIntravsSnapshotBean.getExecutingTask());
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        updateTab(0, 0);

                    }
                }, 0, true, loadIntravsSnapshotBean));
    }

    /**
     * 更新待执行和执行中数量界面
     *
     * @param performTask   待执行数量
     * @param executingTask 执行中数量
     */
    private void updateTab(int performTask, int executingTask) {
        rg_tab_NotPerformed.setText("待执行" + performTask);
        rg_tab_performing.setText("执行中" + executingTask);
    }

}
