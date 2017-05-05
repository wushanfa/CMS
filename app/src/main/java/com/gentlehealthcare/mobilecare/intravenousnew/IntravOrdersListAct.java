package com.gentlehealthcare.mobilecare.intravenousnew;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药待执行、执行中、已执行
 */
public class IntravOrdersListAct extends BaseActivity implements View.OnClickListener {

    private Button excuteWait, excuteing, excuted, back;
    private LinearLayout home;
    private IntravWaitExcuteFra intravWaitExcuteFra;
    private IntravExcuteingFra intravExcuteingFra;
    private IntravExcutedFra intravExcutedFra;
    private SyncPatientBean syncPatientBean;
    private String eventId=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        acceptData();
        intial();
        intialFra();
    }

    private void intial() {
        excuteWait = (Button) findViewById(R.id.btn_wait_excute);
        excuteWait.setOnClickListener(this);
        excuteing = (Button) findViewById(R.id.btn_excuteing);
        excuteing.setOnClickListener(this);
        excuted = (Button) findViewById(R.id.btn_excuted);
        excuted.setOnClickListener(this);
        back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        home = (LinearLayout) findViewById(R.id.ll_home);
        home.setOnClickListener(this);
    }

    private void intialFra(){
        Bundle bundle=new Bundle();
        bundle.putSerializable(GlobalConstant.KEY_PATIENT, syncPatientBean);
        bundle.putString(GlobalConstant.KEY_EVENTID,eventId);
        intravWaitExcuteFra=new IntravWaitExcuteFra();
        intravWaitExcuteFra.setArguments(bundle);
        intravExcuteingFra=new IntravExcuteingFra();
        intravExcuteingFra.setArguments(bundle);
        intravExcutedFra=new IntravExcutedFra();
        intravExcutedFra.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,intravWaitExcuteFra).addToBackStack(null).commit();

    }

    private void acceptData(){
        syncPatientBean=new SyncPatientBean();
        syncPatientBean.setPatId("74457");
        //静脉给药开始时使用
        eventId=getIntent().getStringExtra(GlobalConstant.KEY_EVENTID);
        //syncPatientBean= (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wait_excute:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,intravWaitExcuteFra).addToBackStack(null).commit();
                break;
            case R.id.btn_excuteing:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,intravExcuteingFra).addToBackStack(null).commit();

                break;
            case R.id.btn_excuted:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,intravExcutedFra).addToBackStack(null).commit();

                break;
            case R.id.btn_back:
                break;
            case R.id.ll_home:
                break;
        }
    }
}
