package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.JsonFileUtil;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.SyncPatMajorInfoRequest;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 胰岛素 医嘱列表
 * Created by ouyang on 2015/6/11.
 */
@SuppressLint("ValidFragment")
public class DoctorOrderListFra extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "DoctorOrderListFra";
    private SyncPatientBean patient;
    private List<OrderItemBean> orderItemBeans = null;
    private MedicineListAdapter adapter;
    private OrderItemBean orderItemBean;
    private PullToRefreshListView pullToRefreshListView;
    private static final int SHOW = 0XFF;
    private String state = MedicineConstant.STATE_WAITING;
    private ProgressDialog pDialog;
    private boolean isrun = false;
    private RadioGroup rg_listtab;
    private int checkButtonId;
    private String currentPlanNo = null;

    private ListView listView;

    public DoctorOrderListFra(SyncPatientBean patient, String state) {
        super();
        this.state = state;
        this.patient = patient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("正在同步药品信息，请稍后...");
        orderItemBeans = new ArrayList<OrderItemBean>();
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == SHOW)
                pDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_list, null);
        rg_listtab = (RadioGroup) view.findViewById(R.id.rg_tab_list);
        if (getCheckButtonId() == 0)
            rg_listtab.check(R.id.rbtn_insulin);
        else
            rg_listtab.check(R.id.rbtn_bloodtest);
        rg_listtab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (state.equals(MedicineConstant.STATE_EXECUTING)) {
                    SwitchTab(MedicineConstant.STATE_EXECUTING);
                } else {
                    SwitchTab(MedicineConstant.STATE_WAITING);
                }
            }
        });
        if (state.equals(MedicineConstant.STATE_EXECUTING)) {
            SwitchTab(MedicineConstant.STATE_EXECUTING);
        } else {

            SwitchTab(MedicineConstant.STATE_WAITING);
        }
        if (orderItemBeans != null && orderItemBeans.size() > 0) {
            setOrderItemBean(orderItemBeans.get(0));
            updateRightTxt();
        }
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listView);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshMedicineList();
            }
        });
        listView = pullToRefreshListView.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setAdapter(adapter = new MedicineListAdapter(getActivity(), orderItemBeans));
        listView.setOnItemClickListener(this);
        isrun = !JsonFileUtil.isLocal();
        refreshMedicineList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isrun) {
                    try {
                        Thread.sleep(60 * 1000);
                        if (isrun) {
                            refreshMedicineList();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return view;
    }

    /**
     * 刷新药品列表界面
     */

    public void refreshMedicineList() {
        GetMeicineListReq();
    }

    /**
     * 更新右边按钮的文本
     */
    private void updateRightTxt() {
        android.support.v4.app.Fragment fragment = getParentFragment();
        if (fragment instanceof InsulinFlowSheetFra) {
            String content = "执行";
            if (getOrderItemBean().getPerformStatus().equals(MedicineConstant.STATE_EXECUTING)) {
                String tempId = getOrderItemBean().getTemplateId();
                if (tempId.equals(TemplateConstant.BLOOD_TEST.GetTemplateId())) {
                    content = "录入";
                } else {
                    content = "巡视";
                }
            } else {
                content = "执行";
            }
            ((InsulinFlowSheetFra) fragment).updateRightText(content);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        setOrderItemBean(orderItemBeans.get(position));
        adapter.notifyChanged(position);
        updateRightTxt();
    }

    /**
     * 切换选项卡，操作处理
     *
     * @param state 状态
     */
    public void SwitchTab(String state) {
        if (patient == null)
            return;
        this.state = state;
        GetMeicineListReq();
    }

    /**
     * 加载药品信息
     */
    private void GetMeicineListReq() {
        handler.sendEmptyMessage(SHOW);
        PatMajorInfoBean patMajorInfoBean = new PatMajorInfoBean();
        patMajorInfoBean.setPatId(patient.getPatId());
        patMajorInfoBean.setPerformStatus(state);
        patMajorInfoBean.setType(1);
        if (rg_listtab.getCheckedRadioButtonId() == R.id.rbtn_insulin)
            patMajorInfoBean.setTemplateId(TemplateConstant.INSULIN.GetTemplateId());
        else
            patMajorInfoBean.setTemplateId(TemplateConstant.BLOOD_TEST.GetTemplateId());
        RequestManager.connection(new SyncPatMajorInfoRequest(getActivity(), new IRespose<PatMajorInfoBean>() {
            @Override
            public void doResult(PatMajorInfoBean patMajorInfoBean, int id) {
            }

            @Override
            public void doResult(String result) {
                Gson gson = new Gson();
                PatMajorInfoBean patMajorInfoBean = gson.fromJson(result, PatMajorInfoBean.class);
                orderItemBeans.clear();
                int index = 0;
                if (currentPlanNo == null) {
                    orderItemBeans.addAll(patMajorInfoBean.getOrders());
                } else {
                    List<OrderItemBean> list = patMajorInfoBean.getOrders();

                    for (int i = 0; i < list.size(); i++) {
                        OrderItemBean orderItemBean = list.get(i);
                        if (orderItemBean.getPlanOrderNo().equals(currentPlanNo)) {
                            index = i;
                        }
                        orderItemBeans.add(orderItemBean);
                    }
                }
                if (orderItemBeans.size() > 0) {
                    setOrderItemBean(orderItemBeans.get(index));
                } else {
                    setOrderItemBean(null);
                }
                adapter.notifyDataSetChanged();
                if (index != 0)
                    adapter.notifyChanged(index);
                pDialog.dismiss();
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                pDialog.dismiss();
                pullToRefreshListView.onRefreshComplete();
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
    public void onDestroyView() {
        super.onDestroyView();
        isrun = false;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_list);
        SupportDisplay.resetAllChildViewParam(root);
    }

    public OrderItemBean getOrderItemBean() {
        return orderItemBean;
    }

    public void setOrderItemBean(OrderItemBean orderItemBean) {
        this.orderItemBean = orderItemBean;
    }


    public int getCheckButtonId() {
        return checkButtonId;
    }

    public void setCheckButtonId(int checkButtonId) {
        this.checkButtonId = checkButtonId;
    }

    public String getCurrentPlanNo() {
        return currentPlanNo;
    }

    public void setCurrentPlanNo(String currentPlanNo) {
        this.currentPlanNo = currentPlanNo;
    }

    public List<OrderItemBean> getOrderItemBeans() {
        return orderItemBeans;
    }

    public void setOrderItemBeans(List<OrderItemBean> orderItemBeans) {
        this.orderItemBeans = orderItemBeans;
    }

    public boolean isShow() {
        return pDialog.isShowing();
    }

    public void setPosition(String result) {
        int position = 0;
        for (int i = 0; i < orderItemBeans.size(); i++) {
            if (result.equals(orderItemBeans.get(i).getRelatedBarCode())) {
                position = i;
                break;
            }
        }
        adapter.notifyChanged(position);
    }
}

