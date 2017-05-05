package com.gentlehealthcare.mobilecare.activity.patient.trans;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.bean.transfusion.TranOrderBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
import com.gentlehealthcare.mobilecare.view.adapter.BloodProductsListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Zyy
 * @date 2015-9-18下午2:30:51
 * @category 血品列表界面
 */
@SuppressLint("ValidFragment")
public class TransfusionFra extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TransfusionFra";
    /**
     * 待执行按钮
     */
    RadioButton rb_wait;
    /**
     * 执行中按钮
     */
    RadioButton rb_excuteing;
    /**
     * 血品列表
     */
    private PullToRefreshListView lv_bloodproducts;
    /**
     * 血品适配器
     */
    private BloodProductsListAdapter mBloodProductsListAdapter;
    /**
     * 执行按钮
     */
    private Button btn_left_;
    /**
     * 扫描按钮
     */
//    private Button btn_right_;
    /**
     * 病人ID
     */
    private String mpatId = null;
    /**
     * 血品集合
     */
    public List<BloodProductBean2> mBloodProducts = null;
    /**
     * 加载条
     */
    private ProgressDialog progressDialog = null;
    /**
     * 核对条
     */
    private ProgressDialog CgProgressDialog = null;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 选中血品id
     */
    private String mPlanOrderNo = "0";

    /**
     * 当前页面false:待执行 true:执行中
     */
    private boolean Flag = false;
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
     * 逆向关联冲管医嘱
     */
    private String cgPlanOrderNo = "1";
    /**
     * 选中血品Bean
     */
    private BloodProductBean2 mBloodProductBean2 = null;

    /**
     * 是否刷新
     */
    private boolean isRefresh = false;

    /**
     * 列表
     */
    ListView listView = null;

    /**
     * 数据集合
     */
    public List<BloodProductBean2> mBloodProductsShow = null;

    private String eventId = "";

    private TextView tv_time, tv_order_context;

    private String reqId = "";

    private String excuteOrPatrol = GlobalConstant.VALUE_FLAG_EXECUTE;
    //这个界面需要planorderno可能

    public TransfusionFra(SyncPatientBean patient) {
        super();
        this.patient = patient;

    }

    public TransfusionFra(SyncPatientBean patient, String cgPlanOrderNo) {
        this.patient = patient;
        this.cgPlanOrderNo = cgPlanOrderNo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("eventId")) {
            eventId = getArguments().getString("eventId");
        }
        reqId = getArguments().getString(GlobalConstant.KEY_REQID);
        mBloodProducts = new ArrayList<BloodProductBean2>();
        excuteOrPatrol = getArguments().getString(GlobalConstant.KEY_EXECUTE_PATROL);
    }

    @Override
    public void onResume() {
        super.onResume();
        lv_bloodproducts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isRefresh) {
                    new GetDataTask().execute();
                }
            }
        });
        lv_bloodproducts.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_transfusion, null);
        // 初始化UI组件
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后..");
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_order_context = (TextView) view.findViewById(R.id.tv_order_context);
        if (GlobalConstant.VALUE_FLAG_EXECUTE.equals(excuteOrPatrol)) {
            loadBloodTrans("0");
        } else if (GlobalConstant.VALUE_FLAG_PATROL.equals(excuteOrPatrol)) {
            loadBloodTrans("1");
        } else {
            loadBloodTrans("0");
        }
        LoadKuaiZhao();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rb_wait = (RadioButton) getActivity().findViewById(R.id.rb_wait);
        rb_wait.setOnClickListener(this);
        rb_excuteing = (RadioButton) getActivity().findViewById(R.id.rb_excuteing);
        rb_excuteing.setOnClickListener(this);
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
        tv_tras_name.setText("姓名：" + patient.getName());
        // 返回&主页按钮
        imbtn_home = (ImageButton) getActivity().findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) getActivity().findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
        rb_wait.setTextColor(Color.BLACK);
        rb_excuteing.setTextColor(Color.GRAY);
        lv_bloodproducts = (PullToRefreshListView) getActivity().findViewById(R.id.lv_bloodproducts);
        lv_bloodproducts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isRefresh) {
                    new GetDataTask().execute();
                }
            }
        });
        lv_bloodproducts.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        if (progressDialog.isShowing() || progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_trans_);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.rb_wait:
                rb_wait.setTextColor(Color.BLACK);
                rb_excuteing.setTextColor(Color.GRAY);
                progressDialog.show();
                Flag = false;
                loadBloodTrans("0");
                break;
            case R.id.rb_excuteing:
                rb_wait.setTextColor(Color.GRAY);
                rb_excuteing.setTextColor(Color.BLACK);
                progressDialog.show();
                Flag = true;
                loadBloodTrans("1");
                break;
            case R.id.btn_right_:
                // 核对按钮点击事件
                if (mBloodProductBean2 != null) {

                    if (mBloodProductBean2.getBloodStatus().equals("0")) {// 表示待执行的血品
                        // 3查8对界面
                        ThreeEightCheckFragment threeEightCheckFragment = new ThreeEightCheckFragment(patient,
                                mBloodProductBean2, reqId);
                        Bundle bundle = new Bundle();
                        if (!"".equals(eventId)) {
                            bundle.putString("eventId", eventId);
                        }
                        threeEightCheckFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fl_container,
                                threeEightCheckFragment).addToBackStack(null).commit();
                    } else if (mBloodProductBean2.getBloodStatus().equals("1")) {// 表示执行中的血品
                        // 巡视界面
                        getFragmentManager().beginTransaction().replace(R.id.fl_container, new
                                TrasPatrolFra(patient, mBloodProductBean2)).addToBackStack(null).commit();
                    } else if (mBloodProductBean2.getBloodStatus().equals("9")) {
                        ShowToast("该血品已执行完毕");
                    }
                } else {
                    ShowToast("没有血制品");
                }
                break;
            case R.id.imbtn_back:
                //身份核对界面
