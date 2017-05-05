package com.gentlehealthcare.mobilecare.activity.bloodsugar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * @author Zyy
 * @date 2015-11-9下午4:27:08
 * @category 血糖测试
 */
public class BloodSugarActivity extends ABToolBarActivity {
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 身份核对
     */
    private BloodSugarIdentifyScanFra mBloodSugarIdentifyScanFra = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar);
        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
        //初始化底部导航栏
        initToolBar();
        if (getIntent().hasExtra(GlobalConstant.KEY_PLANORDERNO)) {
            String planorderNo = getIntent().getStringExtra(GlobalConstant.KEY_PLANORDERNO);
            BloodSugarFra bloodSugarFra = new BloodSugarFra(patient, planorderNo);
            //血糖记录界面
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, bloodSugarFra).commit();
        } else {
            mBloodSugarIdentifyScanFra = new BloodSugarIdentifyScanFra(patient);
            // 身份核对界面
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                    mBloodSugarIdentifyScanFra).commit();
        }
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment instanceof BloodSugarIdentifyScanFra) {
            mBloodSugarIdentifyScanFra.DoCameraResult(result);
        } else if (fragment instanceof BloodSugarListFra) {
            BloodSugarListFra bloodSugarListFra = (BloodSugarListFra) fragment;
            if (!bloodSugarListFra.isShow()) {
                bloodSugarListFra.setCurrentPlanNo(result);
                bloodSugarListFra.refreshMedicineList();
                bloodSugarListFra.setPosition(result);
            }
        }
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_bloodsugar);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void initToolBar() {
        setToolBarDrawable(new int[]{R.drawable.act_home_workbtn,
                R.drawable.act_home_notesbtn, R.drawable.act_home_historybtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setValid(true);
        setCheck(0);
        setAbDoToolBar(new ABDoToolBar() {

            @Override
            public void onRightBtnClick() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLeftBtnClick() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onCheckedChanged(int i) {
                switch (i) {
                    case 0:
                        // 工作界
                        break;
                    case 1:
                        // 交接事项
//					startActivity(new Intent(TransfusionActivity.this,
//							WorkAct.class));
                        break;
                    case 2:
                        //系统提示界面
//					startActivity(new Intent(TransfusionActivity.this,
//							SystemAttentionAct.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
