package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.view.IIntravenousPatrolView;

import java.util.Date;
import java.util.List;

import static com.gentlehealthcare.mobilecare.tool.StringTool.patternCode;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药巡视界面
 */
@SuppressLint("ValidFragment")
public class IntravPatrolFra extends BaseFragment implements View.OnClickListener, IIntravenousPatrolView {

    private ProgressDialog progressDialog;
    private Button btn_nomal;
    private Button btn_fast;
    private Button btn_save;
    private Button btn_slow;
    private Button btn_exception;
    private Button btn_pause;
    private EditText edt_patrol_time;
    private EditText edt_ps_jg;
    private EditText edt_input_num;
    private EditText edt_exception_text;
    private ToggleButton tb_end;
    private IntravenousPresenter intravenousPresenter;
    private String patId = null;
    private String patrolText = "正常";
    private LinearLayout ll_nomal;
    private LinearLayout ll_exception;
    private List<IntraDontExcuteBean> list = null;
    private boolean isNomalLayout = true;
    private String exceptionCode = "A1";
    private String exceptionText = "患者原因";
    private OrderListBean orderListBean;
    private String dateString = DateTool.getCurrDateTimeS();
    private boolean isTogleChecked = false;
    private int type;
    private RelativeLayout rl_ps_jg;
    private ImageView iv_image2;
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            dateString = DateTool.formatDate(GlobalConstant.DATE_TIME_SIMPLE, date);
            edt_patrol_time.setText(dateString);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intravenousPresenter = new IntravenousPresenter(null, null, this, null);
        intravenousPresenter.loadVariationDict();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_intravenous_patrol, null);
        intialSource(view);
        getData();
        if (orderListBean != null && orderListBean.getSkinTestResult() != null && !orderListBean
                .getSkinTestResult().equals("")) {
            edt_ps_jg.setText(orderListBean.getSkinTestResult());
        }
        return view;
    }

    private void intialSource(View view) {
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_nomal = (Button) view.findViewById(R.id.btn_nomal);
        btn_fast = (Button) view.findViewById(R.id.btn_fast);
        btn_slow = (Button) view.findViewById(R.id.btn_slow);
        btn_exception = (Button) view.findViewById(R.id.btn_exception);
        btn_pause = (Button) view.findViewById(R.id.btn_pause);
        edt_patrol_time = (EditText) view.findViewById(R.id.edt_patrol_time);
        edt_ps_jg = (EditText) view.findViewById(R.id.edt_ps_jg);
        edt_input_num = (EditText) view.findViewById(R.id.edt_input_num);
        edt_exception_text = (EditText) view.findViewById(R.id.edt_exception_text);
        btn_save.setOnClickListener(this);
        btn_nomal.setOnClickListener(this);
        btn_fast.setOnClickListener(this);
        btn_slow.setOnClickListener(this);
        btn_exception.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        edt_patrol_time.setOnClickListener(this);
        edt_ps_jg.setOnClickListener(this);
        edt_input_num.setOnClickListener(this);
        edt_exception_text.setOnClickListener(this);
        tb_end = (ToggleButton) view.findViewById(R.id.tb_end);
        tb_end.setOnClickListener(this);
        tb_end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTogleChecked = true;
                    tb_end.setBackground(getResources().getDrawable(R.drawable.switch_button_on));
                } else {
                    isTogleChecked = false;
                    tb_end.setBackground(getResources().getDrawable(R.drawable.switch_button_off));
                }
            }
        });
        ll_nomal = (LinearLayout) view.findViewById(R.id.ll_normal);
        ll_exception = (LinearLayout) view.findViewById(R.id.ll_exception);
        intravenousPresenter.setPatrolTime(dateString);
        iv_image2 = (ImageView) view.findViewById(R.id.iv_image2);
        rl_ps_jg = (RelativeLayout) view.findViewById(R.id.rl_ps_jg);
    }

    private void getData() {
        patId = getArguments().getString(GlobalConstant.KEY_PATID);
        orderListBean = (OrderListBean) getArguments().getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);

        if (!TextUtils.isEmpty(orderListBean.getSkinTest()) && orderListBean.getSkinTest().equals("1")) {
            rl_ps_jg.setVisibility(View.VISIBLE);
            iv_image2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                patrol();
                break;
            case R.id.btn_fast:
                isNomalLayout = true;
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_fast.setTextColor(getResources().getColor(R.color.white));
                btn_fast.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_slow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                patrolText = "过快";
                type = 2;
                intravenousPresenter.changeLayout();
                break;
            case R.id.btn_nomal:
                isNomalLayout = true;
                btn_slow.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_fast.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_nomal.setTextColor(getResources().getColor(R.color.white));
                btn_fast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_slow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                patrolText = "正常";
                type = 1;
                intravenousPresenter.changeLayout();
                break;
            case R.id.btn_slow:
                isNomalLayout = true;
                btn_fast.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setTextColor(getResources().getColor(R.color.white));
                btn_fast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_slow.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                patrolText = "过慢";
                type = 3;
                intravenousPresenter.changeLayout();
                break;
            case R.id.btn_exception:
                isNomalLayout = false;
                patrolText = "异常";
                btn_fast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_fast.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                type = 4;
                intravenousPresenter.changeLayout();
                break;
            case R.id.btn_pause:
                isNomalLayout = false;
                btn_fast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_fast.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_slow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                patrolText = "暂停";
                type = 5;
                intravenousPresenter.changeLayout();
                break;
            case R.id.edt_patrol_time:
                intravenousPresenter.showSlideDateTimerPickerInPatrol();
                break;
            case R.id.edt_ps_jg:
                intravenousPresenter.showPSDialog();
                break;
            case R.id.edt_input_num:
                break;
            case R.id.edt_exception_text:
                intravenousPresenter.showDict(list);
                break;

        }
    }

    @Override
    protected void resetLayout(View view) {

    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(getString(R.string.loadingdata));
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void showToast(String msg) {
       // Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show(); why: crash 17-1-22
    }

    @Override
    public void handException() {

    }

    @Override
    public void setSaveButtonbackGroud() {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        getActivity().finish();
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
    public void addVariationDict(List<IntraDontExcuteBean> list) {
        this.list = list;
    }

    @Override
    public void setExceptionText(String text) {

    }

    @Override
    public void changeLayout() {
        if (isNomalLayout) {
            ll_exception.setVisibility(View.GONE);
            ll_nomal.setVisibility(View.VISIBLE);
        } else {
            ll_exception.setVisibility(View.VISIBLE);
            ll_nomal.setVisibility(View.GONE);
        }

    }

    @Override
    public void showPiShi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        final String[] dict = new String[]{"阴性(-)", "阳性(+)"};

        builder.setItems(dict, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_ps_jg.setText(dict[which]);
            }
        }).setCancelable(true);
        builder.show();
    }

    @Override
    public void showVariationDict(final List<IntraDontExcuteBean> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        int length = list.size();
        if (length > 0) {
            String[] dict = new String[length];
            for (int i = 0; i < length; i++) {
                dict[i] = list.get(i).getItemName();
            }
            builder.setItems(dict, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exceptionCode = list.get(which).getItemCode();
                    exceptionText = list.get(which).getItemName();
                    edt_exception_text.setText(exceptionText);
                }
            }).setCancelable(true);
            builder.show();
        }
    }

    @Override
    public void setPatrolTime(String time) {
        edt_patrol_time.setText(time);
    }

    public void setScanResult(String result) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
        String planBarcode = sharedPreferences.getString(
                GlobalConstant.KEY_PLANBARCODE, "");
        if (patternCode(planBarcode, result)) {
            if (result.equals(orderListBean.getRelatedBarcode())) {
                patrol();
            } else {
                showToast("请扫描当前医嘱码或点击按钮进行巡视");
            }
        } else {
            showToast("请扫描医嘱码或点击按钮进行巡视");
        }
    }

    private void patrol() {

        if (isTogleChecked) {
            isTogleChecked = false;
            intravenousPresenter.saveExceptionInfo(patId, UserInfo.getUserName(), orderListBean
                            .getPlanOrderNo(), "-1", edt_input_num.getText().toString().trim(), exceptionCode,
                    exceptionText);
        } else {
            if (patrolText.equals("异常") && edt_exception_text.getText() != null) {
                intravenousPresenter.savePatrolInfo(patId, UserInfo.getUserName(), orderListBean
                        .getPlanOrderNo(), exceptionCode, edt_input_num.getText().toString()
                        .trim(), edt_ps_jg.getText().toString(), type);
            } else {
                intravenousPresenter.savePatrolInfo(patId, UserInfo.getUserName(), orderListBean
                        .getPlanOrderNo(), patrolText, edt_input_num.getText().toString().trim(), edt_ps_jg
                        .getText().toString(), type);
            }
        }
    }
}
