package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.LoadCompletedIntravs;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.ComleteMedicineAdapter;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author ouyang
 * @ClassName: FlowLi
 * @Description: 给药中历史界面
 * @date 2015年2月28日 下午5:44:41
 */
@SuppressLint("ValidFragment")
public class FlowLiFra extends BaseFragment {
    private SyncPatientBean patient;
    private ComleteMedicineAdapter adapter = null;
    private static final int UPDATE = 10012;
    private ListView lv_completeMedicine;
    private List<OrderItemBean> orders = null;
    private ProgressDialog progressDialog = null;
    private int type = 0;//0 给药 1胰岛素


    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    public FlowLiFra(SyncPatientBean patient, int type) {
        super();
        this.type = type;
        this.patient = patient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_li, null);
        lv_completeMedicine = (ListView) view.findViewById(R.id.lv_completeMedicine);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.dataloading));
        orders = new ArrayList<OrderItemBean>();
        ListView lv_completeMedicine = (ListView) view.findViewById(R.id.lv_completeMedicine);
        lv_completeMedicine.setAdapter(adapter = new ComleteMedicineAdapter(getActivity(), orders));
        doLoadCompletedIntravsReq();
        return view;
    }


    private void doLoadCompletedIntravsReq() {
        PatMajorInfoBean patMajorInfoBean = new PatMajorInfoBean();
        patMajorInfoBean.setPatId(patient.getPatId());
        patMajorInfoBean.setType(type);
        patMajorInfoBean.setPerformStatus("9");
        progressDialog.show();
        RequestManager.connection(new LoadCompletedIntravs(getActivity(), new IRespose<PatMajorInfoBean>() {
            @Override
            public void doResult(PatMajorInfoBean patMajorInfoBean, int id) {
            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                Gson gson = new Gson();
                PatMajorInfoBean patMajorInfoBean = gson.fromJson(result, PatMajorInfoBean.class);
                orders.clear();
                orders.addAll(patMajorInfoBean.getOrders());
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, patMajorInfoBean));
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_li);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
