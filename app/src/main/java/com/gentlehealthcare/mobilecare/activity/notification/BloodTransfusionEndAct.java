package com.gentlehealthcare.mobilecare.activity.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
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
 * 类说明：通知过来输血结束界面
 */
public class BloodTransfusionEndAct extends BaseActivity implements View.OnClickListener {
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
     * 输血结束按钮
     */
    private Button btnBloodEnd;
    /**
     * 血品id
     */
    private String bloodId = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<BloodProductBean2> orders = null;
    /**
     * 输血开始时间
     */
    private TextView tvBloodStartTime;
    /**
     * 输血结束时间
     */
    private TextView tvBloodEndTime;
    /**
     * 结束输血提示框
     */
    private AlertDialogTwoBtn endAlertDialogTwoBtn = null;
    /**
     * 延迟2s
     */
    private final int SPLASH_DISPLAY_LENGHT = 1000 * 4;
    /**
     * 结束时间布局
     */
    private LinearLayout llBloodEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(false);
        setContentView(R.layout.act_bloodtransfusionend);
        getData();
        createOrders();
        loadData(bloodId);
        intialSource();
    }


    private void intialSource() {

        tv_tras_bed = (TextView) findViewById(R.id.tv_tras_bed);
        tv_tras_bed.setText(getString(R.string.bed_label) + patient.getBedLabel());
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
        tvBloodStartTime = (TextView) findViewById(R.id.tv_bloodstarttime);
        tvBloodEndTime = (TextView) findViewById(R.id.tv_bloodendtime);
        llBloodEndTime = (LinearLayout) findViewById(R.id.ll_bloodendtime);
        btnBloodEnd = (Button) findViewById(R.id.btn_patrolsure);
        btnBloodEnd.setOnClickListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        patient = (SyncPatientBean) intent.getSerializableExtra(GlobalConstant.KEY_PATIENT);
        bloodId = intent.getStringExtra(GlobalConstant.KEY_BLOODID);
    }

    private void createOrders() {
        if (orders == null) {
            orders = new ArrayList<BloodProductBean2>();
        }
    }

    private void componentAssignments() {
        if (!orders.isEmpty()) {
            if (orders.get(0).getTransDate().equals(null)) {
                tvTime.setText("");
            } else {
                tvTime.setText(orders.get(0).getTransDate());
            }
            if (orders.get(0).getBloodId().equals(null)) {
                tvQd.setText("");
            } else {
                tvQd.setText(orders.get(0).getBloodId());
            }
            if (orders.get(0).getBloodCapacity().equals("")) {
                tvSpeed.setText("");
            } else {
                tvSpeed.setText(orders.get(0).getBloodCapacity() + orders.get(0).getUnit());
            }
            if (orders.get(0).getBloodGroup().equals(null)) {
                tvType.setText("");
            } else {
                tvType.setText(orders.get(0).getBloodGroup());
            }
            if (orders.get(0).getBloodTypeName().equals(null)) {
                tvContext.setText("");
            } else {
                tvContext.setText(orders.get(0).getBloodTypeName());
            }
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }
        tvBloodStartTime.setText(orders.get(0).getTransDate());
    }

    private void loadData(String bloodId) {
        CCLog.i("输血结束界面加载医嘱信息>>>", UrlConstant.loadBloodNoticeDetail() + UserInfo.getUserName() + "&bloodId=" +
                bloodId + "&patId=" + patient.getPatId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadBloodNoticeDetail() + UserInfo.getUserName() +
                "&bloodId=" + bloodId + "&patId=" + patient.getPatId(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    if (result) {
                        jsonArray = jsonObject.getJSONArray("msg");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<BloodProductBean2>>() {
                        }.getType();
                        orders.clear();
                        orders = gson.fromJson(jsonArray.toString(), type);
                        componentAssignments();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(BloodTransfusionEndAct.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(BloodTransfusionEndAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(BloodTransfusionEndAct.this, ErrorAct.class);
        startActivity(intent);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_blood_trans_patrol);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void delayedJump() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(BloodTransfusionEndAct.this, Notification2Activity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    private void endDialog() {
        if (endAlertDialogTwoBtn == null) {
            endAlertDialogTwoBtn = new AlertDialogTwoBtn(BloodTransfusionEndAct.this);
        }
        endAlertDialogTwoBtn.setTitle(getResources().getString(R.string.notification));
        endAlertDialogTwoBtn.setMessage(getResources().getString(R.string.endblood));
        endAlertDialogTwoBtn.setLeftButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endAlertDialogTwoBtn.dismiss();
            }
        });
        endAlertDialogTwoBtn.setRightButton(getResources().getString(R.string.make_sure), new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                compliteTransfusion();
                endAlertDialogTwoBtn.dismiss();
            }
        });
        endAlertDialogTwoBtn.show();
    }

    private void returnReceipt() {
        llBloodEndTime.setVisibility(View.VISIBLE);
        tvBloodEndTime.setText(DateTool.getCurrDateTime());
        delayedJump();
    }

    /**
     * 完成输血
     */
    private void compliteTransfusion() {
        BloodProductBean2 bloodProductBean2 = null;
        if (orders != null) {
            bloodProductBean2 = new BloodProductBean2();
            bloodProductBean2 = orders.get(0);
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
            return;
        }
        String url = UrlConstant.CompliteTransfusion() + UserInfo.getUserName() + "&patId=" + patient
                .getPatId() + "&applyNo=" + bloodProductBean2.getApplyNo() + "&itemNo=" + bloodProductBean2
                .getItemNo() + "&bloodId=" + bloodProductBean2.getBloodId() + "&planOrderNo=" +
                bloodProductBean2.getPlanOrderNo() + "&status=1";
        CCLog.i("完成输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            JSONObject mJsonObject = null;
            boolean result = false;
            JSONArray jsonArray = null;

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(BloodTransfusionEndAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    mJsonObject = new JSONObject(arg0.result);
                    result = mJsonObject.getBoolean("result");
                    if (result) {
                        ShowToast(getString(R.string.endbloodsuccessed));
                        returnReceipt();
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(BloodTransfusionEndAct.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(BloodTransfusionEndAct.this, Notification2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imbtn_home:
                Intent intent2 = new Intent();
                intent2.setClass(BloodTransfusionEndAct.this, HomeAct.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_patrolsure:
                endDialog();
                break;
            default:
                break;
        }
    }
}
