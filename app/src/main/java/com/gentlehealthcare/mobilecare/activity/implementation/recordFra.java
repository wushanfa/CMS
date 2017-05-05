package com.gentlehealthcare.mobilecare.activity.implementation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
import com.gentlehealthcare.mobilecare.presenter.ImplementationRecordPresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.view.IImplementationRecordInputView;

import java.util.Date;
import java.util.List;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：执行记录里面记录界面
 */
public class recordFra extends BaseFragment implements View.OnClickListener, IImplementationRecordInputView {


    private Button btn_back;
    private Button btn_nomal;
    private Button btn_pause;
    private Button btn_exception;
    private TextView edt_ps_jg;
    private EditText edt_record_time;
    private TextView edt_pj;
    private EditText edt_exception;
    private Button btn_save;
    private ImageView iv_image5;
    private ImageView iv_image2;
    private ImageView iv_image4;
    private ProgressDialog progressDialog = null;
    private ImplementationRecordPresenter implementationRecordPresenter;
    private String patId = null;
    private OrderListBean orderListBean;
    private String dateString = DateTool.getCurrDateTimeS();
    private boolean isEnd = false;
    private RelativeLayout rl_ps_jg;
    private RelativeLayout rl_pj;
    private RelativeLayout rl_exception;
    private String exceptionCode;
    private String exceptionText;
    private String status = "1";
    private String skinResult = null;
    private String appraisalCode;
    private String appraisalText;


    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            dateString = DateTool.formatDate(GlobalConstant.DATE_TIME_SIMPLE, date);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        implementationRecordPresenter = new ImplementationRecordPresenter(null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_implementation_record, null);
        intialSource(view);
        getData();
        return view;
    }

    private void intialSource(View view) {
        edt_record_time = (EditText) view.findViewById(R.id.edt_record_time);
        edt_ps_jg = (TextView) view.findViewById(R.id.edt_ps_jg);
        edt_pj = (TextView) view.findViewById(R.id.edt_pj);
        edt_exception = (EditText) view.findViewById(R.id.edt_exception);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_back = (Button) view.findViewById(R.id.btn_back);
        edt_record_time.setText(DateTool.getCurrDateTimeS());
        edt_record_time.setOnClickListener(this);
        btn_nomal = (Button) view.findViewById(R.id.btn_nomal);
        btn_nomal.setOnClickListener(this);
        btn_pause = (Button) view.findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);
        btn_exception = (Button) view.findViewById(R.id.btn_exception);
        btn_exception.setOnClickListener(this);
        rl_ps_jg = (RelativeLayout) view.findViewById(R.id.rl_ps_jg);
        rl_ps_jg.setOnClickListener(this);
        rl_pj = (RelativeLayout) view.findViewById(R.id.rl_pj);
        rl_pj.setOnClickListener(this);
        rl_exception = (RelativeLayout) view.findViewById(R.id.rl_exception);
        rl_exception.setOnClickListener(this);
        iv_image2 = (ImageView) view.findViewById(R.id.iv_image2);
        iv_image5 = (ImageView) view.findViewById(R.id.iv_image5);
        iv_image4 = (ImageView) view.findViewById(R.id.iv_image4);
    }

    private void getData() {
        patId = getArguments().getString(GlobalConstant.KEY_PATID);
        orderListBean = (OrderListBean) getArguments().getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
        if (!TextUtils.isEmpty(orderListBean.getSkinTest())&&orderListBean.getSkinTest().equals("1")) {
            rl_ps_jg.setVisibility(View.VISIBLE);
            iv_image2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
                getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                getActivity().finish();
                break;
            case R.id.btn_save:
                implementationRecordPresenter.saveRecord(patId, UserInfo.getUserName(), orderListBean
                                .getPlanOrderNo(), status, DateTool.getCurrDateTime(), skinResult,
                        exceptionText, exceptionCode, appraisalText, GlobalConstant.tempEventId);
                break;
            case R.id.rl_ps_jg:
                implementationRecordPresenter.showPsDialog();
                break;
            case R.id.btn_nomal:
                status = "1";
                btn_nomal.setTextColor(getResources().getColor(R.color.white));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                rl_exception.setVisibility(View.INVISIBLE);
                iv_image5.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(orderListBean.getSkinTest())&&orderListBean.getSkinTest().equals("1")) {
                    rl_ps_jg.setVisibility(View.VISIBLE);
                    iv_image2.setVisibility(View.VISIBLE);
                }
                rl_pj.setVisibility(View.VISIBLE);
                iv_image4.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pause:
                status = "3";
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                rl_ps_jg.setVisibility(View.INVISIBLE);
                iv_image2.setVisibility(View.INVISIBLE);
                rl_pj.setVisibility(View.INVISIBLE);
                iv_image4.setVisibility(View.INVISIBLE);
                rl_exception.setVisibility(View.VISIBLE);
                iv_image5.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_exception:
                status = "2";
                btn_nomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btn_nomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btn_exception.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btn_pause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                if (!TextUtils.isEmpty(orderListBean.getSkinTest())&&orderListBean.getSkinTest().equals("1")) {
                    rl_ps_jg.setVisibility(View.VISIBLE);
                    iv_image2.setVisibility(View.VISIBLE);
                }
                rl_exception.setVisibility(View.VISIBLE);
                iv_image5.setVisibility(View.VISIBLE);
                iv_image4.setVisibility(View.VISIBLE);
                rl_pj.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_exception:
                implementationRecordPresenter.loadVariationDict();
                break;
            case R.id.rl_pj:
                implementationRecordPresenter.loadAppraisalDict();
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
    public void showPsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        final String[] dict = new String[]{"阴性(-)", "阳性(+)"};

        builder.setItems(dict, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_ps_jg.setText(dict[which]);
                skinResult = dict[which];
            }
        }).setCancelable(true);
        builder.show();
    }

    @Override
    public void showExceptionDialog(final List<IntraDontExcuteBean> list) {

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
                    edt_exception.setText(exceptionText);
                }
            }).setCancelable(true);
            builder.show();
        }
    }

    @Override
    public void showAppraisalDialog(final List<LoadAppraislRecordBean> list) {
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

                    appraisalCode = list.get(which).getItemCode();
                    appraisalText = list.get(which).getItemName();
                    edt_pj.setText(appraisalText);
                }
            }).setCancelable(true);
            builder.show();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
