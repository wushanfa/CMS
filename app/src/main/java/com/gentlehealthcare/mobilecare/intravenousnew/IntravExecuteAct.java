package com.gentlehealthcare.mobilecare.intravenousnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.StartInfusionBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药执行界面
 */
public class IntravExecuteAct extends BaseActivity implements View.OnClickListener {

    private TextView tv_time, tv_medicineinfo, tv_order_qd, tv_order_speed, tv_order_method, tv_order_tool, tv_get_medicine_time, tv_patrol_time;
    private Button btn_intravenous_perform_yes, btn_back;
    private LinearLayout ll_medicine_time;
    private ImageView iv_home;
    private SyncPatientBean patient = null;
    private String planorderno;
    private String injecTool;
    private String speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intravexcute);
        intialSource();
        getData();
    }

    @Override
    protected void resetLayout() {

    }

    private void intialSource() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_medicineinfo = (TextView) findViewById(R.id.tv_medicineinfo);
        tv_order_qd = (TextView) findViewById(R.id.tv_order_qd);
        tv_order_speed = (TextView) findViewById(R.id.tv_order_speed);
        tv_order_method = (TextView) findViewById(R.id.tv_order_method);
        tv_order_tool = (TextView) findViewById(R.id.tv_order_tool);
        tv_get_medicine_time = (TextView) findViewById(R.id.tv_get_medicine_time);
        tv_patrol_time = (TextView) findViewById(R.id.tv_patrol_time);

        btn_intravenous_perform_yes = (Button) findViewById(R.id.btn_intravenous_perform_yes);
        btn_back = (Button) findViewById(R.id.btn_back);
        iv_home = (ImageView) findViewById(R.id.iv_home);

    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        patient = (SyncPatientBean) bundle.getSerializable(GlobalConstant.KEY_PATIENT);
        OrderItemBean orderItemBean = (OrderItemBean) bundle.getSerializable(GlobalConstant.KEY_ORDERITEMBEAN);
        tv_time.setText(orderItemBean.getPlanStartTime());
        tv_medicineinfo.setText(orderItemBean.getOrderText());
        tv_order_qd.setText(orderItemBean.getFrequency());
        tv_order_speed.setText(orderItemBean.getSpeed() + "滴/分");
        tv_order_method.setText(orderItemBean.getAdministration());
        tv_order_tool.setText(orderItemBean.getInjectionTool());
        injecTool = orderItemBean.getInjectionTool();
        speed = orderItemBean.getSpeed();
        planorderno = orderItemBean.getPlanOrderNo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                break;
            case R.id.btn_intravenous_perform_yes:
                if (injecTool.isEmpty() || speed.isEmpty()) {
                    if (injecTool.isEmpty()) {
                        showSkinResult();
                    } else if (speed.isEmpty()) {

                    }

                } else {
                    startMedicine(planorderno, injecTool, speed);
                }
                break;
            case R.id.iv_home:
                break;
        }
    }

    public void showSkinResult() {
        String[] strs = new String[]{"钢针", "留置针"};
        AlertDialog alert = new AlertDialog.Builder(IntravExecuteAct.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("选择注射工具")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            injecTool = "0";
                        } else if (i == 1) {
                            injecTool = "1";
                        }
                    }
                }).create();
        alert.show();
        alert.getWindow().setLayout(680, 650);
    }

    /**
     * 开始给药
     */
    private void startMedicine(String planorderno, String injecTool, String speed) {
        StartInfusionBean bean = new StartInfusionBean();
        bean.setPatId(patient.getPatId());
        bean.setInjectionTool(injecTool);
        bean.setPlanOrderNo(planorderno);
        if (speed != null) {
            bean.setSpeed(speed.split("滴")[0]);
        }
        // CCLog.i(TAG, "开始给药-> " + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.StartInfusion2() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject mJSONObject = null;
                        boolean status = false;
                        try {
                            mJSONObject = new JSONObject(responseInfo.result);
                            status = mJSONObject.getBoolean("result");
                            if (status) {
                                ShowToast(getString(R.string.startdelivery));
                                // loadInspectionTime();
                            } else {
                                ShowToast(getString(R.string.deliveryfailed));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(IntravExecuteAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(IntravExecuteAct.this, ErrorAct.class);
        startActivity(intent);
    }
}
