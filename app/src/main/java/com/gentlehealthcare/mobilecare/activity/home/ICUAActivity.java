package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBTotalBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuOrderBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuOrderResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUABean;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.ICUDataMethod;
import com.gentlehealthcare.mobilecare.tool.ICUResourceSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.MySwitchNameDialog;
import com.gentlehealthcare.mobilecare.view.adapter.GridViewICUAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MyPagerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICUAActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "ICUActivity";
    public ICUAFirstFragment icuAFirst = new ICUAFirstFragment();
    public ICUASecondFragment icuASecond = new ICUASecondFragment();
    public ICUAThirdFragment icuAThird = new ICUAThirdFragment();
    public ViewPager view;
    public MyPagerAdapter pageAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    /**
     * 标题
     */
    private TextView tv_head;

    private final static int REQUEST_CODE = 123;
    // 时间
    private TextView et_date;
    private int arrive_year = 0;
    private int arrive_month = 0;
    private int arrive_day = 0;
    private int arrive_hour = 0;
    private int arrive_min = 0;
    private View dateView;
    private DatePicker dataePick;
    private TimePicker timePick;
    private String myLongDate = new String();
    private Button searchICUByTime;

    /**
     * 向viewpager中传递的数据
     */
    private static LoadIcuOrderResultBean orders = new LoadIcuOrderResultBean();
    private int whichPager = 0;

    /**
     * 获取icu B字典表数据
     */
    private LoadIcuBTotalBean icuBItem = new LoadIcuBTotalBean();

    /**
     * 切换用户
     */
    @ViewInject(R.id.ll_icu_patient_layout)
    private LinearLayout ll_icu_patient_layout;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_lable;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();
    private MySwitchNameDialog nameDialog;
    private int whichPatient = 0;
    /**
     * 提示框
     */
    private ProgressDialog progressDialog;
    /**
     * go back
     */
    public Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_icu);
        ViewUtils.inject(this);

        progressDialog = new ProgressDialog(ICUAActivity.this);
        progressDialog.setMessage("加载医嘱信息");

        /** 同步病人，然后获取orders */
        syncDeptPatientTable();

        /** 后台获取icuB dictionary*/
        loadIntensiveCareBItemDictionary();

        initView();

        initFragment();

        initPager();

        ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant.SAVE_CONDITION);

        /** 把recordingTime存放到sp中 */
        Map<String, Object> recordingTime = new HashMap<String, Object>();
        recordingTime.put("recordingTime", DateTool.getCurrDateTimeS());
        ICUResourceSave.getInstance(ICUAActivity.this).save(recordingTime);

        et_date.setText(DateTool.getCurrDateTimeS());

        tv_head.setBackgroundResource(R.drawable.a_title_bar_switch);

        searchICUA();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ICUResourceSave.getInstance(ICUAActivity.this).clearAll();
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_icu);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void initView() {
        view = (ViewPager) findViewById(R.id.icu_vPager);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setOnClickListener(this);
        et_date = (TextView) findViewById(R.id.et_date);
        et_date.setOnClickListener(this);
        ll_icu_patient_layout.setOnClickListener(this);
        searchICUByTime = (Button) findViewById(R.id.ib_search);
        searchICUByTime.setOnClickListener(this);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    private void initFragment() {
        fragments.add(icuAFirst);
        fragments.add(icuASecond);
        fragments.add(icuAThird);
    }

    private void initPager() {
        pageAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        view.setOffscreenPageLimit(2);
        view.setAdapter(pageAdapter);
        view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                whichPager = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ICUBActivity.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                int returnWhichPatient = bundle.getInt("returnWhichPatient");
                whichPatient = returnWhichPatient;
                tv_parent.setText(patients.get(whichPatient).getName());
                if (patients.get(whichPatient).getBedLabel() != null) {
                    tv_bed_lable.setText(patients.get(whichPatient).getBedLabel() + "床");
                } else {
                    tv_bed_lable.setText("未分配");
                }
                /** 把patId放入sp中*/
                Map<String, Object> sp = new HashMap<String, Object>();
                sp.put("patId", patients.get(0).getPatId());
                ICUResourceSave.getInstance(ICUAActivity.this).save(sp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head:
                ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant.SAVE_CONDITION);
                Intent i = new Intent(ICUAActivity.this, ICUBActivity.class);
                i.putExtra("patients", (Serializable) patients);
                i.putExtra("whichPatient", whichPatient);
                i.putExtra("icuBItem", icuBItem);
                startActivityForResult(i, REQUEST_CODE);
                overridePendingTransition(R.anim.in_or_out_static, R.anim.in_or_out_static);
                break;
            case R.id.et_date:
                DateAlertDialog();
                break;
            case R.id.ll_icu_patient_layout:
                nameDialog = new MySwitchNameDialog(ICUAActivity.this, R.style.switchPatientsName, new
                        MySwitchNameDialog.mySwitchNameListener() {
                            @Override
                            public void myOnItemClick(View view, int position, long id, GridViewICUAdapter
                                    adapter, GridView gv_patient) {
                                tv_parent.setText(patients.get(position).getName());
                                if (patients.get(position).getBedLabel() != null) {
                                    tv_bed_lable.setText(patients.get(position).getBedLabel() + "床");
                                } else {
                                    tv_bed_lable.setText("未分配");
                                }

                                whichPatient = position;

                                String orgTime = (String) ICUResourceSave.getInstance(ICUAActivity.this).get
                                        ("recordingTime");

                                ICUResourceSave.getInstance(ICUAActivity.this).clearAll();
                                ICUCommonMethod.clearEditText();
                                ICUCommonMethod.clearButton(ICUAThirdFragment.getBtn_zhiliao(),
                                        ICUAThirdFragment.getBtn_yaowu(), ICUAThirdFragment.getBtn_xueye(),
                                        ICUAThirdFragment.getBtn_weichang(), ICUAThirdFragment
                                                .getBtn_bengruyaowu(), ICUAThirdFragment.getBtn_chuliang());
                                ICUDataMethod.reStartUpdateA();
                                ICUDataMethod.reStartSaveA();
                                ICUDataMethod.reStartSearchA();

                                /** 把patId放入sp中*/
                                Map<String, Object> sp = new HashMap<String, Object>();
                                sp.put("patId", patients.get(position).getPatId());
                                sp.put("recordingTime", orgTime);
                                ICUResourceSave.getInstance(ICUAActivity.this).save(sp);
                                ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant
                                        .SAVE_CONDITION);

                                adapter.setWhichOne(position);
                                adapter.notifyDataSetChanged();
                                gv_patient.setSelection(position);

                                loadIntensiveCareOrder();

                                nameDialog.dismiss();

                                searchICUA();
                            }

                        }, patients, whichPatient);
                nameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nameDialog.show();
                nameDialog.getWindow().setLayout(720, 500);
                break;
            case R.id.ib_search:
                ICUCommonMethod.clearEditText();
                ICUCommonMethod.clearButton(ICUAThirdFragment.getBtn_zhiliao(), ICUAThirdFragment.getBtn_yaowu(),
                        ICUAThirdFragment.getBtn_xueye(), ICUAThirdFragment.getBtn_weichang(),
                        ICUAThirdFragment.getBtn_bengruyaowu(), ICUAThirdFragment.getBtn_chuliang());
                ICUDataMethod.reStartUpdateA();
                ICUDataMethod.reStartSaveA();
                ICUDataMethod.reStartSearchA();
                searchICUA();
                loadIntensiveCareOrder();
                break;
            case R.id.btn_back:
                ICUResourceSave.getInstance(ICUAActivity.this).clearAll();
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 时间提示框
     */
    public void DateAlertDialog() {

        dateView = View.inflate(ICUAActivity.this, R.layout.datetimepickdialog, null);

        dataePick = (DatePicker) dateView.findViewById(R.id.new_act_date_picker);

        timePick = (TimePicker) dateView.findViewById(R.id.new_act_time_picker);

        String recordingTime = (String) ICUResourceSave.getInstance(ICUAActivity.this).get("recordingTime");
        Calendar calendar = Calendar.getInstance();
        if (recordingTime != null) {
            if (!recordingTime.equals("")) {
                calendar = ICUCommonMethod.parseDateForIcu(recordingTime);
            }
        }
        /** initDate */
        ICUCommonMethod.initDate(calendar, dataePick);
        /**  initTime*/
        ICUCommonMethod.initTime(calendar, timePick);

        AlertDialog mDialog;
        Builder builder = new Builder(ICUAActivity.this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                myLongDate = ICUCommonMethod.getCurrentTime(myLongDate, arrive_year, arrive_month,
                        arrive_day, arrive_hour, arrive_min, dataePick, timePick);
                et_date.setText(myLongDate);
                /** 把recordingTime存放到sp中 */
                Map<String, Object> recordingTime = new HashMap<String, Object>();
                recordingTime.put("recordingTime", myLongDate);
                ICUResourceSave.getInstance(ICUAActivity.this).save(recordingTime);
                ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant.SAVE_CONDITION);

            }
        });
        builder.setNegativeButton("取消", null);
        mDialog = builder.create();
        mDialog.setView(dateView);
        mDialog.setTitle("请选择时间");

        mDialog.show();
    }

    /**
     * syncDeptPatientTable 同步病人列表
     */
    public void syncDeptPatientTable() {
        progressDialog.show();
        String str = GroupInfoSave.getInstance(ICUAActivity.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean bean = new SyncDeptPatientBean();
        bean.setWardCode(str);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + bean.toString(), new
                RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "医嘱查询数据获取成功");
                        String result = responseInfo.result;
                        result.replaceAll("null", "\"\"");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<SyncPatientBean>>() {
                        }.getType();
                        List<SyncPatientBean> list = gson.fromJson(result, type);
                        UserInfo.setDeptPatient(list);
                        patients.clear();
                        patients.addAll(UserInfo.getDeptPatient());
                        tv_parent.setText(patients.get(0).getName());
                        if (patients.get(0).getBedLabel() != null) {
                            tv_bed_lable.setText(patients.get(0).getBedLabel() + "床");
                        } else {
                            tv_bed_lable.setText("未分配");
                        }

                        /** 把patId放入sp中*/
                        Map<String, Object> sp = new HashMap<String, Object>();
                        sp.put("patId", patients.get(0).getPatId());
                        ICUResourceSave.getInstance(ICUAActivity.this).save(sp);

                        /** 获取order dict */
                        loadIntensiveCareOrder();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "医嘱查询数据获取异常");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(ICUAActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(ICUAActivity.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 加载关联医嘱
     */
    private void loadIntensiveCareOrder() {
        LoadIcuOrderBean bean = new LoadIcuOrderBean();
        bean.setPatId(patients.get(whichPatient).getPatId());
        bean.setStartDateTime(DateTool.getCurrDateTime());
        bean.setRepeatIndicator("1");
        CCLog.i(TAG, UrlConstant.loadICUOrders() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadICUOrders() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        Gson gson = new Gson();
                        Type type = new TypeToken<LoadIcuOrderResultBean>() {
                        }.getType();
                        orders = gson.fromJson(responseInfo.result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(ICUAActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });

    }

    /**
     * 加载icu b中dictionary
     */
    private void loadIntensiveCareBItemDictionary() {
        progressDialog.show();
        LoadIcuBTotalBean bean = new LoadIcuBTotalBean();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadIntensiveCareBItems() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<LoadIcuBTotalBean>() {
                        }.getType();
                        icuBItem = gson.fromJson(responseInfo.result, type);
                        CCLog.i(TAG, "加载icu B字典表成功");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "加载icu B字典表fail");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(ICUAActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 查询ICUA
     */
    private void searchICUA() {
        progressDialog.show();
        SearchICUABean bean = new SearchICUABean();
        bean.setPatId((String) ICUResourceSave.getInstance(ICUAActivity.this).get("patId"));
        String recordingTime = (String) ICUResourceSave.getInstance(ICUAActivity.this).get("recordingTime");
        if (recordingTime != null) {
            bean.setRecordingTime(recordingTime);
        } else {
            ICUCommonMethod.showDialogForRemindTime(ICUAActivity.this);
            return;
        }
        CCLog.i(TAG, "ICUAActivity-> " + UrlConstant.searchICUA() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUA() + bean.toString(), new
                RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressDialog.dismiss();
                        String result = responseInfo.result;
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, SearchICUABean>>() {
                        }.getType();
                        Map<String, SearchICUABean> temp = gson.fromJson(result, type);
                        int insertType = 0;
                        if (temp == null || temp.size() == 0) {
                            insertType = 1;
                        } else {
                            insertType = 2;
                        }
                        if (1 == insertType) {
                            Toast.makeText(ICUAActivity.this, "当前病人在查询时间没有ICU记录", Toast.LENGTH_SHORT).show();
                            ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant.SAVE_CONDITION);
                        } else if (2 == insertType) {
                            ICUAFirstFragment.putIntoEtFirst(temp);
                            ICUASecondFragment.putIntoEtSecond(temp);
                            ICUAThirdFragment.putIntoEtThird(temp);
                            ICUDataMethod.changeStorageStatus(ICUAActivity.this, GlobalConstant.UPDATE_CONDITION);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(ICUAActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });
    }

    public static LoadIcuOrderResultBean getOrders() {
        return orders;
    }
}
