package com.gentlehealthcare.mobilecare.activity.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.CustomEditTextDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：通知过来静脉给药巡视界面
 */
public class IntravenouslyPatrolAct extends BaseActivity implements OnClickListener {
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
     * 病人信息
     */
    private SyncPatientBean patient = null;
    /**
     * 医嘱时间
     */
    private TextView tvTime;
    /**
     * 医嘱信息
     */
    private TextView tvQd;
    /**
     * 医嘱滴速
     */
    private TextView tvSpeed;
    /**
     * 医嘱静滴
     */
    private TextView tvType;
    /**
     * 医嘱内容
     */
    private TextView tvContext;
    /**
     * 观察按钮
     */
    private Button btnLook;
    /**
     * 当前医嘱id
     */
    private String planOlderNo = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<OrderItemBean> orders = null;
    /**
     * 注射时间
     */
    private TextView tvInjectionTime;
    /**
     * 巡视时间
     */
    private TextView tvVisitTime;
    /**
     * 等待时间
     */
    private TextView tvWaitTime;
    /**
     * 异常输入框
     */
    private CustomEditTextDialog customEditTextDialog = null;
    /**
     * 延迟2s
     */
    private final int SPLASH_DISPLAY_LENGHT = 1000 * 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(false);
        setContentView(R.layout.act_intravenouslypatrol);
        getData();
        createOrders();
        intialSource();
        loadData(planOlderNo);
    }

    private void getData() {
        Intent intent = getIntent();
        patient = (SyncPatientBean) intent.getSerializableExtra(GlobalConstant.KEY_PATIENT);
        planOlderNo = intent.getStringExtra(GlobalConstant.KEY_PLANORDERNO);
    }

    private void intialSource() {

        tv_tras_bed = (TextView) findViewById(R.id.tv_tras_bed);
        tv_tras_bed.setText(getString(R.string.bed) + patient.getBedLabel());
        tv_tras_name = (TextView) findViewById(R.id.tv_tras_name);
        tv_tras_name.setText(getString(R.string.patient_name) + patient.getName());
        // 返回&主页按钮
        imbtn_home = (ImageButton) findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
        //医嘱信息
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvQd = (TextView) findViewById(R.id.tv_xuedaihao);
        tvSpeed = (TextView) findViewById(R.id.tv_xuexing);
        tvType = (TextView) findViewById(R.id.tv_xueliang);
        tvContext = (TextView) findViewById(R.id.tv_medicineinfo);
        //时间信息
        tvInjectionTime = (TextView) findViewById(R.id.tv_injectiontime);
        tvVisitTime = (TextView) findViewById(R.id.tv_visitingtime);
        tvWaitTime = (TextView) findViewById(R.id.tv_waittime);
        btnLook = (Button) findViewById(R.id.btn_patrolsure);
        btnLook.setOnClickListener(this);

    }

    private void componentAssignments() {
        if (!orders.isEmpty()) {
            if (orders.get(0).getEventStartTime().equals(null)) {
                tvTime.setText("");
            } else {
                tvTime.setText(orders.get(0).getEventStartTime());
            }
            if (orders.get(0).getFrequency().equals(null)) {
                tvQd.setText("");
            } else {
                tvQd.setText(orders.get(0).getFrequency());
            }
            if (orders.get(0).getSpeed().equals("")) {
                tvSpeed.setText("");
            } else {
                tvSpeed.setText(orders.get(0).getSpeed());
            }
            if (orders.get(0).getAdministration().equals(null)) {
                tvType.setText("");
            } else {

                tvType.setText(orders.get(0).getAdministration());
            }
            if (orders.get(0).getOrderText().equals(null)) {
                tvContext.setText("");
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < orders.size(); i++) {
                    stringBuffer.append(orders.get(i).getOrderText() + "\n");
                }
                tvContext.setText(stringBuffer);
            }
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }

        tvInjectionTime.setText(orders.get(0).getEventStartTime());
        tvVisitTime.setText(DateTool.getCurrDateTime());

    }

    private void delayedJump() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(IntravenouslyPatrolAct.this, Notification2Activity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    private void createOrders() {
        if (orders == null) {
            orders = new ArrayList<OrderItemBean>();
        }
    }

    private void loadData(String planOlderNo) {
        CCLog.i("巡视界面加载静脉给药医嘱信息>>>" + UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=1" + "&planOrderNo=" + planOlderNo);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=1" + "&planOrderNo=" + planOlderNo, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    jsonArray = jsonObject.getJSONArray("msg");
                    if (result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderItemBean>>() {
                        }.getType();
                        orders.clear();
                        orders = gson.fromJson(jsonArray.toString(), type);
                        componentAssignments();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(IntravenouslyPatrolAct.this)) {
                        Toast.makeText(IntravenouslyPatrolAct.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(IntravenouslyPatrolAct.this)) {
                    Toast.makeText(IntravenouslyPatrolAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(IntravenouslyPatrolAct.this, ErrorAct.class);
        startActivity(intent);
    }

    private void createEditDialog() {
        if (customEditTextDialog == null) {
            customEditTextDialog = new CustomEditTextDialog(IntravenouslyPatrolAct.this, R.style.myDialogTheme);
        }
        customEditTextDialog.setContent(new String[]{"记录内容:"});
        customEditTextDialog.setTitle(getResources().getString(R.string.notification));
        customEditTextDialog.setLeftButton(getResources().getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(View view) {
                customEditTextDialog.dismiss();
            }
        });
        customEditTextDialog.setRightButton(getResources().getString(R.string.make_sure), new OnClickListener() {
            @Override
            public void onClick(View view) {
                String exception = customEditTextDialog.getText()[0];
                recInspectResult(planOlderNo, exception);
                customEditTextDialog.dismiss();
            }
        });
        customEditTextDialog.show();
    }

    private void recInspectResult(String planOlderNo, String performDesc) {
        CCLog.i("静脉给药界面记录观察" + UrlConstant.GetCompleteExecute(1) + "?patId=" + patient.getPatId() + "&username=" + UserInfo.getUserName() + "&planOrderNo=" + planOlderNo + "&performDesc=" + performDesc);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetCompleteExecute(1) + "?patId=" + patient.getPatId() + "&username=" + UserInfo.getUserName() + "&planOrderNo=" + planOlderNo + "&performDesc=" + performDesc, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonobject = null;
                try {
                    jsonobject = new JSONObject(responseInfo.result);
                    Boolean result = jsonobject.getBoolean("result");

                    if (result) {
                        ShowToast(getResources().getString(R.string.patrolrecordssuccessed));
                        delayedJump();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(IntravenouslyPatrolAct.this)) {
                        Toast.makeText(IntravenouslyPatrolAct.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(IntravenouslyPatrolAct.this)) {
                    Toast.makeText(IntravenouslyPatrolAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });

    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_intr_patrol);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(IntravenouslyPatrolAct.this, Notification2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imbtn_home:
                Intent intent2 = new Intent();
                intent2.setClass(IntravenouslyPatrolAct.this, HomeAct.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_patrolsure:
                createEditDialog();
                break;
            default:
                break;
        }
    }
}
