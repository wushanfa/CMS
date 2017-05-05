package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousSealingView;

import java.util.Date;

import static com.gentlehealthcare.mobilecare.tool.StringTool.patternCode;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药待执行中界面
 */
public class IntravSealingFra extends BaseFragment implements IIntravenousSealingView, View.OnClickListener {
    private EditText edt_start_time;
    private OrderListBean orderListBean;
    private String patId;
    private EditText edt_nurser_name;
    private EditText edt_input_num;
    private String dateString = DateTool.getCurrDateTimeS();
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            dateString = DateTool.formatDate(GlobalConstant.DATE_TIME, date);
            edt_start_time.setText(dateString);
        }
    };
    private IntravenousPresenter intravenousPresenter;
    private Button btn_save;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intravenousPresenter = new IntravenousPresenter(null, null, null, this);
    }


    @Override
    protected void resetLayout(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_intravenous_sealing, null);
        intialSource(view);
        receiveData();
        return view;
    }

    private void intialSource(View view) {
        edt_start_time = (EditText) view.findViewById(R.id.edt_start_time);
        // edt_start_time.setOnClickListener(this);
        edt_nurser_name = (EditText) view.findViewById(R.id.edt_nurser_name);
        edt_input_num = (EditText) view.findViewById(R.id.edt_input_num);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        edt_start_time.setText(dateString);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void handException() {

    }

    @Override
    public void showSlideDateTimerPicker() {
        new SlideDateTimePicker.Builder(getFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                //.setMinDate(0)
                //.setMaxDate(maxDate)
                .setIs24HourTime(true)
                .setTheme(SlideDateTimePicker.HOLO_LIGHT)
                .setIndicatorColor(Color.parseColor("#2ba3d5"))
                .build()
                .show();
    }

    @Override
    public void sealing() {

    }

    @Override
    public void setNurseNameAndSealingTime(String name, String time, String dossage) {
        edt_nurser_name.setText(name);
        edt_start_time.setText(time);
        //  edt_input_num.setText(dossage);
    }

    @Override
    public void receiveData() {
        patId = getArguments().getString(GlobalConstant.KEY_PATID);
        orderListBean = (OrderListBean) getArguments().getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
        String time = DateTool.getCurrDateTimeS();
        String dossage = " ";
        if (orderListBean.getEventEndTime() != null) {
            time = orderListBean.getEventEndTime().toString();
        }
        if (orderListBean.getDosageActual() != null) {
            dossage = orderListBean.getDosageActual();
        }
        intravenousPresenter.setNurseNameAndSealingTime(orderListBean.getNurseInOperate(), time, dossage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_start_time:
                intravenousPresenter.showSealSlideDateTimerPicker();
                break;
            case R.id.btn_save:
                if (orderListBean.getPerformStatus().equals("1")) {
                    end();
                } else if (orderListBean.getPerformStatus().equals("9")) {
                    ShowToast("该医嘱已封管");
                } else if (orderListBean.getPerformStatus().equals("0")) {
                    ShowToast("该医嘱未开始执行");
                } else {
                    ShowToast("该医嘱已异常结束");
                }
                break;
        }
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        getActivity().finish();
    }

    public void setScanResult(String result) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
        String planBarcode = sharedPreferences.getString(
                GlobalConstant.KEY_PLANBARCODE, "");
        if (patternCode(planBarcode, result)) {
            if (result.equals(orderListBean.getRelatedBarcode())) {
                end();
            } else {
                showToast("请扫描当前医嘱码或点击按钮完成输液");
            }
        } else {
            showToast("请扫描医嘱码或点击按钮完成输液");
        }
    }

    private void end(){
        intravenousPresenter.saveExceptionInfo(patId, UserInfo.getUserName(), orderListBean
                .getPlanOrderNo(), "1", edt_input_num.getText().toString(), "", "");
    }


}
