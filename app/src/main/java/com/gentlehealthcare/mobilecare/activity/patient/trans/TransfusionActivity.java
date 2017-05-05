package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.TranafusionPresenter;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.FlowsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zyy
 * @类说名 输血界面
 * @date 2015-9-14下午2:47:57
 */
public class TransfusionActivity extends ABToolBarActivity implements
        OnClickListener {
    private final static String TAG = "TransfusionActivity";
    /**
     * 左侧流程列
     */
    private ListView lv_right;
    /**
     * 流程
     */
    private String[] flows = {"身份", "说明", "输血", "巡视", "记录", "冲管"};
    /**
     * 流程容器
     */
    private List<Map<String, String>> maps;
    /**
     * 左侧流程视图
     */
    private ViewGroup.MarginLayoutParams lvParams;
    /**
     * 左侧流程视图显示宽度
     */
    private int marginRight;
    /**
     * 左侧流程是否显示
     */
    private boolean isVisible = false;
    /**
     * 输血frangment
     */
    private TrasIdentifyScanFra trasIdentifyScanFra = null;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 返回按钮是否返回通知列表界面
     */
    private boolean isBackNotifivationList = false;
    private TranafusionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfusion);
        presenter=new TranafusionPresenter();
        patient = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        initWidget();
        initFlows();
        initToolBar();
        showWitchOne();
    }

    private void showWitchOne() {
        if (getIntent().hasExtra(GlobalConstant.KEY_FLAG_SCAN)) {
            String str = getIntent().getStringExtra(GlobalConstant.KEY_FLAG_SCAN);
            if (str.equals(GlobalConstant.VALUE_FLAG_SCAN)) {
                String type = getIntent().getStringExtra(GlobalConstant.KEY_FLAG);
                if (GlobalConstant.KEY_PLANBARCODE.equals(type)) {
                    String reqId = getIntent().getStringExtra(GlobalConstant.KEY_REQID);
                    TransfusionFra fragment = new TransfusionFra(patient);
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalConstant.KEY_REQID, reqId);
                    bundle.putString(GlobalConstant.KEY_EXECUTE_PATROL,GlobalConstant.VALUE_FLAG_EXECUTE);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
                } else if (GlobalConstant.KEY_BLOODDONORCODE.equals(type)) {
                    BloodProductBean2 bloodProductBean2 = (BloodProductBean2) getIntent().getSerializableExtra
                            (GlobalConstant.KEY_BLOOD);
                    ThreeEightCheckFragment threeEightCheckFragment = new ThreeEightCheckFragment(patient,
                            bloodProductBean2,null);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                            threeEightCheckFragment).commit();
                }
            }
        } else if (getIntent().hasExtra(GlobalConstant.KEY_FLAG_SCAN_PATROL)) {
            String str = getIntent().getStringExtra(GlobalConstant.KEY_FLAG_SCAN_PATROL);
            if (str.equals(GlobalConstant.VALUE_FLAG_SCAN_PATROL)) {
               String type = getIntent().getStringExtra(GlobalConstant.KEY_FLAG);
                if (GlobalConstant.KEY_PLANBARCODE.equals(type)) {
                    String reqId = getIntent().getStringExtra(GlobalConstant.KEY_REQID);
                    TransfusionFra fragment = new TransfusionFra(patient);
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalConstant.KEY_REQID, reqId);
                    bundle.putString(GlobalConstant.KEY_EXECUTE_PATROL,GlobalConstant.VALUE_FLAG_PATROL);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
                } else if (GlobalConstant.KEY_BLOODDONORCODE.equals(type)) {
                    BloodProductBean2 bloodProductBean2 = (BloodProductBean2) getIntent().getSerializableExtra
                            (GlobalConstant.KEY_BLOOD);
                    TrasPatrolFra trasPatrolFra = new TrasPatrolFra(patient, bloodProductBean2);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, trasPatrolFra).commit();
                }
            }

        }
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_trans);
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
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                        intent.putExtras(bundle);
                        intent.setClass(TransfusionActivity.this, TransfusionExcutedAct.class);
                        if (patient != null) {
                            startActivity(intent);
                            finish();
                        } else {
                            ShowToast(getResources().getString(R.string.dataexception));
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 初始化UI组件
     */
    private void initWidget() {
        lv_right = (ListView) findViewById(R.id.lv_right);
    }

    /**
     * 初始化流程说明 flows[i]对应2中状态 checked & normal
     */
    private void initFlows() {
        maps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < flows.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", flows[i]);
            if (i == 0) {
                map.put("state", "checked");
            } else {
                map.put("state", "normal");
            }
            maps.add(map);
        }

        lvParams = (ViewGroup.MarginLayoutParams) lv_right.getLayoutParams();

        lv_right.setAdapter(new FlowsAdapter(this, maps));

        lv_right.postDelayed(lvRunnable, 1000);

        lv_right.setOnTouchListener(new MyTouchListener());
    }

    /**
     * 子线程减少lv的显示宽度为原来*1/4
     */
    Runnable lvRunnable = new Runnable() {
        @Override
        public void run() {
            marginRight = -lv_right.getWidth() / 4 * 3;
            lvParams.rightMargin = marginRight;
            lv_right.setLayoutParams(lvParams);
        }
    };

    @Override
    public void onClick(View arg0) {

    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        presenter.commRec(UserInfo.getUserName(), result, DeviceTool.getDeviceId(this));
        android.support.v4.app.Fragment currentFra = getSupportFragmentManager().findFragmentById(R.id
                .fl_container);
        if (currentFra instanceof ThreeEightCheckFragment) {
            ThreeEightCheckFragment threeEightCheckFragment = (ThreeEightCheckFragment) currentFra;
            threeEightCheckFragment.setScanResult(result);
        }
    }

    /**
     * 匹配条码规则
     */
    public boolean partternCode(String strMatch, String strPattern) {
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strMatch);
        boolean b = m.matches();
        if (b) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @author Zyy
     * @类说名 左侧流程栏触摸事件
     */
    class MyTouchListener implements View.OnTouchListener {
        private float xUp;
        private float xDown;
        private float yUp;
        private float yDown;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xDown = event.getX();
                    yDown = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    xUp = event.getX();
                    yUp = event.getY();
                    if (Math.abs(yUp - yDown) < 100) {
                        if (xUp - xDown > 50) {
                            if (isVisible) {
                                isVisible = false;
                                lvParams.rightMargin = marginRight;
                                lv_right.setLayoutParams(lvParams);
                            }
                        }
                        if (xDown - xUp > 50) {
                            if (!isVisible) {
                                isVisible = true;
                                lvParams.rightMargin = 0;
                                lv_right.setLayoutParams(lvParams);
                            }
                        }
                    }

                    break;
            }

            return false;
        }
    }

}
