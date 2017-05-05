package com.gentlehealthcare.mobilecare.activity.patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.PatientListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面 病人清单 TODO
 */
@SuppressLint("ValidFragment")
public class MyPatientListFra extends BaseFragment {
    private List<SyncPatientBean> patients;
    private PatientListAdapter adapter = null;
    private View rootView;
    private PullToRefreshListView pullToRefreshListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_patient_list, null);
            pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.lv_patients);
            pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(
                        PullToRefreshBase<ListView> refreshView) {
                    syncPatientTable();
                }
            });
            pullToRefreshListView.setRefreshing(false);

            ListView lvPatients = pullToRefreshListView.getRefreshableView();
            //lvPatients.setSelector(R.color.transparent);
            lvPatients.setOnItemClickListener(mItemClickListener);
            patients = new ArrayList<SyncPatientBean>();
            patients.addAll(UserInfo.getMyPatient());
            lvPatients.setAdapter(adapter = new PatientListAdapter(
                    getActivity(), patients));
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 病人列表点击事件
     */
    OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(getActivity(), PatientAct.class);
            intent.putExtra("patient", patients.get(position - 1));
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
    };

    /**
     * @param
     * @return void
     * @throws
     * @Title: syncPatientTable
     * @Description: 同步我的病人表数据
     */
    private void syncPatientTable() {
        SyncPatientBean bean = new SyncPatientBean();
        String url = UrlConstant.GetSyncPatient() + bean.toString();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setMyPatient(list);
                patients.clear();
                patients.addAll(UserInfo.getMyPatient());
                adapter.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pullToRefreshListView.onRefreshComplete();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_patient_list);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
