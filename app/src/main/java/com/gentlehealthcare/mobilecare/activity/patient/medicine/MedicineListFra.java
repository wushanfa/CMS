package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author ouyang
 * @ClassName: MedicineList
 * @Description: 给药 中药品列表
 * @date 2015年2月28日 下午5:53:30
 */
@SuppressLint("ValidFragment")
public class MedicineListFra extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "MedicineListFra";
    private SyncPatientBean patient;
    private MedicineListAdapter adapter;
    private PullToRefreshListView pullToRefreshListView;
    private String state = MedicineConstant.STATE_WAITING;
    private ProgressDialog pDialog;
    private boolean isrun = false;
    private FlowSheetFra fra;
    private int currentIndex = -1;
    private String currentPlanNo = null;
    private OrderItemBean orderItemBean = null;

    private List<OrderItemBean> orders = null;

    private ListView listView;

    private int index = 0;
    private static final int SHOW = 0XFF;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW)
                pDialog.show();

        }
    };

    public MedicineListFra(SyncPatientBean patient, String state, FlowSheetFra fra) {

        super();
        this.state = state;
        this.patient = patient;
        this.fra = fra;
    }

    public MedicineListFra() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orders = new ArrayList<OrderItemBean>();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("正在同步药品信息，请稍后...");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_medicine_list, null);
        view.findViewById(R.id.rg_tab_list).setVisibility(View.GONE);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listView);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshMedicineList();
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setAdapter(adapter = new MedicineListAdapter(getActivity(), orders));
        listView.setOnItemClickListener(this);
        isrun = !JsonFileUtil.isLocal();
        refreshMedicineList();
//        new Thread(new Runnable() {注释 by zyy
//            @Override
//            public void run() {
//                while (isrun) {
//                    try {
//                        Thread.sleep(60 * 1000);
//                        if (isrun) {
//                            refreshMedicineList();
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        return view;
    }

    /**
     * 刷新药品列表界面
     */

    public void refreshMedicineList() {
        GetMeicineListReq();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        currentIndex = position;
        setOrderItemBean(orders.get(position));
        adapter.notifyChanged(position);
    }


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
        patMajorInfoBean.setType(0);
        RequestManager.connection(new SyncPatMajorInfoRequest(getActivity(), new IRespose<PatMajorInfoBean>() {
            @Override
            public void doResult(PatMajorInfoBean patMajorInfoBean, int id) {
            }

            @Override
            public void doResult(String result) {
                Gson gson = new Gson();
                PatMajorInfoBean patMajorInfoBean = gson.fromJson(result, PatMajorInfoBean.class);
                orders.clear();
                int index = 0;
                if (currentPlanNo == null) {
                    orders.addAll(patMajorInfoBean.getOrders());
                } else {
                    List<OrderItemBean> list = patMajorInfoBean.getOrders();

                    for (int i = 0; i < list.size(); i++) {
                        OrderItemBean orderItemBean = list.get(i);
                        if (orderItemBean.getPlanOrderNo().equals(currentPlanNo)) {
                            index = i;
                        }
                        orders.add(orderItemBean);
                    }
                }

                if (orders.size() > 0) {
                    setOrderItemBean(orders.get(index));
                } else {
                    currentIndex = -1;
                    setOrderItemBean(null);
                }
                adapter.notifyDataSetChanged();
                if (index != 0)
                    adapter.notifyChanged(index);
                if (state.equals(MedicineConstant.STATE_WAITING))
                    ((FlowSheetFra) getParentFragment()).updateLeftTab(orders.size());
                else
                    ((FlowSheetFra) getParentFragment()).updateRightTab(orders.size());
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
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_list);
        SupportDisplay.resetAllChildViewParam(root);
    }

    public OrderItemBean getOrderItemBean() {
        return orderItemBean;
    }

    public void setOrderItemBean(OrderItemBean orderItemBean) {
        this.orderItemBean = orderItemBean;
    }

    public String getCurrentPlanNo() {
        return currentPlanNo;
    }

    public void setCurrentPlanNo(String currentPlanNo) {
        this.currentPlanNo = currentPlanNo;
    }

    public List<OrderItemBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItemBean> orders) {
        this.orders = orders;
    }

    public boolean isShow() {
        return pDialog.isShowing();
    }

    public void setPosition(String result) {
        int position = 0;
        for (int i = 0; i < orders.size(); i++) {
            if (result.equals(orders.get(i).getRelatedBarCode())) {
                position = i;
                break;
            }
        }
        listView.setSelection(position);
        adapter.notifyChanged(position);
    }
}
