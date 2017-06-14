package com.gentlehealthcare.mobilecare.activity.gradedcareevaluation;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.event.ShowTextEvent;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.adapter.NursingMeasuresAdapter;
import com.gentlehealthcare.mobilecare.bean.NursingMeasureBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousExecuteView;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 * 分级护理评估评估措施
 */

public class NursingMeasuresAct extends BaseActivity implements ITemperatureView, IIntravenousExecuteView {

    private ListView lv_nursing_measures;
    private String dateString;
    private TextView tv_date_checked;
    private IntravenousPresenter intravenousPresenter;
    private TemperaturePresenter TemperaturePresenter;
    private TextView patName;
    private TextView patSex;
    private TextView patBed;
    private TextView patCode;
    private String patId;
    private LinearLayout rl_top;
    private SyncPatientBean currentPatient;
    private int whichPatients = 0;
    private MyPatientDialog dialog;
    //时间选择器完成后显示选择后的时间
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            dateString = DateTool.formatDate(GlobalConstant.DATE_TIME_SIMPLE, date);
            tv_date_checked.setText(dateString);
        }
    };
    private Button btn_back;
    private ArrayList<NursingMeasureBean> list;
    private NursingMeasuresAdapter adapter;
    private TextView tv_score;
    List<SyncPatientBean> deptPatient;

    @Override
    protected void resetLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nursing_measures);
        initview();
        setadapter();
        intravenousPresenter = new IntravenousPresenter(null, NursingMeasuresAct.this, null, null);
        TemperaturePresenter = new TemperaturePresenter(NursingMeasuresAct.this);
        TemperaturePresenter.initialSrc();
        TemperaturePresenter.getPatients(NursingMeasuresAct.this, whichPatients);
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
//                Logger.e("该类中没有 接收 eventbus 回调的方法");
        }

    }

    @Override
    protected void onDestroy() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            // Logger.e("该类没有注册 eventbus");
        }
        super.onDestroy();
    }

    private void initview() {
        tv_score = (TextView)findViewById(R.id.tv_score);
        tv_score.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        String type = getIntent().getStringExtra("type");
        tv_score.setText(type);
        if ("无需依赖".equals(type)){
            tv_score.setTextColor(getResources().getColor(R.color.black));
        }
        else {
            tv_score.setTextColor(getResources().getColor(R.color.red));
        }
        lv_nursing_measures = (ListView) findViewById(R.id.lv_nursing_measures);
        rl_top = (LinearLayout) findViewById(R.id.rl_top);
        patName = (TextView) findViewById(R.id.tv_pat_name);
        patSex = (TextView) findViewById(R.id.tv_pat_sex);
        patBed = (TextView) findViewById(R.id.tv_pat_bed);
        patCode = (TextView) findViewById(R.id.tv_pat_code);

        //时间选择器
        tv_date_checked = (TextView) findViewById(R.id.tv_date_checked);
        tv_date_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intravenousPresenter.showSlideDateTimerPicker();
            }
        });
        rl_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemperaturePresenter.showPatients();
            }
        });
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setadapter() {
        list = new ArrayList<>();
        list.add(new NursingMeasureBean("运送患者离开病房时，平车需有护栏，必要时约束患者，使用轮椅必须用安全带..运送患者离开病房时，平车需有护栏，必要时约束患者，使用轮椅必须用安全带"));
        list.add(new NursingMeasureBean("嘱患者改变体位遵守“三部曲”：平躺30秒，做起30秒，再行走"));
        list.add(new NursingMeasureBean("嘱患者应在有人陪护下方可离床活动"));
        adapter = new NursingMeasuresAdapter(list, NursingMeasuresAct.this);
        lv_nursing_measures.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showViewPager(ShowTextEvent event) {
        list.get(event.position).setMeasure(event.text);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void showProgressDialog(String msg) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void saveNextPatrolTime() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void initialSrc() {

    }

    @Override
    public void showKey(String[] strs, int flag) {

    }

    @Override
    public void setPatientInfo(SyncPatientBean patientInfo) {
        patName.setText(patientInfo.getName());
        patSex.setText(patientInfo.getSex());
        patBed.setText(StringTool.isBedNumber(patientInfo.getBedLabel()));
        patCode.setText(patientInfo.getPatCode());
        patId = patientInfo.getPatId();
        currentPatient = patientInfo;
    }

    @Override
    public void setPatient(List<SyncPatientBean> list) {

    }

    @Override
    public void showPatients() {
        dialog = new MyPatientDialog(NursingMeasuresAct.this,
                new MyPatientDialog.MySnListener() {

                    @Override
                    public void myOnItemClick(View view, int position, long id) {
                        TemperaturePresenter.setPatientInfo(deptPatient
                                .get(position));
                        whichPatients = position;
                        dialog.dismiss();
                    }

                    @Override
                    public void onRefresh() {
                        // isRefreshPatient = true;
                        TemperaturePresenter.getPatients(NursingMeasuresAct.this,
                                1024);// 1000标记用来刷新
                        dialog.dismiss();
                    }
                }, whichPatients, deptPatient);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void handException() {

    }

    @Override
    public void backToDoctorOrdersAct() {

    }

    //时间选择器监听
    @Override
    public void showSlideDateTimerPicker() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                // .setMinDate(0)
                // .setMaxDate(maxDate)
                .setIs24HourTime(true)
                .setTheme(SlideDateTimePicker.HOLO_LIGHT)
                .setIndicatorColor(Color.parseColor("#2ba3d5"))
                .build()
                .show();
    }
}
