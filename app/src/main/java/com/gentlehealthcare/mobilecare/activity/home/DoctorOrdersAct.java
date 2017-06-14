package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.ExeRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteNoPerformedBean;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteToEndTimeRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteToOrgRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.ExecutionOrderBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadDocOrdersBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrderClassBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderScanBean;
import com.gentlehealthcare.mobilecare.net.bean.SkinResult;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.insertAppraislRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.insertExeRecordBean;
import com.gentlehealthcare.mobilecare.tool.BeanTrans;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.gentlehealthcare.mobilecare.view.adapter.DocAppraislAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.DoctorOrderAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.DoctorOrderAdapter.CallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 医嘱执行
 */
public class DoctorOrdersAct extends BaseActivity implements CallBack, OnClickListener, OnItemLongClickListener,
        ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, PullToRefreshBase
                .OnRefreshListener {
    private static final String TAG = "医嘱列表";
    private ExpandableListView elv_doctororder;// 医嘱查询列表控件
    private PullToRefreshExpandableListView pullToRefreshExpandListView;
    private DoctorOrderAdapter adapter = null;
    private SyncPatientBean patient;
    @ViewInject(R.id.btn_today)
    private Button btn_today;
    @ViewInject(R.id.btn_template)
    private Button btn_template;
    @ViewInject(R.id.btn_order_class)
    private Button btn_order_class;
    private ProgressDialog progressDialog = null;
    private List<LoadDocOrdersBean> list = new ArrayList<LoadDocOrdersBean>();
    // 医嘱列表数据
    private String patId;// 当前病人ID
    private String planTime;// 计划时间
    private String orderType = "null";// 长期，临时
    private String status = "null";// 执行状态
    private String templateId = "null";
    private String orderClass = "null";
    private LoadOrdersBean loadOrdersBean;
    /**
     * 异常字典
     */
    private List<ExeRecordBean> exeList = new ArrayList<ExeRecordBean>();
    /**
     * 病人
     */
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();
    /**
     * 评论字典表
     */
    private List<LoadAppraislRecordBean> larList = new ArrayList<LoadAppraislRecordBean>();
    private DocAppraislAdapter docAppraislAdapter;
    private boolean orderAppraislFlag = false;
    private List<LoadOrderClassBean> orderClassList = new ArrayList<LoadOrderClassBean>();
    private String[] txt;
    private String[] orderstype;
    private String[] itemCode;
    private Button btn_back;
    private TextView tv_head, tv_bed_label, tv_parent;
    private EditText createEditText;
    private CheckBox checkBox1;
    private ListView appraisal_ll;
    private LinearLayout order_ll;
    private Button order_btn_dialog;
    /**
     * 评价内容
     */
    private String createText = "";
    /**
     * 异常内容
     */
    private String exeText = "";
    /**
     * 结束时间
     */
    private String endTimeText = "";
    /**
     * 开始时间
     */
    private String startTimeText = "";
    /**
     * detail
     */
    private TextView createtv;
    private ImageView closed;
    /**
     * endTime
     */
    private int arrive_year = 0;
    private int arrive_month = 0;
    private int arrive_day = 0;
    private int arrive_hour = 0;
    private int arrive_min = 0;
    private View dateView;
    private DatePicker datepick;
    private TimePicker timepick;
    private String myLongDate = new String();
    private int performStatus = 0;
    private AlertDialog orderBaseDialog;
    private LinearLayout ll_pat_info;

    private String result = "";

    private int whichPatients = 0;

    private MyPatientDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fra_doctororder_main);
        ViewUtils.inject(this);
        Log.d("sxz","医嘱执行");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.load_order_message));
        HidnGestWindow(true);

        syncDeptPatientTable(0);
        loadAppraisl();
        loadExe();

//        patId = getIntent().getStringExtra("patId");
//        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
//        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
//        patients = (List<SyncPatientBean>) getIntent().getSerializableExtra("allPatient");
//        exeList = (List<ExeRecordBean>) getIntent().getSerializableExtra("exception");
//        larList = (List<DictCommonBean>) getIntent().getSerializableExtra("appraise");

        planTime = DateTool.getCurrDateTime();
