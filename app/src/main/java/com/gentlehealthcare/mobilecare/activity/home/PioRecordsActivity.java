package com.gentlehealthcare.mobilecare.activity.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadPioRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.gentlehealthcare.mobilecare.view.adapter.PioRecordAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * PIO记录显示界面
 */
public class PioRecordsActivity extends BaseActivity implements OnTouchListener, OnClickListener,
        OnItemLongClickListener, PullToRefreshBase.OnRefreshListener, OnItemClickListener {
    private final static String TAG = "PIO";
    private ListView listView;
    private ProgressDialog progressDialog = null;
    private SyncPatientBean patient = new SyncPatientBean();
    private List<SyncPatientBean> patients = new ArrayList<SyncPatientBean>();
    private List<LoadPioRecordBean> data = new ArrayList<LoadPioRecordBean>();// PIO记录数据
    private PioRecordAdapter adapter = null;
    public PullToRefreshListView pullToRefreshListView = null;
    @ViewInject(R.id.tv_head)
    private TextView tv_head;
    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.btn_search)
    private LinearLayout btn_search;
    @ViewInject(R.id.btn_add)
    private LinearLayout btn_add;

    private LinearLayout activityGoBackMain;

    /**
     * 日期选择
     */
    private DatePicker datePicker;
    /**
     * 日期选择
     */
    private Button btn_date_sure;

    private String searchTime = DateTool.getCurrDateTimeS();

    private Thread myThread = null;
    // 手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    // 手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    // 记录手指按下时的横坐标。
    private float xDown;

    // 记录手指移动时的横坐标。
    private float xMove;

    // 用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    private MyPatientDialog dialog;

    private int whichPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pio_records);
        ViewUtils.inject(this);
        HidnGestWindow(true);

        progressDialog = new ProgressDialog(PioRecordsActivity.this);
        progressDialog.setMessage("获取PIO数据中,请稍后...");

//        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
//        patients = (List<SyncPatientBean>) getIntent().getSerializableExtra("allpatients");

        syncDeptPatientTable(0);
        initView();
        listView.setAdapter(adapter = new PioRecordAdapter(data, PioRecordsActivity.this, patient));
        /** 数据加载*/
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    tv_head.setText(patient.getName() + "护理记录");
                }
            }
        };
        myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

        });
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.activitygobackmain);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DoLoadPioRecordsReq();
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(PioRecordsActivity.this, ErrorAct.class);
        startActivity(intent);
    }

    private void initView() {
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_pio_records);
        listView = pullToRefreshListView.getRefreshableView();
        listView.setSelector(R.color.transparent);
        listView.setOnItemLongClickListener(this);
        pullToRefreshListView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        btn_add.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        activityGoBackMain = (LinearLayout) findViewById(R.id.activitygobackmain);
        activityGoBackMain.setOnTouchListener(PioRecordsActivity.this);
        tv_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                dialogDatePicker();
                break;
            case R.id.btn_add:
                String logTime = DateTool.getCurrDateTimeS();
                Intent intent = new Intent(PioRecordsActivity.this, AddPioRecActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("logTime", logTime);
                startActivity(intent);
                break;
            case R.id.tv_head:
                dialog = new MyPatientDialog(PioRecordsActivity.this, new MyPatientDialog.MySnListener() {

                    @Override
                    public void myOnItemClick(View view, int position, long id) {
                        patient = patients.get(position);
                        tv_head.setText(patient.getName() + "护理记录");
                        whichPatients = position;
                        DoLoadPioRecordsReq();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!data.get(position - 1).getAppraisal().isEmpty()) {
            Toast.makeText(PioRecordsActivity.this, "已评价的记录不可修改", Toast.LENGTH_LONG).show();
        } else {
            String logTime = DateTool.getCurrDateTimeS();
            Intent intent = new Intent(PioRecordsActivity.this, ModifyPioRecActivity.class);
            intent.putExtra("pio", data.get(position - 1));
            intent.putExtra("logTime", logTime);
            intent.putExtra("patient", patient);
            intent.putExtra("flag", 1);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        DoLoadPioRecordsReq();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PioRecordsActivity.this, PioShowActivity.class);
        LoadPioRecordBean perPio = data.get(position - 1);
        intent.putExtra("pio", perPio);
        intent.putExtra("patient", patient);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 弹出日期选择对话框
     */
    protected void dialogDatePicker() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker);
        dialog.setCancelable(true);

        datePicker = (DatePicker) dialog.findViewById(R.id.dpPicker);
        btn_date_sure = (Button) dialog.findViewById(R.id.btn_date_sure);

        btn_date_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker
                        .getDayOfMonth();
                /**根据选中时间条件重新加载数据*/
                DoLoadPioRecordsReq();
                dialog.dismiss();
            }
        });

        datePicker.init(DateTool.getCurrentTimeByOrder(searchTime, 0),
                DateTool.getCurrentTimeByOrder(searchTime, 1),
                DateTool.getCurrentTimeByOrder(searchTime, 2), null);

        dialog.show();
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
            patient = patients.get(flag);
            myThread.start();
            DoLoadPioRecordsReq();
        } else {
            Toast.makeText(getApplication(), "没有找到改病人信息", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载PIO记录请求
     */
    public void DoLoadPioRecordsReq() {
        progressDialog.show();
        PatMajorInfoBean patMajorInfoBean = new PatMajorInfoBean();
        patMajorInfoBean.setPatId(patient.getPatId());
        patMajorInfoBean.setSearchTime(searchTime);
        CCLog.i(TAG, "DoLoadPioRecordsReq>" + UrlConstant.LoadPioRecords() + patMajorInfoBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadPioRecords() + patMajorInfoBean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String result = responseInfo.result;
                        CCLog.i(TAG, "PIO记录获取成功");
                        pullToRefreshListView.onRefreshComplete();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LoadPioRecordBean>>() {
                        }.getType();
                        List<LoadPioRecordBean> list = gson.fromJson(result, type);
                        data.clear();
                        if (list != null && list.size() > 0) {
                            data.addAll(list);
                        } else {
                            Toast.makeText(getApplicationContext(), "暂无护理记录!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CCLog.i(TAG, "PIO记录失败");
                        progressDialog.dismiss();
                        pullToRefreshListView.onRefreshComplete();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(PioRecordsActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                // 活动的距离
                int distanceX = (int) (xMove - xDown);
                // 获取顺时速度
                int xSpeed = getScrollVelocity();
                // 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                    // 设置切换动画，从右边进入，左边退出
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 同步部门病人
     */
    public void syncDeptPatientTable(final int flag) {
        String str = GroupInfoSave.getInstance(PioRecordsActivity.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString()
                , new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CCLog.i(TAG, "PIO列表数据获取成功");
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
                    tv_head.setText(patient.getName() + "护理记录");
                    DoLoadPioRecordsReq();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "PIO列表数据获取失败");
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(PioRecordsActivity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });
    }

}
