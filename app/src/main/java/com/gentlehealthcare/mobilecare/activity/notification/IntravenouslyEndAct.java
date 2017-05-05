package com.gentlehealthcare.mobilecare.activity.notification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：通知过来静脉给药结束界面
 */
public class IntravenouslyEndAct extends BaseActivity{
    private IntravenouslyEndFra intravenouslyEndFra=null;
    private SyncPatientBean patient=null;
    private String planOlderNo=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(false);
        setContentView(R.layout.activity_intravenouslyend);
        getData();
        intialFragment();
    }

    private void intialFragment(){
        if(intravenouslyEndFra==null){
            intravenouslyEndFra=new IntravenouslyEndFra();
        }
        if(planOlderNo!=null&&patient!=null){
            Bundle bundle=new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT,patient);
            bundle.putString(GlobalConstant.KEY_PLANORDERNO,planOlderNo);
            intravenouslyEndFra.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,intravenouslyEndFra).commit();
        }else{
            ShowToast(getString(R.string.dataexception));
        }

    }

    private void getData(){
        Intent intent=getIntent();
        patient= (SyncPatientBean) intent.getSerializableExtra(GlobalConstant.KEY_PATIENT);
        planOlderNo=intent.getStringExtra(GlobalConstant.KEY_PLANORDERNO);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root= (RelativeLayout) findViewById(R.id.root_trans);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