//        initMethod();
        arrive_year = DateTool.getCurrYear();
        arrive_month = DateTool.getCurrMonth();
        arrive_day = DateTool.getCurrDay();
        arrive_hour = DateTool.getCurrHour();
        arrive_min = DateTool.getCurrMIn();
        initView();
        initBind();

        elv_doctororder.setAdapter(adapter = new DoctorOrderAdapter(this, list, this));
    }

    private void initBind() {
        btn_back.setOnClickListener(this);
        btn_template.setOnClickListener(this);
        btn_today.setOnClickListener(this);
        btn_order_class.setOnClickListener(this);
        elv_doctororder.setOnItemLongClickListener(this);
        elv_doctororder.setOnChildClickListener(this);
        elv_doctororder.setOnGroupClickListener(this);
        pullToRefreshExpandListView.setOnRefreshListener(this);
    }

    private void initView() {
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_bed_label = (TextView) findViewById(R.id.tv_bed_label);
        tv_parent = (TextView) findViewById(R.id.tv_parent);
        pullToRefreshExpandListView = (PullToRefreshExpandableListView) findViewById(R.id.elv_doctororder);
        elv_doctororder = pullToRefreshExpandListView.getRefreshableView();
        ll_pat_info = (LinearLayout) findViewById(R.id.ll_pat_info);
        ll_pat_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_pat_info:
                        dialog = new MyPatientDialog(DoctorOrdersAct.this, new MyPatientDialog.MySnListener() {

                            @Override
                            public void myOnItemClick(View view, int position, long id) {
                                patient = patients.get(position);
                                tv_parent.setText(patient.getName());
                                tv_bed_label.setText(patient.getBedLabel() + "床");
                                patId = patient.getPatId();
                                whichPatients = position;
                                DoLoadOrder();
                                dialog.dismiss();
                            }

                            @Override
                            public void onRefresh() {

                            }
                        }, whichPatients, patients);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        break;
                    default:
                        break;
                }
            }
        });
        elv_doctororder.setSelector(R.color.transparent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GlobalConstant.REQUEST_CODE == requestCode) {
            if (resultCode == GlobalConstant.RESUlt_CODE) {
                DoLoadOrder();
            }
        }
    }

    @Override
    public void onClick(View v, int groupPostion, int childPostion) {
        switch (v.getId()) {
            case R.id.pingjia:
                showEvaluationDialog(groupPostion, childPostion);
                break;
            case R.id.yichang:
                ShowSighChooseAlert(groupPostion, childPostion);
                break;
            case R.id.detail:
                showDetailAlert(groupPostion, childPostion);
                break;
            case R.id.endtime:
                DateAlertDialog(groupPostion, childPostion);
                break;
            case R.id.top_right_ll:
                judgeIsSubmit(groupPostion, childPostion);
                break;
            default:
                break;
        }
    }

    /**
     * expandListView 长按事件
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
//            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
//            int childPosition = ExpandableListView.getPackedPositionChild(id);
//            LoadOrdersBean lb = list.get(groupPosition).getOrders().get(childPosition);
//            executeNoPerformed(lb);
            return true;
        }
        return false;
    }

    /**
     * expandListView 子项 点击事件
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String status = list.get(groupPosition).getOrders().get(childPosition).getPerformStatus();
        int visibility = v.findViewById(R.id.bottom_relation).getVisibility();
        /**
         * 如果状态是0，flow开始，通过判断templateId判断是哪个流程，然后跳转
         */
        if (status != null && status.equals("0")) {
            flowStart(groupPosition, childPosition);
        }
        /**
         * 如果状态是1，flow继续，通过判断templateId判断是哪个流程，然后跳转
         */
        if (status != null && !status.equals("0") && visibility == 8) {
            flowContinue(v, groupPosition, childPosition);
        }
        if (status != null && !status.equals("0") && visibility == 0) {
            v.findViewById(R.id.bottom_relation).setVisibility(View.GONE);
        }
        return false;
    }

    /**
     * 如果状态是1，flow继续，通过判断templateId判断是哪个流程，然后跳转
     */
    private void flowContinue(View v, int groupPosition, int childPosition) {
        LoadOrdersBean lb = list.get(groupPosition).getOrders().get(childPosition);
        if (lb.getTemplateId() == null) {
            v.findViewById(R.id.bottom_relation).setVisibility(View.VISIBLE);
        } else if (lb.getTemplateId().equals("AA-1")) {
            if (lb.getPerformStatus().equals("1")) {
                /**
                 * 赵洋洋做的流程界面
                 */
//                Intent medInent = new Intent(DoctorOrdersAct.this, IntravPatrolActivity.class);
//                medInent.putExtra("loadOrdersBean", lb);
//                medInent.putExtra("patient", patient);
//                startActivityForResult(medInent, GlobalConstant.REQUEST_CODE);
                /**
                 * 张教授的流程界面
                 */
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                OrderItemBean orderItemBean = BeanTrans.loadOrdersToOrderItem(lb);
                bundle.putSerializable(GlobalConstant.KEY_ORDERITEMBEAN, orderItemBean);
                bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
                intent.putExtras(bundle);
                intent.setClass(DoctorOrdersAct.this, FlowAct.class);
                startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
            }
        } else if (lb.getTemplateId().equals("AA-5")) {
            if (lb.getPerformStatus().equals("1")) {
                List<BloodProductBean2> bags = lb.getSubOrders().get(0).getBags();
                Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                bundle.putSerializable(GlobalConstant.KEY_BLOODPRODUCTBEAN2, bags.get(0));
                bundle.putSerializable(GlobalConstant.KEY_FLAG_SCAN_PATROL, GlobalConstant.VALUE_FLAG_SCAN_PATROL);
                transfusionIntent.putExtras(bundle);
                startActivityForResult(transfusionIntent, GlobalConstant.REQUEST_CODE);
            }
        } else if (lb.getTemplateId().equals("AA-3")) {
            if (lb.getPerformStatus().equals("1")) {
                /**
                 * 赵洋洋做的流程界面
                 */
//                Intent transfusionIntent = new Intent(DoctorOrdersAct.this, InsulinPatrolActivity.class);
//                transfusionIntent.putExtra("patient", patient);
//                transfusionIntent.putExtra("loadOrdersBean", lb);
//                startActivityForResult(transfusionIntent, GlobalConstant.REQUEST_CODE);
                /**
                 * 张教授流程界面
                 */
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
                OrderItemBean orderItemBean = BeanTrans.loadOrdersToOrderItem(lb);
                bundle.putSerializable("orderItemBean", orderItemBean);
                intent.putExtras(bundle);
                intent.setClass(DoctorOrdersAct.this, InsulinFlowAct.class);
                startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
            }
        }
    }

    /**
     * 如果状态是0，flow开始，通过判断templateId判断是哪个流程，然后跳转
     */
    private void flowStart(int groupPosition, int childPosition) {
        LoadOrdersBean lb = list.get(groupPosition).getOrders().get(childPosition);
        if (lb.getTemplateId() == null) {
            LoadOrdersInfo loadOrdersInfo = lb.getSubOrders().get(0);
            if (loadOrdersInfo.getSkinTest() != null && loadOrdersInfo.getSkinTest().equals("1")) {
                if (loadOrdersInfo.getSkinTestResult() == null || loadOrdersInfo.getSkinTestResult().equals
                        ("")) {
                    showSkinResult(groupPosition, childPosition, lb);
                } else {
                    ExecutionOrder(lb);
                }
            } else {
                ExecutionOrder(lb);
            }
        } else if (lb.getTemplateId().equals("AA-1")) {
            if (GlobalConstant.isSamplePerform) {
                ExecutionOrder(lb);
            } else {
                if (lb.getPerformStatus().equals("0")) {
                    /**
                     * 跳转到洋洋新作的页面
                     *
                     */
//                    Intent medInent = new Intent(DoctorOrdersAct.this, IntravenousActivity.class);
//                    medInent.putExtra("loadOrdersBean", lb);
//                    medInent.putExtra("patient", patient);
//                    startActivityForResult(medInent, GlobalConstant.REQUEST_CODE);
                    /**
                     * 跳转张教授设计的页面中
                     */
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                    OrderItemBean orderItemBean = BeanTrans.loadOrdersToOrderItem(lb);
                    bundle.putSerializable(GlobalConstant.KEY_ORDERITEMBEAN, orderItemBean);
                    bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow
                            .MEDICINE_INTRODUCTION);
                    intent.putExtras(bundle);
                    intent.setClass(DoctorOrdersAct.this, FlowAct.class);
                    startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
                }
            }
        } else if (lb.getTemplateId().equals("AA-5")) {
            if (lb.getPerformStatus().equals("0")) {
                List<BloodProductBean2> bags = lb.getSubOrders().get(0).getBags();
                Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                bundle.putSerializable(GlobalConstant.KEY_BLOODPRODUCTBEAN2, bags.get(0));
                bundle.putSerializable(GlobalConstant.KEY_FLAG_SCAN, GlobalConstant.VALUE_FLAG_SCAN);
                transfusionIntent.putExtras(bundle);
                startActivityForResult(transfusionIntent, GlobalConstant.REQUEST_CODE);
            }
        } else if (lb.getTemplateId().equals("AA-4")) {
            if (GlobalConstant.isSamplePerform) {
                ExecutionOrder(lb);
            } else {
                if (lb.getPerformStatus().equals("0")) {
                    /**
                     * 跳转赵洋洋之前制作的流程
                     */
//                    Intent medInent = new Intent(DoctorOrdersAct.this, BloodTestActivity.class);
//                    medInent.putExtra("loadOrdersBean", lb);
//                    medInent.putExtra("patient", patient);
//                    startActivityForResult(medInent, GlobalConstant.REQUEST_CODE);
                    /**
                     * 张教授版
                     */
                    Intent intent = new Intent();
                    intent.putExtra(GlobalConstant.KEY_PATIENT, patient);
                    intent.putExtra(GlobalConstant.KEY_PLANORDERNO, lb.getPlanOrderNo());
                    intent.setClass(DoctorOrdersAct.this, BloodSugarActivity.class);
                    startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
                }
            }
        } else if (lb.getTemplateId().equals("AA-3")) {
            if (GlobalConstant.isSamplePerform) {
                ExecutionOrder(lb);
            } else {
                if (lb.getPerformStatus().equals("0")) {
                    /**
                     * 赵洋洋之前做的版本
                     */
//                    Intent medInent = new Intent(DoctorOrdersAct.this, InsulinActivity.class);
//                    medInent.putExtra("loadOrdersBean", lb);
//                    medInent.putExtra("patient", patient);
//                    startActivityForResult(medInent, GlobalConstant.REQUEST_CODE);
                    /**
                     * 张教授版
                     */
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                    bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.INJECTIONSITE);
                    OrderItemBean orderItemBean = BeanTrans.loadOrdersToOrderItem(lb);
                    bundle.putSerializable("orderItemBean", orderItemBean);
                    intent.putExtras(bundle);
                    intent.setClass(DoctorOrdersAct.this, InsulinFlowAct.class);
                    startActivityForResult(intent, GlobalConstant.REQUEST_CODE);
                }
            }
        }
    }

    /**
     * expandListView group 点击事件
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    /**
     * expandListView 刷新
     */
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        DoLoadOrder();
        syncDeptPatientTable(1);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_today:
                long oneday = 1000 * 60 * 60 * 24;
                LayoutInflater inflater = (LayoutInflater) DoctorOrdersAct.this.getSystemService
                        (LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_order_base_search, null);
                RadioButton btn_time_yest = (RadioButton) view.findViewById(R.id.btn_time_yest);
                btn_time_yest.setOnClickListener(new myClickLisenter());
                RadioButton btn_time_today = (RadioButton) view.findViewById(R.id.btn_time_today);
                btn_time_today.setOnClickListener(new myClickLisenter());
                RadioButton btn_time_torr = (RadioButton) view.findViewById(R.id.btn_time_torr);
                btn_time_torr.setOnClickListener(new myClickLisenter());
                RadioButton btn_repeat_enduring = (RadioButton) view.findViewById(R.id.btn_repeat_enduring);
                btn_repeat_enduring.setOnClickListener(new myClickLisenter());
                RadioButton btn_repeat_all = (RadioButton) view.findViewById(R.id.btn_repeat_all);
                btn_repeat_all.setOnClickListener(new myClickLisenter());
                RadioButton btn_repeat_temporary = (RadioButton) view.findViewById(R.id.btn_repeat_temporary);
                btn_repeat_temporary.setOnClickListener(new myClickLisenter());
                RadioButton btn_status_perform_yes = (RadioButton) view.findViewById(R.id.btn_status_perform_yes);
                btn_status_perform_yes.setOnClickListener(new myClickLisenter());
                RadioButton btn_status_performing = (RadioButton) view.findViewById(R.id.btn_status_performing);
                btn_status_performing.setOnClickListener(new myClickLisenter());
                RadioButton btn_status_perform_all = (RadioButton) view.findViewById(R.id.btn_status_perform_all);
                btn_status_perform_all.setOnClickListener(new myClickLisenter());
                RadioButton btn_status_perform_no = (RadioButton) view.findViewById(R.id.btn_status_perform_no);
                btn_status_perform_no.setOnClickListener(new myClickLisenter());
                view.findViewById(R.id.btn_sure).setOnClickListener(new myClickLisenter());
                if (orderType.equals("null")) {
                    btn_repeat_all.setChecked(true);
                } else if (orderType.equals("1")) {
                    btn_repeat_enduring.setChecked(true);
                } else if (orderType.equals("0")) {
                    btn_repeat_temporary.setChecked(true);
                }
                if (status.equals("null")) {
                    btn_status_perform_all.setChecked(true);
                } else if (status.equals("0")) {
                    btn_status_perform_no.setChecked(true);
                } else if (status.equals("1")) {
                    btn_status_performing.setChecked(true);
                } else if (status.equals("9")) {
                    btn_status_perform_yes.setChecked(true);
                }
                if (planTime.equals(DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System.currentTimeMillis()))) {
                    btn_time_today.setChecked(true);
                } else if (planTime.equals(DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System
                        .currentTimeMillis() - oneday))) {
                    btn_time_yest.setChecked(true);
                } else if (planTime.equals(DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System
                        .currentTimeMillis() - oneday))) {
                    btn_time_torr.setChecked(true);
                }
                Builder myBuilder = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT);
                orderBaseDialog = myBuilder.create();
                orderBaseDialog.setView(view);
                orderBaseDialog.show();
                orderBaseDialog.getWindow().setLayout(720, 950);
                break;
            case R.id.btn_template:
                txt = new String[]{getString(R.string.order_all), getString(R.string.intravenous_name),
                        getString(R.string.insulin_name), getString(R.string.blood_sugar_name), getString(R
                        .string.transfusion_blood)};
                new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT).setItems(txt, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btn_template.setText(txt[which]);
                                if (which == 0) {
                                    templateId = "null";
                                } else if (which == 1) {
                                    templateId = "AA-1";
                                } else if (which == 2) {
                                    templateId = "AA-3";
                                } else if (which == 3) {
                                    templateId = "AA-4";
                                } else if (which == 4) {
                                    templateId = "AA-5";
                                }
                                DoLoadOrder();
                            }
                        }).create().show();
                break;
            case R.id.btn_order_class:
                int len = orderClassList.size();
                if (len > 0) {
                    orderstype = new String[len + 1];
                    itemCode = new String[len + 1];
                } else {
                    orderstype = new String[1];
                    itemCode = new String[1];
                }
                orderstype[0] = "全部";
                itemCode[0] = "null";
                for (int i = 1; i < orderClassList.size() + 1; i++) {
                    orderstype[i] = orderClassList.get(i - 1).getItemName();
                    itemCode[i] = orderClassList.get(i - 1).getItemCode();
                }
                AlertDialog alertDialog;
                Builder builder = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT)
                        .setItems(orderstype, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btn_order_class.setText(orderstype[which]);
                                if (which == 0) {
                                    orderClass = "null";
                                } else {
                                    orderClass = itemCode[which];
                                }
                                DoLoadOrder();
                            }
                        }).setNegativeButton(getString(R.string.cancel), null);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(680, 650);
                break;
            default:
                break;
        }
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_doctororder);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 红外扫描获取的值
     */
    public void DoCameraResult(String result) {
        CCLog.i(TAG, TAG + "扫描的结果:" + result);
        if (isRelatedBarCode(result)) {
            getScanResult(result);
        } else {
            /**
             * 现在是patCode
             */
            scanPatId(result);
        }
    }

    private boolean isRelatedBarCode(String result) {
        boolean flag = false;
        if (result.contains("||")) {
            String s = result.split("\\|\\|")[0];
            String reg = "^[0-9]{5}$";
            if (Pattern.compile(reg).matcher(s).find()) {
                flag = true;
            }
        }
        if (result.contains("KF") || result.contains("kf")) {
            flag = true;
        }
        return flag;
    }

    /**
     * 长度小于等于5，扫描的结果是病人id
     */
    private void scanPatId(String result) {
        boolean isGetResult = false;
        String patientName = null;
        String bedLabel = "";
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatCode().equals(result)) {
                isGetResult = true;
                patientName = patients.get(i).getName();
                patId = patients.get(i).getPatId();
                if (patients.get(i).getBedLabel() == null && !patients.get(i).getBedLabel().equalsIgnoreCase
                        ("null")) {
                    bedLabel = "未分配";
                } else {
                    bedLabel = patients.get(i).getBedLabel() + getString(R.string.bed);
                }

                break;
            }
        }
        if (isGetResult) {
            tv_parent.setText(patientName);
            tv_bed_label.setText(bedLabel);
            DoLoadOrder();
        } else {
            Toast.makeText(getApplication(), getString(R.string.do_not_find_patient_message) + result, Toast
                    .LENGTH_SHORT).show();
        }
    }

    public void showSkinResult(final int groupPostion, final int childPostion, final LoadOrdersBean lb) {
        String[] strs = new String[]{"阴性", "阳性"};
        AlertDialog alert = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("请录入皮试结果")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            result = "阴性";
                            insertSkinResult(result, groupPostion, childPostion);
                        } else if (i == 1) {
                            result = "阳性";
                            insertSkinResult(result, groupPostion, childPostion);
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel), null).create();
        alert.show();
        alert.getWindow().setLayout(680, 650);
    }

    public void showEvaluationDialog(final int groupPostion, final int childPostion) {
        final AlertDialog secondDialog;
        orderAppraislFlag = false;
        LayoutInflater inflater = (LayoutInflater) DoctorOrdersAct.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_pio_alertdilog_edittext, null);
        createEditText = (EditText) view.findViewById(R.id.createEv);
        appraisal_ll = (ListView) view.findViewById(R.id.appraisal_ll);
        checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
        order_ll = (LinearLayout) view.findViewById(R.id.order_ll);
        order_btn_dialog = (Button) view.findViewById(R.id.order_btn_dialog);
        Builder myBuilder = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT);
        secondDialog = myBuilder.create();
        secondDialog.setTitle(getString(R.string.please_input_evaluation));
        secondDialog.setView(view);
        secondDialog.show();
        secondDialog.getWindow().setLayout(650, 800);
        order_btn_dialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createText = createEditText.getText().toString();
                if (orderAppraislFlag && createText.equals("")) {
                    Toast.makeText(DoctorOrdersAct.this, getString(R.string.please_input_message_first), Toast
                            .LENGTH_SHORT).show();
                } else {
                    String status = list.get(groupPostion).getOrders().get(childPostion).getPerformStatus();
                    if (status != null) {
                        updateAppriasl(createText, groupPostion, childPostion);
                    }
                    secondDialog.dismiss();
                }
            }
        });
        checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 手动选择 true
                    createEditText.setVisibility(view.VISIBLE);
                    appraisal_ll.setVisibility(view.GONE);
                    order_ll.setVisibility(view.VISIBLE);
                } else {
                    // 列表 false
                    createEditText.setVisibility(view.GONE);
                    order_ll.setVisibility(view.GONE);
                    appraisal_ll.setVisibility(view.VISIBLE);
                }
                orderAppraislFlag = isChecked;

            }
        });
        appraisal_ll.setAdapter(docAppraislAdapter = new DocAppraislAdapter(DoctorOrdersAct.this, larList));
        appraisal_ll.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                createText = larList.get(position).getItemCode();
                String status = list.get(groupPostion).getOrders().get(childPostion).getPerformStatus();
                if (status != null) {
                    updateAppriasl(createText, groupPostion, childPostion);
                }
                secondDialog.dismiss();
            }
        });
    }

    private void ShowSighChooseAlert(final int groupPostion, final int childPostion) {
        String[] strs = new String[exeList.size()];
        for (int i = 0; i < exeList.size(); i++) {
            strs[i] = exeList.get(i).getItemName();
        }
        AlertDialog alert = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择异常情况")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String status = list.get(groupPostion).getOrders().get(childPostion).getPerformStatus();
                        if (status != null) {
                            updateExe(i, groupPostion, childPostion);
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel), null).create();
        alert.show();
        alert.getWindow().setLayout(680, 650);
    }

    private void showDetailAlert(int gp, int cp) {
        final AlertDialog secondDialog;
        LayoutInflater inflater = (LayoutInflater) DoctorOrdersAct.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_pio_alertdilog_textview, null);
        StringBuffer sb = new StringBuffer();
        sb.append("病人：");
        sb.append(patient.getName() + "\n");
        sb.append("操作护士：");
        sb.append(list.get(gp).getOrders().get(cp).getSubOrders().get(0).getNurseInOperate() + "\n");
        sb.append("计划开始时间：");
        sb.append(list.get(gp).getStartTime() + "\n");
        if (list.get(gp).getOrders().get(cp).getSubOrders().get(0).getEventStartTime() != null) {
            sb.append("实际开始时间：");
            sb.append(list.get(gp).getOrders().get(cp).getSubOrders().get(0).getEventStartTime() + "\n");
        }
        if (list.get(gp).getOrders().get(cp).getSubOrders().get(0).getEventEndTime() != null) {
            sb.append("实际结束时间：");
            sb.append(list.get(gp).getOrders().get(cp).getSubOrders().get(0).getEventEndTime() + "\n");
        }
        sb.append("医嘱详情:" + "\n");
        List<LoadOrdersInfo> l = list.get(gp).getOrders().get(cp)
                .getSubOrders();
        for (int i = 0; i < l.size(); i++) {
            sb.append((i + 1) + ".");
            sb.append(l.get(i).getOrderText() + "\t");
            if (l.get(i).getDosage() != null) {
                sb.append(l.get(i).getDosage() + "\t");
            }
            if (l.get(i).getSpeed() != null) {
                sb.append(l.get(i).getSpeed() + "\t");
            }
            if (l.get(i).getFrequency() != null) {
                sb.append(l.get(i).getFrequency() + "\t");
            }
            sb.append("\n");

        }
        if (list.get(gp).getOrders().get(cp).getSubOrders().get(0)
                .getNurseEffect() != null) {
            sb.append("评价信息：");
            sb.append(list.get(gp).getOrders().get(cp).getSubOrders().get(0).getNurseEffect() + "\n");
        }
        if (list.get(gp).getOrders().get(cp).getSubOrders().get(0).getVarianceCauseDesc() != null) {
            sb.append("异常信息：");
            sb.append(list.get(gp).getOrders().get(cp).getSubOrders().get(0).getVarianceCauseDesc() + "\n");
        }
        createtv = (TextView) view.findViewById(R.id.createtv);
        createtv.setText(sb.toString());
        closed = (ImageView) view.findViewById(R.id.closed);
        Builder myBuilder = new Builder(DoctorOrdersAct.this, AlertDialog.THEME_HOLO_LIGHT);
        secondDialog = myBuilder.create();
        secondDialog.setView(view);
        secondDialog.show();
        secondDialog.getWindow().setLayout(650, 650);
        closed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                secondDialog.dismiss();
            }
        });
    }

    /**
     * 时间提示框
     *
     * @param groupPostion
     * @param childPostion
     */
    public void DateAlertDialog(final int groupPostion, final int childPostion) {

        dateView = View.inflate(DoctorOrdersAct.this, R.layout.datetimepickdialog, null);

        datepick = (DatePicker) dateView.findViewById(R.id.new_act_date_picker);
        timepick = (TimePicker) dateView .findViewById(R.id.new_act_time_picker);
        /**
         * initDate
         */
        int year = 0;
        int month = 0;
        int day = 0;
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datepick.init(year, month, day, null);
        /**
         * initTime
         */
        int hour = 0;
        int min = 0;

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(calendar.MINUTE);

        timepick.setIs24HourView(true);
        timepick.setCurrentHour(hour);
        timepick.setCurrentMinute(min);

        AlertDialog mDialog;
        Builder builder = new Builder(DoctorOrdersAct.this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setPositiveButton(getString(R.string.make_sure), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrive_year = datepick.getYear();
                arrive_month = datepick.getMonth() + 1;
                arrive_day = datepick.getDayOfMonth();

                arrive_hour = timepick.getCurrentHour();
                arrive_min = timepick.getCurrentMinute();

                myLongDate = DateTool.appendDateOrder(arrive_year, arrive_month, arrive_day, arrive_hour,
                        arrive_min);

                String status = list.get(groupPostion).getOrders().get(childPostion).getPerformStatus();
                if (status != null) {
                    executeEndToTime(myLongDate, groupPostion, childPostion);
                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        mDialog = builder.create();
        mDialog.setView(dateView);
        mDialog.setTitle("请选择结束时间");

        mDialog.show();
    }

    private void judgeIsSubmit(final int groupPostion, final int childPostion) {
        LoadOrdersBean loadOrdersBean = list.get(groupPostion).getOrders().get(childPostion);
        if (loadOrdersBean.getTemplateId() == null) {
            new Builder(DoctorOrdersAct.this,
                    AlertDialog.THEME_HOLO_LIGHT).setMessage("是否需要变更执行状态到未执行")
                    .setPositiveButton(getString(R.string.make_sure), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoadOrdersBean lb = list.get(groupPostion).getOrders().get(childPostion);
                            String status = lb.getPerformStatus();
                            if (status != null && !status.equals("0")) {
                                executeToOrg(lb);
                            }
                        }
                    }).setNegativeButton(getString(R.string.cancel), null).show();
        } else {
            new Builder(DoctorOrdersAct.this,
                    AlertDialog.THEME_HOLO_LIGHT).setMessage("流程医嘱不能变更到未执行状态")
                    .setPositiveButton(getString(R.string.make_sure), null).show();
        }
    }

    private void executeNoPerformed(LoadOrdersBean lb) {
        progressDialog.show();
        ExecuteNoPerformedBean exe = new ExecuteNoPerformedBean();
        exe.setPatId(patId);
        exe.setPlanOrderNo(lb.getPlanOrderNo());
        CCLog.i(TAG, UrlConstant.executeToNoPerformed() + exe.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.executeToNoPerformed() + exe.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ExecuteNoPerformedBean>() {
                        }.getType();
                        ExecuteNoPerformedBean temp = gson.fromJson(result, type);
                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, R.string.excutesuccesd, Toast.LENGTH_SHORT)
                                    .show();
                            DoLoadOrder();
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }

                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(DoctorOrdersAct.this, ErrorAct.class);
        startActivity(intent);
    }

    private void executeToOrg(LoadOrdersBean lb) {
        progressDialog.show();
        ExecuteToOrgRecordBean eo = new ExecuteToOrgRecordBean();
        eo.setPerformStatus("0");
        eo.setPlanOrderNo(lb.getPlanOrderNo());
        CCLog.i(TAG, "执行状态到没执行>" + UrlConstant.ExecuteToOrg() + eo.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.ExecuteToOrg() + eo.toString(), new
                RequestCallBack<String>() {


                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行成功");
                        Gson gson = new Gson();
                        Type type = new TypeToken<ExecuteToOrgRecordBean>() {
                        }.getType();
                        ExecuteToOrgRecordBean temp = gson.fromJson(result, type);
                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.excutesuccesd), Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }
                        startTimeText = "";
                        createText = "";
                        endTimeText = "";
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");

                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }

                    }
                });
    }

    /**
     * 执行请求
     */
    private void ExecutionOrder(LoadOrdersBean lb) {
        progressDialog.show();
        ExecutionOrderBean executionOrderBean = new ExecutionOrderBean();
        executionOrderBean.setPatId(patId);
        executionOrderBean.setWardCode(UserInfo.getWardCode());
        executionOrderBean.setNurseInOperate(UserInfo.getUserName());
        executionOrderBean.setPlanOrderNo(lb.getPlanOrderNo());
        CCLog.i(TAG, "执行请求>" + UrlConstant.ExecutionOrders() + executionOrderBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.ExecutionOrders() + executionOrderBean.toString(),
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行成功");
                        Gson gson = new Gson();
                        Type type = new TypeToken<ExecutionOrderBean>() {
                        }.getType();
                        ExecutionOrderBean temp = gson.fromJson(result, type);
                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.excutesuccesd), Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                        if (startTimeText.equals("")) {
                            startTimeText = "医嘱开始执行时间：" + DateTool.getCurrDateTime();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 插皮试结果
     */
    private void insertSkinResult(String text, int gp, int cp) {
        progressDialog.show();
        final LoadOrdersBean lb = list.get(gp).getOrders().get(cp);
        SkinResult bean = new SkinResult();
        bean.setOrderId(lb.getSubOrders().get(0).getOrderId());
        bean.setPatId(patId);
        if (text.equals("阴性")) {
            bean.setSkinTestResult(0);
        } else if (text.equals("阳性")) {
            bean.setSkinTestResult(1);
        }
        String url = UrlConstant.insertSkinResult() + bean.toString();
        CCLog.i(TAG, "插入皮试>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<SkinResult>() {
                }.getType();
                SkinResult temp = gson.fromJson(result, type);

                if (temp.getResult().equals("success")) {
                    Toast.makeText(DoctorOrdersAct.this, R.string.recordsuccess, Toast.LENGTH_SHORT).show();
                    ExecutionOrder(lb);
                } else {
                    Toast.makeText(DoctorOrdersAct.this, R.string.recordfail, Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }

            }
        });
    }

    /**
     * 插入评论
     */
    private void updateAppriasl(String text, int gp, int cp) {
        progressDialog.show();
        insertAppraislRecordBean bean = new insertAppraislRecordBean();
        bean.setPatId(patId);
        bean.setPerformStatus("9");
        LoadOrdersBean lb = list.get(gp).getOrders().get(cp);
        bean.setPlanOrderNo(lb.getPlanOrderNo());
        bean.setNurseEffect(text);
        CCLog.i(TAG, "插入评论>" + UrlConstant.insertAppriasl() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.insertAppriasl() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<insertAppraislRecordBean>() {
                        }.getType();
                        insertAppraislRecordBean temp = gson.fromJson(result, type);

                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.excutesuccesd), Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.excutefail), Toast
                                    .LENGTH_SHORT).show();
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }

                    }
                });
    }

    /**
     * 变更结束时间
     */
    private void executeEndToTime(final String time, int gp, int cp) {
        progressDialog.show();
        ExecuteToEndTimeRecordBean etet = new ExecuteToEndTimeRecordBean();
        StringBuffer sb = new StringBuffer();
        etet.setPatId(patId);
        etet.setPerformStatus("9");
        LoadOrdersBean lb = list.get(gp).getOrders().get(cp);
        etet.setPlanOrderNo(lb.getPlanOrderNo());
        etet.setEventEndTime(time);
        CCLog.i(TAG, "变更结束时间>" + UrlConstant.executeToEndTime() + etet.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.executeToEndTime() + etet.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ExecuteToEndTimeRecordBean>() {
                        }.getType();
                        ExecuteToEndTimeRecordBean temp = gson.fromJson(result, type);
                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationissuccessful, Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                            endTimeText = time;
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationfailed, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 扫描后变更结束时间
     */
    private void executeEndToTimeOther(final String time, LoadOrdersBean lb) {
        progressDialog.show();
        ExecuteToEndTimeRecordBean etet = new ExecuteToEndTimeRecordBean();
        StringBuffer sb = new StringBuffer();
        etet.setPatId(patId);
        etet.setPerformStatus("9");
        etet.setPlanOrderNo(lb.getPlanOrderNo());
        etet.setEventEndTime(time);
        CCLog.i(TAG, "扫描后变更结束时间>" + UrlConstant.executeToEndTime() + etet.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.executeToEndTime() + etet.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<ExecuteToEndTimeRecordBean>() {
                        }.getType();
                        ExecuteToEndTimeRecordBean temp = gson.fromJson(result, type);
                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationissuccessful, Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                            endTimeText = time;
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationfailed, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 加载过滤条件 orderClass nursingType
     */
    private void loadOrderClass() {
        progressDialog.show();
        LoadOrderClassBean bean = new LoadOrderClassBean();
        bean.setPatId(patId);
        CCLog.i(TAG, UrlConstant.loadOrderClass() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadOrderClass() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LoadOrderClassBean>>() {
                        }.getType();
                        orderClassList = gson.fromJson(result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 变更异常，插入数据库
     */
    private void updateExe(int num, int groupPostion, int childPostion) {
        progressDialog.show();
        ExeRecordBean exerecord = exeList.get(num);
        insertExeRecordBean ie = new insertExeRecordBean();
        ie.setPatId(patId);
        ie.setPerformStatus("-1");
        ie.setPerformResult(exerecord.getItemName());
        LoadOrdersBean lb = list.get(groupPostion).getOrders().get(childPostion);
        ie.setPlanOrderNo(lb.getPlanOrderNo());
        ie.setVarianceCause(exerecord.getItemCode());
        final String vcd = exerecord.getItemName();
        ie.setVarianceCauseDesc(vcd);
        CCLog.i(TAG, UrlConstant.insetExe() + ie.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.insetExe() + ie.toString(), new
                RequestCallBack<String>() {


                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<insertExeRecordBean>() {
                        }.getType();
                        insertExeRecordBean temp = gson.fromJson(result, type);

                        if (temp.getResult().equals("success")) {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationissuccessful, Toast
                                    .LENGTH_SHORT).show();
                            DoLoadOrder();
                            exeText = vcd;
                        } else {
                            Toast.makeText(DoctorOrdersAct.this, R.string.operationfailed, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 核对planorderno是哪个流程的
     * 1静脉给药
     * 2胰岛素
     * 3输血
     * 4血糖
     * 5normal
     */
    private void getScanResult(final String relatedBarCode) {
        progressDialog.show();
        OrderScanBean bean = new OrderScanBean();
        bean.setPatId(patId);
        bean.setPlanStartTime(planTime);
        bean.setRelatedBarCode(relatedBarCode);
        CCLog.i(TAG, UrlConstant.getOrderScan() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.getOrderScan() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        CCLog.i(TAG, "扫描判断数据获取成功");
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        int flag = 0;
                        int performStatus = 0;
                        Gson gson = new Gson();
                        Type type = new TypeToken<OrderScanBean>() {
                        }.getType();
                        OrderScanBean temp = gson.fromJson(result, type);
                        if (temp.isResult()) {
                            if (StringTool.isNotEmpty(temp.getPerformStatus())) {
                                String getPerformStatus = temp.getPerformStatus();
                                if (getPerformStatus.equals("0")) {
                                    performStatus = 0;
                                } else if (getPerformStatus.equals("1")) {
                                    performStatus = 1;
                                } else if (getPerformStatus.equals("9")) {
                                    performStatus = 9;
                                } else if (getPerformStatus.equals("-1")) {
                                    performStatus = -1;
                                }
                            }
                            if ("AA-1".equals(temp.getTemplateId())) {
                                flag = 1;
                            } else if ("AA-3".equals(temp.getTemplateId())) {
                                flag = 2;
                            } else if ("AA-4".equals(temp.getTemplateId())) {
                                flag = 4;
                            } else if ("AA-5".equals(temp.getTemplateId())) {
                                flag = 3;
                            } else {
                                flag = 5;
                            }
                            String planOrderNo = temp.getPlanOrderNo();
                            scanPlanOrderNo(planOrderNo, performStatus, flag);
                        } else {
                            ShowToast(temp.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "扫描判断数据获取失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 1静脉给药
     * 2胰岛素
     * 3输血
     * 4血糖
     * 5normal
     */
    private void scanPlanOrderNo(String result, int queryPerformStatus, int flag) {
        boolean haveOrder = false;
        /** 普通流程 */
        if (flag == 5) {
            for (int i = 0; i < list.size(); i++) {
                List<LoadOrdersBean> orders = list.get(i).getOrders();
                for (int j = 0; j < orders.size(); j++) {
                    LoadOrdersBean subOrder = orders.get(j);
                    String planOrderNo = subOrder.getPlanOrderNo();
                    String performStatus = subOrder.getPerformStatus();
                    String eventEndTime = subOrder.getSubOrders().get(0).getEventEndTime();
                    if (result.equals(planOrderNo)) {
                        /** performStatus为0 医嘱未执行，执行医嘱，跳转到对应item*/
                        if (queryPerformStatus == 0) {
                            ExecutionOrder(subOrder);
                            elv_doctororder.setSelectedChild(i, j, true);
                        }
                        /** performStatus为9 ，医嘱未执行，但是没有*/
                        if (queryPerformStatus == 9 && endTimeText.equals("")) {
                            String currDateTime = DateTool.getCurrDateTime();
                            executeEndToTimeOther(currDateTime, subOrder);
                            Toast.makeText(DoctorOrdersAct.this, "已录上当前时间为结束时间", Toast.LENGTH_SHORT).show();
                            elv_doctororder.setSelectedChild(i, j, true);
                        }
                        if (queryPerformStatus == 9 && !endTimeText.equals("")) {
                            Toast.makeText(DoctorOrdersAct.this, "这条医嘱已经录入结束时间，不需再扫描", Toast.LENGTH_SHORT).show();
                            elv_doctororder.setSelectedChild(i, j, true);
                        }
                        haveOrder = true;
                        break;
                    } else {
                        haveOrder = false;
                    }
                }
            }
            if (!haveOrder) {
                Toast.makeText(DoctorOrdersAct.this, "此医嘱非当前时间可用，请核对", Toast.LENGTH_SHORT).show();
            }
        } else if (flag != 5) {
            ShowToast("流程医嘱请到对应界面执行");
        }
        /** 静脉给药 */
//        } else if (flag == 1) {
//            if (queryPerformStatus == 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent medInent = new Intent(DoctorOrdersAct.this, IntravenousActivity.class);
//                            medInent.putExtra("loadOrdersBean", lb);
//                            medInent.putExtra("patient", patient);
//                            startActivity(medInent);
//                            finish();
//                        }
//                    }
//                }
//            } else if (queryPerformStatus == 1) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent medInent = new Intent(DoctorOrdersAct.this, IntravPatrolActivity.class);
//                            medInent.putExtra("loadOrdersBean", lb);
//                            medInent.putExtra("patient", patient);
//                            startActivity(medInent);
//                            finish();
//                        }
//                    }
//                }
//            }
//            /** 胰岛素 */
//        } else if (flag == 2) {
//            if (queryPerformStatus == 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent medInent = new Intent(DoctorOrdersAct.this, InsulinActivity.class);
//                            medInent.putExtra("loadOrdersBean", lb);
//                            medInent.putExtra("patient", patient);
//                            startActivity(medInent);
//                            finish();
//                        }
//                    }
//                }
//            } else if (queryPerformStatus == 1) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent transfusionIntent = new Intent(DoctorOrdersAct.this, InsulinPatrolActivity
//                                    .class);
//                            transfusionIntent.putExtra("patient", patient);
//                            transfusionIntent.putExtra("loadOrdersBean", lb);
//                            startActivity(transfusionIntent);
//                            finish();
//                        }
//                    }
//                }
//            }
//            /** 输血 */
//        } else if (flag == 3) {
//            if (queryPerformStatus == 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            List<BloodProductBean3> bags = lb.getSubOrders().get(0).getBags();
//                            Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusionSelectActivity
//                                    .class);
//                            transfusionIntent.putExtra("bags", (Serializable) bags);
//                            transfusionIntent.putExtra("patient", patient);
//                            transfusionIntent.putExtra("loadOrdersBean", lb);
//                            startActivity(transfusionIntent);
//                            finish();
//                        }
//                    }
//                }
//            } else if (queryPerformStatus == 1) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            List<BloodProductBean3> bags = lb.getSubOrders().get(0).getBags();
//                            Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusionSelectActivity
//                                    .class);
//                            transfusionIntent.putExtra("bags", (Serializable) bags);
//                            transfusionIntent.putExtra("patient", patient);
//                            transfusionIntent.putExtra("loadOrdersBean", lb);
//                            startActivity(transfusionIntent);
//                            finish();
//                        }
//                    }
//                }
//            } else if (queryPerformStatus == -1) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            List<BloodProductBean3> bags = lb.getSubOrders().get(0).getBags();
//                            Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusionSelectActivity
//                                    .class);
//                            transfusionIntent.putExtra("bags", (Serializable) bags);
//                            transfusionIntent.putExtra("patient", patient);
//                            transfusionIntent.putExtra("loadOrdersBean", lb);
//                            startActivity(transfusionIntent);
//                            finish();
//                        }
//                    }
//                }
//            } else if (queryPerformStatus == 9) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent transfusionIntent = new Intent(DoctorOrdersAct.this, TransfusioActivity
// .class);
//                            transfusionIntent.putExtra("patient", patient);
//                            transfusionIntent.putExtra("loadOrdersBean", lb);
//                            startActivity(transfusionIntent);
//                            finish();
//                        }
//                    }
//                }
//            }
//            /** 血糖 */
//        } else if (flag == 4) {
//            if (queryPerformStatus == 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<LoadOrdersBean> orders = list.get(i).getOrders();
//                    for (int j = 0; j < orders.size(); j++) {
//                        LoadOrdersBean subOrder = orders.get(j);
//                        String planOrderNo = subOrder.getPlanOrderNo();
//                        if (result.equals(planOrderNo)) {
//                            LoadOrdersBean lb = list.get(i).getOrders().get(j);
//                            Intent medInent = new Intent(DoctorOrdersAct.this, BloodTestActivity.class);
//                            medInent.putExtra("loadOrdersBean", lb);
//                            medInent.putExtra("patient", patient);
//                            startActivity(medInent);
//                            finish();
//                        }
//                    }
//                }
//            }
//        }

    }

    /**
     * 加载医嘱信息请求
     */

    public void DoLoadOrder() {
        progressDialog.show();
        loadOrdersBean = new LoadOrdersBean();
        loadOrdersBean.setPatId(patId);
        loadOrdersBean.setPlanTime(planTime);
        loadOrdersBean.setOrderType(orderType);
        loadOrdersBean.setStatus(status);
        loadOrdersBean.setTemplateId(templateId);
        loadOrdersBean.setOrderClass(orderClass);
        CCLog.i(TAG, "DoLoadOrder>" + UrlConstant.LoadOrders() + loadOrdersBean.toString());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadOrders() + loadOrdersBean.toString(), new
                RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        pullToRefreshExpandListView.onRefreshComplete();
                        CCLog.i(TAG, "医嘱内页数据获取成功");
                        try {
                            JSONObject mJsonObject = new JSONObject(result);
                            boolean responseState = mJsonObject.getBoolean("result");
                            if (responseState) {
                                JSONArray mJsonArray = mJsonObject.getJSONArray("order");
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<LoadDocOrdersBean>>() {
                                }.getType();
                                List<LoadDocOrdersBean> temp = gson.fromJson(mJsonArray.toString(), type);
                                list.clear();
                                if (temp != null && temp.size() > 0) {
                                    list.addAll(temp);
                                }
                                adapter.notifyDataSetChanged();
//                                /**
//                                 * 开始展开
//                                 */
//                                for (int i = 0; i < adapter.getGroupCount(); i++) {
//                                    elv_doctororder.expandGroup(i);
//                                }
                                elv_doctororder.expandGroup(0);

                            }
                        } catch (JSONException e) {
                            if (LinstenNetState.isLinkState(getApplicationContext())) {
                                Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            } else {
                                toErrorAct();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        pullToRefreshExpandListView.onRefreshComplete();
                        CCLog.i(TAG, "医嘱内页数据获取失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 加载异常字典
     */
    private void loadExe() {
        progressDialog.show();
        ExeRecordBean exe = new ExeRecordBean();
        CCLog.i(TAG, "加载异常字典>" + UrlConstant.loadExe() + exe.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadExe() + exe.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ExeRecordBean>>() {
                        }.getType();
                        exeList = gson.fromJson(result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 加载评论字典
     */
    private void loadAppraisl() {
        progressDialog.show();
        LoadAppraislRecordBean lar = new LoadAppraislRecordBean();
        CCLog.i(TAG, "加载评论字典>" + UrlConstant.LoadAppriaslRecord() + lar.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadAppriaslRecord() + lar.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LoadAppraislRecordBean>>() {
                        }.getType();
                        larList = gson.fromJson(result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }


    /**
     * 同步部门病人
     */
    public void syncDeptPatientTable(final int flag) {
        String str = GroupInfoSave.getInstance(DoctorOrdersAct.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString
                (), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CCLog.i(TAG, "医嘱查询数据获取成功");
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
                patients.clear();
                patients.addAll(UserInfo.getDeptPatient());
                adapter.notifyDataSetChanged();
                if (flag == 0) {
                    patient = patients.get(0);
                    patId = patient.getPatId();
                    tv_head.setText(R.string.order_execute);
                    tv_parent.setText(patient.getName());
                    if (patient.getBedLabel() == null || patient.getBedLabel().equalsIgnoreCase("null")) {
                        tv_bed_label.setText("未分配");
                    } else {
                        tv_bed_label.setText(patient.getBedLabel() + getString(R.string.bed));
                    }
                    DoLoadOrder();
                    loadOrderClass();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "医嘱查询数据获取异常");
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(DoctorOrdersAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }



    class myClickLisenter implements OnClickListener {
        @Override
        public void onClick(View v) {
            long oneday = 1000 * 60 * 60 * 24;
            switch (v.getId()) {
                case R.id.btn_time_today:
                    planTime = DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System.currentTimeMillis());
                    break;
                case R.id.btn_time_yest:
                    planTime = DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System.currentTimeMillis() - oneday);
                    break;
                case R.id.btn_time_torr:
                    planTime = DateTool.parseDate(DateTool.YYYY_MM_DD_STYPE, System.currentTimeMillis() + oneday);
                    break;
                case R.id.btn_repeat_all:
                    orderType = "null";
                    break;
                case R.id.btn_repeat_enduring:
                    orderType = "1";
                    break;
                case R.id.btn_repeat_temporary:
                    orderType = "0";
                    break;
                case R.id.btn_status_perform_all:
                    status = "null";
                    break;
                case R.id.btn_status_perform_no:
                    status = "0";
                    break;
                case R.id.btn_status_perform_yes:
                    status = "9";
                    break;
                case R.id.btn_status_performing:
                    status = "1";
                    break;
                case R.id.btn_sure:
                    DoLoadOrder();
                    orderBaseDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

    }
}
