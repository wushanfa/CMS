package com.gentlehealthcare.mobilecare.activity.insulin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.adapter.ImplementationListAdapter;
import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.InsulinOverPresenter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.IInsulinOverviewView;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class OverviewFragment extends BaseFragment implements IInsulinOverviewView {

    private InsulinOverPresenter presenter;

    private String patId;
    private OrderListBean orderListBean;

    private ProgressDialog progressDialog;

    private ListView lv_time_pj_nursername;
    private TextView tv_start_time, tv_nurser_name, tv_start_time1, tv_pj, tv_nurser_name1;

    private ImplementationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insulin_overview, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.datasaving));
        Bundle bundle = getArguments();
        patId = bundle.getString("patId");
        orderListBean = (OrderListBean) bundle.getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);

        initView(view);

        presenter = new InsulinOverPresenter(this);
        presenter.getRecord(patId, orderListBean.getPlanOrderNo());
        return view;
    }


    @Override
    public void addRecord(List<ImplementationRecordBean> beans) {
        adapter = new ImplementationListAdapter(getActivity(), beans);
        lv_time_pj_nursername.setAdapter(adapter);
        if (beans != null && !beans.isEmpty() && beans.size() != 0) {
            tv_nurser_name1.setText(beans.get(0).getName());
            tv_pj.setText(beans.get(0).getPerformDesc());
        }
    }

    public void showToast(String str) {
        ShowToast(str);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void initView(View view) {
        lv_time_pj_nursername = (ListView) view.findViewById(R.id.lv_time_pj_nursername);
        tv_start_time = (TextView) view.findViewById(R.id.tv_start_time);
        tv_nurser_name = (TextView) view.findViewById(R.id.tv_nurser_name);
        tv_start_time1 = (TextView) view.findViewById(R.id.tv_start_time1);
        tv_pj = (TextView) view.findViewById(R.id.tv_pj);
        tv_nurser_name1 = (TextView) view.findViewById(R.id.tv_nurser_name1);

        tv_start_time.setText(DateTool.spliteDate(GlobalConstant.TIME, (String) orderListBean.getEventStartTime
                ()));
        tv_start_time1.setText(DateTool.spliteDate(GlobalConstant.TIME, (String) orderListBean.getEventEndTime()));
        tv_nurser_name.setText(orderListBean.getNurseInOperate());
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_overview);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
