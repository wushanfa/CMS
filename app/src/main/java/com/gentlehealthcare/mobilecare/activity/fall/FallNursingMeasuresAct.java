package com.gentlehealthcare.mobilecare.activity.fall;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.adapter.EvaluationNursingMeasuresAdapter;
import com.gentlehealthcare.mobilecare.bean.NursingMeasureBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.InnerListView;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousExecuteView;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 * 跌到评估评估措施
 */

public class FallNursingMeasuresAct extends BaseActivity implements ITemperatureView, IIntravenousExecuteView {

    private InnerListView lv_nursing_measures;
    private InnerListView lv_protective_effect;
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


    @Override
    protected void resetLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall_nursing_measures);
        initview();
        setLv_nursing_measuresAdapter();
        setLv_protective_effectAdapter();
        intravenousPresenter = new IntravenousPresenter(null, FallNursingMeasuresAct.this, null, null);
        TemperaturePresenter = new TemperaturePresenter(FallNursingMeasuresAct.this);
        TemperaturePresenter.initialSrc();
        TemperaturePresenter.getPatients(FallNursingMeasuresAct.this, whichPatients);
    }

    private void initview() {
        lv_protective_effect = (InnerListView) findViewById(R.id.lv_protective_effect);
        lv_nursing_measures = (InnerListView) findViewById(R.id.lv_nursing_measures);
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

    private void setLv_nursing_measuresAdapter() {
        ArrayList<NursingMeasureBean> list = new ArrayList<>();
        list.add(new NursingMeasureBean("告之患者或家属可能 出现压疮的危险性，讲解注意事项"));
        list.add(new NursingMeasureBean("定时翻身更换体位，减轻皮肤受压，避免摩擦"));
        list.add(new NursingMeasureBean("使用（1）气垫（2）棉垫（3）保护膜等"));
        list.add(new NursingMeasureBean("指导及协助家属合理膳食，增强营养"));
        EvaluationNursingMeasuresAdapter adapter = new EvaluationNursingMeasuresAdapter(list, FallNursingMeasuresAct.this);
        lv_nursing_measures.setAdapter(adapter);
    }
    private void setLv_protective_effectAdapter() {
        ArrayList<NursingMeasureBean> list = new ArrayList<>();
        list.add(new NursingMeasureBean("皮肤无异常"));
        list.add(new NursingMeasureBean("皮肤局部出现红肿热痛"));
        list.add(new NursingMeasureBean("皮肤局部出现红肿热痛"));
        EvaluationNursingMeasuresAdapter adapter = new EvaluationNursingMeasuresAdapter(list, FallNursingMeasuresAct.this);
        lv_protective_effect.setAdapter(adapter);
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

    }

    public void showPatients(final List<SyncPatientBean> deptPatient) {
        dialog = new MyPatientDialog(FallNursingMeasuresAct.this,
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
                        TemperaturePresenter.getPatients(FallNursingMeasuresAct.this,
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
