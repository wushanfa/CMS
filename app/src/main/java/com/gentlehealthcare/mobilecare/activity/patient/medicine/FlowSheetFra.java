package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.HashMap;
import java.util.Map;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.patient.common.IdentityScanFra;
import com.gentlehealthcare.mobilecare.activity.patient.common.MedicineNotExecutionFra;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.FlowConstant.PatientFlow;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.AuthenticationBean;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;
import com.gentlehealthcare.mobilecare.net.bean.CompleteExecuteBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIntravsSnapshotBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatrolBean;
import com.gentlehealthcare.mobilecare.net.bean.RecordInspectionTimeBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.AuthenticationRequest;
import com.gentlehealthcare.mobilecare.net.impl.CancelExecuteRequest;
import com.gentlehealthcare.mobilecare.net.impl.CompleteExecuteRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadIntravsSnapshotRequest;
import com.gentlehealthcare.mobilecare.net.impl.PatrolRequest;
import com.gentlehealthcare.mobilecare.net.impl.RecordInspectionTimeReq;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.AutoCallBackTextView;
import com.gentlehealthcare.mobilecare.view.CustomEditTextDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

/***
 * @author ouyang
 * @ClassName: FlowSheet
 * @Description: 给药中的流程界面
 * @date 2015年2月28日 下午5:45:57
 */
@SuppressLint("ValidFragment")
public class FlowSheetFra extends BaseFragment implements OnClickListener, View.OnLongClickListener {

    private SyncPatientBean patient;
    private AutoCallBackTextView tvInfo;
    private RadioGroup rg_tab;
    private Button btn_bottom_center;
    private Button btn_bottom_left;
    private Button btn_bottom_right;
    private PatientFlow pFlow;
    private FlowAct flowAct;
    private RadioButton rg_tab_NotPerformed;//待执行
    private RadioButton rg_tab_performing;//执行中
    private Map<String, String> saveMap = null;
    private ProgressDialog progressDialog = null;
    private AlertDialogTwoBtn alertDialog;
    private CustomEditTextDialog dialog = null;
    private int currentPerformTask;

    public Button getBtn_bottom_left() {
        return btn_bottom_left;
    }

    private PatientFlow patientFlow = null;
    private CheckMedicineBean checkMedicineBean;//核对药品，执行药品
    private Fragment currentFra = null;
    private OrderItemBean orderItemBean = null;
    private Dialog currentDialog = null;
    private String eventId = "";
    private String scanFlag = "";

    public FlowSheetFra(SyncPatientBean patient, PatientFlow patientFlow, OrderItemBean orderItemBean) {

        super();
        this.patient = patient;
        this.patientFlow = patientFlow;
        this.orderItemBean = orderItemBean;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_flow);
        SupportDisplay.resetAllChildViewParam(root);
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
        if (patient.getBedLabel() != null) {
            return "床号: " + patient.getBedLabel() + "\n" + "姓名: " + patient.getName();
        } else {
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
        if (patient.getBedLabel() != null) {
            return "床号: " + patient.getBedLabel() + "\n" + "姓名: " + patient.getName();
        } else {
            return "床号: " + "未分配" + "\n" + "姓名: " + patient.getName();
        }
    }

    public void doGestUp() {
        // TODO Auto-generated method stub
        tvInfo.collection();
    }

