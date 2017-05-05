package com.gentlehealthcare.mobilecare.activity.patient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.TabListener;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.PatientConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.ScreenTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AutoCallBackTextView;
import com.gentlehealthcare.mobilecare.view.adapter.MyFragmentStateAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.SlidingListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 病人信息界面
 */
public class PatientAct extends BaseActivity {

    private SyncPatientBean patient;

    private List<String> list = new ArrayList<String>();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SlidingListAdapter mAdapter;

    private MyFragmentStateAdapter fragmentAdapter;
    private SlidingMenu slidingMenu;
    private ListView lvSliding;

    private Button btn_back;
    private AutoCallBackTextView tv_title;
    private ViewPager vp_work;
    private RadioGroup rg_bottom;
    private Button btn_chooseDay;
    private DatePicker mDatePicker;
    private ProgressDialog progressDialog = null;

    private String eventId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
        eventId = getIntent().getStringExtra("eventId");
        // 初始化控件
        initWidget();
        initSlidingMenu();
        // 时间选择器
        mDatePicker = new DatePicker(PatientAct.this);
        btn_chooseDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PatientAct.this, myDatePickerDialog, mDatePicker
                        .getYear(), mDatePicker.getMonth(), mDatePicker
                        .getDayOfMonth());
                datePickerDialog.show();
            }
        });

        lvSliding.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                //patient = UserInfo.getMyPatient().get(position);  update by:zyy 2016-3-7
                patient = UserInfo.getDeptPatient().get(position);
                setFragments(patient);
                mAdapter.notifyChanged(position);
                slidingMenu.showContent();
                tv_title.setContentStr(getPatientInfo());
                tv_title.collection();
            }
        });

        lvSliding.setSelector(R.color.transparent);
        findViewById(R.id.btn_slidingmenu_home).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_right);
                    }
                });

        lvSliding.setAdapter(mAdapter = new SlidingListAdapter(this, list));
        /**
         * 2016-3-7改
         */
        List<SyncPatientBean> patients = UserInfo.getDeptPatient();
