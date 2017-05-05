package com.gentlehealthcare.mobilecare.activity.implementation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.adapter.ImplementationListAdapter;
import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.presenter.ImplementationRecordPresenter;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.IImplementationRecordView;

import java.util.List;

/**
 * 默认记录内容界面
 */
public class IndexFra extends BaseFragment implements IImplementationRecordView {

    private TextView tv_patrol;
    private TextView tv_plan_time;
    private TextView tv_start_time;
    private TextView tv_nurser_name;
    private RelativeLayout rl_start_time;
    private View v_line3;
    private ListView lv_time_pj_nursername;
    private LinearLayout ll_plan_time;
    private RelativeLayout rl_list_record;
    ImplementationRecordPresenter implementationRecordPresenter;
    OrderListBean orderListBean = null;
    String patId;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        implementationRecordPresenter = new ImplementationRecordPresenter(this, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_implementation_record, null);
        implementationRecordPresenter.intial(view);
        implementationRecordPresenter.receiveData();
        implementationRecordPresenter.loaddata(patId, orderListBean.getPlanOrderNo());
        return view;
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
    public void addRecord(List<ImplementationRecordBean> beans) {
        lv_time_pj_nursername.setAdapter(new ImplementationListAdapter(getActivity(), beans));
        if (!beans.isEmpty()) {
            implementationRecordPresenter.changeLayout(true);
        } else {
            implementationRecordPresenter.changeLayout(false);
        }
    }

    @Override
    public void showLoadFial() {
        ShowToast(getString(R.string.dataexception));
    }


    @Override
    public void intial(View view) {
        rl_start_time = (RelativeLayout) view.findViewById(R.id.rl_start_time);
        lv_time_pj_nursername = (ListView) view.findViewById(R.id.lv_time_pj_nursername);
        v_line3 = view.findViewById(R.id.v_line3);
        ll_plan_time = (LinearLayout) view.findViewById(R.id.ll_plan_time);
        rl_list_record = (RelativeLayout) view.findViewById(R.id.rl_list_record);
        tv_nurser_name = (TextView) view.findViewById(R.id.tv_nurser_name);
        tv_plan_time = (TextView) view.findViewById(R.id.tv_plan_time);
        tv_start_time = (TextView) view.findViewById(R.id.tv_start_time);
    }

    @Override
    public void receiveData() {
        patId = getArguments().getString(GlobalConstant.KEY_PATID);
        orderListBean = (OrderListBean) getArguments().getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
    }

    @Override
    public void changeLayout(boolean flag) {
        if (flag) {
            rl_list_record.setVisibility(View.VISIBLE);
            ll_plan_time.setVisibility(View.GONE);
        } else {
            tv_plan_time.setText("计划:" + StringTool.dateToTime(orderListBean.getStartTime()));
            if (orderListBean.getEventStartTime() != null && orderListBean.getEventStartTime() != "") {
                tv_start_time.setText("实际:" + StringTool.dateToTime(orderListBean.getEventStartTime().toString()));
            } else {
                tv_start_time.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(orderListBean.getNurseInOperate())) {
                tv_nurser_name.setText(orderListBean.getNurseInOperate());
            }
            ll_plan_time.setVisibility(View.VISIBLE);
            rl_list_record.setVisibility(View.GONE);
        }
    }
}
