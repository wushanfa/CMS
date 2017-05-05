package com.gentlehealthcare.mobilecare.activity.notification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：用于在通知过后扫描患者身份
 */
public class StatusScanningAct extends BaseActivity implements View.OnClickListener {
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
     * 流程模版id
     */
    private String templateId = null;
    /**
     * 医嘱Id
     */
    private String planOrderNo = null;
    /**
     * 结束or巡视；默认结束
     */
    private String endOrPatrol = null;
    /**
     * 长按人工扫描按钮
     */
    private Button btnScan;
    /**
     * PatCode输入框
     */
    private AlertDialog patCodeDialog = null;
    /**
     *血袋号
     */
    private String bloodId=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_scaning);
        HidnGestWindow(false);
        getData();
        intialSource();
    }

    private void intialSource() {

        tv_tras_bed = (TextView) findViewById(R.id.tv_tras_bed);
        tv_tras_bed.setText(getResources().getString(R.string.bed_label) + patient.getBedLabel());
        tv_tras_name = (TextView) findViewById(R.id.tv_tras_name);
        tv_tras_name.setText(getResources().getString(R.string.patient_name) + patient.getName());
        // 返回&主页按钮
        imbtn_home = (ImageButton) findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
        btnScan = (Button) findViewById(R.id.btn_right_scan);
        btnScan.setOnClickListener(this);
        btnScan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                createInputPatcodeDialog();
                return false;
            }
        });
    }

    private void createInputPatcodeDialog() {
        final EditText editText = new EditText(StatusScanningAct.this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        editText.setLayoutParams(layoutParams);
        editText.setText(patient.getPatCode());
        editText.setHint(R.string.inputpatientcode);
        patCodeDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.notification)).setView(editText).setCancelable(true).setNegativeButton(getResources().getString(R.string.make_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText().equals(null)) {
                    ShowToast(getResources().getString(R.string.inputpatientcode));
                } else {
                    if (editText.getText().toString().trim().equals(patient.getPatCode())) {
                        statusJudgement(patient.getPatCode());
                        patCodeDialog.dismiss();
                    }
                }
            }
        }).setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                patCodeDialog.dismiss();
            }
        }).show();
    }

    /**
     * 从通知页面过来携带四个参数：patient & AA-? & planolderNo &end or patrol
     */
    private void getData() {
        if (patient == null) {
            patient = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        }

        templateId = getIntent().getStringExtra(GlobalConstant.KEY_TEMPLATEID);
        endOrPatrol = getIntent().getStringExtra(GlobalConstant.KEY_NOTICECLASS);
        if(templateId.equals("AA-1")){
            planOrderNo = getIntent().getStringExtra(GlobalConstant.KEY_PLANORDERNO);
        }else if(templateId.equals("AA-5")){
            bloodId=getIntent().getStringExtra(GlobalConstant.KEY_BLOODID);
        }

    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        statusJudgement(result);
    }

    /**
     * 判断身份核对
     *
     * @param patcode
     */
    private void statusJudgement(String patcode) {
        if (patcode.equals(patient.getPatCode())) {
            if (templateId.equals("AA-1")) {
                CCLog.i("AA-1");
                if (endOrPatrol.equals("complete")) {
                    CCLog.i("endOrPatrol.equals(\"Complete\")");
                    if (patient != null && planOrderNo != null) {
                        CCLog.i("patient!=null&&planOrderNo!=null");
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANORDERNO, planOrderNo);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                        intent.putExtras(bundle);
                        intent.setClass(StatusScanningAct.this, IntravenouslyEndAct.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } else {
                    if (patient != null && planOrderNo != null) {
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANORDERNO, planOrderNo);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                        intent.putExtras(bundle);
                        intent.setClass(StatusScanningAct.this, IntravenouslyPatrolAct.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                }
            } else if (templateId.equals("AA-5")) {
                if (endOrPatrol.equals("complete")) {
                    if (patient != null && bloodId != null) {
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_BLOODID, bloodId);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                        intent.putExtras(bundle);
                        intent.setClass(StatusScanningAct.this, BloodTransfusionEndAct.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } else {
                    if (patient != null && bloodId != null) {
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_BLOODID, bloodId);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                        intent.putExtras(bundle);
                        intent.setClass(StatusScanningAct.this, BloodTransfusionPatrolAct.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                }
            }
        } else {
            ShowToast(getString(R.string.failtocheckpatient));
        }
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_status_scan);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(StatusScanningAct.this, Notification2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imbtn_home:
                Intent intent2 = new Intent();
                intent2.setClass(StatusScanningAct.this, HomeAct.class);
                startActivity(intent2);
                finish();
                break;
            default:
                break;

        }
    }
}