    public void doGestDown() {
        // TODO Auto-generated method stub
        tvInfo.expansion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saveMap = new HashMap<String, String>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        if (getArguments() != null) {
            if (getArguments().getString(GlobalConstant.KEY_FLAG_SCAN) != null) {
                scanFlag = getArguments().getString(GlobalConstant.KEY_FLAG_SCAN);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flow, null);
        flowAct = (FlowAct) getActivity();
        btn_bottom_left = (Button) view.findViewById(R.id.btn_left);
        btn_bottom_right = (Button) view.findViewById(R.id.btn_right);
        btn_bottom_center = (Button) view.findViewById(R.id.btn_center);
        tvInfo = (AutoCallBackTextView) view.findViewById(R.id.tv_info);
        tvInfo.setContentStr(getPatientInfo());
        tvInfo.setDetailStr(getPatientInfoDetail());
        tvInfo.collection();
        rg_tab_NotPerformed = (RadioButton) view.findViewById(R.id.rg_tab_NotPerformed);
        rg_tab_performing = (RadioButton) view.findViewById(R.id.rg_tab_performing);
        rg_tab = (RadioGroup) view.findViewById(R.id.rg_tab);
        btn_bottom_left.setText("取消");
        btn_bottom_right.setText("扫描");
        btn_bottom_left.setOnClickListener(this);
        btn_bottom_right.setOnClickListener(this);
        btn_bottom_right.setOnLongClickListener(this);
        btn_bottom_center.setOnClickListener(this);
        btn_bottom_center.setVisibility(View.GONE);
        //rg_tab.check(R.id.rg_tab_NotPerformed);
        fistShowWitchOne();
        rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                CCLog.i("setOnCheckedChangeListener" + "执行了");
                Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                CCLog.i(fragment + "");
                if (fragment instanceof MedicineListFra) {
                    MedicineListFra mlistFra = (MedicineListFra) fragment;
                    switch (arg1) {
                        case R.id.rg_tab_NotPerformed:
                            CCLog.i("rg_tab_NotPerformed" + "执行了");
                            rg_tab_NotPerformed.setTextColor(Color.BLACK);
                            rg_tab_performing.setTextColor(Color.GRAY);
                            mlistFra.SwitchTab(MedicineConstant.STATE_WAITING);
                            btn_bottom_right.setVisibility(View.VISIBLE);
                            btn_bottom_right.setText("扫描");
                            btn_bottom_left.setText("不执行");
                            btn_bottom_left.setVisibility(View.VISIBLE);
                            pFlow = PatientFlow.MEDICINE_LIST;
                            break;
                        case R.id.rg_tab_performing:
                            CCLog.i("rg_tab_performing" + "执行了");
                            rg_tab_NotPerformed.setTextColor(Color.GRAY);
                            rg_tab_performing.setTextColor(Color.BLACK);
                            mlistFra.SwitchTab(MedicineConstant.STATE_EXECUTING);
                            btn_bottom_left.setText("结束");
                            btn_bottom_left.setVisibility(View.VISIBLE);
                            pFlow = PatientFlow.MEDICINE_END;
                            btn_bottom_right.setText("巡视");
                            break;

                        default:
                            break;
                    }
                }

            }
        });

        String str = saveMap.get("leftBtnVisible");
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

        if (patientFlow == null) {
            IdentityScanFra scanFragment = new IdentityScanFra(patient);
            getChildFragmentManager().beginTransaction().add(R.id.fl_flow, scanFragment).commit();
            pFlow = PatientFlow.IDENTITY_CONFIRM;
        } else {
            if (patientFlow == PatientFlow.MEDICINE_LIST) {
                MedicineListFra listFra = new MedicineListFra(patient, MedicineConstant.STATE_WAITING,
                        FlowSheetFra.this);
                listFra.setCurrentPlanNo(orderItemBean.getPlanOrderNo());
                btn_bottom_center.setText("刷新");
                rg_tab.check(R.id.rg_tab_NotPerformed);
                rg_tab_NotPerformed.setTextColor(Color.BLACK);
                rg_tab_performing.setTextColor(Color.GRAY);
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, listFra).commit();
                pFlow = PatientFlow.MEDICINE_LIST;
                btn_bottom_right.setText("扫描");
                btn_bottom_right.setVisibility(View.VISIBLE);
                btn_bottom_left.setVisibility(View.VISIBLE);
                btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
                btn_bottom_left.setText("不执行");
            } else if (patientFlow == PatientFlow.PATROL_COUNT_DOWN) {
                PatrolCountDownFra countDownFragment = null;
                if (getArguments().containsKey(GlobalConstant.KEY_FLAG_SCAN_PATROL)) {
                    if (getArguments().getString(GlobalConstant.KEY_FLAG_SCAN_PATROL) == GlobalConstant
                            .VALUE_FLAG_SCAN_PATROL) {
                        countDownFragment = new PatrolCountDownFra(patient, orderItemBean, true);
                        btn_bottom_right.setVisibility(View.INVISIBLE);
                    } else {
                        countDownFragment = new PatrolCountDownFra(patient, orderItemBean);
                        btn_bottom_right.setVisibility(View.VISIBLE);
                    }
                } else {
                    countDownFragment = new PatrolCountDownFra(patient, orderItemBean);
                    btn_bottom_right.setVisibility(View.VISIBLE);
                }
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, countDownFragment).commit();
                btn_bottom_left.setVisibility(View.INVISIBLE);
                btn_bottom_right.setText("确认");
                btn_bottom_left.setText("观察");
                btn_bottom_center.setVisibility(View.INVISIBLE);
                pFlow = PatientFlow.MEDICINE_COUT_DOWN;
                ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
                btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
                btn_bottom_left.setTag("0");
            }
            pFlow = patientFlow;
        }
        DoLoadIntravsSnapshotRequest();
        if (GlobalConstant.VALUE_FLAG_SCAN.equals(scanFlag)) {
            scanIntroduce();
        }
        return view;
    }

    private void fistShowWitchOne() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getInt(GlobalConstant.KEY_NOTIFICATION2MEDICINE) == GlobalConstant
                .VALUE_NOTIFICATION2MEDICINE) {
            rg_tab.check(R.id.rg_tab_performing);
            rg_tab_NotPerformed.setTextColor(Color.GRAY);
            rg_tab_performing.setTextColor(Color.BLACK);
        } else {
            rg_tab.check(R.id.rg_tab_NotPerformed);
            rg_tab_NotPerformed.setTextColor(Color.BLACK);
            rg_tab_performing.setTextColor(Color.GRAY);
        }
    }

    /**
     * 巡视记录
     */
    private void DoPatrolRecord() {
        btn_bottom_right.setVisibility(View.VISIBLE);
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof PatrolRecordFra) {
            CompleteExecuteBean completeExecuteBean = new CompleteExecuteBean();
            completeExecuteBean.setPatId(patient.getPatId());
            completeExecuteBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
            PatrolRecordFra fra = (PatrolRecordFra) currentFra;
            completeExecuteBean.setPerformDesc(fra.getRecordTxt());
            if (fra.isException()) {
                completeExecuteBean.setStatus("-1");
                LoadVariationDictBean loadVariationDictBean = fra.getLoadVariationDictBean();
                completeExecuteBean.setVarianceCause(loadVariationDictBean.getItemCode());
                completeExecuteBean.setVarianceCauseDesc(loadVariationDictBean.getItemName());
                completeExecuteBean.setCompleteDosage(fra.getTotal());
            } else {
                completeExecuteBean.setStatus("1");
            }
            progressDialog.setMessage("数据加载，请稍后...");
            progressDialog.show();
            RequestManager.connection(new CompleteExecuteRequest(getActivity(), new
                    IRespose<CompleteExecuteBean>() {
                        @Override
                        public void doResult(CompleteExecuteBean completeExecuteBean, int id) {

                        }

                        @Override
                        public void doResult(String result) throws KeyObsoleteException {
                            CompleteExecuteBean completeExecuteBean = new Gson().fromJson(result,
                                    CompleteExecuteBean
                                            .class);
                            if (completeExecuteBean.getResult()) {
                                DoLoadIntravsSnapshotRequest();
                                String[] strings = null;
                                if (orderItemBean.getInjectionTool().equals("0")) {
                                    strings = new String[]{"1.封管", "2.终末处理"};
                                } else {
                                    strings = new String[]{"1.拔针", "2.终末处理"};
                                }
                                SealTubeFra copySealTubeFra = new SealTubeFra(strings);
                                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, copySealTubeFra)
                                        .commit();
                                btn_bottom_center.setVisibility(View.INVISIBLE);
                                btn_bottom_left.setVisibility(View.INVISIBLE);
                                btn_bottom_right.setText("确认");
                                pFlow = PatientFlow.SEALING;
                            } else {
                                ShowToast(completeExecuteBean.getMsg());
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
                    }, 0, true, completeExecuteBean));
        }
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {
            case R.id.btn_center:
                switch (pFlow) {
                    case MEDICINE_LIST:
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (currentFra instanceof MedicineListFra) {
                            ((MedicineListFra) currentFra).refreshMedicineList();
                        }
                        break;
                    case PATROL_RECORD:
                        DoPatrolRecord();
                        break;
                }
                break;
            case R.id.btn_left:
                switch (pFlow) {
                    case MEDICINE_LIST:
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (currentFra instanceof MedicineListFra) {
                            orderItemBean = ((MedicineListFra) currentFra).getOrderItemBean();
                            if (orderItemBean == null) {
                                ShowToast("请选择一项药品信息");
                                return;
                            }
                        }
                        btn_bottom_center.setVisibility(View.GONE);
                        //不执行
                        NotExecutionFra notExecution = new NotExecutionFra(patient);
                        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, notExecution).commit();
                        btn_bottom_left.setVisibility(View.INVISIBLE);
                        btn_bottom_right.setVisibility(View.VISIBLE);
                        btn_bottom_right.setText("确认");
                        pFlow = PatientFlow.NOT_EXECUTION;
                        break;
                    case IDENTITY_CONFIRM:
                        getActivity().setResult(GlobalConstant.RESUlt_CODE);
                        getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
                        getActivity().finish();
                        break;
                    case MEDICINE_PATORL:
                    case MEDICINE_INJECTION:
                    case MEDICINE_INTRODUCTION:
                        //取消  不给药
                        MedicineNotExecutionFra infusionNotExecutFragment = new MedicineNotExecutionFra(patient);
                        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow,
                                infusionNotExecutFragment).commit();
                        btn_bottom_left.setVisibility(View.INVISIBLE);
                        btn_bottom_right.setVisibility(View.VISIBLE);
                        btn_bottom_right.setText("回清单");
                        pFlow = PatientFlow.MEDICINE_CANCEL;
                        break;
                    case PATROL_RECORD:
                        btn_bottom_right.setVisibility(View.VISIBLE);
                        //记录
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (currentFra instanceof PatrolRecordFra) {
                            String[] strings = new String[]{"15分钟", "30分钟", "忽略"};
                            final String recordTxt = ((PatrolRecordFra) currentFra).getRecordTxt();

                            currentDialog = new AlertDialog.Builder(getActivity()).setTitle("选择下次巡视时间")
                                    .setCancelable(false).setItems(strings, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String str = "15";
                                            switch (i) {
                                                case 1:
                                                    str = "30";
                                                    break;
                                                case 2:
                                                    str = "0";
                                                    break;
                                            }
                                            DoRecordInspectionTimeReq(str, recordTxt, MedicineConstant
                                                    .STATE_EXECUTING);
                                        }
                                    }).create();
                            currentDialog.show();
                        }
                        break;
                    case MEDICINE_END:
                        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);

                        if (currentFra instanceof MedicineListFra) {
                            orderItemBean = ((MedicineListFra) currentFra).getOrderItemBean();
                            if (orderItemBean == null) {
                                ShowToast("暂无药品可供执行该操作");
                                return;
                            }

                            PatrolRecordFra recordFra = new PatrolRecordFra(patient, orderItemBean);
                            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, recordFra).commit();
                            btn_bottom_left.setVisibility(View.INVISIBLE);
                            btn_bottom_left.setText("巡视");
                            btn_bottom_right.setText("封管");
                            btn_bottom_right.setBackgroundResource(R.drawable.skin_btn_blue);
                            btn_bottom_center.setVisibility(View.INVISIBLE);
                            btn_bottom_center.setText("封管");
                            pFlow = PatientFlow.PATROL_RECORD;
                        }
                        break;

                    case PATROL_COUNT_DOWN:
                        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                        if (fragment instanceof PatrolCountDownFra && !((PatrolCountDownFra) fragment).isValid()) {
                            Toast.makeText(getActivity(), "请先扫描药品", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog = new CustomEditTextDialog(getActivity(), R.style.myDialogTheme);
                        dialog.setContent(new String[]{"记录内容:"});
                        dialog.setTitle("提示");
                        dialog.setLeftButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setRightButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                progressDialog.show();
                                PatrolBean patrolBean = new PatrolBean();
                                patrolBean.setPerformDesc(dialog.getText()[0]);
                                patrolBean.setPatId(patient.getPatId());
                                patrolBean.setType(0);
                                patrolBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
                                DoRecInspectResultReq(patrolBean);
                            }

                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_right:
                doOnClick();
                break;

            default:
                break;
        }
    }

    private void DoRecInspectResultReq(PatrolBean patrolBean) {
        DoRecInspectResultReq(patrolBean, false);
    }

    /**
     * 观察/巡视 接口
     *
     * @param patrolBean
     */
    private void DoRecInspectResultReq(PatrolBean patrolBean, final boolean backSheet) {
        progressDialog.show();
        RequestManager.connection(new PatrolRequest(getActivity(), new IRespose<PatrolBean>() {
            @Override
            public void doResult(PatrolBean patrolBean, int id) {
            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                PatrolBean patrolBean = new Gson().fromJson(result, PatrolBean.class);
                if (patrolBean.isResult()) {
                    ShowToast("提交成功");
                    if (backSheet) {
                        PatrolCountDownFra countDownFragment = new PatrolCountDownFra(patient, orderItemBean,
                                true);
                        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, countDownFragment)
                                .commit();
                        btn_bottom_left.setVisibility(View.INVISIBLE);
                        btn_bottom_right.setText("确认");
                        btn_bottom_left.setText("观察");
                        btn_bottom_right.setVisibility(View.INVISIBLE);
                        btn_bottom_center.setVisibility(View.INVISIBLE);
                        pFlow = PatientFlow.PATROL_COUNT_DOWN;
                    }
                } else {
                    ShowToast(patrolBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), "记录异常", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.netstate_content), Toast.LENGTH_SHORT).show();
            }
        }, 0, true, patrolBean));
    }

    /**
     * 封管界面
     */
    public void doSealing() {
        flowAct.updateFlow(pFlow);
        getActivity().setResult(GlobalConstant.RESUlt_CODE);
        getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
        getActivity().finish();
    }

    /**
     * 待执行 药品注射-跳转到新巡视倒计时  如果还有待执行工作,点击确定跳转药品列表,否则退出
     */
    private void doMedicineCoutDown() {
        if (currentPerformTask > 0) {
            DoGotoMedicineListFra();
        } else {
            getActivity().setResult(GlobalConstant.RESUlt_CODE);
            getActivity().finish();
        }
    }

    /**
     * 只有在药界面中的记录界面才显示
     */
    public void DoCurrentViewIsPatrol(int currentItem) {
        if (getActivity() == null || ((FlowAct) getActivity()).getBtn_patientflow() == null)
            return;

        if (pFlow == PatientFlow.PATROL_COUNT_DOWN && currentItem == 0)
            ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
        else
            ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.GONE);
    }

    /**
     * 切换到药品列表界面
     */
    public void DoGotoMedicineListFra() {
        //跳转药品列表界面
        MedicineListFra listFra = new MedicineListFra(patient, MedicineConstant.STATE_WAITING, FlowSheetFra.this);
        btn_bottom_center.setText("刷新");
        rg_tab.check(R.id.rg_tab_NotPerformed);
        rg_tab_NotPerformed.setTextColor(Color.BLACK);
        rg_tab_performing.setTextColor(Color.GRAY);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, listFra).commit();
        pFlow = PatientFlow.MEDICINE_LIST;
        btn_bottom_right.setText("扫描");
        btn_bottom_right.setVisibility(View.VISIBLE);
        btn_bottom_left.setVisibility(View.VISIBLE);
        btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
        btn_bottom_left.setText("不执行");
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
                    /**
                     * 暂不显示-你好，我将要帮你准备施打输液
                     */
