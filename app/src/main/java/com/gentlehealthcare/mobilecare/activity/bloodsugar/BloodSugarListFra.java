package com.gentlehealthcare.mobilecare.activity.bloodsugar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.BSResultItemBean;
import com.gentlehealthcare.mobilecare.net.bean.BloodSugarItemBean;
import com.gentlehealthcare.mobilecare.net.bean.FinishedShugar;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatMajorInfoBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.SyncPatMajorInfoRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.FinishedShugarAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zyy
 * @date 2015-9-18下午2:30:51
 * @category 血糖测试界面
 */
@SuppressLint("ValidFragment")
public class BloodSugarListFra extends BaseFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "BloodSugarListFra";
    /**
     * 待执行按钮
     */
    RadioButton rb_wait;
    /**
     * 执行中按钮
     */
    RadioButton rb_excuteing;
    /**
     * 执行按钮
     */
    private Button btn_left_;
    /**
     * 扫描按钮
     */
    private Button btn_right_;
    /**
     * 加载条
     */
    private ProgressDialog progressDialog = null;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;

    /**
     * 主页按钮
     */
    private ImageButton imbtn_home;
    /**
     * 返回按钮
     */
    private ImageButton imbtn_back;
    /**
     * 病人姓名
     */
    private TextView tv_tras_name;
    /**
     * 病人床号
     */
    private TextView tv_tras_bed;
    /**
     * 血糖测试医嘱列表
     */
    private PullToRefreshListView pullToRefreshListView;
    /**
     * 血糖医嘱数据集合
     */
    private List<OrderItemBean> orderItemBeans = null;
    /**
     * 血糖医嘱列表适配器
     */
    private MedicineListAdapter adapter;
    private OrderItemBean orderItemBean;
    /**
     * 涮新节点
     */
    private String currentPlanNo = null;
    /**
     * 选中的item
     */
    private String xtPlanNo = null;
    /**
     * 数据显示
     */
    ListView listView = null;
    /**
     * 已完成血糖数据集合
     */
    public List<FinishedShugar> mFinishedShugars = null;
    /**
     * 血糖下拉列表
     */
    private List<BloodSugarItemBean> temp = new ArrayList<BloodSugarItemBean>();
    private BSResultItemBean itemBean = new BSResultItemBean();

    public BloodSugarListFra(SyncPatientBean patient) {
        super();
        this.patient = patient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        orderItemBeans = new ArrayList<OrderItemBean>();
        // 初始化加载待执行血品
        GetMeicineListReq();
        getItem();
        // 初始化加载已完成血糖
        mFinishedShugars = new ArrayList<FinishedShugar>();
        loadGlucoseResults();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_bloodsugarlist, null);
        // 初始化UI组件
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后..");
        progressDialog.show();
        // 加载快照数据
        pullToRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.lv_bloodsugar);
//		pullToRefreshListView
//				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//
//					@Override
//					public void onRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						GetMeicineListReq();
//					}
//				});
        listViewShowWait(orderItemBeans);