//		List<SyncPatientBean> patients = UserInfo.getMyPatient();
        for (int i = 0; i < patients.size(); i++) {
            list.add(patients.get(i).getName());
            if (patient.getPatId().equals(patients.get(i).getPatId()))
                mAdapter.notifyChanged(i);
        }
        vp_work.setAdapter(fragmentAdapter = new MyFragmentStateAdapter(
                getSupportFragmentManager(), fragments));

        RadioButton button;
        for (int i = 0; i < rg_bottom.getChildCount(); i++) {
            button = (RadioButton) rg_bottom.getChildAt(i);
            button.setId(i);
        }
        rg_bottom.check(0);

        new TabListener(rg_bottom, vp_work);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragments(patient);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_patientw);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 初始化侧边框
     */
    private void initSlidingMenu() {

        slidingMenu = new SlidingMenu(this);

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 设置边缘滑动模式

        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        slidingMenu.setBehindWidth(ScreenTool.getScreenWidth(this) / 4 * 3);// 设置slidingmenu的宽度
        slidingMenu.setMenu(R.layout.slidingmenu_left);// menu布局

        slidingMenu.setMode(SlidingMenu.LEFT_OF);// 设置slidingmenu两侧menu模式
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                btn_back.setVisibility(View.INVISIBLE);
            }
        });

        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                btn_back.setVisibility(View.VISIBLE);
            }
        });
        ((TextView) findViewById(R.id.tv_head)).setText("病人清单");
        lvSliding = (ListView) findViewById(R.id.lv_sliding);
    }

    /**
     * 初始化控件
     */
    @SuppressLint("WrongViewCast")
    private void initWidget() {
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_title = (AutoCallBackTextView) findViewById(R.id.tv_info);
        vp_work = (ViewPager) findViewById(R.id.vp_work);
        // 待 & 中 & 已
        rg_bottom = (RadioGroup) findViewById(R.id.rgp_bottom);
        btn_back.setBackgroundResource(R.drawable.skin_home);
        tv_title.setDetailStr(getPatientInfoDetail());
        tv_title.setContentStr(getPatientInfo());
        tv_title.collection();
        btn_chooseDay = (Button) findViewById(R.id.btn_chooseDay);
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getPatientInfo
     * @Description: 获取病人信息
     */
    private String getPatientInfo() {
        if (patient.getBedLabel() != null) {
            return getResources().getString(R.string.bed_label) + patient.getBedLabel() + "\n" + getResources()
                    .getString(R.string.patient_name)
                    + patient.getName();
        } else {
            return getResources().getString(R.string.bed_label) + "未分配" + "\n" + getResources().getString(R
                    .string.patient_name)
                    + patient.getName();
        }

    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getPatientInfoDetail
     * @Description: 获取病人详细信息
     */
    private String getPatientInfoDetail() {
        return getPatientInfo();
    }

    /**
     * 加载子Fragment类
     *
     * @param patient
     */
    public void setFragments(SyncPatientBean patient) {
        fragments.clear();
        fragments.add(new ProgressPatientFra(patient,
                PatientConstant.STATE_WAITING));
        fragments.add(new ProgressPatientFra(patient,
                PatientConstant.STATE_EXECUTING));
        fragments.add(new ProgressPatientFra(patient,
                PatientConstant.STATE_EXECUTED));
        fragmentAdapter.notifyDataSetChanged();
    }
    /**
     * 自定义时间选择控件
     */
    public class MyDatePickerDialog implements
            DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = month < 10 ? "0" + month : month + "";
            String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth
                    + "";
            btn_chooseDay.setText(year + "-" + monthStr + "-" + dayStr);
            Fragment currentFra = fragmentAdapter.getItem(vp_work
                    .getCurrentItem());
            if (currentFra instanceof ProgressPatientFra)
                ((ProgressPatientFra) currentFra).notifyData();
        }
    }

    /**
     * 获取时间文本
     *
     * @return
     */
    public String GetDayText() {
        if (btn_chooseDay.getText().equals("今天"))
            return DateTool.formatDate(DateTool.YYYY_MM_DD_STYPE, new Date());
        else
            return btn_chooseDay.getText().toString();
    }

    /**
     * 从后台判断除了输血之外的
     *
     * @param patId
     * @param relatedBarcode
     * @param userName
     */
    private void loadData(String patId, String relatedBarcode, String userName) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getString(R.string.drugloading));
        progressDialog.show();

        String encode = null;
        try {
            encode = URLEncoder.encode(relatedBarcode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = UrlConstant.loadOrdersByRelatedBarcode() + userName + "&relatedBarcode=" +
                encode + "&patId=" + patId + "&eventId=" + eventId;
        CCLog.i("流程界面加载静脉给药医嘱信息>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                String status = null;
                String template = null;
                String msg = null;
                String planOrderNo = null;
                List<OrderItemBean> orders = null;
                List<BloodProductBean2> bloodOrders = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    status = jsonObject.getString("status");
                    template = jsonObject.getString("template");
                    jsonArray = jsonObject.getJSONArray("data");
                    msg = jsonObject.getString("msg");
                    planOrderNo = jsonObject.getString("planOrderNo");
                    if (result) {
                        if (!template.equals("AA-5")) {
                            orders = new ArrayList<OrderItemBean>();
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<OrderItemBean>>() {
                            }.getType();
                            orders.clear();
                            orders = gson.fromJson(jsonArray.toString(), type);
                            if (!orders.isEmpty()) {
                                goTo(template, status, orders.get(0), planOrderNo, null);
                            } else {
                                ShowToast(getString(R.string.dataexception));
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        if (msg != null) {
                            ShowToast(msg);
                        }
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(PatientAct.this, getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(PatientAct.this, getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(PatientAct.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 从后台判断输血医嘱
     *
     * @param patId
     * @param bloodDonorCode
     * @param userName
     */
    private void loadBlood(String patId, String bloodDonorCode, String userName) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getString(R.string.drugloading));
        progressDialog.show();

        String encode = null;
        try {
            encode = URLEncoder.encode(bloodDonorCode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = UrlConstant.loadOrdersByBloodDonorCode() + userName + "&bloodDonorCode=" +
                encode + "&patId=" + patId + "&eventId=" + eventId;
        CCLog.i("流程界面加载输血医嘱信息>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                String status = null;
                String template = null;
                String msg = null;
                String planOrderNo = null;
                List<OrderItemBean> orders = null;
                List<BloodProductBean2> bloodOrders = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    status = jsonObject.getString("status");
                    template = jsonObject.getString("template");
                    jsonArray = jsonObject.getJSONArray("data");
                    msg = jsonObject.getString("msg");
                    planOrderNo = jsonObject.getString("planOrderNo");
                    if (result) {
                        if (template.equals("AA-5")) {
                            bloodOrders = new ArrayList<BloodProductBean2>();
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<BloodProductBean2>>() {
                            }.getType();
                            bloodOrders.clear();
                            bloodOrders = gson.fromJson(jsonArray.toString(), type);
                            if (!bloodOrders.isEmpty()) {
                                goTo(template, status, null, planOrderNo, bloodOrders.get(0));
                            } else {
                                ShowToast(getString(R.string.dataexception));
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        if (msg != null) {
                            ShowToast(msg);
                        }
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(PatientAct.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(PatientAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    /**
     * @param template
     * @param status
     * @param orderItemBean
     * @param planorderNo   :only goto bloodsugar need it
     */
    private void goTo(String template, String status, OrderItemBean orderItemBean, String planorderNo,
                      BloodProductBean2 bloodProductBean2) {
        if (template.equals("AA-1")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
            bundle.putSerializable(GlobalConstant.KEY_ORDERITEMBEAN, orderItemBean);
            if (status.equals("0")) {
                bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow
                        .MEDICINE_INTRODUCTION);
            } else if (status.equals("1")) {
                bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
            }
            intent.putExtras(bundle);
            intent.setClass(PatientAct.this, FlowAct.class);
            startActivity(intent);

        } else if (template.equals("AA-3")) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.KEY_PATIENT, patient);
            if (status.equals("0")) {
                intent.putExtra(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.INJECTIONSITE);
            } else if (status.equals("1")) {
                intent.putExtra(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
            }
            intent.putExtra("orderItemBean", orderItemBean);
            intent.setClass(PatientAct.this, InsulinFlowAct.class);
            startActivity(intent);
        } else if (template.equals("AA-4")) {
            Intent intent = new Intent();
            if (status.equals("0")) {
                intent.putExtra(GlobalConstant.KEY_PATIENT, patient);
                intent.putExtra(GlobalConstant.KEY_PLANORDERNO, planorderNo);
                intent.setClass(PatientAct.this, BloodSugarActivity.class);
            }
            startActivity(intent);

        } else if (template.equals("AA-5")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
            bundle.putSerializable(GlobalConstant.KEY_BLOODPRODUCTBEAN2, bloodProductBean2);
            if (status.equals("0")) {
                bundle.putSerializable(GlobalConstant.KEY_FLAG_SCAN, GlobalConstant.VALUE_FLAG_SCAN);
            } else if (status.equals("1")) {
                bundle.putSerializable(GlobalConstant.KEY_FLAG_SCAN_PATROL, GlobalConstant.VALUE_FLAG_SCAN_PATROL);
            }
            intent.putExtras(bundle);
            intent.setClass(PatientAct.this, TransfusionActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {

        if (result.contains("||") || result.contains("z")) {
            loadData(patient.getPatId(), result, UserInfo.getUserName());
        } else {
            loadBlood(patient.getPatId(), result, UserInfo.getUserName());
        }
    }
}
