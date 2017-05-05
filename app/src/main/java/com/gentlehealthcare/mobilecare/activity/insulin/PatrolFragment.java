package com.gentlehealthcare.mobilecare.activity.insulin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.presenter.InsulinPatrolPresenter;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.IPatrolView;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class PatrolFragment extends BaseFragment implements View.OnClickListener, IPatrolView {

    private InsulinPatrolPresenter presenter;

    private Button btn_sure, btn_cancel;
    private TextView tv_guomin, tv_dixuetang, tv_zhushe;
    private LinearLayout ll_react;

    private Button btn_normal;
    private Button btn_un_normal;

    private TextView tv_patrol_time, tv_patrol_date;

    private String[] guomin = {"硬块", "疼痛", "红晕", "荨麻疹", "肿胀", "紫癜"};
    private String[] dixuetang = {"头晕", "饥饿感", "心慌", "出冷汗", "无力", "抽搐", "昏迷"};
    private String[] zhushe = {"皮下出血", "瘀斑", "硬结"};

    private boolean[] guominBoolean = {false, false, false, false, false, false};
    private boolean[] dixuetangBoolean = {false, false, false, false, false, false, false};
    private boolean[] zhusheBoolean = {false, false, false};

    public AlertDialog builder = null;

    private String time;
    private String patId;
    private OrderListBean orderListBean;
    private String gmStr = "";
    private String dxtStr = "";
    private String zsStr = "";

    private ProgressDialog progressDialog;

    private LinearLayout ll_un_normal, ll_normal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insulin_patrol, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.datasaving));
        initView(view);
        Bundle bundle = getArguments();
        time = bundle.getString("time");
        patId = bundle.getString("patId");
        orderListBean = (OrderListBean) bundle.getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
        presenter = new InsulinPatrolPresenter(this);
        presenter.setTime(time);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                presenter.uploadResult(patId, orderListBean.getPlanOrderNo(), gmStr, dxtStr, zsStr);
                break;
            case R.id.btn_cancel:
                presenter.initText();
                break;
            case R.id.btn_normal:
                ll_react.setVisibility(View.GONE);
                btn_normal.setBackgroundResource(R.drawable.press1);
                btn_un_normal.setBackgroundResource(R.drawable.unpress);
                break;
            case R.id.btn_un_normal:
                ll_react.setVisibility(View.VISIBLE);
                btn_normal.setBackgroundResource(R.drawable.unpress);
                btn_un_normal.setBackgroundResource(R.drawable.press);
                break;
            case R.id.tv_guomin:
                presenter.showWhichText(GlobalConstant.GUO_MIN);
                break;
            case R.id.tv_dixuetang:
                presenter.showWhichText(GlobalConstant.DI_XUE_TANG);
                break;
            case R.id.tv_zhushe:
                presenter.showWhichText(GlobalConstant.ZHU_SHE);
                break;
            default:
                break;
        }
    }

    @Override
    public void showDialog(String str, final String[] items, final boolean[] booleans, final int type) {
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(str)
                .setMultiChoiceItems(items, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        booleans[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = "";
                        for (int j = 0; j < items.length; j++) {
                            if (booleans[j] == true) {
                                result = result + items[j] + ",";
                            }
                        }
                        presenter.putWhichTextResult(type, result);
                    }
                }).setNegativeButton(R.string.cancel, null).create();
        builder.show();
        builder.getWindow().setLayout(680, 750);
    }

    @Override
    public String[] getGuoMin() {
        return guomin;
    }

    @Override
    public String[] getDixueTang() {
        return dixuetang;
    }

    @Override
    public String[] getZhushe() {
        return zhushe;
    }

    @Override
    public void setGuoMin(String str) {
        gmStr = str;
        tv_guomin.setText(str);
    }

    @Override
    public void setDiXueTang(String str) {
        dxtStr = str;
        tv_dixuetang.setText(str);
    }

    @Override
    public void setZhuShe(String str) {
        zsStr = str;
        tv_zhushe.setText(str);
    }

    public boolean[] getGuominBoolean() {
        return guominBoolean;
    }

    public boolean[] getDixuetangBoolean() {
        return dixuetangBoolean;
    }

    public boolean[] getZhusheBoolean() {
        return zhusheBoolean;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void activityFinish() {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        getActivity().finish();
    }

    public void initTextView() {
        tv_guomin.setText("");
        tv_dixuetang.setText("");
        tv_zhushe.setText("");
        gmStr = "";
        dxtStr = "";
        zsStr = "";
    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTime(String date, String time) {
        tv_patrol_time.setText(time);
        tv_patrol_date.setText(date);
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_insulin_patrol);
        SupportDisplay.resetAllChildViewParam(root);
    }


    private void initView(View view) {
        tv_patrol_time = (TextView) view.findViewById(R.id.tv_patrol_time);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        tv_guomin = (TextView) view.findViewById(R.id.tv_guomin);
        tv_dixuetang = (TextView) view.findViewById(R.id.tv_dixuetang);
        tv_zhushe = (TextView) view.findViewById(R.id.tv_zhushe);
        tv_guomin.setOnClickListener(this);
        tv_dixuetang.setOnClickListener(this);
        tv_zhushe.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        ll_react = (LinearLayout) view.findViewById(R.id.ll_react);
        ll_react.setVisibility(View.GONE);
        btn_normal = (Button) view.findViewById(R.id.btn_normal);
        btn_un_normal = (Button) view.findViewById(R.id.btn_un_normal);
        btn_un_normal.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        tv_patrol_date = (TextView) view.findViewById(R.id.tv_patrol_date);
        ll_un_normal = (LinearLayout) view.findViewById(R.id.ll_un_normal);
        ll_normal = (LinearLayout) view.findViewById(R.id.ll_normal);
    }
}
