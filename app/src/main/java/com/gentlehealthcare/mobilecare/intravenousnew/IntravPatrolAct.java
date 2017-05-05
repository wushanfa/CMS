package com.gentlehealthcare.mobilecare.intravenousnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gentlehealthcare.mobilecare.net.bean.RecInspectBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药巡视界面
 */
public class IntravPatrolAct extends BaseActivity implements View.OnClickListener{

    private TextView tv_time, tv_medicineinfo, tv_order_qd, tv_order_speed, tv_order_method, tv_order_tool, tv_get_medicine_time, tv_patrol_time;
    private Button btn_intravenous_perform_yes, btn_back,btnSave;
    private LinearLayout ll_medicine_time;
    private ImageView iv_home;
    private EditText edtRecord;
    private SyncPatientBean patient;
    private String planorderno=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intravpatrol);
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
        tv_patrol_time = (TextView) findViewById(R.id.tv_get_medicine_patrol_time);

        btn_intravenous_perform_yes = (Button) findViewById(R.id.btn_intravenous_perform_yes);
        btn_back = (Button) findViewById(R.id.btn_back);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        btnSave= (Button) findViewById(R.id.btn_save);
        edtRecord= (EditText) findViewById(R.id.edt_record_patrol);

    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        patient= (SyncPatientBean) bundle.getSerializable(GlobalConstant.KEY_PATIENT);
        OrderItemBean orderItemBean = (OrderItemBean) bundle.getSerializable(GlobalConstant.KEY_ORDERITEMBEAN);
        tv_time.setText(orderItemBean.getEventStartTime());
        tv_medicineinfo.setText(orderItemBean.getOrderText());
        tv_order_qd.setText(orderItemBean.getFrequency());
        tv_order_speed.setText(orderItemBean.getSpeed() + "滴/分");
        tv_order_method.setText(orderItemBean.getAdministration());
        tv_order_tool.setText(orderItemBean.getInjectionTool());
        planorderno=orderItemBean.getPlanOrderNo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String msg=edtRecord.getText().toString();
                if(msg.isEmpty()){
                    ShowToast("填写巡视记录后保存");
                }else{
                    recordIntravPatrolMsg(planorderno,msg);
                }

                break;
            case R.id.btn_intravenous_perform_yes:
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_home:
                toHomeAct();
                break;
        }
    }

    private void toHomeAct() {
        Intent intent = new Intent();
        intent.setClass(IntravPatrolAct.this, HomeAct.class);
        startActivity(intent);
        finish();
    }

    /**
     * 记录巡视msg
     */
    private void recordIntravPatrolMsg(String planorderno,String rec) {
        RecInspectBean bean = new RecInspectBean();
        bean.setPatId(patient.getPatId());
        bean.setPlanOrderNo(planorderno);
        bean.setPerformDesc(rec);
        bean.setUsername(UserInfo.getName());
        //CCLog.i(TAG, "记录巡视内容-> " + UrlConstant.GetPatrol(0) + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetPatrol(0) + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject mJSONObject = null;
                        boolean result = false;
                        try {
                            mJSONObject = new JSONObject(responseInfo.result);
                            result = mJSONObject.getBoolean("result");
                            if (result) {
                                ShowToast("巡视记录保存成功");
                                tv_patrol_time.setText("巡视时间:" + DateTool.getCurrDateTimeS());
                                tv_patrol_time.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            if (LinstenNetState.isLinkState(getApplicationContext())) {
                                Toast.makeText(IntravPatrolAct.this, getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            } else {
                                toErrorAct();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(IntravPatrolAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(IntravPatrolAct.this, ErrorAct.class);
        startActivity(intent);
    }
}
