package com.gentlehealthcare.mobilecare.activity.evaluation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.event.ShowEvaluationMeasureTextEvent;
import com.gentlehealthcare.mobilecare.event.ShowEvaluationTextEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

/**
 * Created by Administrator on 2017/6/6.
 * 压疮评估评估措施详情
 */

public class EvaluationNursingMeasuresSecondDetailsAct extends BaseActivity implements View.OnClickListener {

    private EditText tv_measure_details;
    private TextView tv_details_number;
    private Button btn_back;
    private int position;
    private TextView tv_change_finish;

    @Override
    protected void resetLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_evaluation_nursing_measures_details);
        initview();
        initdata();
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
//                Logger.e("该类中没有 接收 eventbus 回调的方法");
        }
    }

    private void initview() {
        position = getIntent().getIntExtra("position", 0);
        tv_measure_details = (EditText) findViewById(R.id.tv_measure_details);
        tv_details_number = (TextView) findViewById(R.id.tv_details_number);
        tv_change_finish = (TextView)findViewById(R.id.tv_change_finish);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_change_finish.setOnClickListener(this);
        tv_measure_details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_details_number.setText(tv_measure_details.getText().toString().length() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initdata() {
        String details = getIntent().getStringExtra("details");
        tv_measure_details.setText(details);
        tv_measure_details.setSelection(tv_measure_details.getText().length());
        int length = details.length();
        tv_details_number.setText(length + "");
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_change_finish:
                EventBus.getDefault().post(new ShowEvaluationMeasureTextEvent(position,tv_measure_details.getText().toString()));
                finish();
                break;
        }
    }
}
