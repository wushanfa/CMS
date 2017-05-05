package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * Created by Zyy on 2016/3/12.
 * 类说明：已完成输血列表界面
 */
public class TransfusionExcutedAct extends ABToolBarActivity{
private SyncPatientBean patient=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(true);
        setContentView(R.layout.activity_transfusioed);
        getData();
        initToolBar();
        intialFragment();
    }

    private void getData(){
        Bundle bundle=this.getIntent().getExtras();
        patient= (SyncPatientBean) bundle.getSerializable(GlobalConstant.KEY_PATIENT);
    }

    private void initToolBar() {
        setToolBarDrawable(new int[]{R.drawable.act_home_workbtn,
                R.drawable.act_home_notesbtn, R.drawable.act_home_historybtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setValid(true);
        setCheck(2);
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
//                        Intent intent=new Intent();
//                        Bundle bundle=new Bundle();
//                        bundle.putSerializable(GlobalConstant.KEY_PATIENT,patient);
//                        intent.putExtras(bundle);
//                        intent.setClass(TransfusionExcutedAct.this, TransfusionActivity.class);
//                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void intialFragment(){
        TransfusionedFra transfusionedFra=new TransfusionedFra();
        Bundle bundle=new Bundle();
        bundle.putSerializable(GlobalConstant.KEY_PATIENT,patient);
        transfusionedFra.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,transfusionedFra,null).commit();
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root= (RelativeLayout) findViewById(R.id.root_transfusioned);
        SupportDisplay.resetAllChildViewParam(root);
    }

}
