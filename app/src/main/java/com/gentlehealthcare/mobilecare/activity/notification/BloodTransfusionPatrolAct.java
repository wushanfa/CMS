package com.gentlehealthcare.mobilecare.activity.notification;

import android.content.Intent;
import android.os.Bundle;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：通知过来输血巡视界面
 */
public class BloodTransfusionPatrolAct extends BaseActivity{
    /**
     * 病人数据
     */
    private SyncPatientBean patient=null;
    /**
     *血品id
     */
    private String  bloodId=null;
    /**
     * 输血巡视观察界面
     */
    private BloodTransfusionpatrallookFra bloodTransfusionpatrallookFra=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(false);
        setContentView(R.layout.activity_bloodtransfusionpatrol);
        getData();
        intialFragment();
    }

    private void getData(){
        Intent intent=getIntent();
        patient= (SyncPatientBean) intent.getSerializableExtra(GlobalConstant.KEY_PATIENT);
        bloodId=intent.getStringExtra(GlobalConstant.KEY_BLOODID);
    }

    private void intialFragment(){
        if(bloodTransfusionpatrallookFra==null){
            bloodTransfusionpatrallookFra=new BloodTransfusionpatrallookFra();
        }
        if(bloodId!=null&&patient!=null){
            Bundle bundle=new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT,patient);
            bundle.putString(GlobalConstant.KEY_BLOODID,bloodId);
            bloodTransfusionpatrallookFra.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,bloodTransfusionpatrallookFra).commit();
        }else{
            ShowToast(getString(R.string.dataexception));
        }

    }

    @Override
    protected void resetLayout() {

    }
}
