package com.gentlehealthcare.mobilecare.activity.patient.trans;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.bean.LookBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.RecInspectResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SpeedDateBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TPRRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.TprSpeedHistoryCommonBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
import com.gentlehealthcare.mobilecare.view.AlertDialogCaution;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.AlertDialogOther;
import com.gentlehealthcare.mobilecare.view.AlertDialogThreeBtnNoicon;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.MyCounter;
import com.gentlehealthcare.mobilecare.view.adapter.GridViewLookAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.SpeedHistoryListAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.TprHistoryListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Zyy
 * @date 2015-9-16下午3:39:05
 * @category TPR 滴速 观察
 */
@SuppressLint("ValidFragment")
public class TPRSpeedObserveFra extends BaseFragment implements AdapterView.OnItemClickListener,
        OnClickListener {
    private static final String TAG = "DoctorOrderListFra";
    private SyncPatientBean patient = null;
    private String planOrderNo = new String();
    private List<OrderItemBean> orderItemBeans = null;
    private MedicineListAdapter adapter;
    private OrderItemBean orderItemBean;
    private ProgressDialog progressDialog;
    private ProgressDialog pDialog;
    private boolean isrun = false;
    private boolean isShowSpeed = true;
    private boolean isShowLook = true;
    private RadioGroup rg_tab_list;
    private RadioButton rbTpr;
    private RadioButton rbSpeed;
    private RadioButton rbLook;
    private int checkButtonId;
    private String currentPlanNo = null;
    /**
     * TPR
     */
    private RelativeLayout rl_tpr;
    private EditText tiwenEt;
    private EditText xintiaoEt;
    private EditText huxiEt;
    private Button tv_sure_tpr;
    /**
     * tpr history
     */
    private TprHistoryListAdapter tprHistoryListAdapter;
    private List<TprSpeedHistoryCommonBean> tprBeans = new ArrayList<TprSpeedHistoryCommonBean>();

    private TextView tv_no_history;
    private TextView tv_tpr_notice;
    private ListView lv_tpr;
    private boolean tFlag = true;
    private boolean pFlag = true;
    private boolean rFlag = true;
    /**
     * speed history
     */
    private ListView lv_speed;
    private TextView speed_no_history;
    private TextView tv_speed_notice;

    private SpeedHistoryListAdapter speedHistoryListAdapter;
    private List<TprSpeedHistoryCommonBean> speedBeans = new ArrayList<TprSpeedHistoryCommonBean>();

    private boolean speedFlag = true;
    /**
     * 滴速
     */
    private RelativeLayout rl_speed;
    private EditText disuEt;
    private Button tv_sure_speed;
    private boolean isSpeedClick = false;

    /**
     * 观察
     */
    private RelativeLayout rl_look;
    private GridViewLookAdapter lookAdapter;
    private boolean isLookClick = false;
    private LinearLayout ll_tso_normal;
    private LinearLayout ll_tso_un_normal;
    /**
     * 跳转到观察界面是否需要开始输血
     */
    private boolean obverseFlag = false;

    /**
     * 观察中动态按钮
     */
    private GridView gv_look;
    /**
     * 观察中动态按钮内容集合
     */
    public List<LookBean> mLookBeans = new ArrayList<LookBean>();
    public List<LookBean> choosenLookBeans = new ArrayList<LookBean>();
    private TextView tv_un_normal;

    /**
     * 标记输血还是巡视 true:输血 false：巡视
     */
    private boolean trasOrNo = true;
    /**
     * 确认观察按钮
     */
    private Button tv_sure_look;
    /**
     * 0观察模式，1巡视时间模式
     */
    private int sureLookStatus = 0;
    /**
     * 下次巡视时间对话框
     */
    private Dialog currentDialog = null;
    private LinearLayout ll_start_to_end_time_obverse;
    /**
     * 观察提示框
     */
    private AlertDialogTwoBtn mChangDialogTwoBtn = null;
    /**
     * 停止输血提示框
     */
    private AlertDialogCaution mCautionDialog = null;
    /**
     * 录入输血其他数据提示框
     */
    private AlertDialogOther otherDialog = null;
    private String otherDesc;
    /**
     * 显示倒计时
     */
    public TextView tv_time;
    private MyCounter mycounter;
    private long mycounterTime = 900000;
    private boolean isMyCounterStart = true;

    /**
     * 默认显示Look
     */
    private int ShowWhichOne = 1;
    /**
     * 巡视点
     */
    private TextView tv_tras_time2;
    /**
     * 倒计时
     */
    private TextView tv_tras_time3;

    /**
     * HANDLER消息
     */
    private static final int UPDATETIMER = 0X0CC;
    /**
     * 血品信息
     */
    private BloodProductBean2 mBloodProductBean2 = null;
    /**
     * 主页按钮
     */
    private ImageButton imbtn_home;
    /**
     * 返回按钮
     */
    private ImageButton imbtn_back;
    /**
     * 病人姓名
     */
    private TextView tv_tras_name;
    /**
     * 病人床号
     */
    private TextView tv_tras_bed;
    /**
     * 头部输血开始时间
     */
    private TextView tv_gc_start;
    /**
     * 头部输血巡视时间
     */
    private TextView tv_gc_patry;
    /**
     * 呼吸警示
     */
    private TextView tv_war_hx;
    /**
     * 体温警示
     */
    private TextView tv_war_tw;
    /**
     * 脉搏警示
     */
    private TextView tv_war_mb;
    /**
     * 其他问题是否输入值
     */
    private boolean otherDescFlag = true;

    private boolean isBack = false;
    private boolean isFirst = true;

    /**
     * tpr界面是否可以退出
     */
    private AlertDialogTwoBtn canBackDialog;

    /**
     * 判断显示哪些界面
     *
     * @param patient
     * @param mloodProductBean2
     * @param ShowWhichOne      1 .从三查八对进入页面，首次进入，显示tpr，speed和look不可点击，冲管不显示
     *                          2.从巡视进入页面，显示tpr，speed，look，不显示冲管，首先显示的是look
     *                          3.从冲管关联界面进入页面，显示tpr，speed和look都不显示，显示冲管
     * @param mycounterTime     没有传null
     */
    public TPRSpeedObserveFra(SyncPatientBean patient, String cgplanOrderNo, BloodProductBean2
            mloodProductBean2, int ShowWhichOne, long mycounterTime) {
        super();
        this.patient = patient;
        this.planOrderNo = cgplanOrderNo;
        this.mBloodProductBean2 = mloodProductBean2;
        this.ShowWhichOne = ShowWhichOne;
        if (mycounterTime != 0) {
            this.mycounterTime = mycounterTime;
        }
    }

    public TPRSpeedObserveFra() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("正在同步药品信息，请稍后...");
        progressDialog = new ProgressDialog(getActivity());
        mLookBeans = new ArrayList<LookBean>();
        /**
         * 获取dict字典
         */
        getLookObserveDate();
        /**
         * 加载tpr，speed的历史记录
         */
        loadTprHistory();
        loadSpeedHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.fra_tpr_speed_obverse, null);

        /**
         * 初始化界面
         */
        initView(view);
        /**
         * 核对tpr数据的正确性
         */
        decideTprNum();

        switch (ShowWhichOne) {
            case 1:
                isSpeedClick = false;
                isLookClick = false;
                trasOrNo = true;
                choosenLookBeans = new ArrayList<LookBean>();
                /** 设置计时器 */
                mycounter = new MyCounter(mycounterTime, 1000, tv_time, sureLookStatus, tv_sure_look);
                break;
            case 2:
                //  由于从巡视界面进入值始终为null,让从本地读取全局变量
                //  LoadEsTimeAndIsTime();
                isSpeedClick = true;
                isLookClick = true;
                trasOrNo = false;
                setCheckButtonId(2);
                rl_tpr.setVisibility(View.GONE);
                rl_speed.setVisibility(View.GONE);
                rl_look.setVisibility(View.VISIBLE);
                ll_start_to_end_time_obverse.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        switch (getCheckButtonId()) {
            case 0:
                rg_tab_list.check(R.id.rbtn_tpr);
                break;
            case 1:
                rg_tab_list.check(R.id.rbtn_speed);
                break;
            case 2:
                rg_tab_list.check(R.id.rbtn_look);
                break;

            default:
                break;
        }

        rbSpeed.setClickable(isSpeedClick);
        rbLook.setClickable(isLookClick);

        // 判断，如果isSpeedClick是真的，替换左侧按钮背景
        if (isSpeedClick) {
            rbSpeed.setBackgroundResource(R.drawable.select_tso_radio_speed);
        }
        // 判断，如果isLookClick是真的，替换左侧按钮背景
        if (isLookClick) {
            rbLook.setBackgroundResource(R.drawable.select_tso_radio_observe);
        }

        rg_tab_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.rbtn_tpr:
                        rl_tpr.setVisibility(View.VISIBLE);
                        rl_speed.setVisibility(View.GONE);
                        rl_look.setVisibility(View.GONE);
                        // 判断，如果isSpeedClick是真的，替换左侧按钮背景
                        if (isSpeedClick) {
                            rbSpeed.setBackgroundResource(R.drawable.select_tso_radio_speed);
                        }
                        rbTpr.setBackgroundResource(R.drawable.select_tso_radio_tpr);
                        break;
                    case R.id.rbtn_speed:
                        rl_tpr.setVisibility(View.GONE);
                        rl_speed.setVisibility(View.VISIBLE);
                        rl_look.setVisibility(View.GONE);
                        // 判断，如果isLookClick是真的，替换左侧按钮背景
                        if (isLookClick) {
                            rbLook.setBackgroundResource(R.drawable.select_tso_radio_observe);
                        }
                        rbSpeed.setBackgroundResource(R.drawable.select_tso_radio_speed);
                        break;
                    case R.id.rbtn_look:
                        rl_tpr.setVisibility(View.GONE);
                        rl_speed.setVisibility(View.GONE);
                        rl_look.setVisibility(View.VISIBLE);

                        if (ShowWhichOne == 2) {
                            ll_start_to_end_time_obverse.setVisibility(View.GONE);
                        }
                        rbLook.setBackgroundResource(R.drawable.select_tso_radio_observe);
                        break;

                    default:
                        break;
                }
            }
        });

        return view;
    }

    /**
     * 比较tpr,speed的数值正确性
     */
    private void decideTprNum() {

        tiwenEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tiwen = tiwenEt.getText().toString().trim();
                if (!tiwen.equals("")) {
                    float num = 0.0f;
                    try {
                        num = Float.parseFloat(tiwen);
                    } catch (Exception e) {
                        ShowToast("请输入数值");
                    }
                    if (num > 43.0f || num < 35.0f) {
                        tiwenEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                        tFlag = false;
                    } else {
                        tFlag = true;
                        tiwenEt.setTextColor(getResources().getColor(R.color.black_text_content));
                    }
                    if (tFlag && pFlag && rFlag) {
                        tv_tpr_notice.setVisibility(View.GONE);
                    } else {
                        tv_tpr_notice.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        xintiaoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String xintiao = xintiaoEt.getText().toString().trim();
                if (!xintiao.equals("")) {
                    float num = 0.0f;
                    try {
                        num = Float.parseFloat(xintiao);
                    } catch (Exception e) {
                        ShowToast("请输入数值");
                    }
                    if (num > 500.0 || num < 0.0) {
                        xintiaoEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                        pFlag = false;
                    } else {
                        pFlag = true;
                        xintiaoEt.setTextColor(getResources().getColor(R.color.black_text_content));
                    }
                    if (tFlag && pFlag && rFlag) {
                        tv_tpr_notice.setVisibility(View.GONE);
                    } else {
                        tv_tpr_notice.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        huxiEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String huxi = huxiEt.getText().toString().trim();
                if (!huxi.equals("")) {
                    float num = 0.0f;
                    try {
                        num = Float.parseFloat(huxi);
                    } catch (Exception e) {
                        ShowToast("请输入数值");
                    }
                    if (num > 100.0 || num < 0.0) {
                        huxiEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                        rFlag = false;
                    } else {
                        rFlag = true;
                        huxiEt.setTextColor(getResources().getColor(R.color.black_text_content));
                    }
                    if (tFlag && pFlag && rFlag) {
                        tv_tpr_notice.setVisibility(View.GONE);
                    } else {
                        tv_tpr_notice.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        disuEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String disu = disuEt.getText().toString().trim();
                if (!disu.equals("")) {
                    int num = 0;
                    try {
                        num = Integer.parseInt(disu);
                    } catch (Exception e) {
                        ShowToast("请输入数值");
                    }
                    if (num > 20 || num < 0) {
                        disuEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                        tv_speed_notice.setVisibility(View.VISIBLE);
                    } else {
                        tv_speed_notice.setVisibility(View.GONE);
                        disuEt.setTextColor(getResources().getColor(R.color.black_text_content));
                    }
                }
            }
        });
    }

    /**
     * 初始化界面资源
     */
    private void initView(final View view) {
        // radioGroup
        rg_tab_list = (RadioGroup) view.findViewById(R.id.rg_tab_list);
        rbTpr = (RadioButton) view.findViewById(R.id.rbtn_tpr);
        rbSpeed = (RadioButton) view.findViewById(R.id.rbtn_speed);
        rbLook = (RadioButton) view.findViewById(R.id.rbtn_look);

        rl_tpr = (RelativeLayout) view.findViewById(R.id.rl_tpr);
        rl_speed = (RelativeLayout) view.findViewById(R.id.rl_speed);
        rl_look = (RelativeLayout) view.findViewById(R.id.rl_look);
        gv_look = (GridView) view.findViewById(R.id.gv_look);

        tv_war_tw = (TextView) view.findViewById(R.id.tv_war_tw);
        tv_war_hx = (TextView) view.findViewById(R.id.tv_war_hx);
        tv_war_mb = (TextView) view.findViewById(R.id.tv_war_mb);

        tiwenEt = (EditText) view.findViewById(R.id.et_item_fyh_type_);
        xintiaoEt = (EditText) view.findViewById(R.id.et_item_fyh_type_2);
        huxiEt = (EditText) view.findViewById(R.id.et_item_fyh_type_3);
        // 滴速
        disuEt = (EditText) view.findViewById(R.id.et_item_fyh_type_4);

        tv_time = (TextView) view.findViewById(R.id.tv_time_sy);

        tv_sure_tpr = (Button) view.findViewById(R.id.tv_sure_tpr);
        tv_sure_tpr.setOnClickListener(this);

        tv_sure_speed = (Button) view.findViewById(R.id.tv_sure_speed);
        tv_sure_speed.setOnClickListener(this);

        tv_sure_look = (Button) view.findViewById(R.id.tv_sure);

        tv_sure_look.setOnClickListener(this);

        tv_tras_time2 = (TextView) view.findViewById(R.id.tv_tras_time2);
        tv_tras_time3 = (TextView) view.findViewById(R.id.tv_tras_time3);

        tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText("床号：" + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }

        tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);
        tv_tras_name.setText("姓名:" + patient.getName());

        imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);

        tv_gc_start = (TextView) view.findViewById(R.id.tv_gc_start);
        tv_gc_start.setVisibility(view.VISIBLE);
        if (GlobalConstant.bloodStartTime != null) {
            String bloodStartTime = GlobalConstant.bloodStartTime;
            String startTime = DateTool.toMinAndSecond(bloodStartTime);
            tv_gc_start.setText(startTime);
        }
        tv_gc_patry = (TextView) view.findViewById(R.id.tv_gc_patry);
        tv_gc_patry.setVisibility(view.VISIBLE);
        if (GlobalConstant.bloodPatroyTime != null) {
            String bloodEndTime = GlobalConstant.bloodPatroyTime;
            String endTime = DateTool.toMinAndSecond(bloodEndTime);
            tv_gc_patry.setText(endTime);
        }
        tv_no_history = (TextView) view.findViewById(R.id.tv_no_history);
        tv_tpr_notice = (TextView) view.findViewById(R.id.tv_tpr_notice);
        lv_tpr = (ListView) view.findViewById(R.id.lv_tpr);
        lv_speed = (ListView) view.findViewById(R.id.lv_speed);
        speed_no_history = (TextView) view.findViewById(R.id.speed_no_history);
        tv_speed_notice = (TextView) view.findViewById(R.id.tv_speed_notice);
        ll_tso_normal = (LinearLayout) view.findViewById(R.id.ll_tso_normal);
        ll_tso_un_normal = (LinearLayout) view.findViewById(R.id.ll_tso_un_normal);
        tv_un_normal = (TextView) view.findViewById(R.id.tv_un_normal);
        ll_start_to_end_time_obverse = (LinearLayout) view.findViewById(R.id.ll_start_to_end_time_obverse);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure_tpr:
                int xtLength = xintiaoEt.getText().toString().trim().length();
                int hxLength = huxiEt.getText().toString().trim().length();
                int twLength = tiwenEt.getText().toString().trim().length();
                if (xtLength > 0 && hxLength > 0 && twLength > 0) {
                    saveTPRDate();

                    /**
                     * 跳转到speed部分
                     */
                    if (!isSpeedClick) {
                        isSpeedClick = true;
                        rbSpeed.setClickable(isSpeedClick);
                        rg_tab_list.check(R.id.rbtn_speed);
                        rbSpeed.setBackgroundResource(R.drawable.tso_speed);
                        rbTpr.setBackgroundResource(R.drawable.tso_tpr_s);
                    }
                } else {
                    ShowToast("请填写体征值");
                }

                break;
            case R.id.tv_sure_speed:
                int disuLength = disuEt.getText().toString().trim().length();
                if (disuLength > 0) {
                    saveSpeedDate();
                    tv_speed_notice.setVisibility(View.GONE);
                    if (!isLookClick) {
                        isLookClick = true;
                        rbLook.setClickable(isLookClick);
                        rg_tab_list.check(R.id.rbtn_look);
                    }
                } else {
                    ShowToast("请输入滴速");
                }

                break;
            case R.id.tv_sure:
                if (tv_sure_look.getText().toString().equals("退 出")) {
                    Intent intent = new Intent();
                    intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                    getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                    getActivity().finish();
                } else {
                    if (sureLookStatus == 0) {
                        if (choosenLookBeans.isEmpty()) {
                            if(isFirst){
                                recNormal();
                            }
                        } else {
                            showAlertDialog(choosenLookBeans);
                        }
                    }
                }
                break;
//            case R.id.bg_back:
//                // 到巡视界面
//                getFragmentManager().beginTransaction().replace(R.id.fl_container, new TrasPatrolFra(patient,
//                        mBloodProductBean2)).commit();
//                break;
            case R.id.imbtn_back:
                if (ShowWhichOne == 1) {
                    if (rl_look.getVisibility() == View.VISIBLE) {
                        showCanNotBack();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                        getActivity().finish();
                    }
                } else if (ShowWhichOne == 2) {
                    /**
                     * 跳转到巡视界面
                     */
//                    getFragmentManager().beginTransaction().replace(R.id.fl_container, new TrasPatrolFra
//                            (patient, mBloodProductBean2)).commit();
                    Intent intent = new Intent();
                    intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                    getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                    super.getActivity().onBackPressed();
                }
                break;
            case R.id.imbtn_home:
                /**
                 * 返回主界面
                 */
                getActivity().startActivity(new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        setOrderItemBean(orderItemBeans.get(position));
        adapter.notifyChanged(position);
    }

    /**
     * 记录观察问题数据
     *
     * @param status
     * @param itemCode
     * @param dosage
     */
    private void recInspectResult(final String status, final String itemCode, final String dosage) {
        RecInspectResultBean rec = new RecInspectResultBean();
        rec.setUsername(UserInfo.getUserName());
        rec.setPatId(patient.getPatId());
        rec.setPlanOrderNO(mBloodProductBean2.getPlanOrderNo());
        rec.setItemCode(itemCode);
        rec.setStatus(status);
        if (status.equals("-1")) {
            rec.setHandleText("出现输血反应，停止输血");
        } else if (status.equals("1")) {
            rec.setHandleText("出现输血问题");
        } else if (status.equals("100")) {
            rec.setHandleText("出现输血反应,继续输血");
        }
        rec.setBloodId(mBloodProductBean2.getBloodId());
        if (otherDesc != null) {
            rec.setOtherDesc(otherDesc);
        }
        if (dosage != null) {
            rec.setDosage(dosage);
        }
        String url = UrlConstant.RecInspectDate() + rec.toString();
        CCLog.i("输血反应>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                if (status.equals("1")) {
//                    mChangDialogTwoBtn.dismiss();
//                } else {
//                    if(mCautionDialog!=null){
//                        mCautionDialog.dismiss();
//                    }
//
//                }

                Gson gson = new Gson();
                RecInspectResultBean recResult = gson.fromJson(result, RecInspectResultBean.class);
                if (recResult.isResult()) {
                    choosenLookBeans.clear();//避免重复提交
                    StringBuffer itemDesc = new StringBuffer();
                    String[] wrongItemCode = itemCode.split(",");
                    for (String code : wrongItemCode) {
                        for (int i = 0; i < mLookBeans.size(); i++) {
                            if (mLookBeans.get(i).getItemCode().equals(code)) {
                                String itemName = mLookBeans.get(i).getItemName();
                                itemDesc.append(itemName);
                                itemDesc.append(",");
                                mLookBeans.remove(i);
                            }
                        }
                    }
                    StringBuffer oldText = new StringBuffer();
                    oldText.append(tv_un_normal.getText().toString().trim());
                    oldText.append(",");
                    oldText.append(itemDesc.toString());
                    String text = oldText.toString();
                    text = text.substring(0, text.length() - 1);
                    if (text.indexOf(",") == 0) {
                        text = text.substring(1);
                    }
                    tv_un_normal.setText(text);
                    lookAdapter.notifyDataSetChanged();
                    ll_tso_un_normal.setVisibility(View.VISIBLE);
                    ll_tso_normal.setVisibility(View.GONE);
                   // choosenLookBeans = new ArrayList<LookBean>();
                    /**
                     * 异常结束输血
                     */
                    if (status.equals("-1")) {
                        compliteTransfusionNew("-1", dosage);
                    }
                } else {
                    Toast.makeText(getActivity(), recResult.getMsg(), Toast.LENGTH_SHORT).show();
                    CCLog.i(TAG, recResult.getMsg());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                if (status.equals("1")) {
//                    mChangDialogTwoBtn.dismiss();
//                } else {
//                    if(mCautionDialog!=null){
//                        mCautionDialog.dismiss();
//                    }
//                }
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * 记录观察问题正常
     */
    private void recNormal() {
        RecInspectResultBean rec = new RecInspectResultBean();
        rec.setUsername(UserInfo.getUserName());
        rec.setPatId(patient.getPatId());
        rec.setPlanOrderNO(mBloodProductBean2.getPlanOrderNo());
        String url = UrlConstant.recNormal() + rec.toString();

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                RecInspectResultBean recResult = gson.fromJson(result, RecInspectResultBean.class);
                if (recResult.isResult()) {
                    isFirst=false;
                    Toast.makeText(getActivity(), recResult.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), recResult.getMsg(), Toast.LENGTH_SHORT).show();
                    CCLog.i(TAG, recResult.getMsg());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 加载输血开始时间和下次巡视时间
     */
    private void LoadEsTimeAndIsTime() {
        String url = UrlConstant.LoadEsTimeAndIsTime() + UserInfo.getUserName() + "&patId=" + patient
                .getPatId()
                + "&planOrderNo=" + mBloodProductBean2.getPlanOrderNo();
        CCLog.i(TAG, "加载输血开始时间>>>：" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                progressDialog.dismiss();
                JSONObject mJsonObject = null;
                String mStratTime = null;
                String mPatrolTime = null;
                boolean state = false;
                try {
                    mJsonObject = new JSONObject(arg0.result);

                    state = mJsonObject.getBoolean("result");
                    if (state) {
                        mStratTime = mJsonObject.getString("eventStartTime");
                        mPatrolTime = mJsonObject.getString("inspectionTime");
                    }

                } catch (JSONException e1) {
                    CCLog.e(TAG, "加载输血时间异常");
                    e1.printStackTrace();
                }
                //观察界面用在这儿赋值
                if (mStratTime != null) {
                    tv_gc_start.setText(DateTool.toMinAndSecond(mStratTime));
                } else {
                    tv_gc_start.setVisibility(View.GONE);
                }
                if (mPatrolTime != null) {
                    tv_gc_patry.setText(DateTool.toMinAndSecond(mPatrolTime));
                } else {
                    tv_gc_patry.setVisibility(View.GONE);
                }
                GlobalConstant.bloodStartTime = mStratTime;
                GlobalConstant.bloodPatroyTime = mPatrolTime;
            }
        });
    }

    /**
     * 保存TPR信息 需要传递的参数：
     * 1.username//用户名
     * 2.patId//病人id
     * 3.planOrderNo//医嘱执行任务id
     * 4.temperature//体温
     * 5.pulse//脉搏
     * 6.respire//呼吸
     */
    private void saveTPRDate() {
        progressDialog.setMessage("正在保存..");
        progressDialog.show();
        final String dateTime = DateTool.getCurrDateTime();
        TPRRecordBean tprBean = new TPRRecordBean();
        tprBean.setPatId(patient.getPatId());
        tprBean.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
        tprBean.setPulse(xintiaoEt.getText().toString());
        tprBean.setRespire(huxiEt.getText().toString());
        tprBean.setTemperature(tiwenEt.getText().toString());
        tprBean.setUsername(UserInfo.getUserName());

        String url = UrlConstant.saveTPRDate() + tprBean.toString();
        CCLog.i("保存tpr数据>>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                Gson gson = new Gson();
                TPRRecordBean tpr = gson.fromJson(result, TPRRecordBean.class);
                if (tpr.isResult()) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    if (!isSpeedClick) {
                        isSpeedClick = true;
                        rbSpeed.setClickable(isSpeedClick);
                        rg_tab_list.check(R.id.rbtn_speed);
                        rbSpeed.setBackgroundResource(R.drawable.tso_speed);
                        rbTpr.setBackgroundResource(R.drawable.tso_tpr_s);
                    }
                    TprSpeedHistoryCommonBean tprBean = new TprSpeedHistoryCommonBean();
                    tprBean.setTime(DateTool.toTpr(dateTime));
                    tprBean.setT(tiwenEt.getText().toString());
                    tprBean.setP(xintiaoEt.getText().toString());
                    tprBean.setR(huxiEt.getText().toString());
                    tprBeans.add(0, tprBean);
                    xintiaoEt.setText("");
                    huxiEt.setText("");
                    tiwenEt.setText("");
                    tv_tpr_notice.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                }
                tv_no_history.setVisibility(View.GONE);
                lv_tpr.setVisibility(View.VISIBLE);
                tprHistoryListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * 保存输血速度的数值
     */
    private void saveSpeedDate() {
        progressDialog.setMessage("正在保存..");
        progressDialog.show();
        final String dateTime = DateTool.getCurrDateTime();
        SpeedDateBean speed = new SpeedDateBean();
        speed.setPatId(patient.getPatId());
        speed.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
        speed.setSpeed(disuEt.getText().toString());
        speed.setUsername(UserInfo.getUserName());
        String url = UrlConstant.saveSpeedDate() + speed.toString();
        CCLog.i("保存滴速>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                Gson gson = new Gson();
                SpeedDateBean spe = gson.fromJson(result, SpeedDateBean.class);
                if (spe.isResult()) {
                    Toast.makeText(getActivity(), "记录成功", Toast.LENGTH_SHORT).show();
                    TprSpeedHistoryCommonBean bean = new TprSpeedHistoryCommonBean();
                    bean.setTime(DateTool.toTpr(dateTime));
                    bean.setSpeed(disuEt.getText().toString() + "滴/分钟");
                    speedBeans.add(0, bean);
                    speed_no_history.setVisibility(View.GONE);
                    lv_speed.setVisibility(View.VISIBLE);
                    speedHistoryListAdapter.notifyDataSetChanged();
                    disuEt.setText("");
                    /**
                     * 系统开始输血
                     */
                    if (!obverseFlag) {
                        startTrasfusion();
                    }
                } else {
                    Toast.makeText(getActivity(), "数据提交失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * 获得观察dict的值
     */
    private void getLookObserveDate() {
        String url = UrlConstant.GetLookObserve() + "?username=" + UserInfo.getUserName() + "&bloodDonorCode=" +
                mBloodProductBean2.getBloodDonorCode();
        CCLog.v(TAG, "观察字典加载成功:" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.v(TAG, "观察字典加载失败");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                CCLog.v(TAG, "观察字典加载成功");
                JSONObject mJsonObject = null;
                JSONArray mJsonArray = null;
                boolean responseState = false;
                String varianceCause = new String();
                String varianceCauseDesc = new String();
                try {
                    mJsonObject = new JSONObject(arg0.result);
                    responseState = mJsonObject.getBoolean("result");
                    varianceCause = mJsonObject.getString("varianceCause");
                    varianceCauseDesc = mJsonObject.getString("varianceCauseDesc");
                    if (responseState) {
                        if (varianceCause.equals("") || varianceCause.equals("null")) {
                            ll_tso_normal.setVisibility(View.VISIBLE);
                            ll_tso_un_normal.setVisibility(View.GONE);
                        } else {
                            ll_tso_normal.setVisibility(View.GONE);
                            ll_tso_un_normal.setVisibility(View.VISIBLE);
                            tv_un_normal.setText(varianceCauseDesc);
                        }
                        mJsonArray = mJsonObject.getJSONArray("dict");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LookBean>>() {
                        }.getType();
                        mLookBeans = gson.fromJson(mJsonArray.toString(), type);

                        lookAdapter = new GridViewLookAdapter(getActivity(), mLookBeans);
                        gv_look.setAdapter(lookAdapter);
                        gv_look.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (mLookBeans.get(position).isSelected()) {
                                    if (choosenLookBeans.isEmpty()) {
                                        ShowToast(getActivity().getResources().getString(R.string
                                                .had_not_choosen));
                                    } else {
                                        if (choosenLookBeans.contains(mLookBeans.get(position)) && mLookBeans.get
                                                (position).isSelected()) {
                                            mLookBeans.get(position).setIsSelected(false);
                                            choosenLookBeans.remove(mLookBeans.get(position));
                                            lookAdapter.notifyDataSetChanged();
                                        } else {
                                            ShowToast(getActivity().getResources().getString(R.string
                                                    .had_not_choosen));
                                        }
                                    }
                                } else {
                                    boolean flag = true;
                                    if (mLookBeans.get(position).getItemCode().equalsIgnoreCase("Z")) {
                                        flag = showOtherDictDialog();
                                    }
                                    if (flag) {
                                        mLookBeans.get(position).setIsSelected(true);
                                        choosenLookBeans.add(mLookBeans.get(position));
                                    }
                                    lookAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        mLookBeans = null;
                        ShowToast("没有观察字典数据,请联系运维");
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

            }
        });
    }

    /**
     * 开始输血 startTransfusion
     */
    private void startTrasfusion() {
        String tempSpeed = speedBeans.get(0).getSpeed();
        String speed = tempSpeed.substring(0, tempSpeed.lastIndexOf("滴/分钟"));
        String url = UrlConstant.StartTrasfusion() + UserInfo.getUserName() + "&patId=" + patient.getPatId() +
                "&planOrderNo=" + mBloodProductBean2.getPlanOrderNo() + "&speed=" + speed +
                "&applyNo=" + mBloodProductBean2.getApplyNo() + "&itemNo=" + mBloodProductBean2
                .getItemNo() + "&bloodId=" + mBloodProductBean2.getBloodId();
        // 判断是否是输血
        if (trasOrNo) {
            HttpUtils http = new HttpUtils();
            CCLog.i("开始输血接口>>>", url);
            http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    ShowToast("系统输血失败");
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    mycounter.start();
                    ShowToast("系统已开始输血");
                    obverseFlag = true;
                    //只有输血开始后才有输血开始时间和结束时间
                    LoadEsTimeAndIsTime();
                }
            });
            trasOrNo = false;
        }
    }

    /**
     * 观察问题的dialog
     *
     * @param mybeans
     */
    private void showAlertDialog(final List<LookBean> mybeans) {
        final AlertDialogThreeBtnNoicon alertDialogThreeBtnNoicon = new AlertDialogThreeBtnNoicon(getActivity());
        alertDialogThreeBtnNoicon.setTitle("");
        alertDialogThreeBtnNoicon.setMessage("出现输血反应,是否继续输血");
        alertDialogThreeBtnNoicon.setLeftButton("继续", new OnClickListener() {
            @Override
            public void onClick(View v) {
                recInspectResult("100", splitString(mybeans), null);
                lookAdapter.notifyDataSetChanged();
                alertDialogThreeBtnNoicon.dismiss();
            }
        });
        alertDialogThreeBtnNoicon.setRightButton("结束", new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 异常停止输血
                showInputDialog(splitString(mybeans));
                alertDialogThreeBtnNoicon.dismiss();
            }
        });

        alertDialogThreeBtnNoicon.setBtnPauseButton("暂停", new OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedTrans(patient.getPatId(),UserInfo.getUserName(), mBloodProductBean2.getPlanOrderNo(), splitString(mybeans));
                alertDialogThreeBtnNoicon.dismiss();
            }
        });
        alertDialogThreeBtnNoicon.show();
    }

    /**
     * 观察问题输入输血量的dialog
     *
     * @param itemCode
     */
    private void showInputDialog(final String itemCode) {
        mCautionDialog = new AlertDialogCaution(getActivity());
        mCautionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCautionDialog.show();

        final EditText edt = (EditText) mCautionDialog.findViewById(R.id.et_caution);
        Button confirm = (Button) mCautionDialog.findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                recInspectResult("-1", itemCode, edt.getText().toString());
                lookAdapter.notifyDataSetChanged();
                if (mycounter != null) {
                    mycounter.cancel();
                }
                mCautionDialog.dismiss();
            }
        });

    }

    /**
     * 观察问题的dialog
     */
    private boolean showOtherDictDialog() {
        otherDescFlag = true;
        otherDialog = new AlertDialogOther(getActivity());
        otherDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otherDialog.show();

        final EditText edt = (EditText) otherDialog.findViewById(R.id.et_other);
        Button confirm = (Button) otherDialog.findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                otherDesc = edt.getText().toString().trim();
                if (otherDesc.equals("")) {
                    ShowToast("请输入描述");
                    otherDescFlag = false;
                }
                otherDialog.dismiss();
            }
        });
        return otherDescFlag;
    }

    private void showCanNotBack() {
        canBackDialog = new AlertDialogTwoBtn(getActivity());
        String title = getString(R.string.notice_message_title);
        String context = getString(R.string.advise_not_leave);
        canBackDialog.setTitle(title);
        canBackDialog.setMessage(context);
        canBackDialog.setLeftButton("离开", new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                getActivity().finish();
                canBackDialog.dismiss();
            }
        });
        canBackDialog.setRightButton("继续观察", new OnClickListener() {

            @Override
            public void onClick(View v) {
                canBackDialog.dismiss();
            }
        });
        canBackDialog.show();
    }

    /**
     * 完成输血 新接口
     *
     * @param status:1表示正常结束 -1表示异常结束
     * @param dosage:        异常结束输入量 正常结束可传null
     */
    private void compliteTransfusionNew(String status, String dosage) {
        String url = UrlConstant.CompliteTransfusion() + UrlTool.transWord(UserInfo.getUserName()) + "&patId=" + UrlTool.transWord(patient.getPatId())
                + "&applyNo=" + UrlTool.transWord(mBloodProductBean2.getApplyNo()) + "&itemNo=" + UrlTool.transWord(mBloodProductBean2.getItemNo())
                + "&bloodId=" + UrlTool.transWord(mBloodProductBean2.getBloodId()) + "&planOrderNo=" +
                UrlTool.transWord(mBloodProductBean2.getPlanOrderNo()) + "&status=" + UrlTool.transWord(status) + "&dosage=" + UrlTool.transWord(dosage);
        CCLog.i("完成输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            JSONObject mJsonObject = null;
            boolean result = false;
            JSONArray jsonArray = null;

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.i("巡视界面加载输血医嘱失败了");
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                CCLog.i("巡视界面加载输血医嘱成功了");
                try {
                    mJsonObject = new JSONObject(arg0.result);
                    result = mJsonObject.getBoolean("result");
                    if (result) {
                        ShowToast("系统已结束输血");
                        tv_sure_look.setText("退 出");
                        /**
                         * 如果isback是真的，提交按钮文字变成退出，点击，销毁activity
                         */
                        isBack = true;
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                        getActivity().finish();
                    } else {
                        ShowToast("结束输血失败");
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }
        });
    }

    /**
     * 加载tpr的历史记录
     */
    private void loadTprHistory() {
        pDialog.show();
        TprSpeedHistoryCommonBean bean = new TprSpeedHistoryCommonBean();
        bean.setPatId(patient.getPatId());
        bean.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
        String url = UrlConstant.loadTprHistory() + bean.toString();
        CCLog.i(TAG, "加载tpr历史记录>" + url);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pDialog.dismiss();
                CCLog.i(TAG, "加载tpr的历史记录成功");
                String result = responseInfo.result;
                Gson gson = new Gson();
                Type type = new TypeToken<List<TprSpeedHistoryCommonBean>>() {
                }.getType();
                tprBeans = gson.fromJson(result, type);
                if (tprBeans == null) {
                    tprBeans = new ArrayList<TprSpeedHistoryCommonBean>();
                }
                if (!tprBeans.isEmpty()) {
                    tv_no_history.setVisibility(View.GONE);
                    lv_tpr.setVisibility(View.VISIBLE);
                } else {
                    tv_no_history.setVisibility(View.VISIBLE);
                    lv_tpr.setVisibility(View.GONE);
                }
                lv_tpr.setAdapter(tprHistoryListAdapter = new TprHistoryListAdapter(getActivity(), tprBeans));
                tprHistoryListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "加载tpr的历史记录失败");
                pDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * 加载speed的历史记录
     */
    private void loadSpeedHistory() {
        pDialog.show();
        TprSpeedHistoryCommonBean bean = new TprSpeedHistoryCommonBean();
        bean.setPatId(patient.getPatId());
        bean.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
        String url = UrlConstant.loadSpeedHistory() + bean.toString();
        CCLog.i(TAG, "加载Speed历史记录>" + url);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pDialog.dismiss();
                CCLog.i(TAG, "加载speed的历史记录成功");
                String result = responseInfo.result;
                Gson gson = new Gson();
                Type type = new TypeToken<List<TprSpeedHistoryCommonBean>>() {
                }.getType();
                speedBeans = gson.fromJson(result, type);
                if (speedBeans == null) {
                    speedBeans = new ArrayList<TprSpeedHistoryCommonBean>();
                }
                if (!speedBeans.isEmpty()) {
                    speed_no_history.setVisibility(View.GONE);
                    lv_speed.setVisibility(View.VISIBLE);
                } else {
                    speed_no_history.setVisibility(View.VISIBLE);
                    lv_speed.setVisibility(View.GONE);
                }
                lv_speed.setAdapter(speedHistoryListAdapter = new SpeedHistoryListAdapter(getActivity(),
                        speedBeans));
                speedHistoryListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "加载speed的历史记录失败");
                pDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * @param title
     * @param message
     */
    private void createDialog(String title, String message) {
        final AlertDialogOneBtn promptDialog = new AlertDialogOneBtn(
                getActivity());
        promptDialog.setTitle(title);
        promptDialog.setMessage(message);
        promptDialog.setButton("确定");
        promptDialog.setButtonListener(true, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // getActivity().finish();
                promptDialog.dismiss();

            }
        });
        promptDialog.show();
    }

    /**
     * 拼合观察情况字符串
     *
     * @param mybeans
     * @return
     */
    private String splitString(List<LookBean> mybeans) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < mybeans.size(); i++) {
            LookBean lookbean = mybeans.get(i);
            str.append(lookbean.getItemCode());

            if (i < mybeans.size() - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }

    /**
     * 显示时间
     *
     * @param time
     * @return
     */
    private String getTime(long time) {

        long h = time / 3600;

        time = time % 3600;

        long m = time / 60;

        time = time % 60;

        long s = time;

        return getTimeString(h) + ":" + getTimeString(m) + ":"
                + getTimeString(s);
    }

    /**
     * 时间转化
     *
     * @param l
     * @return
     */
    private String getTimeString(long l) {

        String string;

        if (l < 10) {
            string = "0" + l;
        } else {
            string = String.valueOf(l);
        }

        return string;
    }

    private void pausedTrans(String patId,String username, String planOrderNo, String performDesc) {
        String url = UrlConstant.pausedTrans() + patId + "&planOrderNo=" + planOrderNo + "&performDesc=" + performDesc + "&suspEndAttr=1"+"&username="+username;
        CCLog.i("暂停输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonobject = new JSONObject(responseInfo.result);
                    boolean result = jsonobject.getBoolean("result");
                    if (result) {
                        choosenLookBeans.clear();
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ShowToast("暂停失败,请检查数据");
            }
        });
    }

    /**
     * 更新时间显示ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATETIMER:
                    //如果时间到了就自动退出
                    if (msg.obj.equals("00:00")) {
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                        getActivity().finish();
                    }
                    tv_tras_time3.setText("还剩:" + (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isrun = false;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_tpr_sp_look);
        SupportDisplay.resetAllChildViewParam(root);
    }

    public OrderItemBean getOrderItemBean() {
        return orderItemBean;
    }

    public void setOrderItemBean(OrderItemBean orderItemBean) {
        this.orderItemBean = orderItemBean;
    }

    public int getCheckButtonId() {
        return checkButtonId;
    }

    public void setCheckButtonId(int checkButtonId) {
        this.checkButtonId = checkButtonId;
    }

    public String getCurrentPlanNo() {
        return currentPlanNo;
    }

    public void setCurrentPlanNo(String currentPlanNo) {
        this.currentPlanNo = currentPlanNo;
    }

    public boolean isShowSpeed() {
        return isShowSpeed;
    }

    public void setShowSpeed(boolean isShowSpeed) {
        this.isShowSpeed = isShowSpeed;
    }

    public boolean isShowLook() {
        return isShowLook;
    }

    public void setShowLook(boolean isShowLook) {
        this.isShowLook = isShowLook;
    }

    public long getMycounterTime() {
        return mycounterTime;
    }

    public void setMycounterTime(long mycounterTime) {
        this.mycounterTime = mycounterTime;
    }

}
