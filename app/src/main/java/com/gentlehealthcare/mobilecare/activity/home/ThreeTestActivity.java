package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestParamBean;
import com.gentlehealthcare.mobilecare.net.bean.ModifyThreeTestBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.ThreeTestPutIntoBean;
import com.gentlehealthcare.mobilecare.view.adapter.MyPagerAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.ThreeTestAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体温
 *
 * @author dzw
 */
public class ThreeTestActivity extends BaseActivity implements OnClickListener, AdapterView
        .OnItemClickListener, OnPageChangeListener, View.OnTouchListener {
    private static final String TAG = "ThreeTestAct";
    private static final int ITEM_CLICK = 1;
    private static final int SCAN = 2;
    private static final int NORMAL = 3;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager viewpager;
    private MyPagerAdapter viewAdapter;
    public ThreeTestCurrentTimeFragment currentF = new ThreeTestCurrentTimeFragment();
    public ThreeTestPastTimeFragment pastF = new ThreeTestPastTimeFragment();

    private ProgressDialog progressDialog = null;
    @ViewInject(R.id.tv_head)
    private TextView tv_head;

    private LinearLayout ll_time;

    /**
     * 返回,add
     */
    private Button btn_back;
    private LinearLayout btn_save;

//    @ViewInject(R.id.ib_search)
//    private Button ib_search;

    /**
     * 右侧列表
     */
    private ThreeTestAdapter adapter = null;// 病人列表适配器
    private ListView lv_patients;// 病人列表空间
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();// 病人数据
    private String patId = "";// 当前病人ID

    /**
     * time
     */
    private TextView et_date;

    private int arrive_year = 0;
    private int arrive_month = 0;
    private int arrive_day = 0;
    private int arrive_time = 0;
    private View dateView;
    private DatePicker datepick;
    private NumberPicker numberpick;
    private String myLongDate = new String();
    private String myDate = new String();

    /**
     * keyborad
     */
    private GestureDetector gestureDetector;
    private final int UP = 0;
    private final int DOWN = 1;
    public ListView myListView;

    /**
     * pagenumber
     */
    private TextView page_number;

    /**
     * 加载体温单
     */
    private List<LoadThreeTestBean> loadtts = new ArrayList<LoadThreeTestBean>();
    private EditText et_tiwen, et_huxi, et_maibo, et_pingfen, et_xueyashousuo7,
            et_xueyashuzhang7, et_xueyashousuo11, et_xueyashuzhang11,
            et_xueyashousuo15, et_xueyashuzhang15, et_xueyashousuo19,
            et_xueyashuzhang19, et_add1, et_add2, et_weight, et_dabian1,
            et_dabian2, et_xiaobian1, et_xiaobian2, et_chushuiliang,
            et_rushuiliang, et_xuantian, et_xuantian2;
    private Button guomintishi, add1, add2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_threetest);
        ViewUtils.inject(ThreeTestActivity.this);
        progressDialog = new ProgressDialog(this);
        HidnGestWindow(true);
        syncDeptPatientTable();
        patients.addAll(UserInfo.getDeptPatient());

        arrive_year = DateTool.getCurrYear();
        arrive_month = DateTool.getCurrMonth();
        arrive_day = DateTool.getCurrDay();
        arrive_time = getSixTime(DateTool.getCurrHour());
        myDate = DateTool.appendDate(arrive_year, arrive_month, arrive_day, arrive_time);
        myLongDate = DateTool.appendDateDouleDay(arrive_year, arrive_month, arrive_day);

        initView();
        initFragment();
        initPager();
        tv_head.setText("生命体征");
        /**
         * 获得位置,ontouchlistener监听
         */
        gestureDetector = new GestureDetector(ThreeTestActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                        float x = e2.getX() - e1.getX();
                        float y = e2.getY() - e1.getY();

                        if (y < 0) {
                            // doResult(UP);
                        } else if (y > 0) {
                            // doResult(DOWN);
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
        /**
         * 病人列表
         */
        if (patients.size() > 0) {
            patId = patients.get(0).getPatId();
        }
        adapter = new ThreeTestAdapter(patients, this);
        lv_patients.setAdapter(adapter);
        lv_patients.setOnItemClickListener(this);
        viewpager.setOnPageChangeListener(this);

        LoadThreeTestByRecordingTime(false);
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_threetest);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 初始化
     */
    private void initView() {
        viewpager = (ViewPager) ThreeTestActivity.this
                .findViewById(R.id.vPager);
        lv_patients = (ListView) findViewById(R.id.lv_patients);
        et_date = (TextView) findViewById(R.id.et_date);
        et_date.setText(DateTool.appendDate(arrive_year, arrive_month,
                arrive_day, arrive_time));
//        et_date.setOnClickListener(this);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_time.setOnClickListener(this);
        page_number = (TextView) findViewById(R.id.page_number);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_save = (LinearLayout) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

    }

    private void initFragment() {
        fragments.add(currentF);
        fragments.add(pastF);
    }

    private void initPager() {
        viewAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(viewAdapter);
    }

    private void initDateAndTime() {
        dateView = View.inflate(ThreeTestActivity.this, R.layout.datepickdialog, null);
        datepick = (DatePicker) dateView.findViewById(R.id.new_act_date_picker);
        numberpick = (NumberPicker) dateView.findViewById(R.id.new_act_number_picker);
        /**
         * initDate
         */
        int year = 0;
        int month = 0;
        int day = 0;
        if (et_date.getText().toString().isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = arrive_year;
            month = arrive_month;
            day = arrive_day;
        }
        datepick.init(year, (month - 1), day, null);
        /**
         * initTime
         */
        int hour = 0;
        int time = 0;

        if (et_date.getText().toString().isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            time = hour;
//            time = getTime(hour);
        } else {
            time = arrive_time;
//            time = getTime(arrive_time);
        }
        String[] nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16",
                "17", "18", "19", "20", "21", "22", "23"};
        numberpick.setDisplayedValues(nums);
        numberpick.setMinValue(0);
        numberpick.setMaxValue(nums.length - 1);
        numberpick.setValue(time);
        numberpick.setWrapSelectorWheel(true);
    }

    /**
     * 切换病人，保存修改，重新加载数据
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ModifyThreeTest(ITEM_CLICK, position);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 0) {
            et_date.setText(myDate);
            page_number.setBackgroundResource(R.drawable.threetest_medical_records_page_first);

        } else {
            et_date.setText(myDate);
            page_number.setBackgroundResource(R.drawable.threetest_medical_records_page_second);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time:
                initDateAndTime();
                DateAlertDialog(datepick, numberpick);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                ModifyThreeTest(NORMAL, 0);
                break;
            default:
                break;
        }
    }

    /**
     * 自定义键盘的滑动事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 时间提示框
     *
     * @param datepick
     * @param numberpick
     */
    public void DateAlertDialog(final DatePicker datepick, final NumberPicker numberpick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeTestActivity.this, AlertDialog
                .THEME_HOLO_LIGHT);
        builder.setView(dateView);
        builder.setTitle("请选择时间");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrive_year = datepick.getYear();
                arrive_month = datepick.getMonth() + 1;
                arrive_day = datepick.getDayOfMonth();

                arrive_time = numberpick.getValue();
                myDate = DateTool.appendDate(arrive_year, arrive_month, arrive_day, arrive_time);
                myLongDate = DateTool.appendDateDouleDay(arrive_year, arrive_month, arrive_day);
                et_date.setText(myDate);
                LoadThreeTestByRecordingTime(true);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 修改
     */
    public void ModifyThreeTest(final int status, final int position) {
        progressDialog.setMessage("正在保存...");
        progressDialog.show();

        Map<String, ModifyThreeTestBean> modC = currentF.getModifyMap();
        Map<String, ModifyThreeTestBean> modP = pastF.getModifyMap();

        modC.putAll(modP);

        StringBuffer orgVsId = new StringBuffer();
        StringBuffer vitalSigns = new StringBuffer();
        StringBuffer vitalSignsValues = new StringBuffer();
        StringBuffer vitalSignsValues2 = new StringBuffer();
        StringBuffer recordingTimes = new StringBuffer();

        for (Map.Entry<String, ModifyThreeTestBean> entry : modC.entrySet()) {
            orgVsId.append(entry.getValue().getOrgVsId());
            orgVsId.append("<<");
            vitalSigns.append(entry.getValue().getVitalSigns());
            vitalSigns.append("<<");
            vitalSignsValues.append(entry.getValue().getVitalSignsValues());
            vitalSignsValues.append("<<");
            vitalSignsValues2.append(entry.getValue().getVitalSignsValues2());
            vitalSignsValues2.append("<<");
            String my = new String();
            String nd = myDate;
            String allTime = DateTool.toGetComplete(nd);
            String s = new String();
            if (entry.getKey().contains("DP") || entry.getKey().contains("SP")) {
                my = entry.getKey().substring(2) + ":00";
                s = allTime.substring(0, 10);
                recordingTimes.append(s + " " + my);
                recordingTimes.append("<<");
            } else {
                recordingTimes.append(myDate);
                recordingTimes.append("<<");
            }
        }
        if (vitalSignsValues.toString().equals("") && vitalSignsValues2.toString().equals("")) {
            progressDialog.dismiss();
            if (status == ITEM_CLICK) {
                patId = patients.get(position).getPatId();
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
            } else if (status == SCAN) {
                patId = patients.get(position).getPatId();
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
                lv_patients.setSelection(position);
            }
            LoadThreeTestByRecordingTime(true);
        } else {
            CCLog.e(TAG, vitalSignsValues + "--" + vitalSignsValues2);
            ModifyThreeTestBean mtt = new ModifyThreeTestBean();
            mtt.setLogBy(UserInfo.getUserName());
            mtt.setRecordingTime(recordingTimes.toString());
            mtt.setPatId(patId);
            mtt.setOrgVsId(orgVsId.toString());
            mtt.setVitalSigns(vitalSigns.toString());
            mtt.setVitalSignsValues(vitalSignsValues.toString());
            mtt.setVitalSignsValues2(vitalSignsValues2.toString());

            String url = UrlConstant.modifyThreeTest() + mtt.toString();
            CCLog.i(TAG, "threeTestActivity ModifyThreeTest>" + url);
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String result = responseInfo.result;
                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    ModifyThreeTestBean mod = gson.fromJson(result, ModifyThreeTestBean.class);
                    if (mod.getResult().equals("success")) {
                        Toast.makeText(ThreeTestActivity.this, "提交成功.", Toast.LENGTH_SHORT).show();
                        if (status == ITEM_CLICK) {
                            patId = patients.get(position).getPatId();
                            adapter.setSelectPosition(position);
                            adapter.notifyDataSetChanged();
                        } else if (status == SCAN) {
                            patId = patients.get(position).getPatId();
                            adapter.setSelectPosition(position);
                            adapter.notifyDataSetChanged();
                            lv_patients.setSelection(position);
                        } else if (status == NORMAL) {

                        }
                        currentF.myclear();
                        pastF.myClear();
                    } else if (mod.getResult().equals("noData")) {
                        Toast.makeText(ThreeTestActivity.this, "请先录入数据再保存", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ThreeTestActivity.this, "提交失败.", Toast.LENGTH_SHORT).show();
                    }
                    LoadThreeTestByRecordingTime(true);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    progressDialog.dismiss();
                    CCLog.i(TAG, "threeTest数据保存失败");
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(ThreeTestActivity.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }

                }
            });
        }
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(ThreeTestActivity.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 根据查询时间查询病人信息
     */
    public void LoadThreeTestByRecordingTime(boolean isNotFirst) {
        progressDialog.setMessage("正在获取体温单数据");
        progressDialog.show();
        if (isNotFirst) {
            pastF.myClear();
            currentF.myclear();
        }
        LoadThreeTestParamBean param = new LoadThreeTestParamBean();
        param.setRecordingTime(myDate);
        param.setPatId(patId);
        String url = UrlConstant.loadThreeTest() + param.toString();
        CCLog.i(TAG, "threeTestActivity LoadThreeTestByRecordingTime>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<List<LoadThreeTestBean>>() {
                }.getType();
                List<LoadThreeTestBean> list = gson.fromJson(result, type);

                    if (!loadtts.isEmpty()) {
                        loadtts.clear();
                    }
                    loadtts.addAll(list);
                    Map<String, LoadThreeTestBean> m = new HashMap<String, LoadThreeTestBean>();
                    for (int i = 0; i < loadtts.size(); i++) {
                        ThreeTestPutIntoBean.putTTIntoMap(loadtts.get(i), m);
                    }
                    currentF.setList(m);
                    pastF.setList(m, 0, 0, false, false);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();
                CCLog.i(TAG, "threeTest数据request失败");
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(ThreeTestActivity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }

            }
        });
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: syncDeptPatientTable
     * @Description: 同步部门病人
     */
    public void syncDeptPatientTable() {
        progressDialog.setMessage("正在获取病人列表");
        progressDialog.show();
        String str = GroupInfoSave.getInstance(ThreeTestActivity.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        CCLog.i(TAG, "threeTestActivity syncDeptPatientTable>" + UrlConstant.GetSyncDeptPatient() +
                syncDeptPatientBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean
                .toString()
                , new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
                patients.clear();
                patients.addAll(UserInfo.getDeptPatient());
                adapter.notifyDataSetChanged();
                patId = patients.get(0).getPatId();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(ThreeTestActivity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });
    }

    private int getTime(int hour) {
        int index = 0;
        if (hour > 0 && hour <= 3)
            index = 0;
        else if (hour <= 7 && hour > 3) {
            index = 1;
        } else if (hour <= 11 && hour > 7) {
            index = 2;
        } else if (hour <= 15 && hour < 11) {
            index = 3;
        } else if (hour <= 19 && hour > 15) {
            index = 4;
        } else if (hour <= 23 && hour > 19) {
            index = 5;
        } else {
            index = 0;
        }
        return index;
    }

    private int getSixTime(int hour) {
        int index = 0;
        if (hour > 0 && hour <= 3)
            index = 3;
        else if (hour <= 7 && hour > 3) {
            index = 7;
        } else if (hour <= 11 && hour > 7) {
            index = 11;
        } else if (hour <= 15 && hour < 11) {
            index = 15;
        } else if (hour <= 19 && hour > 15) {
            index = 19;
        } else if (hour <= 23 && hour > 19) {
            index = 23;
        } else {
            index = 3;
        }
        return index;
    }

    private int getNumberTime(int value) {
        int hour = 0;
        if (value == 0) {
            hour = 3;
        } else if (value == 1) {
            hour = 7;
        } else if (value == 2) {
            hour = 11;
        } else if (value == 3) {
            hour = 15;
        } else if (value == 4) {
            hour = 19;
        } else {
            hour = 23;
        }
        return hour;
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        CCLog.i(TAG, TAG + "扫描的结果:" + result);
        boolean isGetResult = false;
        int flag = 0;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatCode().equals(result)) {
                isGetResult = true;
                flag = i;
                break;
            }
        }
        if (isGetResult) {
            ModifyThreeTest(SCAN, flag);
        } else {
            Toast.makeText(getApplication(), "没有找到改病人信息", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public MyPagerAdapter getAdapter() {
        return viewAdapter;
    }
}
