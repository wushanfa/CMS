package com.gentlehealthcare.mobilecare.intravenousnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.view.adapter.IntravListAdater;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zyy on 2016/4/18.
 * 类说明：静脉给药待执行界面
 */
public class IntravWaitExcuteFra extends BaseFragment {

    PullToRefreshListView pullToRefreshListView;
    ListView listView;
    IntravListAdater intravListAdater;
    private SyncPatientBean syncPatientBean;
    private List<OrderItemBean> orders = new ArrayList<OrderItemBean>();
    private String eventId = null;

    @Override
    protected void resetLayout(View view) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_intrav_wait_execute, null);
        intial(view);
        loadWaitExcuteOrders("0");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataToAdapter();
    }

    private void setDataToAdapter() {
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadWaitExcuteOrders("0");
                if (!orders.isEmpty()) {
                    intravListAdater.notifyDataSetChanged();
                }
                pullToRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshListView.onRefreshComplete();
                    }
                }, 1000);
            }
        });
    }

    private void intial(View view) {
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_orders);
        listView = pullToRefreshListView.getRefreshableView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toIntravExecuteAct(orders.get(position - 1), eventId);
            }
        });
    }

    private void toIntravExecuteAct(OrderItemBean orderItemBean, String eventId) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalConstant.KEY_ORDERITEMBEAN, orderItemBean);
        bundle.putString(GlobalConstant.KEY_EVENTID, eventId);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), IntravExecuteAct.class);
        startActivity(intent);
    }

    private void receiveData() {
        syncPatientBean = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        eventId = getArguments().getString(GlobalConstant.KEY_EVENTID);
    }

    private void loadWaitExcuteOrders(String performStatus) {
        HttpUtils http = new HttpUtils();
        // String params="?username=" + UserInfo.getUserName() + "&patId=" + syncPatientBean.getPatId()+"&performStatus=" + performStatus + "&templateId=AA-1";
        String params = "?username=01693" + "&patId=" + syncPatientBean.getPatId() + "&performStatus=" + performStatus + "&templateId=AA-1";
        String url = "http://192.168.8.41:8080/mnis/intrav/loadIntravsByPatId" + params;
        // String url=UrlConstant.GetOrders(0)+params;
        CCLog.i(url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                boolean result = false;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    if (result) {
                        jsonArray = jsonObject.getJSONArray("orders");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderItemBean>>() {
                        }.getType();
                        orders.clear();
                        orders = gson.fromJson(jsonArray.toString(), type);
                        intravListAdater = new IntravListAdater(getActivity(), orders);
                        pullToRefreshListView.setAdapter(intravListAdater);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }
}
