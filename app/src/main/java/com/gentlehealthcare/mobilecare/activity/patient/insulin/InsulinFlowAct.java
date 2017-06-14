package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowLiFra;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowZhuFra;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.HelpAct;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteMedicineInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatrolBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.PatrolRequest;
import com.gentlehealthcare.mobilecare.tool.DesUtil;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.CustomEditTextDialog;
import com.gentlehealthcare.mobilecare.view.adapter.FlowsheetAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MyFragmentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;

/**
 * 胰岛素流程
 */
public class InsulinFlowAct extends ABToolBarActivity implements View.OnClickListener {

    private String[] fuctions = {"药", "注", "历"};

    private String[] flows = {"身份", "说明", "部位", "药物", "巡视"};

    private List<Map<String, String>> maps;

    private FlowsheetAdapter mAdapter;

    private InsulinFlowSheetFra insulinFlowSheetFra;

    private ViewGroup.MarginLayoutParams lvParams;

    private int marginRight;

    private boolean isVisible = false;

    private int Flowpath = 1;

    private SyncPatientBean patient;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;
    private ViewPager vp_flow;
    private RadioGroup radioGroup;

    private ListView lv_right;

    private ProgressDialog progressDialog = null;
    private AlertDialogOneBtn alertDialog = null;

    public Button getBtn_patientflow() {
        return btn_patientflow;
    }

    private Button btn_patientflow;
    private ImageView iv_backSheet;
    CustomEditTextDialog dialog;
    private FlowConstant.PatientFlow flow;
    private OrderItemBean orderItemBean;
    private String planId;


