package com.gentlehealthcare.mobilecare.activity.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.ExeRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.OrdersAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 医嘱查询 科室病人界面
 */
public class OrdersActivity extends BaseActivity {
    private static final String TAG = "医嘱查询";
    @ViewInject(R.id.lv_orders)
    private ListView listview;
    private OrdersAdapter adapter;
    private List<SyncPatientBean> patients;

    @ViewInject(R.id.tv_head)
    private TextView tv_head;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    @ViewInject(R.id.btn_back)
    private Button btn_back;

    private ProgressDialog progressDialog = null;

    /**
     * 异常字典
     */
    private List<ExeRecordBean> exeList = new ArrayList<ExeRecordBean>();

    /**
     * 评论字典表
     */
    private List<LoadAppraislRecordBean> larList = new ArrayList<LoadAppraislRecordBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ViewUtils.inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载病人..");
        loadExe();
        loadAppraisl();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent intent = new Intent(OrdersActivity.this, DoctorOrdersAct.class);
                intent.putExtra("allPatient", (Serializable) patients);
                intent.putExtra("patient", patients.get(i));
                intent.putExtra("patId", patients.get(i).getPatId());
                intent.putExtra("exception", (Serializable) exeList);
                intent.putExtra("appraise", (Serializable) larList);
                startActivity(intent);
            }
        });
        patients = new ArrayList<SyncPatientBean>();
        patients.addAll(UserInfo.getDeptPatient());
        syncDeptPatientTable();
        adapter = new OrdersAdapter(this, patients);
        listview.setAdapter(adapter);
        tv_parent.setVisibility(View.GONE);
        tv_bed_label.setVisibility(View.GONE);
        tv_head.setText("科室病人");
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_orders);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @OnClick(R.id.btn_back)
    public void setOnClick(View view) {
        finish();
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        CCLog.i(TAG, TAG + "扫描的结果:" + result);
        boolean isGetResult = false;
        int flag = 0;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatCode().equals(result)) {
                isGetResult = true;
                flag = i;
                break;
            }
        }

        if (isGetResult) {
            Intent intent = new Intent(OrdersActivity.this,
                    DoctorOrdersAct.class);
            intent.putExtra("patient", patients.get(flag));
            intent.putExtra("patId", patients.get(flag).getPatId());
            startActivity(intent);
        } else {
            Toast.makeText(getApplication(), "没有找到改病人信息", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 加载异常字典
     */
    private void loadExe() {
        progressDialog.show();
        ExeRecordBean exe = new ExeRecordBean();
        CCLog.i(TAG, "加载异常字典>" + UrlConstant.loadExe() + exe.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadExe() + exe.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ExeRecordBean>>() {
                        }.getType();
                        exeList = gson.fromJson(result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(OrdersActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(OrdersActivity.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 加载评论字典
     */
    private void loadAppraisl() {
        progressDialog.show();
        LoadAppraislRecordBean lar = new LoadAppraislRecordBean();
        CCLog.i(TAG, "加载评论字典>" + UrlConstant.LoadAppriaslRecord() + lar.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadAppriaslRecord() + lar.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LoadAppraislRecordBean>>() {
                        }.getType();
                        larList = gson.fromJson(result, type);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "选中医嘱执行失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(OrdersActivity.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }


    /**
     * 同步部门病人
     */
    public void syncDeptPatientTable() {
        String str = GroupInfoSave.getInstance(OrdersActivity.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString
                (), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CCLog.i(TAG, "医嘱查询数据获取成功");
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
                patients.clear();
                patients.addAll(UserInfo.getDeptPatient());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "医嘱查询数据获取异常");
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(OrdersActivity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }
}
