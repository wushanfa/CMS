package com.gentlehealthcare.mobilecare.activity.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.gradedcareevaluation.GradCareMeasureAct;
import com.gentlehealthcare.mobilecare.adapter.EvaluationGradedCareEvaluationAdapter;
import com.gentlehealthcare.mobilecare.adapter.GradedCareEvaluationAdapter;
import com.gentlehealthcare.mobilecare.bean.GradedCareEvaluationBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;

import java.util.ArrayList;
import java.util.List;
/*
* 压疮评估评估列表
* */
public class EvaluationGradedCareEvaluationAct extends BaseActivity implements ITemperatureView, View.OnClickListener {

    private ListView lv_evaluation_list_nursing;
    private Button btn_add_assessment;
    private LinearLayout rl_top;
    private TemperaturePresenter TemperaturePresenter;
    private int whichPatients = 0;
    private TextView patName;
    private TextView patSex;
    private TextView patBed;
    private TextView patCode;
    private String patId;
    private SyncPatientBean currentPatient;
    private MyPatientDialog dialog;
    private Button btn_back;
    List<SyncPatientBean> deptPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_evaluation_graded_care_evaluation);
        initView();
        setadapter();
    }

    //初始化控件
    private void initView() {
        rl_top = (LinearLayout) findViewById(R.id.rl_top);
        patName = (TextView) findViewById(R.id.tv_pat_name);
        patSex = (TextView) findViewById(R.id.tv_pat_sex);
        patBed = (TextView) findViewById(R.id.tv_pat_bed);
        patCode = (TextView) findViewById(R.id.tv_pat_code);
        lv_evaluation_list_nursing = (ListView) findViewById(R.id.lv_evaluation_list_nursing);
        btn_add_assessment = (Button) findViewById(R.id.btn_add_assessment);
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_add_assessment.setOnClickListener(this);
        rl_top.setOnClickListener(this);
        TemperaturePresenter = new TemperaturePresenter(this);
        TemperaturePresenter.initialSrc();
        TemperaturePresenter.getPatients(this, whichPatients);
        lv_evaluation_list_nursing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Intent = new Intent();
                Intent.setClass(EvaluationGradedCareEvaluationAct.this, EvaluationGradCareMeasureAct.class);
                startActivity(Intent);
            }
        });
    }

    //适配器
    private void setadapter() {
        ArrayList<GradedCareEvaluationBean> list = new ArrayList();

        list.add(new GradedCareEvaluationBean("2017-05-18   16:00", "11分 高度危险", "张红婷", "嘱患者如上厕所或淋浴时有需要帮助时使用紧急呼叫器"));
        list.add(new GradedCareEvaluationBean("2017-05-17   16:00", "20分 轻度危险", "李婷", "教会患者家属使用床栏方法，需下床时先将床档放下，切勿翻越"));
        list.add(new GradedCareEvaluationBean("2017-05-18   16:00", "13分 中度危险", "张红婷", "嘱患者如上厕所或淋浴时有需要帮助时使用紧急呼叫器"));
        EvaluationGradedCareEvaluationAdapter adapter = new EvaluationGradedCareEvaluationAdapter(list, this);
        lv_evaluation_list_nursing.setAdapter(adapter);
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top:
                TemperaturePresenter.showPatients();
                //新增跳转页面
                break;
            case R.id.btn_add_assessment:
                Intent Intent = new Intent();
                Intent.setClass(EvaluationGradedCareEvaluationAct.this,EvaluationGradCareMeasureAct.class);
                startActivity(Intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void showProgressDialog(String msg) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void showToast(String str) {

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
        dialog = new MyPatientDialog(EvaluationGradedCareEvaluationAct.this,
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
                        TemperaturePresenter.getPatients(EvaluationGradedCareEvaluationAct.this,
                                1024);// 1000标记用来刷新
                        dialog.dismiss();
                    }
                }, whichPatients, deptPatient);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
