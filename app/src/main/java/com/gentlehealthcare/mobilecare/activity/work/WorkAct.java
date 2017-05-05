package com.gentlehealthcare.mobilecare.activity.work;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.TabListener;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.home.SystemAttentionAct;
import com.gentlehealthcare.mobilecare.activity.patient.DeptPatientAct;
import com.gentlehealthcare.mobilecare.constant.PatientConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.ScreenTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MyFragmentStateAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.SlidingListAdapter;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作界面
 */
public class WorkAct extends ABToolBarActivity implements OnItemClickListener {
    private SyncPatientBean patient;
    private List<SyncPatientBean> patients;
    private List<String> list = new ArrayList<String>();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SlidingListAdapter mAdapter;
    private MyFragmentStateAdapter fragmentAdapter;
    private SlidingMenu slidingMenu;
    private ListView lvSliding;
    private Button btn_back;
    private TextView tv_title;
    private ViewPager vp_work;
    private RadioGroup rg_bottom;
    private ProgressDialog pDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_main);
        //数据库Dao实例化
        patients = UserInfo.getMyPatient();
//        patients = UserInfo.getDeptPatient();
        if (!patients.isEmpty()&&patients!=null&&patients.size()>0) {
            patient = patients.get(0);
        }else{
            patient=new SyncPatientBean();
        }
        //初始化侧边框
        initSildmenu();
        //初始化控件
        initWidget();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.getpatientdatapleaseholdon));
        findViewById(R.id.btn_slidingmenu_home).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //设置适配器
        /**
         * position == 0 口服给药
         * position == 1 静脉输液
         * position == 2 输血
         * position == 3 胰岛素
         * position == 4 测血糖
         */
        list.add("静脉输液");
        list.add("输   血");
        list.add("胰岛素");
        list.add("血   糖");
        lvSliding.setAdapter(mAdapter = new SlidingListAdapter(this, list));
        vp_work.setAdapter(fragmentAdapter = new MyFragmentStateAdapter(getSupportFragmentManager(), fragments));
        for (int i = 0; i < patients.size(); i++) {
            if (patient.getPatId() == patients.get(i).getPatId())
                mAdapter.notifyChanged(i);
        }
        updateTitle("0", "0", "0");
        DoLoadTaskByCategoryRecordReq(TemplateConstant.MEDICINE.GetTemplateId());
        mAdapter.notifyChanged(0);
        setFragments(TemplateConstant.MEDICINE.GetTemplateId());
        RadioButton button;
        for (int i = 0; i < rg_bottom.getChildCount(); i++) {
            button = (RadioButton) rg_bottom.getChildAt(i);
            button.setId(i);
        }
        if (rg_bottom.getChildCount() > 0)
            rg_bottom.check(0);
        new TabListener(rg_bottom, vp_work);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                finish();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                if (vp_work.getCurrentItem() < vp_work.getChildCount())
//                    vp_work.setCurrentItem(vp_work.getCurrentItem() + 1);
                if (vp_work.getCurrentItem() > 0){
                    vp_work.setCurrentItem(vp_work.getCurrentItem() - 1);
                }
            }
        });

        //配置工具栏
        setToolBarDrawable(new int[]{R.drawable.act_home_deptpatbtn, R.drawable.act_home_mypatbtn, R.drawable
                .act_home_homebtn, R.drawable.act_home_workbtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int i) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(WorkAct.this, DeptPatientAct.class));
                        break;
                    case 1:
                        Intent intent = new Intent(WorkAct.this, HomeAct.class);
                        intent.putExtra("toMyPatient", true);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(WorkAct.this, HomeAct.class));
                        break;
                    case 3:
                        break;
                    case 4:
                        startActivity(new Intent(WorkAct.this, SystemAttentionAct.class));
                        break;
                }
            }

            @Override
            public void onLeftBtnClick() {
            }

            @Override
            public void onRightBtnClick() {
            }
        });
    }

    /**
     * 侧边框初始化
     */
    private void initSildmenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 设置边缘滑动模式
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setBehindWidth(ScreenTool.getScreenWidth(this) / 4 * 3);// 设置slidingmenu的宽度
        slidingMenu.setMenu(R.layout.slidingmenu_left);// menu布局
        slidingMenu.setMode(SlidingMenu.LEFT_OF);// 设置slidingmenu两侧menu模式
