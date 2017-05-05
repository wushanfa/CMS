package com.gentlehealthcare.mobilecare.intravenousnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药身份核对
 */
public class IntravIdentityCheckAct extends BaseActivity implements View.OnClickListener {
    private TextView tv_name, tv_patid, tv_admission_times, tv_bed_label, tv_age, tv_nursing_grades;
    private Button btnManualCheck, btnBack;
    private ImageView iv_image, iv_home;
    private SyncPatientBean patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_check);
        intial();
        getData();
    }

    @Override
    protected void resetLayout() {

    }

    private void intial() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_patid = (TextView) findViewById(R.id.tv_patid);
        tv_admission_times = (TextView) findViewById(R.id.tv_admission_times);
        tv_bed_label = (TextView) findViewById(R.id.tv_bed_label);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_nursing_grades = (TextView) findViewById(R.id.tv_nursing_grades);
        btnManualCheck = (Button) findViewById(R.id.btn_manual_check);
        btnManualCheck.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_manual_check:
                toIntravOrdersListAct(null);
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
        intent.setClass(IntravIdentityCheckAct.this, HomeAct.class);
        startActivity(intent);
        finish();
    }

    private void toIntravOrdersListAct(String eventId) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
        bundle.putString(GlobalConstant.KEY_EVENTID, eventId);
        intent.setClass(IntravIdentityCheckAct.this, IntravOrdersListAct.class);
        startActivity(intent);
        finish();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        patient = (SyncPatientBean) bundle.getSerializable(GlobalConstant.KEY_PATIENT);
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        loadCurrentPatient(UserInfo.getUserName(), result);
    }

    /**
     * 扫描加载当前的病人
     */
    public void loadCurrentPatient(String username, String patCode) {
        final ProgressDialog pd = ProgressDialog.show(this, null, getResources().getString(R.string.dataloading));
        CCLog.i(getResources().getString(R.string.HomeActLoadCurrentPatient), UrlConstant.loadCurrentPatient() +
                username + "&patCode=" + patCode + "&wardCode=null");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadCurrentPatient() + username + "&patCode=" +
                patCode + "&wardCode=null", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                SyncPatientBean patient = new SyncPatientBean();
                JSONObject jsonobject = null;
                try {
                    jsonobject = new JSONObject(responseInfo.result);
                    String eventId = (String) jsonobject.get("eventId");
                    if (patient != null) {
                        toIntravOrdersListAct(eventId);
                    } else {
                        ShowToast(getString(R.string.pleasemanualcheck));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        ShowToast(getResources().getString(R.string.fialtoloadpatient));
                    } else {
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                toErrorAct();
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(IntravIdentityCheckAct.this, ErrorAct.class);
        startActivity(intent);
    }
}