//                    IdentityScanSuccessFra confirmFragment = new IdentityScanSuccessFra(patient, 0);
//                    getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, confirmFragment).commit();
                    eventId = authenticationBean.getEventId();
                    btn_bottom_left.setVisibility(View.INVISIBLE);
                    btn_bottom_right.setVisibility(View.VISIBLE);
//                    btn_bottom_right.setText("确认");
//                    pFlow = PatientFlow.EXPLAIN;
//                    ((FlowAct) getActivity()).updateFlow(PatientFlow.EXPLAIN);
                    pFlow = PatientFlow.MEDICINE_LIST;
                    DoGotoMedicineListFra();
                } else {
                    ShowToast(authenticationBean.getMsg());
                }
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), "操作异常.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), getString(R.string.netstate_content), Toast.LENGTH_SHORT).show();
            }
        }, 0, true, authenticationBean));

    }

    /**
     * 不执行工作处理
     */
    private void DoNotExecution() {
        progressDialog.show();
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof NotExecutionFra) {
            CompleteExecuteBean t = new CompleteExecuteBean();
            t.setCompleteDosage("0");
            t.setPerformDesc(((NotExecutionFra) currentFra).getNotExecutionReason());
            t.setPlanOrderNo(orderItemBean.getPlanOrderNo());
            t.setVarianceCause("");
            t.setPatId(patient.getPatId());
            t.setVarianceCauseDesc(((NotExecutionFra) currentFra).getNotExecutionReason());
            RequestManager.connection(new CancelExecuteRequest(getActivity(), new IRespose<CompleteExecuteBean>() {
                @Override
                public void doResult(CompleteExecuteBean completeExecuteBean, int id) {

                }

                @Override
                public void doResult(String result) throws KeyObsoleteException {
                    CompleteExecuteBean completeExecuteBean = new Gson().fromJson(result, CompleteExecuteBean
                            .class);
                    if (completeExecuteBean.getResult()) {
                        if (completeExecuteBean.getPerformTask() <= 0) {
                            getActivity().setResult(GlobalConstant.RESUlt_CODE);
                            getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim
                                    .slide_out_right);
                            getActivity().finish();
                        } else {
                            DoGotoMedicineListFra();
                        }
                    } else {
                        ShowToast(completeExecuteBean.getMsg());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void doException(Exception e, boolean networkState) {
                    progressDialog.dismiss();
                    if (networkState)
                        Toast.makeText(getActivity(), "执行异常", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), getString(R.string.netstate_content), Toast.LENGTH_SHORT)
                                .show();
                }
            }, 0, true, t));
        }

    }

    /**
     * 快速键
     */
    public void doOnClick() {
        if (currentDialog != null && currentDialog.isShowing())
            return;
        if (progressDialog != null && progressDialog.isShowing())
            return;
        switch (pFlow) {
            case IDENTITY_CONFIRM:
                //病人身份扫描
                ShowToast("请使用红外扫描核对患者");
                break;
            case EXPLAIN:
                // 用药解释说明
                DoGotoMedicineListFra();
                break;
            case MEDICINE_LIST:
                //药品列表
                if (rg_tab.getCheckedRadioButtonId() == R.id.rg_tab_NotPerformed)
                    DoMedicineList();
                break;
            case NOT_EXECUTION:
                //不执行
                DoNotExecution();
                break;
            case MEDICINE_INTRODUCTION:
                //药物简介
                currentDialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false)
                        .setMessage("您确定手动核对药品信息的正确吗?").setNegativeButton("确定", new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DoIntroduction(null, true);
                            }
                        }).setPositiveButton("取消", null).create();
                currentDialog.show();
                break;
            /**
             * 2016-3-26注释，重复提交
             */