//        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
//            @Override
//            public void onOpen() {
//                btn_back.setVisibility(View.GONE);
//            }
//        });
//        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
//            @Override
//            public void onClose() {
//                btn_back.setVisibility(View.GONE);
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setValid(false);
        setCheck(3);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_work);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 更新标题
     */
    private void updateTitle(String performTask, String executingTask, String completedTask) {
       // tv_title.setText("待执行" + performTask + "项，执行中" + executingTask + "项,已完成" + completedTask + "项");
        tv_title.setText(performTask);
    }

    /**
     * 初始化控件
     */
    @SuppressLint("WrongViewCast")
    private void initWidget() {
        btn_back = (Button) findViewById(R.id.btn_back);
        lvSliding = (ListView) findViewById(R.id.lv_sliding);
        tv_title = (TextView) findViewById(R.id.tv_info);
        vp_work = (ViewPager) findViewById(R.id.vp_work);
        rg_bottom = (RadioGroup) findViewById(R.id.rgp_bottom);
        btn_back.setBackgroundResource(R.drawable.skin_home);
        //tv_title.collection();
        // btn_back.setVisibility(View.GONE);
        lvSliding.setOnItemClickListener(this);
        lvSliding.setSelector(R.color.transparent);
        ((TextView) findViewById(R.id.tv_head)).setText("项目清单");
    }


    /**
     * 设置工作Fragment数据
     *
     * @param
     */
    public void setFragments(String templateId) {
        fragments.clear();
        fragments.add(new ProgressWorkFra(templateId, PatientConstant.STATE_WAITING));
        fragments.add(new ProgressWorkFra(templateId, PatientConstant.STATE_EXECUTING));
        fragments.add(new ProgressWorkFra(templateId, PatientConstant.STATE_EXECUTED));
        fragmentAdapter.notifyDataSetChanged();
    }

    /**
     * 病人点击Item事件
     *
     * @param arg0
     * @param arg1
     * @param position
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        String[] temp = new String[]{TemplateConstant.MEDICINE.GetTemplateId(),
                TemplateConstant.TRANSFUSION.GetTemplateId(),
                TemplateConstant.INSULIN.GetTemplateId(),
                TemplateConstant.BLOOD_TEST.GetTemplateId()};
        setFragments(temp[position]);
        mAdapter.notifyChanged(position);
        slidingMenu.showContent();
        DoLoadTaskByCategoryRecordReq(temp[position]);
    }


    private void DoLoadTaskByCategoryRecordReq(String templateId) {
        //更改头部显示项目数为“流程名称”
//        LoadTaskByCategoryRecordBean loadTaskByCategoryRecordBean = new LoadTaskByCategoryRecordBean();
//        loadTaskByCategoryRecordBean.setTemplateId(templateId);
//        String url = UrlConstant.LoadTaskByCategoryRecord() + loadTaskByCategoryRecordBean.toString();
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String result = responseInfo.result;
//                LoadTaskByCategoryRecordBean loadTaskByCategoryRecordBean = new Gson().fromJson(result,
//                        LoadTaskByCategoryRecordBean.class);
//                if (loadTaskByCategoryRecordBean.isResult()) {
//                    updateTitle(loadTaskByCategoryRecordBean.getPerformTask(), loadTaskByCategoryRecordBean
//                            .getExecutingTask(), loadTaskByCategoryRecordBean.getCompletedTask());
//                } else {
//                    ShowToast(loadTaskByCategoryRecordBean.getMsg());
//                    updateTitle("0", "0", "0");
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                updateTitle("0", "0", "0");
//            }
//        });
        if(templateId.equals("AA-1")){
            updateTitle("静脉给药", "", "");
        }else if(templateId.equals("AA-3")){
            updateTitle("胰岛素","","");
        }else if(templateId.equals("AA-4")){
            updateTitle("测血糖","","");
        }else if(templateId.equals("AA-5")){
            updateTitle("输血","","");
        }
    }


}