    @Override
    protected void onDestroy() {

        editor.putBoolean(patient.getPatId(), false);

        editor.commit();

        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_flow);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        initWidget();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.datasaving));
        patient = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
        flow = (FlowConstant.PatientFlow) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENTFLOW);
        orderItemBean = (OrderItemBean) getIntent().getSerializableExtra("orderItemBean");
        preferences = getSharedPreferences("NURSE_MOBILE", MODE_PRIVATE);
        planId = getIntent().getStringExtra("planId");
        editor = preferences.edit();
        editor.putBoolean(patient.getPatId(), true);
        editor.commit();
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
        insulinFlowSheetFra = new InsulinFlowSheetFra(patient, flow, orderItemBean);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent, insulinFlowSheetFra).commit();

        final List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(insulinFlowSheetFra);

        fragments.add(new FlowZhuFra());

        fragments.add(new FlowLiFra(patient, 1));

        vp_flow.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
        vp_flow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        lvParams = (ViewGroup.MarginLayoutParams) lv_right.getLayoutParams();

        lv_right.setAdapter(mAdapter = new FlowsheetAdapter(this, maps));

        lv_right.postDelayed(lvRunnable, 1000);

        lv_right.setOnTouchListener(new MyTouchListener());

        lv_right.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                alertDialog = new AlertDialogOneBtn(InsulinFlowAct.this);

                alertDialog.setTitle(flows[position] + "流程说明");

                alertDialog.setMessage("流程说明" + (position + 1));

                alertDialog.setButton("知道了");

                alertDialog.setButtonListener(false, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });
                return false;
            }
        });
        iv_backSheet = (ImageView) findViewById(R.id.iv_backSheet);
        iv_backSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_flowcontent);
                if (fragment instanceof InsulinFlowSheetFra)
                    ((InsulinFlowSheetFra) fragment).backSheet();
            }
        });

        setToolBarDrawable(new int[]{R.drawable.act_home_workbtn, R.drawable.act_home_notesbtn, R.drawable
                .act_home_historybtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setValid(true);
        setCheck(0);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int checkedId) {
                if (checkedId == 0) {
                    iv_backSheet.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,
                            insulinFlowSheetFra).commit();
                } else if (checkedId == 1) {
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent, new FlowZhuFra()
                    ).commit();
                } else {
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent, new FlowLiFra
                            (patient, 1)).commit();
                }


            }

            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                //跳转到帮助界面
                //startActivity(new Intent(InsulinFlowAct.this, HelpAct.class));
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        vp_flow = (ViewPager) findViewById(R.id.vp_flow);
        lv_right = (ListView) findViewById(R.id.lv_right);
        radioGroup = (RadioGroup) findViewById(R.id.rgp_bottom);
        btn_patientflow = (Button) findViewById(R.id.btn_patientflow);
        btn_patientflow.setVisibility(View.GONE);
        RadioButton button;

        for (int i = 0; i < radioGroup.getChildCount(); i++) {

            button = (RadioButton) radioGroup.getChildAt(i);

            button.setId(i);

            button.setText(fuctions[i]);
        }

        radioGroup.check(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                vp_flow.setCurrentItem(checkedId);
                if (checkedId == 0) {
                    iv_backSheet.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,
                            insulinFlowSheetFra).commit();
                } else if (checkedId == 1) {
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent, new FlowZhuFra()
                    ).commit();
                } else {
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent, new FlowLiFra
                            (patient, 1)).commit();
                }
            }
        });
        vp_flow.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                radioGroup.getChildAt(i).performClick();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        findViewById(R.id.btn_back).setOnClickListener(this);
        btn_patientflow.setOnClickListener(this);
        findViewById(R.id.btn_patient_help).setOnClickListener(this);
        findViewById(R.id.btn_patient_search).setOnClickListener(this);
    }


    /**
     * @param @param flow
     * @param @param medicine
     * @return void
     * @throws
     * @Title: updateFlow
     * @Description: 更新给药进度
     */
    public void updateFlow(FlowConstant.PatientFlow flow) {
        changeFlowsColor(flow);
        int j = 0;
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).get("state").equals("checked")) {
                Flowpath = i + 1;
            } else if (maps.get(i).get("state").equals("excuted")) {
                j++;
            }
        }
    }


    /**
     * 根据流程进展，更新流程列表显示效果
     *
     * @param flow
     */
    private void changeFlowsColor(FlowConstant.PatientFlow flow) {
        boolean isChange = false;
        int position = 0;
        switch (flow) {
            case IDENTITY_CONFIRM:
                position = 0;
                isChange = true;
                break;
            case EXPLAIN:
                position = 1;
                isChange = true;
                break;
            case CHOOSESITE:
                position = 2;
                isChange = true;
                break;
            case MEDICINE_INJECTION:
                position = 3;
                isChange = true;
                break;
            case PATROL_COUNT_DOWN:
                position = 4;
                isChange = true;
                break;
            default:
                break;
        }
        if (isChange) {
            for (int i = 0; i < maps.size(); i++) {
                if (i >= position) {
                    maps.get(i).put("state", "checked");
                } else {
                    maps.get(i).put("state", "excuted");
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    Runnable lvRunnable = new Runnable() {
        @Override
        public void run() {
            marginRight = -lv_right.getWidth() / 4 * 3;
            lvParams.rightMargin = marginRight;
            lv_right.setLayoutParams(lvParams);
        }
    };

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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
        super.onBackPressed();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(InsulinFlowAct.this, HomeAct.class));
                finish();
                overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
                break;
            case R.id.btn_patientflow:
                dialog = new CustomEditTextDialog(InsulinFlowAct.this, R.style.myDialogTheme);
                dialog.setContent(new String[]{"记录内容:"});
                dialog.setTitle("提示");
                dialog.setLeftButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setRightButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        /** 胰岛素完成注射30分钟后巡视 */
                        recInspectResult();
                    }

                });
                dialog.show();
                break;
            case R.id.btn_patient_help:
                Intent intent = new Intent(InsulinFlowAct.this, HelpAct.class);
                intent.putExtra("flow", Flowpath);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
                break;
            case R.id.btn_patient_search:
                break;
            default:
                break;
        }
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        if (vp_flow.getCurrentItem() == 0) {
            insulinFlowSheetFra.DoCameraResult(result);
        }

    }

    /**
     * 胰岛素完成注射30分钟后巡视
     */
    private void recInspectResult() {
        progressDialog.show();
        PatrolBean patrolBean = new PatrolBean();
        patrolBean.setPerformDesc(dialog.getText()[0]);
        RequestManager.connection(new PatrolRequest(InsulinFlowAct.this, new
                IRespose<PatrolBean>() {
                    @Override
                    public void doResult(PatrolBean patrolBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        progressDialog.dismiss();
                        if (UserInfo.getKey() != null && !"".equals(UserInfo.getKey())) {
                            try {
                                result = DesUtil.decrypt(result, UserInfo.getKey());
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<ExecuteMedicineInfo>>() {
                                }.getType();
                                List<ExecuteMedicineInfo> list = gson.fromJson(result, type);
                                if (list != null && list.size() > 0) {
                                    boolean flag = true;
                                    for (ExecuteMedicineInfo executeMedicineInfo : list) {
                                        if (!flag)
                                            break;
                                        flag = executeMedicineInfo.getResult().equals("success");
                                    }
                                    if (flag) {
//                                        doPatrolRequest(fra);

                                    } else {
                                        //执行注射失败
                                        Toast.makeText(InsulinFlowAct.this, "执行注射操作失败（部分药品正在注射或者已注射)",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (BadPaddingException e) {
                                throw new KeyObsoleteException("密钥过期");
                            } catch (Exception e) {
                                if (LinstenNetState.isLinkState(getApplicationContext())) {
                                    Toast.makeText(InsulinFlowAct.this, getString(R.string.unstable), Toast
                                            .LENGTH_SHORT).show();
                                } else {
                                    toErrorAct();
                                }
                            }
                        }
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState) {
                            Toast.makeText(InsulinFlowAct.this, R.string.unstable, Toast.LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                }, 0, true, patrolBean));
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(InsulinFlowAct.this, ErrorAct.class);
        startActivity(intent);
    }

}