//            case MEDICINE_INJECTION:
//                //药品执行
//                if (orderItemBean.getInjectionTool() == null) {
//                    currentDialog = new AlertDialog.Builder(getActivity()).setTitle("请选择注射工具").setCancelable
//                            (false).setItems(new String[]{"留置针", "钢针"}, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            orderItemBean.setInjectionTool(i + "");
//                            DoAddSpeed();
//                        }
//                    }).create();
//                    currentDialog.show();
//                } else {
//                    DoInjection("MEDICINE_INJECTION+药品执行");
//                }
//                break;
            case MEDICINE_COUT_DOWN:
                //药品计时
                doMedicineCoutDown();
                break;
            case MEDICINE_END:
                //结束给药
                if (rg_tab.getCheckedRadioButtonId() == R.id.rg_tab_performing) {
                    DoMedicineEnd();
                }
                break;
            case PATROL_COUNT_DOWN:
                //执行中中倒计时
                Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
                if (fragment instanceof PatrolCountDownFra && !((PatrolCountDownFra) fragment).isValid()) {
                    Toast.makeText(getActivity(), "请先扫描药品", Toast.LENGTH_SHORT).show();
                    return;
                }
                DoCountDown();
                break;
            case MEDICINE_CANCEL:
                //取消给药
                DoGotoMedicineListFra();
                break;
            case SEALING:
                //封管界面
                doSealing();
                break;
            case PATROL_RECORD:
                //巡视记录 完成按钮
                DoPatrolRecord();
                break;
            case MEDICINE_PATORL:
                PatrolRecordFra patrolRecordFra = new PatrolRecordFra(patient, orderItemBean);
                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, patrolRecordFra).commit();
                btn_bottom_left.setVisibility(View.VISIBLE);
                if (orderItemBean.getInjectionTool().equals("0"))
                    btn_bottom_right.setText("封管");
                else
                    btn_bottom_right.setText("拔针");
                btn_bottom_left.setText("巡视");
                btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_blue_unselect);
                btn_bottom_center.setText("封管");
                pFlow = PatientFlow.PATROL_RECORD;
                ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 执行 巡视记录
     */
    public void DoMedicineEnd() {
        currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (currentFra instanceof MedicineListFra) {
            orderItemBean = ((MedicineListFra) currentFra).getOrderItemBean();
            if (orderItemBean == null) {
                ShowToast("请选择一项药品信息");
                return;
            }
            PatrolCountDownFra countDownFragment = new PatrolCountDownFra(patient, orderItemBean, true);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, countDownFragment).commit();
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_right.setVisibility(View.INVISIBLE);
            btn_bottom_right.setText("确认");
            btn_bottom_left.setText("观察");
            btn_bottom_center.setVisibility(View.INVISIBLE);
            ((FlowAct) getActivity()).updateFlow(PatientFlow.PATROL_RECORD);
            ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
            pFlow = PatientFlow.PATROL_COUNT_DOWN;
        }

    }

    /**
     * 显示俩按钮
     */
    public void showBottomBtn() {
        btn_bottom_right.setVisibility(View.VISIBLE);
        btn_bottom_left.setVisibility(View.VISIBLE);
    }

    /**
     * 执行 巡视
     */
    public void DoCountDown() {
        ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.GONE);
        flowAct.updateFlow(pFlow);
        PatrolRecordFra patrolRecordFra = new PatrolRecordFra(patient, orderItemBean);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, patrolRecordFra).commit();
        pFlow = PatientFlow.PATROL_RECORD;
        btn_bottom_right.setText("封管");
        btn_bottom_left.setVisibility(View.VISIBLE);
        btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_blue_unselect);
        btn_bottom_left.setText("巡视");

    }

    /**
     * 记录巡视间隔时间请求
     *
     * @param time
     */
    private void DoRecordInspectionTimeReq(String time, String recordTxt, final String state) {
        progressDialog.show();
        RecordInspectionTimeBean recordInspectionTimeBean = new RecordInspectionTimeBean();
        recordInspectionTimeBean.setPatId(patient.getPatId());
        recordInspectionTimeBean.setType(0);
        recordInspectionTimeBean.setPerformDesc(recordTxt);
        recordInspectionTimeBean.setInspectionTime(time);
        recordInspectionTimeBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        RequestManager.connection(new RecordInspectionTimeReq(getActivity(), new
                IRespose<RecordInspectionTimeBean>() {
                    @Override
                    public void doResult(RecordInspectionTimeBean recordInspectionTimeBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        RecordInspectionTimeBean recordInspectionTimeBean = new Gson().fromJson(result,
                                RecordInspectionTimeBean.class);
                        if (recordInspectionTimeBean.getResult()) {
                            if (state.equals(MedicineConstant.STATE_WAITING)) {
                                PatrolCountDownFra copyPatrolCountDownFra = new PatrolCountDownFra(patient,
                                        orderItemBean);
                                getChildFragmentManager().beginTransaction().replace(R.id.fl_flow,
                                        copyPatrolCountDownFra).commit();
                                btn_bottom_left.setVisibility(View.INVISIBLE);
                                btn_bottom_right.setText("确认");
                                btn_bottom_left.setText("观察");
                                ((FlowAct) getActivity()).updateFlow(PatientFlow.PATROL_RECORD);
                                pFlow = PatientFlow.MEDICINE_COUT_DOWN;
                            } else {
                                doMedicineCoutDown();
                            }
                        } else {
//                            ShowToast(recordInspectionTimeBean.getMsg());
                            ShowToast("test");
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState)
                            Toast.makeText(getActivity(), "操作异常.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), getString(R.string.netstate_content), Toast
                                    .LENGTH_SHORT).show();
                    }
                }, 0, true, recordInspectionTimeBean));
    }

    /**
     * 弹出输入滴速对话框
     */
    private void DoAddSpeed() {
        if (!"".equals(orderItemBean.getSpeed())) {
            DoInjection();
            return;
        }
        final EditText editText = new EditText(getActivity());
        editText.setKeyListener(new DigitsKeyListener(false, true));
        currentDialog = new AlertDialog.Builder(getActivity()).setTitle("请输入滴速值").setCancelable(false).setView
                (editText).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backSheet();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText().toString().length() > 0) {
                    orderItemBean.setSpeed(editText.getText().toString());
                    DoInjection();
                } else {
                    ShowToast("请输入滴速");
                    backSheet();
                }
            }
        }).create();

        currentDialog.show();
    }

    /**
     * 执行 注射
     */
    public void DoInjection() {
        progressDialog.setMessage("正在进行注射操作,请销等...");
        progressDialog.show();
        checkMedicineBean = new CheckMedicineBean();
        checkMedicineBean.setPatId(patient.getPatId());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setType(0);
        checkMedicineBean.setEventId(eventId);
        checkMedicineBean.setInjectionTool(orderItemBean.getInjectionTool());
        String url = UrlConstant.GetExecuteMedicine() + checkMedicineBean.toString();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CheckMedicineBean checkMedicineBean = new Gson().fromJson(result, CheckMedicineBean.class);
                if (checkMedicineBean.getResult()) {
                    //提交记录巡视间隔时间
                    String[] strings = new String[]{"15分钟", "30分钟", "忽略"};

                    currentDialog = new AlertDialog.Builder(getActivity()).setTitle("选择下次巡视时间").setCancelable
                            (false).setItems(strings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String str = "15";
                            switch (i) {
                                case 1:
                                    str = "30";
                                    break;
                                case 2:
                                    str = "0";
                                    break;
                                default:
                                    break;
                            }
                            DoRecordInspectionTimeReq(str, "", MedicineConstant.STATE_WAITING);
                        }
                    }).create();
                    currentDialog.show();
                    DoLoadIntravsSnapshotRequest();
                } else {
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "操作异常.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 核对药品信息
     */
    private void DoIntroduction(final String relatedBarcode, boolean okMode) {
        progressDialog.show();
        checkMedicineBean = new CheckMedicineBean();
        checkMedicineBean.setPatId(patient.getPatId());
        checkMedicineBean.setInjectionTool(orderItemBean.getInjectionTool());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setRelatedBarcode(relatedBarcode);
        if (okMode) {
            checkMedicineBean.setCheckMode("manually");
        }
        checkMedicineBean.setType(0);
        String url = UrlConstant.GetCheckMedicine(checkMedicineBean.getType()) + checkMedicineBean.toString();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CheckMedicineBean checkMedicineBean = new Gson().fromJson(result, CheckMedicineBean.class);
                if (checkMedicineBean.getResult()) {
                    /**
                     * 注射界面暂时不需要
                     */
                    flowAct.updateFlow(pFlow);
//                    MedicineExecutedFra medicineExecuted = new MedicineExecutedFra(patient, orderItemBean);
//                    getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, medicineExecuted)
// .commit();
//                    btn_bottom_right.setText("注射");
                    pFlow = PatientFlow.MEDICINE_INJECTION;

                    if (orderItemBean.getInjectionTool() == null) {
                        currentDialog = new AlertDialog.Builder(getActivity()).setTitle("请选择注射工具").setCancelable
                                (false).setItems(new String[]{"留置针", "钢针"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                orderItemBean.setInjectionTool(i + "");
//                            DoInjection();
                                DoAddSpeed();
                            }
                        }).create();
                        currentDialog.show();
                    } else {
                        DoInjection();
                    }
                } else {
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "操作异常.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 扫描药品列表
     */
    private void DoMedicineList() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_flow);
        if (fragment instanceof MedicineListFra) {
            MedicineListFra mlistFra = (MedicineListFra) fragment;
            orderItemBean = mlistFra.getOrderItemBean();
            if (orderItemBean == null) {
                ShowToast("请选择一项药品信息");
                return;
            }
            btn_bottom_center.setVisibility(View.GONE);

            MedicineScanSuccessFra infusionFragment = new MedicineScanSuccessFra(patient, orderItemBean);
            flowAct.updateFlow(pFlow);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, infusionFragment).commit();
            btn_bottom_left.setVisibility(View.VISIBLE);
            btn_bottom_right.setText("给药");
            btn_bottom_left.setText("取消");
            pFlow = PatientFlow.MEDICINE_INTRODUCTION;
//            }
        }
    }

    /**
     * 扫描药品列表
     */
    public void scanIntroduce() {
        if (orderItemBean == null) {
            ShowToast("请选择一项药品信息");
            return;
        }
        btn_bottom_center.setVisibility(View.GONE);
        MedicineScanSuccessFra infusionFragment = new MedicineScanSuccessFra(patient, orderItemBean);
        flowAct.updateFlow(pFlow);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, infusionFragment).commit();
        btn_bottom_left.setVisibility(View.VISIBLE);
        btn_bottom_right.setText("给药");
        btn_bottom_left.setText("取消");
        pFlow = PatientFlow.MEDICINE_INTRODUCTION;
    }

    /**
     * 返回上一步流程
     */
    public void backSheet() {
        if (pFlow == PatientFlow.IDENTITY_CONFIRM) {
            alertDialog = new AlertDialogTwoBtn(getActivity());
            alertDialog.setTitle("提示");
            alertDialog.setMessage("是否回到主界面");
            alertDialog.setLeftButton("取消", new OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setRightButton("确定", new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), HomeAct.class));
                    alertDialog.dismiss();
                    getActivity().setResult(GlobalConstant.RESUlt_CODE);
                    getActivity().finish();
                }
            });
        } else if (pFlow == PatientFlow.MEDICINE_LIST || pFlow == PatientFlow.MEDICINE_END) {
            //回到确定患者身份
            IdentityScanFra scanFragment = new IdentityScanFra(patient);

            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, scanFragment).commit();
            pFlow = PatientFlow.IDENTITY_CONFIRM;
            btn_bottom_right.setText("扫描");

            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_center.setVisibility(View.GONE);
            ((FlowAct) getActivity()).updateFlow(PatientFlow.EXPLAIN);
        } else if (pFlow == PatientFlow.MEDICINE_INTRODUCTION || pFlow == PatientFlow.MEDICINE_PATORL || pFlow
                == PatientFlow.PATROL_COUNT_DOWN || pFlow == PatientFlow.NOT_EXECUTION || pFlow == PatientFlow
                .MEDICINE_COUT_DOWN || pFlow == PatientFlow.SEALING) {
            //回到药品列表界面
            DoGotoMedicineListFra();
        } else if (pFlow == PatientFlow.MEDICINE_INJECTION) {
            //回到给要界面
            MedicineScanSuccessFra infusionFragment = new MedicineScanSuccessFra(patient, orderItemBean);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, infusionFragment).commit();
            btn_bottom_left.setVisibility(View.VISIBLE);
            btn_bottom_right.setText("给药");
            btn_bottom_left.setText("取消");
            pFlow = PatientFlow.MEDICINE_INTRODUCTION;
        } else if (pFlow == PatientFlow.PATROL_RECORD) {
            PatrolCountDownFra countDownFragment = new PatrolCountDownFra(patient, orderItemBean, true);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, countDownFragment).commit();
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_right.setVisibility(View.INVISIBLE);
            btn_bottom_right.setText("确认");
            btn_bottom_left.setText("观察");

            btn_bottom_center.setVisibility(View.INVISIBLE);
            ((FlowAct) getActivity()).getBtn_patientflow().setVisibility(View.VISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_left.setTag("0");
            pFlow = PatientFlow.MEDICINE_PATORL;
        } else if (pFlow == PatientFlow.EXPLAIN) {
            //回到确定患者身份
            IdentityScanFra scanFragment = new IdentityScanFra(patient);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_flow, scanFragment).commit();
            pFlow = FlowConstant.PatientFlow.IDENTITY_CONFIRM;
            btn_bottom_right.setText("扫描");
            btn_bottom_left.setVisibility(View.INVISIBLE);
            btn_bottom_left.setBackgroundResource(R.drawable.skin_btn_red);
            btn_bottom_center.setVisibility(View.GONE);
            ((FlowAct) getActivity()).updateFlow(FlowConstant.PatientFlow.IDENTITY_CONFIRM);
        }
    }


    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        if (pFlow == FlowConstant.PatientFlow.IDENTITY_CONFIRM) {
            DoIdentityConfirm(result.equals(patient.getPatCode()));
        } else if (pFlow == PatientFlow.MEDICINE_INTRODUCTION) {
            if (!progressDialog.isShowing())
                DoIntroduction(result, false);
        } else if (pFlow == PatientFlow.PATROL_COUNT_DOWN) {
            currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
            if (currentFra instanceof PatrolCountDownFra) {
                PatrolCountDownFra patrolCountDownFra = (PatrolCountDownFra) currentFra;
                if (!patrolCountDownFra.isShow()) patrolCountDownFra.DoIntroduction(result, false);
            }
        } else if (pFlow == PatientFlow.MEDICINE_LIST) {
            currentFra = getChildFragmentManager().findFragmentById(R.id.fl_flow);
            if (currentFra instanceof MedicineListFra) {
                MedicineListFra mlistFra = (MedicineListFra) currentFra;
                if (!mlistFra.isShow()) {
                    mlistFra.setCurrentPlanNo(result);
                    mlistFra.refreshMedicineList();
                    mlistFra.setPosition(result);
                }
            }
        }

    }


    /**
     * 获取待执行和执行中数量
     */
    private void DoLoadIntravsSnapshotRequest() {
        LoadIntravsSnapshotBean loadIntravsSnapshotBean = new LoadIntravsSnapshotBean();
        loadIntravsSnapshotBean.setPatId(patient.getPatId());
        RequestManager.connection(new LoadIntravsSnapshotRequest(getActivity(), new
                IRespose<LoadIntravsSnapshotBean>() {
                    @Override
                    public void doResult(LoadIntravsSnapshotBean loadIntravsSnapshotBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        LoadIntravsSnapshotBean loadIntravsSnapshotBean = new Gson().fromJson(result,
                                LoadIntravsSnapshotBean.class);
                        updateTab(loadIntravsSnapshotBean.getPerformTask(), loadIntravsSnapshotBean
                                .getExecutingTask());
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        updateTab(0, 0);

                    }
                }, 0, true, loadIntravsSnapshotBean));
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
                        currentDialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable
                                (false).setView(editText).setNegativeButton("确定", new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (editText.getText().toString().equals("")) {
                                    ShowToast("请输入编号值");
                                } else {
                                    if (pFlow == PatientFlow.IDENTITY_CONFIRM)
                                        DoIdentityConfirm(editText.getText().toString().equals(patient
                                                .getPatCode()));
                                    else if (pFlow == PatientFlow.MEDICINE_INTRODUCTION)
                                        DoIntroduction(editText.getText().toString(), false);
                                }
                            }
                        }).setPositiveButton("取消", null).create();
                        currentDialog.show();
                        ShowToast("请使用红外扫描核对患者");
                        break;

                }
                break;
        }
        return false;
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
        this.currentPerformTask = performTask;

    }

    public void updateLeftTab(int performTask) {
        rg_tab_NotPerformed.setText("待执行" + performTask);
        this.currentPerformTask = performTask;
    }

    public void updateRightTab(int executingTask) {
        rg_tab_performing.setText("执行中" + executingTask);
    }

}