//                getFragmentManager().beginTransaction().replace(R.id.fl_container, new TrasIdentifyScanFra
//                        (patient)).commit();
//                Intent intent = new Intent(getActivity(), DocOrdersActivity.class);
//                getActivity().setResult(GlobalConstant.RESUlt_CODE);
//                getActivity().startActivity(intent);
//                getActivity().finish();
//                getActivity().overridePendingTransition(R.anim.in_or_out_static,
//                        R.anim.slide_out_right);
                Intent intent = new Intent();
                intent.putExtra(GlobalConstant.KEY_REQID, reqId);
                getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                super.getActivity().onBackPressed();
                break;
            case R.id.imbtn_home:
                getActivity().startActivity(new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static,
                        R.anim.slide_out_right);
                break;
            default:
                break;
        }

    }

    /**
     * 加载快照
     */

    private void LoadKuaiZhao() {
        String url = UrlConstant.LoadKuaiZhao() + UserInfo.getUserName() + "&patId=" + patient.getPatId();
        CCLog.i(TAG, "快照请求>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.i(TAG, "快照数据获取失败");
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject object = null;
                Boolean result = false;
                try {
                    object = new JSONObject(arg0.result);
                    result = object.getBoolean("result");

                    if (result) {
                        rb_wait.setText("待执行" + object.optString("performTask"));
                        rb_excuteing.setText("执行中" + object.optString("executingTask"));
                    }

                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }
        });
    }

    private void loadBloodTrans(final String status) {
        String url = UrlConstant.loadNewBlood() + UserInfo.getUserName() + "&patId=" + patient
                .getPatId() + "&reqId=" + UrlTool.transWord(reqId) + "&status=" + status;
        CCLog.i(TAG, "加载新的输血信息>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                progressDialog.dismiss();
                String result = arg0.result;
                isRefresh = true;
                CCLog.i(TAG, "加载新的输血信息成功");
                JSONObject object = null;
                Boolean flag = false;
                JSONObject tempJson;
                JSONArray transJson;
                JSONArray orderJson;
                try {
                    object = new JSONObject(result);
                    flag = object.getBoolean("result");
                    if (flag) {
                        tempJson = object.getJSONObject("data");
                        transJson = tempJson.getJSONArray("trans");
                        orderJson = tempJson.getJSONArray("order");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<BloodProductBean2>>() {
                        }.getType();
                        final List<BloodProductBean2> trans = gson.fromJson(transJson.toString(), type);
                        listView = lv_bloodproducts.getRefreshableView();
                        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        mBloodProductsListAdapter = new BloodProductsListAdapter(getActivity(), trans);
                        listView.setAdapter(mBloodProductsListAdapter);
                        mBloodProductsShow = trans;
                        if (mBloodProductsShow.size() == 1) {//只有一袋血制品不需要点击
                            mBloodProductBean2 = mBloodProductsShow.get(0);
                            // 核对按钮点击事件
                            if (mBloodProductBean2.getBloodStatus().equals("0")) {// 表示待执行的血品
                                // 3查8对界面
                                ThreeEightCheckFragment threeEightCheckFragment = new
                                        ThreeEightCheckFragment(patient, mBloodProductBean2, reqId);

                                getFragmentManager().beginTransaction().replace(R.id.fl_container,
                                        threeEightCheckFragment).addToBackStack(null).commit();
                            } else if (mBloodProductBean2.getBloodStatus().equals("1")) {// 表示执行中的血品
                                getFragmentManager().beginTransaction().replace(R.id.fl_container, new
                                        TrasPatrolFra(patient, mBloodProductBean2)).addToBackStack(null)
                                        .commit();
                            } else if (mBloodProductBean2.getBloodStatus().equals("9")) {
                                ShowToast("该血品已执行完毕");
                            }
                        } else if (mBloodProductsShow.size() == 0) {
                            ShowToast("血袋记录未找到");
                        } else {
                            listView.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mBloodProductBean2 = mBloodProductsShow.get(position - 1);
                                    // 核对按钮点击事件
                                    if (mBloodProductBean2 != null) {
                                        if (mBloodProductBean2.getBloodStatus().equals("0")) {// 表示待执行的血品
                                            // 3查8对界面
                                            ThreeEightCheckFragment threeEightCheckFragment = new
                                                    ThreeEightCheckFragment(patient, mBloodProductBean2, reqId);

                                            getFragmentManager().beginTransaction().replace(R.id.fl_container,
                                                    threeEightCheckFragment).addToBackStack(null).commit();
                                            //ShowToast("请使用红外扫描核对血品信息");
                                        } else if (mBloodProductBean2.getBloodStatus().equals("1")) {// 表示执行中的血品
                                            getFragmentManager().beginTransaction().replace(R.id.fl_container, new
                                                    TrasPatrolFra(patient, mBloodProductBean2)).addToBackStack(null)
                                                    .commit();
                                        } else if (mBloodProductBean2.getBloodStatus().equals("9")) {
                                            ShowToast("该血品已执行完毕");
                                        }
                                    } else {
                                        ShowToast("没有血制品");
                                    }
                                }
                            });
                        }
                        Gson gsonOrder = new Gson();
                        Type typeOrder = new TypeToken<List<TranOrderBean>>() {
                        }.getType();
                        List<TranOrderBean> orders = gsonOrder.fromJson(orderJson.toString(), typeOrder);
                        TranOrderBean order = orders.get(0);
                        tv_time.setText(DateTool.spliteDate(GlobalConstant.DATE_TIME_PART, order.getPlanStartTime
                                ()));
                        tv_order_context.setText(order.getOrderText());
                    }
                } catch (JSONException e) {
                    CCLog.e(TAG, "加载新的输血信息异常" + e);
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();
                CCLog.i(TAG, "加载新的输血信息失败");
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

        });
    }

    /**
     * 异步加载数据内部类
     */
    public class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!Flag) {
                loadBloodTrans("0");
            } else {
                loadBloodTrans("1");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 通知数据改变了
            mBloodProductsListAdapter.notifyDataSetChanged();
            // 加载完成后停止刷新
            lv_bloodproducts.onRefreshComplete();
        }
    }

    /**
     * 设置选中位置
     */
    public void setPosition(String result) {
        int position = 0;
        for (int i = 0; i < mBloodProductsShow.size(); i++) {
            if (result.equals(mBloodProductsShow.get(i).getPlanOrderNo())) {
                mBloodProductBean2 = mBloodProductsShow.get(i);
                position = i;
                break;
            }
        }
        listView.setSelection(position + 1);
        mBloodProductsListAdapter.notifyChanged(position);
    }

}