//        listView = pullToRefreshListView.getRefreshableView();
//		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		listView.setAdapter(adapter = new MedicineListAdapter(getActivity(),
//				orderItemBeans));
//		listView.setOnItemClickListener(this);

        return view;
    }

    /**
     * 刷新药品列表界面
     */

    public void refreshMedicineList() {
        GetMeicineListReq();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rb_wait = (RadioButton) getActivity().findViewById(R.id.rb_wait);
        rb_wait.setOnClickListener(this);
        rb_excuteing = (RadioButton) getActivity().findViewById(
                R.id.rb_excuteing);
        rb_excuteing.setOnClickListener(this);
        rb_wait.setTextColor(Color.BLACK);
        rb_excuteing.setTextColor(Color.GRAY);

        btn_right_ = (Button) getActivity().findViewById(R.id.btn_right_);
        btn_right_.setOnClickListener(this);
        btn_left_ = (Button) getActivity().findViewById(R.id.btn_left_);
        btn_left_.setOnClickListener(this);
        tv_tras_bed = (TextView) getActivity().findViewById(R.id.tv_tras_bed);
        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText("床号：" + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }
        tv_tras_name = (TextView) getActivity().findViewById(R.id.tv_tras_name);
        tv_tras_name.setText("姓名:" + patient.getName());

        // 返回&主页按钮
        imbtn_home = (ImageButton) getActivity().findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) getActivity().findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_bs_list);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 加载已完成血糖记录
     */
    private void loadGlucoseResults() {
        // UrlConstant.UpLoadThreeEight()
        CCLog.i(TAG, "加载已完成血糖记录>>" + UrlConstant.loadGlucoseResults() + patient.getPatId());

        HttpUtils http = new HttpUtils();
        http.send(
                HttpRequest.HttpMethod.POST,
                UrlConstant.loadGlucoseResults() + patient.getPatId(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ShowToast("血糖记录加载失败");
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        CCLog.i(TAG, "血糖记录加载成功");
                        JSONObject object = null;
                        JSONArray jsonArray = null;
                        Boolean result = false;

                        try {
                            object = new JSONObject(arg0.result);
                            result = object.getBoolean("result");
                            if (result) {
                                jsonArray = object.getJSONArray("data");

                                Gson gson = new Gson();
                                Type type = new TypeToken<List<FinishedShugar>>() {
                                }.getType();

                                mFinishedShugars = gson.fromJson(
                                        jsonArray.toString(), type);
                                rb_excuteing.setText("已完成" + mFinishedShugars.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.btn_right_:
                if (xtPlanNo != null) {
                    // 选择好测血糖医嘱后跳转到测血糖界面
                    Bundle bundel = new Bundle();
                    bundel.putSerializable("itemList", (Serializable) temp);
                    BloodSugarFra bloodSugarFra = new BloodSugarFra(patient, xtPlanNo);
                    bloodSugarFra.setArguments(bundel);
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, bloodSugarFra).commit();
                } else {
                    ShowToast("请选择血糖医嘱");
                }
                break;
            case R.id.imbtn_back:
                // 身份核对界面
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container,
                                new BloodSugarIdentifyScanFra(patient)).commit();
                break;
            case R.id.imbtn_home:
                getActivity().startActivity(
                        new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static,
                        R.anim.slide_out_right);
                break;
            case R.id.rb_wait:
                btn_right_.setVisibility(View.VISIBLE);
                rb_wait.setTextColor(Color.BLACK);
                rb_excuteing.setTextColor(Color.GRAY);
                listViewShowWait(orderItemBeans);
                break;
            case R.id.rb_excuteing:
                btn_right_.setVisibility(View.INVISIBLE);
                rb_wait.setTextColor(Color.GRAY);
                rb_excuteing.setTextColor(Color.BLACK);
                listViewShowFinished(mFinishedShugars);
                break;
            default:
                break;
        }

    }

    /**
     * 加载血糖医嘱信息
     */
    private void GetMeicineListReq() {
        PatMajorInfoBean patMajorInfoBean = new PatMajorInfoBean();
        patMajorInfoBean.setPatId(patient.getPatId());
        patMajorInfoBean.setPerformStatus("0");
        patMajorInfoBean.setType(1);
        patMajorInfoBean.setTemplateId(TemplateConstant.BLOOD_TEST
                .GetTemplateId());

        RequestManager.connection(new SyncPatMajorInfoRequest(getActivity(),
                new IRespose<PatMajorInfoBean>() {
                    @Override
                    public void doResult(PatMajorInfoBean patMajorInfoBean,
                                         int id) {
                    }

                    @Override
                    public void doResult(String result) {
                        Gson gson = new Gson();
                        PatMajorInfoBean patMajorInfoBean = gson.fromJson(
                                result, PatMajorInfoBean.class);
                        orderItemBeans.clear();
                        int index = 0;
                        if (currentPlanNo == null) {
                            orderItemBeans.addAll(patMajorInfoBean.getOrders());
                        } else {
                            List<OrderItemBean> list = patMajorInfoBean
                                    .getOrders();

                            for (int i = 0; i < list.size(); i++) {
                                OrderItemBean orderItemBean = list.get(i);
                                if (orderItemBean.getPlanOrderNo().equals(
                                        currentPlanNo)) {
                                    index = i;
                                }
                                orderItemBeans.add(orderItemBean);
                            }
                        }
                        if (orderItemBeans.size() > 0) {
                            setOrderItemBean(orderItemBeans.get(index));
                            xtPlanNo = orderItemBeans.get(0).getPlanOrderNo();
                        } else {
                            setOrderItemBean(null);
                            ShowToast("患者没有血糖测试医嘱");
                        }
                        rb_wait.setText("待执行" + orderItemBeans.size());
                        adapter.notifyDataSetChanged();
                        adapter.notifyChanged(index);
                        progressDialog.dismiss();
                        pullToRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        pullToRefreshListView.onRefreshComplete();
                        if (networkState)
                            Toast.makeText(getActivity(), "操作异常",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(
                                    getActivity(),
                                    getResources().getString(
                                            R.string.netstate_content),
                                    Toast.LENGTH_SHORT).show();
                    }
                }, 0, true, patMajorInfoBean));

    }

    /**
     * 获取血糖项目
     */
    private void getItem() {
        CCLog.i(TAG, "获得血糖选项:->" + UrlConstant.BloodSugarGetItem());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.BloodSugarGetItem(),
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<BSResultItemBean>() {
                        }.getType();
                        itemBean = gson.fromJson(result, type);
                        if (itemBean.isResult()) {
                            temp = itemBean.getData();
                            CCLog.i(TAG, itemBean.getMsg());
                        } else {
                            CCLog.i(TAG, "血糖列表加载失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ShowToast("请求异常");
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    /**
     * 已完成血糖数据显示
     *
     * @param FinishedShugarsList
     */
    private void listViewShowFinished(List<FinishedShugar> FinishedShugarsList) {
        listView = pullToRefreshListView.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setAdapter(new FinishedShugarAdapter(FinishedShugarsList,
                getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    /**
     * 待执行血糖数据显示
     *
     * @param orderItemBeans
     */
    private void listViewShowWait(List<OrderItemBean> orderItemBeans) {

        pullToRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        GetMeicineListReq();
                    }
                });

        listView = pullToRefreshListView.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setAdapter(adapter = new MedicineListAdapter(getActivity(),
                orderItemBeans));
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        position -= 1;
        xtPlanNo = orderItemBeans.get(position).getPlanOrderNo();
        setOrderItemBean(orderItemBeans.get(position));
        adapter.notifyChanged(position);
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

    public List<OrderItemBean> getOrderItemBeans() {
        return orderItemBeans;
    }

    public void setOrderItemBeans(List<OrderItemBean> orderItemBeans) {
        this.orderItemBeans = orderItemBeans;
    }

    public boolean isShow() {
        return progressDialog.isShowing();
    }

    public void setPosition(String result) {
        int position = 0;
        for (int i = 0; i < orderItemBeans.size(); i++) {
            if (result.equals(orderItemBeans.get(i).getPlanOrderNo())) {
                position = i;
                break;
            }
        }
        listView.setSelection(position);
    }
}
