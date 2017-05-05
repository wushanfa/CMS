package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 胰岛素药品核对
 * Created by ouyang on 2015/6/11.
 */
@SuppressLint("ValidFragment")
public class InsulinCheckMedicineFra extends BaseFragment{
    private String site;
    private ListView lv_medicine;
    private SyncPatientBean patient=null;
    private List<OrderItemBean> orderItemBeans=null;
    private OrderItemBean orderItemBean=null;
    public InsulinCheckMedicineFra(String site,SyncPatientBean patient,OrderItemBean orderItemBean){
        this.site=site;
        this.patient=patient;
        this.orderItemBean=orderItemBean;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fra_insulin_checkmedicine,null);
        ((TextView)rootView.findViewById(R.id.tv_injectionsite)).setText(site);
        lv_medicine= (ListView) rootView.findViewById(R.id.lv_medicine);
        orderItemBeans=new ArrayList<OrderItemBean>();
        orderItemBeans.add(orderItemBean);
        lv_medicine.setAdapter( new MedicineListAdapter(getActivity(), orderItemBeans));
        return rootView;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_insulin_check);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
