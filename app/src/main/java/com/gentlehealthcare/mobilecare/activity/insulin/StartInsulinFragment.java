package com.gentlehealthcare.mobilecare.activity.insulin;

import android.app.ProgressDialog;
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
import com.gentlehealthcare.mobilecare.presenter.StartInsulinPresenter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.IStartInsulinView;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class StartInsulinFragment extends BaseFragment implements IStartInsulinView, View.OnClickListener {

    private StartInsulinPresenter presenter;

    private TextView tv_injection_site, tv_blood, tv_start_time, tv_patrol_time;

    private Button btn_sure;

    private ProgressDialog progressDialog;

    private String patId;
    private OrderListBean orderListBean;
    private int site;
    private int status;
    private String desc;
    private String siteId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insulin_start, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.datasaving));
        initView(view);
        presenter = new StartInsulinPresenter(this);
        Bundle bundle = getArguments();
        patId = bundle.getString("patId");
        desc = bundle.getString("desc");
        siteId = bundle.getString("siteId");
        site = bundle.getInt("site");
        status = bundle.getInt("status");
        orderListBean = (OrderListBean) bundle.getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
        presenter.getBlood(this.patId);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                presenter.startInsulin(patId, orderListBean.getPlanOrderNo(), orderListBean.getPlanId(), status,
                        site, siteId);
                break;
            default:
                break;
        }
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
    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, orderListBean.getPlanOrderNo());
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        getActivity().finish();
    }

    @Override
    public void setBlood(String blood) {
        tv_blood.setText(blood);
        tv_injection_site.setText(desc);
        tv_start_time.setText(DateTool.getHourMinuteSecond());
        tv_patrol_time.setText(DateTool.getHMS30Min());
    }

    private void initView(View view) {
        tv_patrol_time = (TextView) view.findViewById(R.id.tv_patrol_time);
        tv_injection_site = (TextView) view.findViewById(R.id.tv_injection_site);
        tv_start_time = (TextView) view.findViewById(R.id.tv_start_time);
        tv_blood = (TextView) view.findViewById(R.id.tv_blood);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_insulin_start);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
